package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

/**
 * 打印预览
 */

public class PreviewPrintActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.web_preview)
    WebView web_preview;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("表单预览");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_preview_print;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        //String htmlCachePath = getDiskDirPath(this, "preview");
        String htmlCachePath = "/mnt/sdcard/documents/preview";
        if (!new File(htmlCachePath).exists()) {
            new File(htmlCachePath).mkdirs();
        }
        if (!new File(htmlCachePath).exists()) {
            new File(htmlCachePath).mkdirs();
        }

        String htmlCacheFileName = htmlCachePath + File.separator + "preview.html";
        try {
            convert2Html("wastewater_sampling.doc", htmlCacheFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebSettings webSettings = web_preview.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        web_preview.loadUrl("file:/" + htmlCacheFileName);


    }


    private File getDiskCacheDir(Context context, String dirName) {
        boolean isExternalStorageEnable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String fileCachePath;
        if (isExternalStorageEnable) {
            fileCachePath = context.getExternalCacheDir().getPath();
        } else {
            fileCachePath = context.getCacheDir().getPath();
        }
        return new File(fileCachePath + File.separator + dirName);
    }

    private String getDiskDirPath(Context context, String dirName) {
        boolean isExternalStorageEnable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String fileCachePath;
        if (isExternalStorageEnable) {
            fileCachePath = context.getExternalCacheDir().getPath();
        } else {
            fileCachePath = context.getCacheDir().getPath();
        }
        return fileCachePath + File.separator + dirName;
    }

    private String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void convert2Html(String fileName, String outPutFile)
            throws TransformerException, IOException,
            ParserConfigurationException {
        //HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
        HWPFDocument wordDocument = new HWPFDocument(getResources().getAssets().open(fileName));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        /*
        //设置图片路径
        wordToHtmlConverter.setPicturesManager(new PicturesManager()
        {
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches )
            {
                String name = docName.substring(0,docName.indexOf("."));
                return name+"/"+suggestedName;
            }
        } );

        //保存图片
        List<Picture> pics=wordDocument.getPicturesTable().getAllPictures();
        if(pics!=null){
            for(int i=0;i<pics.size();i++){
                Picture pic = (Picture)pics.get(i);
                System.out.println( pic.suggestFullFileName());
                try {
                    String name = docName.substring(0,docName.indexOf("."));
                    pic.writeImageContent(new FileOutputStream(savePath+ name + "/"
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        */
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        //保存html文件
        writeFile(new String(out.toByteArray()), outPutFile);
    }

    /**
     * 将html文件保存到sd卡
     */
    public void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }


}
