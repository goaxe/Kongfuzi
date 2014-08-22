package com.kongfuzi.teacher;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

import com.kongfuzi.teacher.welcome.WelcomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
  public Context mContext;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash);
    mContext = this;
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
      }

    }, 2500);

  }
  
  @Override
  public void onResume() {
    super.onResume();
    JPushInterface.onResume(mContext);
  }
  
  @Override
  public void onPause() {
    super.onPause();
    JPushInterface.onPause(mContext);
  }
}
