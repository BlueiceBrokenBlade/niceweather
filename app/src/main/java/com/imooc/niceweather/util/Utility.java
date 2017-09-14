package com.imooc.niceweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.imooc.niceweather.db.NiceWeatherDBmanager;
import com.imooc.niceweather.model.City;
import com.imooc.niceweather.model.County;
import com.imooc.niceweather.model.Province;
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
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp,
                    publishTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的天气数据存储在SharedPreferences文件中
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                        String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = context.getSharedPreferences("weatherInfo", MODE_PRIVATE).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_Code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_time", sdf.format(new Date()));
        editor.commit();
    }


}
