package com.ethanco.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ethanco.lib.PasswordDialog;
import com.ethanco.lib.PasswordInput;
import com.ethanco.lib.abs.OnPositiveButtonListener;

public class MainActivity extends AppCompatActivity {

    private PasswordInput passwordInputFirst;
    private PasswordInput passwordInputAgain;
    private Button btnShowDialog;
    private Button btnShowDialogSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInputFirst = (PasswordInput) findViewById(R.id.passwordInput_first);
        passwordInputAgain = (PasswordInput) findViewById(R.id.passwordInput_again);
        btnShowDialogSimple = (Button) findViewById(R.id.btn_show_dialog_simple);
        btnShowDialog = (Button) findViewById(R.id.btn_show_dialog);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwdFirst = passwordInputFirst.getText().toString();
                Toast.makeText(MainActivity.this, "密码是:" + pwdFirst, Toast.LENGTH_SHORT).show();
            }
        });


        //Text长度改变监听
        passwordInputFirst.setTextLenChangeListener(new PasswordInput.TextLenChangeListener() {
            @Override
            public void onTextLenChange(CharSequence text, int len) {
                if (len == 6) {
                    //do something
                    //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //密码对话框 常用情况
        btnShowDialogSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordDialog.Builder builder = new PasswordDialog.Builder(MainActivity.this)
                        .setTitle(R.string.please_input_password)  //Dialog标题
                        .setBoxCount(4) //设置密码位数
                        .setBorderNotFocusedColor(R.color.colorSecondaryText) //边框颜色
                        .setDotNotFocusedColor(R.color.colorSecondaryText)  //密码圆点颜色
                        .setPositiveListener(new OnPositiveButtonListener() { //确定回调
                            @Override
                            public void onPositiveClick(DialogInterface dialog, int which, String text) {
                                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addCheckPasswordFilter(new CountCheckFilter()); //添加过滤器
                builder.create().show();
            }
        });

        //密码对话框 所有可用配置
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layout可自定义，PasswordInput的tag需为passwordInput_dialog
                //PasswordDialog.Builder builder = new PasswordDialog.Builder(MainActivity.this, R.layout.custom_dialog_password)

                PasswordDialog.Builder builder = new PasswordDialog.Builder(MainActivity.this) //使用默认布局
                        .setTitle(R.string.please_input_password)  //Dialog标题
                        .setBoxRadius(3) //每个密码方块的Radius dp
                        .setBoxMarge(3) //每个密码间隔的距离 dp
                        .setBoxCount(4) //设置密码位数 dp
                        //当没有获得焦点时的颜色，若setFocusColorChangeEnable为false，则无论是否获取焦点，
                        //都是set_____NotFocusedColor的颜色，Border为边框，Not为密码圆点
                        .setBorderNotFocusedColor(R.color.colorSecondaryText)
                        .setDotNotFocusedColor(R.color.colorSecondaryText)
                        //.setBorderFocusedColor(R.color.colorAccent) //当获得焦点时的颜色 setFocusColorChangeEnable为true时生效
                        //.setDotFocusedColor(R.color.colorAccent)
                        //.setBackgroundColor(R.color.colorPrimaryDark) //绘制背景色，当有margin或padding时生效
                        .setFocusColorChangeEnable(false)
                        .setPositiveText("确定")
                        .setPositiveListener(new OnPositiveButtonListener() {
                            @Override
                            public void onPositiveClick(DialogInterface dialog, int which, String text) {
                                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeText("取消")
                        .setNegativeListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //.setCheckPasswordFilers() //设置过滤器List 这将清除默认和之前设置的所有过滤器
                        .addCheckPasswordFilter(new CountCheckFilter()) //添加过滤器
                        .setCancelable(false); //dialog弹出后点击空白处或物理返回键，dialog不消失
                builder.create().show();
            }
        });
    }
}
