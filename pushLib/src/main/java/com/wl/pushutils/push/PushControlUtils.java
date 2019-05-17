package com.wl.pushutils.push;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.coloros.mcssdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.wl.pushutils.push.huawei.HMSAgent;
import com.wl.pushutils.push.huawei.common.handler.ConnectHandler;
import com.wl.pushutils.push.huawei.handler.EnableReceiveNormalMsgHandler;
import com.wl.pushutils.push.huawei.handler.EnableReceiveNotifyMsgHandler;
import com.wl.pushutils.push.huawei.handler.GetTokenHandler;
import com.wl.pushutils.push.jpush.JPushCode;
import com.wl.pushutils.push.oppo.OppoPushCallBack;
import com.wl.pushutils.uitls.RomUtils;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.push
 * @Author: HSL
 * @Time: 2019/05/07 18:50
 * @E-mail: xxx@163.com
 * @Description: 推送开关控制~
 */
public class PushControlUtils implements PushMessageCallBack {

    private static PushControlUtils mPushControlUtils;
    private BasePushReceiver mBasePushReceiver;

    public PushControlUtils() {
        // TODO: 2019/5/17
    }

    public static PushControlUtils getInstance() {
        if (mPushControlUtils == null) {
            mPushControlUtils = new PushControlUtils();
        }
        return mPushControlUtils;
    }

    /**
     * 初始化推送
     * <p>
     * 根据不同机型，初始化对应厂商PUSH，
     * </P>
     */
    public void initPushInApplication(Application application, BasePushReceiver basePushReceiver) {
        if (basePushReceiver == null) {
            new Throwable("basePushReceiver is null!!");
        }
        mBasePushReceiver = basePushReceiver;
        if (RomUtils.isHuawei()) {
            //初始化华为推送
            HMSAgent.init(application);
        } else if (RomUtils.isXiaomi()) {
            //初始化小米推送
            initMiPush(application);
        } else if (RomUtils.isVivo()) {
            // TODO: 2019/5/8 VIVO没有透传消息，通知栏通知到达时也没有回调，只有点击才会有回调结果，
            // TODO: 2019/5/8 针对这种情况，同时也初始化极光推送，用极光推送来处理通知到达时的回调。
            // TODO: 2019/5/8 需要对极光做处理：1.静默处理 2.在极光通知到达时，极光的回调广播里立即
            // TODO: 2019/5/8 清除所有极光的通知，只留下VIVO的，这样瞒天过海，偷天换日。3.推送开关
            // TODO: 2019/5/8 出添加极光相应的开关
            //ViVo
            initViVoPush(application);
            //设置开启日志,发布时请关闭日志
            JPushInterface.setDebugMode(true);
            //初始化极光推送
            JPushInterface.init(application);
            //极光静默0点0分-23点59分
            JPushInterface.setSilenceTime(application, 0, 0, 23, 59);
        } else if (RomUtils.isOppo()) {
            //OPPO
            initOPPOPush(application);
        } else if (RomUtils.isMeizu()) {
            //魅族
            initMeiZu(application);
        } else {
            //设置开启日志,发布时请关闭日志
            JPushInterface.setDebugMode(true);
            //初始化极光推送
            JPushInterface.init(application);
        }
        //LOG
        initLogger();
    }

