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

import android.widget.TextView;
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


import static cn.cdjzxy.monitoringassistant.utils.FileUtils.saveInOI;
import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_CIRCLE;
import static com.yinghe.whiteboardlib.bean.StrokeRecord.STROKE_TYPE_DRAW;

public class AutographActivity extends BaseTitileActivity {
    @BindView(R.id.sketch_view)
    SketchView sketchView;//采样人签名
    //    @BindView(R.id.sketch_view_check)
//    SketchView sketchViewCheck;//校核人签名
//    @BindView(R.id.sketch_view_examine)
//    SketchView sketchViewExamine;//审核人签名
//    @BindView(R.id.img_empty_check)
//    ImageView imgEmptyCheck;
//    @BindView(R.id.img_empty_examine)
//    ImageView imgEmptyExamine;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    @BindView(R.id.text_view_name)
    TextView textViewName;

    private String autographPath;//保存签名文件的路径
    private String autographId;//表单id;
    public static final String AUTOGRAPH_ID = "autographId";
    private static final String AUTOGRAPH_PATH = "/AUTOGRAPH/";
//    private AutographType autographType;


    //    public static final String INTENT_SAMPLING_PATH = "SamplingPath";
//    public static final String INTENT_CHECK_PATH = "checkPath";
//    public static final String INTENT_EXAMINE_PATH = "examinePath";
    public static final String INTENT_AUTOGRAPH_PATH = "autograph_path";//签名文件路径
    public static final String INTENT_CAN_CHANGE = "isCanChange";
    public static final String INTENT_AUTOGRAPH_NAME = "autograph_name";//签名文件名称

    //    private String pngSamplingPath;
//    private String pngCheckPath;
//    private String pngExaminePath;
    private String pngAutographPath;
    private String autographName;
    private boolean isCanChange = true;//是否可以编辑

//    private enum AutographType {
//        sampling,
//        check,
//        examine
//    }

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
//            pngCheckPath = getIntent().getStringExtra(INTENT_CHECK_PATH);
//            pngExaminePath = getIntent().getStringExtra(INTENT_EXAMINE_PATH);
//            pngSamplingPath = getIntent().getStringExtra(INTENT_SAMPLING_PATH);
            pngAutographPath = getIntent().getStringExtra(INTENT_AUTOGRAPH_PATH);
            autographName = getIntent().getStringExtra(INTENT_AUTOGRAPH_NAME);
            isCanChange = getIntent().getBooleanExtra(INTENT_CAN_CHANGE, true);
            switch (autographName) {
                case "_SAMPLING.png":
                    textViewName.setText("采样人签名");
                    break;
                case "_EXAMINE.png":
                    textViewName.setText("校核人人签名");
                    break;
                case "_CHECK.png":
                    textViewName.setText("审核人签名");
                    break;
            }
        }
    }

    /**
     * 初始化画笔参数
     */
    private void initSketchView() {
//        SketchData newSketchSampling = new SketchData();
//        SketchData newSketchExamine = new SketchData();
        SketchData newSketchCheck = new SketchData();

//        if (pngSamplingPath != null && !pngSamplingPath.equals("")) {
//            newSketchSampling.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngSamplingPath);
//            sketchViewSampling.setSketchData(newSketchSampling);
//            sketchViewSampling.setEditMode(SketchView.EDIT_PHOTO);
//        } else {
//            sketchViewSampling.setSketchData(newSketchSampling);
//            sketchViewSampling.setStrokeType(STROKE_TYPE_DRAW);
//        }
//        if (pngExaminePath != null && !pngExaminePath.equals("")) {
//            newSketchExamine.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngExaminePath);
//            sketchViewExamine.setSketchData(newSketchExamine);
//            sketchViewExamine.setEditMode(SketchView.EDIT_PHOTO);
//        } else {
//            sketchViewExamine.setSketchData(newSketchExamine);
//            sketchViewExamine.setStrokeType(STROKE_TYPE_DRAW);
//        }
//        if (pngCheckPath != null && !pngCheckPath.equals("")) {
//            newSketchCheck.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngCheckPath);
//            sketchViewCheck.setSketchData(newSketchCheck);
//            sketchViewCheck.setEditMode(SketchView.EDIT_PHOTO);
//        } else {
//            sketchViewCheck.setSketchData(newSketchCheck);
//            sketchViewCheck.setStrokeType(STROKE_TYPE_DRAW);
//        }
        if (pngAutographPath != null && !pngAutographPath.equals("")) {
            newSketchCheck.thumbnailBM = BitmapUtils.getBitmapForFile(this, pngAutographPath);
            sketchView.setSketchData(newSketchCheck);
            sketchView.setBackgroundByPath(pngAutographPath);
            sketchView.setEditMode(SketchView.EDIT_PHOTO);
        } else {
            sketchView.setSketchData(newSketchCheck);
            sketchView.setStrokeType(STROKE_TYPE_DRAW);
        }
        if (!isCanChange) {
            imgEmpty.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.img_empty, R.id.btn_post})
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.img_empty_check:
//                sketchViewCheck.erase();
//                sketchViewCheck.setStrokeType(STROKE_TYPE_DRAW);
//                break;
            case R.id.img_empty:
                sketchView.erase();
                sketchView.setStrokeType(STROKE_TYPE_DRAW);
                break;
