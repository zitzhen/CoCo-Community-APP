package com.zitzhen.coco_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 跳转到登录页面
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        
        // 结束MainActivity，这样用户按下返回键时不会回到空白的MainActivity
        finish()
    }
}