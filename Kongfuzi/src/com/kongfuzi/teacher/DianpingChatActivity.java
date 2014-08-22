package com.kongfuzi.teacher;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class DianpingChatActivity extends Activity {
  public static final String DEBUG_TAG = "DianpingChatActivity";
  
  private Activity mActivity;
  private String mCurrentClassId;
  private String mCurrentStudentId;
  

  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dianping_chat);
    mActivity = this;
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    mCurrentStudentId = getIntent().getStringExtra(Constants.STUDENT_ID);
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    
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
        JSONObject jsonObject = client.dianpingDetail(mCurrentClassId, mCurrentStudentId, Constants.TYPE_RECV, 0, 20);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String postTime = object.getString("posttime");
          String message = object.getString("message");
          String picId = object.getString("picid");
          String mType = object.getString("mtype");
          ListItem item = new ListItem(postTime, message, picId, mType);
          mCurrentList.add(item);
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            mListAdapter.notifyDataSetChanged();
          }
        });

        
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      }
      
      finally {
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }
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
      View view;
      if (item.mType.endsWith(Constants.TYPE_SEND)) {
        view = getLayoutInflater().inflate(R.layout.dianping_chat_from_item_layout);
      } else {
        view = getLayoutInflater().inflate(R.layout.dianping_chat_to_item_layout);
      }
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      LinearLayout dateLayout = (LinearLayout) view.findViewById(R.id.date_linearlayout);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      TextView contentTextView = (TextView) view.findViewById(R.id.content_textview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.picId, avatarImageView);
      contentTextView.setText(item.message);
      dateTextView.setText(item.postTime);
      
      return view;
    }
    
    
  }
  
  public class ListItem {
    String postTime;
    String message;
    String picId;
    String mType;

    public ListItem(String postTime, String message, String picId, String mType) {
      this.postTime = postTime;
      this.message = message;
      this.picId = picId;
      this.mType = mType;
    }
  }
}
