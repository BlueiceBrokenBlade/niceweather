package com.imooc.niceweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooc.niceweather.R;
import com.imooc.niceweather.model.Constant;
import com.imooc.niceweather.util.HttpCallbackListener;
import com.imooc.niceweather.util.HttpUtil;
import com.imooc.niceweather.util.MyLog;
import com.imooc.niceweather.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    private LinearLayout forecastLayout;
    private LinearLayout switchLayout;

    private Button mBtnSwitchCity;

    private TextView mTextDate; //今日日期
    private TextView mTextCityName; //城市名
    private TextView mTextWendu; //温度
    private TextView mTextMinWendu; //最低温度
    private TextView mTextMaxWendu; //最高温度
    private TextView mTextShidu; //湿度
    private TextView mTextFengXiang;
    private TextView mTextFengji; //风级
    private TextView mTextPm2; //PM2.5
    private TextView mTextPm10; //PM10
    private TextView mTextAqi; //aqi
    private TextView mTextAirType; //空气质量
    private TextView mTextNotice; //提醒

    private TextView mTextTypeYesterday; //昨日天气类型
    private TextView mTextMinWenduYesterday; //昨日最小温度
    private TextView mTextMaxWenduYesterday; //昨日最大温度
    private TextView mTextDateYesterday; //昨日日期

    private TextView mTextTypeForecastOneday; //未来第一天天气类型
    private TextView mTextMinWenduForcastOneday; //未来第一天最低温度
    private TextView mTextMaxWenduForcastOneday; //未来第一天最高温度
    private TextView mtextDateForcastOneday; //未来第一天日期

    private TextView mTextTypeForecastTwoday; //未来第二天天气类型
    private TextView mTextMinWenduForcastTwoday; //未来第二天最低温度
    private TextView mTextMaxWenduForcastTwoday; //未来第二天最高温度
    private TextView mtextDateForcastTwoday; //未来第二天日期

    private TextView mTextTypeForecastThreeday; //未来第三天天气类型
    private TextView mTextMinWenduForcastThreeday; //未来第三天最低温度
    private TextView mTextMaxWenduForcastThreeday; //未来第三天最高温度
    private TextView mtextDateForcastThreeday; //未来第三天日期


    private ImageView mImageWeather; //今日天气图片
    private ImageView mImageNotice; //今日提醒图片
    private ImageView mImageYesteday; //昨日天气图片
    private ImageView mImageForecastOneday; //未来第一天天气图片
    private ImageView mImageForecastTwoday; //未来第二天天气图片
    private ImageView mImageForecastThreeday; //未来第三天天气图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_weather);

        initView();
        String countyName = getIntent().getStringExtra("county_name");
        if(!TextUtils.isEmpty(countyName)){
            MyLog.e("WeatherAcitivity 选择的城市：",countyName);
//            //通过县名查询天气
            mTextDate.setText("同步中...");
            mTextCityName.setVisibility(View.VISIBLE);
            queryWeatherByCountyName(countyName);
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
        mTextCityName.setText(sdf.getString("city", ""));
        mTextWendu.setText(sdf.getString("wendu", ""));
        mTextMinWendu.setText(sdf.getString("today_low", ""));
        mTextMaxWendu.setText(sdf.getString("today_high", ""));
        mTextShidu.setText(sdf.getString("shidu", ""));
        mTextFengXiang.setText(sdf.getString("today_fx", ""));
        mTextFengji.setText(sdf.getString("today_fl", ""));
        mTextPm2.setText(sdf.getInt("pm25", 0) + "");
        mTextPm10.setText(sdf.getInt("pm10", 0) + "");
        mTextAqi.setText(sdf.getInt("today_aqi", 0) + "");
        mTextAirType.setText(sdf.getString("quality", ""));

        mTextNotice.setText(sdf.getString("today_notice", ""));

        mTextTypeYesterday.setText(sdf.getString("yesterday_type", ""));
        mTextMinWenduYesterday.setText(sdf.getString("yesterday_low", ""));
        mTextMaxWenduYesterday.setText(sdf.getString("yesterday_high", ""));
        mTextDateYesterday.setText(sdf.getString("yesterday_date", ""));

        mTextTypeForecastOneday.setText(sdf.getString("forecastOneday_type", ""));
        mTextMinWenduForcastOneday.setText(sdf.getString("forecastOneday_low", ""));
        mTextMaxWenduForcastOneday.setText(sdf.getString("forecastOneday_high", ""));
        mtextDateForcastOneday.setText(sdf.getString("forecastOneday_date", ""));

        mTextTypeForecastTwoday.setText(sdf.getString("forecastTwoday_type", ""));
        mTextMinWenduForcastTwoday.setText(sdf.getString("forecastTwoday_low", ""));
        mTextMaxWenduForcastTwoday.setText(sdf.getString("forecastTwoday_high", ""));
        mtextDateForcastTwoday.setText(sdf.getString("forecastTwoday_date", ""));

        mTextTypeForecastThreeday.setText(sdf.getString("forecastThreeday_type", ""));
        mTextMinWenduForcastThreeday.setText(sdf.getString("forecastThreeday_low", ""));
        mTextMaxWenduForcastThreeday.setText(sdf.getString("forecastThreeday_high", ""));
        mtextDateForcastThreeday.setText(sdf.getString("forecastThreeday_date", ""));

        weatherInfoLayout.setVisibility(View.VISIBLE);
        mTextDate.setText(sdf.getString("today_date", ""));
        //激活定时服务后台更新天气
//        Intent intent = new Intent(this, AutoUpdateService.class);
//        startService(intent);
    }

    /**
     * 通过县名查询天气信息
     * @param countyName
     */
    private void queryWeatherByCountyName(String countyName) {

        String address = Constant.API_URL+countyName;
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String respose) {
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

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextDate.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBtnSwitchCity = (Button) findViewById(R.id.btn_switch_city);
        mBtnSwitchCity.setOnClickListener(this);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.layout_weatherInfo);
