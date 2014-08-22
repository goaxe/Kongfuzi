package com.kongfuzi.teacher.activities;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.DatePickerDialog;
import org.holoeverywhere.widget.DatePicker;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChengjiLuRuActivity extends Activity {

  public static final String DEBUG_TAG = "ChengjiLuRuActivity";
  
  private Activity mActivity;
  private String mCurrentClassId;
  private ListView mListView;
  private ListAdapter mListAdapter;

  private FrameLayout mDateFrameLayout;
  private TextView mDateTextView;
  private EditText mTitleEditText;

  private List<String> mCurrentList = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("录入成绩");
    setContentView(R.layout.activity_chengji_lu_ru);
    mActivity = this;
    mCurrentClassId = getIntent().getStringExtra(Constants.ID);

    mDateFrameLayout = (FrameLayout) findViewById(R.id.date_framelayout);
    mDateTextView = (TextView) findViewById(R.id.date_textview);
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String date = sDateFormat.format(new java.util.Date());
    mDateTextView.setText(date);
    mTitleEditText = (EditText) findViewById(R.id.title_edittext);
    mDateFrameLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateTextView.setText("" + year + "-" + new DecimalFormat("00").format(monthOfYear + 1) + "-"
                + new DecimalFormat("00").format(dayOfMonth));
          }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));// 表示默认的年月日
        dialog.show();
      }
    });

    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    readCache();
    new RefreshDataThread().start();
  }
  
  public void readCache() {
    
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  public class ListAdapter extends BaseAdapter {

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
      final String item = mCurrentList.get(position);
      View view = getLayoutInflater().inflate(R.layout.subject_list_item_layout);
      TextView subjectTextView = (TextView) view.findViewById(R.id.subject_textview);
      subjectTextView.setText(item);
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mActivity, ExamStudentListActivity.class);
          intent.putExtra(Constants.ID, mCurrentClassId);
          intent.putExtra(Constants.DATE, mDateTextView.getText().toString());
          intent.putExtra(Constants.TITLE, mTitleEditText.getText().toString());
          intent.putExtra(Constants.SUBJECT, item);
          startActivity(intent);

        }
      });
      return view;
    }

  }

  public class RefreshDataThread extends Thread {
    private MyDialog myDialog;
    
    @Override
    public void run() {
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(mActivity);
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      
      try {
        JSONObject jsonObject = client.subjectList();
        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
        mCurrentList = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
          String subject = (String) jsonArray.get(i);
          mCurrentList.add(subject);
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            mListAdapter.notifyDataSetChanged();
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }

    }
  }

}
