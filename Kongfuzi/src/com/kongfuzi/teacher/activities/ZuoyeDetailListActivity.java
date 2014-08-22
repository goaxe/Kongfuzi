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
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ZuoyeDetailListActivity extends Activity {
  public static final String DEBUG_TAG = "ZuoyeDetailListActivity";
  public static ListItem mCurrentItem = null;
  private Activity mActivity;
  private String mCurrentHWListId;
  private String mCurrentDate;
  private String mCurrentClassName;
  private String mCurrentTitle;
  private TextView titleTextView;
  private TextView dateTextView;
  private TextView subjecTextView;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_zuoye_detail_list);
    mActivity = this;
    mCurrentHWListId = getIntent().getStringExtra(Constants.ID);
    mCurrentClassName = getIntent().getStringExtra(Constants.NAME);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    mCurrentTitle = getIntent().getStringExtra(Constants.TITLE);
    getSupportActionBar().setTitle(mCurrentClassName + " " + mCurrentDate + " " + mCurrentTitle);

    titleTextView = (TextView) findViewById(R.id.title_textview);
    dateTextView = (TextView) findViewById(R.id.date_textview);
    subjecTextView = (TextView) findViewById(R.id.subject_textview);

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
      View view = getLayoutInflater().inflate(R.layout.zuoye_detail_student_list_item_layout);
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.studentPicId, avatarImageView);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      nameTextView.setText(item.studentName);
      TextView scoreTextView = (TextView) view.findViewById(R.id.score_textview);
      scoreTextView.setText("成绩：" + item.score);
      view.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          mCurrentItem = item;
          Intent intent = new Intent(mActivity, ZuoyeModifyActivity.class);
          intent.putExtra(Constants.NAME, item.studentName);
          intent.putExtra(Constants.ID, item.hwDetailId);
          intent.putExtra(Constants.SCORE, item.score);
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
        JSONObject jsonObject = client.zuoyeDetailList(mCurrentHWListId, 0, 20);
        final String hwDate = jsonObject.getString("hwdate");
        final String hwTitle = jsonObject.getString("hwtitle");
        final String hwSubject = jsonObject.getString("hwsubject");
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            titleTextView.setText(hwTitle + ",");
            dateTextView.setText(hwDate + ",");
            subjecTextView.setText(hwSubject);
          }
        });
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String studentName = object.getString("studentname");
          String studentSex = object.getString("studentsex");
          String studentPhone = object.getString("studentphone");
          String studentAge = object.getString("studentage");
          String studentPicId = object.getString("studentpicid");
          String score = object.getString("score");
          String hwDetailId = object.getString("hwdetailid");
          JSONObject commentObject = object.getJSONObject("comment");
          JSONArray filesArray = object.getJSONArray("files");
          ListItem item = new ListItem(studentName, studentSex, studentPhone, studentAge, studentPicId, score,
              hwDetailId, commentObject, filesArray);
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
    public String studentName;
    public String studentSex;
    public String studentPhone;
    public String studentAge;
    public String studentPicId;
    public String score;
    public String hwDetailId;
    public JSONObject commentObject;
    public JSONArray filesArray;

    public ListItem(String studentName, String studentSex, String studentPhone, String studentAge, String studentPicId,
        String score, String hwDetailId, JSONObject commentObject, JSONArray filesArray) {
      this.studentName = studentName;
      this.studentSex = studentSex;
      this.studentPhone = studentPhone;
      this.studentAge = studentAge;
      this.studentPicId = studentPicId;
      this.score = score;
      this.hwDetailId = hwDetailId;
      this.commentObject = commentObject;
      this.filesArray = filesArray;
    }
  }

}
