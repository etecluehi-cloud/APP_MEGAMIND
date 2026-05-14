package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ConteudoRed extends AppCompatActivity
{
    //1) atributos
    RecyclerView recyclerView; // lista visual em grade de cards
    List<Curso> lista; // lista de cursos que será exibida
    CursoAdapter adapter; // adapter que conecta a lista ao RecyclerView
    ImageButton btnHome;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo_red);

        btnHome = (ImageButton) findViewById(R.id.btnHome);

        // ligar com o xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // substituir essa simulação por dados reais do banco de dados (back-end)
        // criar lista - SIMULAÇÃO
        lista = new ArrayList<>();
        lista.add(new Curso("Estrutura do Texto", 3, 1));
        lista.add(new Curso("Tipologia Textual", 2, 0));
        lista.add(new Curso("Cpmpetências da Redação do ENEM", 5, 3));
        lista.add(new Curso("Argumentação", 4, 0));
        lista.add(new Curso("Proposta de Intervenção", 5, 2));
        lista.add(new Curso("Temas Sociais Frequentes", 7, 1));
        lista.add(new Curso("Erros Que Zeram ou Diminuem a Nota", 4, 0));

        // adapter
        adapter = new CursoAdapter(lista);

        // 2 colunas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // conectar adapter
        recyclerView.setAdapter(adapter);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConteudoRed.this, Home.class);
                startActivity(it);
            }
        });
    }
}