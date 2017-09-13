package com.imooc.niceweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imooc.niceweather.model.City;
import com.imooc.niceweather.model.County;
import com.imooc.niceweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 * Created by xhx12366 on 2017-09-11.
 */

public class NiceWeatherDBmanager {

    public static final String DB_NAME = "nice_weather";

    public static final int VERSION = 1;

    private static NiceWeatherDBmanager dBmanager;

    private SQLiteDatabase db;

    private NiceWeatherDBmanager(Context context){
        NiceWeatherOpenHelper dbHelper= new NiceWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 单例模式获取NiceWeatherDBmanager实例
     * @return
     */
    public synchronized static NiceWeatherDBmanager getInstance(Context context){
        if(dBmanager == null){
            dBmanager = new NiceWeatherDBmanager(context);
        }
        return dBmanager;
    }

    /**
     * 将Province实例存储到数据库
     * @param province
     */
    public void saveProvince(Province province){
        if(province != null){
            db.execSQL("insert into Province (province_name,province_code) values (?,?)",
                    new Object[]{province.getProvinceName(),province.getProvinceCode()});
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return list
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.rawQuery("select * from Province",null);
        while(cursor.moveToNext()){
            Province province = new Province();
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            list.add(province);
        }
        return list;
    }

    /**
     * 将City实例存储到数据库
     * @param city
     */
    public void saveCity(City city){
        if(city != null){
            db.execSQL("insert into City (city_name,city_code,province_id) values " +
                    "(?,?,?)", new Object[]{city.getCityName(), city.getCityCode(),
            city.getProvinceId()});
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     * @return list
     */
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.rawQuery("select * from City where province_id = ?",
                new String[]{String.valueOf(provinceId)});
        while(cursor.moveToNext()){
            City city = new City();
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
            list.add(city);
        }
        return list;
    }

    /**
     * 将County实例存储到数据库
     * @param county
     */
    public void saveCounty(County county){
        if(county != null){
            db.execSQL("insert into County (county_name,county_code,city_id) values (?,?,?)",
                    new Object[]{county.getCountyName(), county.getCountyCode(),
                    county.getCityId()});
        }
    }

    /**
     * 从数据库读取某个城市所有县城信息
     * @return list
     */
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.rawQuery("select * from County where city_id = ?",new String[]{
                String.valueOf(cityId)});
        while(cursor.moveToNext()){
            County county = new County();
            county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            county.setCityId(cityId);
            list.add(county);
        }
        return list;
    }


}