    /**
     * 推送额外需要在Activity初始化内容
     */
    public void initPushExtraInActivity(Activity activity) {
        if (RomUtils.isHuawei()) {
            //华为要求
            //请务必在应用启动后的首个activity的onCreate方法中调用connect接口，确保HMS SDK和HMS APK的连接。
            HMSAgent.connect(activity, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    // TODO: 2019/5/17 连接结果
                    Logger.t("huawei").d("连接结果：" + rst);
                }
            });
            HMSAgent.Push.getToken(new GetTokenHandler() {
                @Override
                public void onResult(int rst) {
                    Logger.t("huawei").d("获取Token结果：" + rst);
                }
            });
        } else if (RomUtils.isXiaomi()) {
            //小米推送

        } else if (RomUtils.isVivo()) {

        } else if (RomUtils.isOppo()) {

        } else if (RomUtils.isMeizu()) {

        } else {

        }
    }

    /**
     * 初始化Logger
     */
    private void initLogger() {
        PrettyFormatStrategy.Builder newBuilder = PrettyFormatStrategy.newBuilder();
        //（可选）是否显示线程信息。 默认值为true
        newBuilder.showThreadInfo(true);
        //（可选）要显示的方法行数。 默认2
        newBuilder.methodCount(2);
        //（可选）设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
        newBuilder.methodOffset(0);
        //（可选）每个日志的全局标记。 默认PRETTY_LOGGER（如上图）
        newBuilder.tag("log-wl");
        FormatStrategy formatStrategy = newBuilder.build();
        //修改全局的TAG
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        //控制打印开关
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
        //保存log到文件 /storage/emulated/0
        Logger.addLogAdapter(new DiskLogAdapter());
    }

    /**
     * 推送开关控制
     *
     * @param context
     * @param enable
     */
    public void enablePush(Context context, boolean enable) {
        if (RomUtils.isHuawei()) {
            enableHuaWeiPush(enable);
        } else if (RomUtils.isXiaomi()) {
            enableXiaoMiPush(context, enable);
        } else if (RomUtils.isVivo()) {
            enableVIVOPush(context, enable);
        } else if (RomUtils.isMeizu()) {
            enableMeiZuPush(context, enable);
        } else if (RomUtils.isOppo()) {
            enableOPPOPush(context, enable);
        } else {
            //极光开关
            enableJPush(context, enable);
        }
    }

    /**
     * 小米推送
     */
    private void initMiPush(Application application) {
        //正式
        String appID = "2882303761517514040";
        String appKey = "5131751434040";
        if (shouldInit(application)) {
            //初始化mi push推送服务
            MiPushClient.registerPush(application, appID, appKey);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Logger.t("xiaomi").d(content, t);
            }

            @Override
            public void log(String content) {
                Logger.t("xiaomi").d(content);
            }
        };
        com.xiaomi.mipush.sdk.Logger.setLogger(application, newLogger);

    }

    private boolean shouldInit(Application application) {
        ActivityManager am = ((ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = application.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * VIVO推送
     */
    private void initViVoPush(Application application) {
        if (!PushClient.getInstance(application).isSupport()) {
            Logger.t("vivo").d("系统不支持PUSH!");
            return;
        }
        // 在当前工程入口函数，建议在 Application 的 onCreate 函数中，添加以下代码：
        PushClient.getInstance(application).initialize();
        // 当需要打开推送服务时，调用以下代码：
        PushClient.getInstance(application).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                // TODO: 开关状态处理
                Logger.t("vivo").d("开关状态：" + state);
            }
        });
    }

    /**
     * OPPO推送
     */
    private void initOPPOPush(Application application) {
        String appKey = "";
        String appSecret = "";
        if (!PushManager.isSupportPush(application)) return;
        PushManager.getInstance().register(application, appKey, appSecret, new OppoPushCallBack());
    }

    /**
     * 魅族推送
     */
    private void initMeiZu(Application application) {
        String appID = "1005682";
        String appKey = "40b210344e2044c3b0f4d0f4898c37e7";
        if (MzSystemUtils.isBrandMeizu(application)) {
            com.meizu.cloud.pushsdk.PushManager.register(application, appID, appKey);
        }
    }

    /**
     * 华为推送开关控制
     *
     * @param enable
     */
    private void enableHuaWeiPush(boolean enable) {
        //透传消息
        HMSAgent.Push.enableReceiveNormalMsg(enable, new EnableReceiveNormalMsgHandler() {
            @Override
            public void onResult(int rst) {
                Logger.t("huawei").d("透传消息控制：" + rst);
            }
        });
        //通知栏通知
        HMSAgent.Push.enableReceiveNotifyMsg(enable, new EnableReceiveNotifyMsgHandler() {
            @Override
            public void onResult(int rst) {
                Logger.t("huawei").d("通知控制：" + rst);
            }
        });
    }

    /**
     * 小米推送开关控制
     *
     * @param context
     * @param enable
     */
    private void enableXiaoMiPush(Context context, boolean enable) {
        if (enable) {
            //开启推送
            MiPushClient.resumePush(context, null);
        } else {
            //暂定推送
            MiPushClient.pausePush(context, null);
        }
    }

    /**
     * VIVO推送开关控制
     *
     * @param context
     * @param enable
     */
    private void enableVIVOPush(Context context, boolean enable) {
        // TODO: 2019/5/8 VIVO没有透传消息，通知栏通知到达时也没有回调，只有点击才会有回调结果，
        // TODO: 2019/5/8 针对这种情况，同时也初始化极光推送，用极光推送来处理通知到达时的回调。
        // TODO: 2019/5/8 需要对极光做处理：1.静默处理 2.在极光通知到达时，极光的回调广播里立即
        // TODO: 2019/5/8 清除所有极光的通知，只留下VIVO的，这样瞒天过海，偷天换日。3.推送开关
        // TODO: 2019/5/8 出添加极光相应的开关
        if (enable) {
            PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int i) {
                    if (i != 0) {
                        Logger.t("vivo").d("vivo推送打开成功~");
                    } else {
                        Logger.t("vivo").d("vivo推送打开失败~");
                    }
                }
            });
        } else {
            PushClient.getInstance(context).turnOffPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int i) {
                    if (i != 0) {
                        Logger.t("vivo").d("vivo推送关闭成功~");
                    } else {
                        Logger.t("vivo").d("vivo推送关闭失败~");
                    }
                }
            });
        }
        //极光开关
        enableJPush(context, enable);
    }

    /**
     * OPPO推送开关控制
     *
     * @param context
     * @param enable
     */
    private void enableOPPOPush(Context context, boolean enable) {
        if (enable) {
            PushManager.getInstance().resumePush();
        } else {
            PushManager.getInstance().pausePush();
        }
    }

    /**
     * 魅族推送开关控制
     *
     * @param context
     * @param enable
     */
    private void enableMeiZuPush(Context context, boolean enable) {
        String appID = "";
        String appKey = "";
        //注册成功后返回的pushID
        String pushID = "";
        com.meizu.cloud.pushsdk.PushManager.switchPush(context, appID, appKey, pushID, enable);
    }

    /**
     * 极光推送开关控制
     *
     * @param context
     * @param enable
     */
    private void enableJPush(Context context, boolean enable) {
        //极光开关
        if (enable) {
            if (JPushInterface.isPushStopped(context))
                JPushInterface.resumePush(context);
        } else {
            JPushInterface.stopPush(context);
        }
    }

    /**
     * 设置推送TAG
     *
     * @param tag
     */
    public void setPushTag(Context context, String tag) {
        if (RomUtils.isHuawei()) {
            // TODO: 2019/5/17
        } else if (RomUtils.isXiaomi()) {
            //订阅主题
            MiPushClient.subscribe(context, tag, null);
        } else if (RomUtils.isVivo()) {
            PushClient.getInstance(context).setTopic(tag, new IPushActionListener() {
                @Override
                public void onStateChanged(int i) {

                }
            });
        } else if (RomUtils.isMeizu()) {
            String pushId = com.meizu.cloud.pushsdk.PushManager.getPushId(context);
            com.meizu.cloud.pushsdk.PushManager.
                    subScribeTags(context, "appid", "appKey", pushId, tag);
        } else if (RomUtils.isOppo()) {
            List<String> tags = new ArrayList<>();
            tags.add(tag);
            PushManager.getInstance().setTags(tags);
        } else {
            //极光
            Set<String> set = new HashSet<>();
            set.add(tag);
            JPushInterface.setTags(context, JPushCode.ADD, set);
        }
    }

    /**
     * 取消推送TAG
     *
     * @param context
     * @param tag
     */
    public void cancelPushTag(Context context, String tag) {
        // TODO: 2019/5/17
    }


    /**
     * 通知栏推送到达
     *
     * @param context
     * @param str
     */
    @Override
    public void onPushNoticeArrive(Context context, String str) {
        if (mBasePushReceiver != null) {
            mBasePushReceiver.onPushNoticeArrive(context, str);
        }
    }

    /**
     * 通知栏推送点击
     *
     * @param context
     * @param str
     */
    @Override
    public void onPushNoticeClick(Context context, String str) {
        if (mBasePushReceiver != null) {
            mBasePushReceiver.onPushNoticeClick(context, str);
        }
    }

    /**
     * 透传消息回调
     *
     * @param context
     * @param str
     */
    @Override
    public void onReceiverMessage(Context context, String str) {
        if (mBasePushReceiver != null) {
            mBasePushReceiver.onReceiverMessage(context, str);
        }
    }

    /**
     * 华为Token回调
     *
     * @param context
     * @param token
     */
    @Override
    public void onReceiverHuaWeiToken(Context context, String token) {
        if (mBasePushReceiver != null) {
            mBasePushReceiver.onReceiverHuaWeiToken(context, token);
        }
    }
}
