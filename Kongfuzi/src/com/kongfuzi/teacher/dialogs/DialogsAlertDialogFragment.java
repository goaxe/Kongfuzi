package com.kongfuzi.teacher.dialogs;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.AlertDialog.Builder;

import com.kongfuzi.teacher.R;

import android.os.Bundle;

public class DialogsAlertDialogFragment extends DialogFragment {
    public DialogsAlertDialogFragment() {
        setDialogType(DialogType.AlertDialog);
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActivity());
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_launcher);
        prepareBuilder(builder);
        return builder.create();
    }

    protected void prepareBuilder(Builder builder) {
    }
}
