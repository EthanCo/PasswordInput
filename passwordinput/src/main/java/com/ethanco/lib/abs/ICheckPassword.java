package com.ethanco.lib.abs;

import android.content.Context;

/**
 * Created by EthanCo on 2016/11/13.
 */

public interface ICheckPassword {
    boolean onCheckPassword(Context context, CharSequence password);
}
