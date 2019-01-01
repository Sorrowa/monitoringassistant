package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;

import org.simple.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 分瓶信息详情
 */

public class BottleSplitDetailFragment extends BaseFragment {

    @BindView(R.id.sample_project)
    TextView sample_project;
    @BindView(R.id.sample_quantity)
    EditText sample_quantity;
    @BindView(R.id.sample_vessel)
    EditText sample_vessel;
    @BindView(R.id.sample_number)
    EditText sample_number;
    @BindView(R.id.sample_method)
    EditText sample_method;
    @BindView(R.id.sample_date)
    TextView sample_date;
    @BindView(R.id.sample_place)
    TextView sample_place;



    Unbinder unbinder;


    public BottleSplitDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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

    @OnClick({R.id.btn_back,R.id.sample_date,R.id.sample_place})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
                break;
            case R.id.sample_date:
                showDateSelectDialog(sample_date);
                break;
            case R.id.sample_place:
                break;
            default:
                break;

        }
    }

    /**
     * data picker
     */
    private void showDateSelectDialog(TextView dateTextView) {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //PrecipitationActivity.mSampling.setSamplingTimeBegin(DateUtils.getDate(date));
                dateTextView.setText(DateUtils.getDate(date));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }
}
