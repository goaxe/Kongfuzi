package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import de.greenrobot.event.EventBus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MarkDateListActivity extends Activity {

  public static final String DEBUG_TAG = "MarkDateListActivity";
  public static final EventBus mBus = new EventBus();

  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private String mCurrentClassId = null;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("成绩");
    setContentView(R.layout.activity_mark_date_list);
    mActivity = this;
    mBus.register(this);
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    
    

    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);

    new RefreshDataThread().start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem menuItem = menu.add("更多");
    menuItem.setIcon(R.drawable.more_btn);
    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle().equals("更多")) {
      DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
          // TODO Auto-generated method stub
          Log.e(DEBUG_TAG, "" + which);
          if (which == 0) {
            Intent intent = new Intent(mActivity, ChengjiLuRuActivity.class);
            intent.putExtra(Constants.ID, mCurrentClassId);
            startActivity(intent);
          } else if (which == 1) {
            Intent intent = new Intent(mActivity, MarkMubanListActivity.class);
            startActivity(intent);
          } /*else if (which == 2) {
            Intent intent = new Intent(mActivity, ChengjiQuxianActivity.class);
            intent.putExtra(Constants.ID, mCurrentClassId);
            startActivity(intent);
          }*/
          dialog.dismiss();
        }
      };

      /*
       * <string-array name="chengji_choices"> <item>录入成绩</item>
       * <item>屏幕模版</item> <item>成绩曲线</item> <item>取消</item> </string-array>
       */
      new AlertDialog.Builder(mActivity).setTitle("请选择")
          .setSingleChoiceItems(getResources().getStringArray(R.array.chengji_choices), 0, listener).show();
    } else {
      finish();
    }
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
      View view = getLayoutInflater().inflate(R.layout.mark_date_list_item_layout);
      LinearLayout scrollLayout = (LinearLayout) view.findViewById(R.id.scrollview_linear_layout);
      TextView titleTextView = (TextView) view.findViewById(R.id.title_textview);
      titleTextView.setText(item.examTitle);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      dateTextView.setText(item.examDate);
      TextView aveScoreTextView = (TextView) view.findViewById(R.id.ave_score_textview);
      aveScoreTextView.setText("平均分:" + item.aveScore);
      TextView mScoreTextView = (TextView) view.findViewById(R.id.m_score_textview);
      mScoreTextView.setText("最低/最高分:" + item.minScore + "/" + item.maxScore);
      for (int i = 0; i < item.subjects.size(); i++) {
        SubjectItem subjectItem = item.subjects.get(i);
        View subjectView = getLayoutInflater().inflate(R.layout.scrollview_item_layout);
        TextView subjecTextView = (TextView) subjectView.findViewById(R.id.subject_textview);
        TextView scoreTextView = (TextView) subjectView.findViewById(R.id.score_textview);
        subjecTextView.setText(subjectItem.examSubject);
        scoreTextView.setText(subjectItem.aveScore);
        
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100,
            1); // , 1是可选写的
        lp.setMargins(10, 15, 10, 20);
        scrollLayout.addView(subjectView, lp);
      }

      final Activity forIntent = mActivity;
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(forIntent, MarkStudentListActivity.class);
          intent.putExtra(Constants.DATE, item.examDate);
          intent.putExtra(Constants.TITLE, item.examTitle);
          intent.putExtra(Constants.ID, item.examTitleId);
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
        JSONObject jsonObject = client.queryExamList(mCurrentClassId, 0, 20);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String examTitleId = object.getString("examtitleid");
          String examDate = object.getString("examdate");
          String examTitle = object.getString("examtitle");
          String aveScore = object.getString("ave_score");
          String maxScore = object.getString("max_score");
          String minScore = object.getString("min_score");
          JSONArray subjectArray = object.getJSONArray("subjects");
          List<SubjectItem> subjects = new ArrayList<SubjectItem>();
          for (int j = 0; j < subjectArray.length(); j++) {
            JSONObject subject = subjectArray.getJSONObject(j);
            String examSubject = subject.getString("examsubject");
            String aveScore1 = subject.getString("ave_score");
            String maxScore1 = subject.getString("max_score");
            String minScore1 = subject.getString("min_score");
            SubjectItem subjectItem = new SubjectItem(examSubject, aveScore1, maxScore1, minScore1);
            subjects.add(subjectItem);
          }
          ListItem item = new ListItem(examTitleId, examDate, examTitle, aveScore, maxScore, minScore, subjects);
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
    String examTitleId;
    String examDate;
    String examTitle;
    String aveScore;
    String maxScore;
    String minScore;
    List<SubjectItem> subjects;

    public ListItem(String examTitleId, String examDate, String examTitle, String aveScore, String maxScore,
        String minScore, List<SubjectItem> subjects) {
      this.examTitleId = examTitleId;
      this.examDate = examDate;
      this.examTitle = examTitle;
      this.aveScore = aveScore;
      this.maxScore = maxScore;
      this.minScore = minScore;
      this.subjects = subjects;
    }
  }

  public class SubjectItem {
    String examSubject;
    String aveScore;
    String maxScore;
    String minScore;

    public SubjectItem(String examSubject, String aveScore, String maxScore, String minScore) {
      this.examSubject = examSubject;
      this.aveScore = aveScore;
      this.maxScore = maxScore;
      this.minScore = minScore;
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
