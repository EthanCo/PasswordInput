package com.ethanco.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.ethanco.lib.abs.OnPositiveButtonListener;


/**
 * @Description 密码对话框
 * Created by EthanCo on 2016/7/5.
 */
public class PasswordDialog {
    /**
     * @param context
     * @param title
     * @param positiveListener 确定按钮 监听
     * @param negativeListener 取消按钮 监听
     */
    public static void show(Context context, String title, final OnPositiveButtonListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.dialog_password, null);
        final PasswordInput pwdInput = (PasswordInput) textEntryView.findViewById(R.id.pwdInput_dialog);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle(title);
        builder.setView(textEntryView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pwdStr = pwdInput.getText().toString();
                if (checkPwd(pwdStr)) {
                    if (null != positiveListener) {
                        positiveListener.onPositiveClick(dialog, which, pwdStr);
                    }
                }
            }
        });

        if (null != negativeListener) {
            builder.setNegativeButton("取消", negativeListener);
        }
        builder.create().show();
    }

    public static void show(Context context, String title, OnPositiveButtonListener positiveListener) {
        show(context, title, positiveListener, null);
    }

    private static boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            //T.show(R.string.please_input_password);
        } else {
            return true;
        }
        return false;
    }
}
