package com.example.doumi;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageView walkitalkiButton;
    private SpeechRecognizer speechRecognizer;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        databaseReference = FirebaseDatabase.getInstance().getReference("actions");

        backButton = findViewById(R.id.back_button);
        walkitalkiButton = findViewById(R.id.walkitalki_button);

        backButton.setOnClickListener(v -> onBackPressed());

        walkitalkiButton.setOnClickListener(v -> {
            showToastMessage();  // Custom Toast message
            startSpeechRecognition();  // Start speech recognition
        });
    }

    private void showToastMessage() {
        // Inflate the custom layout for the Toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        // Set the text for the Toast
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText("해당 목적지로 안내하겠습니다");

        // Create and show the Toast
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void startSpeechRecognition() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(UserActivity.this, "음성 인식 시작", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null) {
                        for (String result : matches) {
                            if (result.contains("화장실")) {
                                fetchActionFromFirebase("화장실");
                                break;
                            }
                            else if (result.contains("강의실")) {
                                fetchActionFromFirebase("강의실");
                                break;
                            }
                            else if (result.contains("물품보관함")) {
                                fetchActionFromFirebase("물품보관함");
                                break;
                            }
                        }
                    }
                }

                @Override public void onError(int error) { /* Handle errors */ }
                @Override public void onBeginningOfSpeech() { /* Handle beginning of speech */ }
                @Override public void onEndOfSpeech() { /* Handle end of speech */ }
                @Override public void onBufferReceived(byte[] buffer) { /* Handle buffer received */ }
                @Override public void onEvent(int eventType, Bundle params) { /* Handle events */ }
                @Override public void onPartialResults(Bundle partialResults) { /* Handle partial results */ }
                @Override public void onRmsChanged(float rmsdB) { /* Handle RMS changes */ }
            });
            speechRecognizer.startListening(intent);
        } else {
            Toast.makeText(this, "음성 인식을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchActionFromFirebase(String key) {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String action = snapshot.getValue(String.class);
                    if (action != null) {
                        // 로봇 액션
                        handleAction(action);
                    }
                } else {
                    Toast.makeText(UserActivity.this, "해당 액션을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAction(String action) {
        Toast.makeText(UserActivity.this, "서버에서 받은 액션: " + action, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
