package com.kongfuzi.student.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.Spinner;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kongfuzi.student.R;
import com.kongfuzi.student.SearchJigouListActivity;

public class JigouFragment extends Fragment{
  
  public static final String DEBUG_TAG = "JigouFragment";
  private Spinner mMyPlaceSpinner;
  private Spinner mSchoolPlaceSpinner;
  private Spinner mSchoolSpinner;
  
  private TextView mSubmitTextView;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_jigou);
  }
  
  

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.province_titles));
    mMyPlaceSpinner = (Spinner) view.findViewById(R.id.my_place_spinner);
    mMyPlaceSpinner.setAdapter(adapter);
    mSchoolPlaceSpinner = (Spinner) view.findViewById(R.id.school_place_spinner);
    mSchoolPlaceSpinner.setAdapter(adapter);
    mSchoolSpinner = (Spinner) view.findViewById(R.id.school_spinner);
    mSchoolSpinner.setAdapter(adapter);
    mSubmitTextView = (TextView) view.findViewById(R.id.submit_textview);
    mSubmitTextView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), SearchJigouListActivity.class);
        startActivity(intent);
      }
    });
  }
}