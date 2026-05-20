package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Desempenho extends AppCompatActivity
{

    // 1) atributos
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desempenho);

        // 2) linkando elementos
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnDesempenho = (ImageButton) findViewById(R.id.btnDesempenho);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
        btnPerfil = (ImageButton) findViewById(R.id.btnPerfil);

        // eventos do botoes do footer
        btnHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Desempenho.this, Home.class);
                startActivity(it);
            }
        });

        btnDesempenho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Desempenho.this, Desempenho.class);
                startActivity(it);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Desempenho.this, Curso.Buscar.class);
                startActivity(it);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Desempenho.this, PerfilUsuario.class);
                startActivity(it);
            }
        });

    }
}