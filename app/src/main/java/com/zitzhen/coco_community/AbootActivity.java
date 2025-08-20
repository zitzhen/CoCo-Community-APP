package com.zitzhen.coco_community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AbootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 传递Activity，使用aboot.xml布局文件
        setContentView(R.layout.aboot);

        // 获取aboot.xml布局中的按钮
        Button agreementButton = findViewById(R.id.btn_user_agreement);
        Button privacyButton = findViewById(R.btn_privacy_policy);
        Button CheckupdateButton = findViewById(R.btn_Check_update);

        // 给按钮设置点击动作
        agreementButton.setOnClickListener(view -> {
            // 创建打开浏览器的意图
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cc.zitzhen.cn/agreement/useragreement/"));
            startActivity(browserIntent);
        });

        privacyButton.setOnClickListener(view -> {
            // 创建打开浏览器的意图
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cc.zitzhen.cn/agreement/privacypolicy/"));
            startActivity(browserIntent);
        });

        CheckupdateButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("自动更新")
                    .setMessage("自动更新已被禁止")
                    .setPositiveButton("确定", null)
                    .show();
        });

        // 后续在此新增代码...
    }
}
