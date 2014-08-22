package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
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

public class HomeworkSubjectListActivity extends Activity {
  public static final String DEBUG_TAG = "HomeworkSubjectListActivity";
  public static final EventBus mBus = new EventBus();

  private Activity mActivity;
  private String mCurrentDate;
  private String mCurrentSubject;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_homework_subject_list);
    mActivity = this;
    mBus.register(this);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    mCurrentSubject = getIntent().getStringExtra(Constants.SUBJECT);
    getSupportActionBar().setTitle(mCurrentDate + " " + mCurrentSubject + "作业汇总");
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    
    new RefreshDataThread().start();
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
      View view = getLayoutInflater().inflate(R.layout.homework_subject_list_item_layout);
      TextView classTextView = (TextView) view.findViewById(R.id.class_textview);
      TextView wjTextView = (TextView) view.findViewById(R.id.wj_textview);
      TextView wpTextView = (TextView) view.findViewById(R.id.wp_textview);
      TextView ypTextView = (TextView) view.findViewById(R.id.yp_textview);
      classTextView.setText(item.classname);
      wjTextView.setText("未交" + item.countWeijiao + "/" + item.countAll);
      wpTextView.setText("未批" + item.countWeipi + "/" + item.countAll);
      ypTextView.setText("已批" + item.countYipi + "/" + item.countAll);
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mActivity, HomeworkClassListActivity.class);
          intent.putExtra(Constants.DATE, mCurrentDate);
          intent.putExtra(Constants.SUBJECT, mCurrentSubject);
          intent.putExtra(Constants.ID, item.classId);
          intent.putExtra(Constants.NAME, item.classname);
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
      String secretKey = MainApplication.secretKey;
      client.setSecretKey(secretKey);

      try {
        JSONObject jsonObject = client.zuoyeListByCalss(mCurrentDate, mCurrentSubject, 0, 20);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String classId = object.getString("classid");
          String classname = object.getString("classname");
          Integer countAll = object.getInt("count_all");
          Integer countWeijiao = object.getInt("count_weijiao");
          Integer countWeipi = object.getInt("count_weipi");
          Integer countYipi = object.getInt("count_yipi");
          ListItem item = new ListItem(classId, classname, countAll, countWeijiao, countWeipi, countYipi);
          mCurrentList.add(item);
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
    String classId;
    String classname;
    Integer countAll;
    Integer countWeijiao;
    Integer countWeipi;
    Integer countYipi;

    public ListItem(String classId, String classname, Integer countAll, Integer countWeijiao, Integer countWeipi,
        Integer countYipi) {
      this.classId = classId;
      this.classname = classname;
      this.countAll = countAll;
      this.countWeijiao = countWeijiao;
      this.countWeipi = countWeipi;
      this.countYipi = countYipi;
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
