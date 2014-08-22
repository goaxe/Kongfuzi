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
import com.kongfuzi.teacher.MainActivity;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.events.DianmingEvent;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.fragments.AttendanceFragment;
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

public class StudentListActivity extends Activity {

  public static final String DEBUG_TAG = "StudentListActivity";
  public static final EventBus mBus = new EventBus();

  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private JSONArray mJsonArray = new JSONArray();
  private String mCurrentClassId = null;
  private ImageView mSubmitImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("点名");
    setContentView(R.layout.activity_student_list);
    mBus.register(this);
    mActivity = this;
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);

    mSubmitImageView = (ImageView) findViewById(R.id.submit_imageview);
    mSubmitImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        new SubmitDianmingThread().start();
      }
    });
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
      View view = getLayoutInflater().inflate(R.layout.student_list_item_layout);
      if (item.statue == 0) {
        view.setBackgroundColor(getResources().getColor(R.color.normal));
      } else if (item.statue == 1) {
        view.setBackgroundColor(getResources().getColor(R.color.dao));
      } else {
        view .setBackgroundColor(getResources().getColor(R.color.other));
      }
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.picId, avatarImageView);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      TextView phoneTextView = (TextView) view.findViewById(R.id.phone_textview);
      TextView cdTextView = (TextView) view.findViewById(R.id.qj_textview);
      TextView kkTextView = (TextView) view.findViewById(R.id.kk_textview);
      TextView ztTextView = (TextView) view.findViewById(R.id.zt_textview);
      TextView qjTextView = (TextView) view.findViewById(R.id.other_textview);
      
      cdTextView.setText("" + (item.appCallnameCountCd));
      kkTextView.setText("" + item.appCallnameCountKk);
      ztTextView.setText("" + item.appCallnameCountZt);
      qjTextView.setText("" + (item.appCallnameCountBj + item.appCallnameCountSj));
      nameTextView.setText(item.name);
      phoneTextView.setText(item.phone);

      final Activity forIntent = mActivity;
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(forIntent, StartRollCallActivity.class);
          intent.putExtra(Constants.NAME, item.name);
          intent.putExtra(Constants.PHONE, item.phone);
          intent.putExtra(Constants.ID, item.orgStudentId);
          intent.putExtra(Constants.PIC_ID, item.picId);
          intent.putExtra(Constants.QINGJIA_COUNT, item.appCallnameCountBj + item.appCallnameCountSj);
          intent.putExtra(Constants.KUANGKE_COUNT, item.appCallnameCountKk);
          intent.putExtra(Constants.ZAOTUI_COUNT, item.appCallnameCountZt);
          intent.putExtra(Constants.QITA_COUNT, item.appCallnameCountCd + item.appCallnameCountD);
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
        JSONObject jsonObject = client.queryStudentsList(mCurrentClassId, null);
        Log.e(DEBUG_TAG, jsonObject.toString());
        
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
          Integer appCallnameCountBj = object.getInt("app_callname_count_bj");
          Integer appCallnameCountCd = object.getInt("app_callname_count_cd");
          Integer appCallnameCountD = object.getInt("app_callname_count_d");
          Integer appCallnameCountKk = object.getInt("app_callname_count_kk");
          Integer appCallnameCountSj = object.getInt("app_callname_count_sj");
          Integer appCallnameCountZt = object.getInt("app_callname_count_zt");
          Integer appCallnameCountAll = object.getInt("app_callname_count_all");
          ListItem item = new ListItem(0, name, number, orgStudentId, sex, email, phone, age, picId, idCard,
              appCallnameCountBj, appCallnameCountCd, appCallnameCountD, appCallnameCountKk, appCallnameCountSj,
              appCallnameCountZt, appCallnameCountAll);
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
    Integer statue;//0 not selected 1 dao 2 other
    String name;
    String number;
    String orgStudentId;
    String sex;
    String email;
    String phone;
    String age;
    String picId;
    String idCard;
    Integer appCallnameCountBj;
    Integer appCallnameCountCd;
    Integer appCallnameCountD;
    Integer appCallnameCountKk;
    Integer appCallnameCountSj;
    Integer appCallnameCountZt;
    Integer appCallnameCountAll;

    public ListItem(Integer statue, String name, String number, String orgStudentId, String sex, String email, String phone,
        String age, String picId, String idCard, Integer appCallnameCountBj, Integer appCallnameCountCd,
        Integer appCallnameCountD, Integer appCallnameCountKk, Integer appCallnameCountSj, Integer appCallnameCountZt,
        Integer appCallnameCountAll) {
      this.statue = statue;
      this.name = name;
      this.number = number;
      this.orgStudentId = orgStudentId;
      this.sex = sex;
      this.email = email;
      this.phone = phone;
      this.age = age;
      this.picId = picId;
      this.idCard = idCard;
      this.appCallnameCountBj = appCallnameCountBj;
      this.appCallnameCountCd = appCallnameCountCd;
      this.appCallnameCountD = appCallnameCountD;
      this.appCallnameCountKk = appCallnameCountKk;
      this.appCallnameCountSj = appCallnameCountSj;
      this.appCallnameCountZt = appCallnameCountZt;
      this.appCallnameCountAll = appCallnameCountAll;
    }
  }
  
  public void onEventMainThread(DianmingEvent event) {
    Log.e(DEBUG_TAG, event.orgStudentId + "  " + event.status);
    for (int i= 0; i < mCurrentList.size(); i ++) {
      ListItem item = mCurrentList.get(i);
      if (item.orgStudentId.equals(event.orgStudentId)) {
        if (event.status.equals(Constants.STATUE_DAO)) {
          item.statue = 1;
        } else {
          item.statue = 2;
        }
      }
    }
    mListAdapter.notifyDataSetChanged();
    try {
      mJsonArray.put(new JSONObject().put("orgstudentid", event.orgStudentId).put("status", event.status));
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }
  
  public class SubmitDianmingThread extends Thread {
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
      String secretKey = MainApplication.prefs.getString(Constants.SECRET_KEY, "");
      client.setSecretKey(secretKey);
      JSONObject jsonObject;
      try {
        jsonObject = client.submitDianming(mCurrentClassId, mJsonArray);
      } catch (KFZNetworkError e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      mJsonArray = new JSONArray();
      //new RefreshDataThread().start();
      AttendanceFragment.mBus.post(new RefreshDataEvent());
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          myDialog.dismiss();
          Intent intent = new Intent(mActivity, MainActivity.class);
          startActivity(intent);
          finish();
        }
      });
      
    }
  }

}
