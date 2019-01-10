package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FsExtends;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TypeActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.WeatherActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 基础信息
 */

public class BasicFragment extends BaseFragment {

    private static final int REQUEST_CODE = 23;

    @BindView(R.id.base_sample_date)
    TextView base_sample_date;
    @BindView(R.id.base_sample_user)
    TextView base_sample_user;
    @BindView(R.id.base_sample_property)
    TextView base_sample_property;
    @BindView(R.id.base_sample_point)
    TextView base_sample_point;
    @BindView(R.id.base_sample_frequency)
    EditText base_sample_frequency;
    @BindView(R.id.base_sample_method)
    TextView base_sample_method;
    @BindView(R.id.base_sample_handle)
    CheckedTextView base_sample_handle;
    @BindView(R.id.water_info_layout)
    View water_info_layout;
    @BindView(R.id.water_arrow)
    TextView water_arrow;
    @BindView(R.id.layout_water_information_container)
    View layout_water_information_container;
    @BindView(R.id.water_temp)
    EditText water_temp;
    @BindView(R.id.water_speed)
    EditText water_speed;
    @BindView(R.id.water_flow)
    EditText water_flow;
    @BindView(R.id.weather_arrow)
    TextView weather_arrow;
    @BindView(R.id.layout_weather_information_container)
    View layout_weather_information_container;
    @BindView(R.id.weather_state)
    TextView weather_state;
    @BindView(R.id.weather_temp)
    EditText weather_temp;
    @BindView(R.id.weather_pressure)
    EditText weather_pressure;
    @BindView(R.id.more_arrow)
    TextView more_arrow;
    @BindView(R.id.layout_more_information)
    View layout_more_information;
    @BindView(R.id.more_name)
    EditText more_name;
    @BindView(R.id.more_address)
    EditText more_address;
    @BindView(R.id.more_device)
    EditText more_device;
    @BindView(R.id.more_waterbody)
    EditText more_waterbody;
    @BindView(R.id.tv_arrow)
    TextView tv_arrow;
    @BindView(R.id.layout_flow_information_container)
    View layout_flow_information_container;
    @BindView(R.id.tv_flow_method)
    EditText tv_flow_method;
    @BindView(R.id.tv_flow_date)
    TextView tv_flow_date;
    @BindView(R.id.base_sample_comment)
    TextView base_sample_comment;
    @BindView(R.id.iv_add_photo)
    ImageView iv_add_photo;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<SamplingFile> sampleFiles = new ArrayList<>();
    private SamplingFileAdapter sampleFileAdapter;
    private FsExtends fsExtends;

    Unbinder unbinder;

