package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kongfuzi.student.fragments.DaxueFragment;
import com.kongfuzi.student.fragments.HomeFragment;
import com.kongfuzi.student.fragments.JiaowuFragment;
import com.kongfuzi.student.fragments.JigouFragment;
import com.kongfuzi.student.fragments.MyDaxueFragment;
import com.kongfuzi.student.fragments.MyGuanzhuFragment;
import com.kongfuzi.student.fragments.MyHomeFragment;
import com.kongfuzi.student.fragments.MyJigouFragment;
import com.kongfuzi.student.fragments.MyKechengFragment;
import com.kongfuzi.student.fragments.SettingFragment;
import com.kongfuzi.student.internal.Constants;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends Activity implements NavigationSlidingMenu.Callbacks {
  public static final String DEBUG_TAG = "MainActivity";
  
  private Activity mActivity;
  private ActionBar mActionBar;
  private NavigationSlidingMenu mNavigationSlidingMenu;
  
  
  private HomeFragment mHomeFragment;
  private DaxueFragment mDaxueFragment;
  private JigouFragment mJigouFragment;
  private JiaowuFragment mJiaowuFragment;
  private SettingFragment mSettingFragment;
  private MyHomeFragment mMyHomeFragment;
  private MyKechengFragment mMyKechengFragment;
  private MyDaxueFragment mMyDaxueFragment;
  private MyGuanzhuFragment mMyGuanzhuFragment;
  private MyJigouFragment mMyJigouFragment;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mActivity = this;
    mActionBar = getSupportActionBar();
    
    initActionbar();
    initFragments(savedInstanceState);
    initSlingMenu();
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mNavigationSlidingMenu.isMenuShowing()) {
      mNavigationSlidingMenu.showContent();
    } else {
      mNavigationSlidingMenu.showMenu();
    }
    return true;
    
  }
  
  public void initFragments(Bundle savedInstanceState) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    if (savedInstanceState != null) {
      mHomeFragment = (HomeFragment) fm.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
      mDaxueFragment = (DaxueFragment) fm.findFragmentByTag(Constants.DAXUE_FRAGMENT_TAG);
      mJigouFragment = (JigouFragment) fm.findFragmentByTag(Constants.JIGOU_FRAGMENT_TAG);
      mJiaowuFragment = (JiaowuFragment) fm.findFragmentByTag(Constants.JIAOWU_FRAGMENT_TAG);
      mSettingFragment = (SettingFragment) fm.findFragmentByTag(Constants.SETTING_FRAGMENT_TAG);
      mMyHomeFragment = (MyHomeFragment) fm.findFragmentByTag(Constants.MYHOME_FRAGMENT_TAG);
      mMyKechengFragment = (MyKechengFragment) fm.findFragmentByTag(Constants.MYKECHENG_FRAGMENT_TAG);
      mMyDaxueFragment = (MyDaxueFragment) fm.findFragmentByTag(Constants.MYDAXUE_FRAGMENT_TAG);
      mMyGuanzhuFragment = (MyGuanzhuFragment) fm.findFragmentByTag(Constants.MYGUANZHU_FRAGMENT_TAG);
      mMyJigouFragment = (MyJigouFragment) fm.findFragmentByTag(Constants.MYJIGOU_FRAGMENT_TAG);
    } else {
      mHomeFragment = new HomeFragment();
      ft.add(R.id.content_frame, mHomeFragment, Constants.HOME_FRAGMENT_TAG);
      mDaxueFragment = new DaxueFragment();
      ft.add(R.id.content_frame, mDaxueFragment, Constants.DAXUE_FRAGMENT_TAG);
      ft.detach(mDaxueFragment);
      mJigouFragment = new JigouFragment();
      ft.add(R.id.content_frame, mJigouFragment, Constants.JIGOU_FRAGMENT_TAG);
      ft.detach(mJigouFragment);
      mJiaowuFragment = new JiaowuFragment();
      ft.add(R.id.content_frame, mJiaowuFragment, Constants.JIAOWU_FRAGMENT_TAG);
      ft.detach(mJiaowuFragment);
      mSettingFragment = new SettingFragment();
      ft.add(R.id.content_frame, mSettingFragment, Constants.SETTING_FRAGMENT_TAG);
      ft.detach(mSettingFragment);
      mMyHomeFragment = new MyHomeFragment();
      ft.add(R.id.content_frame, mMyHomeFragment, Constants.MYHOME_FRAGMENT_TAG);
      ft.detach(mMyHomeFragment);
      mMyKechengFragment = new MyKechengFragment();
      ft.add(R.id.content_frame, mMyKechengFragment, Constants.MYKECHENG_FRAGMENT_TAG);
      ft.detach(mMyKechengFragment);
      mMyDaxueFragment = new MyDaxueFragment();
      ft.add(R.id.content_frame, mMyDaxueFragment, Constants.MYDAXUE_FRAGMENT_TAG);
      ft.detach(mMyDaxueFragment);
      mMyGuanzhuFragment = new MyGuanzhuFragment();
      ft.add(R.id.content_frame, mMyGuanzhuFragment, Constants.MYGUANZHU_FRAGMENT_TAG);
      ft.detach(mMyGuanzhuFragment);
      mMyJigouFragment = new MyJigouFragment();
      ft.add(R.id.content_frame, mMyJigouFragment, Constants.MYJIGOU_FRAGMENT_TAG);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
    }
  }
  
  public void initSlingMenu() {
    mNavigationSlidingMenu = new NavigationSlidingMenu(this, R.layout.sliding_menu_navigation_layout);
    mNavigationSlidingMenu.setMode(SlidingMenu.LEFT);
    mNavigationSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    mNavigationSlidingMenu.setShadowWidth(20);
    mNavigationSlidingMenu.setBehindWidth(300);
    mNavigationSlidingMenu.setFadeDegree(0.35f);
    mNavigationSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
  }
  
  public void initActionbar() {
    mActionBar = getSupportActionBar();
    //mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_cta_blue));
    mActionBar.setDisplayShowHomeEnabled(true);
    mActionBar.setHomeButtonEnabled(true);
    mActionBar.setIcon(R.drawable.sliding_menu_icon);
  }

  @Override
  public void onNavigationSlidingMenuItemClicked(int position) {
    // TODO Auto-generated method stub
    Log.e(DEBUG_TAG, "click " + position);
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    switch (position) {
    case 0:
      ft.attach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;
    case 1:
      ft.detach(mHomeFragment);
      ft.attach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 2:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.attach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 3:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.attach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 4:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.attach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 5:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.attach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 6:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.attach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 7:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.attach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;

    case 8:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.attach(mMyGuanzhuFragment);
      ft.detach(mMyJigouFragment);
      
      ft.commit();
      break;
    case 9:
      ft.detach(mHomeFragment);
      ft.detach(mDaxueFragment);
      ft.detach(mJigouFragment);
      ft.detach(mJiaowuFragment);
      ft.detach(mSettingFragment);
      ft.detach(mMyHomeFragment);
      ft.detach(mMyKechengFragment);
      ft.detach(mMyDaxueFragment);
      ft.detach(mMyGuanzhuFragment);
      ft.attach(mMyJigouFragment);
      
      ft.commit();
      break;

    default:
      break;
    }
    
    if (mNavigationSlidingMenu.isMenuShowing()) {
      mNavigationSlidingMenu.showContent();
    }
  }

    
}
