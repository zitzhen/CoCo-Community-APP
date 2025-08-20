package com.example.coco_community;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private View errorLayout;
    private MaterialButton retryButton;
    private TextView errorCodeTextView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建帧布局作为根容器
        ConstraintLayout rootLayout = new ConstraintLayout(this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        rootLayout.setLayoutParams(layoutParams);
        setContentView(rootLayout);

        // 创建WebView
        webView = new WebView(this);
        webView.setId(View.generateViewId());
        ConstraintLayout.LayoutParams webViewLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        webView.setLayoutParams(webViewLayoutParams);
        rootLayout.addView(webView);

        // 创建错误布局 - 使用正确的资源ID
        errorLayout = getLayoutInflater().inflate(getResources().getLayout(R.layout.error), rootLayout, false);
        errorLayout.setId(View.generateViewId());
        errorLayout.setVisibility(View.GONE);
        rootLayout.addView(errorLayout);

        // 获取错误页面组件 - 使用正确的资源ID
        retryButton = errorLayout.findViewById(getResources().getIdentifier("retryButton", "id", getPackageName()));
        errorCodeTextView = errorLayout.findViewById(getResources().getIdentifier("errorcode", "id", getPackageName()));

        // 设置重试按钮点击事件
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏错误页面，显示WebView
                errorLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                // 重新加载网页
                webView.reload();
            }
        });

        // WebView基础配置
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);

        // 设置自定义WebViewClient处理错误
        webView.setWebViewClient(new WebViewClient() {
            // 处理API 23+的错误
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request != null && request.isForMainFrame()) {
                    showErrorPage(error != null ? error.getErrorCode() : -1,
                            error != null ? error.getDescription().toString() : "Unknown error");
                }
            }

            // 处理旧版本错误
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showErrorPage(errorCode, description != null ? description : "Unknown error");
            }

            // 处理HTTP错误 (状态码 >= 400)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, android.webkit.WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (request != null && request.isForMainFrame()) {
                    int statusCode = errorResponse != null ? errorResponse.getStatusCode() : -1;
                    if (statusCode < 200 || statusCode >= 300) {
                        showErrorPage(statusCode, "HTTP Error: " + statusCode);
                    }
                }
            }

            // 所有链接在WebView内部打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 加载网页
        webView.loadUrl("https://cc.zitzhen.cn/");
    }

    private void showErrorPage(final int errorCode, final String errorDescription) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在错误页面上显示错误代码
                errorCodeTextView.setText("错误代码: " + errorCode + "\n" + errorDescription);

                // 记录错误日志
                Log.e("WebViewError", "Error " + errorCode + ": " + errorDescription);

                // 显示错误页面，隐藏WebView
                webView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    // 处理返回键
    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}