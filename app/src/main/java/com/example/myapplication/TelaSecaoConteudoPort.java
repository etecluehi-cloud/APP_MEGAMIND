package com.example.myapplication; // ← troque pelo seu pacote real

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TelaSecaoConteudoPort extends AppCompatActivity {

    ImageButton btnVoltar;
    LinearLayout lnlLeituraInterpretacao, lnlGenerosTextuais, lnlVariacaoLinguistica,
            lnlGramatica, lnlCoesaoCoerencia, lnlFigurasLinguagem, lnlLiteratura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_port);

        // Inicializa todas as views
        btnVoltar                = findViewById(R.id.btnVoltar);
        lnlLeituraInterpretacao  = findViewById(R.id.lnlLeituraInterpretacao);
        lnlGenerosTextuais       = findViewById(R.id.lnlGenerosTextuais);
        lnlVariacaoLinguistica   = findViewById(R.id.lnlVariacaoLinguistica);
        lnlGramatica             = findViewById(R.id.lnlGramatica);
        lnlCoesaoCoerencia       = findViewById(R.id.lnlCoesaoCoerencia);
        lnlFigurasLinguagem      = findViewById(R.id.lnlFigurasLinguagem);
        lnlLiteratura            = findViewById(R.id.lnlLiteratura);

        // Botão voltar
        btnVoltar.setOnClickListener(v -> finish());

        // Listeners de cada card
        lnlLeituraInterpretacao.setOnClickListener(v -> abrirDetalhe("leitura_interpretacao"));
        lnlGenerosTextuais.setOnClickListener(v -> abrirDetalhe("generos_textuais"));
        lnlVariacaoLinguistica.setOnClickListener(v -> abrirDetalhe("variacao_linguistica"));
        lnlGramatica.setOnClickListener(v -> abrirDetalhe("gramatica"));
        lnlCoesaoCoerencia.setOnClickListener(v -> abrirDetalhe("coesao_coerencia"));
        lnlFigurasLinguagem.setOnClickListener(v -> abrirDetalhe("figuras_linguagem"));
        lnlLiteratura.setOnClickListener(v -> abrirDetalhe("literatura"));
    }

    private void abrirDetalhe(String conteudoId) {
        Intent intent = new Intent(this, detalhes_conteudos.class);
        intent.putExtra("conteudoId", conteudoId);
        startActivity(intent);
    }
}