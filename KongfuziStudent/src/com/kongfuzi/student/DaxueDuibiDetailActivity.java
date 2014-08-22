package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import com.kongfuzi.student.DaxueDuibiListActivity.ListAdapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class DaxueDuibiDetailActivity extends Activity {

  public static final String DEBUG_TAG = "MyKechengActivity";
  
  private Activity mActivity;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_daxue_duibi_detail);
    mActivity = this;
    
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListAdapter.initRes();
    mListView.setAdapter(mListAdapter);
  }
  
  public class ListAdapter extends BaseAdapter {
    private String[] titles;
    
    public void initRes() {
      titles = getResources().getStringArray(R.array.duibi_titles);
    }
    
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return titles.length;
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
      View view = getLayoutInflater().inflate(R.layout.daxue_duibi_detail_list_item_layout);
      TextView schoolTextView = (TextView) view.findViewById(R.id.school_textview);
      TextView attr1TextView = (TextView) view.findViewById(R.id.textview1);
      TextView attr2TextView = (TextView) view.findViewById(R.id.textview2);
      schoolTextView.setText(titles[position]);
      return view;
    }
    
  }
}
