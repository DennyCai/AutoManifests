package com.denny.automanifests;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.denny.annotation.Activity;

@Activity(configChanges = "+|orientation|keyboard")
public class Auto2Activity extends AutoActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
