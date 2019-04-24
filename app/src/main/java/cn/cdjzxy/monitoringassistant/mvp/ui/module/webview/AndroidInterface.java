package cn.cdjzxy.monitoringassistant.mvp.ui.module.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.fanneng.android.web.SuperWebX5;

/**
 * describe：公共加载fragment类
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 * email：luyufengc@enn.cn
 */
public class AndroidInterface {
    public interface Back {
        void onBack();
    }

    public Back back;

    public AndroidInterface(Back back) {
        this.back = back;
    }

    @JavascriptInterface
    public void onBack() {
        if (back != null)
            back.onBack();
    }

}
