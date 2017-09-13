package com.imooc.niceweather.util;

/**
 * Created by xhx12366 on 2017-09-12.
 */

public interface HttpCallbackListener {

    void onFinish(String respose);

    void onError(Exception e);
}
