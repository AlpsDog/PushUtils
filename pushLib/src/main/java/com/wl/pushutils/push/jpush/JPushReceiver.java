package com.wl.pushutils.push.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;
import com.wl.pushutils.uitls.RomUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * @Description: 极光推送自定义接收器
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";
    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                //极光注册成功，分发RegistrationID
                //RegistrationID：SDK向JPush Server注册所得到的注册ID
                //一般来说，可不处理此广播信息
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                // TODO: 2018/11/12
                Logger.t(TAG).d("极光注册ID:%s", regId);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                //接收到自定义消息
                //自定义消息：SDK只是传递，不会有任何界面上的展示(通知栏不显示)，所以必须在此处处理
                //需要在 AndroidManifest.xml 里配置此 Receiver action
                operatorJPushMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //接收到JPush通知，如果通知为空，则在通知栏上不会展示通知
                //但是这个广播Intent还是会有,开发者可以取到通知内容外的其他信息
                //富媒体消息也在这里
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//标题
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);//对应于推送框内的信息
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//字段json
                //.........还有好多可以接受的内容，详见api
                // TODO: 2018/11/12
                Logger.t(TAG).d("极光推送通知："
                        + "\n" + "标题：" + title
                        + "\n" + "内容：" + content
                        + "\n" + "额外：" + extras);
                if (RomUtils.isVivo()) {
                    //移除所有通知~
                    // TODO: 2019/5/8 VIVO没有透传消息，通知栏通知到达时也没有回调，只有点击才会有回调结果，
                    // TODO: 2019/5/8 针对这种情况，同时也初始化极光推送，用极光推送来处理通知到达时的回调。
                    // TODO: 2019/5/8 需要对极光做处理：1.静默处理 2.在极光通知到达时，极光的回调广播里立即
                    // TODO: 2019/5/8 清除所有极光的通知，只留下VIVO的，这样瞒天过海，偷天换日。3.推送开关
                    // TODO: 2019/5/8 出添加极光相应的开关
                    JPushInterface.clearAllNotifications(context);
                }
                mMessageCallBack.onPushNoticeArrive(context, extras);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                //用户点击了通知栏的通知
                //没注册广播，默认打开应用程序的主Activity，相当于用户点击桌面图标的效果
                //注册了就不会默认打开主Activity，在这里处理逻辑
                onClickReceivedNotification(context, bundle);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                //此处尽然也可以处理富消息
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                // TODO: 2018/11/12

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                //JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);

            } else {

                // TODO: 2018/11/12
            }
        } catch (Exception e) {
            Logger.t(TAG).e(e.getMessage());
        }

    }

    /**
     * 处理JPush消息
     *
     * @param context
     * @param bundle
     */
    private void operatorJPushMessage(Context context, Bundle bundle) {
        //推送的标题
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        //推送的内容
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        //额外字段(JSON字符串)
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        //logger
        Logger.t(TAG).d("极光推送消息："
                + "\n" + "标题：" + title
                + "\n" + "内容：" + message
                + "\n" + "额外：" + extras);
        //业务处理
        mMessageCallBack.onReceiverMessage(context, extras);
    }

    /**
     * 处理通知栏通知的点击事件
     *
     * @param context
     * @param bundle
     */
    private void onClickReceivedNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//标题
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);//对应于推送框内的信息
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//字段json
        Logger.t(TAG).d("点击了通知栏的JPush通知："
                + "\n" + "标题：" + title
                + "\n" + "内容：" + content
                + "\n" + "额外：" + extras);
        mMessageCallBack.onPushNoticeClick(context, extras);
    }

}
