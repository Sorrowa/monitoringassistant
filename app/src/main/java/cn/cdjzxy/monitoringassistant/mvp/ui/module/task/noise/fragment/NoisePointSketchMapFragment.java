package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.ui.routeguide.mapmode.subview.N;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.serenegiant.utils.FileUtils;
import com.wonders.health.lib.base.http.imageloader.glide.ImageConfigImpl;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.yinghe.whiteboardlib.bean.SketchData;
import com.yinghe.whiteboardlib.view.SketchView;
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
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.AutographActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseMapActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoisePointPicActivity;
import cn.cdjzxy.monitoringassistant.utils.BitmapUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.updateData;
import static cn.cdjzxy.monitoringassistant.utils.FileUtils.saveInOI;

public class NoisePointSketchMapFragment extends BaseFragment implements IView {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    //    @BindView(R.id.image_view)
//    ImageView imageView;
//    @BindView(R.id.sketch_view)
//    SketchView sketchView;
    @BindView(R.id.linear_add)
    LinearLayout layoutAdd;
    @BindView(R.id.linear_map)
    LinearLayout layoutMap;
    @BindView(R.id.linear_clean)
    LinearLayout layoutClean;
    //    @BindView(R.id.linear_noise)
//    LinearLayout linearNoise;
//    @BindView(R.id.text_noise_source)
//    TextView tvNoiseSource;
//    @BindView(R.id.text_noise_point)
//    TextView tvNoisePoint;
//    @BindView(R.id.text_noise_monitor)
//    TextView tvNoiseMonitor;
    @BindView(R.id.image_view)
    ImageView imageView;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_point_map, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mPrivateData != null && mPrivateData.getImageSYT() != null) {
            if (mPrivateData.getImageSYT().startsWith("/Upload")) {
                Glide.with(getContext()).
                        load(UserInfoHelper.get().getUserInfo().getWebUrl() +
                                mPrivateData.getImageSYT()).
                        into(imageView);
            } else {
                Glide.with(getContext()).
                        load(mPrivateData.getImageSYT()).
                        into(imageView);
            }

//            sketchView.setBackgroundByPath(UserInfoHelper.get().getUserInfo().getWebUrl() +
//                    mPrivateData.getImageSYT());
        }
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
        if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
            return;
        }
        switch (v.getId()) {
            case R.id.linear_add:
                startPicAct();
                break;
            case R.id.linear_map:
                startMapAct();
                break;
            case R.id.linear_clean:
                if (mPrivateData.getImageSYT() == null || mPrivateData.getImageSYT().equals("")) {
                    showDeleteHintPicDialog("请您先选择一张图片或者地图编辑后，在进行删除", false);
                } else {
                    showDeleteHintPicDialog("请问您是否确定删除这张图片？", true);
                }
                break;

        }
    }

    /**
     * 提示删除
     */
    private void showDeleteHintPicDialog(String msg, boolean isDelete) {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDelete) {
                            saveSample("");
                        }
                        imageView.setVisibility(View.GONE);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 跳转测点示意图图片编辑界面
     */
    private void startPicAct() {
        Intent intent = new Intent();
        intent.setClass(getContext(), NoisePointPicActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("filePngPath");
                    if (path == null || path.equals("")) return;
                    saveSample(path);
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).
                            load(path).
                            into(imageView);
                }
            }
        });
    }

    /**
     * 跳转地图瞄点界面
     */
    private void startMapAct() {
        Intent intent = new Intent();
        intent.setClass(getContext(), NoiseMapActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("filePngPath");
                    if (path == null || path.equals("")) return;
                    saveSample(path);
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).
                            load(path).
                            into(imageView);
                }
            }
        });
    }

    private void saveSample(String path) {
        NoisePrivateData privateData = mPrivateData;
        privateData.setImageSYT(path);
        String jsonStr = new Gson().toJson(privateData);
        mSample.setPrivateData(jsonStr);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

}
