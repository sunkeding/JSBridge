package com.example.androidwebstudy;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    private static final String TAG = "MyWebViewClient";
    private MyWebView webView;

    public MyWebViewClient(MyWebView webView) {
        this.webView = webView;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        LogUtil.d(TAG, "onLoadResource:" + url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtil.d(TAG, "onPageStarted:" + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtil.d(TAG, "onPageFinished:" + url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        LogUtil.d(TAG, "shouldOverrideUrlLoading:" + request.toString());
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        LogUtil.d(TAG, "shouldInterceptRequest:" + request.toString());
        return super.shouldInterceptRequest(view, request);
    }


    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        LogUtil.d(TAG, "shouldOverrideKeyEvent:" + event.toString());
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        LogUtil.d(TAG, "onReceivedError:" + request.toString() + "," + error.toString());
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        LogUtil.d(TAG, "onReceivedHttpError:" + request.toString());
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        LogUtil.d(TAG, "onReceivedSslError:" + error.toString());
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
        LogUtil.d(TAG, "onReceivedHttpAuthRequest:" + host);
    }
}
