package com.imooc.niceweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.niceweather.R;
import com.imooc.niceweather.db.NiceWeatherDBmanager;
import com.imooc.niceweather.model.City;
import com.imooc.niceweather.model.County;
import com.imooc.niceweather.model.Province;
import com.imooc.niceweather.util.HttpCallbackListener;
import com.imooc.niceweather.util.HttpUtil;
import com.imooc.niceweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private TextView mTextTitle;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private NiceWeatherDBmanager dBmanager;
    private List<String> dataList = new ArrayList<String>();

    ProgressDialog progressDialog;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);

        mListView = (ListView) findViewById(R.id.list_view);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                dataList);
        mListView.setAdapter(adapter);
        dBmanager = NiceWeatherDBmanager.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();//加载省级数据
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        provinceList = dBmanager.loadProvince();
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
          queryFromServer(null, "province");
        }
    }

    /**
     * 查询选中省下所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        cityList = dBmanager.loadCity(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextTitle.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else{
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询选中市下所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        countyList = dBmanager.loadCounty(selectedCity.getId());
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTextTitle.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else{
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 根据代号和类型从服务器查询数据
     * @param code
     * @param type
     */
    private void queryFromServer(final String code, final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String respose) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(dBmanager, respose);
                } else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(dBmanager, respose, selectedProvince.getId());
                } else if ("county".equals(type)){
                    result =Utility.handleCountiesResponse(dBmanager, respose, selectedCity.getId());
                }
                if(result){
                    //通过runOnUiThread（）方法在主线程更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            } else if ("city".equals(type)){
                                queryCities();
                            } else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    /**
     * 捕获Back按键，根据当前级别判断，返回市、省列表还是直接退出
     */
    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY){
            queryCities();
        } else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        } else {
            finish();
        }
    }
}
