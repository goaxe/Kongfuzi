package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
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

public class MarkStudentListActivity extends Activity {

  public static final String DEBUG_TAG = "MarkStudentListActivity";
  public static final EventBus mBus = new EventBus();
  public static ListItem mListItemForIntent = null;
  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private String mCurrentExamTitleId = null;
  private String mCurrentTitle = null;
  private String mCurrentDate = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_mark_student_list);
    mCurrentExamTitleId = getIntent().getStringExtra(Constants.ID);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    mCurrentTitle = getIntent().getStringExtra(Constants.TITLE);
    getSupportActionBar().setTitle(mCurrentDate + mCurrentTitle + "详情");
    mActivity = this;
    mBus.register(this);

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
      final ListItem listItem = mCurrentList.get(position);
      View view = getLayoutInflater().inflate(R.layout.mark_student_list_item_layout);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      nameTextView.setText(listItem.studentName);
      TextView scoreTextView = (TextView) view.findViewById(R.id.score_textview);
      scoreTextView.setText("总分：" + listItem.totalScore);
      TextView rankTextView = (TextView) view.findViewById(R.id.rank_textview);
      rankTextView.setText("总排名：" + listItem.totalRank);
      LinearLayout scrollLayout = (LinearLayout) view.findViewById(R.id.scrollview_linear_layout);
      for (int i = 0; i < listItem.subjectList.size(); i++) {
        View subjectView = getLayoutInflater().inflate(R.layout.scrollview_item_layout);
        SubjectItem subjectItem = listItem.subjectList.get(i);
        TextView subjecTextView = (TextView) subjectView.findViewById(R.id.subject_textview);
        TextView scoreTextView1 = (TextView) subjectView.findViewById(R.id.score_textview);
        subjecTextView.setText(subjectItem.examSubject);
        scoreTextView1.setText(subjectItem.score);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100,
            1); // , 1是可选写的
        lp.setMargins(10, 15, 10, 20);
        scrollLayout.addView(subjectView, lp);
      }

      final Activity forIntent = mActivity;
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          mListItemForIntent = listItem;
          Intent intent = new Intent(forIntent, ModifyMarkActivity.class);
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
        JSONObject responseObject = client.examDetailList(mCurrentExamTitleId, 0, 20);
        JSONArray resultsArray = responseObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < resultsArray.length(); i++) {
          JSONObject jsonObject = resultsArray.getJSONObject(i);
          String studentName = jsonObject.getString("studentname");
          String studentSex = jsonObject.getString("studentsex");
          String studentPhone = jsonObject.getString("studentphone");
          String studentAge = jsonObject.getString("studentage");
          String studentPicId = jsonObject.getString("studentpicid");
          String totalScore = jsonObject.getString("totalscore");
          String totalRank = jsonObject.getString("totalrank");
          String lastScore = jsonObject.getString("lastscore");
          JSONArray subjectsArray = jsonObject.getJSONArray("subjects");
          List<SubjectItem> subjectList = new ArrayList<SubjectItem>();
          for (int j = 0; j < subjectsArray.length(); j++) {
            JSONObject object = subjectsArray.getJSONObject(j);
            String score = object.getString("score");
            String rank = object.getString("rank");
            String examSubject = object.getString("examsubject");
            String examScoreId = object.getString("examscoreid");
            String commentObject = object.getString("comment");
            String examDate = object.getString("examdate");
            String filesArray = object.getString("files");
            SubjectItem subjectItem = new SubjectItem(score, rank, examSubject, examScoreId, commentObject, examDate,
                filesArray);
            subjectList.add(subjectItem);
          }
          ListItem listItem = new ListItem(studentName, studentSex, studentPhone, studentAge, studentPicId, totalScore,
              totalRank, lastScore, subjectList);
          mCurrentList.add(listItem);
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
    public String studentName = null;
    public String studentSex = null;
    public String studentPhone = null;
    public String studentAge = null;
    public String studentPicId = null;
    public String totalScore = null;
    public String totalRank = null;
    public String lastScore = null;
    List<SubjectItem> subjectList = null;

    public ListItem(String studentName, String studentSex, String studentPhone, String studentAge, String studentPicId,
        String totalScore, String totalRank, String lastScore, List<SubjectItem> subjectList) {
      this.studentName = studentName;
      this.studentSex = studentSex;
      this.studentPhone = studentPhone;
      this.studentAge = studentAge;
      this.studentPicId = studentPicId;
      this.totalScore = totalScore;
      this.totalRank = totalRank;
      this.lastScore = lastScore;
      this.subjectList = subjectList;
    }
  }

  public class SubjectItem {
    public String score = null;
    public String rank = null;
    public String examSubject = null;
    public String examScoreId = null;
    public String commentObject = null;
    public String examDate = null;
    public String filesArray = null;

    public SubjectItem(String score, String rank, String examSubject, String examScoreId, String commentObject,
        String examDate, String filesArray) {
      this.score = score;
      this.rank = rank;
      this.examSubject = examSubject;
      this.examScoreId = examScoreId;
      this.commentObject = commentObject;
      this.examDate = examDate;
      this.filesArray = filesArray;
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
