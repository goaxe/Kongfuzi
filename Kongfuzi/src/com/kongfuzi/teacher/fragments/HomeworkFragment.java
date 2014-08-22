package com.kongfuzi.teacher.fragments;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ListView;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.activities.HomeworkDateListActivity;
import com.kongfuzi.teacher.activities.HomeworkSubjectListActivity;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import de.greenrobot.event.EventBus;

public class HomeworkFragment extends Fragment {
  public static final String DEBUG_TAG = "HomeworkFragment";
  public static final EventBus mBus = new EventBus();
  
  private ListView mHistoryListView;
  private HistoryAdapter mHistoryAdapter;
  private ListView mTodayListView;
  private TodayAdapter mTodayAdapter;
  private ImageView mTodayImageView;
  private ImageView mHistoryImageView;
  private List<TodayItem> mTodayList = new ArrayList<TodayItem>();
  private List<String> mHistoryList = new ArrayList<String>();
private Boolean isFirst = true;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mBus.register(this);
    return inflater.inflate(R.layout.fragment_homework);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    mHistoryListView = (ListView) view.findViewById(R.id.history_listview);
    mHistoryAdapter = new HistoryAdapter();
    mHistoryListView.setAdapter(mHistoryAdapter);
    
    mTodayListView = (ListView) view.findViewById(R.id.today_listview);
    mTodayAdapter = new TodayAdapter();
    mTodayListView.setAdapter(mTodayAdapter);
    
