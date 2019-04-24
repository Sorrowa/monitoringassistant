package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class TabHolder extends BaseHolder<Tab> {


    @BindView(R.id.tab_name)
    TextView mTabName;
    @BindView(R.id.tab_icon)
    ImageView mTabIcon;

    public TabHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position) {
        mTabName.setText(data.getTabName());
        if (data.getResId() != -1 || data.getResId() != 0) {
            mTabIcon.setVisibility(View.VISIBLE);
            mTabIcon.setImageResource(data.getResId());
        }
        if (data.isSelected()) {
            itemView.setBackgroundColor(Color.parseColor("#c7e4ff"));
        } else {
            itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    protected void onRelease() {

        this.mTabName = null;

    }
}
