package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ConteudoPort extends AppCompatActivity
{
    //1) atributos
    RecyclerView recyclerView; // lista visual em grade de cards
    List<Curso> lista; // lista de cursos que será exibida
    CursoAdapter adapter; // adapter que conecta a lista ao RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo_port);

        // ligar com o xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // substituir essa simulação por dados reais do banco de dados (back-end)
        // criar lista - SIMULAÇÃO
        lista = new ArrayList<>();
        lista.add(new Curso("Leitura e Interpretação de Texto", 7, 2));
        lista.add(new Curso("Gêneros Textuais", 7, 1));
        lista.add(new Curso("Linguagem e Variação Linguística", 4, 1));
        lista.add(new Curso("Gramática Contextualizada", 7, 0));
        lista.add(new Curso("Coesão e Coerência Textual", 3, 2));
        lista.add(new Curso("Figuras de Linguagem", 10, 1));
        lista.add(new Curso("Literatura", 4, 3));

        // adapter
        adapter = new CursoAdapter(lista);

        // 2 colunas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // conectar adapter
        recyclerView.setAdapter(adapter);


    }
}