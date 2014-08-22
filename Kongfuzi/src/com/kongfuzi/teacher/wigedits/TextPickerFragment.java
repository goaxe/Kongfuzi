package com.kongfuzi.teacher.wigedits;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.AlertDialog.Builder;

import com.kongfuzi.teacher.R;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TextPickerFragment extends DialogFragment {
  public ClearableEditText mEditText;
  public String mTitle;

  private android.content.DialogInterface.OnClickListener doNegativeClick = new android.content.DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
  };

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity(),
        getTheme());

    View view = getLayoutInflater().inflate(R.layout.text_picker);
    mEditText = (ClearableEditText) view.findViewById(R.id.edittext);

    builder.setTitle(mTitle);
    builder.setView(view);
    builder.setNegativeButton(android.R.string.cancel, doNegativeClick);

    prepareBuilder(builder);
    return builder.create();
  }

  protected void prepareBuilder(Builder builder) {
  }
}
