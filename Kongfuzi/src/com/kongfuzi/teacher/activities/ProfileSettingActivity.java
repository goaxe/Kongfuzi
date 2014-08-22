package com.kongfuzi.teacher.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;
import com.kongfuzi.teacher.wigedits.RoundImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileSettingActivity extends Activity {
  public static final String DEBUG_TAG = "ProfileSettingActivity";
  public static final int NONE = 0;
  public static final int PHOTOHRAPH = 1;// 拍照
  public static final int PHOTOZOOM = 2; // 缩放
  public static final int PHOTORESOULT = 3;// 结果
  public static final String IMAGE_UNSPECIFIED = "image/*";

  private Activity mActivity;

  private LinearLayout nameLayout;
  private TextView nameTextView;
  private LinearLayout emailLayout;
  private TextView emailTextView;
  private LinearLayout phoneLayout;
  private TextView phoneTextView;
  private String mAvatarId = null;

  private RoundImageView mAvatarImageView;
  private ImageView mModifyImageView;
  private ImageView mSubmitImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("个人资料设置");
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    setContentView(R.layout.activity_profile_setting);
    mActivity = this;
    nameLayout = (LinearLayout) findViewById(R.id.name_layout);
    nameTextView = (TextView) findViewById(R.id.name_textview);
    nameTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.TEACHER_NAME, ""));
    phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
    phoneTextView = (TextView) findViewById(R.id.phone_textview);
    phoneTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.TEACHER_PHONE, ""));
    emailLayout = (LinearLayout) findViewById(R.id.email_layout);
    emailTextView = (TextView) findViewById(R.id.email_textview);
    emailTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.TEACHER_EMAIL, ""));
    nameLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        final EditText editText = new EditText(mActivity);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Log.e(DEBUG_TAG, editText.getText().toString());
            nameTextView.setText(editText.getText().toString());
            // new AccountModifyThread(editText.getText().toString(), null,
            // null, null).start();
          }
        };
        new AlertDialog.Builder(mActivity).setTitle("请输入姓名").setView(editText).setPositiveButton("确定", listener)
            .setNegativeButton("取消", null).show();
      }
    });
    phoneLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub

        final EditText editText = new EditText(mActivity);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Log.e(DEBUG_TAG, editText.getText().toString());
            phoneTextView.setText(editText.getText().toString());
            // new AccountModifyThread(null, editText.getText().toString(),
            // null, null).start();
          }
        };
        new AlertDialog.Builder(mActivity).setTitle("请输入电话").setView(editText).setPositiveButton("确定", listener)
            .setNegativeButton("取消", null).show();
      }
    });
    emailLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub

        final EditText editText = new EditText(mActivity);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Log.e(DEBUG_TAG, editText.getText().toString());
            emailTextView.setText(editText.getText().toString());
            // new AccountModifyThread(null, null,
            // editText.getText().toString(), null).start();
          }
        };
        new AlertDialog.Builder(mActivity).setTitle("请输入邮箱").setView(editText).setPositiveButton("确定", listener)
            .setNegativeButton("取消", null).show();
      }
    });

    mAvatarImageView = (RoundImageView) findViewById(R.id.avatar_imageview);
    MainApplication.imageLoader.displayImage(Constants.image_url
        + PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.AVATAR_ID, "null"), mAvatarImageView);
    mModifyImageView = (ImageView) findViewById(R.id.modify_imageview);
    mModifyImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            Log.e(DEBUG_TAG, "" + which);
            // caputer
            if (which == 0) {
              Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(MainApplication.filesPath, "temp1.jpg")));
              startActivityForResult(intent, PHOTOHRAPH);
            } else if (which == 1) {// album
              Intent intent = new Intent(Intent.ACTION_PICK, null);
              intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
              startActivityForResult(intent, PHOTOZOOM);
            }
            dialog.dismiss();
          }
        };
        new AlertDialog.Builder(mActivity).setTitle("请选择")
            .setSingleChoiceItems(getResources().getStringArray(R.array.choice_titles), 0, listener).show();
      }
    });
    mSubmitImageView = (ImageView) findViewById(R.id.submit_imageview);
    mSubmitImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            new AccountModifyThread(nameTextView.getText().toString(), phoneTextView.getText().toString(),
                emailTextView.getText().toString(), mAvatarId).start();
            // new AccountModifyThread(null, null,
            // editText.getText().toString(), null).start();
          }
        };
        new AlertDialog.Builder(mActivity).setTitle("请输入邮箱").setMessage("确定提交修改吗？").setPositiveButton("确定", listener)
            .setNegativeButton("取消", null).show();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == NONE)
      return;
    // 拍照
    if (requestCode == PHOTOHRAPH) {
      File picture = new File(MainApplication.filesPath + "/temp1.jpg");
      startPhotoZoom(Uri.fromFile(picture));

    }

    if (data == null)
      return;

    // 读取相册缩放图片
    if (requestCode == PHOTOZOOM) {
      startPhotoZoom(data.getData());
    }
    // 处理结果
    if (requestCode == PHOTORESOULT) {
      Bundle extras = data.getExtras();
      if (extras != null) {
        Bitmap photo = extras.getParcelable("data");
        try {
          String fileName = MainApplication.filesPath + "/temp2.png";
          Log.e(DEBUG_TAG, fileName);
          File file = new File(fileName);
          if (file.exists()) {
            file.delete();
          }
          FileOutputStream out = new FileOutputStream(fileName);
          photo.compress(Bitmap.CompressFormat.PNG, 90, out);
          out.flush();
          out.close();
          Log.e(DEBUG_TAG, "saved");
          // File file = new File(MainApplication.filesPath + "/temp2.jpg");
          new ModifyAvatarThread(file).start();
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          Log.e(DEBUG_TAG, "FileNotFoundException");
          e.printStackTrace();
        } catch (IOException e) {
          // TODO: handle exception
          e.printStackTrace();
        }

      }

    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  public class AccountModifyThread extends Thread {
    private MyDialog myDialog;
    private String name = null;
    private String phone = null;
    private String email = null;
    private String fileId = null;

    public AccountModifyThread(String name, String phone, String email, String fileId) {
      this.name = name;
      this.phone = phone;
      this.email = email;
      this.fileId = fileId;
    }

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
        JSONObject jsonObject = client.profileModify(this.name, this.phone, this.email, null, null, this.fileId);
        if (jsonObject.getBoolean("success")) {
          mActivity.runOnUiThread(new Runnable() {
            public void run() {
              if (name != null) {
                nameTextView.setText(name);
                MainApplication.prefs.edit().putString(Constants.TEACHER_EMAIL, name).commit();
              }
              if (phone != null) {
                phoneTextView.setText(phone);
                MainApplication.prefs.edit().putString(Constants.TEACHER_PHONE, phone).commit();
              }
              if (email != null) {
                emailTextView.setText(email);
                MainApplication.prefs.edit().putString(Constants.TEACHER_EMAIL, email).commit();
              }
              if (fileId != null) {
                MainApplication.prefs.edit().putString(Constants.AVATAR_ID, mAvatarId).commit();
              }
            }
          });
        }
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

  public class ModifyAvatarThread extends Thread {

    private File file = null;
    private MyDialog myDialog;
    public ModifyAvatarThread(File file) {
      this.file = file;
    }

    @Override
    public void run() {
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(mActivity, Constants.UPLOAD_IMAGE_PROGRESS);
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      String secretKey = PreferenceManager.getDefaultSharedPreferences(mActivity).getString(Constants.SECRET_KEY,
          "null");
      client.setSecretKey(secretKey);
      JSONObject jsonObject = client.uploadFile(file, Constants.image_upload);
      try {
        mAvatarId = jsonObject.getString("fileID");
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            Toast.makeText(getApplicationContext(), "上传头像成功", Toast.LENGTH_SHORT).show();
            MainApplication.imageLoader.displayImage(Constants.image_url + mAvatarId, mAvatarImageView);
          }
        });
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

  public void startPhotoZoom(Uri uri) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
    intent.putExtra("crop", "true");
    // aspectX aspectY 是宽高的比例
    /*
     * intent.putExtra("aspectX", 6); intent.putExtra("aspectY", 9);
     */
    // outputX outputY 是裁剪图片宽高
    /*
     * intent.putExtra("outputX", 60); intent.putExtra("outputY", 90);
     */
    intent.putExtra("return-data", true);
    startActivityForResult(intent, PHOTORESOULT);
  }
}
