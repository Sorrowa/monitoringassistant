package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 基本信息
 */

public class BasicFragment extends BaseFragment {

    private static final int REQUEST_CODE = 24;

    @BindView(R.id.tv_sampling_date)
    TextView       tvSamplingDate;
    @BindView(R.id.tv_sampling_user)
    TextView       tvSamplingUser;
    @BindView(R.id.tv_sampling_type)
    TextView       tvSamplingType;
    @BindView(R.id.tv_sampling_point)
    TextView       tvSamplingPoint;
    @BindView(R.id.tv_sampling_no)
    EditText       tvSamplingNo;
    @BindView(R.id.tv_sampling_height)
    EditText       tvSamplingHeight;
    @BindView(R.id.et_sampling_area)
    EditText       etSamplingArea;
    @BindView(R.id.tv_sampling_method)
    TextView       tvSamplingMethod;
    @BindView(R.id.tv_sampling_device)
    TextView       tvSamplingDevice;
    @BindView(R.id.layout_flow_information)
    RelativeLayout layoutFlowInformation;
    @BindView(R.id.tv_flow_method)
    EditText       tvFlowMethod;
    @BindView(R.id.tv_flow_date)
    EditText       tvFlowDate;
    @BindView(R.id.tv_comment)
    EditText       tvComment;
    @BindView(R.id.layout_flow_information_container)
    LinearLayout   layoutFlowInformationContainer;
    @BindView(R.id.iv_add_photo)
    ImageView      ivAddPhoto;
    @BindView(R.id.tv_arrow)
    TextView       tvArrow;
    @BindView(R.id.recyclerview)
    RecyclerView   recyclerview;

    Unbinder unbinder;


    private PreciptationPrivateData mPrivateData;

    private List<SamplingFile> mSamplingFiles = new ArrayList<>();
    private SamplingFileAdapter mSamplingFileAdapter;

    public BasicFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preciptation_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPrivateData = new PreciptationPrivateData();
        mSamplingFiles.add(new SamplingFile());
        if (!CheckUtil.isNull(PrecipitationActivity.mSampling)) {
            tvSamplingDate.setText(PrecipitationActivity.mSampling.getSamplingTimeBegin());
            tvSamplingUser.setText(PrecipitationActivity.mSampling.getSamplingUserName());
            tvSamplingType.setText(PrecipitationActivity.mSampling.getFormTypeName());
            tvSamplingPoint.setText(PrecipitationActivity.mSampling.getAddressName());
            tvSamplingNo.setText(PrecipitationActivity.mSampling.getAddressNo());
            if (!CheckUtil.isEmpty(PrecipitationActivity.mSampling.getPrivateData())) {
                mPrivateData = JSONObject.parseObject(PrecipitationActivity.mSampling.getPrivateData(), PreciptationPrivateData.class);
                if (!CheckUtil.isNull(mPrivateData)) {
                    tvSamplingHeight.setText(mPrivateData.getSampHight());
                    etSamplingArea.setText(mPrivateData.getSampArea());
                }
            }
            tvSamplingMethod.setText(PrecipitationActivity.mSampling.getMethodName());
            tvSamplingDevice.setText(PrecipitationActivity.mSampling.getDeviceName());
            tvFlowMethod.setText(PrecipitationActivity.mSampling.getTransfer());
            tvFlowDate.setText(PrecipitationActivity.mSampling.getSendSampTime());
            tvComment.setText(PrecipitationActivity.mSampling.getComment());
            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
            mSamplingFiles.addAll(samplingFiles);
        }

