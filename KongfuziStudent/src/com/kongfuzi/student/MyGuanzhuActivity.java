package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.app.ActionBar;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyGuanzhuActivity extends Activity {
  public static final String DEBUG_TAG = "MyGuanzhuActivity";
  
  private ActionBar mActionBar;
  private TextView mSubmitTextView;
  private ImageView mEditImageView;
  
  private ListView mListView;
  private ListAdapter mListAdapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_guanzhu);
    mActionBar = getSupportActionBar();
    View view = getLayoutInflater().inflate(R.layout.actionbar_my_guanzhu);
    mSubmitTextView = (TextView) view.findViewById(R.id.submit_textview);
    mEditImageView = (ImageView) view.findViewById(R.id.edit_imageview);
    mSubmitTextView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mSubmitTextView.setVisibility(View.GONE);
        mEditImageView.setVisibility(View.VISIBLE);
        mListAdapter.setEditable(false);
        mListAdapter.notifyDataSetChanged();
      }
    });
    mEditImageView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
       mEditImageView.setVisibility(View.GONE);
       mSubmitTextView.setVisibility(View.VISIBLE);
       mListAdapter.setEditable(true);
       mListAdapter.notifyDataSetChanged();
      }
    });
    
    mActionBar.setDisplayShowCustomEnabled(true);
    mActionBar.setCustomView(view);
    
    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
  }
  
  public class ListAdapter extends BaseAdapter {
    
    private Boolean editable = false;
    
    public void setEditable(Boolean editable) {
      this.editable = editable;
    }

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
      View view = getLayoutInflater().inflate(R.layout.my_guanzhu_list_item_layout);
      TextView joinTextView = (TextView) view.findViewById(R.id.join_textview);
      ImageView deleteImageView = (ImageView) view.findViewById(R.id.delete_imageview);
      if (editable) {
        joinTextView.setVisibility(View.GONE);
        deleteImageView.setVisibility(View.VISIBLE);
      } else {
        deleteImageView.setVisibility(View.GONE);
        joinTextView.setVisibility(View.VISIBLE);
      }
      return view;
    }
    
  }
}

