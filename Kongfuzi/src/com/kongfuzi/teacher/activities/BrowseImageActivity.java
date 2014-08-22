package com.kongfuzi.teacher.activities;

import org.holoeverywhere.app.Activity;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.internal.Constants;

import android.os.Bundle;
import android.widget.ImageView;

public class BrowseImageActivity extends Activity {

  
  private String mCurrentFileId = null;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("孔夫子教师版");
    setContentView(R.layout.activity_browse_image);
    
    mCurrentFileId = getIntent().getStringExtra(Constants.ID);
    
    ImageView imageView = (ImageView) findViewById(R.id.imageview);
    MainApplication.imageLoader.displayImage(Constants.image_url + mCurrentFileId + "&type=large", imageView);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }
}
