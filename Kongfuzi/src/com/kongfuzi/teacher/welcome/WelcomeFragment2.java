package com.kongfuzi.teacher.welcome;


import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;

import com.kongfuzi.teacher.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment2 extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_welcome_2);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LinearLayout ll = (LinearLayout) view.findViewById(R.id.welcome);
    ll.setBackgroundResource(R.drawable.welcome_3);
  }
}

