package com.example.androidwebstudy;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyWebView extends WebView {
    public MyWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WebSettings webSettings = this.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        StringBuilder var4 = new StringBuilder(webSettings.getUserAgentString());

        webSettings.setUserAgentString(var4.toString());
        if (Build.VERSION.SDK_INT < 18) {
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
        }
        // 移除不安全的接口
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            this.removeJavascriptInterface("searchBoxJavaBridge_");
            this.removeJavascriptInterface("accessibilityTraversal");
            this.removeJavascriptInterface("accessibility");
        }
        this.setWebViewClient(new MyWebViewClient(this));
        this.setWebChromeClient(new MyWebChromeClient(this));
    }
}
