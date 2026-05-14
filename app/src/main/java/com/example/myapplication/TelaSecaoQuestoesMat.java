package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TelaSecaoQuestoesMat extends AppCompatActivity {

    // Os botões são CardViews — igual ao seu XML
    CardView cardGeometriaPlana, cardGeometriaEspacial, cardGrandezas,
            cardEstatistica, cardProbabilidade, cardFuncoes;

    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_questoes_mat);

        // Linkando os cards — os IDs são exatamente os do seu XML
        cardGeometriaPlana   = findViewById(R.id.cardGeometriaPlana);
        cardGeometriaEspacial= findViewById(R.id.cardGeometriaEspacial);
        cardGrandezas        = findViewById(R.id.cardGrandezas);
        cardEstatistica      = findViewById(R.id.cardEstatistica);
        cardProbabilidade    = findViewById(R.id.cardProbabilidade);
        cardFuncoes          = findViewById(R.id.cardFuncoes);
        btnVoltar            = findViewById(R.id.btnVoltar);

        // Botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Geometria Plana → passa "geometria_plana" para TelaQuestoes
        cardGeometriaPlana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "geometria_plana");
                startActivity(it);
            }
        });

        // Geometria Espacial → passa "geometria_espacial"
        cardGeometriaEspacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "geometria_espacial");
                startActivity(it);
            }
        });

        // Grandezas e Medidas → passa "grandezas"
        cardGrandezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "grandezas");
                startActivity(it);
            }
        });

        // Estatística → passa "estatistica"
        cardEstatistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "estatistica");
                startActivity(it);
            }
        });

        // Probabilidade → passa "probabilidade"
        cardProbabilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "probabilidade");
                startActivity(it);
            }
        });

        // Funções → passa "funcoes"
        cardFuncoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "funcoes");
                startActivity(it);
            }
        });
    }
}
