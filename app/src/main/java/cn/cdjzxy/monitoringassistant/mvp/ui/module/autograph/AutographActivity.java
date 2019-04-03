package cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.support.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.navisdk.ui.routeguide.mapmode.subview.G;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.yinghe.whiteboardlib.Utils.BitmapUtils;
import com.yinghe.whiteboardlib.Utils.TimeUtils;
import com.yinghe.whiteboardlib.bean.SketchData;
import com.yinghe.whiteboardlib.view.SketchView;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;


import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_CIRCLE;
import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_DRAW;

public class AutographActivity extends BaseTitileActivity {
    @BindView(R.id.sketch_view_sampling)
    SketchView sketchViewSampling;//采样人签名
    @BindView(R.id.sketch_view_check)
    SketchView sketchViewCheck;//校核人签名
    @BindView(R.id.sketch_view_examine)
    SketchView sketchViewExamine;//审核人签名
    @BindView(R.id.img_empty_check)
    ImageView imgEmptyCheck;
    @BindView(R.id.img_empty_examine)
    ImageView imgEmptyExamine;
    @BindView(R.id.img_empty_sampling)
    ImageView imgEmptySampling;

    private String autographPath;//保存签名文件的路径
    private String autographId;//表单id;
    public static final String AUTOGRAPH_ID = "autographId";
    private static final String AUTOGRAPH_PATH = "/AUTOGRAPH/";
    private AutographType autographType;


    public static final String INTENT_SAMPLING_PATH = "SamplingPath";
    public static final String INTENT_CHECK_PATH = "checkPath";
    public static final String INTENT_EXAMINE_PATH = "examinePath";
    public static final String INTENT_CAN_CHANGE = "isCanChange";

    private String pngSamplingPath;
    private String pngCheckPath;
    private String pngExaminePath;
    private boolean isCanChange = true;

    private enum AutographType {
        sampling,
        check,
        examine
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_autograph;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initFilePath();
        initSketchView();

    }


    /**
     * 初始化文件保存的目录
     */
    private void initFilePath() {
        if (getIntent() != null) {
            autographId = getIntent().getStringExtra(AUTOGRAPH_ID);
            autographPath = Constant.FILE_DIR + Constant.PNG_DIR + AUTOGRAPH_PATH;
            pngCheckPath = getIntent().getStringExtra(INTENT_CHECK_PATH);
            pngExaminePath = getIntent().getStringExtra(INTENT_EXAMINE_PATH);
            pngSamplingPath = getIntent().getStringExtra(INTENT_SAMPLING_PATH);
            isCanChange = getIntent().getBooleanExtra(INTENT_CAN_CHANGE, true);
        }
    }

