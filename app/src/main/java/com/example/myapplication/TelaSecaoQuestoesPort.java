package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TelaSecaoQuestoesPort extends AppCompatActivity {

    CardView cardInterpretacaoTexto, cardFigurasLinguagem, cardFuncoesLinguagem,
            cardVariacaoLinguistica, cardGenerosTextuais, cardLinguagemVerbal,
            cardIntertextualidade, cardDenotacaoConotacao, cardLiteratura, cardGramatica;

    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_questoes_port);

        // Linkando os cards
        cardInterpretacaoTexto  = findViewById(R.id.cardInterpretacaoTexto);
        cardFigurasLinguagem    = findViewById(R.id.cardFigurasLinguagem);
        cardFuncoesLinguagem    = findViewById(R.id.cardFuncoesLinguagem);
        cardVariacaoLinguistica = findViewById(R.id.cardVariacaoLinguistica);
        cardGenerosTextuais     = findViewById(R.id.cardGenerosTextuais);
        cardLinguagemVerbal     = findViewById(R.id.cardLinguagemVerbal);
        cardIntertextualidade   = findViewById(R.id.cardIntertextualidade);
        cardDenotacaoConotacao  = findViewById(R.id.cardDenotacaoConotacao);
        cardLiteratura          = findViewById(R.id.cardLiteratura);
        cardGramatica           = findViewById(R.id.cardGramatica);
        btnVoltar               = findViewById(R.id.btnVoltar);

        // Botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Interpretação de Texto → passa "interpretacao_texto"
        cardInterpretacaoTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "interpretacao_texto");
                startActivity(it);
            }
        });

        // Figuras de Linguagem → passa "figuras_linguagem"
        cardFigurasLinguagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "figuras_linguagem");
                startActivity(it);
            }
        });

        // Funções da Linguagem → passa "funcoes_linguagem"
        cardFuncoesLinguagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "funcoes_linguagem");
                startActivity(it);
            }
        });

        // Variação Linguística → passa "variacao_linguistica"
        cardVariacaoLinguistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "variacao_linguistica");
                startActivity(it);
            }
        });

        // Gêneros Textuais → passa "generos_textuais"
        cardGenerosTextuais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "generos_textuais");
                startActivity(it);
            }
        });

        // Linguagem Verbal e Não Verbal → passa "linguagem_verbal"
        cardLinguagemVerbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "linguagem_verbal");
                startActivity(it);
            }
        });

        // Intertextualidade → passa "intertextualidade"
        cardIntertextualidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "intertextualidade");
                startActivity(it);
            }
        });

        // Denotação e Conotação → passa "denotacao_conotacao"
        cardDenotacaoConotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "denotacao_conotacao");
                startActivity(it);
            }
        });

        // Literatura → passa "literatura"
        cardLiteratura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "literatura");
                startActivity(it);
            }
        });

        // Gramática → passa "gramatica"
        cardGramatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "gramatica");
                startActivity(it);
            }
        });
    }
}