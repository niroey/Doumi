package com.example.doumi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProtecterActivity extends AppCompatActivity {

    private ImageButton cameraButton;
    private ImageButton mapButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protecter);

        // UI 요소들 초기화
        cameraButton = findViewById(R.id.camera_button);
        mapButton = findViewById(R.id.map_button);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        // 버튼 클릭 이벤트 설정
        cameraButton.setOnClickListener(v -> openCameraActivity());
        mapButton.setOnClickListener(v -> openMapActivity());
    }

    private void openCameraActivity() {
        Intent intent = new Intent(ProtecterActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void openMapActivity() {
        Intent intent = new Intent(ProtecterActivity.this, MapActivity.class);
        startActivity(intent);
    }
}