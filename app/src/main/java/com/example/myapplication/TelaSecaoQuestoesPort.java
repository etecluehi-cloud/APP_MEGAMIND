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

        // Interpretação de Texto
        cardInterpretacaoTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "leitura_interpretacao");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Figuras de Linguagem
        cardFigurasLinguagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "figuras_linguagem");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Funções da Linguagem
        cardFuncoesLinguagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "funcoes_linguagem");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Variação Linguística
        cardVariacaoLinguistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "variacao_linguistica");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Gêneros Textuais
        cardGenerosTextuais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "generos_textuais");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Linguagem Verbal e Não Verbal
        cardLinguagemVerbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "linguagem_verbal_nao_verbal");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Intertextualidade
        cardIntertextualidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "intertextualidade");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Denotação e Conotação
        cardDenotacaoConotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "denotacao_conotacao");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Literatura
        cardLiteratura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "literatura");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });

        // Gramática
        cardGramatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TelaSecaoQuestoesPort.this, TelaQuestoes.class);
                it.putExtra("conteudo_id", "gramatica");
                it.putExtra("colecao", "questoes_portugues");
                startActivity(it);
            }
        });
    }
}