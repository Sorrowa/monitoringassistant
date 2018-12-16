package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;

/**
 * 任务
 */

public class TaskFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_month)
    TextView  btnMonth;
    @BindView(R.id.btn_week)
    TextView  btnWeek;
    @BindView(R.id.btn_day)
    TextView  btnDay;
    @BindView(R.id.bar_chart)
    BarChart  barChart;
    @BindView(R.id.pie_chart)
    PieChart  pieChart;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.pie_chart1)
    PieChart  pieChart1;
    @BindView(R.id.btn_date)
    TextView  btnDate;

    public TaskFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initSamplingView();
        initPendingSamplingView();
        initReceivedView();
        initWaitReceivedView();
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


    /**
     * 已采样
     */
    private void initSamplingView() {
        //通用设置
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setHighlightFullBarEnabled(false);

        //**********设置X轴相关属性********************
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);//绘制纵轴线
        xAxis.setDrawGridLines(false); //绘制横轴线
        xAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));
        xAxis.setGranularity(1);//设置缩放时保持横坐标值固定

        xAxis.setValueFormatter(new IAxisValueFormatter() {//绘制横坐标值
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "环境质量" + value;
            }
        });


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);//设置梯度个数
        leftAxis.setAxisMinimum(0f);//设置会影响绘制
        leftAxis.setDrawGridLines(true); //绘制横轴线
        leftAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));

        setSamplingViewData();
    }

    /**
     * 待采样
     */
    private void initPendingSamplingView() {
        //通用设置
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(75f);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTouchEnabled(false);
        pieChart.getLegend().setEnabled(false);


        SpannableString spannableString = new SpannableString("待采样\n\n\n26");
        //设置字体大小
        spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_16)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_20)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.text_color_666666)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.text_color_41a5ff)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(spannableString);

        setPendingSamplingViewData();

        //*******设置值显示线*************
        PieDataSet pieDataSet = (PieDataSet) pieChart.getData().getDataSet();
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.2f);
        pieDataSet.setValueLinePart2Length(0.4f);
        pieDataSet.setDrawValues(true);
        pieDataSet.setValueLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    }

    /**
     * 已收样
     */
    private void initReceivedView() {
        //通用设置
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        //**********设置X轴相关属性********************
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);//绘制纵轴线
        xAxis.setDrawGridLines(false); //绘制横轴线
        xAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));
        xAxis.setGranularity(1);//设置缩放时保持横坐标值固定
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "09/0" + (int) value;
            }
        });
        //**********设置Y轴相关属性********************
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);//设置梯度个数
        leftAxis.setAxisMinimum(0f);//设置会影绘制
        leftAxis.setAxisLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));

        setReceivedViewData();
    }

    /**
     * 待收样
     */
    private void initWaitReceivedView() {
        //通用设置
        pieChart1.getDescription().setEnabled(false);
        pieChart1.setHoleRadius(75f);
        pieChart1.setUsePercentValues(false);
        pieChart1.setDrawEntryLabels(false);
        pieChart1.setTouchEnabled(false);
        pieChart1.getLegend().setEnabled(false);

        SpannableString spannableString = new SpannableString("待收样\n\n\n30");
        //设置字体大小
        spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_16)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_20)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.text_color_666666)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.text_color_41a5ff)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart1.setCenterText(spannableString);

        setWaitReceivedViewData();
        //*******设置值显示线*************
        PieDataSet pieDataSet = (PieDataSet) pieChart1.getData().getDataSet();
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.2f);
        pieDataSet.setValueLinePart2Length(0.4f);
        pieDataSet.setValueLineColor(ContextCompat.getColor(getContext(), R.color.dc_d1d1d1));
        pieDataSet.setDrawValues(true);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    }


    private void setSamplingViewData() {
        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();

        for (int i = 0; i < 4; i++) {
            float val = (float) (Math.random() * 4);
            barEntries.add(new BarEntry(i, val));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.bg_color_2b99ff));
        barDataSet.setDrawValues(true);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.35f);
        data.setValueTextColor(ContextCompat.getColor(getContext(), R.color.text_color_333333));
        barChart.setData(data);
        barChart.setFitBars(true);
    }

    private void setPendingSamplingViewData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

        for (int i = 0; i < 2; i++) {
            pieEntries.add(new PieEntry((float) ((Math.random() * 2)), "Party A" + i));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Election Results");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(getContext(), R.color.bg_color_ffd269));
        colors.add(ContextCompat.getColor(getContext(), R.color.bg_color_41af91));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(getContext(), R.color.text_color_333333));
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (dataSetIndex == 0) {
                    return "一周内到期: " + (int) (value * 26);
                }
                return "一周以上到期: " + (int) (value * 26);
            }
        });

        pieChart.setData(data);
    }

    private void setReceivedViewData() {

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "");

        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.bg_color_ff912d));
        lineDataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.bg_color_ff912d));
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setDrawValues(false);

        LineData data = new LineData(lineDataSet);
        data.setValueTextColor(ContextCompat.getColor(getContext(), R.color.text_color_333333));
        lineChart.setData(data);
    }

    private void setWaitReceivedViewData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();

        for (int i = 0; i < 2; i++) {
            pieEntries.add(new PieEntry((float) ((Math.random() * 2)), "Party A" + i));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Election Results");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();


        colors.add(ContextCompat.getColor(getContext(), R.color.bg_color_c23531));
        colors.add(ContextCompat.getColor(getContext(), R.color.bg_color_2f4554));


        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(getContext(), R.color.text_color_333333));
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (dataSetIndex == 0) {
                    return "一周内到期: " + (int) (value * 30);
                }
                return "一周以上到期: " + (int) (value * 30);
            }
        });

        pieChart1.setData(data);
    }

    @OnClick({R.id.btn_month, R.id.btn_week, R.id.btn_day, R.id.btn_date, R.id.btn_sampling, R.id.btn_wait_sampling, R.id.btn_receive, R.id.btn_wait_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_month:
                break;
            case R.id.btn_week:
                break;
            case R.id.btn_day:
                break;
            case R.id.btn_date:
                break;
            case R.id.btn_sampling:
                ArtUtils.makeText(getContext(), "已采样");
                break;
            case R.id.btn_wait_sampling:
                ArtUtils.startActivity(TaskActivity.class);
                break;
            case R.id.btn_receive:
                ArtUtils.makeText(getContext(), "已收样");
                break;
            case R.id.btn_wait_receive:
                ArtUtils.startActivity(WastewaterActivity.class);
                break;
        }
    }
}
