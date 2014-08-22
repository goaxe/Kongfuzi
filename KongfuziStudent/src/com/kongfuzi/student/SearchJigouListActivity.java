package com.kongfuzi.student;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;


public class SearchJigouListActivity extends Activity {
  public static final String DEBUG_TAG = "SearchJigouListActivity";
  private Activity mActivity;
  private ActionBar mActionBar;
  private FrameLayout mDuibiFrameLayout;
  private TextView mCountTextView;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_jigou_list);
    
    mActivity = this;
    mActionBar = getSupportActionBar();
    View view = getLayoutInflater().inflate(R.layout.actionbar_xuan_daxue_list);
    mDuibiFrameLayout = (FrameLayout) view.findViewById(R.id.duibi_framelayout);
    mCountTextView = (TextView) view.findViewById(R.id.count_textview);
    mCountTextView.setText("0");
    mActionBar.setDisplayShowCustomEnabled(true);
    mActionBar.setCustomView(view);
    
    
    for (int i = 0; i < 6; i ++) {
      ListItem item = new ListItem(false);
      mCurrentList.add(item);
    }
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    mDuibiFrameLayout.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mActivity, DaxueDuibiListActivity.class);
        startActivity(intent);
      }
    });
  }
  
  public class ListAdapter extends BaseAdapter {

    public int getCheckedCount() {
      int count = 0;
      for (int i = 0; i < mCurrentList.size(); i ++) {
        if (mCurrentList.get(i).isChecked) {
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
      View view = getLayoutInflater().inflate(R.layout.search_jigou_list_item_layout);
      LinearLayout duibiLinearLayout = (LinearLayout) view.findViewById(R.id.duibi_linearlayout);
      final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
      checkBox.setChecked(item.isChecked);
      duibiLinearLayout.setOnClickListener(new OnClickListener() {
        
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          item.isChecked = !item.isChecked;
          checkBox.setChecked(item.isChecked);
          mCountTextView.setText("" + getCheckedCount());
        }
      });
      return view;
    }
  }
  
  public class ListItem {
    Boolean isChecked;
    public ListItem(Boolean isChecked) {
      this.isChecked = isChecked;
    }
    
  }
}