    /**
     * 初始化画笔参数
     */
    private void initSketchView() {
        SketchData newSketchSampling = new SketchData();
        SketchData newSketchCheck = new SketchData();
        SketchData newSketchExamine = new SketchData();
        if (pngSamplingPath != null && !pngSamplingPath.equals("")) {
            newSketchSampling.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngSamplingPath);
            sketchViewSampling.setSketchData(newSketchSampling);
            sketchViewSampling.setEditMode(SketchView.EDIT_PHOTO);
        } else {
            sketchViewSampling.setSketchData(newSketchSampling);
            sketchViewSampling.setStrokeType(STROKE_TYPE_DRAW);
        }
        if (pngExaminePath != null && !pngExaminePath.equals("")) {
            newSketchExamine.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngExaminePath);
            sketchViewExamine.setSketchData(newSketchExamine);
            sketchViewExamine.setEditMode(SketchView.EDIT_PHOTO);
        } else {
            sketchViewExamine.setSketchData(newSketchExamine);
            sketchViewExamine.setStrokeType(STROKE_TYPE_DRAW);
        }
        if (pngCheckPath != null && !pngCheckPath.equals("")) {
            newSketchCheck.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngCheckPath);
            sketchViewCheck.setSketchData(newSketchCheck);
            sketchViewCheck.setEditMode(SketchView.EDIT_PHOTO);
        } else {
            sketchViewCheck.setSketchData(newSketchCheck);
            sketchViewCheck.setStrokeType(STROKE_TYPE_DRAW);
        }
        if (!isCanChange) {
            imgEmptyCheck.setVisibility(View.GONE);
            imgEmptyExamine.setVisibility(View.GONE);
            imgEmptySampling.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.img_empty_sampling, R.id.img_empty_examine, R.id.img_empty_check, R.id.btn_post})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_empty_check:
                sketchViewCheck.erase();
                sketchViewCheck.setStrokeType(STROKE_TYPE_DRAW);
                break;
            case R.id.img_empty_sampling:
                sketchViewSampling.erase();
                sketchViewSampling.setStrokeType(STROKE_TYPE_DRAW);
                break;
            case R.id.img_empty_examine:
                sketchViewExamine.erase();
                sketchViewExamine.setStrokeType(STROKE_TYPE_DRAW);
                break;
            case R.id.btn_post:
                postAutograph();
                break;
        }
    }

    /**
     * 保存签名
     */
    private void postAutograph() {
        if (sketchViewSampling.getRecordCount() == 0//采样人没有签写
                && sketchViewExamine.getRecordCount() == 0//审核人签名
                && sketchViewCheck.getRecordCount() == 0) {//校核人签名
            Toast.makeText(this, "您还没有进行任何签名", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String name;
            if (sketchViewSampling.getRecordCount() != 0) {
                name = autographId + "Sampling";
                new SaveToFileTask().execute(name);
                autographType = AutographType.sampling;
            }
            if (sketchViewCheck.getRecordCount() != 0) {
                name = autographId + "Check";
                new SaveToFileTask().execute(name);
                autographType = AutographType.check;
            }
            if (sketchViewExamine.getRecordCount() != 0) {
                name = autographId + "Examine";
                new SaveToFileTask().execute(name);
                autographType = AutographType.examine;
            }
        }

    }

    //异步保存
    class SaveToFileTask extends AsyncTask<String, Void, File> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("正在保存", false);
        }

        @Override
        protected File doInBackground(String... photoName) {
            return saveInOI(autographPath, photoName[0], 80);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file == null) return;
            if (file.exists()) {
                switch (autographType) {
                    case check:
                        pngCheckPath = file.getPath();
                        break;
                    case examine:
                        pngExaminePath = file.getPath();
                        break;
                    case sampling:
                        pngSamplingPath = file.getPath();
                        break;
                }
                Toast.makeText(AutographActivity.this, "成功保存", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AutographActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
            }
            closeLoadingDialog();
        }
    }

    /**
     * show 保存图片到本地文件，耗时操作
     *
     * @param filePath 文件保存路径
     * @param imgName  文件名
     * @param compress 压缩百分比1-100
     * @return 返回保存的图片文件
     * @author TangentLu
     * create at 16/6/17 上午11:18
     */
    public File saveInOI(String filePath, String imgName, int compress) {
        if (!imgName.contains(".png")) {
            imgName += ".png";
        }
        Log.e(TAG, "saveInOI: " + System.currentTimeMillis());
        if (autographType == null) {
            Log.e(TAG, "saveInOI: " + "autographType+异常为null");
            return null;
        }
        Bitmap newBM;
        switch (autographType) {
            case check:
                newBM = sketchViewCheck.getResultBitmap();
                break;
            case examine:
                newBM = sketchViewExamine.getResultBitmap();
                break;
            case sampling:
                newBM = sketchViewSampling.getResultBitmap();
                break;
            default:
                newBM = sketchViewSampling.getResultBitmap();
                break;
        }

        Log.e(TAG, "saveInOI: " + System.currentTimeMillis());
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(filePath, imgName);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

            if (compress >= 1 && compress <= 100)
                newBM.compress(Bitmap.CompressFormat.PNG, compress, out);
            else {
                newBM.compress(Bitmap.CompressFormat.PNG, 80, out);
            }
            Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

            out.close();
            newBM.recycle();
            newBM = null;
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("签名");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra(INTENT_SAMPLING_PATH, pngSamplingPath);
        intent.putExtra(INTENT_EXAMINE_PATH, pngExaminePath);
        intent.putExtra(INTENT_CHECK_PATH, pngCheckPath);
        setResult(RESULT_OK, intent);
        finish();
    }
}
