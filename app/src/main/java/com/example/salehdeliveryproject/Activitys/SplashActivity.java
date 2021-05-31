package com.example.salehdeliveryproject.Activitys;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.salehdeliveryproject.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends Activity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,LoginUserActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
*/

        EasySplashScreen config = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(SliderActivity.class)
                .withSplashTimeOut(SPLASH_DISPLAY_LENGTH)
                .withAfterLogoText("Loading...")
                .withLogo(R.drawable.logo_);

        config.getAfterLogoTextView().setTextColor(Color.BLACK);
        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}