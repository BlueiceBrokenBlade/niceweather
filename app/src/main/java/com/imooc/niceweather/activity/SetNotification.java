package com.imooc.niceweather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.niceweather.R;
import com.imooc.niceweather.model.Constant;
import com.imooc.niceweather.util.MyLog;

import java.util.Objects;

public class SetNotification extends Activity implements View.OnClickListener{
    private ImageView mImageBack;
    private ImageView mImageStartNotification;
    private RelativeLayout mLayoutTextColor;
    private RelativeLayout mLayoutBackgroundColor;
    private TextView mTextCurrentTextColor;

    private String wendu;//温度
    private String minWendu;//最低温度
    private String maxWendu;//最高温度
    private String weather;//天气
    private String quality;//空气质量
    private RemoteViews rv;
    private NotificationManager manager;

    private boolean isStartNotification = false;//通知开启与否标志
    private int notificationTextColor = 0;
    private int notificationBackgroundColor = 0;

    private int currentTextColorId = 0;//当前通知颜色id
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_notification);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStartNotification = (Boolean) loadSetAttribute("isStratNotification");//通知开关状态
        currentTextColorId = (int)loadSetAttribute("notificationTextColor");//当前通知颜色选择

        mTextCurrentTextColor.setText(getResources().getTextArray(R.array.notification_text_color_choose)[currentTextColorId]);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //获取设置的属性值
        notificationBackgroundColor = (int)loadSetAttribute("notificationBackgroundColor");
        MyLog.e("SetNotification:", "文本颜色：" + notificationTextColor + "背景颜色：" + notificationBackgroundColor);

        mImageBack = (ImageView) findViewById(R.id.image_back);
        mImageStartNotification = (ImageView) findViewById(R.id.image_startNotification);
        mLayoutTextColor = (RelativeLayout) findViewById(R.id.layout_textColor);
        mLayoutBackgroundColor = (RelativeLayout) findViewById(R.id.layout_backgroundColor);
        mTextCurrentTextColor = (TextView) findViewById(R.id.text_currentTextColor);

        mImageBack.setOnClickListener(this);
        mImageStartNotification.setOnClickListener(this);
        mLayoutTextColor.setOnClickListener(this);
        mLayoutBackgroundColor.setOnClickListener(this);

        if (isStartNotification){
            mImageStartNotification.setImageResource(R.drawable.choose_yes);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.image_startNotification:
                if(!isStartNotification){
                    mImageStartNotification.setImageResource(R.drawable.choose_yes);
                    MyLog.e("SetNotification", "发送了一个通知");
                    showNotification(currentTextColorId, notificationBackgroundColor);
                    isStartNotification = true;
                }else{
                    mImageStartNotification.setImageResource(R.drawable.choose_no);
                    if(manager != null){
                        manager.cancel(NOTIFICATION_ID);
                    }
                    isStartNotification = false;
                }
                //保存通知栏开关的属性值
                saveSetAttribute("isStratNotification", isStartNotification);
                break;
            case R.id.layout_textColor:
                reviseTextColor();
                break;
            case R.id.layout_backgroundColor:
                reviseBackgroundColor();
                break;
        }
    }

    /**
     * 更改通知栏背景颜色
     */
    private void reviseBackgroundColor() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("背景颜色")
                .setSingleChoiceItems(R.array.notification_text_color_choose, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                break;
                            case 1:
                                setNotificationColor(getResources().getColor(R.color.notification_red), "background");
                                break;
                            case 2:
                                setNotificationColor(getResources().getColor(R.color.notification_green), "background");
                                break;
                            case 3:
                                setNotificationColor(getResources().getColor(R.color.notification_yellow), "background");
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    /**
     * 更改通知栏字体颜色
     */
    private void reviseTextColor() {
        notificationBackgroundColor = (int) loadSetAttribute("notificationBackgroundColor");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("文字颜色")
                .setSingleChoiceItems(R.array.notification_text_color_choose, currentTextColorId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case Constant.NOTIFICATION_TEXT_DEFAULT:
                                if(Constant.NOTIFICATION_TEXT_DEFAULT != currentTextColorId) {
                                    showNotification(Constant.NOTIFICATION_TEXT_DEFAULT,
                                            Constant.NOTIFICATION_BACKGROUND_COLOR[notificationBackgroundColor]);
                                    saveSetAttribute("notificationTextColor", Constant.NOTIFICATION_TEXT_DEFAULT);
                                }
                                break;
                            case Constant.NOTIFICATION_TEXT_RED:
                                if(Constant.NOTIFICATION_TEXT_RED != currentTextColorId) {
                                    showNotification(Constant.NOTIFICATION_TEXT_RED,
                                            Constant.NOTIFICATION_BACKGROUND_COLOR[notificationBackgroundColor]);
                                    saveSetAttribute("notificationTextColor", Constant.NOTIFICATION_TEXT_RED);
                                }
                                break;
                            case Constant.NOTIFICATION_TEXT_GREEN:
                                if(Constant.NOTIFICATION_TEXT_GREEN != currentTextColorId) {
                                    showNotification(Constant.NOTIFICATION_TEXT_GREEN,
                                            Constant.NOTIFICATION_BACKGROUND_COLOR[notificationBackgroundColor]);
                                    saveSetAttribute("notificationTextColor", Constant.NOTIFICATION_TEXT_GREEN);
                                }
                                break;
                            case Constant.NOTIFICATION_TEXT_YELLOW:
                                if(Constant.NOTIFICATION_TEXT_YELLOW != currentTextColorId) {
                                    showNotification(Constant.NOTIFICATION_TEXT_YELLOW,
                                            Constant.NOTIFICATION_BACKGROUND_COLOR[notificationBackgroundColor]);
                                    saveSetAttribute("notificationTextColor", Constant.NOTIFICATION_TEXT_YELLOW);
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    /**
     * 设置通知栏文本颜色或者背景颜色
     * @param colorId 颜色数组下标
     */
    private void setNotificationColor(int colorId, String type) {
        if(type.equals("text")){
            rv.setTextColor(R.id.text_wendu, getColorByColorId(colorId));
            rv.setTextColor(R.id.text_minWendu, getColorByColorId(colorId));
            rv.setTextColor(R.id.text_spaceMark, getColorByColorId(colorId));
            rv.setTextColor(R.id.text_maxWendu, getColorByColorId(colorId));
            rv.setTextColor(R.id.text_weather, getColorByColorId(colorId));
            rv.setTextColor(R.id.text_quality, getColorByColorId(colorId));
        }else if(type.equals("background")){
        }

    }

    /**
     * 提取封装通过下标获得颜色数组的R.color.*子元素
     * @param colorId 颜色数组下标
     * @return R.color.
     */
    private int getColorByColorId(int colorId){
        return getResources().getColor(Constant.NOTIFICATION_TEXT_COLOR[colorId]);
    }

    /**
     * 显示天气通知栏
     */
    private void showNotification(int textColorId, int backgroundColor) {
        //获取通知栏提示信息
        SharedPreferences sdf = getSharedPreferences("weatherInfo", MODE_PRIVATE);
        wendu = sdf.getString("wendu", "");
        minWendu = sdf.getString("today_low", "");
        maxWendu = sdf.getString("today_high", "");
        weather = sdf.getString("today_type", "");
        quality = sdf.getString("quality", "");

        manager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);
        //通知响应事件
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //设置一个远程视图作为自定义视图
        rv = new RemoteViews(getPackageName(),
                R.layout.notification_layout);
        rv.setTextViewText(R.id.text_wendu, wendu);
        rv.setTextViewText(R.id.text_minWendu, minWendu);
        rv.setTextViewText(R.id.text_maxWendu, maxWendu);
        rv.setTextViewText(R.id.text_weather, weather);
        rv.setTextViewText(R.id.text_quality, quality);
        rv.setTextColor(R.id.text_quality, getResources().getColor(R.color.notification_red));

        setNotificationColor(textColorId, "text");//设置文本颜色
        rv.setTextColor(R.layout.notification_layout, getResources().getColor(R.color.notification_yellow));

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.switch1)
//                .setContentTitle("标题")
//                .setContentText("内容")
                .setCustomContentView(rv) //自定义通知栏视图
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent) //点击意图
                .setOngoing(true);

        manager.notify(NOTIFICATION_ID, builder.build());

    }

    /**
     * 通过类型存储相应的设置属性
     * @param type 设置的类型
     * @param value 设置的属性值
     */
    private void saveSetAttribute(String type, Object value){
        SharedPreferences.Editor editor = getSharedPreferences("setInfo", MODE_PRIVATE).edit();

        if(type.equals("isStratNotification")){
            editor.putBoolean("isStratNotification", (Boolean) value);
        }else if(type.equals("notificationTextColor")){
            editor.putInt("notificationTextColor", (int) value);
        }else if(type.equals("notificationBackgroundColor")){
            editor.putInt("notificationBackgroundColor", (int) value);
        }

        editor.commit();
    }

    /**
     * 通过类型获得设置的属性值
     * @param type 设置类型
     * @return 属性值
     */
    private Object loadSetAttribute(String type){
        SharedPreferences sdf = getSharedPreferences("setInfo", MODE_PRIVATE);
        if(type.equals("isStratNotification")){
            return sdf.getBoolean("isStratNotification", false);
        }else if(type.equals("notificationTextColor")){
            return sdf.getInt("notificationTextColor", 0);
        }else if(type.equals("notificationBackgroundColor")){
            return sdf.getInt("notificationBackgroundColor", 0);
        }

        return null;
    }
}
