package com.wl.pushutils.push.meizu;

import android.content.Context;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.push.meizu
 * @Author: HSL
 * @Time: 2019/05/09 10:21
 * @E-mail: xxx@163.com
 * @Description: 魅族实现自有的PushReceiver, 实现消息接收，注册与反注册回调~
 */
public class MeiZuPushMsgReceiver extends MzPushMessageReceiver {

    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    /**
     * 调用新版订阅PushManager.register(context,appId,appKey)回调
     *
     * @param context
     * @param registerStatus
     */
    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {

    }

    /**
     * 新版反订阅回调
     *
     * @param context
     * @param unRegisterStatus
     */
    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }


    /**
     * 调用PushManager.register(context）方法后，会在此回调注册状态
     * 应用在接受返回的pushid
     *
     * @param context
     * @param s
     */
    @Override
    public void onRegister(Context context, String s) {

    }

    /**
     * 调用PushManager.unRegister(context）方法后，会在此回调反注册状态
     *
     * @param context
     * @param b
     */
    @Override
    public void onUnRegister(Context context, boolean b) {

    }

    /**
     * 透传消息回调
     *
     * @param context
     * @param
     * @param
     */
    @Override
    public void onMessage(Context context, String message, String platformExtra) {
        super.onMessage(context, message, platformExtra);
        mMessageCallBack.onReceiverMessage(context, "");
    }

    /**
     * 通知点击
     *
     * @param context
     * @param mzPushMessage
     */
    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        mMessageCallBack.onPushNoticeClick(context, "");
    }

    /**
     * 通知到达
     *
     * @param context
     * @param mzPushMessage
     */
    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        super.onNotificationArrived(context, mzPushMessage);
        mMessageCallBack.onPushNoticeArrive(context, "");
    }

    /**
     * 检查通知栏和透传消息开关状态回调
     *
     * @param context
     * @param pushSwitchStatus
     */
    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    /**
     * 标签回调
     *
     * @param context
     * @param subTagsStatus
     */
    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    /**
     * 别名回调
     *
     * @param context
     * @param subAliasStatus
     */
    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }
}
