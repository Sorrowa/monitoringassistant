package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseActivity;
import cn.cdjzxy.monitoringassistant.widgets.PullScrollView;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 流转任务activity 包含流转已收样和 流转待收样界面
 */
public class WanderTaskActivity extends BaseActivity<ApiPresenter> implements IView {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_tip)
    TextView tvEmptyTip;
    @BindView(R.id.layout_motor_one_pull_view)
    PullScrollView layoutMotorOnePullView;
    private TaskAdapter adapter;

    public static final String INTENT_WANDER_FROM = "intent_from";
    public static final String INTENT_FROM_ALREADY = "0";//流转待收样
    public static final String INTENT_FROM_WAIT = "1";//流转已收样
    private String intentWanderFrom;//流转单状态（0待流转，1已流转，10自送样，20待流转已流转一起查）
    private AppComponent mAppComponent;
    private int page = 1;
    private List<Project> list;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();
        if (getIntent() != null) {
            intentWanderFrom = getIntent().getStringExtra(INTENT_WANDER_FROM);
        } else {
            intentWanderFrom = "0";
        }
        Map<String, String> map = new HashMap<>();
        map.put("status", intentWanderFrom);
        map.put("page", page + "");
        mPresenter.getSampleStorageProject(map, Message.obtain(this, new Object()), true);
        adapter = new TaskAdapter(list);

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        mAppComponent = ArtUtils.obtainAppComponentFromContext(this);
        return new ApiPresenter(mAppComponent);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_FAILURE://加载失败
                break;
            case Message.RESULT_OK://下拉刷新
                if (list!=null){
                    list.clear();
                }else {
                    list=new ArrayList<>();
                }
                list.addAll((ArrayList<Project>)message.obj);
                break;
            case 1001://上拉加载
                if (list==null){
                    list=new ArrayList<>();
                }
                list.addAll((ArrayList<Project>)message.obj);
                break;
        }
    }
}
