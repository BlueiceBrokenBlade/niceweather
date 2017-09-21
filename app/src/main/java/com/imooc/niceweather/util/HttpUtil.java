package com.imooc.niceweather.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
                        MyLog.e("从服务器请求回来的数据：",respose.toString());
                        //测试专用，避免重复调用接口
                        saveResposeToLocal(respose.toString());
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

    /**
     * 将服务器返回的数据储存到本地，以为免费接口限制重复调用
     */
    private static void saveResposeToLocal(String respose) {
        //判断外存状态
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return;
        }

        FileOutputStream fos = null;
        //获取外部设备
        File file = new File(Environment.getExternalStorageDirectory()+"/downloads/", "weather.txt");
        try {
            fos = new FileOutputStream(file);
            //写入文件
            fos.write(respose.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
