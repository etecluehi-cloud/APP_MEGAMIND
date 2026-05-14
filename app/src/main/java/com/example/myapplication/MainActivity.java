package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    // 1) atributos
    Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        boolean modoEscuro = getSharedPreferences("config", MODE_PRIVATE)
                .getBoolean("modo_escuro", false);

        if (modoEscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2) linkando os elementos
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);

        // 3) evento do btnCadastro
        btnCadastro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(MainActivity.this, TelaCadastro.class);
                startActivity(it);
            }
        });

        // 4) evento do btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(MainActivity.this, TelaLogin.class);
                startActivity(it);
            }
        });
    }
}