package com.ethanco.sample;

import android.content.Context;
import android.widget.Toast;

import com.ethanco.lib.abs.ICheckPasswordFilter;

/**
 * Created by EthanCo on 2016/11/13.
 */

public class CountCheckFilter implements ICheckPasswordFilter {
    @Override
    public boolean onCheckPassword(Context context, CharSequence password) {
        if (password.length() != 4) {
            Toast.makeText(context, "密码需为4位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
