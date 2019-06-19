package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.preview.PreviewActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TypeActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;


import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity.mSampling;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mSample;
import static cn.cdjzxy.monitoringassistant.utils.SamplingUtil.isMySampling;

/**
 * 基本信息
 */

public class BasicFragment extends BaseFragment {


    private static final int REQUEST_CODE = 24;

    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout tvSamplingDate;
    @BindView(R.id.my_layout_sample_user)
    MyDrawableLinearLayout tvSamplingUser;
    @BindView(R.id.my_layout_sample_type)
    MyDrawableLinearLayout tvSamplingType;
    @BindView(R.id.my_layout_sample_point)
    MyDrawableLinearLayout tvSamplingPoint;
    @BindView(R.id.my_layout_sample_no)
    MyDrawableLinearLayout tvSamplingNo;
    @BindView(R.id.my_layout_monitor_name)
    MyDrawableLinearLayout edMonitorName;//监测项目
    @BindView(R.id.my_layout_monitor_height)
    MyDrawableLinearLayout tvSamplingHeight;
    @BindView(R.id.my_layout_monitor_area)
    MyDrawableLinearLayout etSamplingArea;
    @BindView(R.id.my_layout_monitor_method)
    MyDrawableLinearLayout tvSamplingMethod;
    @BindView(R.id.my_layout_monitor_device)
    MyDrawableLinearLayout tvSamplingDevice;
    @BindView(R.id.layout_flow_information)
    RelativeLayout layoutFlowInformation;

    @BindView(R.id.tv_comment)
    EditText tvComment;

    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    //流转信息
    @BindView(R.id.tv_arrow)
    TextView tvArrow;
    @BindView(R.id.layout_flow_information_container)
    LinearLayout layoutFlowInformationContainer;
    @BindView(R.id.my_layout_flow_method)
    MyDrawableLinearLayout tvFlowMethod;
    @BindView(R.id.my_layout_flow_date)
    MyDrawableLinearLayout tvFlowDate;
    @BindView(R.id.my_layout_receive_date)
    MyDrawableLinearLayout tv_receive_date;

    Unbinder unbinder;

    private static final int request_Code = 20001;


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
        if (!CheckUtil.isNull(mSampling)) {
            tvSamplingDate.setRightTextStr(DateUtils.strGetDate(mSampling.getSamplingTimeBegin()));
            tvSamplingUser.setRightTextStr(mSampling.getSamplingUserName());
            tvSamplingType.setRightTextStr(mSampling.getTagName());
            tvSamplingPoint.setRightTextStr(mSampling.getAddressName());
            tvSamplingNo.setRightTextStr(mSampling.getAddressNo());
            edMonitorName.setRightTextStr(mSampling.getMonitemName());
            if (!CheckUtil.isEmpty(mSampling.getPrivateData())) {
                mPrivateData = JSONObject.parseObject(mSampling.getPrivateData(), PreciptationPrivateData.class);
                if (!CheckUtil.isNull(mPrivateData)) {
                    tvSamplingHeight.setEditTextStr(mPrivateData.getSampHight());
                    etSamplingArea.setEditTextStr(mPrivateData.getSampArea());
                }
            }
            tvSamplingMethod.setRightTextStr(mSampling.getMethodName());
            tvSamplingDevice.setRightTextStr(mSampling.getDeviceName());
            tvFlowMethod.setEditTextStr(mSampling.getTransfer());
            tvFlowDate.setRightTextStr(DateUtils.strGetTimeShort(mSampling.getSendSampTime()));
            tv_receive_date.setRightTextStr(DateUtils.strGetTimeShort(mSampling.getReciveTime()));
            tvComment.setText(mSampling.getComment());
            mSamplingFiles.addAll(mSampling.getSamplingFiless());

            tvSamplingDate.setEnabled(mSampling.getIsCanEdit());
            tvSamplingUser.setEnabled(mSampling.getIsCanEdit());
            tvSamplingType.setEnabled(mSampling.getIsCanEdit());
            tvSamplingPoint.setEnabled(mSampling.getIsCanEdit());
            tvSamplingNo.setEnabled(mSampling.getIsCanEdit());
            tvSamplingHeight.setEnabled(mSampling.getIsCanEdit());
            etSamplingArea.setEnabled(mSampling.getIsCanEdit());
            tvSamplingMethod.setEnabled(mSampling.getIsCanEdit());
            tvSamplingDevice.setEnabled(mSampling.getIsCanEdit());
            tvFlowMethod.setEnabled(mSampling.getIsCanEdit());
            tvFlowDate.setEnabled(mSampling.getIsCanEdit());
            tvComment.setEnabled(mSampling.getIsCanEdit());
        }

