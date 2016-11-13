# PasswordInput #
密码输入框    

![](http://i4.piimg.com/567571/1b0798bc3c3e3cca.gif)  

![](/passwordInput.gif)  

![](http://yotuku.cn/link?url=BkTFp8HWl&tk_plan=free&tk_storage=tietuku&tk_vuid=0b1372f4-d75a-448f-b3c6-484b81b02b70&tk_time=2016111311)  

![](/PasswordDialog.jpg)

v1.5
  
- 增加了PasswordDialog，采用Buider模式，可直接使用
- 精简了代码和自定义属性，只保留主要功能，以降低代码大小和使用成本
- 获得焦点时可改变颜色，区分获得焦点的控件和未获得焦点的控件
- 具有过渡动画，在输入、删除、焦点切换时进行较好的过渡，解决用户使用时的突兀感
- 使字符方块固定为正方形，取消了长方形的情况

## 自定义属性 ##

	<!--获得焦点时的颜色-->
    <attr name="focusedColor" format="color" />
    <!--未获得焦点时的颜色-->
    <attr name="notFocusedColor" format="color" />
    <!--背景色-->
    <attr name="backgroundColor" format="color" />
    <!--字符方块的数量-->
    <attr name="boxCount" format="integer" />
    <!--获得焦点时颜色是否改变-->
    <attr name="focusColorChangeEnable" format="boolean" />
    <!--圆点半径-->
    <attr name="dotRaduis" format="dimension" />

## 使用 ##

在你的项目中加入PasswordInput Library，之后在xml使用即可  
	
	<com.ethanco.lib.PasswordInput
    android:id="@+id/passwordInput_first"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    app:boxCount="6"
    app:focusedColor="@color/colorAccent"
    app:notFocusedColor="@color/colorPrimary" />

### 获得输入的内容 ###

	PasswordInput passwordInput = (PasswordInput) findViewById(R.id.passwordInput);
	String pwdFirst = passwordInput.getText().toString();

### 设置内容改变监听 ###

    passwordInput.setTextLenChangeListen(new PasswordInput.TextLenChangeListen() {
        @Override
        public void onTextLenChange(CharSequence text, int len) {
           //do something
        }
    });