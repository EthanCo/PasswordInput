package com.ethanco.lib.abs;

import android.content.DialogInterface;

/**
 * @Description 确认监听
 * Created by EthanCo on 2016/7/7.
 */
public interface OnPositiveButtonListener {
    void onPositiveClick(DialogInterface dialog, int which, String text);
}
