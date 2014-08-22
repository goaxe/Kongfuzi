package com.kongfuzi.teacher.welcome;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.internal.Constants;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeFragment1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_welcome_1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      LinearLayout ll = (LinearLayout) view.findViewById(R.id.welcome);
      int position = this.getArguments().getInt(Constants.POS);
      switch (position) {
        case 0:
          ll.setBackgroundResource(R.drawable.welcome_1);
          break;
        case 1:
          ll.setBackgroundResource(R.drawable.welcome_2);
          break;
      }
    }
  }
