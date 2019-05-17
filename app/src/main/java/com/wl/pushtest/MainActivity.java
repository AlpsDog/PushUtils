package com.wl.pushtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wl.pushutils.push.PushControlUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushControlUtils.getInstance().initPushExtraInActivity(this);
        PushControlUtils.getInstance().setPushTag(this,"53231323");
    }
}
