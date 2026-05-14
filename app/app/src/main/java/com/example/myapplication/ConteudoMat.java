package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ConteudoMat extends AppCompatActivity
{
    //1) atributos
    RecyclerView recyclerView; // lista visual em grade de cards
    List<Curso> lista; // lista de cursos que será exibida
    CursoAdapter adapter; // adapter que conecta a lista ao RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo_mat);

        // ligar com o xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // substituir essa simulação por dados reais do banco de dados (back-end)
        // criar lista - SIMULAÇÃO
        lista = new ArrayList<>();
        lista.add(new Curso("Matemática Básica", 5, 2));
        lista.add(new Curso("Porcentagem", 4, 1));
        lista.add(new Curso("Álgebra", 4, 1));
        lista.add(new Curso("Funções", 5, 0));
        lista.add(new Curso("Geometria Plana", 4, 0));
        lista.add(new Curso("Geometria Espacial", 6, 1));
        lista.add(new Curso("Grandezas e Medidas", 4, 3));
        lista.add(new Curso("Estatística", 5, 3));
        lista.add(new Curso("Probabilidade", 2, 0));
        lista.add(new Curso("Matemática Financeira", 3, 1));

        // adapter
        adapter = new CursoAdapter(lista);

        // 2 colunas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // conectar adapter
        recyclerView.setAdapter(adapter);


    }
}