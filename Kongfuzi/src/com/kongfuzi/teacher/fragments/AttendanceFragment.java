package com.kongfuzi.teacher.fragments;

import java.util.ArrayList;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.activities.AttendanceSummaryActivity;
import com.kongfuzi.teacher.activities.ClassListActivity;
import com.kongfuzi.teacher.activities.DianmingStudentListActivity;
import com.kongfuzi.teacher.activities.AttendanceDayActivity.ListItem;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import de.greenrobot.event.EventBus;

public class AttendanceFragment extends Fragment {

  public final static String DEBUG_TAG = "AttendanceFragment";
  public final static EventBus mBus = new EventBus();
  
  private String mCurrentCallnameListId = null;
  private String mCurrentDate = null;
  private String mCurrentPostTime = null;

  private FrameLayout rateFrameLayout;
  private ImageView startDianmingImageView;
  private TextView summaryTextView;
  private TextView rateTextView;
  private TextView dateClassTextView;
  private LinearLayout allLinearLayout;
  private TextView allTextView;
  private LinearLayout sdLinearLayout;
  private TextView sdTextView;
  private LinearLayout cdLinearLayout;
  private TextView cdTextView;
  private LinearLayout otherLinearLayout;
  private TextView otherTextView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mBus.register(this);
    return inflater.inflate(R.layout.fragment_attendance);
  }
  
  

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    rateFrameLayout = (FrameLayout) view.findViewById(R.id.attendance_summary_rate_frameLayout);
    startDianmingImageView = (ImageView) view.findViewById(R.id.start_dianming_imageView);
    rateFrameLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AttendanceSummaryActivity.class);
        startActivity(intent);
      }
    });
    startDianmingImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getSupportActivity(), ClassListActivity.class);
        startActivity(intent);
      }
    });
    summaryTextView = (TextView) view.findViewById(R.id.attendance_summary_textView);
    rateTextView = (TextView) view.findViewById(R.id.attendance_summary_rate_textView);
    dateClassTextView = (TextView) view.findViewById(R.id.date_class_textview);
    dateClassTextView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), AttendanceSummaryActivity.class);
        startActivity(intent);
      }
    });

    allLinearLayout = (LinearLayout) view.findViewById(R.id.all_layout);
    allLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), DianmingStudentListActivity.class);
        intent.putExtra(Constants.CALLNAME_LIST_ID, mCurrentCallnameListId);
        intent.putExtra(Constants.STATUE, Constants.STATUE_ALL);
        intent.putExtra(Constants.DATE, mCurrentDate);
        intent.putExtra(Constants.POST_TIME, mCurrentPostTime);
        startActivity(intent);
      }
    });
    allTextView = (TextView) view.findViewById(R.id.all_textview);
    sdLinearLayout = (LinearLayout) view.findViewById(R.id.sd_layout);
    sdLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), DianmingStudentListActivity.class);
        intent.putExtra(Constants.CALLNAME_LIST_ID, mCurrentCallnameListId);
        intent.putExtra(Constants.STATUE, Constants.STATUE_DAO);
        intent.putExtra(Constants.DATE, mCurrentDate);
        intent.putExtra(Constants.POST_TIME, mCurrentPostTime);
        startActivity(intent);
      }
    });
    sdTextView = (TextView) view.findViewById(R.id.sd_textview);
    cdLinearLayout = (LinearLayout) view.findViewById(R.id.cd_layout);
    cdLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), DianmingStudentListActivity.class);
        intent.putExtra(Constants.CALLNAME_LIST_ID, mCurrentCallnameListId);
        intent.putExtra(Constants.STATUE, Constants.STATUE_CHIDAO);
        intent.putExtra(Constants.DATE, mCurrentDate);
        intent.putExtra(Constants.POST_TIME, mCurrentPostTime);
        startActivity(intent);
      }
    });
    cdTextView = (TextView) view.findViewById(R.id.cd_textview);
    otherLinearLayout = (LinearLayout) view.findViewById(R.id.other_layout);
    otherLinearLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), DianmingStudentListActivity.class);
        intent.putExtra(Constants.CALLNAME_LIST_ID, mCurrentCallnameListId);
        intent.putExtra(Constants.STATUE, Constants.STATUE_OTHER);
        intent.putExtra(Constants.DATE, mCurrentDate);
        intent.putExtra(Constants.POST_TIME, mCurrentPostTime);
        startActivity(intent);
      }
    });
    otherTextView = (TextView) view.findViewById(R.id.other_textview);
    readCache();
    new RefreshDataThread().start();
  }
  
  public void readCache() {
    try {
      
      JSONObject object = new JSONObject(MainApplication.prefs.getString(Constants.AttendanceFragment, ""));
      final String rate = object.getString("rate");
      String callnameListId = object.getString("callnamelistid");
      mCurrentCallnameListId = callnameListId;
      final String className = object.getString("classname");
      final String latestDate = object.getString("latest_date");
      final String latestTime = object.getString("latest_time");

      final Integer latestCd = object.getInt("latest_cd");
      final Integer latestD = object.getInt("latest_d");
      /*
       * Integer latestBj = object.getInt("latest_bj"); Integer latestKk =
       * object.getInt("latest_kk"); Integer latestSj =
       * object.getInt("latest_sj"); Integer latestZt =
       * object.getInt("latest_zt");
       */
      mCurrentDate = latestDate;
      mCurrentPostTime = latestTime;
      final Integer latestAll = object.getInt("latest_all");
          rateTextView.setText(rate);
          summaryTextView.setText("考勤汇总：到勤率" + rate + "%");
          dateClassTextView.setText(latestTime + " " + latestDate + " " + className);
          allTextView.setText("" + latestAll);
          sdTextView.setText("" + latestD);
          cdTextView.setText("" + latestCd);
          otherTextView.setText("" + (latestAll - latestD - latestCd));
        
    } catch (JSONException e) {
      e.printStackTrace();
    }
    
  
  }

  public class RefreshDataThread extends Thread {
    private MyDialog myDialog;
    @Override
    public void run() {
      getActivity().runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(getSupportActivity());
          myDialog.show();
        }
      });
      KFZClient kfzClient = new KFZClient();
      String secretKey = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.SECRET_KEY,
          "");
      kfzClient.setSecretKey(secretKey);
      try {
        JSONObject object = kfzClient.dianmingInit();
        MainApplication.prefs.edit().putString(Constants.AttendanceFragment, object.toString()).commit();
        final String rate = object.getString("rate");
        String callnameListId = object.getString("callnamelistid");
        mCurrentCallnameListId = callnameListId;
        final String className = object.getString("classname");
        final String latestDate = object.getString("latest_date");
        final String latestTime = object.getString("latest_time");

        final Integer latestCd = object.getInt("latest_cd");
        final Integer latestD = object.getInt("latest_d");
        /*
         * Integer latestBj = object.getInt("latest_bj"); Integer latestKk =
         * object.getInt("latest_kk"); Integer latestSj =
         * object.getInt("latest_sj"); Integer latestZt =
         * object.getInt("latest_zt");
         */
        mCurrentDate = latestDate;
        mCurrentPostTime = latestTime;
        final Integer latestAll = object.getInt("latest_all");
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            rateTextView.setText(rate);
            summaryTextView.setText("考勤汇总：到勤率" + rate + "%");
            dateClassTextView.setText(latestTime + " " + latestDate + " " + className);
            allTextView.setText("" + latestAll);
            sdTextView.setText("" + latestD);
            cdTextView.setText("" + latestCd);
            otherTextView.setText("" + (latestAll - latestD - latestCd));
          }
        });
        // AttendanceFragment.mBus.post(new RefreshDianmingInitEvent(rate,
        // callnameListId, className, latestDate, latestTime, latestBj,
        // latestCd, latestD, latestKk, latestSj, latestZt, latestAll));
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }
    }
  }

  public void onEventMainThread(RefreshDataEvent event) {
    new RefreshDataThread().start();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }
}
