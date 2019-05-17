package com.wl.pushutils.push.vivo;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.push.vivo
 * @Author: HSL
 * @Time: 2019/05/08 17:11
 * @E-mail: xxx@163.com
 * @Description: 这个人太懒，没留下什么踪迹~
 */
public class VIVOPushMsgReceiver extends OpenClientPushMessageReceiver {

    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    /**
     * 点击通知栏通知
     * // TODO: 2019/5/8 VIVO没有透传消息，通知栏通知到达时也没有回调，只有点击才会有回调结果，
     * // TODO: 2019/5/8 针对这种情况，同时也初始化极光推送，用极光推送来处理通知到达时的回调。
     * // TODO: 2019/5/8 需要对极光做处理：1.静默处理 2.在极光通知到达时，极光的回调广播里立即
     * // TODO: 2019/5/8 清除所有极光的通知，只留下VIVO的，这样瞒天过海，偷天换日。3.推送开关
     * // TODO: 2019/5/8 出添加极光相应的开关
     *
     * @param context
     * @param upsNotificationMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        mMessageCallBack.onPushNoticeClick(context, "");
    }

    @Override
    public void onReceiveRegId(Context context, String s) {
        Logger.t("vivo").d("vivo注册ID:" + s);
    }


}
