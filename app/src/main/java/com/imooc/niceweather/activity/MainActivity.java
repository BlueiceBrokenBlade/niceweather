package com.imooc.niceweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooc.niceweather.R;
import com.imooc.niceweather.activity.fragment.NewsFragment;
import com.imooc.niceweather.activity.fragment.SetFragment;
import com.imooc.niceweather.activity.fragment.WeatherFragment;
import com.imooc.niceweather.adpter.MyFragmentPagerAdapter;
import com.imooc.niceweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private RelativeLayout mLayoutTitle;
    private ViewPager viewPager;
    private List<Fragment> mfragmentList;
    private MyFragmentPagerAdapter mfragmentPagerAdapter;
    private String countyName;
    private boolean isFromChooseActivity;
    private TextView mTextCountyName;
    private TextView mTextDate;
    private Button mBtnSwitchCity;
    private TextView mTextSwitchWeather,mTextSwitchZixu,mTextSwitchSet;
    private LinearLayout mLayoutSwitchOne,mLayoutSwitchTwo,mLayoutSwitchThree;
    public static final int SWITCH_WEATHER = 0;
    public static final int SWITCH_ZIXUN = 1;
    public static final int SWITCH_SET = 2;

    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        mfragmentList = new ArrayList<Fragment>();
        mfragmentList.add(weatherFragment);
        mfragmentList.add(new NewsFragment());
        mfragmentList.add(new SetFragment());
        mfragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mfragmentList);
        viewPager.setAdapter(mfragmentPagerAdapter);
        //滑动页面监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTextSwitchWeather.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                mTextSwitchZixu.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                mTextSwitchSet.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

                switch (position){
                    case SWITCH_WEATHER:
                        mTextSwitchWeather.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                        mLayoutTitle.setVisibility(View.VISIBLE);
                        break;
                    case SWITCH_ZIXUN:
                        mTextSwitchZixu.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                        mLayoutTitle.setVisibility(View.GONE) ;
                        break;
                    case SWITCH_SET:
                        mTextSwitchSet.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                        mLayoutTitle.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SharedPreferences sdf = getSharedPreferences("weatherInfo", MODE_PRIVATE);
        isFromChooseActivity = getIntent().getBooleanExtra("from_choose_activity", false);
        if(isFromChooseActivity){
            countyName = getIntent().getStringExtra("county_name");
        }else{
            countyName = sdf.getString("city", "");
        }
        mTextCountyName.setText(countyName);
        mTextDate.setText(sdf.getString("today_date", ""));

        sendCountyNameToWeather(countyName);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mLayoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mTextCountyName = (TextView) findViewById(R.id.text_CountyName);
        mTextDate = (TextView) findViewById(R.id.text_date);
        mBtnSwitchCity = (Button) findViewById(R.id.btn_switch_city);
        mBtnSwitchCity.setOnClickListener(this);
        mTextSwitchWeather = (TextView) findViewById(R.id.text_switch_weather);
        mTextSwitchWeather.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        mTextSwitchZixu = (TextView) findViewById(R.id.text_switch_zixun);
        mTextSwitchSet = (TextView) findViewById(R.id.text_switch_set);

        mLayoutSwitchOne = (LinearLayout) findViewById(R.id.layout_switch_one);
        mLayoutSwitchTwo = (LinearLayout) findViewById(R.id.layout_switch_two);
        mLayoutSwitchThree = (LinearLayout) findViewById(R.id.layout_switch_three);
        mLayoutSwitchOne.setOnClickListener(this);
        mLayoutSwitchTwo.setOnClickListener(this);
        mLayoutSwitchThree.setOnClickListener(this);



        weatherFragment = new WeatherFragment();
    }

    /**
     * activity向weatherfragment传递一些属性
     * @param countyName
     */
    public void sendCountyNameToWeather(String countyName){
        Bundle bundle = new Bundle();
        bundle.putString("couty_name", countyName);
        bundle.putBoolean("from_choose_activity", isFromChooseActivity);
        weatherFragment.setArguments(bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_switch_city:
                Intent intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_switch_one:
                viewPager.setCurrentItem(SWITCH_WEATHER);
                break;
            case R.id.layout_switch_two:
                viewPager.setCurrentItem(SWITCH_ZIXUN);
                break;
            case R.id.layout_switch_three:
                viewPager.setCurrentItem(SWITCH_SET);
                break;

        }
    }
}
