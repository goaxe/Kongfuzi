package com.kongfuzi.teacher;

import org.holoeverywhere.app.Activity;

import cn.jpush.android.api.JPushInterface;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.activities.QueryClassesListActivity;
import com.kongfuzi.teacher.internal.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
public class MainActivity extends Activity {
  public static final String DEBUG_TAG = "MainActivity";
  public static final String MESSAGE_RECEIVED_ACTION = "com.kongfuzi.teacher.MESSAGE_RECEIVED_ACTION";
  public static Boolean isForeground = false;
  private MainTabsFragment mTabs;
  private MessageReceiver mMessageReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_main);
    JPushInterface.init(getApplicationContext());
    mTabs = (MainTabsFragment) getSupportFragmentManager().findFragmentById(R.id.main_tabs_fragment);
    registerMessageReceiver();
  }

  public void registerMessageReceiver() {
    mMessageReceiver = new MessageReceiver();
    IntentFilter filter = new IntentFilter();
    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
    filter.addAction(MESSAGE_RECEIVED_ACTION);
    registerReceiver(mMessageReceiver, filter);
  }

  @Override
  public void onDestroy() {
    unregisterReceiver(mMessageReceiver);
    super.onDestroy();
  }

  @Override
  public void onResume() {
    isForeground = true;
    super.onResume();
  }

  @Override
  protected void onPause() {
    isForeground = false;
    super.onPause();
  }

  public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      Log.e("mainactivity", "1");
      if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
        Log.e("mainactivity", "2");

        // textView.setText("" + getIntent().getIntExtra("huangjian", 0));

        String messge = intent.getStringExtra(Constants.KEY_MESSAGE);
//        String extras = intent.getStringExtra(Constants.KEY_EXTRAS);
        StringBuilder showMsg = new StringBuilder();
        showMsg.append(Constants.KEY_MESSAGE + " : " + messge + "\n");
        Log.e(DEBUG_TAG, showMsg.toString());
        /*
         * if (!ExampleUtil.isEmpty(extras)) { showMsg.append(KEY_EXTRAS + " : "
         * + extras + "\n"); } setCostomMsg(showMsg.toString());
         */

      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem menuItem = menu.add("添加作业");
    menuItem.setIcon(R.drawable.add_btn);
    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle().equals("添加作业")) {
      mTabs.setCurrentTab(2);
      Intent intent = new Intent(this, QueryClassesListActivity.class);
      startActivity(intent);
    }
    return true;
  }

}