    public BasicFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        //设置文件选择
        sampleFiles.add(new SamplingFile());
        if (!CheckUtil.isNull(WastewaterActivity.mSample)) {
            Gson gson=new Gson();
            fsExtends=gson.fromJson(WastewaterActivity.mSample.getPrivateData(),FsExtends.class);
            if (fsExtends==null){
                fsExtends=new FsExtends();
            }
            base_sample_date.setText(WastewaterActivity.mSample.getSamplingTimeBegin());
            base_sample_user.setText(WastewaterActivity.mSample.getSamplingUserName());
            base_sample_property.setText(WastewaterActivity.mSample.getTagName());
            base_sample_point.setText(WastewaterActivity.mSample.getAddressName());
            base_sample_frequency.setText(fsExtends==null?"":fsExtends.getFrequencyNo());
            base_sample_method.setText(WastewaterActivity.mSample.getMethodName());
            boolean sewageDisposal=false;
            if (fsExtends!=null && !TextUtils.isEmpty(fsExtends.getSewageDisposal()) && fsExtends.getSewageDisposal().equals("是")){
                sewageDisposal=true;
            }
            base_sample_handle.setChecked(sewageDisposal);
            base_sample_comment.setText(WastewaterActivity.mSample.getComment());
            //水体信息
            water_temp.setText(fsExtends==null?"":fsExtends.getWaterWD());
            water_speed.setText(fsExtends==null?"":fsExtends.getWaterLS());
            water_flow.setText(fsExtends==null?"":fsExtends.getWaterLL());
            //气象信息
            weather_state.setText(WastewaterActivity.mSample.getWeather());
            weather_temp.setText(WastewaterActivity.mSample.getTemprature());
            weather_pressure.setText(WastewaterActivity.mSample.getPressure());
            //更多信息
            more_name.setText(fsExtends==null?"":fsExtends.getClientName());
            more_address.setText(fsExtends==null?"":fsExtends.getClientAdd());
            more_device.setText(fsExtends==null?"":fsExtends.getHandleDevice());
            more_waterbody.setText(fsExtends==null?"":fsExtends.getReceiving());
            //流转信息
            tv_flow_method.setText(WastewaterActivity.mSample.getTransfer());
            tv_flow_date.setText(WastewaterActivity.mSample.getSendSampTime());
            //图片信息
            if (WastewaterActivity.mSample.getSamplingFiless()!=null && WastewaterActivity.mSample.getSamplingFiless().size()>0){
                sampleFiles.addAll(WastewaterActivity.mSample.getSamplingFiless());
            }

        }else {
            fsExtends=new FsExtends();
        }

