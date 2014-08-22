package com.kongfuzi.student;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class XuanDaxueByScoreActivity extends Activity {
  public static final String  DEBUG_TAG = "XuanDaxueByScoreActivity";
  private Activity mActivity;
  
  private TextView mSubmitTextView; 
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_xuan_daxue_by_score);
    mActivity = this;
    
    mSubmitTextView = (TextView) findViewById(R.id.submit_textview);
    mSubmitTextView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mActivity, XuanDaxueListActivity.class);
        startActivity(intent);
      }
    });
    
    
  }
}
