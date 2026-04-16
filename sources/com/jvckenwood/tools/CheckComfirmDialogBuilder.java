package com.jvckenwood.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.jvckenwood.HID_ThinClient.KWD.R;

/* JADX INFO: loaded from: classes.dex */
public class CheckComfirmDialogBuilder {
    private AlertDialog.Builder mBuilder;
    private Context mContext;
    private DialogInterface.OnClickListener mOnPositiveButtonOnClickListener = null;
    private DialogInterface.OnClickListener mOnNegativeButtonOnClickListener = null;
    private CharSequence mPositiveButtonText = null;
    private CharSequence mNegativeButtonText = null;
    private int mPreferenceCheckedId = -1;

    public CheckComfirmDialogBuilder(Context context) {
        this.mBuilder = null;
        this.mContext = null;
        this.mBuilder = new AlertDialog.Builder(context);
        this.mContext = context;
    }

    public CheckComfirmDialogBuilder(Context context, int themeResId) {
        this.mBuilder = null;
        this.mContext = null;
        this.mBuilder = new AlertDialog.Builder(context, themeResId);
        this.mContext = context;
    }

    public CheckComfirmDialogBuilder setTitle(int titleId) {
        this.mBuilder.setTitle(titleId);
        return this;
    }

    public CheckComfirmDialogBuilder setTitle(CharSequence title) {
        this.mBuilder.setTitle(title);
        return this;
    }

    public CheckComfirmDialogBuilder setMessage(int messageId) {
        this.mBuilder.setMessage(messageId);
        return this;
    }

    public CheckComfirmDialogBuilder setMessage(CharSequence message) {
        this.mBuilder.setMessage(message);
        return this;
    }

    public CheckComfirmDialogBuilder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        this.mPositiveButtonText = this.mContext.getText(textId);
        this.mOnPositiveButtonOnClickListener = listener;
        return this;
    }

    public CheckComfirmDialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        this.mPositiveButtonText = text;
        this.mOnPositiveButtonOnClickListener = listener;
        return this;
    }

    public CheckComfirmDialogBuilder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
        this.mNegativeButtonText = this.mContext.getText(textId);
        this.mOnNegativeButtonOnClickListener = listener;
        return this;
    }

    public CheckComfirmDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        this.mNegativeButtonText = text;
        this.mOnNegativeButtonOnClickListener = listener;
        return this;
    }

    public CheckComfirmDialogBuilder setCancelable(boolean cancelable) {
        this.mBuilder.setCancelable(cancelable);
        return this;
    }

    public void setPreferecesCheck(int prefResId) {
        this.mPreferenceCheckedId = prefResId;
    }

    public AlertDialog create() {
        final AlertDialog dialog = this.mBuilder.create();
        LinearLayout layout = (LinearLayout) dialog.getLayoutInflater().inflate(R.layout.dialog_never_check_confirm, (ViewGroup) null);
        final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
        if (this.mPreferenceCheckedId != -1) {
            checkBox.setChecked(PrefsUtils.getBoolean(this.mContext, this.mPreferenceCheckedId, true) ? false : true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.jvckenwood.tools.CheckComfirmDialogBuilder.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Button btn = dialog.getButton(-1);
                    btn.setEnabled(!isChecked);
                }
            });
            dialog.setView(layout);
        }
        if (this.mPositiveButtonText != null || this.mOnPositiveButtonOnClickListener != null) {
            dialog.setButton(-1, this.mPositiveButtonText, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.tools.CheckComfirmDialogBuilder.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog2, int which) {
                    if (CheckComfirmDialogBuilder.this.mPreferenceCheckedId != -1) {
                        PrefsUtils.setBoolean(CheckComfirmDialogBuilder.this.mContext, CheckComfirmDialogBuilder.this.mPreferenceCheckedId, true);
                    }
                    if (CheckComfirmDialogBuilder.this.mOnPositiveButtonOnClickListener != null) {
                        CheckComfirmDialogBuilder.this.mOnPositiveButtonOnClickListener.onClick(dialog2, which);
                    }
                }
            });
        }
        if (this.mNegativeButtonText != null || this.mOnNegativeButtonOnClickListener != null) {
            dialog.setButton(-2, this.mNegativeButtonText, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.tools.CheckComfirmDialogBuilder.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog2, int which) {
                    if (CheckComfirmDialogBuilder.this.mPreferenceCheckedId != -1) {
                        PrefsUtils.setBoolean(CheckComfirmDialogBuilder.this.mContext, CheckComfirmDialogBuilder.this.mPreferenceCheckedId, !checkBox.isChecked());
                    }
                    if (CheckComfirmDialogBuilder.this.mOnNegativeButtonOnClickListener != null) {
                        CheckComfirmDialogBuilder.this.mOnNegativeButtonOnClickListener.onClick(dialog2, which);
                    }
                }
            });
        }
        return dialog;
    }

    public AlertDialog show() {
        return this.mBuilder.show();
    }
}
