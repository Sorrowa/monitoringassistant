package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

public class MsgDetailActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.tv_content)
    TextView tvContent;

    TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("消息详情");
        mTitleBarView = titleBar;
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTitleBarView.setTitleMainText(getIntent().getStringExtra("title"));
        tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content")));
    }


}
