package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import java.io.File;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;

/**
 * 知识库
 */
public class RepositoryHolder extends BaseHolder<File> {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_name)
    TextView  mTvName;
    @BindView(R.id.tv_desc)
    TextView  mTvDesc;

    public RepositoryHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(File data, int position) {
        mTvName.setText(data.getName());
        if (data.isDirectory()) {
            mIvIcon.setImageResource(R.mipmap.ic_folder);
            mTvDesc.setText("暂无文件目录说明");
        } else {
            mIvIcon.setImageResource(R.mipmap.ic_file);
            mTvDesc.setText("暂无文件说明");
        }
    }

    @Override
    protected void onRelease() {
        this.mIvIcon = null;
        this.mTvName = null;
        this.mTvDesc = null;

    }
}
