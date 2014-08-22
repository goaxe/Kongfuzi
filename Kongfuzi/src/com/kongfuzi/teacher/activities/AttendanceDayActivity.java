package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AttendanceDayActivity extends Activity {
  
  public static final String DEBUG_TAG = "AttendanceDayActivity";
  public static final EventBus mBus = new EventBus();
  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private String mCurrentDate = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_attendance_day);
    mActivity = this;    
    mBus.register(this);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    getSupportActionBar().setTitle(mCurrentDate + "考勤");
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    readCache();
    new RefreshDataThread().start();
  }
  
  public void readCache() {
    mCurrentList = new ArrayList<ListItem>();
    try {
      JSONObject jsonObject = new JSONObject(MainApplication.prefs.getString(Constants.AttendanceDayActivity, ""));
      String date = jsonObject.getString("date");
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      mCurrentList = new ArrayList<ListItem>();
      for (int i = 0; i < jsonArray.length(); i ++) {
        JSONObject object = jsonArray.getJSONObject(i);
        Log.e(DEBUG_TAG, object.toString());
        String callnameListId = object.getString("callnamelistid");
        String className = object.getString("classname");
        String postTime = object.getString("posttime");
        Integer countBj = object.getInt("count_bj");
        Integer countCd = object.getInt("count_cd");
        Integer countD = object.getInt("count_d");
        Integer countKk = object.getInt("count_kk");
        Integer countSj = object.getInt("count_sj");
        Integer countZt = object.getInt("count_zt");
        Integer countAll = object.getInt("count_all");
        ListItem item = new ListItem(callnameListId, className, date, postTime, countBj, countCd, countD, countKk, countSj, countZt, countAll);
        mCurrentList.add(item);
      }
      mListAdapter.notifyDataSetChanged();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  public class ListAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mCurrentList.size();
    }

    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      final ListItem item = mCurrentList.get(position);
      
      View view = getLayoutInflater().inflate(R.layout.attendance_day_list_item_layout);
      TextView dateClassTextView = (TextView) view.findViewById(R.id.date_class_textview);
      TextView allTextView = (TextView) view.findViewById(R.id.all_textview);
      TextView sdTextView = (TextView) view.findViewById(R.id.sd_textview);
      TextView kkTextView = (TextView) view.findViewById(R.id.kk_textview);
      TextView otherTextView = (TextView) view.findViewById(R.id.other_textview);
      dateClassTextView.setText(item.postTime + " " + item.className);
      allTextView.setText("" + item.countAll);
      sdTextView.setText("" + item.countD);
      kkTextView.setText("" + item.countKk);
      otherTextView.setText("" + (item.countAll - item.countD - item.countKk));
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mActivity, DianmingStudentListActivity.class);
          intent.putExtra(Constants.CALLNAME_LIST_ID, item.callnameListId);
          intent.putExtra(Constants.DATE, item.date);
          intent.putExtra(Constants.POST_TIME, item.postTime);
          startActivity(intent);
        }
      });
      return view;
    }

  }

  public class RefreshDataThread extends Thread {
    private MyDialog myDialog;
    @Override
    public void run() {
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
           myDialog = new MyDialog(mActivity);
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      String secretKey = PreferenceManager.getDefaultSharedPreferences(mActivity).getString(Constants.SECRET_KEY, "");
      client.setSecretKey(secretKey);
      
      try {
        JSONObject jsonObject = client.dianmingOneDay(mCurrentDate);
        MainApplication.prefs.edit().putString(Constants.AttendanceDayActivity, jsonObject.toString()).commit();
        String date = jsonObject.getString("date");
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          Log.e(DEBUG_TAG, object.toString());
          String callnameListId = object.getString("callnamelistid");
          String className = object.getString("classname");
          String postTime = object.getString("posttime");
          Integer countBj = object.getInt("count_bj");
          Integer countCd = object.getInt("count_cd");
          Integer countD = object.getInt("count_d");
          Integer countKk = object.getInt("count_kk");
          Integer countSj = object.getInt("count_sj");
          Integer countZt = object.getInt("count_zt");
          Integer countAll = object.getInt("count_all");
          ListItem item = new ListItem(callnameListId, className, date, postTime, countBj, countCd, countD, countKk, countSj, countZt, countAll);
          mCurrentList.add(item);
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
           mListAdapter.notifyDataSetChanged(); 
          }
        });
        
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
           Toast.makeText(mActivity, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show(); 
          }
        });
        e.printStackTrace();
      }  catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }
    }
  }

  public class ListItem {
    public String callnameListId = null;
    public String className = null;
    public String date = null;
    public String postTime = null;
    public Integer countBj = null;
    public Integer countCd = null;
    public Integer countD = null;
    public Integer countKk = null;
    public Integer countSj = null;
    public Integer countZt = null;
    public Integer countAll = null;

    public ListItem(String callnameListId, String className, String date, String postTime, Integer countBj,
        Integer countCd, Integer countD, Integer countKk, Integer countSj, Integer countZt, Integer countAll) {
      this.callnameListId = callnameListId;
      this.className = className;
      this.date = date;
      this.postTime = postTime;
      this.countBj = countBj;
      this.countCd = countCd;
      this.countD = countD;
      this.countKk = countKk;
      this.countSj = countSj;
      this.countZt = countZt;
      this.countAll = countAll;
    }
  }
  public void onEvent(RefreshDataEvent event) {
    Log.e(DEBUG_TAG, "refresh Data");
    new RefreshDataThread().start();
  }
  
  @Override
  public void onDestroy() {
    mBus.unregister(this);
    super.onDestroy();
  }
}
