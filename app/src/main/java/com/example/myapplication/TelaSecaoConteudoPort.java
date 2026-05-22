package com.example.myapplication; // ← troque pelo seu pacote real

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class TelaSecaoConteudoPort extends AppCompatActivity
{
    //1) atributos
    RecyclerView recyclerView;
    List<Curso> lista;
    CursoAdapter adapter;
    ImageButton btnVoltar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_port);

        // ligar xml
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // lista
        lista = new ArrayList<>();

        lista.add(new Curso(
                "Leitura e Interpretação",
                "leitura_interpretacao",
                false));

        lista.add(new Curso(
                "Gêneros Textuais",
                "generos_textuais",
                false));

        lista.add(new Curso(
                "Variação Linguística",
                "variacao_linguistica",
                false));

        lista.add(new Curso(
                "Gramática",
                "gramatica",
                false));

        lista.add(new Curso(
                "Coesão e Coerência",
                "coesao_coerencia",
                false));

        lista.add(new Curso(
                "Figuras de Linguagem",
                "figuras_linguagem",
                false));

        lista.add(new Curso(
                "Literatura",
                "literatura",
                false));

        // adapter
        adapter = new CursoAdapter(lista);

        // grid 2 colunas
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);

        // voltar
        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });
    }
}