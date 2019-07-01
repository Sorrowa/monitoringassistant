package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MsgAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class MsgActivity extends BaseTitileActivity<ApiPresenter> implements IView {


    @BindView(R.id.btn_read_all)
    TextView     btnReadAll;
    @BindView(R.id.recyclerView_msg)
    RecyclerView recyclerViewMsg;
    @BindView(R.id.tabview)
    CustomTab    tabview;

    private MsgAdapter mMsgAdapter;

    List<Msg> mMsgs;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("消息中心");
        //        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        //        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ArtUtils.makeText(getApplicationContext(), "搜索");
        //            }
        //        });
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(MsgSearchActivity.class);
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTabData();
        initMsgData();

        List<Msg> msgs = DBHelper.get().getMsgDao().queryBuilder().
                where(MsgDao.Properties.MsgStatus.eq(0)).list();
        if (!CheckUtil.isEmpty(msgs)) {
            btnReadAll.setVisibility(View.VISIBLE);
        } else {
            btnReadAll.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("全部", "任务通知", "审核通知", "报警消息");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {

            }
        });

    }

    /**
     * 初始化Tab数据
     */
    private void initMsgData() {
        ArtUtils.configRecyclerView(recyclerViewMsg, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mMsgs = DBHelper.get().getMsgDao().queryBuilder().orderDesc(MsgDao.Properties.SendTime).list();
        mMsgAdapter = new MsgAdapter(mMsgs);
        mMsgAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMsgStatus(position);
                Intent intent = new Intent(MsgActivity.this, MsgDetailActivity.class);
                intent.putExtra("title", mMsgs.get(position).getMsgTitle());
                intent.putExtra("content", mMsgs.get(position).getMsgContent());
                ArtUtils.startActivity(intent);
            }
        });
        recyclerViewMsg.setAdapter(mMsgAdapter);
    }


    @OnClick(R.id.btn_read_all)
    public void onClick() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            if (!CheckUtil.isEmpty(mMsgs)) {
                List<String> messageIds = new ArrayList<>();
                for (Msg msg : mMsgs) {
                    messageIds.add(msg.getId());
                }
                mPresenter.putReadMsg(Message.obtain(this, new Object()), messageIds);
            }
        } else {
            showMessage("网络未连接，请联网后操作");
        }

    }

    @Override
    public void showLoading() {
        showLoadingDialog("提交中...");
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_OK:
                updateMsgStatus();
                showMessage("提交成功");
                break;
            case Message.RESULT_FAILURE:

                break;

        }
    }

    private void updateMsgStatus() {
        for (Msg msg : mMsgs) {
            msg.setMsgStatus(1);
        }
        mMsgAdapter.notifyDataSetChanged();
        DBHelper.get().getMsgDao().updateInTx(mMsgs);
    }

    private void updateMsgStatus(int position) {
        mMsgs.get(position).setMsgStatus(1);
        mMsgAdapter.notifyDataSetChanged();
        DBHelper.get().getMsgDao().updateInTx(mMsgs);
    }

}
