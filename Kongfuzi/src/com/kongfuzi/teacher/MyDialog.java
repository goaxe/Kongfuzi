package com.kongfuzi.teacher;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.ProgressDialog;

public class MyDialog {
  private Activity activity = null;
  private ProgressDialog progressDialog = null;
  private String message = null;
  public MyDialog(Activity activity) {
    this.activity = activity;
    this.message = "正在加载";
    progressDialog = new ProgressDialog(this.activity);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setMessage(this.message);
    progressDialog.setCancelable(false);
    // progressDialog.show();
  }
  
  public MyDialog(Activity activity, String message) {
    this.activity = activity;
    this.message = message;
    progressDialog = new ProgressDialog(this.activity);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setMessage(this.message);
    progressDialog.setCancelable(false);
    // progressDialog.show();
  }
  
  public void show() {
    progressDialog.show();
  }
  
  public void dismiss() {
    progressDialog.dismiss();
  }

}
