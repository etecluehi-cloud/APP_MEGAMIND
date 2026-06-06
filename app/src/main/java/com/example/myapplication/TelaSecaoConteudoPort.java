package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaSecaoConteudoPort extends AppCompatActivity {

    FirebaseFirestore db;
    String userId;

    ImageButton btnVoltar;

    // CARDS
    CardView cardLeituraInterpretacao,
            cardGenerosTextuais,
            cardVariacaoLinguistica,
            cardGramatica,
            cardCoesaoCoerencia,
            cardFigurasLinguagem,
            cardLiteratura;

    // PROGRESSOS
    ProgressBar progLeituraInterpretacao,
            progGenerosTextuais,
            progVariacaoLinguistica,
            progGramatica,
            progCoesaoCoerencia,
            progFigurasLinguagem,
            progLiteratura;

    // TEXTOS
    TextView txtProgLeituraInterpretacao,
            txtProgGenerosTextuais,
            txtProgVariacaoLinguistica,
            txtProgGramatica,
            txtProgCoesaoCoerencia,
            txtProgFigurasLinguagem,
            txtProgLiteratura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_port);

        db = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            finish();
            return;
        }

        // BOTÃO
        btnVoltar = findViewById(R.id.btnVoltar);

        // CARDS
        cardLeituraInterpretacao =
                findViewById(R.id.cardLeituraInterpretacao);

        cardGenerosTextuais =
                findViewById(R.id.cardGenerosTextuais);

        cardVariacaoLinguistica =
                findViewById(R.id.cardVariacaoLinguistica);

        cardGramatica =
                findViewById(R.id.cardGramatica);

        cardCoesaoCoerencia =
                findViewById(R.id.cardCoesaoCoerencia);

        cardFigurasLinguagem =
                findViewById(R.id.cardFigurasLinguagem);

        cardLiteratura =
                findViewById(R.id.cardLiteratura);

        // PROGRESS BARS
        progLeituraInterpretacao =
                findViewById(R.id.progLeituraInterpretacao);

        progGenerosTextuais =
                findViewById(R.id.progGenerosTextuais);

        progVariacaoLinguistica =
                findViewById(R.id.progVariacaoLinguistica);

        progGramatica =
                findViewById(R.id.progGramatica);

        progCoesaoCoerencia =
                findViewById(R.id.progCoesaoCoerencia);

        progFigurasLinguagem =
                findViewById(R.id.progFigurasLinguagem);

        progLiteratura =
                findViewById(R.id.progLiteratura);

        // TEXTOS
        txtProgLeituraInterpretacao =
                findViewById(R.id.txtProgLeituraInterpretacao);

        txtProgGenerosTextuais =
                findViewById(R.id.txtProgGenerosTextuais);

        txtProgVariacaoLinguistica =
                findViewById(R.id.txtProgVariacaoLinguistica);

        txtProgGramatica =
                findViewById(R.id.txtProgGramatica);

        txtProgCoesaoCoerencia =
                findViewById(R.id.txtProgCoesaoCoerencia);

        txtProgFigurasLinguagem =
                findViewById(R.id.txtProgFigurasLinguagem);

        txtProgLiteratura =
                findViewById(R.id.txtProgLiteratura);

        // EVENTOS
        abrirConteudo(
                cardLeituraInterpretacao,
                "leitura_interpretacao"
        );

        abrirConteudo(
                cardGenerosTextuais,
                "generos_textuais"
        );

        abrirConteudo(
                cardVariacaoLinguistica,
                "variacao_linguistica"
        );

        abrirConteudo(
                cardGramatica,
                "gramatica"
        );

        abrirConteudo(
                cardCoesaoCoerencia,
                "coesao_coerencia"
        );

        abrirConteudo(
                cardFigurasLinguagem,
                "figuras_linguagem"
        );

        abrirConteudo(
                cardLiteratura,
                "literatura"
        );

        btnVoltar.setOnClickListener(v -> finish());

        carregarProgresso();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProgresso();
    }

    private void abrirConteudo(
            CardView card,
            String conteudoId
    ) {

        card.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TelaSecaoConteudoPort.this,
                            DetalhesVideos.class
                    );




            intent.putExtra(
                    "conteudoId",
                    conteudoId
            );

            startActivity(intent);
        });
    }

    private void carregarProgresso() {

        carregarCard(
                "leitura_interpretacao",
                progLeituraInterpretacao,
                txtProgLeituraInterpretacao
        );

        carregarCard(
                "generos_textuais",
                progGenerosTextuais,
                txtProgGenerosTextuais
        );

        carregarCard(
                "variacao_linguistica",
                progVariacaoLinguistica,
                txtProgVariacaoLinguistica
        );

        carregarCard(
                "gramatica",
                progGramatica,
                txtProgGramatica
        );

        carregarCard(
                "coesao_coerencia",
                progCoesaoCoerencia,
                txtProgCoesaoCoerencia
        );

        carregarCard(
                "figuras_linguagem",
                progFigurasLinguagem,
                txtProgFigurasLinguagem
        );

        carregarCard(
                "literatura",
                progLiteratura,
                txtProgLiteratura
        );
    }

    private void carregarCard(
            String conteudoId,
            ProgressBar progressBar,
            TextView txtProgresso
    ) {

        db.collection("conteudo")
                .document(conteudoId)
                .get()
                .addOnSuccessListener(conteudoDoc -> {

                    if (!conteudoDoc.exists()) return;

                    String video1 = "";

                    if (conteudoDoc.getString("videoId1") != null &&
                            !conteudoDoc.getString("videoId1").isEmpty()) {

                        video1 =
                                conteudoDoc.getString("videoId1");

                    } else if (conteudoDoc.getString("videoId") != null &&
                            !conteudoDoc.getString("videoId").isEmpty()) {

                        video1 =
                                conteudoDoc.getString("videoId");
                    }

                    String video2 =
                            conteudoDoc.getString("videoId2") != null
                                    ? conteudoDoc.getString("videoId2")
                                    : "";

                    String video3 =
                            conteudoDoc.getString("videoId3") != null
                                    ? conteudoDoc.getString("videoId3")
                                    : "";

                    int total = 0;

                    if (!video1.isEmpty()) total++;
                    if (!video2.isEmpty()) total++;
                    if (!video3.isEmpty()) total++;

                    final int totalVideos = total;

                    db.collection("progresso_videos")
                            .document(userId)
                            .collection(conteudoId)
                            .get()
                            .addOnSuccessListener(query -> {

                                int vistos = 0;

                                for (com.google.firebase.firestore.DocumentSnapshot doc
                                        : query.getDocuments()) {

                                    boolean visto =
                                            Boolean.TRUE.equals(
                                                    doc.getBoolean("visto")
                                            );

                                    if (visto) {
                                        vistos++;
                                    }
                                }

                                int porcentagem =
                                        totalVideos > 0
                                                ? (vistos * 100) / totalVideos
                                                : 0;

                                progressBar.setProgress(porcentagem);

                                txtProgresso.setText(
                                        vistos + " de " + totalVideos + " vídeos"
                                );
                            });
                });
    }
}