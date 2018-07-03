package com.denny.automanifests;

import android.support.v7.app.AppCompatActivity;

import com.denny.annotation.LaunchMode;

@com.denny.annotation.Activity(
        launchMode = LaunchMode.SingleTop,
        taskAffinity = "com.denny.launcher",
        ignore = true
)
public class BaseActivity extends AppCompatActivity {
}
