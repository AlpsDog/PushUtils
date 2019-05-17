package com.wl.pushutils.push.xiaomi;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.wl.pushutils.push.PushControlUtils;
import com.wl.pushutils.push.PushMessageCallBack;
import com.wl.pushutils.uitls.JsonUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;
import java.util.Map;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.push.xiaomi
 * @Author: HSL
 * @Time: 2019/05/06 11:47
 * @E-mail: xxx@163.com
 * @Description: 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 *   {@code
 *    <receiver
 *        android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *        android:exported="true">
 *        <intent-filter>
 *            <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *        </intent-filter>
 *        <intent-filter>
 *            <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *       </intent-filter>
 *        <intent-filter>
 *            <action android:name="com.xiaomi.mipush.ERROR" />
 *        </intent-filter>
 *    </receiver>
 *    }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 */
public class MiPushMsgReceiver extends PushMessageReceiver {

    private PushMessageCallBack mMessageCallBack = PushControlUtils.getInstance();

    /**
     * 接收服务器推送的透传消息
     *
     * @param context
     * @param message
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        //passThrough值为1,则是透传消息；passThrough值为0,则是通知栏消息
        int passThrough = message.getPassThrough();
        //消息的id
        String messageId = message.getMessageId();
        //消息的类型，分为三种:MESSAGE_TYPE_REG、MESSAGE_TYPE_ALIAS、MESSAGE_TYPE_TOPIC
        int messageType = message.getMessageType();
        //表示消息是否通过通知栏传给app的。如果为true，表示消息在通知栏出过通知；
        //如果为false，表示消息是直接传给app的，没有弹出过通知
        boolean notified = message.isNotified();
        //****
        int notifyId = message.getNotifyId();
        //推送标题
        String title = message.getTitle();
        //主题
        String topic = message.getTopic();
        //别名
        String alias = message.getAlias();
        //****
        String category = message.getCategory();
        //消息摘要
        String description = message.getDescription();
        //传输数据
        String content = message.getContent();
        //额外字段
        Map<String, String> extra = message.getExtra();

        Logger.t("xiaomi").d(
                "通知ro消息：" + passThrough + "\n"
                        + "消息ID:" + messageId + "\n"
                        + "消息的类型:" + messageType + "\n"
                        + "否通过通知栏:" + notified + "\n"
                        + "notifyId:" + title + "\n"
                        + "推送标题:" + messageId + "\n"
                        + "主题:" + topic + "\n"
                        + "alias:" + alias + "\n"
                        + "category:" + category + "\n"
                        + "消息摘要:" + description + "\n"
                        + "传输数据:" + content + "\n"
                        + "额外字段:" + extra
        );
        mMessageCallBack.onReceiverMessage(context, JsonUtils.toStr(extra));
    }

    /**
     * 接收服务器推送的通知消息，用户点击后触发
     *
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Logger.t("xiaomi").d("用户通知点击：" + message);
        Map<String, String> extra = message.getExtra();
        mMessageCallBack.onPushNoticeClick(context, JsonUtils.toStr(extra));
    }

    /**
     * 接收服务器推送的通知消息，消息到达客户端时触发，还可以接受应用在前台时不弹出通知的通知消息，
     * 消息封装在 MiPushMessage类中。
     * 在MIUI上，只有应用处于启动状态，或者自启动白名单中，才可以通过此方法接受到该消息
     *
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Logger.t("xiaomi").d("通知到达：" + message);
        Map<String, String> extra = message.getExtra();
        mMessageCallBack.onPushNoticeArrive(context, JsonUtils.toStr(extra));
    }

    /**
     * 获取给服务器发送命令的结果
     *
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                //注册成功
                String mRegId = cmdArg1;
                Logger.t("xiaomi").d("注册成功~:" + mRegId);
            } else {

            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mAlias = cmdArg1;
                //设置别名成功
            } else {

            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mAlias = cmdArg1;
                //取消指定用户的某个别名
            } else {
                // TODO: 2019/5/6
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mAccount = cmdArg1;
                //设置UserAccount成功
            } else {

            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mAccount = cmdArg1;
                //取消UserAccount
            } else {
                // TODO: 2019/5/6  
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mTopic = cmdArg1;
                //订阅主题成功
            } else {
                // TODO: 2019/5/6  
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mTopic = cmdArg1;
                //取消订阅主题
            } else {
                // TODO: 2019/5/6  
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mStartTime = cmdArg1;
                String mEndTime = cmdArg2;
                //设置接受时段成功
            } else {
                // TODO: 2019/5/6  
            }
        } else {
            log = message.getReason();
        }

    }

    /**
     * 获取给服务器发送注册命令的结果
     *
     * @param context
     * @param message
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                String mRegId = cmdArg1;
                //注册成功
                Logger.t("xiaomi").d("注册成功~");
                // TODO: 2019/5/7 调用接口，把regID上传服务器
            } else {
                // TODO: 2019/5/6
            }
        } else {
            log = message.getReason();
        }
    }

}
