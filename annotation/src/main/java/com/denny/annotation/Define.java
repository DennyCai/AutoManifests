package com.denny.annotation;

/**
 * Created by caidong on 2018/6/26.
 */
public interface Define {

    String Null = "";

    String APPLICATION = "android.app.Application";
    String PROVIDER = "android.content.ContentProvider";
    String SERVICE = "android.app.Service";
    String RECEIVER = "android.content.BroadcastReceiver";

    interface Activity {
        String APP = "android.app.Activity";
        String APPCOMPAT = "android.support.v7.app.AppCompatActivity";
    }

}
