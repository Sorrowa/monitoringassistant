package cn.cdjzxy.monitoringassistant.mvp.ui.module.webview;

import android.webkit.JavascriptInterface;

public interface WebViewJavaScriptFunction {
    @JavascriptInterface
    void onBack();
}
