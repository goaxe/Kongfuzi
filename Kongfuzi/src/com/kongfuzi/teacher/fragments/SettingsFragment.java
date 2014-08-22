package com.kongfuzi.teacher.fragments;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog.Builder;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kongfuzi.teacher.NotificationListActivity;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.DianpingStudentListActivity.RefreshDataThread;
import com.kongfuzi.teacher.activities.LoginActivity;
import com.kongfuzi.teacher.activities.ProfileSettingActivity;
import com.kongfuzi.teacher.dialogs.DialogsAlertDialogFragment;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;
import com.umeng.fb.FeedbackAgent;

public class SettingsFragment extends Fragment {
  public static final String DEBUG_TAG = "SettingsFragment";
  private LinearLayout profileLayout;
  private LinearLayout feedbackLayout;
  private LinearLayout notificationLayout;
  private LinearLayout aboutLayout;
  private LinearLayout exitLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_settings);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    ;
    profileLayout = (LinearLayout) view.findViewById(R.id.profileLayout);
    feedbackLayout = (LinearLayout) view.findViewById(R.id.feedbackLayout);
    notificationLayout = (LinearLayout) view.findViewById(R.id.notificationLayout);
    aboutLayout = (LinearLayout) view.findViewById(R.id.aboutLayout);
    exitLayout = (LinearLayout) view.findViewById(R.id.exitLayout);

    profileLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getSupportActivity(), ProfileSettingActivity.class);
        startActivity(intent);
      }
    });
    feedbackLayout.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        FeedbackAgent agent = new FeedbackAgent(getActivity());
        agent.startFeedbackActivity();
      }
    });
    
    notificationLayout.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), NotificationListActivity.class);
        startActivity(intent);
      }
    });

    aboutLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        new GetNewestVersionThread().start();
      }
    });

    exitLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        CloseDialog closeDialog = new CloseDialog();
        closeDialog.show(getSupportActivity().getSupportFragmentManager(), "CloseDialog");
      }
    });
    
  }

  public class CloseDialog extends DialogsAlertDialogFragment {
    private android.content.DialogInterface.OnClickListener doPositiveClick = new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        MainApplication.prefs.edit().clear().commit();
        MainApplication.secretKey = "null";
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

      }
    };

    @Override
    protected void prepareBuilder(Builder builder) {
      builder.setTitle("退出");
      builder.setMessage("确定要退出吗？");
      builder.setNegativeButton(android.R.string.cancel, null);
      builder.setPositiveButton(android.R.string.ok, doPositiveClick);
    }
  }

  public class AboutDialog extends DialogsAlertDialogFragment {
    private String currentVersion;
    private String newestVersion;
    private String address;

    public AboutDialog(String currentVersion, String newestVersion, String address) {
      this.currentVersion = currentVersion;
      this.newestVersion = newestVersion;
      this.address = address;
    }

    private android.content.DialogInterface.OnClickListener doPositiveClick = new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (!currentVersion.equals(newestVersion)) {
          Intent intent = new Intent();
          intent.setAction("android.intent.action.VIEW");
          Uri content_url = Uri.parse(address);
          intent.setData(content_url);
          startActivity(intent);
        }
      }
    };

    @Override
    protected void prepareBuilder(Builder builder) {
      builder.setTitle("关于孔夫子");
      String message = "您当前的版本是" + currentVersion + ",最新的版本是" + newestVersion + "";
      if (!currentVersion.equals(newestVersion)) {
        message += "是否前去官网下载更新？";
      }
      builder.setMessage(message);

      builder.setPositiveButton(android.R.string.ok, doPositiveClick);
    }
  }

  public class GetNewestVersionThread extends Thread {
    private MyDialog myDialog;
    @Override
    public void run() {
      getSupportActivity().runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(getSupportActivity());
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      
      try {
        JSONObject jsonObject = client.getNewestVersion();
        JSONObject object = jsonObject.getJSONObject("teacher").getJSONObject("android");
        final String version = object.getString("version");
        final String address = object.getString("address");
        Log.e(DEBUG_TAG, version + " " + address);
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            AboutDialog aboutDialog = new AboutDialog(MainApplication.prefs.getString(Constants.VERSION, ""), version,
                address);
            aboutDialog.show(getSupportActivity().getSupportFragmentManager(), "AboutDialog");
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        getSupportActivity().runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }
    }
  }
}
