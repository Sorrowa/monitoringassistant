package cn.cdjzxy.monitoringassistant.mvp.ui.module.progress;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;

/**
 * 进度
 */

public class ProgressFragment extends BaseFragment {

    // 模板文集地址
    private static final String demoPath = Constant.FILE_DIR + "/test.doc";
    // 创建生成的文件地址
    private static final String newPath  = Constant.FILE_DIR + "/testS.doc";


    Unbinder unbinder;

    public ProgressFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, null);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btn_test)
    public void onClick() {
        try {
            InputStream inputStream = getContext().getAssets().open("precipitation_sampling.doc");
            copyFile(inputStream, new File((demoPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        doScan();
    }


    private void doScan() {
        //获取模板文件
        File demoFile = new File(demoPath);
        //创建生成的文件
        File newFile = new File(newPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("$QYMC$", "xxx科技股份有限公司");
        map.put("$QYDZ$", "上海市杨浦区xx路xx号");
        map.put("$QYFZR$", "张三");
        map.put("$FRDB$", "李四");
        map.put("$CJSJ$", "2000-11-10");
        map.put("$SCPZMSJWT$", "5");
        map.put("$XCJCJBQ$", "6");
        writeDoc(demoFile, newFile, map);
        //查看
        doOpenWord();
    }

    /**
     * 调用手机中安装的可打开word的软件
     */
    private void doOpenWord() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String fileMimeType = "application/msword";
        intent.setDataAndType(Uri.fromFile(new File(newPath)), fileMimeType);
        try {
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "未找到软件", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
    public void writeDoc(File demoFile, File newFile, Map<String, String> map) {
        try {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            Range range = hdt.getRange();// 读取word文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());  // 替换文本内容
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            out.write(ostream.toByteArray()); // 输出字节流
            out.close();
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 复制文件
     *
     * @param input
     * @param dest
     * @throws IOException
     */
    private static void copyFile(InputStream input, File dest) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

}