        //点位编号
        tvSamplingNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                mSampling.setAddressNo(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
                //点位编号修改后依据新的点位编号重新生成采样单编号
                String snPointPosition = "采样点位编号未填写";
                if (!CheckUtil.isEmpty(s.toString())) {
                    snPointPosition = s.toString();
                }

                String snUserId = UserInfoHelper.get().getUser().getIntId() + "";
                for (SamplingDetail samplingDetail : mSampling.getSamplingDetailResults()) {
                    String[] sampingCode = samplingDetail.getSampingCode().split("-");
                    samplingDetail.setSampingCode(sampingCode[0] + "-" + snPointPosition + snUserId + "-" + sampingCode[2]);
                }
            }
        });

        tvSamplingHeight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrivateData.setSampHight(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
                mSampling.setPrivateData(JSONObject.toJSONString(mPrivateData));
            }
        });

        etSamplingArea.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                mPrivateData.setSampArea(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
                mSampling.setPrivateData(JSONObject.toJSONString(mPrivateData));
            }
        });

        tvFlowMethod.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                mSampling.setTransfer(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
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
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                mSampling.setComment(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
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
                if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
                    showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
                    return;
                }
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                choosePhoto(REQUEST_CODE);
            }

            @Override
            public void onDeletePhoto(int position) {
                if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
                    showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
                    return;
                }
                if (mSampling == null && !isMySampling(mSampling.getSubmitId())) {
                    ArtUtils.makeText(getContext(), "编辑无权限,他人表单只能查看");
                    return;
                }
                SamplingFile samplingFile = mSamplingFiles.remove(position);

                //记录删除的文件，提交给服务端
                if (samplingFile != null) {
                    mSampling.addDeleteFiles(samplingFile.getId());
                }

                mSampling.setSamplingFiless(mSamplingFiles);
                mSamplingFileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPirViewPhoto(int position) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                Bundle bundle = new Bundle();
                List<SamplingFile> list = new ArrayList<>();
                list.addAll(mSamplingFiles);
                list.remove(0);
                bundle.putParcelableArrayList(PreviewActivity.PREVIEW_PHOTOS
                        , (ArrayList<SamplingFile>) list);
                intent.putExtra(PreviewActivity.PREVIEW_PHOTOS, bundle);
                intent.putExtra(PreviewActivity.POSITION, position);
                getActivity().startActivityForResult(intent, request_Code);
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
        if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            return;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            List<String> paths = Matisse.obtainPathResult(data);
            for (String path : paths) {
                SamplingFile samplingFile = new SamplingFile();
                File file = new File(path);
                samplingFile.setLocalId("LC-" + UUID.randomUUID().toString());
                samplingFile.setId("");
                samplingFile.setFilePath(path);
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(mSampling.getId());
                samplingFile.setUpdateTime(DateUtils.getTime(new Date().getTime()));
                mSamplingFiles.add(samplingFile);
            }
            mSampling.setSamplingFiless(mSamplingFiles);
            mSamplingFileAdapter.notifyDataSetChanged();
        } else if (requestCode == request_Code && resultCode == PreviewActivity.BACK_RESULT_CODE) {
            Bundle bundle = data.getBundleExtra(PreviewActivity.PREVIEW_PHOTOS);
            List<SamplingFile> list = bundle.getParcelableArrayList(PreviewActivity.PREVIEW_PHOTOS);
            SamplingFile file = mSamplingFiles.get(0);
            mSamplingFiles.clear();
            mSamplingFiles.add(file);
            mSamplingFiles.addAll(list);
            mSampling.setSamplingFiless(mSamplingFiles);
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


    @OnClick({R.id.layout_flow_information, R.id.my_layout_sample_type, R.id.my_layout_flow_date,
            R.id.my_layout_sample_date, R.id.iv_add_photo, R.id.my_layout_sample_user,
            R.id.my_layout_sample_point, R.id.my_layout_monitor_method, R.id.my_layout_monitor_device})
    public void onClick(View view) {
        hideSoftInput();
        if (!PrecipitationActivity.isNewCreate &&
                !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.my_layout_sample_date:
                showDateSelectDialog();
                break;
            case R.id.my_layout_flow_date:
                showDateSelectDialog1();
                break;
            case R.id.layout_flow_information:
                if (tvArrow.getRotation() == 90f) {
                    tvArrow.setRotation(0f);
                    setViewStyle(false,tvArrow);
                    layoutFlowInformationContainer.setVisibility(View.GONE);
                } else {
                    tvArrow.setRotation(90f);
                    setViewStyle(true,tvArrow);
                    layoutFlowInformationContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_add_photo:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.my_layout_sample_type:
                Intent intent = new Intent(getContext(), TypeActivity.class);
                intent.putExtra("tagId", mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mSampling.setTagId(data.getStringExtra("TagId"));
                            mSampling.setTagName(data.getStringExtra("TagName"));
                            tvSamplingType.setRightTextStr(mSampling.getTagName());
                            setMonItemNameData();
                        }
                    }
                });
                break;
            case R.id.my_layout_sample_user:
                Intent intent1 = new Intent(getContext(), UserActivity.class);
                intent1.putExtra("projectId", mSampling.getProjectId());
                intent1.putExtra("selectUserIds", mSampling.getSamplingUserId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                            mSampling.setSamplingUserName(data.getStringExtra("UserName"));
                            tvSamplingUser.setRightTextStr(mSampling.getSamplingUserName());
                        }
                    }
                });
                break;
            case R.id.my_layout_sample_point:
                if (CheckUtil.isEmpty(mSampling.getTagId())) {
                    ArtUtils.makeText(getContext(), "请先选择降水类型");
                    return;
                }
                Intent intent2 = new Intent(getContext(), PointSelectActivity.class);
                intent2.putExtra("projectId", mSampling.getProjectId());
                intent2.putExtra("tagId", mSampling.getTagId());
                new AvoidOnResult(getActivity()).startForResult(intent2, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mSampling.setAddressName(data.getStringExtra("Address"));
                            mSampling.setAddressId(data.getStringExtra("AddressId"));
                            mSampling.setAddressNo(data.getStringExtra("AddressNo"));
                            tvSamplingPoint.setRightTextStr(mSampling.getAddressName());
                            tvSamplingNo.setRightTextStr(mSampling.getAddressNo());
                            setMonItemNameData();
                        }
                    }
                });
                break;
            case R.id.my_layout_monitor_method:
                if (CheckUtil.isEmpty(mSampling.getParentTagId())) {
                    ArtUtils.makeText(getContext(), "请先选择降水类型");
                    return;
                }
                Intent intent3 = new Intent(getContext(), MethodActivity.class);
                intent3.putExtra("tagId", mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent3, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mSampling.setMethodName(data.getStringExtra("MethodName"));
                            mSampling.setMethodId(data.getStringExtra("MethodId"));
                            tvSamplingMethod.setRightTextStr(mSampling.getMethodName());
                        }
                    }
                });
                break;
            case R.id.my_layout_monitor_device:
                if (CheckUtil.isEmpty(mSampling.getMethodId())) {
                    ArtUtils.makeText(getContext(), "请先选择方法");
                    return;
                }
                Intent intent4 = new Intent(getContext(), DeviceActivity.class);
                intent4.putExtra("methodId", mSampling.getMethodId());
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            String deviceId = data.getStringExtra("DeviceId");
                            String deviceName = data.getStringExtra("DeviceName");
                            String deviceCode = data.getStringExtra("DeviceCode");
                            String sourceWay = data.getStringExtra("SourceWay");
                            String expireDate = data.getStringExtra("ExpireDate");
                            String deviceText;
                            if (expireDate != null && !expireDate.equals("")) {
                                String[] s = expireDate.split(" ");
                                deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, s[0]);
                            } else {
                                deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, expireDate);
                            }
                            tvSamplingDevice.setRightTextStr(deviceText);
                            mSampling.setDeviceId(deviceId);
                            mSampling.setDeviceName(deviceText);
                        }
                    }
                });
                break;
        }
    }
    /**
     * 给view设置选中样式
     *
     * @param isSelect 选中还是未选中
     * @param view     view
     */
    private void setViewStyle(boolean isSelect, TextView view) {
        Drawable drawable;
        if (isSelect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.ic_has_open);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.ic_has_open);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.ic_has_next);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.ic_has_next);
            }
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }
    /**
     * 设置监测项目
     */
    public void setMonItemNameData() {
        if (mSampling.getAddressId() == null || mSampling.getAddressId().equals("")) return;
        if (mSampling.getTagId() == null || mSampling.getTagId().equals("")) return;
        List<ProjectDetial> projectDetialList = DbHelpUtils.getProjectDetialList(mProject.getId()
                , mSampling.getAddressId(), mSampling.getTagId());
        if (CheckUtil.isEmpty(projectDetialList)) {
            mSampling.setMonitemName("");
            edMonitorName.setRightTextStr(mSampling.getMonitemName());
            return;
        }
        StringBuilder stringBuilderName = new StringBuilder();
        StringBuilder stringBuilderId = new StringBuilder();

        for (ProjectDetial content : projectDetialList) {
            stringBuilderName.append(content.getMonItemName()).append(",");
            stringBuilderId.append(content.getMonItemId()).append(",");
        }
        if (stringBuilderName.lastIndexOf(",") > 0) {
            stringBuilderName.deleteCharAt(stringBuilderName.lastIndexOf(","));
        }
        if (stringBuilderId.lastIndexOf(",") > 0)
            stringBuilderId.deleteCharAt(stringBuilderId.lastIndexOf(","));
        mSampling.setMonitemName(stringBuilderName.toString());
        mSampling.setMonitemId(stringBuilderId.toString());
        edMonitorName.setRightTextStr(mSampling.getMonitemName());
    }


    private void showDateSelectDialog() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateStr = DateUtils.getDate(date);
                mSampling.setSamplingTimeBegin(dateStr);
                tvSamplingDate.setRightTextStr(mSampling.getSamplingTimeBegin());
                mSampling.setSamplingNo(SamplingUtil.createSamplingNo(dateStr));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void showDateSelectDialog1() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mSampling.setSendSampTime(DateUtils.getTimeShort(date.getTime()));
                tvFlowDate.setRightTextStr(mSampling.getSendSampTime());
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .isCyclic(true)
                .build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }


}
