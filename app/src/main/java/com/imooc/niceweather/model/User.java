package com.imooc.niceweather.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by xhx12366 on 2017-10-17.
 */

public class User extends BmobUser {
    private Boolean sex;

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
