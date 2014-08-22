package com.kongfuzi.teacher.activities;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.events.DianmingEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class StartRollCallActivity extends Activity {
  public static final String DEBUG_TAG = "StartRollCallActivity";

  private String mCurrentName = null;
  private String mCurrentPhone = null;
  private String mCurrentId = null;
  private Integer mQJCount = null;
  private Integer mKKCount = null;
  private Integer mZTCount = null;
  private Integer mQTCount = null;


  private TextView mNameTextView;
  private TextView mPhoneTextView;
  private TextView mQJTextView;
  private TextView mKKTextView;
  private TextView mZTTextView;
  private TextView mQTTextView;

  private ImageView sdImageView;
  private ImageView cdImageView;
  private ImageView ztImageView;
  private ImageView kkImageView;
  private ImageView sjImageView;
  private ImageView bjImageView;

  private String mCurrentStatue = null;
  private String mCurrentPicId = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("开始点名");
    
    setContentView(R.layout.activity_start_roll_call);
    
    mCurrentName = getIntent().getStringExtra(Constants.NAME);
    mCurrentPhone = getIntent().getStringExtra(Constants.PHONE);
    mCurrentId = getIntent().getStringExtra(Constants.ID);
    mCurrentPicId = getIntent().getStringExtra(Constants.PIC_ID);
    
    RoundImageView avatarImageView = (RoundImageView) findViewById(R.id.avatar_imageview);
    MainApplication.imageLoader.displayImage(Constants.image_url + mCurrentPicId, avatarImageView);
    
    mQJCount = getIntent().getIntExtra(Constants.QINGJIA_COUNT, 0);
    mKKCount = getIntent().getIntExtra(Constants.KUANGKE_COUNT, 0);
    mZTCount = getIntent().getIntExtra(Constants.ZAOTUI_COUNT, 0);
    mQTCount = getIntent().getIntExtra(Constants.QITA_COUNT, 0);
    mQJTextView = (TextView) findViewById(R.id.qj_textview);
    mQJTextView.setText("" + mQJCount);
    mKKTextView = (TextView) findViewById(R.id.kk_textview);
    mKKTextView.setText(""+ mKKCount);
    mZTTextView = (TextView) findViewById(R.id.zt_textview);
    mZTTextView.setText("" + mZTCount);
    mQTTextView = (TextView) findViewById(R.id.other_textview);
    mQTTextView.setText("" + mQTCount);

    mNameTextView = (TextView) findViewById(R.id.name_textview);
    mPhoneTextView = (TextView) findViewById(R.id.phone_textview);
    mNameTextView.setText(mCurrentName);
    mPhoneTextView.setText(mCurrentPhone);
    
    sdImageView = (ImageView) findViewById(R.id.sd_imageview);
    sdImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_DAO;
        sdImageView.setImageResource(R.drawable.dao_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });
    cdImageView = (ImageView) findViewById(R.id.cd_imageview);
    cdImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_CHIDAO;
        cdImageView.setImageResource(R.drawable.chi_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });
    ztImageView = (ImageView) findViewById(R.id.zt_imageview);
    ztImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_ZAOTUI;
        ztImageView.setImageResource(R.drawable.zao_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });
    kkImageView = (ImageView) findViewById(R.id.kk_imageview);
    kkImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_KUANGKE;
        kkImageView.setImageResource(R.drawable.kuang_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });
    sjImageView = (ImageView) findViewById(R.id.sj_imageview);
    sjImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_SHIJIA;
        sjImageView.setImageResource(R.drawable.shi_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });
    bjImageView = (ImageView) findViewById(R.id.bj_imageview);
    bjImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_BINGJIA;
        bjImageView.setImageResource(R.drawable.bing_selected);
        StudentListActivity.mBus.post(new DianmingEvent(mCurrentId,
            mCurrentStatue));
        finish();
      }
    });

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  
}
