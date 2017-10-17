package com.imooc.niceweather.activity.fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.imooc.niceweather.R;
import com.imooc.niceweather.activity.MainActivity;
import com.imooc.niceweather.activity.SetNotification;
import com.imooc.niceweather.activity.UserInfo;
import com.imooc.niceweather.activity.UserLoginActivity;
import com.imooc.niceweather.adpter.MySetItemAdapter;
import com.imooc.niceweather.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {
    private ListView mListViewAccount;
    private ListView mListViewSet;
    private ListView mListViewOther;

    private List<Map<String, Object>> accountData;
    private List<Map<String, Object>> setData;
    private List<Map<String, Object>> otherData;

    private Integer[] accountItemIcons = {
            R.drawable.set_notice
    };
    private Integer[] setItemIcons = {
            R.drawable.set_notice, R.drawable.set_notice, R.drawable.set_notice
    };
    private Integer[] otherItemIcons = {
            R.drawable.set_notice, R.drawable.set_notice
    };

    private String[] accountItemNames = {
            "账号管理"
    };
    private String[] setItemNames = {
            "通知栏", "天气信息推送", "天气背景"
    };
    private String[] otherItemNames = {
            "产品介绍", "使用教程"
    };

    private static final int ACCOUNT_MANAGE = 0;
    private static final int SET_NOTIFICATION = 0;
    private static final int SET_PUSH = 1;
    private static final int SET_BACKGROUND = 2;
    private static final int OTHER_INTRODUCE = 0;
    private static final int OTHER_COURSE = 1;

    private SharedPreferences sp;

    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();

        MySetItemAdapter accountAdapter = new MySetItemAdapter(getContext(), accountData);
        MySetItemAdapter setAdapter = new MySetItemAdapter(getContext(), setData);
        MySetItemAdapter otherAdapter = new MySetItemAdapter(getContext(), otherData);
        mListViewAccount.setAdapter(accountAdapter);
        mListViewSet.setAdapter(setAdapter);
        mListViewOther.setAdapter(otherAdapter);

        mListViewAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    //跳转到账号管理，未登录跳转到登录界面，已经登录跳转到用户信息界面
                    case ACCOUNT_MANAGE:
                        if(sp.getBoolean("isLogin", false)){
                            intent.setClass(getContext(), UserInfo.class);
                        }else{
                            intent.setClass(getContext(), UserLoginActivity.class);
                        }
                        startActivity(intent);
                        break;
                }
            }
        });

        mListViewSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case SET_NOTIFICATION:
                        intent.setClass(getContext(), SetNotification.class);
                        startActivity(intent);
                        break;
                    case SET_PUSH:

                        break;
                    case SET_BACKGROUND:

                        break;
                }
            }
        });

        mListViewOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case OTHER_INTRODUCE:

                        break;
                    case OTHER_COURSE:

                        break;
                }
            }
        });
    }

    /**
     * 添加初始的账号列表，设置列表和其它列表数据
     */
    private void initData() {
        accountData = new ArrayList<Map<String, Object>>();
        setData = new ArrayList<Map<String, Object>>();
        otherData = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < accountItemIcons.length; i++){
            Map map = new HashMap<String, Object>();
            map.put("itemIcon", accountItemIcons[i]);
            map.put("itemName", accountItemNames[i]);
            map.put("itemChoosed", null);
            accountData.add(map);
        }

        for(int i = 0; i < setItemIcons.length; i++){
            Map map = new HashMap<String, Object>();
            map.put("itemIcon", setItemIcons[i]);
            map.put("itemName", setItemNames[i]);
            map.put("itemChoosed", null);
            setData.add(map);
        }

        for(int i = 0; i < otherItemIcons.length; i++){
            Map map = new HashMap<String, Object>();
            map.put("itemIcon", otherItemIcons[i]);
            map.put("itemName", otherItemNames[i]);
            map.put("itemChoosed", null);
            otherData.add(map);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mListViewAccount = (ListView) getView().findViewById(R.id.listView_account);
        mListViewSet = (ListView) getView().findViewById(R.id.listView_set);
        mListViewOther = (ListView) getView().findViewById(R.id.listView_other);

        sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.e("SetFragment", sp.getString("UserName", "?"));
//        MyLog.e("SetFragment", sp.getBoolean("isLogin", false)+"");
    }
}
