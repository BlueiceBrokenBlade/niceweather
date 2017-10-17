package com.imooc.niceweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import cn.bmob.v3.Bmob;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化BmobSDK
        Bmob.initialize(this, "c251cdd7e6ee343353f0c6666273705c");
    }

    Toast mToast;
    public void ShowToast(final String text){
        if(!TextUtils.isEmpty(text)){
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (mToast == null) {
                       mToast = Toast.makeText(getApplicationContext(), text,
                               Toast.LENGTH_LONG);
                   } else {
                       mToast.setText(text);
                   }
                   mToast.show();
               }
           });
        }
    }
}
