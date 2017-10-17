package com.imooc.niceweather.activity.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.imooc.niceweather.R;
import com.imooc.niceweather.model.Constant;
import com.imooc.niceweather.util.HttpCallbackListener;
import com.imooc.niceweather.util.HttpUtil;
import com.imooc.niceweather.util.Utility;

import static android.content.Context.MODE_PRIVATE;

/**
 * 天气界面
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements View.OnClickListener{
    private LinearLayout weatherInfoLayout;
    private LinearLayout forecastLayout;
    private LinearLayout switchLayout;

    private Button mBtnSwitchCity;

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
    private ImageView mImageYesteday; //昨日天气图片
    private ImageView mImageForecastOneday; //未来第一天天气图片
    private ImageView mImageForecastTwoday; //未来第二天天气图片
    private ImageView mImageForecastThreeday; //未来第三天天气图片
    private String countyName;
    private boolean isFromChooseActivity;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 当页面滑动到第三fragment，第一fragment调用onPause(),onStop(),onDestroyView
         * 当第一fragment重新加载时，调用onCreateView(),onStart(),onResume()
         * 因此只需初始化一次的，放在onCreate()，此处传递参数作用防止切换页面重复查询服务器
         */
        countyName = getArguments().getString("couty_name");
        isFromChooseActivity = getArguments().getBoolean("from_choose_activity");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        weatherInfoLayout = (LinearLayout) getView().findViewById(R.id.layout_weatherInfo);
        forecastLayout = (LinearLayout) getView().findViewById(R.id.layout_weather_forecast);

        //今日天气
        mTextWendu = (TextView) getView().findViewById(R.id.text_wendu);
        mTextMinWendu = (TextView) getView().findViewById(R.id.text_wendu_min);
        mTextMaxWendu = (TextView) getView().findViewById(R.id.text_wendu_max);
        mTextShidu = (TextView) getView().findViewById(R.id.text_shidu);
        mTextFengXiang = (TextView) getView().findViewById(R.id.text_fengxiang);
        mTextFengji = (TextView) getView().findViewById(R.id.text_fengji);
        mTextPm2 = (TextView) getView().findViewById(R.id.text_pm2_5);
        mTextPm10 = (TextView) getView().findViewById(R.id.text_pm10);
        mTextAqi = (TextView) getView().findViewById(R.id.text_aqi);
        mTextAirType = (TextView) getView().findViewById(R.id.text_air_type);
        mTextNotice = (TextView) getView().findViewById(R.id.text_notice);

        //昨天天气控件
        mTextTypeYesterday = (TextView) getView().findViewById(R.id.text_type_yesterday);
        mTextMinWenduYesterday = (TextView) getView().findViewById(R.id.text_wendu_min_yesterday);
        mTextMaxWenduYesterday = (TextView) getView().findViewById(R.id.text_wendu_max_yesterday);
        mTextDateYesterday = (TextView) getView().findViewById(R.id.text_date_yesterday);

        //未来第一天预报控件
        mTextTypeForecastOneday = (TextView) getView().findViewById(R.id.text_type_forcast_oneday);
        mTextMinWenduForcastOneday = (TextView) getView().findViewById(R.id.text_wendu_min_forcast_oneday);
        mTextMaxWenduForcastOneday = (TextView) getView().findViewById(R.id.text_wendu_max_forcast_oneday);
        mtextDateForcastOneday = (TextView) getView().findViewById(R.id.text_date_forcast_oneday);

        //未来第二天预报控件
        mTextTypeForecastTwoday = (TextView) getView().findViewById(R.id.text_type_forcast_twoday);
        mTextMinWenduForcastTwoday = (TextView) getView().findViewById(R.id.text_wendu_min_forcast_twoday);
        mTextMaxWenduForcastTwoday = (TextView) getView().findViewById(R.id.text_wendu_max_forcast_twoday);
        mtextDateForcastTwoday = (TextView) getView().findViewById(R.id.text_date_forcast_twoday);

        //未来第三天预报控件
        mTextTypeForecastThreeday = (TextView) getView().findViewById(R.id.text_type_forcast_threeday);
        mTextMinWenduForcastThreeday = (TextView) getView().findViewById(R.id.text_wendu_min_forcast_threeday);
        mTextMaxWenduForcastThreeday = (TextView) getView().findViewById(R.id.text_wendu_max_forcast_threeday);
        mtextDateForcastThreeday = (TextView) getView().findViewById(R.id.text_date_forcast_threeday);

        //图片控件
        mImageWeather = (ImageView) getView().findViewById(R.id.image_weather);
        mImageYesteday = (ImageView) getView().findViewById(R.id.image_yesterday);
        mImageForecastOneday = (ImageView) getView().findViewById(R.id.image_forecast_oneday);
        mImageForecastTwoday = (ImageView) getView().findViewById(R.id.image_forecast_twoday);
        mImageForecastThreeday = (ImageView) getView().findViewById(R.id.image_forecast_threeday);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        if(isFromChooseActivity){
            queryWeatherByCountyName();
            isFromChooseActivity = false;
        }else{
            showWeather();
        }

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 通过城市名查询相应天气
     */
    private void queryWeatherByCountyName() {

        if(!TextUtils.isEmpty(countyName)){
            String address = Constant.API_URL+countyName;
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String respose) {
                    Utility.handleWeatherResponse(getContext(), respose);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //在界面上显示天气
                            showWeather();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }else{

        }
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather() {
        SharedPreferences sdf = getContext().getSharedPreferences("weatherInfo", MODE_PRIVATE);
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

        mImageWeather.setImageResource(GetImageResourceByStyle(sdf.getString("today_type", "")));
        mImageYesteday.setImageResource(GetImageResourceByStyle(sdf.getString("yesterday_type", "")));
        mImageForecastOneday.setImageResource(GetImageResourceByStyle(sdf.getString("forecastOneday_type", "")));
        mImageForecastTwoday.setImageResource(GetImageResourceByStyle(sdf.getString("forecastTwoday_type", "")));
        mImageForecastThreeday.setImageResource(GetImageResourceByStyle(sdf.getString("forecastThreeday_type", "")));

//        weatherInfoLayout.setVisibility(View.VISIBLE);
        //激活定时服务后台更新天气
//        Intent intent = new Intent(this, AutoUpdateService.class);
//        startService(intent);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 通过天气类型获得图片资源id
     * @param style 天气类型
     * @return 图片资源id
     */
    public int GetImageResourceByStyle(String style){
        switch (style){
            case "晴":
                return R.drawable.airtype_qing;
            case "多云":
                return R.drawable.airtype_duoyun;
            case "阴":
                return R.drawable.airtype_yin;
            case "阵雨":
                return R.drawable.airtype_zhenyu;
            case "小雨":
                return R.drawable.airtype_xiaoyu;
            default:
                return 0;
        }
    }

//    /**
//     * 强制WeatherFragment实现OnQueryWeatherByCountyNameListener回调接口
//     * @param context
//     */
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mListener = (OnQueryWeatherByCountyNameListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + "must implement " +
//                    "OnQueryWeatherByCountyNameListener");
//        }
//
//    }

//    /**
//     * 城市选择显示天气回调接口
//     */
//    public interface OnQueryWeatherByCountyNameListener{
//        public void onQueryWeatherByCountyName(String coutyName);
//    }
}
