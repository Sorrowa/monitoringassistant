package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class SearchHistoryHolder extends BaseHolder<Tab> {


    //    @BindView(R.id.tab_name)
    //    TextView mTabName;

    public SearchHistoryHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position) {
        //        mTabName.setText(data.getTabName());
        //        if (data.isSelected()) {
        //            itemView.setBackgroundColor(Color.parseColor("#c7e4ff"));
        //        } else {
        //            itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //        }
    }

    @Override
    protected void onRelease() {

        //        this.mTabName = null;

    }
}
