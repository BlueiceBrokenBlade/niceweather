package com.imooc.niceweather.model;

import com.imooc.niceweather.R;

/**
 * 常量类
 * Created by xhx12366 on 2017-09-20.
 */

public class Constant {
    public static final String API_URL = "http://www.sojson.com/open/api/weather/json.shtml?city=";

    public static final int NOTIFICATION_TEXT_DEFAULT = 0;
    public static final int NOTIFICATION_TEXT_RED = 1;
    public static final int NOTIFICATION_TEXT_GREEN = 2;
    public static final int NOTIFICATION_TEXT_YELLOW = 3;
    public static final int[] NOTIFICATION_TEXT_COLOR = new int[]{R.color.notification_default,
            R.color.notification_red, R.color.notification_green, R.color.notification_yellow
    };

    public static final int NOTIFICATION_BACKGROUND_RED = 1;
    public static final int NOTIFICATION_BACKGROUND_GREEN = 2;
    public static final int NOTIFICATION_BACKGROUND_YELLOW = 3;
    public static final int[] NOTIFICATION_BACKGROUND_COLOR = new int[]{
            R.color.notification_red, R.color.notification_green, R.color.notification_yellow
    };
}
