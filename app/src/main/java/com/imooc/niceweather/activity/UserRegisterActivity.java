package com.imooc.niceweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import com.imooc.niceweather.R;
import com.imooc.niceweather.model.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 用户注册
 */

public class UserRegisterActivity extends BaseActivity {
    Button btn_register;
    EditText et_username,et_password,et_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_register);


        et_username= (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        et_confirmpassword= (EditText) findViewById(R.id.et_confirmpassword);

        btn_register= (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        String name=et_username.getText().toString();
        String password=et_password.getText().toString();
        String pwd_again=et_confirmpassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            ShowToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ShowToast("密码不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd_again)){
            ShowToast("确认密码不能为空");
            return;
        }
        if(!password.equals(pwd_again)){
            ShowToast("两次密码输入不一致");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(UserRegisterActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        final BmobUser user=new BmobUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    ShowToast("注册成功");
                    progress.dismiss();
                    Intent intent=new Intent(UserRegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ShowToast("注册失败");
                    progress.dismiss();
                }
            }
        });

    }
}
