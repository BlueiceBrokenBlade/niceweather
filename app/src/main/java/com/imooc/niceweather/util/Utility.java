package com.imooc.niceweather.util;

import android.text.TextUtils;

import com.imooc.niceweather.db.NiceWeatherDBmanager;
import com.imooc.niceweather.model.City;
import com.imooc.niceweather.model.County;
import com.imooc.niceweather.model.Province;

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
}
