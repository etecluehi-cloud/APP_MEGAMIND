package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TelaSecaoConteudoRed extends AppCompatActivity {

    private ImageButton btnVoltar;

    private CardView cardEstruturaTexto;
    private CardView cardTipologiaTextual;
    private CardView cardCompetencias;
    private CardView cardArgumentacao;
    private CardView cardPropostaIntervencao;
    private CardView cardTemasSociais;
    private CardView cardErrosZeram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_red);

        btnVoltar = findViewById(R.id.btnVoltar);

        cardEstruturaTexto = findViewById(R.id.cardEstruturaTexto);
        cardTipologiaTextual = findViewById(R.id.cardTipologiaTextual);
        cardCompetencias = findViewById(R.id.cardCompetencias);
        cardArgumentacao = findViewById(R.id.cardArgumentacao);
        cardPropostaIntervencao = findViewById(R.id.cardPropostaIntervencao);
        cardTemasSociais = findViewById(R.id.cardTemasSociais);
        cardErrosZeram = findViewById(R.id.cardErrosZeram);

        btnVoltar.setOnClickListener(v -> finish());

        abrirConteudo(cardEstruturaTexto, "estrutura_texto");
        abrirConteudo(cardTipologiaTextual, "tipologia_textual");
        abrirConteudo(cardCompetencias, "competencias_enem");
        abrirConteudo(cardArgumentacao, "argumentacao");
        abrirConteudo(cardPropostaIntervencao, "proposta_intervencao");
        abrirConteudo(cardTemasSociais, "temas_sociais");
        abrirConteudo(cardErrosZeram, "erros_zeram");
    }

    private void abrirConteudo(CardView card, String conteudoId) {

        card.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TelaSecaoConteudoRed.this,
                    DetalhesVideos.class
            );

            intent.putExtra("conteudoId", conteudoId);

            startActivity(intent);

        });
    }
}
