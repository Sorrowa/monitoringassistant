package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.wonders.health.lib.base.http.imageloader.ImageConfig;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.ui.SelectedPreviewActivity;

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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.preview.PreviewActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TypeActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.WeatherActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.EnterRelatePointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mSample;


/**
 * 基础信息
 */

public class BasicFragment extends BaseFragment {

    private static final int REQUEST_CODE = 23;

    @BindView(R.id.my_layout_number)
    MyDrawableLinearLayout base_sample_no;//采样单编号
    @BindView(R.id.my_layout_sample_md)
    MyDrawableLinearLayout base_sample_md;//采样单目的
    @BindView(R.id.my_layout_sample_xz)
    MyDrawableLinearLayout base_sample_xz;//采样性质
    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout base_sample_date;//采样日期
    @BindView(R.id.my_layout_sample_user)
    MyDrawableLinearLayout base_sample_user;//采样人

    @BindView(R.id.my_layout_sample_property)
    MyDrawableLinearLayout base_sample_property;//样品性质
    @BindView(R.id.my_layout_sample_point)
    MyDrawableLinearLayout base_sample_point;//采样点位
    @BindView(R.id.my_layout_sample_method)
    MyDrawableLinearLayout base_sample_method;//采样方法
    @BindView(R.id.base_sample_handle)
    CheckedTextView base_sample_handle;
    @BindView(R.id.text_view_sample_handle)
    TextView textViewSampleHandler;
    //@BindView(R.id.water_info_layout)
    //View water_info_layout;
    //@BindView(R.id.water_arrow)
    //TextView water_arrow;
    //@BindView(R.id.layout_water_information_container)
    //View layout_water_information_container;
    //@BindView(R.id.water_temp)
    //EditText water_temp;
    //@BindView(R.id.water_speed)
    //EditText water_speed;
    //@BindView(R.id.water_flow)
    //EditText water_flow;
    //天气
    @BindView(R.id.text_view_weather_arrow)
    TextView text_view_weather_arrow;
    @BindView(R.id.weather_arrow)
    TextView weather_arrow;
    @BindView(R.id.layout_weather_information_container)
    View layout_weather_information_container;
    @BindView(R.id.my_layout_weather_state)
    MyDrawableLinearLayout weather_state;
    @BindView(R.id.my_layout_weather_temp)
    MyDrawableLinearLayout weather_temp;
    @BindView(R.id.my_layout_weather_pressure)
    MyDrawableLinearLayout weather_pressure;
    //更多信息
    @BindView(R.id.text_view_more_arrow)
    TextView text_view_more_arrow;
    @BindView(R.id.more_arrow)
    TextView more_arrow;
    @BindView(R.id.layout_more_information)
    View layout_more_information;
    @BindView(R.id.my_layout_more_name)
    MyDrawableLinearLayout more_name;//企业名称
    @BindView(R.id.my_layout_more_address)
    MyDrawableLinearLayout more_address;//企业地址
    @BindView(R.id.my_layout_more_device)
    MyDrawableLinearLayout more_device;//处理设施
    @BindView(R.id.my_layout_more_waterbody)
    MyDrawableLinearLayout more_waterbody;//受纳水体
    @BindView(R.id.my_layout_more_build_date)
    MyDrawableLinearLayout more_build_date;//建设时间
    @BindView(R.id.text_view_more_gw)
    TextView text_view_more_gw;
    @BindView(R.id.more_gw)
    CheckedTextView more_gw;//是否进入管网
    //流转信息
    @BindView(R.id.text_view_tv_arrow)
    TextView text_view_tv_arrow;
    @BindView(R.id.tv_arrow)
    TextView tv_arrow;
    @BindView(R.id.layout_flow_information_container)
    View layout_flow_information_container;
    @BindView(R.id.my_layout_flow_method)
    MyDrawableLinearLayout tv_flow_method;//运输方式
    @BindView(R.id.my_layout_flow_date)
    MyDrawableLinearLayout tv_flow_date;//送样时间
    @BindView(R.id.my_layout_receive_date)
    MyDrawableLinearLayout tv_receive_date;//收样时间

