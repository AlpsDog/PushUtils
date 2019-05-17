package com.wl.pushutils.push.oppo;

import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;

import java.util.List;

/**
 * @Project: PushTest
 * @Package: com.wl.pushutils.push.oppo
 * @Author: HSL
 * @Time: 2019/05/09 09:01
 * @E-mail: xxx@163.com
 * @Description: SDK操作的回调，如不需要使用所有回调接口，可传入PushAdapter，选择需要的回调接口来重写~
 * 所有回调都需要根据responseCode来判断操作是否成功, 0 代表成功,其他代码失败
 */
public class OppoPushCallBack implements PushCallback {

    /**
     * 注册的结果,如果注册成功,registerID就是客户端的唯一身份标识
     *
     * @param i
     * @param s
     */
    @Override
    public void onRegister(int i, String s) {

    }

    /**
     * 反注册的结果
     *
     * @param i
     */
    @Override
    public void onUnRegister(int i) {

    }


    @Override
    public void onGetAliases(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onSetAliases(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onUnsetAliases(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onSetUserAccounts(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onGetUserAccounts(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onSetTags(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onUnsetTags(int i, List<SubscribeResult> list) {

    }

    @Override
    public void onGetTags(int i, List<SubscribeResult> list) {

    }

    /**
     * 获取当前的push状态返回,根据返回码判断当前的push状态,返回码具体含义可以参考[错误码]
     *
     * @param i
     * @param i1
     */
    @Override
    public void onGetPushStatus(int i, int i1) {

    }

    @Override
    public void onSetPushTime(int i, String s) {

    }

    @Override
    public void onGetNotificationStatus(int i, int i1) {

    }
}
