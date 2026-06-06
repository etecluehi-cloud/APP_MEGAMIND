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

public class TelaSecaoConteudoMat extends AppCompatActivity {

    FirebaseFirestore db;
    String userId;

    ImageButton btnVoltar;

    // CARDS
    CardView cardMatBasica,
            cardPorcentagem,
            cardAlgebra,
            cardFuncoes,
            cardGeometriaPlana,
            cardGeometriaEspacial,
            cardGrandezasMedidas,
            cardEstatistica,
            cardProbabilidade,
            cardMatFinanceira;

    // PROGRESSOS
    ProgressBar progMatBasica,
            progPorcentagem,
            progAlgebra,
            progFuncoes,
            progGeoPlana,
            progGeoEsp,
            progGrandezas,
            progEstatistica,
            progProbabilidade,
            progMatFinanceira;

    // TEXTOS PROGRESSO
    TextView txtProgMatBasica,
            txtProgPorcentagem,
            txtProgAlgebra,
            txtProgFuncoes,
            txtProgGeoPlana,
            txtProgGeoEsp,
            txtProgGrandezas,
            txtProgEstatistica,
            txtProgProbabilidade,
            txtProgMatFinanceira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_mat);

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
        cardMatBasica = findViewById(R.id.cardMatBasica);
        cardPorcentagem = findViewById(R.id.cardPorcentagem);
        cardAlgebra = findViewById(R.id.cardAlgebra);
        cardFuncoes = findViewById(R.id.cardFuncoes);
        cardGeometriaPlana = findViewById(R.id.cardGeometriaPlana);
        cardGeometriaEspacial = findViewById(R.id.cardGeometriaEspacial);
        cardGrandezasMedidas = findViewById(R.id.cardGrandezasMedidas);
        cardEstatistica = findViewById(R.id.cardEstatistica);
        cardProbabilidade = findViewById(R.id.cardProbabilidade);
        cardMatFinanceira = findViewById(R.id.cardMatFinanceira);

        // PROGRESS BARS
        progMatBasica = findViewById(R.id.progMatBasica);
        progPorcentagem = findViewById(R.id.progPorcentagem);
        progAlgebra = findViewById(R.id.progAlgebra);
        progFuncoes = findViewById(R.id.progFuncoes);
        progGeoPlana = findViewById(R.id.progGeoPlana);
        progGeoEsp = findViewById(R.id.progGeoEsp);
        progGrandezas = findViewById(R.id.progGrandezas);
        progEstatistica = findViewById(R.id.progEstatistica);
        progProbabilidade = findViewById(R.id.progProbabilidade);
        progMatFinanceira = findViewById(R.id.progMatFinanceira);

        // TEXTOS
        txtProgMatBasica = findViewById(R.id.txtProgMatBasica);
        txtProgPorcentagem = findViewById(R.id.txtProgPorcentagem);
        txtProgAlgebra = findViewById(R.id.txtProgAlgebra);
        txtProgFuncoes = findViewById(R.id.txtProgFuncoes);
        txtProgGeoPlana = findViewById(R.id.txtProgGeoPlana);
        txtProgGeoEsp = findViewById(R.id.txtProgGeoEsp);
        txtProgGrandezas = findViewById(R.id.txtProgGrandezas);
        txtProgEstatistica = findViewById(R.id.txtProgEstatistica);
        txtProgProbabilidade = findViewById(R.id.txtProgProbabilidade);
        txtProgMatFinanceira = findViewById(R.id.txtProgMatFinanceira);

        // EVENTOS
        abrirConteudo(cardMatBasica, "matematica_basica");
        abrirConteudo(cardPorcentagem, "porcentagem");
        abrirConteudo(cardAlgebra, "algebra");
        abrirConteudo(cardFuncoes, "funcoes");
        abrirConteudo(cardGeometriaPlana, "geometria_plana");
        abrirConteudo(cardGeometriaEspacial, "geometria_espacial");
        abrirConteudo(cardGrandezasMedidas, "unidades_medida_velocidade_escala");
        abrirConteudo(cardEstatistica, "estatistica");
        abrirConteudo(cardProbabilidade, "probabilidade");
        abrirConteudo(cardMatFinanceira, "matematica_financeira");

        btnVoltar.setOnClickListener(v -> finish());

        carregarProgresso();
    }

    // Atualizado

    @Override
    protected void onResume() {
        super.onResume();
        carregarProgresso();
    }

    private void abrirConteudo(CardView card, String conteudoId) {

        card.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TelaSecaoConteudoMat.this,
                            DetalhesVideos.class
                    );

            intent.putExtra("conteudoId", conteudoId);

            startActivity(intent);
        });
    }

    private void carregarProgresso() {

        carregarCard(
                "matematica_basica",
                progMatBasica,
                txtProgMatBasica
        );

        carregarCard(
                "porcentagem",
                progPorcentagem,
                txtProgPorcentagem
        );

        carregarCard(
                "algebra",
                progAlgebra,
                txtProgAlgebra
        );

        carregarCard(
                "funcoes",
                progFuncoes,
                txtProgFuncoes
        );

        carregarCard(
                "geometria_plana",
                progGeoPlana,
                txtProgGeoPlana
        );

        carregarCard(
                "geometria_espacial",
                progGeoEsp,
                txtProgGeoEsp
        );

        carregarCard(
                "unidades_medida_velocidade_escala",
                progGrandezas,
                txtProgGrandezas
        );

        carregarCard(
                "estatistica",
                progEstatistica,
                txtProgEstatistica
        );

        carregarCard(
                "probabilidade",
                progProbabilidade,
                txtProgProbabilidade
        );

        carregarCard(
                "matematica_financeira",
                progMatFinanceira,
                txtProgMatFinanceira
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

                    // compatibilidade antiga
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