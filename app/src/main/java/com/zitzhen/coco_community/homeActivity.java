package com.zitzhen.coco_community;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;

public class homeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用 home.xml 布局文件
        setContentView(R.layout.home);

        Button publishWidgetButton = findViewById(R.id.publishWidgetButton);
        Button publishArticleButton = findViewById(R.id.publishArticleButton);

        publishArticleButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("发布文章")
                    .setMessage("文章功能正在制作中")
                    .setPositiveButton("确定", null)
                    .show();
        });

        publishWidgetButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("发布控件")
                    .setMessage("发布控件功能正在制作中")
                    .setPositiveButton("确定", null)
                    .show();
        });
    }
}