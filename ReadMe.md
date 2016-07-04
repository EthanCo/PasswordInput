# PasswordInput #
密码输入框  

![PasswordInput](/passwordInput.gif)

## 自定义属性 ##

	<!--背景色-->
    <attr name="myBackground" format="color" />
    <!--每个字符的边框边角-->
    <attr name="myBoxRadius" format="dimension" />
    <!--每个字符的边框颜色-->
    <attr name="myBoxBorderColor" format="color" />
    <!--每个字符的边框宽度-->
    <attr name="myBoxBorderWidth" format="dimension" />
    <!--每个字符marge-->
    <attr name="myBoxMarge" format="dimension" />
    <!--每个字符的边框颜色-->
    <attr name="myPasswordColor" format="color" />
    <!--密码字符宽度-->
    <attr name="myPasswordWidth" format="dimension" />
    <!--密码的长度-->
    <attr name="myPasswordLength" format="integer" />  


## 使用 ##

在你的项目中加入PasswordInput Library，之后在xml使用即可  

	<com.ethanco.lib.PasswordInput
    android:id="@+id/passwordInput"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:cursorVisible="false"
    android:focusable="true"
    android:inputType="number"
    android:maxLength="6"
    app:myBackground="@android:color/white"
    app:myBoxBorderColor="@color/colorAccent"
    app:myBoxBorderWidth="1dp"
    app:myBoxMarge="8dp"
    app:myBoxRadius="10dp"
    app:myPasswordColor="@color/colorAccent"
    app:myPasswordLength="6"
    app:myPasswordWidth="8dp" />

### 设置内容改变监听 ###

	PasswordInput passwordInput = (PasswordInput) findViewById(R.id.passwordInput);
        passwordInput.setTextLenChangeListen(new PasswordInput.TextLenChangeListen() {
            @Override
            public void onTextLenChange(CharSequence text, int len) {
               //do something
            }
        });