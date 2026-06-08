package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TelaSecaoQuestoesMat extends AppCompatActivity {

    ImageButton btnVoltar;
    LinearLayout lnlGeometriaPlana, lnlGeometriaEspacial, lnlGrandezas,
            lnlEstatistica, lnlProbabilidade, lnlFuncoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_questoes_mat);

        btnVoltar           = findViewById(R.id.btnVoltar);
        lnlGeometriaPlana   = findViewById(R.id.lnlGeometriaPlana);
        lnlGeometriaEspacial= findViewById(R.id.lnlGeometriaEspacial);
        lnlGrandezas        = findViewById(R.id.lnlGrandezas);
        lnlEstatistica      = findViewById(R.id.lnlEstatistica);
        lnlProbabilidade    = findViewById(R.id.lnlProbabilidade);
        lnlFuncoes          = findViewById(R.id.lnlFuncoes);

        btnVoltar.setOnClickListener(v -> finish());

        lnlGeometriaPlana.setOnClickListener(v -> abrirQuestoes("geometria_plana"));
        lnlGeometriaEspacial.setOnClickListener(v -> abrirQuestoes("geometria_espacial"));
        lnlGrandezas.setOnClickListener(v -> abrirQuestoes("grandezas_medidas"));
        lnlEstatistica.setOnClickListener(v -> abrirQuestoes("estatistica"));
        lnlProbabilidade.setOnClickListener(v -> abrirQuestoes("probabilidade"));
        lnlFuncoes.setOnClickListener(v -> abrirQuestoes("funcoes"));
    }

    private void abrirQuestoes(String conteudoId) {
        Intent intent = new Intent(this, TelaQuestoes.class);
        intent.putExtra("conteudoId", conteudoId);
        startActivity(intent);
    }
}