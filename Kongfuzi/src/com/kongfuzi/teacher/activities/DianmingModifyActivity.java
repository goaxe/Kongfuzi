package com.kongfuzi.teacher.activities;

import org.holoeverywhere.app.Activity;
import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.events.DianmingModifyEvent;
import com.kongfuzi.teacher.internal.Constants;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DianmingModifyActivity extends Activity {

  public static final String DEBUG_TAG = "DianmingModifyActivity";

  private ImageView sdImageView;
  private ImageView cdImageView;
  private ImageView ztImageView;
  private ImageView kkImageView;
  private ImageView sjImageView;
  private ImageView bjImageView;
  private ImageView mSubmitImageView;

  private String mCurrentCallnameDetailId = null;
  private String mCurrentStatue = null;
  private String mCurrentName = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_dianming_modify);
    mCurrentCallnameDetailId = getIntent().getStringExtra(Constants.CALLNAME_DETAIL_ID);
    mCurrentStatue = getIntent().getStringExtra(Constants.STATUE);
    mCurrentName = getIntent().getStringExtra(Constants.NAME);
    getSupportActionBar().setTitle("修改点名-" + mCurrentName);
    
    sdImageView = (ImageView) findViewById(R.id.sd_imageview);
    sdImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_DAO;
        setImageState();
      }
    });
    cdImageView = (ImageView) findViewById(R.id.cd_imageview);
    cdImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_CHIDAO;
        setImageState();
      }
    });
    ztImageView = (ImageView) findViewById(R.id.zt_imageview);
    ztImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_ZAOTUI;
        setImageState();
      }
    });
    kkImageView = (ImageView) findViewById(R.id.kk_imageview);
    kkImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_KUANGKE;
        setImageState();
      }
    });
    sjImageView = (ImageView) findViewById(R.id.sj_imageview);
    sjImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_SHIJIA;
        setImageState();
      }
    });
    bjImageView = (ImageView) findViewById(R.id.bj_imageview);
    bjImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mCurrentStatue = Constants.STATUE_BINGJIA;
        setImageState();
      }
    });
    setImageState();
    mSubmitImageView = (ImageView) findViewById(R.id.submit_imageview);
    mSubmitImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        DianmingStudentListActivity.mBus.post(new DianmingModifyEvent(mCurrentCallnameDetailId, mCurrentStatue));
        finish();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  public void setImageState() {
    sdImageView.setImageResource(R.drawable.dao_normal);
    bjImageView.setImageResource(R.drawable.bing_normal);
    cdImageView.setImageResource(R.drawable.chi_normal);
    kkImageView.setImageResource(R.drawable.kuang_normal);
    sjImageView.setImageResource(R.drawable.shi_normal);
    ztImageView.setImageResource(R.drawable.zao_normal);
    if (mCurrentStatue.equals(Constants.STATUE_DAO)) {
      sdImageView.setImageResource(R.drawable.dao_selected);
    } else if (mCurrentStatue.equals(Constants.STATUE_BINGJIA)) {
      bjImageView.setImageResource(R.drawable.bing_selected);
    } else if (mCurrentStatue.equals(Constants.STATUE_CHIDAO)) {
      cdImageView.setImageResource(R.drawable.chi_selected);
    } else if (mCurrentStatue.equals(Constants.STATUE_KUANGKE)) {
      kkImageView.setImageResource(R.drawable.kuang_selected);
    } else if (mCurrentStatue.equals(Constants.STATUE_SHIJIA)) {
      sjImageView.setImageResource(R.drawable.shi_selected);
    } else if (mCurrentStatue.equals(Constants.STATUE_ZAOTUI)) {
      ztImageView.setImageResource(R.drawable.zao_selected);
    }

  }

}
