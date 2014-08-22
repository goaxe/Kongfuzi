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
import com.kongfuzi.teacher.events.DianmingModifyEvent;
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

public class DianmingStudentListActivity extends Activity {
  
  public static final String DEBUG_TAG = "DianmingStudentListActivity";
  public static final EventBus mBus = new EventBus();

  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mTotalList = new ArrayList<ListItem>();
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private String mCurrentCallnameListId = null;
  private String mCurrentDate = null;
  private String mCurrentPostTime = null;
  private String mCurrentStatue = Constants.STATUE_ALL;
  
  private ImageView sdImageView;
  private ImageView cdImageView;
  private ImageView ztImageView;
  private ImageView kkImageView;
  private ImageView sjImageView;
  private ImageView bjImageView;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_dianming_student_list);
    mActivity = this;
    mBus.register(this);
    mCurrentDate = getIntent().getStringExtra(Constants.DATE);
    mCurrentPostTime = getIntent().getStringExtra(Constants.POST_TIME);
    getSupportActionBar().setTitle(mCurrentPostTime + " " + mCurrentDate + "考勤");

    mCurrentCallnameListId = getIntent().getStringExtra(Constants.CALLNAME_LIST_ID);
    if (getIntent().getStringExtra(Constants.STATUE) != null) {
      mCurrentStatue = getIntent().getStringExtra(Constants.STATUE);
    }
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    sdImageView = (ImageView) findViewById(R.id.sd_imageview);
    cdImageView = (ImageView) findViewById(R.id.cd_imageview);
    ztImageView = (ImageView) findViewById(R.id.zt_imageview);
    kkImageView = (ImageView) findViewById(R.id.kk_imageview);
    sjImageView = (ImageView) findViewById(R.id.sj_imageview);
    bjImageView = (ImageView) findViewById(R.id.bj_imageview);
    
    sdImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_DAO;
        setImageStatus();
        sdImageView.setImageResource(R.drawable.daoqin);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    cdImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_CHIDAO;
        setImageStatus();
        cdImageView.setImageResource(R.drawable.chidao);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    ztImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        mCurrentStatue = Constants.STATUE_ZAOTUI;
        // TODO Auto-generated method stub
        setImageStatus();
        ztImageView.setImageResource(R.drawable.zaotui);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    kkImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_KUANGKE;
        setImageStatus();
        
        kkImageView.setImageResource(R.drawable.kuangke);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    sjImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_SHIJIA;
        setImageStatus();
        
        sjImageView.setImageResource(R.drawable.shijia);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    bjImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_BINGJIA;
        setImageStatus();
        bjImageView.setImageResource(R.drawable.bingjia);
        mListAdapter.setStatus();
        mListAdapter.notifyDataSetChanged();
      }
    });
    
    setImageStatus();
    new RefreshDataThread().start();
  }

  public void setImageStatus() {
    sdImageView.setImageResource(R.drawable.daoqin_normal);
    cdImageView.setImageResource(R.drawable.chidao_normal);
    ztImageView.setImageResource(R.drawable.zaotui_normal);
    kkImageView.setImageResource(R.drawable.kuangke_normal);
    sjImageView.setImageResource(R.drawable.shijia_normal);
    bjImageView.setImageResource(R.drawable.bingjia_normal);
    if (mCurrentStatue.equals(Constants.STATUE_DAO)) {
      sdImageView.setImageResource(R.drawable.daoqin);
    }
    if (mCurrentStatue.equals(Constants.STATUE_CHIDAO)) {
      cdImageView.setImageResource(R.drawable.chidao);
    }
    
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  public class ListAdapter extends BaseAdapter {
    public void setStatus() {
      if (mCurrentStatue.equals(Constants.STATUE_ALL)) {
        mCurrentList = mTotalList;
      } else {
        copyList();
      }
    }
    
    public void copyList() {
      mCurrentList = new ArrayList<ListItem>();
      if (mCurrentStatue.equals(Constants.STATUE_OTHER)) {
        for (int i = 0;i < mTotalList.size(); i ++) {
          ListItem item = mTotalList.get(i);
          if (!item.status.equals(Constants.STATUE_DAO) && !item.status.equals(Constants.STATUE_CHIDAO)) {
            mCurrentList.add(item);
          }
        }
      } else {
        for (int i = 0; i < mTotalList.size(); i ++) {
          ListItem item = mTotalList.get(i);
          if (item.status.equals(mCurrentStatue)) {
            mCurrentList.add(item);
          }
        }
      }
      
    }
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
      View view = getLayoutInflater().inflate(R.layout.dianming_student_list_item_layout);
      if (item.statueCode == 1) {
        view.setBackgroundColor(getResources().getColor(R.color.dao));
      } else if (item.statueCode == 2) {
        view.setBackgroundColor(getResources().getColor(R.color.other));
      }
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.studentPicId, avatarImageView);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      nameTextView.setText(item.studentName);
      view.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mActivity, DianmingModifyActivity.class);
          intent.putExtra(Constants.CALLNAME_DETAIL_ID, item.callnameDetailId);
          intent.putExtra(Constants.STATUE, item.status);
          intent.putExtra(Constants.NAME, item.studentName);
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
      mTotalList = new ArrayList<ListItem>();
      fetchDate(client, Constants.STATUE_BINGJIA);
      fetchDate(client, Constants.STATUE_CHIDAO);
      fetchDate(client, Constants.STATUE_DAO);
      fetchDate(client, Constants.STATUE_KUANGKE);
      fetchDate(client, Constants.STATUE_SHIJIA);
      fetchDate(client, Constants.STATUE_ZAOTUI);
      mListAdapter.setStatus();
      //mListAdapter.setStatus(mCurrentStatue);
      //mCurrentStatue = Constants.STATUE_ALL;
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          mListAdapter.notifyDataSetChanged();
          myDialog.dismiss();
        }
      });
      
    }

  }
  
  public void fetchDate(KFZClient client, String requestStatus) {
    
    
    try {
      JSONObject jsonObject = client.dianmingStudentList(mCurrentCallnameListId,requestStatus);
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      for (int i = 0; i < jsonArray.length(); i ++) {
        JSONObject object = jsonArray.getJSONObject(i);
        String callnameDetailId = object.getString("callnamedetailid");
        String orgStudentId = object.getString("orgstudentid");
        String studentName = object.getString("studentname");
        String studentSex = object.getString("studentsex");
        String studentAge = object.getString("studentage");
        String studentPhone = object.getString("studentphone");
        String studentPicId = object.getString("studentpicid");
        String status = object.getString("status");
        int statusCode = 0;
        if (status.equals(Constants.STATUE_DAO)) {
          statusCode = 1;
        } else {
          statusCode = 2;
        }
        ListItem item = new ListItem(callnameDetailId, orgStudentId, studentName, studentSex, studentAge, studentPhone, studentPicId, status, statusCode);
        mTotalList.add(item);
      }
    } catch (KFZNetworkError e) {
      // TODO: handle exception
      e.printStackTrace();
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public class ListItem {
    public int statueCode = 0;//0 none 1 dao 2 others
    public String callnameDetailId = null;
    public String orgStudentId = null;
    public String studentName = null;
    public String studentSex = null;
    public String studentAge = null;
    public String studentPhone = null;
    public String studentPicId = null;
    public String status = null;

    public ListItem(String callnameDetailId, String orgStudentId, String studentName, String studentSex,
        String studentAge, String studentPhone, String studentPicId, String status, int statusCode) {
      this.callnameDetailId = callnameDetailId;
      this.orgStudentId = orgStudentId;
      this.studentName = studentName;
      this.studentSex = studentSex;
      this.studentAge = studentAge;
      this.studentPhone = studentPhone;
      this.studentPicId = studentPicId;
      this.status = status;
      this.statueCode = statusCode;
    }
  }
  public class DianmingModifyThread extends Thread {
    public String callnameDetailId = null;
    public String statue = null;
    private MyDialog myDialog;
    public DianmingModifyThread(String callnameDetailId, String statue) {
      this.callnameDetailId = callnameDetailId;
      this.statue = statue;
    }
    
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
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(new JSONObject().put("callnamedetailid", callnameDetailId).put("status", statue));
        JSONObject object = client.dianmingModify(jsonArray);
        Log.e(DEBUG_TAG, object.toString());
        new RefreshDataThread().start();
        AttendanceDayActivity.mBus.post(new RefreshDataEvent());
        AttendanceSummaryActivity.mBus.post(new RefreshDataEvent());
        AttendanceFragment.mBus.post(new RefreshDataEvent());
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
  
  public void onEventMainThread(DianmingModifyEvent event) {
    setImageStatus();
    new DianmingModifyThread(event.callnameDetailId, event.statue).start();
  }
  
  
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }

}