    @BindView(R.id.base_sample_comment)
    TextView base_sample_comment;
    @BindView(R.id.iv_add_photo)
    ImageView iv_add_photo;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    private static final int PirView_request_Code = 20002;

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
        if (!CheckUtil.isNull(mSample)) {
            Gson gson = new Gson();
            fsExtends = gson.fromJson(mSample.getPrivateData(), FsExtends.class);
            if (fsExtends == null) {
                fsExtends = new FsExtends();
            }
            base_sample_no.setRightTextStr(mSample.getSamplingNo());
            base_sample_md.setRightTextStr(mProject.getName());//监测目的
            //base_sample_xz.setText(WastewaterActivity.mSample.getMontype());
            base_sample_xz.setRightTextStr(mProject.getType());

            base_sample_date.setRightTextStr(DateUtils.strGetDate(mSample.getSamplingTimeBegin()));
            base_sample_user.setRightTextStr(mSample.getSamplingUserName());
            base_sample_property.setRightTextStr(mSample.getTagName());
            base_sample_point.setRightTextStr(mSample.getAddressName());
            base_sample_method.setRightTextStr(mSample.getMethodName());
            boolean sewageDisposal = false;
            if (fsExtends != null && !TextUtils.isEmpty(fsExtends.getSewageDisposal()) && fsExtends.getSewageDisposal().equals("是")) {
                sewageDisposal = true;
                fsExtends.setSewageDisposal("是");
            } else {
                fsExtends.setSewageDisposal("否");
            }
            base_sample_handle.setChecked(sewageDisposal);
            base_sample_comment.setText(mSample.getComment());
            setViewStyleDrawable(sewageDisposal, textViewSampleHandler);
            /*
            //水体信息
            water_temp.setText(fsExtends == null ? "" : fsExtends.getWaterWD());
            water_speed.setText(fsExtends == null ? "" : fsExtends.getWaterLS());
            water_flow.setText(fsExtends == null ? "" : fsExtends.getWaterLL());
            */

            //气象信息
            //只要填写填了一个就设置选中颜色
            setViewStyleDrawable(!CheckUtil.isEmpty(mSample.getWeather())
                            || !CheckUtil.isEmpty(mSample.getTemprature())
                            || !CheckUtil.isEmpty(mSample.getPressure()),
                    text_view_weather_arrow);
            weather_state.setRightTextStr(mSample.getWeather());
            weather_temp.setEditTextStr(mSample.getTemprature());
            weather_pressure.setEditTextStr(mSample.getPressure());
            setViewStyle(false, weather_arrow);
            //更多信息
            boolean gw = false;
            if (fsExtends != null && !TextUtils.isEmpty(fsExtends.getAccessPipeNetwork()) && fsExtends.getAccessPipeNetwork().equals("是")) {
                gw = true;
                fsExtends.setAccessPipeNetwork("是");
            } else {
                fsExtends.setAccessPipeNetwork("否");
            }
            more_gw.setChecked(gw);
            setViewStyleDrawable(gw, text_view_more_gw);
            setViewStyleDrawable(gw
                            || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getClientName())
                            || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getClientAdd())
                            || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getHandleDevice())
                            || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getReceiving())
                            || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getBuildTime())
                    , text_view_more_arrow);