        base_sample_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base_sample_handle.setChecked(!base_sample_handle.isChecked());
                if (!base_sample_handle.isChecked()){
                    fsExtends.setSewageDisposal("是");
                }else{
                    fsExtends.setSewageDisposal("否");
                }
            }
        });

        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this.getContext(), 9) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        sampleFileAdapter = new SamplingFileAdapter(sampleFiles, new SamplingFileAdapter.OnSamplingFileListener() {
            @Override
            public void onChoosePhoto() {
                choosePhoto(REQUEST_CODE);
            }

            @Override
            public void onDeletePhoto(int position) {
                sampleFiles.remove(position);
                WastewaterActivity.mSample.setSamplingFiless(sampleFiles);
                sampleFileAdapter.notifyDataSetChanged();
            }
        });
        recyclerview.setAdapter(sampleFileAdapter);

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

            for (String path : paths) {
                SamplingFile samplingFile = new SamplingFile();
                File file = new File(path);
                samplingFile.setId("FS-" + UUID.randomUUID().toString());
                samplingFile.setFilePath(path);
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(WastewaterActivity.mSample.getId());
                sampleFiles.add(samplingFile);
            }

            WastewaterActivity.mSample.setSamplingFiless(sampleFiles);
            sampleFileAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.iv_add_photo, R.id.base_sample_date, R.id.base_sample_user, R.id.base_sample_property, R.id.base_sample_point, R.id.base_sample_method, R.id.water_info_layout, R.id.weather_info_layout, R.id.more_info_layout, R.id.layout_flow_information, R.id.weather_state, R.id.tv_flow_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_photo:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.base_sample_date:
                showDateSelectDialog(base_sample_date);
                break;
            case R.id.base_sample_user:
                showSamplingUser();
                break;
            case R.id.base_sample_property:
                showSamplingType();
                break;
            case R.id.base_sample_point:
                showSamplingPoints();
                break;
            case R.id.base_sample_method:
                showSamplingMethods();
                break;
            case R.id.water_info_layout:
                if (water_arrow.getRotation() == 90f) {
                    water_arrow.animate().rotation(0f);
                    layout_water_information_container.setVisibility(View.GONE);
                } else {
                    water_arrow.animate().rotation(90f);
                    layout_water_information_container.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.weather_info_layout:
                if (weather_arrow.getRotation() == 90f) {
                    weather_arrow.animate().rotation(0f);
                    layout_weather_information_container.setVisibility(View.GONE);
                } else {
                    weather_arrow.animate().rotation(90f);
                    layout_weather_information_container.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.more_info_layout:
                if (more_arrow.getRotation() == 90f) {
                    more_arrow.animate().rotation(0f);
                    layout_more_information.setVisibility(View.GONE);
                } else {
                    more_arrow.animate().rotation(90f);
                    layout_more_information.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_flow_information:
                if (tv_arrow.getRotation() == 90f) {
                    tv_arrow.animate().rotation(0f);
                    layout_flow_information_container.setVisibility(View.GONE);
                } else {
                    tv_arrow.animate().rotation(90f);
                    layout_flow_information_container.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.weather_state:
                showWeatherChoose();
                break;
            case R.id.tv_flow_date:
                showDateSelectDialog(tv_flow_date);
                break;
            default:
                break;
        }

    }

    /**
     * 时间选择器
     * data picker
     */
    private void showDateSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                WastewaterActivity.mSample.setSamplingTimeBegin(DateUtils.getDate(date));
                dateTextView.setText(DateUtils.getDate(date));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 选择采样人
     */
    private void showSamplingUser() {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("projectId", WastewaterActivity.mSample.getProjectId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
                        WastewaterActivity.mSample.setSamplingUserId(data.getStringExtra("UserId"));
                        WastewaterActivity.mSample.setSamplingUserName(data.getStringExtra("UserName"));
                        base_sample_user.setText(WastewaterActivity.mSample.getSamplingUserName());
                    }
                }
            }
        });
    }

    /**
     * 选择样品性质
     */
    private void showSamplingType() {
        Intent intent = new Intent(getContext(), TypeActivity.class);
        intent.putExtra("tagId", WastewaterActivity.mSample.getParentTagId());
        intent.putExtra("title", "请选择样品性质");
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    WastewaterActivity.mSample.setTagId(data.getStringExtra("TagId"));
                    WastewaterActivity.mSample.setTagName(data.getStringExtra("TagName"));
                    base_sample_property.setText(WastewaterActivity.mSample.getTagName());
                }
            }
        });
    }

    /**
     * 选择采样点位
     */
    private void showSamplingPoints() {
        if (CheckUtil.isEmpty(WastewaterActivity.mSample.getTagId())) {
            ArtUtils.makeText(getContext(), "请先选择样品性质");
            return;
        }
        Intent intent = new Intent(getContext(), PointSelectActivity.class);
        intent.putExtra("projectId", WastewaterActivity.mSample.getProjectId());
        intent.putExtra("tagId", WastewaterActivity.mSample.getTagId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    WastewaterActivity.mSample.setAddressName(data.getStringExtra("Address"));
                    WastewaterActivity.mSample.setAddressId(data.getStringExtra("AddressId"));
                    WastewaterActivity.mSample.setAddressNo(data.getStringExtra("AddressNo"));
                    base_sample_point.setText(WastewaterActivity.mSample.getAddressName());
                }
            }
        });
    }

    /**
     * 选择采样方法
     */
    private void showSamplingMethods() {
        if (CheckUtil.isEmpty(WastewaterActivity.mSample.getFormType())) {
            ArtUtils.makeText(getContext(), "请先设置表单类型");
            return;
        }
        Intent intent = new Intent(getContext(), MethodActivity.class);
        intent.putExtra("tagId", WastewaterActivity.mSample.getFormType());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    WastewaterActivity.mSample.setMethodName(data.getStringExtra("MethodName"));
                    WastewaterActivity.mSample.setMethodId(data.getStringExtra("MethodId"));
                    base_sample_method.setText(WastewaterActivity.mSample.getMethodName());
                }
            }
        });
    }

    /**
     * 选择天气
     */
    private void showWeatherChoose() {
        Intent intent = new Intent(getContext(), WeatherActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    WastewaterActivity.mSample.setWeather(data.getStringExtra("weather"));
                    weather_state.setText(WastewaterActivity.mSample.getWeather());
                }
            }
        });
    }

}
