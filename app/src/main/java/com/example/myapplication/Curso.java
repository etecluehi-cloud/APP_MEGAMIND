package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

// classe que representa um curso/matéria do app
public class Curso {

    // atributos privados - só acessados pelos getters e setters
    private String nome; // nome do conteúdo
    private int totalAulas; // total de aulas desse conteúdo
    private int aulasAssistidas; // quantas aulas o usuário já assistiu

    // Construtor - usado para criar um novo objeto Curso com os dados
    public Curso(String nome, int totalAulas, int aulasAssistidas)
    {
        this.nome = nome;
        this.totalAulas = totalAulas;
        this.aulasAssistidas = aulasAssistidas;
    }

    // getters -> permitem ler os valores de fora da classe
    public String getNome()
        {
            return nome;
        }
    public int getTotalAulas()
        {
            return totalAulas;
        }
    public int getAulasAssistidas()
        {
            return aulasAssistidas;
        }

    // setter - permite atualizar as aulas assistidas
    public void setAulasAssistidas(int aulasAssistidas)
        {
            this.aulasAssistidas = aulasAssistidas;
        }

    public static class Buscar extends AppCompatActivity
    {

        // 1) atributos
        ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_buscar);

            // 2) linkando os elementos
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
                    Intent it = new Intent(Buscar.this, Home.class);
                    startActivity(it);
                }
            });

            btnDesempenho.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent it = new Intent(Buscar.this, Desempenho.class);
                    startActivity(it);
                }
            });

            btnBuscar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent it = new Intent(Buscar.this, Buscar.class);
                    startActivity(it);
                }
            });

            btnPerfil.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent it = new Intent(Buscar.this, PerfilUsuario.class);
                    startActivity(it);
                }
            });

        }
    }
}