//            more_name.setEditTextStr(fsExtends == null ? "" : fsExtends.getClientName());
            //企业名称
            more_name.getEditText().setClickable(false);
            more_name.getEditText().setFocusable(false);
            more_name.getEditText().setHint("");
            more_name.getEditText().setText(mProject.getClientName());
            more_address.setEditTextStr(fsExtends == null ? "" : fsExtends.getClientAdd());
            more_device.setEditTextStr(fsExtends == null ? "" : fsExtends.getHandleDevice());
            more_waterbody.setEditTextStr(fsExtends == null ? "" : fsExtends.getReceiving());
            more_build_date.setRightTextStr(fsExtends == null ? "" : fsExtends.getBuildTime());
            setViewStyle(false, more_arrow);

            //流转信息
            setViewStyleDrawable(!CheckUtil.isEmpty(mSample.getTransfer())
                            || !CheckUtil.isEmpty(mSample.getSendSampTime())
                            || !CheckUtil.isEmpty(mSample.getReciveTime()),
                    text_view_tv_arrow);
            tv_flow_method.setEditTextStr(mSample.getTransfer());
            tv_flow_date.setRightTextStr(DateUtils.strGetTimeShort(mSample.getSendSampTime()));
            tv_receive_date.setRightTextStr(DateUtils.strGetTimeShort(mSample.getSendSampTime()));
            setViewStyle(false, tv_arrow);
            //图片信息
            if (mSample.getSamplingFiless() != null && mSample.getSamplingFiless().size() > 0) {
                sampleFiles.addAll(mSample.getSamplingFiless());
            }
        } else {
            fsExtends = new FsExtends();
        }

        base_sample_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base_sample_handle.setChecked(!base_sample_handle.isChecked());
                setViewStyleDrawable(base_sample_handle.isChecked(), textViewSampleHandler);
                if (base_sample_handle.isChecked()) {
                    fsExtends.setSewageDisposal("是");
                } else {
                    fsExtends.setSewageDisposal("否");
                }
            }
        });

        more_gw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_gw.setChecked(!more_gw.isChecked());
                setViewStyleDrawable(more_gw.isChecked(), text_view_more_gw);
                if (more_gw.isChecked()) {
                    fsExtends.setAccessPipeNetwork("是");
                } else {
                    fsExtends.setAccessPipeNetwork("否");
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
                if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
                    showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
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
                SamplingFile samplingFile = sampleFiles.remove(position);
                //记录删除的文件，提交给服务端
                if (samplingFile != null) {
                    mSample.addDeleteFiles(samplingFile.getId());
                }
                mSample.setSamplingFiless(sampleFiles);
                sampleFileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPirViewPhoto(int position) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                Bundle bundle = new Bundle();
                List<SamplingFile> list = new ArrayList<>();
                list = sampleFiles;
                list.remove(0);
                bundle.putParcelableArrayList(PreviewActivity.PREVIEW_PHOTOS, (ArrayList<SamplingFile>) list);
                intent.putExtra(PreviewActivity.PREVIEW_PHOTOS, bundle);
                intent.putExtra(PreviewActivity.POSITION, position);
                getActivity().startActivityForResult(intent, PirView_request_Code);
            }
        });
        recyclerview.setAdapter(sampleFileAdapter);

    }

    /**
     * 给view设置选中样式
     *
     * @param isSelect 选中还是未选中
     * @param view     view
     */
    private void setViewStyleDrawable(boolean isSelect, TextView view) {
        Drawable drawable;
        if (isSelect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_yes_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_yes_data);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_no_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_no_data);
            }
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
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
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {//添加图片
            List<String> paths = Matisse.obtainPathResult(data);
            ArtUtils.makeText(getContext(), paths.toString());
            for (String path : paths) {
                SamplingFile samplingFile = new SamplingFile();
                File file = new File(path);
                samplingFile.setLocalId(UUID.randomUUID().toString());
                samplingFile.setId("");
                samplingFile.setFilePath(path);
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(mSample.getId());
                samplingFile.setUpdateTime(DateUtils.getTime(new Date().getTime()));
                sampleFiles.add(samplingFile);
            }
            mSample.setSamplingFiless(sampleFiles);
            sampleFileAdapter.notifyDataSetChanged();
        } else if (requestCode == PirView_request_Code
                && resultCode == PreviewActivity.BACK_RESULT_CODE) {//预览图片
            Bundle bundle = data.getBundleExtra(PreviewActivity.PREVIEW_PHOTOS);
            List<SamplingFile> list = bundle.getParcelableArrayList(PreviewActivity.PREVIEW_PHOTOS);
            mSample.setSamplingFiless(list);
            SamplingFile file = sampleFiles.get(0);
            sampleFiles.clear();
            sampleFiles.add(file);
            sampleFiles.addAll(list);
            sampleFileAdapter.notifyDataSetChanged();
        }
    }

    private void choosePhoto(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,
                        "cn.cdjzxy.monitoringassistant.android7.fileprovider",
                        "MonitoringAssistant"))
                .maxSelectable(5)
                //.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .showSingleMediaType(true)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCode);
    }

    @OnClick({R.id.iv_add_photo, R.id.my_layout_sample_date, R.id.my_layout_sample_user, R.id.my_layout_sample_property,
            R.id.my_layout_sample_point, R.id.my_layout_sample_method, R.id.weather_info_layout,
            R.id.more_info_layout, R.id.layout_flow_information, R.id.my_layout_weather_state, R.id.my_layout_flow_date, R.id.my_layout_receive_date, R.id.my_layout_more_build_date})
    public void onClick(View view) {
        hideSoftInput();
        if (!WastewaterActivity.isNewCreate && !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.iv_add_photo:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.my_layout_sample_date:
                showDateSelectDialog(base_sample_date.getRightTextView());
                break;
            case R.id.my_layout_sample_user:
                showSamplingUser();
                break;
            case R.id.my_layout_sample_property:
                showSamplingType();
                break;
            case R.id.my_layout_sample_point:
                showSamplingPoints();
                break;
            case R.id.my_layout_sample_method:
                showSamplingMethods();
                break;
//            case R.id.water_info_layout:
//                setArrowAnimate(water_arrow, layout_water_information_container);
//                break;
            case R.id.weather_info_layout:
                setArrowAnimate(weather_arrow, layout_weather_information_container);
                break;
            case R.id.more_info_layout:
                setArrowAnimate(more_arrow, layout_more_information);
                break;
            case R.id.layout_flow_information:
                setArrowAnimate(tv_arrow, layout_flow_information_container);
                break;
            case R.id.my_layout_weather_state:
                showWeatherChoose();
                break;
            case R.id.my_layout_flow_date:
                showSendDateSelectDialog(tv_flow_date.getRightTextView());
                break;
//            case R.id.my_layout_receive_date:
//                //showReceiveDateSelectDialog(tv_receive_date);
//                break;
            case R.id.my_layout_more_build_date:
                showBuildTimeSelectDialog(more_build_date.getRightTextView());
                break;
            default:
                break;
        }

    }

    /**
     * 时间选择器(采样日期)
     * data picker
     */
    private void showDateSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String date1 = DateUtils.getDate(date);
                mSample.setSamplingTimeBegin(date1);
                dateTextView.setText(date1);
                mSample.setSamplingNo(SamplingUtil.createSamplingNo(date1));
                base_sample_no.setRightTextStr(mSample.getSamplingNo());
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 设置送样时间
     *
     * @param dateTextView
     */
    private void showSendDateSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mSample.setSendSampTime(DateUtils.getTime(date.getTime()));
                dateTextView.setText(DateUtils.getTimeShort(date.getTime()));
                setViewStyleDrawable(!CheckUtil.isEmpty(mSample.getTransfer())
                                || !CheckUtil.isEmpty(mSample.getSendSampTime())
                                || !CheckUtil.isEmpty(mSample.getReciveTime()),
                        text_view_tv_arrow);
            }
        }).setType(new boolean[]{true, true, true, true, true, false}).isCyclic(true).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 选择收样时间
     *
     * @param dateTextView
     */
    private void showReceiveDateSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mSample.setReciveTime(DateUtils.getTime(date.getTime()));
                dateTextView.setText(DateUtils.getTime(date.getTime()));
                setViewStyleDrawable(!CheckUtil.isEmpty(mSample.getTransfer())
                                || !CheckUtil.isEmpty(mSample.getSendSampTime())
                                || !CheckUtil.isEmpty(mSample.getReciveTime()),
                        text_view_tv_arrow);
            }
        }).isCyclic(true).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 建设时间
     *
     * @param dateTextView
     */
    private void showBuildTimeSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                fsExtends.setBuildTime(DateUtils.getYears(date));
                dateTextView.setText(DateUtils.getYears(date));
                setViewStyleDrawable(!CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getClientName())
                                || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getClientAdd())
                                || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getHandleDevice())
                                || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getReceiving())
                                || !CheckUtil.isEmpty(fsExtends == null ? "" : fsExtends.getBuildTime())
                        , text_view_more_arrow);
            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 选择采样人
     */
    private void showSamplingUser() {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("projectId", mSample.getProjectId());
        intent.putExtra("selectUserIds", mSample.getSamplingUserId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
                        mSample.setSamplingUserId(data.getStringExtra("UserId"));
                        mSample.setSamplingUserName(data.getStringExtra("UserName"));
                        base_sample_user.setRightTextStr(mSample.getSamplingUserName());
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
        intent.putExtra("tagId", mSample.getParentTagId());
        intent.putExtra("title", "请选择样品性质");
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setTagId(data.getStringExtra("TagId"));
                    mSample.setTagName(data.getStringExtra("TagName"));
                    base_sample_property.setRightTextStr(mSample.getTagName());
                }
            }
        });
    }

    /**
     * 选择采样点位
     */
    private void showSamplingPoints() {
        if (CheckUtil.isEmpty(mSample.getTagId())) {
            ArtUtils.makeText(getContext(), "请先选择样品性质");
            return;
        }
        Intent intent = new Intent();
        if (mSample.getMontype() == 3) {//环境质量
            intent.setClass(getContext(), PointSelectActivity.class);
            intent.putExtra("projectId", mSample.getProjectId());
            intent.putExtra("tagId", mSample.getTagId());
        } else {//污染源
            intent.setClass(getContext(), EnterRelatePointSelectActivity.class);
            intent.putExtra("projectId", mSample.getProjectId());
            intent.putExtra("rcvId", mProject.getRcvId());
            intent.putExtra("tagId", mSample.getTagId());
        }

        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setAddressName(data.getStringExtra("Address"));
                    mSample.setAddressId(data.getStringExtra("AddressId"));
                    mSample.setAddressNo(data.getStringExtra("AddressNo"));
                    base_sample_point.setRightTextStr(mSample.getAddressName());
                }
            }
        });
    }

    /**
     * 选择采样方法
     */
    private void showSamplingMethods() {
        if (CheckUtil.isEmpty(mSample.getFormType())) {
            ArtUtils.makeText(getContext(), "请先设置表单类型");
            return;
        }
        Intent intent = new Intent(getContext(), MethodActivity.class);
        intent.putExtra("tagId", mSample.getFormType());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setMethodName(data.getStringExtra("MethodName"));
                    mSample.setMethodId(data.getStringExtra("MethodId"));
                    base_sample_method.setRightTextStr(mSample.getMethodName());
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
                    mSample.setWeather(data.getStringExtra("weather"));
                    weather_state.setRightTextStr(mSample.getWeather());
                    setViewStyleDrawable(!CheckUtil.isEmpty(mSample.getWeather())
                            || !CheckUtil.isEmpty(mSample.getTemprature())
                            || !CheckUtil.isEmpty(mSample.getPressure()), text_view_weather_arrow);

                }
            }
        });
    }

    /**
     * 设置箭头的动画效果
     *
     * @param arrow
     * @param container
     */
    private void setArrowAnimate(TextView arrow, View container) {
        if (arrow.getRotation() == 90f) {
            arrow.animate().rotation(0f);
            setViewStyle(false, arrow);
            container.setVisibility(View.GONE);
        } else {
            arrow.animate().rotation(90f);
            setViewStyle(true, arrow);
            container.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 保存扩展信息
     *
     * @return
     */
    private void saveFsExtends() {
        if (fsExtends != null && mSample != null) {
            /*
            fsExtends.setWaterWD(water_temp.getText().toString());
            fsExtends.setWaterLL(water_flow.getText().toString());
            fsExtends.setWaterLS(water_speed.getText().toString());
            */
            fsExtends.setClientName(more_name.getEditTextStr().toString());
            fsExtends.setClientAdd(more_address.getEditTextStr().toString());
            fsExtends.setHandleDevice(more_device.getEditTextStr().toString());
            fsExtends.setReceiving(more_waterbody.getEditTextStr().toString());

            Gson gson = new Gson();
            String jsonStr = gson.toJson(fsExtends);
            mSample.setPrivateData(jsonStr);
        }
    }

    /**
     * 保存设置采样基本信息
     */
    public void saveSamplingData() {
        mSample.setWeather(weather_state.getRightTextViewStr());
        mSample.setTemprature(weather_temp.getEditTextStr());
        mSample.setPressure(weather_pressure.getEditTextStr());
        mSample.setTransfer(tv_flow_method.getEditTextStr());
        mSample.setSendSampTime(tv_flow_date.getRightTextViewStr());
        mSample.setComment(base_sample_comment.getText().toString());
        saveFsExtends();
    }

}
