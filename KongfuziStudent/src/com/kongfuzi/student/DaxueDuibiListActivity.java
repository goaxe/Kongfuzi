package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;


public class DaxueDuibiListActivity extends Activity {

  public static final String DEBUG_TAG = "MyKechengActivity";
  
  private Activity mActivity;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  
  private TextView mDuibiTextView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_daxue_duibi_list);
    mActivity = this;
    
    mDuibiTextView = (TextView) findViewById(R.id.duibi_textview);
    mDuibiTextView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mActivity, DaxueDuibiDetailActivity.class);
        startActivity(intent);
      }
    });
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
  }
  
  public class ListAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return 5;
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
      View view = getLayoutInflater().inflate(R.layout.daxue_duibi_list_item_layout);
      
      return view;
    }
    
  }
}
