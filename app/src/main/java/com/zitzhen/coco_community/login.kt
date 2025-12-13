package com.zitzhen.coco_community

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val GITHUB_OAUTH_URL = "https://github.com/login/oauth/authorize?client_id=Ov23lii4E31EzV9VMW7B&redirect_uri=https://cc.zitzhen.cn/auth/github?client=mobile"
        private const val REDIRECT_URI = "https://cc.zitzhen.cn/auth/github"
    }

    private lateinit var mainLayout: LinearLayout
    private lateinit var webView: WebView
    private lateinit var githubLoginButton: Button
    private lateinit var returnButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_login)

        // 初始化视图
        initViews()
        
        // 设置按钮点击事件
        setupClickListeners()
        
        // 初始化WebView
        initWebView()
    }

    private fun initViews() {
        mainLayout = findViewById(R.id.main_layout)
        webView = findViewById(R.id.webview)
        githubLoginButton = findViewById(R.id.github_login_button)
        returnButton = findViewById(R.id.retun)
    }

    private fun setupClickListeners() {
        // 设置GitHub登录按钮点击事件监听器
        githubLoginButton.setOnClickListener {
            startOAuthFlow()
        }
        
        // 设置返回按钮点击事件
        returnButton.setOnClickListener {
            finish() // 关闭当前Activity
        }
    }

    private fun initWebView() {
        // 配置WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        
        // 设置WebViewClient来监听页面加载和重定向
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { 
                    Log.d(TAG, "WebView loading URL: $url")
                    
                    // 检查是否是OAuth回调URL
                    if (url.startsWith(REDIRECT_URI)) {
                        // 处理OAuth回调
                        processOAuthCallback(url)
                        return true // 阻止WebView继续加载
                    }
                }
                return false // 允许WebView加载其他URL
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(TAG, "Page finished loading: $url")
            }
        }
    }

    private fun startOAuthFlow() {
        // 隐藏主界面，显示WebView
        mainLayout.visibility = View.GONE
        webView.visibility = View.VISIBLE
        
        // 加载GitHub OAuth授权页面
        webView.loadUrl(GITHUB_OAUTH_URL)
        Log.d(TAG, "开始OAuth流程，加载URL: $GITHUB_OAUTH_URL")
    }

    private fun processOAuthCallback(url: String) {
        try {
            // 解析URL获取查询参数
            val uri = android.net.Uri.parse(url)
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")
            val errorDescription = uri.getQueryParameter("error_description")

            if (error != null) {
                // 处理错误情况
                Log.e(TAG, "OAuth error: $error - $errorDescription")
                showError("登录失败: $error")
                return
            }

            if (code != null) {
                // 成功获取授权码，构建JSON响应
                val responseJson = JSONObject().apply {
                    put("code", code)
                    put("status", "success")
                    put("message", "授权成功")
                }

                Log.d(TAG, "OAuth成功，授权码: $code")
                Log.d(TAG, "返回的JSON: ${responseJson.toString()}")

                // 显示成功消息并返回主界面
                showSuccess("GitHub登录成功！")
                
                // 这里可以添加后续逻辑，比如将授权码发送到服务器获取access token
                // 或者跳转到主页面
            } else {
                Log.e(TAG, "未收到授权码")
                showError("登录失败：未收到授权信息")
            }
        } catch (e: Exception) {
            Log.e(TAG, "处理OAuth回调时出错", e)
            showError("登录失败：处理错误")
        }
    }

    private fun showSuccess(message: String) {
        // 隐藏WebView，显示主界面
        webView.visibility = View.GONE
        mainLayout.visibility = View.VISIBLE
        
        // 显示成功消息
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        
        // 这里可以添加后续逻辑，比如跳转到主页面
        // val intent = Intent(this, MainActivity::class.java)
        // startActivity(intent)
        // finish()
    }

    private fun showError(message: String) {
        // 隐藏WebView，显示主界面
        webView.visibility = View.GONE
        mainLayout.visibility = View.VISIBLE
        
        // 显示错误消息
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        // 如果WebView可见，按返回键时返回WebView的上一个页面或隐藏WebView
        if (webView.visibility == View.VISIBLE) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                // 如果WebView没有历史记录，隐藏WebView显示主界面
                webView.visibility = View.GONE
                mainLayout.visibility = View.VISIBLE
            }
        } else {
            super.onBackPressed()
        }
    }
}
