package com.zitzhen.coco_community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.github_login_button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {f
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_login)

        // 设置按钮点击事件监听器
        github_login_button.setOnClickListener {
            // 使用MaterialAlertDialogBuilder来创建对话框
            MaterialAlertDialogBuilder(this)
                .setTitle("GitHub登录")
                .setMessage("您确定要使用GitHub登录吗？")
                .setPositiveButton("确定") { dialog, which ->
                    // 在这里添加您想要执行的登录逻辑
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss() // 取消对话框
                }
                .show()
        }
    }
}
