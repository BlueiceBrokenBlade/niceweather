package com.imooc.niceweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.imooc.niceweather.db.NiceWeatherDBmanager;
import com.imooc.niceweather.model.City;
import com.imooc.niceweather.model.County;
import com.imooc.niceweather.model.Province;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static android.content.Context.MODE_PRIVATE;

/**
 * 解析处理服务器返回的省市县数据
 * 省市县数据格式 “代号|城市,代号|城市”
 * Created by xhx12366 on 2017-09-12.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param niceWeatherDBmanager
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(
            NiceWeatherDBmanager niceWeatherDBmanager,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //解析出来的数据存储到Province表
                    niceWeatherDBmanager.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的市级数据
     * @param niceWeatherDBmanager
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCitiesResponse(NiceWeatherDBmanager niceWeatherDBmanager,
                                               String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length >0){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出来的市级数据存储到City表中
                   niceWeatherDBmanager.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析处理服务器返回的县级数据
     * @param niceWeatherDBmanager
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountiesResponse(NiceWeatherDBmanager niceWeatherDBmanager,
                                                 String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0){
                for(String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    //将解析出来的县级数据存储到County表
                    niceWeatherDBmanager.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON格式天气数据，并将其通过SharedPreferences存储在本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context, String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt("status") == 200){
                String city = jsonObject.getString("city");
                JSONObject data = jsonObject.getJSONObject("data");
                String shidu = data.getString("shidu");
                int pm25 = data.getInt("pm25");
                int pm10 = data.getInt("pm10");
                String quality = data.getString("quality");
                String wendu = data.getString("wendu");
                String ganmao = data.getString("ganmao");

                JSONObject yesterday = data.getJSONObject("yesterday");
                String yesterday_date = yesterday.getString("date");
                String yesterday_sunrise = yesterday.getString("sunrise");
                String yesterday_sunset = yesterday.getString("sunset");
                String yesterday_high = yesterday.getString("high");
                String yesterday_low = yesterday.getString("low");
                int yesterday_aqi = yesterday.getInt("aqi");
                String yesterday_fx = yesterday.getString("fx");
                String yesterday_fl = yesterday.getString("fl");
                String yesterday_type = yesterday.getString("type");
                String yesterday_notice = yesterday.getString("notice");

                JSONArray forecastArray = data.getJSONArray("forecast");
                JSONObject today = forecastArray.getJSONObject(0);
                String today_date = today.getString("date");
                String today_sunrise = today.getString("sunrise");
                String today_sunset = today.getString("sunset");
                String today_high = today.getString("high");
                String today_low = today.getString("low");
                int today_aqi = today.getInt("aqi");
                String today_fx = today.getString("fx");
                String today_fl = today.getString("fl");
                String today_type = today.getString("type");
                String today_notice = today.getString("notice");

                JSONObject forecastOneday = forecastArray.getJSONObject(1);
                String forecastOneday_date = forecastOneday.getString("date");
                String forecastOneday_sunrise = forecastOneday.getString("sunrise");
                String forecastOneday_sunset = forecastOneday.getString("sunset");
                String forecastOneday_high = forecastOneday.getString("high");
                String forecastOneday_low = forecastOneday.getString("low");
                int forecastOneday_aqi = forecastOneday.getInt("aqi");
                String forecastOneday_fx = forecastOneday.getString("fx");
                String forecastOneday_fl = forecastOneday.getString("fl");
                String forecastOneday_type = forecastOneday.getString("type");
                String forecastOneday_notice = forecastOneday.getString("notice");


                JSONObject forecastTwoday = forecastArray.getJSONObject(2);
                String forecastTwoday_date = forecastTwoday.getString("date");
                String forecastTwoday_sunrise = forecastTwoday.getString("sunrise");
                String forecastTwoday_sunset = forecastTwoday.getString("sunset");
                String forecastTwoday_high = forecastTwoday.getString("high");
                String forecastTwoday_low = forecastTwoday.getString("low");
                int forecastTwoday_aqi = forecastTwoday.getInt("aqi");
                String forecastTwoday_fx = forecastTwoday.getString("fx");
                String forecastTwoday_fl = forecastTwoday.getString("fl");
                String forecastTwoday_type = forecastTwoday.getString("type");
                String forecastTwoday_notice = forecastTwoday.getString("notice");

                JSONObject forecastThreeday = forecastArray.getJSONObject(3);
                String forecastThreeday_date = forecastThreeday.getString("date");
                String forecastThreeday_sunrise = forecastThreeday.getString("sunrise");
                String forecastThreeday_sunset = forecastThreeday.getString("sunset");
                String forecastThreeday_high = forecastThreeday.getString("high");
                String forecastThreeday_low = forecastThreeday.getString("low");
                int forecastThreeday_aqi = forecastThreeday.getInt("aqi");
                String forecastThreeday_fx = forecastThreeday.getString("fx");
                String forecastThreeday_fl = forecastThreeday.getString("fl");
                String forecastThreeday_type = forecastThreeday.getString("type");
                String forecastThreeday_notice = forecastThreeday.getString("notice");

                saveWeatherInfo(context, city, shidu, pm25, pm10, quality, wendu, ganmao,
                        yesterday_date, yesterday_sunrise, yesterday_sunset, yesterday_high, yesterday_low,
                        yesterday_aqi, yesterday_fx, yesterday_fl, yesterday_type, yesterday_notice,
                        today_date, today_sunrise, today_sunset, today_high, today_low, today_aqi,
                        today_fx, today_fl, today_type, today_notice,
                        forecastOneday_date, forecastOneday_sunrise, forecastOneday_sunset, forecastOneday_high, forecastOneday_low,
                        forecastOneday_aqi, forecastOneday_fx, forecastOneday_fl, forecastOneday_type, forecastOneday_notice,
                        forecastTwoday_date, forecastTwoday_sunrise, forecastTwoday_sunset, forecastTwoday_high, forecastTwoday_low,
                        forecastTwoday_aqi, forecastTwoday_fx, forecastTwoday_fl, forecastTwoday_type, forecastTwoday_notice,
                        forecastThreeday_date, forecastThreeday_sunrise, forecastThreeday_sunset, forecastThreeday_high, forecastThreeday_low,
                        forecastThreeday_aqi, forecastThreeday_fx, forecastThreeday_fl, forecastThreeday_type, forecastThreeday_notice);
            } else {
                MyLog.e("Utility:","查询失败！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的天气数据存储在SharedPreferences文件中
     * @param context
     * @param city
     * @param shidu
     * @param pm25
     * @param pm10
     * @param quality
     * @param wendu
     * @param ganmao
     * @param yesterday_date
     * @param yesterday_sunrise
     * @param yesterday_sunset
     * @param yesterday_high
     * @param yesterday_low
     * @param yesterday_aqi
     * @param yesterday_fx
     * @param yesterday_fl
     * @param yesterday_type
     * @param yesterday_notice
     * @param today_date
     * @param today_sunrise
     * @param today_sunset
     * @param today_high
     * @param today_low
     * @param today_aqi
     * @param today_fx
     * @param today_fl
     * @param today_type
     * @param today_notice
     * @param forecastOneday_date
     * @param forecastOneday_sunrise
     * @param forecastOneday_sunset
     * @param forecastOneday_high
     * @param forecastOneday_low
     * @param forecastOneday_aqi
     * @param forecastOneday_fx
     * @param forecastOneday_fl
     * @param forecastOneday_type
     * @param forecastOneday_notice
     * @param forecastTwoday_date
     * @param forecastTwoday_sunrise
     * @param forecastTwoday_sunset
     * @param forecastTwoday_high
     * @param forecastTwoday_low
     * @param forecastTwoday_aqi
     * @param forecastTwoday_fx
     * @param forecastTwoday_fl
     * @param forecastTwoday_type
     * @param forecastTwoday_notice
     * @param forecastThreeday_date
     * @param forecastThreeday_sunrise
     * @param forecastThreeday_sunset
     * @param forecastThreeday_high
     * @param forecastThreeday_low
     * @param forecastThreeday_aqi
     * @param forecastThreeday_fx
     * @param forecastThreeday_fl
     * @param forecastThreeday_type
     * @param forecastThreeday_notice
     */
    private static void saveWeatherInfo(Context context, String city, String shidu, int pm25, int pm10, String quality,
                                        String wendu, String ganmao, String yesterday_date, String yesterday_sunrise,
                                        String yesterday_sunset, String yesterday_high, String yesterday_low,
                                        int yesterday_aqi, String yesterday_fx, String yesterday_fl, String yesterday_type,
                                        String yesterday_notice, String today_date, String today_sunrise, String today_sunset,
                                        String today_high, String today_low, int today_aqi, String today_fx, String today_fl,
                                        String today_type, String today_notice, String forecastOneday_date, String forecastOneday_sunrise,
                                        String forecastOneday_sunset, String forecastOneday_high, String forecastOneday_low,
                                        int forecastOneday_aqi, String forecastOneday_fx, String forecastOneday_fl,
                                        String forecastOneday_type, String forecastOneday_notice, String forecastTwoday_date,
                                        String forecastTwoday_sunrise, String forecastTwoday_sunset, String forecastTwoday_high,
                                        String forecastTwoday_low, int forecastTwoday_aqi, String forecastTwoday_fx,
                                        String forecastTwoday_fl, String forecastTwoday_type, String forecastTwoday_notice,
                                        String forecastThreeday_date, String forecastThreeday_sunrise, String forecastThreeday_sunset,
                                        String forecastThreeday_high, String forecastThreeday_low, int forecastThreeday_aqi,
                                        String forecastThreeday_fx, String forecastThreeday_fl, String forecastThreeday_type, String forecastThreeday_notice) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        SharedPreferences.Editor editor = context.getSharedPreferences("weatherInfo", MODE_PRIVATE).edit();
        editor.putString("city", city);
        editor.putString("shidu", shidu);
        editor.putInt("pm25", pm25);
        editor.putInt("pm10", pm10);
        editor.putString("quality", quality);
        editor.putString("wendu", wendu);
        editor.putString("ganmao", ganmao);

        editor.putString("yesterday_date", yesterday_date);
