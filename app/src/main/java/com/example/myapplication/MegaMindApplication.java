package com.example.myapplication;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class MegaMindApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        boolean modoEscuro = getSharedPreferences("config", MODE_PRIVATE)
                .getBoolean("modo_escuro", false);

        AppCompatDelegate.setDefaultNightMode(
                modoEscuro
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}