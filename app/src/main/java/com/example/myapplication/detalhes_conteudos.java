package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class detalhes_conteudos extends AppCompatActivity {

    FirebaseFirestore db;
    String conteudoId, userId;

    TextView txtTituloDetalhe, txtResumo;
    CheckBox checkVisto;
    Button btnQuestoes;
    LinearLayout layoutVideoSimulado;
    ImageView imgYoutubeLogo;
    String videoUrl = ""; // Variável para armazenar a URL do vídeo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_conteudos);

        txtTituloDetalhe = findViewById(R.id.txtTituloDetalhe);
        txtResumo = findViewById(R.id.txtResumo);
        checkVisto = findViewById(R.id.checkVisto);
        btnQuestoes = findViewById(R.id.btnQuestoes);
        layoutVideoSimulado = findViewById(R.id.layoutVideoSimulado);
        imgYoutubeLogo = findViewById(R.id.imgYoutubeLogo);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        conteudoId = getIntent().getStringExtra("conteudoId");

        android.util.Log.d("DETALHE", "conteudoId: " + conteudoId);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        carregarConteudo();

        // Usuario pode marcar e desmarcar checkbox
        checkVisto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnQuestoes.setEnabled(isChecked);
            salvarProgresso(isChecked);
        });

        btnQuestoes.setOnClickListener(view -> {
            Intent it = new Intent(detalhes_conteudos.this, TelaSecaoQuestoesMat.class);
            it.putExtra("conteudoId", conteudoId);
            startActivity(it);
        });
    }

    private void carregarConteudo() {
        db.collection("conteudo")
                .document(conteudoId)
                .get()
                .addOnSuccessListener(document -> {
                    android.util.Log.d("DETALHE", "documento existe: " + document.exists());
                    android.util.Log.d("DETALHE", "titulo: " + document.getString("titulo"));
                    android.util.Log.d("DETALHE", "resumo: " + document.getString("resumo"));
                    android.util.Log.d("DETALHE", "videoId: " + document.getString("videoId"));

                    if (document.exists()) {
                        txtTituloDetalhe.setText(document.getString("titulo"));
                        txtResumo.setText(document.getString("resumo"));

                        // Pega o videoId e cria a URL completa
                        String videoId = document.getString("videoId");
                        if (videoId != null && !videoId.isEmpty()) {
                            videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                            configurarPlayerSimulado(); // Configura o player com fundo preto
                        }

                        verificarProgresso();
                    }
                })
                .addOnFailureListener(e -> {
                    txtResumo.setText("Erro ao carregar conteúdo");
                });
    }

    /**
     * Configura o player simulado (fundo preto + logo do YouTube)
     * Ambos os elementos abrem o vídeo no YouTube quando clicados
     */
    private void configurarPlayerSimulado() {
        // Torna o layout visível (caso esteja GONE no XML)
        layoutVideoSimulado.setVisibility(View.VISIBLE);

        // Listener para abrir o YouTube
        View.OnClickListener abrirYouTube = v -> {
            if (videoUrl != null && !videoUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                startActivity(intent);
            }
        };

        // Aplica o listener tanto no fundo preto quanto no logo
        layoutVideoSimulado.setOnClickListener(abrirYouTube);
        imgYoutubeLogo.setOnClickListener(abrirYouTube);
    }

    private void verificarProgresso() {
        String progressoId = userId + "_" + conteudoId;
        db.collection("progresso")
                .document(progressoId)
                .get()
                .addOnSuccessListener(document -> {
                    boolean visto = document.exists()
                            && Boolean.TRUE.equals(document.getBoolean("visto"));
                    checkVisto.setChecked(visto);
                    btnQuestoes.setEnabled(visto);
                });
    }

    private void salvarProgresso(boolean visto) {
        String progressoId = userId + "_" + conteudoId;
        Map<String, Object> dados = new HashMap<>();
        dados.put("visto", visto);
        dados.put("userId", userId);
        dados.put("conteudoId", conteudoId);

        db.collection("progresso")
                .document(progressoId)
                .set(dados);
    }
}