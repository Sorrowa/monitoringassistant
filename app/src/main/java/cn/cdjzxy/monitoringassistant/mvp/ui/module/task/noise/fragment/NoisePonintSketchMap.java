package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.navisdk.adapter.impl.BaiduNaviManager;
import com.bumptech.glide.Glide;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.preview.PreviewActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;

public class NoisePonintSketchMap extends BaseFragment implements IView {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.linear_add)
    LinearLayout layoutAdd;
    @BindView(R.id.linear_map)
    LinearLayout layoutMap;
    @BindView(R.id.linear_clean)
    LinearLayout layoutClean;

    private List<Tab> mTabs;
    private static final int REQUEST_CODE = 1053;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_noise_point_map, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @OnClick({R.id.linear_add, R.id.linear_map, R.id.linear_clean})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_add:
                imageView.setVisibility(View.VISIBLE);
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.linear_map:
                showMessage("待开发");
                // TODO: 2019/4/23 待开发，等待索贝完成后再做  以免代码重复和多导入百度地图依赖
                break;
            case R.id.linear_clean:
                showMessage("待开发");
                break;

        }
    }

    private void choosePhoto(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,
                        "cn.cdjzxy.monitoringassistant.android7.fileprovider",
                        "MonitoringAssistant"))
                .maxSelectable(1)
                //.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .showSingleMediaType(true)
                .imageEngine(new Glide4Engine())
                .originalEnable(true)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {//添加图片
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                imageView.getLayoutParams().width = frameLayout.getWidth();
                imageView.getLayoutParams().height = frameLayout.getHeight();
                Glide.with(getContext()).
                        load(paths.get(0)).
                        into(imageView);
            }

        }
    }


}
