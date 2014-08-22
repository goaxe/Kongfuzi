package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AttendanceSummaryActivity extends Activity {

  public static final String DEBUG_TAG = "DianmingSummary";
  public static final EventBus mBus = new EventBus();
  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private Boolean isFirst = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_attendance_summary);
    mActivity = this;
    mBus.register(this);

    mListView = (ListView) findViewById(R.id.listView);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    readCache();
    new RefreshDataThread().start();
  }
  
  public void readCache() {
    try {
      JSONObject jsonObject = new JSONObject(MainApplication.prefs.getString(Constants.AttendanceSummaryActivity, ""));
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      mCurrentList = new ArrayList<ListItem>();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject object = jsonArray.getJSONObject(i);
        String date = object.getString("date");
        String week = object.getString("week");
        Integer count = object.getInt("count");
        mCurrentList.add(new ListItem(date, week, count));
      }
      mListAdapter.notifyDataSetChanged();
      
    } catch (JSONException e) {
      // TODO: handle exception
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
      return mCurrentList.size() + 1;
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
      // TODO Auto-generated method stub
      if (position == mCurrentList.size()) {
        View view = getLayoutInflater().inflate(R.layout.load_more_item_layout);
        view.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            new RefreshDataThread().start();
          }
        });
        return view;
      }
      final ListItem item = mCurrentList.get(position);
      View view = getLayoutInflater().inflate(R.layout.attendance_summary_list_item_layout, null);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      TextView weekTextView = (TextView) view.findViewById(R.id.week_textview);
      TextView countTextView = (TextView) view.findViewById(R.id.count_textview);
      dateTextView.setText(item.date);
      weekTextView.setText(item.week);
      countTextView.setText("" + item.count + "次考勤");
      final Activity forIntent = mActivity;
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(forIntent, AttendanceDayActivity.class);
          intent.putExtra(Constants.DATE, item.date);
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
        JSONObject summaryObject;
        if (isFirst) {
          
          summaryObject = client.dianmingSummary(0, 5);
          mCurrentList = new ArrayList<ListItem>();
          MainApplication.prefs.edit().putString(Constants.AttendanceSummaryActivity, summaryObject.toString()).commit();
          isFirst = false;
        } else {
          summaryObject = client.dianmingSummary(mCurrentList.size(), 5);
        }
        
        JSONArray jsonArray = summaryObject.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String date = object.getString("date");
          String week = object.getString("week");
          Integer count = object.getInt("count");
          mCurrentList.add(new ListItem(date, week, count));
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            mListAdapter.notifyDataSetChanged();
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
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
    public String date = null;
    public String week = null;
    public Integer count = null;

    public ListItem(String date, String week, Integer count) {
      this.date = date;
      this.week = week;
      this.count = count;
    }
  }
  
  public void onEventMainThread(RefreshDataEvent event) {
    new RefreshDataThread().start();
  }
  
  @Override
  public void onDestroy() {
    mBus.unregister(this);
    super.onDestroy();
  }

}
