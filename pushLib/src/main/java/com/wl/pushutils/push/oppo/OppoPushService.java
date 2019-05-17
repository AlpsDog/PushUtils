package com.wl.pushutils.push.oppo;

import android.content.Context;

import com.coloros.mcssdk.PushService;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.CommandMessage;
import com.coloros.mcssdk.mode.SptDataMessage;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;

/**
 * @Author: HSL
 * @Time: 2019/5/9 9:12
 * @E-mail: xxx@163.com
 * @Description: 如果应用需要解析和处理Push消息（如透传消息），则继承PushService来处理，并在Manifest文件中申明Service
 * 如果不需要处理Push消息，则不需要继承PushService，直接在Manifest文件申明PushService即可~
 */
public class OppoPushService extends PushService {

    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    /**
     * 命令消息，主要是服务端对客户端调用的反馈，一般应用不需要重写此方法
     *
     * @param context
     * @param commandMessage
     */
    @Override
    public void processMessage(Context context, CommandMessage commandMessage) {
        super.processMessage(context, commandMessage);
        // TODO: 2019/5/9
    }

    /**
     * 普通应用消息，视情况看是否需要重写
     *
     * @param context
     * @param appMessage
     */
    @Override
    public void processMessage(Context context, AppMessage appMessage) {
        super.processMessage(context, appMessage);
        String content = appMessage.getContent();
        // TODO: 2019/5/9
        mMessageCallBack.onPushNoticeClick(context, "");
        mMessageCallBack.onPushNoticeArrive(context, "");
    }


    /**
     * 透传消息处理，应用可以打开页面或者执行命令,如果应用不需要处理透传消息，则不需要重写此方法
     *
     * @param context
     * @param sptDataMessage
     */
    @Override
    public void processMessage(Context context, SptDataMessage sptDataMessage) {
        super.processMessage(context.getApplicationContext(), sptDataMessage);
        String content = sptDataMessage.getContent();
        // TODO: 2019/5/9
        mMessageCallBack.onReceiverMessage(context, "");
    }
}
