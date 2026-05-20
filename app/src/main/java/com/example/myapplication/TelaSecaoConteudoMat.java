package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

public class TelaSecaoConteudoMat extends AppCompatActivity {

    //10 atributos
    RecyclerView recyclerView;
    List<Curso> lista;
    CursoAdapter adapter;
    ImageButton btnVoltar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_mat);

        //2) linkando os elementos

        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        lista = new ArrayList<>();

        lista.add(new Curso("Matemática Básica", 4, 1,
                "matematica_basica"));

        lista.add(new Curso("Porcentagem", 4, 0,
                "porcentagem"));

        lista.add(new Curso("Álgebra", 4, 1,
                "algebra"));

        lista.add(new Curso("Funções", 5, 2,
                "funcoes"));

        lista.add(new Curso("Geometria Plana", 4, 0,
                "geometria_plana"));

        lista.add(new Curso("Geometria Espacial", 6, 0,
                "geometria_espacial"));

        lista.add(new Curso("Grandezas e Medidas", 4, 3,
                "grandezas_medidas"));

        lista.add(new Curso("Estatística", 5, 0,
                "estatistica"));

        lista.add(new Curso("Probabilidade", 2, 0,
                "probabilidade"));

        lista.add(new Curso("Matemática Financeira", 3, 1,
                "matematica_financeira"));

        //adapter
        adapter = new CursoAdapter(lista);

        // grid 2 colunas
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);

        //3) evento do btnVoltar
        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });


    }
}