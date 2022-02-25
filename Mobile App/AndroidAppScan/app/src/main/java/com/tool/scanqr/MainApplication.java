package com.tool.scanqr;

import android.app.Application;

import com.marcinorlowski.fonty.Fonty;

public class MainApplication extends Application {

    private static Application application;

    public static Application getInstance() {
        return application;
    }

    public MainApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        Fonty
                .context(this)
                .normalTypeface("GothamMedium.ttf")
                .italicTypeface("GothamMediumItalic.ttf")
                .boldTypeface("GothamBold.ttf")
                .build();
    }
}
