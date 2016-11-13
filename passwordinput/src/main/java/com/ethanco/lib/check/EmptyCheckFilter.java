package com.ethanco.lib.check;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.ethanco.lib.R;
import com.ethanco.lib.abs.ICheckPasswordFilter;

/**
 * Created by EthanCo on 2016/11/13.
 */

public class EmptyCheckFilter implements ICheckPasswordFilter {

    @Override
    public boolean onCheckPassword(Context context, CharSequence password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.please_input_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