        //点位编号
        tvSamplingNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PrecipitationActivity.mSampling.setAddressNo(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
            }
        });

        tvSamplingHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrivateData.setSampHight(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
                PrecipitationActivity.mSampling.setPrivateData(JSONObject.toJSONString(mPrivateData));
            }
        });

        etSamplingArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrivateData.setSampArea(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
                PrecipitationActivity.mSampling.setPrivateData(JSONObject.toJSONString(mPrivateData));
            }
        });

        tvFlowMethod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PrecipitationActivity.mSampling.setTransfer(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
            }
        });

        tvFlowDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PrecipitationActivity.mSampling.setSendSampTime(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
            }
        });

        tvComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PrecipitationActivity.mSampling.setComment(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
            }
        });


        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this.getContext(), 9) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        mSamplingFileAdapter = new SamplingFileAdapter(mSamplingFiles, new SamplingFileAdapter.OnSamplingFileListener() {
            @Override
            public void onChoosePhoto() {
                choosePhoto(REQUEST_CODE);
            }

            @Override
            public void onDeletePhoto(int position) {
                mSamplingFiles.remove(position);
                PrecipitationActivity.mSampling.setSamplingFiless(mSamplingFiles);
                mSamplingFileAdapter.notifyDataSetChanged();
            }
        });
        recyclerview.setAdapter(mSamplingFileAdapter);

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            List<String> paths = Matisse.obtainPathResult(data);
            for (String path : paths) {
                SamplingFile samplingFile = new SamplingFile();
                File file = new File(path);
                samplingFile.setId(UUID.randomUUID().toString());
                samplingFile.setFilePath(path);
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(PrecipitationActivity.mSampling.getId());
                mSamplingFiles.add(samplingFile);
            }

            PrecipitationActivity.mSampling.setSamplingFiless(mSamplingFiles);
            mSamplingFileAdapter.notifyDataSetChanged();
        }
    }

    private void choosePhoto(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "cn.cdjzxy.monitoringassistant.android7.fileprovider", "MonitoringAssistant"))
                .maxSelectable(20)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .showSingleMediaType(true)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCode);
    }

    @OnClick({R.id.layout_flow_information, R.id.tv_sampling_date, R.id.iv_add_photo, R.id.tv_sampling_user, R.id.tv_sampling_point, R.id.tv_sampling_method, R.id.tv_sampling_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sampling_date:
                showDateSelectDialog();
                break;
            case R.id.layout_flow_information:
                if (tvArrow.getRotation() == 90f) {
                    tvArrow.setRotation(0f);
                    layoutFlowInformationContainer.setVisibility(View.GONE);
                } else {
                    tvArrow.setRotation(90f);
                    layoutFlowInformationContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_add_photo:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.tv_sampling_user:
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra("projectId", PrecipitationActivity.mSampling.getProjectId());
                new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
                                PrecipitationActivity.mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                                PrecipitationActivity.mSampling.setSamplingUserName(data.getStringExtra("UserName"));
                                tvSamplingUser.setText(PrecipitationActivity.mSampling.getSamplingUserName());
                            }
                        }
                    }
                });
                break;
            case R.id.tv_sampling_point:
                Intent intent1 = new Intent(getContext(), PointSelectActivity.class);
                intent1.putExtra("tagId", PrecipitationActivity.mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            PrecipitationActivity.mSampling.setAddressName(data.getStringExtra("Address"));
                            PrecipitationActivity.mSampling.setAddressId(data.getStringExtra("AddressId"));
                            tvSamplingPoint.setText(PrecipitationActivity.mSampling.getAddressName());
                        }
                    }
                });
                break;
            case R.id.tv_sampling_method:
                Intent intent2 = new Intent(getContext(), MethodActivity.class);
                intent2.putExtra("tagId", PrecipitationActivity.mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent2, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            PrecipitationActivity.mSampling.setMethodName(data.getStringExtra("MethodName"));
                            PrecipitationActivity.mSampling.setMethodId(data.getStringExtra("MethodId"));
                            tvSamplingMethod.setText(PrecipitationActivity.mSampling.getMethodName());
                        }
                    }
                });
                break;
            case R.id.tv_sampling_device:
                Intent intent3 = new Intent(getContext(), DeviceActivity.class);
                intent3.putExtra("methodId", PrecipitationActivity.mSampling.getMethodId());
                new AvoidOnResult(getActivity()).startForResult(intent3, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            PrecipitationActivity.mSampling.setDeviceName(data.getStringExtra("DeviceName"));
                            PrecipitationActivity.mSampling.setDeviceId(data.getStringExtra("DeviceId"));
                            tvSamplingDevice.setText(PrecipitationActivity.mSampling.getDeviceName());
                        }
                    }
                });
                break;
        }
    }

    private void showDateSelectDialog() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                PrecipitationActivity.mSampling.setSamplingTimeBegin(DateUtils.getDate(date));
                tvSamplingDate.setText(PrecipitationActivity.mSampling.getSamplingTimeBegin());
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

}