    mTodayImageView = (ImageView) view.findViewById(R.id.today_imageview);
    mTodayImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mTodayImageView.setImageResource(R.drawable.today_selected);
        mHistoryImageView.setImageResource(R.drawable.history_normal);
        mHistoryListView.setVisibility(View.GONE);
        mTodayListView.setVisibility(View.VISIBLE);
      }
    });
    
    mHistoryImageView = (ImageView) view.findViewById(R.id.history_imageview);
    mHistoryImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mHistoryImageView.setImageResource(R.drawable.history_selected);
        mTodayImageView.setImageResource(R.drawable.today_normal);
        mTodayListView.setVisibility(View.GONE);
        mHistoryListView.setVisibility(View.VISIBLE);
        
      }
    });
    readCache();
    new RefreshTodayThread().start();
    new RefreshHistoryThread().start();
  }
  
  public void readCache() {
    try {
      /*JSONObject jsonObject = new JSONObject(MainApplication.prefs.getString(Constants.HomeworkFragment_TODAY, ""));
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      mHistoryList = new ArrayList<String>();
      for (int i = 0; i < jsonArray.length(); i ++) {
        JSONObject object = jsonArray.getJSONObject(i);
        String date = object.getString("date");
        mHistoryList.add(date);
      }
      mHistoryAdapter.notifyDataSetChanged();*/
      JSONObject jsonObject = new JSONObject(MainApplication.prefs.getString(Constants.HomeworkFragment_TODAY, ""));
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      mTodayList = new ArrayList<TodayItem>();
      for (int i = 0; i < jsonArray.length(); i ++) {
        JSONObject object = jsonArray.getJSONObject(i);
        
        String date = object.getString("date");
        String classId = object.getString("classid");
        String className = object.getString("classname");
        String hwListId = object.getString("hwlistid");
        String hwTitle = object.getString("hwtitle");
        String hwSubject = object.getString("hwsubject");
        Integer countAll = object.getInt("count_all");
        Integer countWeijiao = object.getInt("count_weijiao");
        Integer countWeipi = object.getInt("count_weipi");
        Integer countYipi = object.getInt("count_yipi");
        TodayItem item = new TodayItem(date, classId, className, hwListId, hwTitle, hwSubject, countAll, countWeijiao,
            countWeipi, countYipi);
        mTodayList.add(item);
      }
      
      mTodayAdapter.notifyDataSetChanged();
    } catch (JSONException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }
  
  public class TodayAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mTodayList.size();
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
      final TodayItem item = mTodayList.get(position);
      View view = getLayoutInflater().inflate(R.layout.homework_date_list_item_layout);
      TextView subjectTextView = (TextView) view.findViewById(R.id.subject_textview);
      TextView wjTextView = (TextView) view.findViewById(R.id.wj_textview);
      TextView wpTextView = (TextView) view.findViewById(R.id.wp_textview);
      TextView ypTextView = (TextView) view.findViewById(R.id.yp_textview);
      subjectTextView.setText(item.hwSubject);
      wjTextView.setText("未交" + item.countWeijiao + "/" + item.countAll);
      wpTextView.setText("未批" + item.countWeipi + "/" + item.countAll);
      ypTextView.setText("已批" + item.countYipi + "/" + item.countAll);
      
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getActivity(), HomeworkSubjectListActivity.class);
          intent.putExtra(Constants.DATE, item.date);
          intent.putExtra(Constants.SUBJECT, item.hwSubject);
          startActivity(intent);
        }
      });
      return view;
    }
    
  }

  public class HistoryAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mHistoryList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (position == mHistoryList.size()) {
        View view = getLayoutInflater().inflate(R.layout.load_more_item_layout);
        view.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            new RefreshHistoryThread().start();
          }
        });
        return view;
      }
      final String item = mHistoryList.get(position);
      View view = getLayoutInflater().inflate(R.layout.homework_list_item_layout);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      dateTextView.setText(item);

      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getSupportActivity(), HomeworkDateListActivity.class);
          intent.putExtra(Constants.DATE, item);
          startActivity(intent);
        }
      });
      return view;
    }

  }
  
  public class RefreshTodayThread extends Thread {
    private MyDialog myDialog;
    @Override
    public void run() {
      getSupportActivity().runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(getSupportActivity());
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      String secretKey = MainApplication.secretKey;
      client.setSecretKey(secretKey);

      
      try {
        JSONObject jsonObject = client.zuoyeToday(null, 0, 20);
        MainApplication.prefs.edit().putString(Constants.HomeworkFragment_TODAY, jsonObject.toString()).commit();
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mTodayList = new ArrayList<TodayItem>();
        
        for (int i = 9; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String date = object.getString("date");
          String classId = object.getString("classid");
          String className = object.getString("classname");
          String hwListId = object.getString("hwlistid");
          String hwTitle = object.getString("hwtitle");
          String hwSubject = object.getString("hwsubject");
          Integer countAll = object.getInt("count_all");
          Integer countWeijiao = object.getInt("count_weijiao");
          Integer countWeipi = object.getInt("count_weipi");
          Integer countYipi = object.getInt("count_yipi");
          TodayItem item = new TodayItem(date, classId, className, hwListId, hwTitle, hwSubject, countAll, countWeijiao,
              countWeipi, countYipi);
          mTodayList.add(item);
        }
        
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            mTodayAdapter.notifyDataSetChanged();
          }
        });
        
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

  public class RefreshHistoryThread extends Thread {

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      String secretKey = MainApplication.secretKey;
      client.setSecretKey(secretKey);
      

      try {
        JSONObject jsonObject;
        if (isFirst) {
          jsonObject = client.zuoyeHistory(0, 10);
          mHistoryList = new ArrayList<String>();
          MainApplication.prefs.edit().putString(Constants.HomeworkFragment_HISTORY, jsonObject.toString()).commit();
          isFirst = false;
        } else {
          jsonObject = client.zuoyeHistory(mHistoryList.size(), 10);
        }
        Log.e(DEBUG_TAG, jsonObject.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        //mHistoryList = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String date = object.getString("date");
          mHistoryList.add(date);
        }
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            mHistoryAdapter.notifyDataSetChanged();
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public class TodayItem {
    String date;
    String classId;
    String className;
    String hwListId;
    String hwTitle;
    String hwSubject;
    Integer countAll;
    Integer countWeijiao;
    Integer countWeipi;
    Integer countYipi;

    public TodayItem(String date, String classId, String className, String hwListId, String hwTitle, String hwSubject,
        Integer countAll, Integer countWeijiao, Integer countWeipi, Integer countYipi) {
      this.date = date;
      this.classId = classId;
      this.className = className;
      this.hwListId = hwListId;
      this.hwTitle = hwTitle;
      this.hwSubject = hwSubject;
      this.countAll = countAll;
      this.countWeijiao = countWeijiao;
      this.countWeipi = countWeipi;
      this.countYipi = countYipi;
    }
  }
  
  public void onEventMainThread(RefreshDataEvent event) {
    new RefreshTodayThread().start();
    new RefreshHistoryThread().start();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }
}