//        editor.putString("yesterday_sunrise", yesterday_sunrise);
//        editor.putString("yesterday_sunset", yesterday_sunset);
        editor.putString("yesterday_high", yesterday_high.split("\\s")[1]);
        editor.putString("yesterday_low", yesterday_low.split("\\s")[1]);
//        editor.putInt("yesterday_aqi", yesterday_aqi);
//        editor.putString("yesterday_fx", yesterday_fx);
//        editor.putString("yesterday_fl", yesterday_fl);
        editor.putString("yesterday_type", yesterday_type);
//        editor.putString("yesterday_notice", yesterday_notice);

        editor.putString("today_date", sdf.format(new Date()));
//        editor.putString("today_sunrise", today_sunrise);
//        editor.putString("today_sunset", today_sunset);
        editor.putString("today_high", today_high.split("\\s")[1]);
        editor.putString("today_low", today_low.split("\\s")[1]);
        editor.putInt("today_aqi", today_aqi);
        editor.putString("today_fx", today_fx);
        editor.putString("today_fl", today_fl);
        editor.putString("today_type", today_type);
        editor.putString("today_notice", today_notice);

        editor.putString("forecastOneday_date", forecastOneday_date);
//        editor.putString("forecastOneday_sunrise", forecastOneday_sunrise);
//        editor.putString("forecastOneday_sunset", forecastOneday_sunset);
        editor.putString("forecastOneday_high", forecastOneday_high.split("\\s")[1]);
        editor.putString("forecastOneday_low", forecastOneday_low.split("\\s")[1]);
        editor.putInt("forecastOneday_aqi", forecastOneday_aqi);
