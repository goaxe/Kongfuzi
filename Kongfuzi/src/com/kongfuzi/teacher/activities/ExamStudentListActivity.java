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
import com.kongfuzi.teacher.wigedits.RoundImageView;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ExamStudentListActivity extends Activity {

  public static final EventBus mBus = new EventBus();

  private Activity mActivity;
  private String mCurrentClassId;
  private String mCurrentDate;
  private String mCurrentTitle;
  private String mCurrentSubject;

  private ListView mListView;
  private ListAdapter mListAdapter;
  public static ListItem mListItemForIntent;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("学生名单");
    setContentView(R.layout.activity_zuoye_student_list);
    mActivity = this;
    mBus.register(this);

    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    mCurrentTitle = getIntent().getStringExtra(Constants.TITLE);
    mCurrentSubject = getIntent().getStringExtra(Constants.SUBJECT);
    

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
      // TODO Auto-generated method stub
      final ListItem item = mCurrentList.get(position);
      View view = getLayoutInflater().inflate(R.layout.zuoye_student_list_item_layout);
      ImageView selectedImageView = (ImageView) view.findViewById(R.id.selected_imageview);
      if (item.finished == true) {
        view.setBackgroundColor(getResources().getColor(R.color.dao));
        selectedImageView.setImageResource(R.drawable.studentlist_selected_btn);
      } else {
        view.setBackgroundColor(getResources().getColor(R.color.normal));
        selectedImageView.setImageResource(R.drawable.studentlist_unselected);
      }
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.picId, avatarImageView);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      nameTextView.setText(item.name);
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          mListItemForIntent = item;
          Intent intent = new Intent(getApplicationContext(), ExamSubmitActivity.class);
          intent.putExtra(Constants.ID, mCurrentClassId);
          intent.putExtra(Constants.DATE, mCurrentDate);
          intent.putExtra(Constants.TITLE, mCurrentTitle);
          intent.putExtra(Constants.SUBJECT, mCurrentSubject);
          intent.putExtra(Constants.STUDENT_ID, item.orgStudentId);
          intent.putExtra(Constants.NAME, item.name);
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
      client.setSecretKey(MainApplication.secretKey);
      
      try {
        // String examSubjectId = jsonObject.getString("examsubjectid");
        JSONObject jsonObject = client.examStudentList(mCurrentClassId, mCurrentDate, mCurrentTitle, mCurrentSubject);
        JSONArray jsonArray = jsonObject.getJSONArray("students");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String name = object.getString("name");
          String number = object.getString("number");
          String orgStudentId = object.getString("orgstudentid");
          String sex = object.getString("sex");
          String email = object.getString("email");
          String phone = object.getString("phone");
          String age = object.getString("age");
          String picId = object.getString("picid");
          String idCard = object.getString("idcard");
          Boolean finished = object.getBoolean("finished");
          ListItem item = new ListItem(name, number, orgStudentId, sex, email, phone, age, picId, idCard, finished);
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
    public String name;
    public String number;
    public String orgStudentId;
    public String sex;
    public String email;
    public String phone;
    public String age;
    public String picId;
    public String idCard;
    public Boolean finished;

    public ListItem(String name, String number, String orgStudentId, String sex, String email, String phone,
        String age, String picId, String idCard, Boolean finished) {
      this.name = name;
      this.number = number;
      this.orgStudentId = orgStudentId;
      this.sex = sex;
      this.email = email;
      this.phone = phone;
      this.age = age;
      this.picId = picId;
      this.idCard = idCard;
      this.finished = finished;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }
  
  public void onEventMainThread(RefreshDataEvent event) {
    new RefreshDataThread().start();
  }

}
