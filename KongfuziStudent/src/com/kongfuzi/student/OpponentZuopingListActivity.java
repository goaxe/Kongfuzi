package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.GridView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class OpponentZuopingListActivity extends Activity {
  
  public static final String DEBUG_TAG = "OpponentZuopingListActivity";
  
  private GridView mGridView;
  private GridAdapter mGridAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_opponent_zuoping_list);
    mGridView = (GridView) findViewById(R.id.gridview);
    mGridAdapter = new GridAdapter();
    mGridView.setAdapter(mGridAdapter);
  }
  
  public class GridAdapter extends BaseAdapter {
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return 6;
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
      View view = getLayoutInflater().inflate(R.layout.opponent_zuoping_list_item_layout);
      return view;
    }
  }
}