//        editor.putString("forecastOneday_fx", forecastOneday_fx);
//        editor.putString("forecastOneday_fl", forecastOneday_fl);
        editor.putString("forecastOneday_type", forecastOneday_type);
//        editor.putString("forecastOneday_notice", forecastOneday_notice);

        editor.putString("forecastTwoday_date", forecastTwoday_date);
//        editor.putString("forecastTwoday_sunrise", forecastTwoday_sunrise);
//        editor.putString("forecastTwoday_sunset", forecastTwoday_sunset);
        editor.putString("forecastTwoday_high", forecastTwoday_high.split("\\s")[1]);
        editor.putString("forecastTwoday_low", forecastTwoday_low.split("\\s")[1]);
//        editor.putInt("forecastTwoday_aqi", forecastTwoday_aqi);
//        editor.putString("forecastTwoday_fx", forecastTwoday_fx);
//        editor.putString("forecastTwoday_fl", forecastTwoday_fl);
        editor.putString("forecastTwoday_type", forecastTwoday_type);
//        editor.putString("forecastTwoday_notice", forecastTwoday_notice);

        editor.putString("forecastThreeday_date", forecastThreeday_date);
//        editor.putString("forecastThreeday_sunrise", forecastThreeday_sunrise);
//        editor.putString("forecastThreeday_sunset", forecastThreeday_sunset);
        editor.putString("forecastThreeday_high", forecastThreeday_high.split("\\s")[1]);
        editor.putString("forecastThreeday_low", forecastThreeday_low.split("\\s")[1]);
//        editor.putInt("forecastThreeday_aqi", forecastThreeday_aqi);
//        editor.putString("forecastThreeday_fx", forecastThreeday_fx);
//        editor.putString("forecastThreeday_fl", forecastThreeday_fl);
        editor.putString("forecastThreeday_type", forecastThreeday_type);
//        editor.putString("forecastThreeday_notice", forecastThreeday_notice);

        editor.commit();
    }
}
