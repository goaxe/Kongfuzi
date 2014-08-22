package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.os.Bundle;

public class ChengjiQuxianActivity extends Activity {

  public static final String DEBUG_TAG = "ChengjiQuxianActivity";
  private Activity mActivity;
  
  private String mCurrentClassId = null;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chengji_quxian);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    mActivity = this;
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    
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
        JSONObject jsonObject = client.chengjiQuxian(mCurrentClassId, null, "30");
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          Double score = object.getDouble("score");
          String date = object.getString("date");
          String day = object.getString("day");
          ListItem item = new ListItem(score, date, day);
          mCurrentList.add(item);
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            
            
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
    Double score;
    String date;
    String day;
    
    public ListItem(Double score, String date, String day) {
      this.score = score;
      this.date = date;
      this.day = day;
    }
  }
}
