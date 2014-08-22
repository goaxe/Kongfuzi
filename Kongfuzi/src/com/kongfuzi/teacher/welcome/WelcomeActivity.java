package com.kongfuzi.teacher.welcome;

import android.os.Bundle;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.SplashActivity;
import com.kongfuzi.teacher.activities.LoginActivity;
import com.kongfuzi.teacher.internal.Constants;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class WelcomeActivity extends Activity {
  public static final String DEBUG_TAG = "WelcomeActivity";

  WelcomeFragmentAdapter mAdapter;
  ViewPager mPager;
  PageIndicator mIndicator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    this.getSupportActionBar().hide();

    String savedVersion = MainApplication.prefs.getString(Constants.VERSION, "");
    String currentVersion = "";
    try {
      currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    Log.e(DEBUG_TAG, savedVersion + "xxx" + currentVersion);
    
    if (savedVersion.equals(currentVersion)) {
      showLogin(null);
    }

    MainApplication.prefs.edit().putString(Constants.VERSION, currentVersion).commit();

    mAdapter = new WelcomeFragmentAdapter(getSupportFragmentManager());

    mPager = (ViewPager) findViewById(R.id.pager);
    mPager.setAdapter(mAdapter);

    mIndicator = (LinePageIndicator) findViewById(R.id.indicator);
    mIndicator.setViewPager(mPager);
  }

  public void showLogin(View view) {
    Intent intent = new Intent(this, LoginActivity.class);
    
    startActivity(intent);
    finish();
  }

  class WelcomeFragmentAdapter extends FragmentPagerAdapter {

    public WelcomeFragmentAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
      switch (pos) {
        case 0:
        case 1:
          Bundle bundle = new Bundle();
          bundle.putInt(Constants.POS, pos);
          return Fragment.instantiate(WelcomeFragment1.class, bundle);
        case 2:
          return Fragment.instantiate(WelcomeFragment2.class, null);
      }

      return null;
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}
