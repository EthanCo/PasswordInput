package com.ethanco.passwordinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PasswordInput passwordInput = (PasswordInput) findViewById(R.id.passwordInput);
//        passwordInput.setTextLenChangeListen(new PasswordInput.TextLenChangeListen() {
//            @Override
//            public void onTextLenChange(CharSequence text, int len) {
//                if (len == 6) {
//                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
