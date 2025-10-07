package com.zitzhen.coco_community;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AbootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 传递Activity，使用aboot.xml布局文件
        setContentView(R.layout.aboot);

        // 获取aboot.xml布局中的按钮
        Button agreementButton = findViewById(R.id.btn_user_agreement);
        Button privacyButton = findViewById(R.id.btn_privacy_policy);
        Button CheckupdateButton = findViewById(R.id.btn_Check_update);

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
            checkUpdate();
        });

        // 后续在此新增代码...
    }

    private void checkUpdate() {
        new Thread(() -> {
            try {
                String result = sendGetRequest("https://api.zitzhen.cn/Issue_number/app/cczit/inquire/");
                JSONObject json = new JSONObject(result);

                // 读取版本
                boolean success = json.getBoolean("success");
                if (!success) {
                    runOnUiThread(() -> new MaterialAlertDialogBuilder(AbootActivity.this)
                            .setTitle("自动更新")
                            .setMessage("抱歉，我们的服务器出来了点问题，暂时无法获取")
                            .setPositiveButton("确定", null)
                            .show());
                } else {
                    int serverVersionCode = json.getInt("data"); // 假设服务器返回的是版本号
                    
                    // 获取当前应用的versionCode
                    int currentVersionCode = getCurrentVersionCode();
                    
                    runOnUiThread(() -> {
                        if (serverVersionCode > currentVersionCode) {
                            // 服务器版本更高，提示更新
                            new MaterialAlertDialogBuilder(AbootActivity.this)
                                    .setTitle("发现新版本")
                                    .setMessage("有新版本可用，请更新以获得更好的体验")
                                    .setNegativeButton("稍后再说", null)
                                    .show();
                        } else {
                            // 已经是最新版本
                            new MaterialAlertDialogBuilder(AbootActivity.this)
                                    .setTitle("检查更新")
                                    .setMessage("当前已是最新版本")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> new MaterialAlertDialogBuilder(AbootActivity.this)
                        .setTitle("自动更新")
                        .setMessage("检查更新失败: " + e.getMessage())
                        .setPositiveButton("确定", null)
                        .show());
            }
        }).start();
    }

    private int getCurrentVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String sendGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false}";
        }
    }
}