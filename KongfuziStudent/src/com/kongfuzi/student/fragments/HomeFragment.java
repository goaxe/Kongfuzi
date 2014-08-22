package com.kongfuzi.student.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import com.kongfuzi.student.MyDaxueActivity;
import com.kongfuzi.student.MyGuanzhuActivity;
import com.kongfuzi.student.MyKechengActivity;
import com.kongfuzi.student.R;
import com.kongfuzi.student.XuanDaxueByProvinceActivity;
import com.kongfuzi.student.XuanDaxueByScoreActivity;
import com.kongfuzi.student.XuanDaxueBySubjectActivity;
import com.kongfuzi.student.XuanDaxueListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
public class HomeFragment extends Fragment {
  
  public static final String DEBUG_TAG = "HomeFragment";
  
  private EditText mSearchEditText;
  private ImageView mSearchImageView;
  private ImageView mKechengImageView;
  private ImageView mDaxueImageView;
  private ImageView mGuanzhuImageView;
  private ImageView mJiaowuImageView;
  private LinearLayout mMoreLinearLayout;
  private LinearLayout mXuanLinearLayout;
  private LinearLayout mScoreLinearLayout;
  private LinearLayout mSubjectLinearLayout;
  private LinearLayout mProvinceLinearLayout;
  
  private LinearLayout mZuopingLinearLayout;
  
  
  private TextView mInfoTextView;
  private ImageView mZuoping1ImageView;
  private ImageView mZuoping2ImageView;
  private Button mAnotherButton;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_home);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    mSearchEditText = (EditText) view.findViewById(R.id.search_edittext);
    mSearchImageView = (ImageView) view.findViewById(R.id.search_imageview);
    mKechengImageView = (ImageView) view.findViewById(R.id.kecheng_imageview);
    mDaxueImageView = (ImageView) view.findViewById(R.id.daxue_imageview);
    mGuanzhuImageView = (ImageView) view.findViewById(R.id.guanzhu_imageview);
    mJiaowuImageView = (ImageView) view.findViewById(R.id.jiaowu_imageview);
    mMoreLinearLayout = (LinearLayout) view.findViewById(R.id.more_linearlayout);
    mXuanLinearLayout = (LinearLayout) view.findViewById(R.id.xuan_linearlayout);
    mScoreLinearLayout = (LinearLayout) view.findViewById(R.id.score_linearlayout);
    mSubjectLinearLayout = (LinearLayout) view.findViewById(R.id.subject_linearlayout);
    mProvinceLinearLayout = (LinearLayout) view.findViewById(R.id.province_linearlayout);
    mZuopingLinearLayout = (LinearLayout) view.findViewById(R.id.zuoping_linearlayout);
    mInfoTextView = (TextView) view.findViewById(R.id.info_textview);
    mZuoping1ImageView = (ImageView) view.findViewById(R.id.zuoping1_imageview);
    mZuoping2ImageView = (ImageView) view.findViewById(R.id.zuoping2_imageview);
    mAnotherButton = (Button) view.findViewById(R.id.another_button);
    
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
    
    mMoreLinearLayout.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mXuanLinearLayout.getVisibility() == View.GONE) {
          mZuopingLinearLayout.setVisibility(View.GONE);
          mXuanLinearLayout.setVisibility(View.VISIBLE);
        } else {
          mXuanLinearLayout.setVisibility(View.GONE);
          mZuopingLinearLayout.setVisibility(View.VISIBLE);
        }
      }
    });
    
    mSearchImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
       Log.e(DEBUG_TAG, "mSearchImageView"); 
       Intent intent = new Intent(getActivity(), XuanDaxueListActivity.class);
       startActivity(intent);
      }
    });
    mKechengImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        resetImageView();
        mKechengImageView.setImageResource(R.drawable.mycourse_select);
        Intent intent = new Intent(getActivity(), MyKechengActivity.class);
        startActivity(intent);
      }
    });
    mDaxueImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        resetImageView();
        mDaxueImageView.setImageResource(R.drawable.myuniversity_select);
        Intent intent = new Intent(getActivity(), MyDaxueActivity.class);
        startActivity(intent);
      }
    });
    mGuanzhuImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        resetImageView();
        mGuanzhuImageView.setImageResource(R.drawable.myfollow_select);
        Intent intent = new Intent(getActivity(), MyGuanzhuActivity.class);
        startActivity(intent);
      }
    });
    mJiaowuImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        resetImageView();
        mJiaowuImageView.setImageResource(R.drawable.cloudorg_select);
      }
    });
    
    mAnotherButton.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.e(DEBUG_TAG, "mAnotherButton");
      }
    });
    
    
  }
  
  
  public void resetImageView() {
    mKechengImageView.setImageResource(R.drawable.mycourse);
    mDaxueImageView.setImageResource(R.drawable.myuniversity);
    mGuanzhuImageView.setImageResource(R.drawable.myfollow);
    mJiaowuImageView.setImageResource(R.drawable.cloudorg);
   
  }
}
