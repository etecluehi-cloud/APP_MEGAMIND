package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TelaSecaoQuestoesMat extends AppCompatActivity {

    CardView cardGeometriaPlana, cardGeometriaEspacial, cardFuncoes, cardEstatistica,
            cardProbabilidade, cardAnalise, cardRazao, cardRegraDeTres,
            cardPorcentagem, cardMatFinanceira, cardTrigonometria,
            cardMatrizes, cardSistemasLineares;

    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_questoes_mat);

        // Linkando os cards
        cardGeometriaPlana    = findViewById(R.id.cardGeometriaPlana);
        cardGeometriaEspacial = findViewById(R.id.cardGeometriaEspacial);
        cardFuncoes           = findViewById(R.id.cardFuncoes);
        cardEstatistica       = findViewById(R.id.cardEstatistica);
        cardProbabilidade     = findViewById(R.id.cardProbabilidade);
        cardAnalise           = findViewById(R.id.cardAnalise);
        cardRazao             = findViewById(R.id.cardRazao);
        cardRegraDeTres       = findViewById(R.id.cardRegraDeTres);
        cardPorcentagem       = findViewById(R.id.cardPorcentagem);
        cardMatFinanceira     = findViewById(R.id.cardMatFinanceira);
        cardTrigonometria     = findViewById(R.id.cardTrigonometria);
        cardMatrizes          = findViewById(R.id.cardMatrizes);
        cardSistemasLineares  = findViewById(R.id.cardSistemasLineares);
        btnVoltar             = findViewById(R.id.btnVoltar);

        // Botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Geometria Plana
        cardGeometriaPlana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "geometria_plana");
                startActivity(it);
            }
        });

        // Geometria Espacial
        cardGeometriaEspacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "geometria_espacial");
                startActivity(it);
            }
        });

        // Funções
        cardFuncoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "funcoes");
                startActivity(it);
            }
        });

        // Estatística
        cardEstatistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "estatistica");
                startActivity(it);
            }
        });

        // Probabilidade
        cardProbabilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "probabilidade");
                startActivity(it);
            }
        });

        // Análise Combinatória
        cardAnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "analise_combinatoria");
                startActivity(it);
            }
        });

        // Razão e Proporção
        cardRazao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "razao_proporcao");
                startActivity(it);
            }
        });

        // Regra de Três
        cardRegraDeTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "regra_de_tres");
                startActivity(it);
            }
        });

        // Porcentagem
        cardPorcentagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "porcentagem");
                startActivity(it);
            }
        });

        // Matemática Financeira
        cardMatFinanceira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "matematica_financeira");
                startActivity(it);
            }
        });

        // Trigonometria
        cardTrigonometria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "trigonometria");
                startActivity(it);
            }
        });

        // Matrizes
        cardMatrizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "matrizes");
                startActivity(it);
            }
        });

        // Sistemas Lineares
        cardSistemasLineares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesMat.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "sistemas_lineares");
                startActivity(it);
            }
        });
    }
}