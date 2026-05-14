package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TelaCodigoSenha extends AppCompatActivity
{
    // 1) atributos
    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_codigo_senha);

        // 2) linkando os elementos
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);

        //3) evento do btnVoltar
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaCodigoSenha.this, TelaNovaSenha.class);
                startActivity(it);
            }
        });
    }
}