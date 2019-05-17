package com.wl.pushutils.push.jpush;

import android.content.Context;
import android.os.Handler;

import com.orhanobut.logger.Logger;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @Project: loan_market_android
 * @Package: com.yunxi.dc.receiver
 * @Description: 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class JPushMsgReceiver extends JPushMessageReceiver {

    private int mSetRetryNumber = 0;
    private int mDeleteRetryNumber = 0;

    /**
     * tag增删查改的操作结果
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        switch (jPushMessage.getSequence()) {
            case JPushCode.ADD:
                onADDJPushTagResult(context, jPushMessage);
                break;
            case JPushCode.DELETE:
                onDeleteJPushTagResult(context, jPushMessage);
                break;
            default:
                break;
        }
    }

    /**
     * 查询某个Tag与当前用户的绑定状态的结果
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {

    }

    /**
     * alias 相关的操作结果
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        switch (jPushMessage.getSequence()) {
            case JPushCode.SET:
                onSetJPushAliasResult(context, jPushMessage);
                break;
            case JPushCode.DELETE:
                onDeleteJPushAliasResult(context, jPushMessage);
                break;
            default:
                break;
        }
    }

    /**
     * 设置手机号码结果
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {

    }

    /**
     * 设置别名结果处理
     *
     * @param context
     * @param jPushMessage
     */
    private void onSetJPushAliasResult(final Context context, JPushMessage jPushMessage) {
        int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            mSetRetryNumber = 0;
            Logger.t("jp").d("别名设置成功!");
            return;
        }
        if (errorCode == 6002 || errorCode == 6024) {
            //6002超时,6024服务器内部错误,延时5S重试，重试5次
            if (mSetRetryNumber > 4) {
                Logger.t("jp").e("jPush设置别名失败");
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    JPushInterface.setAlias(context, JPushCode.SET, "");
                    mSetRetryNumber++;
                }
            }, 5000);
        }
    }

    /**
     * 删除别名
     *
     * @param context
     * @param jPushMessage
     */
    private void onDeleteJPushAliasResult(final Context context, JPushMessage jPushMessage) {
        int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            mDeleteRetryNumber = 0;
            Logger.t("jp").d("别名删除成功!");
            return;
        }
        if (errorCode == 6002 || errorCode == 6024) {
            //6002超时,6024服务器内部错误,延时5S重试，重试5次
            if (mDeleteRetryNumber > 4) {
                Logger.t("jp").e("jPush删除别名失败");
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    JPushInterface.deleteAlias(context, JPushCode.DELETE);
                    mDeleteRetryNumber++;
                }
            }, 5000);
        }
    }

    private void onADDJPushTagResult(final Context context, JPushMessage jPushMessage) {
        int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            mSetRetryNumber = 0;
            Logger.t("jp").d("TAG设置成功!");
            return;
        }
        if (errorCode == 6002 || errorCode == 6024) {
            //6002超时,6024服务器内部错误,延时5S重试，重试5次
        }
    }

    private void onDeleteJPushTagResult(final Context context, JPushMessage jPushMessage) {
        int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            mDeleteRetryNumber = 0;
            Logger.t("jp").d("TAG删除成功!");
            return;
        }
        if (errorCode == 6002 || errorCode == 6024) {
            //6002超时,6024服务器内部错误,延时5S重试，重试5次

        }
    }

}
