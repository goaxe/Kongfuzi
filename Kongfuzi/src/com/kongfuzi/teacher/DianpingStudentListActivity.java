package com.kongfuzi.teacher;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.fasterxml.jackson.databind.node.NullNode;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DianpingStudentListActivity extends Activity {
  public static final String DEBUG_TAG = "DianpingStudentListActivity";
  
  private Activity mActivity;
  private ActionBar mActionBar;
  private String mCurrentClassId;
  
  private View mActionbarView;
  private Button mSubmitButton;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dianping_student_list);
    mActivity = this;
    mActionBar = getSupportActionBar();
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    mActionbarView = getLayoutInflater().inflate(R.layout.actionbar_submit_layout);
    mSubmitButton = (Button) mActionbarView.findViewById(R.id.submit_button);
    mActionBar.setCustomView(mActionbarView);
    mActionBar.setDisplayShowCustomEnabled(true);
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    
    
    for (int i = 0; i < 10; i ++) {
      ListItem item = new ListItem(false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      mCurrentList.add(item);
    }
    mListAdapter.notifyDataSetChanged();
     //new RefreshDataThread().start();
    
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
          JSONObject jsonObject = client.queryStudentsList(mCurrentClassId, null);
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
            ListItem item = new ListItem(false, name, number, orgStudentId, sex, email, phone, age, picId, idCard,
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
  
  public class ListAdapter extends BaseAdapter {
    
    public int getSelectedCount() {
      int count = 0;
      for (int i = 0; i < mCurrentList.size(); i ++) {
        if (mCurrentList.get(i).isSelected) {
          count ++;
        }
      }
      return count;
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
      
      View view = getLayoutInflater().inflate(R.layout.dianping_student_list_item_layout);
      final ImageView selectedImageView = (ImageView) view.findViewById(R.id.selected_imageview);
      if (item.isSelected) {
        selectedImageView.setImageResource(R.drawable.studentlist_selected_btn);
      } else {
        selectedImageView.setImageResource(R.drawable.studentlist_unselected);
      }
      selectedImageView.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          if (item.isSelected) {
            selectedImageView.setImageResource(R.drawable.studentlist_unselected);
            item.isSelected = false;
          } else {
            selectedImageView.setImageResource(R.drawable.studentlist_selected_btn);
            item.isSelected = true;
          }
          mSubmitButton.setText("确定(" + getSelectedCount() + "人)");
        }
      });
      
      return view;
    }
    
  }
  
  public class ListItem {
    
    Boolean isSelected;
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

    public ListItem(Boolean isSelected, String name, String number, String orgStudentId, String sex, String email, String phone,
        String age, String picId, String idCard, Integer appCallnameCountBj, Integer appCallnameCountCd,
        Integer appCallnameCountD, Integer appCallnameCountKk, Integer appCallnameCountSj, Integer appCallnameCountZt,
        Integer appCallnameCountAll) {
      this.isSelected = isSelected;
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
}
