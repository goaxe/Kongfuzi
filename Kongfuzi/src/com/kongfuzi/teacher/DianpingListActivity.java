package com.kongfuzi.teacher;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DianpingListActivity extends Activity {
  public static final String DEBUG_TAG = "DianpingListActivity";

  private Activity mActivity;
  private String mCurrentClassId;

  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  private ListView mListView;
  private ListAdapter mListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = this;
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);
    Log.e(DEBUG_TAG, "class id: " + mCurrentClassId);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("消息列表");
    setContentView(R.layout.activity_dianping_list);
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    new RefreshDataThread().start();
    // new SendMessageThread("表现不错，继续努力哈1~").start();
    // new SendMessageThread("表现不错，继续努力哈2~").start();
    // new SendMessageThread("表现不错，继续努力哈3~").start();
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
      View view = getLayoutInflater().inflate(R.layout.dianping_list_item_layout);
      RoundImageView avatarImageView = (RoundImageView) view.findViewById(R.id.avatar_imageview);
      MainApplication.imageLoader.displayImage(Constants.image_url + item.studentPicId, avatarImageView);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      TextView dateTextView = (TextView) view.findViewById(R.id.date_textview);
      TextView contentTextView = (TextView) view.findViewById(R.id.content_textview);
      nameTextView.setText(item.studentName);
      dateTextView.setText(item.postTime);
      contentTextView.setText(item.message);

      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub

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
        JSONObject jsonObject = client.dianpingList(mCurrentClassId, 0, 20);
        Log.e(DEBUG_TAG, jsonObject.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String uType = object.getString("utype");
          String uid = object.getString("uid");
          String studentName = object.getString("studentname");
          String studentSex = object.getString("studentsex");
          String studentPhone = object.getString("studentphone");
          String studentAge = object.getString("studentage");
          String studentPicId = object.getString("studentpicid");
          String parentPicId = object.getString("parentpicid");
          String parentName = object.getString("parentname");
          String parentPhone = object.getString("parentphone");
          String news = object.getString("news");
          String postTime = object.getString("posttime");
          String message = object.getString("message");
          String mType = object.getString("mtype");
          ListItem item = new ListItem(uType, uid, studentName, studentSex, studentPhone, studentAge, studentPicId,
              parentPicId, parentName, parentPhone, news, postTime, message, mType);

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
    String uType;
    String uid;
    String studentName;
    String studentSex;
    String studentPhone;
    String studentAge;
    String studentPicId;
    String parentPicId;
    String parentName;
    String parentPhone;
    String news;
    String postTime;
    String message;
    String mType;

    /*
     * String parentPicId; String parentPhone; String parentEmail; Integer news;
     * String postTime; String message; String mType;
     */

    public ListItem(String uType, String uid, String studentName, String studentSex, String studentPhone,
        String studentAge, String studentPicId, String parentPicId, String parentName, String parentPhone, String news,
        String postTime, String message, String mType) {
      this.uType = uType;
      this.uid = uid;
      this.studentName = studentName;
      this.studentSex = studentSex;
      this.studentPhone = studentPhone;
      this.studentAge = studentAge;
      this.studentPicId = studentPicId;
      this.parentName = parentName;
      this.parentPhone = parentPhone;
      this.parentPicId = parentPicId;
      this.news = news;
      this.postTime = postTime;
      this.message = message;
      this.mType = mType;

    }
  }

  public class SendMessageThread extends Thread {

    private String content;

    public SendMessageThread(String content) {
      this.content = content;
    }

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      JSONArray jsonArray = new JSONArray();
      JSONObject object = new JSONObject();
      try {
        object.put("orgstudentid", "0a683b92cd5f42218b408f3e41fbaaf9").put("utype", "TYPE_STUDENT");
        JSONObject jsonObject = client.dianpingSend(mCurrentClassId, this.content, jsonArray);
        Log.e(DEBUG_TAG, jsonObject.toString());
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }

  /*
   * }
   */
}
