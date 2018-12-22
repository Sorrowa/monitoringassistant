package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodTagRelationDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

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

    Unbinder unbinder;

    private PreciptationPrivateData mPrivateData;

    public BasicFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preciptation_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPrivateData = new PreciptationPrivateData();
        if (!CheckUtil.isNull(PrecipitationActivity.mSampling)) {
            tvSamplingDate.setText(PrecipitationActivity.mSampling.getSamplingTimeBegin());
            tvSamplingUser.setText(PrecipitationActivity.mSampling.getSamplingUserName());
            tvSamplingType.setText(PrecipitationActivity.mSampling.getFormTypeName());
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
            ArtUtils.makeText(getContext(), paths.toString());
        }
    }

    private void choosePhoto(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "cn.cdjzxy.monitoringassistant.android7.fileprovider", "MonitoringAssistant"))
                .maxSelectable(5)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .showSingleMediaType(true)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCode);
    }

    @OnClick({R.id.layout_flow_information, R.id.iv_add_photo, R.id.tv_sampling_user, R.id.tv_sampling_point, R.id.tv_sampling_method, R.id.tv_sampling_device})
    public void onClick(View view) {
        switch (view.getId()) {
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
                showUserSelectDialog();
                break;
            case R.id.tv_sampling_point:
                Intent intent = new Intent(getContext(), PointSelectActivity.class);
                intent.putExtra("tagId", PrecipitationActivity.mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            PrecipitationActivity.mSampling.setAddressName(data.getStringExtra("Address"));
                            PrecipitationActivity.mSampling.setAddressId(data.getStringExtra("AddressId"));
                            tvSamplingPoint.setText(data.getStringExtra("Address"));
                        }
                    }
                });
                break;
            case R.id.tv_sampling_method:
                showMethodSelectDialog();
                break;
            case R.id.tv_sampling_device:
                Intent intent2 = new Intent(getContext(), DeviceActivity.class);
                intent2.putExtra("methodId", PrecipitationActivity.mSampling.getMethodId());
                new AvoidOnResult(getActivity()).startForResult(intent2, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {

                        }
                    }
                });
                break;
        }
    }

    /**
     * 创建人员选择dialog
     */
    private void showUserSelectDialog() {

    }

    /**
     * 创建方法选择dialog
     */
    private void showMethodSelectDialog() {
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(PrecipitationActivity.mSampling.getParentTagId())).unique();
        if (!CheckUtil.isNull(tags)) {
            List<Methods> methods = tags.getMMethods();

            if (!CheckUtil.isEmpty(methods)) {
                PrecipitationActivity.mSampling.setMethodId(methods.get(0).getId());
                PrecipitationActivity.mSampling.setMethodName(methods.get(0).getName());
                tvSamplingMethod.setText(PrecipitationActivity.mSampling.getMethodName());
            }


        }

    }
}
