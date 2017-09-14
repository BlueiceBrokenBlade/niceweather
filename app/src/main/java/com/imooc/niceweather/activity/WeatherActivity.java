package com.imooc.niceweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooc.niceweather.R;
import com.imooc.niceweather.util.HttpCallbackListener;
import com.imooc.niceweather.util.HttpUtil;
import com.imooc.niceweather.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    private TextView mTextCityName;
    private TextView mTextPublishTime;
    private TextView mTextDesp;
    private TextView mTextTemp1;
    private TextView mTextTemp2;
    private TextView mTextCurrentTime;
    private Button mBtnSwitchCity;
    private Button mBtnRefreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        initView();
        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            //有县级代号就去查询天气
            mTextPublishTime.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            mTextCityName.setVisibility(View.VISIBLE);
            queryWeatherCode(countyCode);
        } else {
            //没有县级代号就直接显示本地天气
            showWeather();
        }


    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather() {
        SharedPreferences sdf = getSharedPreferences("weatherInfo", MODE_PRIVATE);
        mTextCityName.setText(sdf.getString("city_name", ""));
        mTextTemp1.setText(sdf.getString("temp1", ""));
        mTextTemp2.setText(sdf.getString("temp2", ""));
        mTextDesp.setText(sdf.getString("weather_desp", ""));
        mTextPublishTime.setText(sdf.getString("publish_time", "") + "发布");
        mTextCurrentTime.setText(sdf.getString("current_time", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 查询县级代号所对应的天气代号
     * @param countyCode
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气信息
     * @param weatherCode
     */
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String respose) {
                if("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(respose)){
                        //查询解析服务器返回的天气代号
                        String[] array = respose.split("\\|");
                        if(array != null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if("weatherCode".equals(type)){
                    //解析天气数据并存入ShardPerferences中
                    Utility.handleWeatherResponse(WeatherActivity.this, respose);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //在界面上显示天气
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextPublishTime.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        weatherInfoLayout = (LinearLayout) findViewById(R.id.layout_weather_info);
        mTextCityName = (TextView) findViewById(R.id.text_city);
        mTextPublishTime = (TextView) findViewById(R.id.text_publishTime);
        mTextDesp = (TextView) findViewById(R.id.text_weather_describe);
        mTextTemp1 = (TextView) findViewById(R.id.text_temp1);
        mTextTemp2 = (TextView) findViewById(R.id.text_temp2);
        mTextCurrentTime = (TextView) findViewById(R.id.text_current_data);
        mBtnSwitchCity = (Button) findViewById(R.id.btn_switch_city);
        mBtnSwitchCity.setOnClickListener(this);
        mBtnRefreshWeather = (Button) findViewById(R.id.btn_refresh_weather);
        mBtnRefreshWeather.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_refresh_weather:
                mTextPublishTime.setText("正在同步");
                //重新获得县的ID，重新从服务器解析天气信息并刷新SharedPreferences文件中
                SharedPreferences prefs = getSharedPreferences("weatherInfo", MODE_PRIVATE);
                String weatherCode = prefs.getString("weather_code", "");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
