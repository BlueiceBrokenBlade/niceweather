package com.imooc.niceweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by xhx12366 on 2017-09-12.
 */

public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(address);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer respose = new StringBuffer();
                    String line;
                    while((line = reader.readLine()) != null){
                        respose.append(line);
                    }
                    if(listener != null){
                        //回调onFinish（）方法
                        listener.onFinish(respose.toString());
                    }
                } catch (Exception e) {
                    if(listener != null){
                        //回调onError方法
                        listener.onError(e);
                    }
                } finally {
                    if(conn != null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

}
