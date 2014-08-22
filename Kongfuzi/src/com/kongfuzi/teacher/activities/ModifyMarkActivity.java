package com.kongfuzi.teacher.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.activities.MarkStudentListActivity.ListItem;
import com.kongfuzi.teacher.activities.MarkStudentListActivity.SubjectItem;
import com.kongfuzi.teacher.events.RefreshDataEvent;
import com.kongfuzi.teacher.events.RefreshPingyuEvent;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import de.greenrobot.event.EventBus;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyMarkActivity extends Activity {

  public static final String DEBUG_TAG = "ModifyMarkActivity";
  public static final EventBus mBus = new EventBus();
  public static final int NONE = 0;
  public static final int PHOTOHRAPH = 1;// 拍照
  public static final int PHOTOZOOM = 2; // 缩放
  public static final int PHOTORESOULT = 3;// 结果
  public static final String IMAGE_UNSPECIFIED = "image/*";
  private Activity mActivity;
  private View mView;

  private TextView dateTextView;
  private TextView subjectTextView;
  private TextView nameTextView;
  private EditText scoreEditText;
  private FrameLayout mSubjectLayout;

  private ImageView mSubmitImageView;
  private ImageView mAddMoreImageView;
  private ListItem mCurrentItem = null;
  private List<SubjectItem> mCurrentSubjectList = null;
  private SubjectItem mCurrentSubjectItem = null;
  private Map<String, ArrayList<String>> mMessageMap = new HashMap<String, ArrayList<String>>();

  private LayoutParams mLayoutParams = null;
  private LinearLayout.LayoutParams lp = null;
  private String mCommentStr = null;
  private String mEditTextContent = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(null);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("修改考试成绩");
    setContentView(R.layout.activity_modify_mark);
    mActivity = this;
    mBus.register(this);
    mLayoutParams = new LayoutParams(67, 67);
    lp = new LinearLayout.LayoutParams(85, 85, 1); // , 1是可选写的
    lp.setMargins(9, 9, 9, 9);
    if (savedInstanceState == null) {
      mCurrentItem = MarkStudentListActivity.mListItemForIntent;
      mCurrentSubjectList = mCurrentItem.subjectList;

      nameTextView = (TextView) findViewById(R.id.name_textview);
      dateTextView = (TextView) findViewById(R.id.date_textview);
      subjectTextView = (TextView) findViewById(R.id.subject_textview);
      scoreEditText = (EditText) findViewById(R.id.score_edittext);
      mCurrentSubjectItem = mCurrentSubjectList.get(0);
      mSubjectLayout = (FrameLayout) findViewById(R.id.title_framelayout);
      RefreshTextView();
      mMessageMap = getMap();
      refreshMainView();

      mSubjectLayout.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              mCurrentSubjectItem = mCurrentSubjectList.get(which);
              RefreshTextView();
              mMessageMap = getMap();
              LinearLayout linearLayout = (LinearLayout) findViewById(R.id.scrollview_linear_layout);
              linearLayout.removeAllViews();
              refreshMainView();
              dialog.dismiss();
            }
          };
          ArrayList<String> titleList = new ArrayList<String>();
          for (int i = 0; i < mCurrentSubjectList.size(); i++) {
            titleList.add(mCurrentSubjectList.get(i).examSubject);
          }
          new AlertDialog.Builder(mActivity).setTitle("请选择")
              .setSingleChoiceItems(titleList.toArray(new CharSequence[titleList.size()]), 0, listener).show();
        }
      });

    } else {
      LinearLayout linearLayout = (LinearLayout) findViewById(R.id.scrollview_linear_layout);
      linearLayout.removeAllViews();
      Log.e(DEBUG_TAG, "saved is not null");
    }

    mAddMoreImageView = (ImageView) findViewById(R.id.more_imageview);
    mAddMoreImageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        final LinearLayout vScrollLayout = (LinearLayout) findViewById(R.id.scrollview_linear_layout);
        final View vView = getLayoutInflater().inflate(R.layout.scrollview_picture_comment_item_layout);
        ImageView morePingyu = (ImageView) vView.findViewById(R.id.add_imageview);
        final EditText editText = (EditText) vView.findViewById(R.id.edittext);
        editText.setTag(mMessageMap.size());
        editText.setText("");
        morePingyu.setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View v) {
            Intent intent = new Intent(mActivity, MarkMubanListActivity.class);
            intent.putExtra(Constants.TAG, (Integer) editText.getTag());
            startActivity(intent);
          }
        });

        TextWatcher textWatcher = new TextWatcher() {

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

          }

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            mEditTextContent = editText.getText().toString();
          }

          @Override
          public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            ArrayList<String> fileIdList = new ArrayList<String>();
            fileIdList = mMessageMap.get(mEditTextContent);
            mMessageMap.remove(mEditTextContent);
            mMessageMap.put(editText.getText().toString(), fileIdList);
          }
        };
        editText.addTextChangedListener(textWatcher);

        final LinearLayout hScrollLayout = (LinearLayout) vView.findViewById(R.id.horizontal_scrollview_linear_layout);

        final ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(mLayoutParams);
        imageView.setImageResource(R.drawable.image_add_btn);
        hScrollLayout.addView(imageView, lp);
        imageView.setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e(DEBUG_TAG, "" + which);
                mCommentStr = editText.getText().toString();
                if (which == 0) {
                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  intent.putExtra(MediaStore.EXTRA_OUTPUT,
                      Uri.fromFile(new File(MainApplication.filesPath, "temp1.jpg")));
                  startActivityForResult(intent, PHOTOHRAPH);
                } else if (which == 1) {
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
        vScrollLayout.addView(vView);
        mView = vScrollLayout;
        mMessageMap.put(editText.getText().toString(), new ArrayList<String>());
      }
    });
    mSubmitImageView = (ImageView) findViewById(R.id.submit_imageview);
    mSubmitImageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        new SubmitModifyThread().start();
      }
    });
  }

  public void RefreshTextView() {
    dateTextView.setText(mCurrentSubjectItem.examDate);
    subjectTextView.setText(mCurrentSubjectItem.examSubject);
    nameTextView.setText(mCurrentItem.studentName);
    scoreEditText.setText(mCurrentSubjectItem.score);
  }

  public class SubmitModifyThread extends Thread {
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
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("examscoreid", mCurrentSubjectList.get(0).examScoreId);
        jsonObject.put("score", scoreEditText.getText().toString());
        JSONObject commentObject = new JSONObject();
        JSONArray filesArray = new JSONArray();
        Iterator<String> iter = mMessageMap.keySet().iterator();
        while (iter.hasNext()) {
          final String commentStr = iter.next();
          ArrayList<String> fileIdList = mMessageMap.get(commentStr);
          if (fileIdList != null) {
            for (int i = 0; i < fileIdList.size(); i++) {
              commentObject.put(fileIdList.get(i), commentStr);
              filesArray.put(fileIdList.get(i));
            }
          }
        }
        jsonObject.put("comment", commentObject);
        jsonObject.put("files", filesArray);
        jsonArray.put(jsonObject);
        JSONObject response = client.examScoreModify(jsonArray);
        MarkStudentListActivity.mBus.post(new RefreshDataEvent());
        MarkDateListActivity.mBus.post(new RefreshDataEvent());
        Log.e(DEBUG_TAG, response.toString());
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    finish();
    return true;
  }

  @Override
  // 为了防止万一程序被销毁的风险，这个方法可以保证重要数据的正确性
  // 不写这个方法并不意味着一定出错，但是一旦遇到了一些非常奇怪的数据问题的时候
  // 可以看看是不是由于某些重要的数据没有保存，在程序被销毁时被重置
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // Save away the original text, so we still have it if the activity
    // needs to be killed while paused.
    ArrayList<String> tempList = new ArrayList<String>();
    Iterator<String> tempIter = mMessageMap.keySet().iterator();
    while (tempIter.hasNext()) {
      final String tempStr = tempIter.next();
      tempList.add(tempStr);
    }
    savedInstanceState.putStringArrayList(Constants.COMMENT_STR_LIST, tempList);
    savedInstanceState.putString(Constants.COMMENT_STR, mCommentStr);
    for (int i = 0; i < tempList.size(); i++) {
      savedInstanceState.putStringArrayList(tempList.get(i), mMessageMap.get(tempList.get(i)));
    }

    super.onSaveInstanceState(savedInstanceState);

    Log.e(DEBUG_TAG, "onSaveInstanceState");
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    mMessageMap = new HashMap<String, ArrayList<String>>();
    ArrayList<String> commentList = savedInstanceState.getStringArrayList(Constants.COMMENT_STR_LIST);
    for (int i = 0; i < commentList.size(); i++) {
      String tempStr = commentList.get(i);
      mMessageMap.put(tempStr, savedInstanceState.getStringArrayList(tempStr));
    }
    mCommentStr = savedInstanceState.getString(Constants.COMMENT_STR);
    Log.e(DEBUG_TAG, "onRestoreInstanceState+IntTest=");
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
          new UploadImageThread(file).start();
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

  public Map<String, ArrayList<String>> getMap() {
    SubjectItem subjectItem = mCurrentSubjectItem;
    Map<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
    try {
      JSONArray files = new JSONArray(subjectItem.filesArray);
      JSONObject comment = new JSONObject(subjectItem.commentObject);
      for (int i = 0; i < files.length(); i++) {
        String fileId = files.getString(i);
        String commentStr = comment.getString(fileId);
        ArrayList<String> value = (ArrayList<String>) result.get(commentStr);
        if (value == null) {
          value = new ArrayList<String>();
        }
        value.add(fileId);
        result.put(commentStr, value);
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return result;
  }

  public class UploadImageThread extends Thread {
    private MyDialog myDialog;
    private File file = null;

    public UploadImageThread(File file) {
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
      String secretKey = MainApplication.prefs.getString(Constants.SECRET_KEY, "null");
      client.setSecretKey(secretKey);
      JSONObject jsonObject = client.uploadFile(file, Constants.image_upload);
      Log.e(DEBUG_TAG, jsonObject.toString());
      try {
        final String fileId = jsonObject.getString("fileID");
        Iterator<String> iter = mMessageMap.keySet().iterator();
        while (iter.hasNext()) {
          final String tempStr = iter.next();
          if (tempStr.equals(mCommentStr)) {
            ArrayList<String> fileIdList = mMessageMap.get(tempStr);
            if (fileIdList == null) {
              fileIdList = new ArrayList<String>();
            }
            fileIdList.add(fileId);
            mMessageMap.put(mCommentStr, fileIdList);
            break;
          }
        }
        mActivity.runOnUiThread(new Runnable() {
          public void run() {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.scrollview_linear_layout);
            linearLayout.removeAllViews();
            refreshMainView();
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

  public void refreshMainView() {

    final LinearLayout vScrollLayout = (LinearLayout) findViewById(R.id.scrollview_linear_layout);
    vScrollLayout.removeAllViews();
    Iterator<String> iter = mMessageMap.keySet().iterator();
    int count = 0;
    while (iter.hasNext()) {
      final String commentStr = iter.next();
      List<String> fileId = mMessageMap.get(commentStr);
      final View vView = getLayoutInflater().inflate(R.layout.scrollview_picture_comment_item_layout);
      final EditText editText = (EditText) vView.findViewById(R.id.edittext);
      editText.setText(commentStr);
      editText.setTag(count);
      count++;
      TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          // TODO Auto-generated method stub
          mEditTextContent = editText.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
          // TODO Auto-generated method stub
          ArrayList<String> fileIdList = new ArrayList<String>();
          fileIdList = mMessageMap.get(mEditTextContent);
          mMessageMap.remove(mEditTextContent);
          mMessageMap.put(editText.getText().toString(), fileIdList);
        }
      };
      editText.addTextChangedListener(textWatcher);
      ImageView morePingyu = (ImageView) vView.findViewById(R.id.add_imageview);
      morePingyu.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          Intent intent = new Intent(mActivity, MarkMubanListActivity.class);
          intent.putExtra(Constants.TAG, (Integer) editText.getTag());
          startActivity(intent);
        }
      });

      final LinearLayout hScrollLayout = (LinearLayout) vView.findViewById(R.id.horizontal_scrollview_linear_layout);
      for (int i = 0; i < fileId.size() + 1; i++) {
        if (i < fileId.size()) {
          final String tempId = fileId.get(i);
          ImageView imageView = new ImageView(getApplicationContext());
          imageView.setLayoutParams(mLayoutParams);
          hScrollLayout.addView(imageView, lp);
          MainApplication.imageLoader.displayImage(Constants.image_url + tempId, imageView);
          imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
              Intent intent = new Intent(mActivity, BrowseImageActivity.class);
              intent.putExtra(Constants.ID, tempId);
              startActivity(intent);
            }
          });
        } else {
          final ImageView imageView = new ImageView(getApplicationContext());
          imageView.setLayoutParams(mLayoutParams);
          imageView.setImageResource(R.drawable.image_add_btn);
          hScrollLayout.addView(imageView, lp);
          imageView.setTag(hScrollLayout);
          imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
              // TODO Auto-generated method stub
              DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                  // TODO Auto-generated method stub
                  Log.e(DEBUG_TAG, "" + which);
                  mCommentStr = commentStr;
                  if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(MainApplication.filesPath, "temp1.jpg")));
                    startActivityForResult(intent, PHOTOHRAPH);
                  } else if (which == 1) {
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

        }
      }
      vScrollLayout.addView(vView);
      mView = vScrollLayout;
    }

    if (count == 0) {
      final View vView = getLayoutInflater().inflate(R.layout.scrollview_picture_comment_item_layout);
      ImageView morePingyu = (ImageView) vView.findViewById(R.id.add_imageview);
      final EditText editText = (EditText) vView.findViewById(R.id.edittext);
      if (mMessageMap == null) {
        mMessageMap = new HashMap<String, ArrayList<String>>();
      }
      editText.setTag(mMessageMap.size());
      editText.setText("");
      morePingyu.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mActivity, MarkMubanListActivity.class);
          intent.putExtra(Constants.TAG, (Integer) editText.getTag());
          startActivity(intent);
        }
      });

      TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          // TODO Auto-generated method stub
          mEditTextContent = editText.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
          // TODO Auto-generated method stub
          ArrayList<String> fileIdList = new ArrayList<String>();
          fileIdList = mMessageMap.get(mEditTextContent);
          mMessageMap.remove(mEditTextContent);
          mMessageMap.put(editText.getText().toString(), fileIdList);
        }
      };
      editText.addTextChangedListener(textWatcher);

      final LinearLayout hScrollLayout = (LinearLayout) vView.findViewById(R.id.horizontal_scrollview_linear_layout);

      final ImageView imageView = new ImageView(getApplicationContext());
      imageView.setLayoutParams(mLayoutParams);
      imageView.setImageResource(R.drawable.image_add_btn);
      hScrollLayout.addView(imageView, lp);
      imageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              // TODO Auto-generated method stub
              Log.e(DEBUG_TAG, "" + which);
              mCommentStr = editText.getText().toString();
              if (which == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(MainApplication.filesPath, "temp1.jpg")));
                startActivityForResult(intent, PHOTOHRAPH);
              } else if (which == 1) {
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
      vScrollLayout.addView(vView);
      mView = vScrollLayout;
      mMessageMap.put(editText.getText().toString(), new ArrayList<String>());
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

  @Override
  public void onStop() {
    super.onStop();
  }

  public void onEventMainThread(RefreshPingyuEvent event) {
    EditText editText = (EditText) mView.findViewWithTag(event.tag);
    editText.setText(editText.getText().toString() + event.content);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBus.unregister(this);
  }
}
