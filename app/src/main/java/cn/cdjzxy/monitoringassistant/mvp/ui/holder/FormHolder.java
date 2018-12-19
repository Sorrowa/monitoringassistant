package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;

/**
 * 主页tab
 */

public class FormHolder extends BaseHolder<FormSelect> {

    @BindView(R.id.tv_name)
    TextView mTvName;

    public FormHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(FormSelect data, int position) {
        mTvName.setText(data.getFormName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
