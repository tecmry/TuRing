package com.example.tecmry.turing.LeanCloudService;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

public class MyLeanCloud extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"lJGXtjDRUqFmsY7Kd4l2IkRC-gzGzoHsz","qfgqUbD3jW2eCfv1U8mdgV05");
        AVOSCloud.setDebugLogEnabled(true);
        AVAnalytics.enableCrashReport(this,true);
    }
}
