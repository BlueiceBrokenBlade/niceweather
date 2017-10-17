package com.imooc.niceweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.imooc.niceweather.R;
import com.imooc.niceweather.model.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xhx12366 on 2017-06-04.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener{
    EditText  et_username,et_password;
    Button btn_login;
    TextView btn_register;
    CheckBox checkBox_remember;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_login);


        sp=getSharedPreferences("UserInfo",MODE_PRIVATE);

        et_username= (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        btn_login= (Button) findViewById(R.id.btn_login);
        btn_register= (TextView) findViewById(R.id.btn_register);
        checkBox_remember= (CheckBox) findViewById(R.id.checkBox_remember);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        et_username.setText(sp.getString("UserName",""));
        et_password.setText(sp.getString("UserPassword",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                Intent intent=new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login(){
        final String name=et_username.getText().toString();
        final String password=et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast("用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast("密码不能为空");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(UserLoginActivity.this);
        progress.setMessage("正在登陆...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        BmobUser user=new BmobUser();
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<User>() {

            @Override
            public void done(User o, BmobException e) {
                if(e==null){
                    editor=sp.edit();
                    if(checkBox_remember.isChecked()){
                        editor.putString("UserName",name);
                        editor.putString("UserPassword",password);
                    }else{
                        editor.putString("UserName","");
                        editor.putString("UserPassword","");
                    }
                    editor.putBoolean("isLogin", true);
                    editor.commit();
                    progress.dismiss();
//                    ShowToast("登录成功");
                    Intent intent=new Intent(UserLoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ShowToast("登录失败");
                    progress.dismiss();
                }
            }
        });
    }
}

