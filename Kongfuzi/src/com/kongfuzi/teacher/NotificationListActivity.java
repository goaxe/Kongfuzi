package com.kongfuzi.teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotificationListActivity extends Activity {
  public static final String DEBUG_TAG = "NotificationListActivity";
  
  private List<ListItem> mCurrentList = new ArrayList<NotificationListActivity.ListItem>();
  private ListView mListView;
  private ListAdapter mListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification_list);
    getSupportActionBar().setTitle("系统通知列表");
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    
    readData();

  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  
  public void readData() {
//    MainApplication.prefs.getStringSet(//Constants.system, defValue)
    try {
      Set<String> jsonSet = MainApplication.prefs.getStringSet(Constants.SYSTEM_NOTIFICATION, null);
      if (jsonSet == null) {
        return ;
      }
      mCurrentList = new ArrayList<ListItem>();
      for (String jsonStr : jsonSet) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        String date = jsonObject.getString(Constants.DATE);
        String title = jsonObject.getString(Constants.TITLE);
        String message = jsonObject.getString(Constants.MESSAGE);
        ListItem item = new ListItem(title, date, message);
        mCurrentList.add(item);
      }
      mListAdapter.notifyDataSetChanged();
    } catch (JSONException e) {
      // TODO: handle exception
    }
    
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
      View view = getLayoutInflater().inflate(R.layout.dianping_list_item_layout);
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      TextView messageTextView = (TextView) view.findViewById(R.id.content_textview);
      TextView titleTextView = (TextView) view.findViewById(R.id.title_textview);
      dateTextView.setText(item.date);
      titleTextView.setText(item.title);
      messageTextView.setText(item.message);
      return view;
    }

  }

  public class ListItem {
    String title;
    String date;
    String message;

    public ListItem(String title, String date, String message) {
      this.title = title;
      this.date = date;
      this.message = message;
    }

  }
}
