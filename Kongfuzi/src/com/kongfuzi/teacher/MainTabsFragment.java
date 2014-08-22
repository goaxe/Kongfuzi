package com.kongfuzi.teacher;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.TabSwipeFragment;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.fragments.AttendanceFragment;
import com.kongfuzi.teacher.fragments.DianpingFragment;
import com.kongfuzi.teacher.fragments.HomeworkFragment;
import com.kongfuzi.teacher.fragments.MarkFragment;
import com.kongfuzi.teacher.fragments.SettingsFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainTabsFragment extends TabSwipeFragment {
  private static String[] tabsName = null;

  protected void loadTabsName() {
    if (tabsName == null)
      tabsName = getResources().getStringArray(R.array.tab_titles);
  }

  @Override
  public void onHandleTabs() {
    loadTabsName();
    Log.e("MainTabsFragment", "Add tabs");
    addTab(tabsName[0], AttendanceFragment.class);
    addTab(tabsName[1], MarkFragment.class);
    addTab(tabsName[2], HomeworkFragment.class);
    addTab(tabsName[3], DianpingFragment.class);
    addTab(tabsName[4], SettingsFragment.class);

    addonTabber().mController.mViewPager.setOffscreenPageLimit(3);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setOnTabSelectedListener(new OnTabSelectedListener() {
      @Override
      public void onTabSelected(int position) {
        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
      }
    });
  }
}