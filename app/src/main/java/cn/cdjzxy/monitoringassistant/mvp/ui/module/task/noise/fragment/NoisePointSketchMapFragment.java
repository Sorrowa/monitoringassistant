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
import cn.cdjzxy.monitoringassistant.utils.BitmapUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.updateData;
import static cn.cdjzxy.monitoringassistant.utils.FileUtils.saveInOI;

public class NoisePointSketchMapFragment extends BaseFragment implements IView {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    //    @BindView(R.id.image_view)
//    ImageView imageView;
    @BindView(R.id.sketch_view)
    SketchView sketchView;
    @BindView(R.id.linear_add)
    LinearLayout layoutAdd;
    @BindView(R.id.linear_map)
    LinearLayout layoutMap;
    @BindView(R.id.linear_clean)
    LinearLayout layoutClean;
    @BindView(R.id.linear_noise)
    LinearLayout linearNoise;
    @BindView(R.id.text_noise_source)
    TextView tvNoiseSource;
    @BindView(R.id.text_noise_point)
    TextView tvNoisePoint;
    @BindView(R.id.text_noise_monitor)
    TextView tvNoiseMonitor;
    @BindView(R.id.image_view)
    ImageView imageView;


    private static final int REQUEST_CODE = 1053;
    private String FILE_PATH, FILE_NAME;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_point_map, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        SketchData newSketchCheck = new SketchData();
        sketchView.setSketchData(newSketchCheck);
        sketchView.setEditMode(SketchView.EDIT_PHOTO);
        FILE_PATH = Constant.FILE_DIR + Constant.PNG_DIR + "/noise";
        if (mPrivateData != null && mPrivateData.getImageSYT() != null) {
            linearNoise.setVisibility(View.GONE);
            sketchView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.linear_add, R.id.linear_map, R.id.linear_clean, R.id.text_noise_source,
            R.id.text_noise_point, R.id.text_noise_monitor, R.id.linear_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_add:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.linear_map:
                startMapAct();
                break;
            case R.id.linear_clean:
                sketchView.erase();
                break;
            case R.id.text_noise_monitor:
                sketchView.addPhotoByBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_noise_monitor));
                break;
            case R.id.text_noise_source:
                sketchView.addPhotoByBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_noise_source));
                break;
            case R.id.text_noise_point:
                sketchView.addPhotoByBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_noise_point));
                break;
            case R.id.linear_save:
                if (sketchView.getRecordCount() == 0) {
                    showNoChangeSaveDialog();
                } else {
                    showSavePicDialog();
                }
                break;

        }
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
                    FILE_NAME = new File(path).getName();
                    linearNoise.setVisibility(View.GONE);
                    sketchView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).
                            load(path).
                            into(imageView);
                }
            }
        });
    }

    /**
     * 显示没有做任何改变的保存dialog
     */
    private void showNoChangeSaveDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("是否直接保存图片？")
                .setMessage("您是想直接保存这张图片嘛？因为您没有添加任何噪声点")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePic();
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
     * 显示保存图片的dialog
     */
    private void showSavePicDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("是否确定保存本次所有编辑？")
                .setMessage("本次保存后，无法修改，如果您需要修改，需要你点击“清空标记”，从新编辑！")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePic();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void savePic() {
        showLoadingDialog("正在保存");
        if (FILE_NAME == null || FILE_NAME.equals("")) {
            FILE_NAME = System.currentTimeMillis() + ".png";
        }
        new saveToFileTask().execute(FILE_NAME);
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
                FILE_NAME = new File(paths.get(0)).getName();
                linearNoise.setVisibility(View.VISIBLE);
                sketchView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                sketchView.setBackgroundByPath(paths.get(0));
            }
        }
    }

    class saveToFileTask extends AsyncTask<String, Void, File> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected File doInBackground(String... photoName) {
            return saveInOI(sketchView.getResultBitmap(), FILE_PATH, photoName[0], 100);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            hideLoading();
            if (file.exists())
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
            linearNoise.setVisibility(View.GONE);
            sketchView.erase();
            sketchView.setBackgroundByPath(file.getPath());
            sketchView.setEditMode(SketchView.EDIT_PHOTO);
            saveSample(file.getPath());

        }
    }

    private void saveSample(String path) {
        NoisePrivateData privateData = mPrivateData;
        privateData.setImageSYT(path);
        String jsonStr = new Gson().toJson(privateData);
        mSample.setPrivateData(jsonStr);
        updateData();
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

}
