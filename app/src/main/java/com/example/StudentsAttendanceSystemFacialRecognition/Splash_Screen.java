package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginint = new Intent(Splash_Screen.this,LoginActivity.class);
                startActivity(loginint);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
