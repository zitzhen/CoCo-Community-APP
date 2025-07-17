import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final String USER_AGREEMENT_URL = "https://cc.zitzhen.cn/agreement/useragreement/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        String fullText = "继续代表您同意我们的《用户协议》";

        // 创建可点击的文本
        SpannableString spannableString = new SpannableString(fullText);

        // 找到《用户协议》在文本中的位置
        int start = fullText.indexOf("《");
        int end = fullText.indexOf("》") + 1;

        // 创建可点击区域
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 使用隐式Intent打开默认浏览器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(USER_AGREEMENT_URL));

                // 验证是否有浏览器可以处理这个Intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        };

        // 将可点击区域应用到文本
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置TextView
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // 使点击生效
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent)); // 可选：移除点击高亮
    }
}