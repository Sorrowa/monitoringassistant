package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class PrinterHolder extends BaseHolder<Tab> {

    //    @BindView(R.id.iv_task_type)
    //    ImageView mIvTaskType;
    //    @BindView(R.id.tab_name)
    //    TextView  mTabName;

    public PrinterHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position) {
        //        mIvTaskType.setVisibility(View.VISIBLE);
        //        mTabName.setText(data.getTabName());
        //        if (data.isSelected()) {
        //            mTabName.setTextColor(Color.WHITE);
        //            mTabIcon.setImageResource(data.getSelectedResId());
        //            itemView.setBackgroundColor(Color.parseColor("#0f87ff"));
        //        } else {
        //            mTabName.setTextColor(Color.parseColor("#939FB0"));
        //            mTabIcon.setImageResource(data.getResId());
        //            itemView.setBackgroundColor(Color.TRANSPARENT);
        //        }
    }

    @Override
    protected void onRelease() {
        //        this.mIvTaskType = null;
        //        this.mTabName = null;

    }
}
