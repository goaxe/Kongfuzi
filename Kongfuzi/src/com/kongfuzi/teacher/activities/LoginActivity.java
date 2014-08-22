package com.kongfuzi.teacher.activities;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.ProgressDialog;
import org.holoeverywhere.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainActivity;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class LoginActivity extends Activity {

  public static final String DEBUG_TAG = "LoginActivity";
  
  private EditText mUsernamEditText;
  private EditText mPasswordEditText;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("登陆");
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_login);
    String secretKey = MainApplication.prefs.getString(Constants.SECRET_KEY, "null");
    if (!secretKey.equals("") && !secretKey.equals("null")) {
      startMainActivity();
    }
    mUsernamEditText = (EditText) findViewById(R.id.name_edittext);
    mPasswordEditText = (EditText) findViewById(R.id.password_edittext);

  };

  public void login(View view) {
    new LoginThread(this).start();
  }

  public void startMainActivity() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
  
  public class LoginThread extends Thread {
    public static final String DEBUG_TAG = "LoginThread";

    private Activity mActivity;
    private ProgressDialog progressDialog;

    public LoginThread(Activity mActivity) {
      this.mActivity = mActivity;

      final Activity forDialog = this.mActivity;
      this.mActivity.runOnUiThread(new Runnable() {
        public void run() {
          progressDialog = new ProgressDialog(forDialog);
          progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
          progressDialog.setMessage("正在登陆");
          progressDialog.setCancelable(false);
          progressDialog.show();
        }
      });
    }

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      try {
        
        JSONObject teacherObject = client.login(mUsernamEditText.getText().toString(), mPasswordEditText.getText().toString());
        //JSONObject teacherObject = client.login("teacher1", " ");
        Log.e(DEBUG_TAG, teacherObject.toString());
        client.setSecretKey(teacherObject.getString("secretkey"));
        MainApplication.secretKey = teacherObject.getString("secretkey");
        MainApplication.prefs.edit().putString(Constants.SECRET_KEY, teacherObject.getString("secretkey")).commit();
        MainApplication.prefs.edit().putString(Constants.AVATAR_ID, teacherObject.getString("avatarid")).commit();
        MainApplication.prefs.edit().putString(Constants.TEACHER_NAME, teacherObject.getString("teachername")).commit();
        MainApplication.prefs.edit().putString(Constants.TEACHER_EMAIL, teacherObject.getString("teacheremail")).commit();
        MainApplication.prefs.edit().putString(Constants.TEACHER_PHONE, teacherObject.getString("teacherphone")).commit();
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            startMainActivity();
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();

      } finally {
        this.mActivity.runOnUiThread(new Runnable() {
          public void run() {
            progressDialog.dismiss();
          }
        });
      }

    }
  }

}
