package com.example.doumi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView serviceText;
    private ImageButton button1;
    private ImageButton button2;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 요소들 초기화
        logo = findViewById(R.id.logo);
        serviceText = findViewById(R.id.service_text);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // 버튼 클릭 이벤트 설정
        button1.setOnClickListener(v -> openUserActivity());
        button2.setOnClickListener(v -> openProtecterActivity());
    }

    private void openUserActivity() {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    private void openProtecterActivity() {
        Intent intent = new Intent(MainActivity.this, ProtecterActivity.class);
        startActivity(intent);
    }


}