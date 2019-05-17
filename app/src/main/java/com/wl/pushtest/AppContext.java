package com.wl.pushtest;

import android.app.Application;

import com.wl.pushtest.push.WlPushReceiver;
import com.wl.pushutils.push.PushControlUtils;


/**
 * @Project: ADPush
 * @Package: com.wl.pushutils
 * @Author: HSL
 * @Time: 2019/05/09 16:53
 * @E-mail: xxx@163.com
 * @Description: 这个人太懒，没留下什么踪迹~
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化推送
        PushControlUtils
                .getInstance()
                .initPushInApplication(this, new WlPushReceiver());
    }
}
