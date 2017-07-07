package com.ethanco.lib;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ethanco.lib.abs.ICheckPasswordFilter;
import com.ethanco.lib.abs.OnPositiveButtonListener;
import com.ethanco.lib.check.EmptyCheckFilter;

import java.util.ArrayList;
import java.util.List;

import static com.ethanco.lib.utils.Utils.dp2px;
import static com.ethanco.lib.utils.Utils.getColor;


/**
 * @Description 密码对话框
 * Created by EthanCo on 2016/7/5.
 */
public class PasswordDialog {
    private final AlertDialog mDialog;

    public PasswordDialog(Builder builder) {
        final Context context = builder.context;
        final String title = builder.title;
        final View entityView = builder.entryView;
        final PasswordInput passwordInput = builder.passwordInput;
        final String positiveText = builder.positiveText;
        final String negativeText = builder.negativeText;
        final OnPositiveButtonListener positiveListener = builder.positiveListener;
        final DialogInterface.OnClickListener negativeListener = builder.negativeListener;
        final List<ICheckPasswordFilter> checkpasswordFilers = builder.checkPasswordFilers;
        final boolean isCancelable = builder.isCancelable;

        if (null == passwordInput) {
            throw new IllegalArgumentException("passwordInput is null，please check tag is " + builder.SIGN);
        }

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(title);
        b.setView(entityView);
        b.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pwdStr = passwordInput.getText().toString();
                if (checkPwd(context, pwdStr, checkpasswordFilers)) {
                    if (null != positiveListener) {
                        positiveListener.onPositiveClick(dialog, which, pwdStr);
                    }
                }
            }
        });
        if (null != negativeListener) {
            b.setNegativeButton(negativeText, negativeListener);
        }
        b.setCancelable(isCancelable);
        mDialog = b.create();
    }

    private boolean checkPwd(Context context, CharSequence password, List<ICheckPasswordFilter> checkpasswordList) {
        if (null == checkpasswordList) return true;

        for (ICheckPasswordFilter checkPassword : checkpasswordList) {
            if (!checkPassword.onCheckPassword(context, password)) {
                return false;
            }
        }
        return true;
    }

    public void show() {
        mDialog.show();
    }

    public static class Builder {

        public static final String SIGN = "passwordInput_dialog";
        private final PasswordInput passwordInput;
        private final View entryView;
        private Context context;
        private String title;
        private OnPositiveButtonListener positiveListener; //"确定"监听
        private DialogInterface.OnClickListener negativeListener; //"取消"监听
        private String positiveText;
        private String negativeText;
        private List<ICheckPasswordFilter> checkPasswordFilers;
        private boolean isCancelable = true;

        public Builder(Context context) {
            this(context, R.layout.dialog_password);
        }

        public Builder(Context context, int layoutRes) {
            this.context = context;
            LayoutInflater factory = LayoutInflater.from(context);
            entryView = factory.inflate(layoutRes, null);
            passwordInput = (PasswordInput) entryView.findViewWithTag(SIGN);

            title = context.getString(R.string.please_input_password);
            positiveText = context.getString(R.string.sure);
            negativeText = context.getString(R.string.cancel);
            checkPasswordFilers = new ArrayList<>();
            checkPasswordFilers.add(new EmptyCheckFilter());
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int stringId) {
            return setTitle(context.getString(stringId));
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setPositiveText(int stringId) {
            return setPositiveText(context.getString(stringId));
        }

        public Builder setPositiveListener(OnPositiveButtonListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setNegativeText(int stringId) {
            setNegativeText(context.getString(stringId));
            return this;
        }

        public Builder setNegativeListener(DialogInterface.OnClickListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public Builder setTextLenChangeListener(PasswordInput.TextLenChangeListener lenListener) {
            passwordInput.setTextLenChangeListener(lenListener);
            return this;
        }

        public Builder addCheckPasswordFilter(ICheckPasswordFilter checkPassword) {
            if (!checkPasswordFilers.contains(checkPassword)) {
                checkPasswordFilers.add(checkPassword);
            }
            return this;
        }

        public Builder setCheckPasswordFilers(List<ICheckPasswordFilter> newCheckPassowrdList) {
            this.checkPasswordFilers = newCheckPassowrdList;
            return this;
        }

        public Builder setBorderNotFocusedColor(@ColorRes int borderNotFocusedColor) {
            passwordInput.setBorderNotFocusedColor(getColor(context, borderNotFocusedColor));
            return this;
        }

        public Builder setBorderFocusedColor(@ColorRes int borderFocusedColor) {
            passwordInput.setBorderFocusedColor(getColor(context, borderFocusedColor));
            return this;
        }

        public Builder setBorderWidth(int borderWidth) {
            passwordInput.setBorderWidth(borderWidth);
            return this;
        }

        public Builder setDotNotFocusedColor(@ColorRes int dotNotFocusedColor) {
            passwordInput.setDotNotFocusedColor(getColor(context, dotNotFocusedColor));
            return this;
        }

        public Builder setDotFocusedColor(@ColorRes int dotFocusedColor) {
            passwordInput.setDotFocusedColor(getColor(context, dotFocusedColor));
            return this;
        }

        public Builder setDotRadius(float dotRadius) {
            passwordInput.setDotRadius(dotRadius);
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            passwordInput.setBackgroundColor(getColor(context, backgroundColor));
            return this;
        }

        public Builder setBoxCount(int boxCount) {
            passwordInput.setBoxCount(boxCount);
            return this;
        }

        public Builder setBoxMarge(float boxMarge) {
            passwordInput.setBoxMarge(dp2px(context, boxMarge));
            return this;
        }

        public Builder setBoxRadius(float boxRadius) {
            passwordInput.setBoxRadius(dp2px(context, boxRadius));
            return this;
        }

        public Builder setFocusColorChangeEnable(boolean enable) {
            passwordInput.setFocusColorChangeEnable(enable);
            return this;
        }

        public Builder setCancelable(boolean enable) {
            isCancelable = enable;
            return this;
        }

        public PasswordDialog create() {
            return new PasswordDialog(this);
        }
    }
}
