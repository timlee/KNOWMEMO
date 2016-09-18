package com.knowmemo.usermanagement;

import android.app.Application;

import knowmemoAPI.knowmemoConnection;

/**
 * Created by ShiangChiLee on 2016/3/15.
 */
public class global extends Application {

    @Override
    public void onCreate(){
        try {
           knowmemoAPI.knowmemoAPI.initAPI(this,new knowmemoConnection());
            super.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