//        forecastLayout = (LinearLayout) findViewById(R.id.layout_forecast);
//        switchLayout = (LinearLayout) findViewById(R.id.layout_switchFunctioin);

        mTextDate = (TextView) findViewById(R.id.text_date);
        mTextWendu = (TextView) findViewById(R.id.text_wendu);
        mTextMinWendu = (TextView) findViewById(R.id.text_wendu_min);
        mTextMaxWendu = (TextView) findViewById(R.id.text_wendu_max);
        mTextShidu = (TextView) findViewById(R.id.text_shidu);
        mTextFengXiang = (TextView) findViewById(R.id.text_fengxiang);
        mTextFengji = (TextView) findViewById(R.id.text_fengji);
        mTextPm2 = (TextView) findViewById(R.id.text_pm2_5);
        mTextPm10 = (TextView) findViewById(R.id.text_pm10);
        mTextAqi = (TextView) findViewById(R.id.text_aqi);
        mTextAirType = (TextView) findViewById(R.id.text_air_type);
        mTextNotice = (TextView) findViewById(R.id.text_notice);

        //昨天天气控件
        mTextTypeYesterday = (TextView) findViewById(R.id.text_type_yesterday);
        mTextMinWenduYesterday = (TextView) findViewById(R.id.text_wendu_min_yesterday);
        mTextMaxWenduYesterday = (TextView) findViewById(R.id.text_wendu_max_yesterday);
        mTextDateYesterday = (TextView) findViewById(R.id.text_date_yesterday);

        //未来第一天预报控件
        mTextTypeForecastOneday = (TextView) findViewById(R.id.text_type_forcast_oneday);
        mTextMinWenduForcastOneday = (TextView) findViewById(R.id.text_wendu_min_forcast_oneday);
        mTextMaxWenduForcastOneday = (TextView) findViewById(R.id.text_wendu_max_forcast_oneday);
        mtextDateForcastOneday = (TextView) findViewById(R.id.text_date_forcast_oneday);

        //未来第二天预报控件
        mTextTypeForecastTwoday = (TextView) findViewById(R.id.text_type_forcast_twoday);
        mTextMinWenduForcastTwoday = (TextView) findViewById(R.id.text_wendu_min_forcast_twoday);
        mTextMaxWenduForcastTwoday = (TextView) findViewById(R.id.text_wendu_max_forcast_twoday);
        mtextDateForcastTwoday = (TextView) findViewById(R.id.text_date_forcast_twoday);

        //未来第三天预报控件
        mTextTypeForecastThreeday = (TextView) findViewById(R.id.text_type_forcast_threeday);
        mTextMinWenduForcastThreeday = (TextView) findViewById(R.id.text_wendu_min_forcast_threeday);
        mTextMaxWenduForcastThreeday = (TextView) findViewById(R.id.text_wendu_max_forcast_threeday);
        mtextDateForcastThreeday = (TextView) findViewById(R.id.text_date_forcast_threeday);

        //图片控件
        mImageWeather = (ImageView) findViewById(R.id.image_weather);
        mImageNotice = (ImageView) findViewById(R.id.image_notice);
        mImageYesteday = (ImageView) findViewById(R.id.image_yesterday);
        mImageForecastOneday = (ImageView) findViewById(R.id.image_forecast_oneday);
        mImageForecastTwoday = (ImageView) findViewById(R.id.image_forecast_twoday);
        mImageForecastThreeday = (ImageView) findViewById(R.id.image_forecast_threeday);

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
//            case R.id.btn_refresh_weather:
//                mTextPublishTime.setText("正在同步");
//                //重新获得县的ID，重新从服务器解析天气信息并刷新SharedPreferences文件中
//                SharedPreferences prefs = getSharedPreferences("weatherInfo", MODE_PRIVATE);
//                String weatherCode = prefs.getString("weather_code", "");
//                if(!TextUtils.isEmpty(weatherCode)){
//                    queryWeatherInfo(weatherCode);
//                }
//                break;
            default:
                break;
        }
    }
}
