package com.kongfuzi.teacher.activities;

import org.holoeverywhere.app.Activity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class QueryClassesListActivity extends Activity {
  
  public static final String DEBUG_TAG = "ClassListActivity";

  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("班级列表");
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_query_classes_list);
    mActivity = this;

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
      View view = getLayoutInflater().inflate(R.layout.class_list_item_layout);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      nameTextView.setText(item.name);
      TextView countTextView = (TextView) view.findViewById(R.id.count_textview);
      countTextView.setText("" + item.stuCount + "人");
      final Activity forIntent = mActivity;
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(forIntent, ZuoyeSubjectListActivity.class);
          intent.putExtra(Constants.ID, item.id);
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
        JSONObject jsonObject = client.queryClassesList();
        JSONArray jsonArray = jsonObject.getJSONArray("classes");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i ++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String name = object.getString("name");
          String id = object.getString("id");
          Integer stuCount = object.getInt("stucount");
          ListItem item = new ListItem(name, id, stuCount);
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
    public String name = null;
    public String id = null;
    public Integer stuCount = null;

    public ListItem(String name, String id, Integer stuCount) {
      this.name = name;
      this.id = id;
      this.stuCount = stuCount;
    }
  }

}
