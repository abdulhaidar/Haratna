package com.haratna;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class App extends Application {
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue mRequestQueue;
    private static App sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }
    public static synchronized App getInstance() {
        return sInstance;
    }
    public static Context getContext() {
        return sInstance.getApplicationContext();
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.e("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);

        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
