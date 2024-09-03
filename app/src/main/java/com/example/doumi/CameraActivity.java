package com.example.doumi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView backButton;
    private static final String TAG = "CameraActivity";

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera); // 변경: activity_main -> activity_camera

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        webView = findViewById(R.id.webview);
        if (webView == null) {
            Log.e(TAG, "WebView is null. Check your XML layout file.");
            return;
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "Page loading started: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "Page loading finished: " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "Page load error: " + description);
            }
        });

        // Flask 서버 URL
        webView.loadUrl("http://192.168.219.108:5000/"); // 실제 서버 IP 주소 사용
    }
}
