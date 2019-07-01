package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.yinghe.whiteboardlib.bean.SketchData;
import com.yinghe.whiteboardlib.view.SketchView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;

import static cn.cdjzxy.monitoringassistant.utils.FileUtils.saveInOI;

/**
 * 测试点示意图： 添加图片界面
 */
public class NoisePointPicActivity extends BaseTitileActivity implements IView {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.sketch_view)
    SketchView sketchView;
    @BindView(R.id.linear_add)
    LinearLayout layoutAdd;
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

    private static final int REQUEST_CODE = 1053;
    private String FILE_PATH, FILE_NAME, FILE_PNG_PATH;

    private boolean isNeedSave = false;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_noise_pic;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("测点示意图——图片");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeedSave) {
                    showSaveDialog("是否保存图片？",
                            "请问您选择了这样图片是否需要保存呢？"
                            , true);
                } else {
                    onBack();
                }


            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        FILE_PATH = Constant.FILE_DIR + Constant.PNG_DIR + "/noise/";
        SketchData newSketchCheck = new SketchData();
        sketchView.setSketchData(newSketchCheck);
        sketchView.setEditMode(SketchView.EDIT_PHOTO);
        choosePhoto(REQUEST_CODE);
    }

    @OnClick({R.id.linear_add, R.id.linear_clean, R.id.text_noise_source,
            R.id.text_noise_point, R.id.text_noise_monitor, R.id.linear_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_add:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.linear_clean:
                sketchView.erase();
                isNeedSave = false;
                FILE_PNG_PATH = "";
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
                    if (isNeedSave) {
                        showSaveDialog("是否直接保存图片？",
                                "您是想直接保存这张图片？不在上面添加任何噪声点吗？"
                                , false);
                    } else {
                        showMessage("请您至少选择一张图片在进行保存");
                    }
                } else {
                    showSaveDialog("是否确定保存本次所有编辑？",
                            "请注意：本次保存后，无法修改！！！\n如果您需要修改，需要你点击“删除”，从新选择图片在进行编辑！"
                            , false);
                }
                break;
        }
    }

    /**
     * 选择图片
     *
     * @param requestCode
     */
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
                sketchView.setBackgroundByPath(paths.get(0));
                isNeedSave = true;
            } else {
                isNeedSave = false;
            }
        }
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    /**
     * 显示没有做任何改变的保存dialog
     */
    private void showSaveDialog(String title, String msg, boolean isFinsh) {
        final Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePic();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isFinsh) {
                            onBack();
                        }
                    }
                }).create();
        dialog.show();
    }

    /**
     * 保存图片
     */
    private void savePic() {
        showLoadingDialog("正在保存");
        if (FILE_NAME == null || FILE_NAME.equals("")) {
            FILE_NAME = System.currentTimeMillis() + ".png";
        }
        new SaveToFileTask().execute(FILE_NAME);
    }


    class SaveToFileTask extends AsyncTask<String, Void, File> {
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
            isNeedSave = false;
            if (file.exists())
                showMessage("保存成功");
            else
                showMessage("保存失败!");
            FILE_PNG_PATH = file.getPath();
            onBack();
        }
    }

    public void onBack() {
        closeLoadingDialog();
        Intent intent = new Intent();
        intent.putExtra("filePngPath", FILE_PNG_PATH);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
