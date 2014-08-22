package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyKechengActivity extends Activity {

  public static final String DEBUG_TAG = "MyKechengActivity";
  
  private Activity mActivity;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_kecheng);
    mActivity = this;
    
    
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
      View view = getLayoutInflater().inflate(R.layout.my_kecheng_list_item_layout);
      TextView meTextView = (TextView) view.findViewById(R.id.me_textview);
      TextView opponentTextView = (TextView) view.findViewById(R.id.opponent_textview);
      meTextView.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          Intent intent = new Intent(mActivity, MyZuopingListActivity.class);
          startActivity(intent);
        }
      });
      opponentTextView.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          Intent intent = new Intent(mActivity, OpponentZuopingListActivity.class);
          startActivity(intent);
        }
      });
      return view;
    }
    
  }
}
