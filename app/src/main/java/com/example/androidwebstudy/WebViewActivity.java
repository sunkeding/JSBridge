package com.example.androidwebstudy;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class WebViewActivity extends AppCompatActivity {
    private MyWebView webView;
    private EditText editText;
    private Button showBtn;
    private Button showBtn2;
    private WebViewActivity self = this;
    private NativeSDK nativeSDK = new NativeSDK(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initWebView();
        initEvent();
    }

    private void initEvent() {
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputValue = editText.getText().toString();
                self.showWebDialog(inputValue);
            }
        });

        showBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativeSDK.getWebEditTextValue(new Callback() {
                    @Override
                    public void invoke(String value) {
                        new AlertDialog.Builder(self).setMessage("Web 输入框的值：" + value).create().show();
                    }
                });
            }
        });

    }

    private void initView() {
        webView = findViewById(R.id.webView);
        editText = findViewById(R.id.editText);
        showBtn = findViewById(R.id.showBtn);
        showBtn2 = findViewById(R.id.showBtn2);
    }

    private void initWebView() {

        webView.addJavascriptInterface(new NativeBridge(this, nativeSDK), "NativeBridge");
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        webView.loadUrl("file:///android_asset/index.html");

    }

    private void showWebDialog(String text) {
        String jsCode = String.format("window.showWebDialog('%s')", text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jsCode, null);
        }
    }

    interface Callback {
        void invoke(String value);
    }

    class NativeSDK {
        private Context ctx;
        private int id = 1;
        private HashMap<Integer, Callback> callbackMap = new HashMap<>();

        NativeSDK(Context ctx) {

        }

        public void getWebEditTextValue(Callback callback) {
            int callbackId = id++;
            callbackMap.put(callbackId, callback);
            String jsCode = String.format("window.getWebEditTextValue(%s)", callbackId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                self.webView.evaluateJavascript(jsCode, null);
            }
        }

        public void receivedMessage(int callbackId, String value) {
            if (callbackMap.containsKey(callbackId)) {
                // run on ui thread?
                callbackMap.get(callbackId).invoke(value);
            }
        }
    }

    class NativeBridge {
        private Context ctx;
        private NativeSDK nativeSDK;

        NativeBridge(Context ctx, NativeSDK nativeSDK) {
            this.ctx = ctx;
            this.nativeSDK = nativeSDK;
        }

        @JavascriptInterface
        public void showNativeDialog(String text) {
            new AlertDialog.Builder(ctx).setMessage(text).create().show();
        }

        @JavascriptInterface
        public void getNativeEditTextValue(int callbackId) {
            final WebViewActivity mainActivity = (WebViewActivity) ctx;
            String value = mainActivity.editText.getText().toString();
            final String jsCode = String.format("window.JSSDK.receivedMessage(%s, '%s')", callbackId, value);
            // 在 UI 线程更新 UI
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        mainActivity.webView.evaluateJavascript(jsCode, null);
                    }
                }
            });
        }

        @JavascriptInterface
        public void receivedMessage(int callbackId, String value) {
            if (nativeSDK.callbackMap.containsKey(callbackId)) {
                // run on ui thread?
                nativeSDK.callbackMap.get(callbackId).invoke(value);
            }
        }
    }
}
