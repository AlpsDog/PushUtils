package com.wl.pushutils.push;

import android.content.Context;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils
 * @Author: HSL
 * @Time: 2019/05/17 08:58
 * @E-mail: xxx@163.com
 * @Description: 推送回调类~
 */
public interface PushMessageCallBack {

    /**
     * 通知栏通知到达
     *
     * @param context
     * @param str
     */
    void onPushNoticeArrive(Context context, String str);

    /**
     * 通知栏通知点击
     *
     * @param context
     * @param str
     */
    void onPushNoticeClick(Context context, String str);

    /**
     * 透传消息
     *
     * @param context
     * @param str
     */
    void onReceiverMessage(Context context, String str);

    /**
     * 华为Token
     *
     * @param context
     * @param token
     */
    void onReceiverHuaWeiToken(Context context, String token);
}
