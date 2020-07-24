package com.example.androidwebstudy;

import android.os.Build;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class MyWebChromeClient extends WebChromeClient {
    private static final String TAG = "MyWebChromeClient";
    /**
     * 是否注入了Js文件
     */
    private boolean hasInject = false;
    private MyWebView webView;

    public MyWebChromeClient(MyWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogUtil.d(TAG, "onJsAlert->url:" + url);
        LogUtil.d(TAG, "onJsAlert->message:" + message);
        LogUtil.d(TAG, "onJsAlert->result:" + result);
        LogUtil.d(TAG, "onJsAlert->threadName:" + Thread.currentThread().getName());
        LogUtil.d(TAG, "onJsAlert->threadId:" + Thread.currentThread().getId());
        result.confirm();
//                return true;
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        LogUtil.d(TAG, "onJsConfirm->url:" + url);
        LogUtil.d(TAG, "onJsConfirm->message:" + message);
        LogUtil.d(TAG, "onJsConfirm->result:" + result);
        LogUtil.d(TAG, "onJsConfirm->threadName:" + Thread.currentThread().getName());
        LogUtil.d(TAG, "onJsConfirm->threadId:" + Thread.currentThread().getId());
        //表示手动帮忙选择取消按钮
//                result.cancel();
        //表示手动帮忙选择确定按钮
        result.confirm();
        return true;
//                return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        LogUtil.d(TAG, "onJsPrompt->url:" + url);
        LogUtil.d(TAG, "onJsPrompt->message:" + message);
        LogUtil.d(TAG, "onJsPrompt->defaultValue:" + defaultValue);
        LogUtil.d(TAG, "ronJsPrompt->result:" + result);
        LogUtil.d(TAG, "onJsPrompt->threadName:" + Thread.currentThread().getName());
        LogUtil.d(TAG, "onJsPrompt->threadId:" + Thread.currentThread().getId());
        result.confirm("onJsPrompt->native666");
        return true;
//                return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        try {
            LogUtil.d(TAG, "onConsoleMessage->message:" + consoleMessage.message());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        LogUtil.d(TAG, "onProgressChanged:" + newProgress);
        if (newProgress < 25) {
            if (this.hasInject) {
                this.hasInject = false;
            }
        } else if (!this.hasInject) {
//            com.didi.onehybrid.b.b.a(var1, "fusion/didibridge4.js");
            InputStream is = null;
            try {
                is = webView.getContext().getAssets().open("didibridge4.js");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String js = new String(buffer);

                this.hasInject = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void executeJavascript(String script) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtil.d(TAG, "onReceiveValue:" + value);
                }
            });
        } else {
            webView.loadUrl("javascript:" + script);
        }
    }
}
