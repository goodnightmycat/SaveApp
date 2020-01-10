package com.example.saveapp;

import android.content.Context;

public class ContextHolder {
    private static Context contextHolder;

    public static void init(Context context) {
        contextHolder = context;
    }

    public static Context getContext() {
        return contextHolder;
    }
}
