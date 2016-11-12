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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInputFirst = (PasswordInput) findViewById(R.id.passwordInput_first);
        passwordInputAgain = (PasswordInput) findViewById(R.id.passwordInput_again);
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
        /*passwordInputFirst.setTextLenChangeListener(new PasswordInput.TextLenChangeListener() {
            @Override
            public void onTextLenChange(CharSequence text, int len) {
                if (len == 6) {
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordDialog.show(MainActivity.this, "Title", new OnPositiveButtonListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which, String text) {
                        Toast.makeText(MainActivity.this, "text:" + text, Toast.LENGTH_SHORT).show();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
    }
}
