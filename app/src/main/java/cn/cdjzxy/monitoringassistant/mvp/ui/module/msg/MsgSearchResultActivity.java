package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MsgAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class MsgSearchResultActivity extends BaseTitileActivity {

    @BindView(R.id.btn_read_all)
    TextView btnReadAll;
    @BindView(R.id.recyclerView_msg)
    RecyclerView recyclerViewMsg;
    @BindView(R.id.tabview)
    CustomTab tabview;

    private MsgAdapter mMsgAdapter;

    List<Msg> mMsgs;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnReadAll.setVisibility(View.GONE);
        tabview.setVisibility(View.GONE);
        String key = getIntent().getStringExtra("key");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        initMsgData(startDate, endDate, key);
    }

    /**
     * 数据库中msg DATA
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param s         关键字
     */
    private void initMsgData(String startDate, String endDate, String s) {
        if (CheckUtil.isEmpty(s)) return;
        ArtUtils.configRecyclerView(recyclerViewMsg, new LinearLayoutManager(this));
        QueryBuilder<Msg> builder = DBHelper.get().getMsgDao().queryBuilder();
        builder.whereOr(MsgDao.Properties.MsgTitle.like(s),
                MsgDao.Properties.MsgContent.like(s));
        if (!CheckUtil.isEmpty(startDate) && !CheckUtil.isEmpty(endDate)) {
            builder.where(MsgDao.Properties.SendTime.between(startDate, endDate));
        }
        builder.orderDesc(MsgDao.Properties.SendTime);
        List<Msg> msgList = builder.list();
        mMsgAdapter = new MsgAdapter(msgList);
        mMsgAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMsgStatus(position);
                Intent intent = new Intent(MsgSearchResultActivity.this, MsgDetailActivity.class);
                intent.putExtra("title", mMsgs.get(position).getMsgTitle());
                intent.putExtra("content", mMsgs.get(position).getMsgContent());
                ArtUtils.startActivity(intent);
            }
        });
        recyclerViewMsg.setAdapter(mMsgAdapter);

    }

    private void updateMsgStatus(int position) {
        mMsgs.get(position).setMsgStatus(1);
        mMsgAdapter.notifyDataSetChanged();
        DBHelper.get().getMsgDao().updateInTx(mMsgs);
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("搜索结果");
    }
}
