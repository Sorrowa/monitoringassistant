package cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.AutographActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.adapter.AutographFragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import static android.app.Activity.RESULT_OK;

public class AutographFragment extends BaseFragment {
    @BindView(R.id.list_view)
    ListView listView;
    private String pngSamplingPath;
    private String pngCheckPath;
    private String pngExaminePath;
    private List<String> list;
    private AutographFragmentAdapter adapter;
    private List<SamplingFile> autographList;
    private int position;


    public AutographFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auagment_list, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        autographList = WastewaterActivity.mSample.getAutographList();
        if (autographList == null) {
            autographList = new ArrayList<>();
        }

        list = new ArrayList<>();
        list.add("采样人签名");
        list.add("校核人签名");
        list.add("审核人签名");
        adapter = new AutographFragmentAdapter(list, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name, path;
                switch (position) {
                    case 0://采样人
                        name = "_SAMPLING.png";
                        break;
                    case 1://校核人签名
                        name = "_EXAMINE.png";
                        break;
                    case 2://审核人签名
                        name = "_CHECK.png";
                        break;
                    default:
                        return;
                }
                for (SamplingFile samplingFile : autographList) {
                    if (samplingFile.getFileName().equals(WastewaterActivity.mSample.getId() + name)) {
                        path = samplingFile.getFilePath();
                        startAutographAct(path, name);
                        return;
                    }
                }
                startAutographAct("", name);
            }
        });
    }

    /**
     * 启动签名界面
     */
    private void startAutographAct(String path, String name) {
        Intent intent = new Intent(getContext(), AutographActivity.class);
        intent.putExtra(AutographActivity.AUTOGRAPH_ID, WastewaterActivity.mSample.getId());
        intent.putExtra(AutographActivity.INTENT_AUTOGRAPH_PATH, path);
        intent.putExtra(AutographActivity.INTENT_AUTOGRAPH_NAME, name);
        if (WastewaterActivity.mSample.getStatus() == 0 ||
                WastewaterActivity.mSample.getStatus() == 4 ||
                WastewaterActivity.mSample.getStatus() == 9) {
            //还没有上传 可以编辑
            intent.putExtra(AutographActivity.INTENT_CAN_CHANGE, true);
        } else {
            intent.putExtra(AutographActivity.INTENT_CAN_CHANGE, false);
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == RESULT_OK) {
                    //todo 介绍签名文件进行处理
                    boolean isCanChange = data.getBooleanExtra(AutographActivity.INTENT_CAN_CHANGE, false);
                    //保存前面图片
                    if (isCanChange) {
                        String pngPath = data.getStringExtra(AutographActivity.INTENT_AUTOGRAPH_PATH);
                       // String pngName = data.getStringExtra(AutographActivity.INTENT_AUTOGRAPH_NAME);
                        SamplingFile samplingFile = new SamplingFile();
                        File file = new File(pngPath);
                        samplingFile.setLocalId(UUID.randomUUID().toString());
                        samplingFile.setId("");
                        samplingFile.setFilePath(pngPath);
                        samplingFile.setFileName(file.getName());
                        samplingFile.setSamplingId(WastewaterActivity.mSample.getId());
                        samplingFile.setUpdateTime(DateUtils.getTime(new Date().getTime()));
                        WastewaterActivity.mSample.getAutographList().add(samplingFile);
                    }
                }
            }
        });

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
