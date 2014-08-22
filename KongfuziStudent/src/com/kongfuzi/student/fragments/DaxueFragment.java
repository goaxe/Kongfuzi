package com.kongfuzi.student.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kongfuzi.student.R;
import com.kongfuzi.student.XuanDaxueByProvinceActivity;
import com.kongfuzi.student.XuanDaxueByScoreActivity;
import com.kongfuzi.student.XuanDaxueBySubjectActivity;

public class DaxueFragment extends Fragment {
  public static final String DEBUG_TAG = "DaxueFragment";

  private LinearLayout mScoreLinearLayout;
  private LinearLayout mSubjectLinearLayout;
  private LinearLayout mProvinceLinearLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_daxue);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    mScoreLinearLayout = (LinearLayout) view.findViewById(R.id.score_linearlayout);
    mSubjectLinearLayout = (LinearLayout) view.findViewById(R.id.subject_linearlayout);
    mProvinceLinearLayout = (LinearLayout) view.findViewById(R.id.province_linearlayout);

    mScoreLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), XuanDaxueByScoreActivity.class);
        startActivity(intent);
      }
    });
    mSubjectLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), XuanDaxueBySubjectActivity.class);
        startActivity(intent);
      }
    });
    mProvinceLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), XuanDaxueByProvinceActivity.class);
        startActivity(intent);
      }
    });
  }
}