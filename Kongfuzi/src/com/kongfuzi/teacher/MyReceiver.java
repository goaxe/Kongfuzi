package com.kongfuzi.teacher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.kongfuzi.teacher.internal.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
  private static final String DEBUG_TAG = "MyReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle bundle = intent.getExtras();
    Log.e(DEBUG_TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

    if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
      String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
      Log.e(DEBUG_TAG, "[MyReceiver] 接收Registration Id : " + regId);
      // send the Registration Id to your server...

    } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
      Log.e(DEBUG_TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
      processCustomMessage(context, bundle);

    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
      Log.e(DEBUG_TAG, "[MyReceiver] 接收到推送下来的通知");
      int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
      String notifactionTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String notifactionMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
      Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
      String notifactionDate = formatter.format(curDate);
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(Constants.DATE, notifactionDate).put(Constants.TITLE, notifactionTitle).put(Constants.MESSAGE, notifactionMessage);
        Set<String> notifactionSet = MainApplication.prefs.getStringSet(Constants.SYSTEM_NOTIFICATION, null);
        if (notifactionSet == null) {
          notifactionSet = new HashSet<String>();
        }
        notifactionSet.add(jsonObject.toString());
        MainApplication.prefs.edit().putStringSet(Constants.SYSTEM_NOTIFICATION, notifactionSet).commit();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      
      Log.e(DEBUG_TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
      Log.e(DEBUG_TAG, "[MyReceiver] 用户点击打开了通知");

      JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));

      // 打开自定义的Activity
      Intent i = new Intent(context, MainActivity.class);
      intent.putExtra("huangjian", printBundle(bundle));
      // i.putExtras(bundle);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(i);

    } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
      Log.e(DEBUG_TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
      // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

    } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
      boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
      Log.e(DEBUG_TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
    } else {
      Log.e(DEBUG_TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
    }
  }

  // 打印所有的 intent extra 数据
  private static String printBundle(Bundle bundle) {
    StringBuilder sb = new StringBuilder();
    for (String key : bundle.keySet()) {
      if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
      } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
      } else {
        sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
      }
    }
    return sb.toString();
  }

  // send msg to MainActivity
  private void processCustomMessage(Context context, Bundle bundle) {
    if (MainActivity.isForeground) {
      String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
      String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
      Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
      msgIntent.putExtra(Constants.KEY_MESSAGE, message);
      try {
        JSONObject extraJson = new JSONObject(extras);
        if (null != extraJson && extraJson.length() > 0) {
          msgIntent.putExtra(Constants.KEY_EXTRAS, extras);
        }
      } catch (JSONException e) {

      }

      context.sendBroadcast(msgIntent);
    }
  }
}
