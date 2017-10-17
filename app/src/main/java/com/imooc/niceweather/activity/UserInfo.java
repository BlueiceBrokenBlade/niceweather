package com.imooc.niceweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooc.niceweather.R;
import com.imooc.niceweather.activity.fragment.SetFragment;
import com.imooc.niceweather.util.Utility;

import cn.bmob.v3.BmobUser;

public class UserInfo extends BaseActivity implements View.OnClickListener{
    private ImageView mImageBack;
    private Button mBtnExitLogin;
    private TextView mTextNickName;
    private TextView mTextSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);

        initView();

        BmobUser user = BmobUser.getCurrentUser();

        mTextNickName.setText(user.getUsername().toString());

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mImageBack = (ImageView) findViewById(R.id.image_back);
        mBtnExitLogin = (Button) findViewById(R.id.btn_exitLogin);

        mTextNickName = (TextView) findViewById(R.id.text_nickName);
        mTextSex = (TextView) findViewById(R.id.text_sex);

        mImageBack.setOnClickListener(this);
        mBtnExitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_exitLogin:
                Utility.exitLogin(UserInfo.this);
                finish();
                break;
        }
    }
}