//            case R.id.img_empty_examine:
//                sketchViewExamine.erase();
//                sketchViewExamine.setStrokeType(STROKE_TYPE_DRAW);
//                break;
            case R.id.btn_post:
                postAutograph();
                break;
        }
    }

    /**
     * 保存签名
     */
    private void postAutograph() {
        if (sketchView.getRecordCount() == 0//没有签名
//                && sketchViewExamine.getRecordCount() == 0//审核人签名
//                && sketchViewCheck.getRecordCount() == 0
                ) {//校核人签名
            Toast.makeText(this, "您还没有进行任何签名", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String name = autographId + autographName;
            new SaveToFileTask().execute(name);
            Log.e(TAG, "postAutograph:签名文件名称 " + name);

//            if (sketchViewSampling.getRecordCount() != 0) {
//                name = autographId + "Sampling";
//                new SaveToFileTask().execute(name);
//                autographType = AutographType.sampling;
//            }
//            if (sketchViewCheck.getRecordCount() != 0) {
//                name = autographId + "Check";
//                new SaveToFileTask().execute(name);
//                autographType = AutographType.check;
//            }
//            if (sketchViewExamine.getRecordCount() != 0) {
//                name = autographId + "Examine";
//                new SaveToFileTask().execute(name);
//                autographType = AutographType.examine;
//            }
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
            return saveInOI(sketchView.getResultBitmap(), autographPath, photoName[0], 80);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file == null) return;
            if (file.exists()) {
                pngAutographPath = file.getPath();
                autographName = file.getName();
//                switch (autographType) {
//                    case check:
//                        pngCheckPath = file.getPath();
//                        break;
//                    case examine:
//                        pngExaminePath = file.getPath();
//                        break;
//                    case sampling:
//                        pngSamplingPath = file.getPath();
//                        break;
//                }
                Toast.makeText(AutographActivity.this, "成功保存", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AutographActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
            }
            closeLoadingDialog();
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
        intent.putExtra(INTENT_CAN_CHANGE, isCanChange);
//        intent.putExtra(INTENT_SAMPLING_PATH, pngSamplingPath);
//        intent.putExtra(INTENT_EXAMINE_PATH, pngExaminePath);
//        intent.putExtra(INTENT_CHECK_PATH, pngCheckPath);
        intent.putExtra(INTENT_AUTOGRAPH_PATH, pngAutographPath);
        setResult(RESULT_OK, intent);
        finish();
    }
}
