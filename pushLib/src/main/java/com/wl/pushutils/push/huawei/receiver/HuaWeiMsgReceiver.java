package com.wl.pushutils.push.huawei.receiver;

import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.orhanobut.logger.Logger;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.receiver.huawei
 * @Author: HSL
 * @Time: 2019/04/30 12:59
 * @E-mail: xxx@163.com
 * @Description: 应用需要创建一个子类继承com.huawei.hms.support.api.push.PushReceiver，
 * 实现onToken，onPushState ，onPushMsg，onEvent，这几个抽象方法，用来接收token返回，push连接状态，透传消息和通知栏点击事件处理。
 * onToken 调用getToken方法后，获取服务端返回的token结果，返回token以及belongId
 * onPushState 调用getPushState方法后，获取push连接状态的查询结果
 * onPushMsg 推送消息下来时会自动回调onPushMsg方法实现应用透传消息处理。本接口必须被实现。 在开发者网站上发送push消息分为通知和透传消息
 * 通知为直接在通知栏收到通知，通过点击可以打开网页，应用 或者富媒体，不会收到onPushMsg消息
 * 透传消息不会展示在通知栏，应用会收到onPushMsg
 * onEvent 该方法会在设置标签、点击打开通知栏消息、点击通知栏上的按钮之后被调用。由业务决定是否调用该函数。
 */
public class HuaWeiMsgReceiver extends PushReceiver {

    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        super.onToken(context, token, extras);
        Logger.t("huawei").d("推送token:" + token);
        mMessageCallBack.onReceiverHuaWeiToken(context, token);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        super.onPushState(context, pushState);
        Logger.t("huawei").d("连接状态" + pushState);
    }

    /**
     * 处理透传消息
     *
     * @param context
     * @param msgBytes
     * @param extras
     * @return
     */
    @Override
    public boolean onPushMsg(Context context, byte[] msgBytes, Bundle extras) {
        // TODO: 2019/5/17  
        String msg = new String(msgBytes);
        Logger.t("huawei").d("透传内容：" + msg);
        //华为到达--》用透传消息代替
        mMessageCallBack.onPushNoticeArrive(context, msg);
        return super.onPushMsg(context, msgBytes, extras);
    }
}
