package com.minichain.minichain;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//SplashScreen Activity displaying the App Logo at the launch

public class WelcomeScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(WelcomeScreen.this, HomeScreen.class);
                startActivity(main);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
