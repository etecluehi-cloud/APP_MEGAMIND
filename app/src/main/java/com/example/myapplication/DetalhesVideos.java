package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetalhesVideos extends AppCompatActivity {

    TextView txtTituloConteudo, txtResumo, txtProgressoVideos;
    TextView txtTituloVideo1, txtTituloVideo2, txtTituloVideo3;
    LinearLayout lnlVideo1, lnlVideo2, lnlVideo3;
    CheckBox checkVideo1, checkVideo2, checkVideo3;
    ProgressBar progressVideos;
    Button btnQuestoes;

    FirebaseFirestore db;
    String userId, conteudoId;
    String videoId = "", videoId2 = "", videoId3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_videos);

        db       = FirebaseFirestore.getInstance();
        userId   = FirebaseAuth.getInstance().getCurrentUser().getUid();
        conteudoId = getIntent().getStringExtra("conteudoId");

        txtTituloConteudo  = findViewById(R.id.txtTituloConteudo);
        txtResumo          = findViewById(R.id.txtResumo);
        txtProgressoVideos = findViewById(R.id.txtProgressoVideos);
        txtTituloVideo1    = findViewById(R.id.txtTituloVideo1);
        txtTituloVideo2    = findViewById(R.id.txtTituloVideo2);
        txtTituloVideo3    = findViewById(R.id.txtTituloVideo3);
        lnlVideo1          = findViewById(R.id.lnlVideo1);
        lnlVideo2          = findViewById(R.id.lnlVideo2);
        lnlVideo3          = findViewById(R.id.lnlVideo3);
        checkVideo1        = findViewById(R.id.checkVideo1);
        checkVideo2        = findViewById(R.id.checkVideo2);
        checkVideo3        = findViewById(R.id.checkVideo3);
        progressVideos     = findViewById(R.id.progressVideos);
        btnQuestoes        = findViewById(R.id.btnQuestoes);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        carregarConteudo();

        // Checkboxes manuais
        checkVideo1.setOnCheckedChangeListener((b, checked) -> {
            salvarProgresso("video1", checked);
            atualizarProgresso();
        });
        checkVideo2.setOnCheckedChangeListener((b, checked) -> {
            salvarProgresso("video2", checked);
            atualizarProgresso();
        });
        checkVideo3.setOnCheckedChangeListener((b, checked) -> {
            salvarProgresso("video3", checked);
            atualizarProgresso();
        });

        // Clicar no vídeo abre no YouTube e marca como visto
        lnlVideo1.setOnClickListener(v -> {
            abrirYoutube(videoId);
            checkVideo1.setChecked(true);
        });
        lnlVideo2.setOnClickListener(v -> {
            abrirYoutube(videoId2);
            checkVideo2.setChecked(true);
        });
        lnlVideo3.setOnClickListener(v -> {
            abrirYoutube(videoId3);
            checkVideo3.setChecked(true);
        });

        // Botão questões
        btnQuestoes.setOnClickListener(v -> {
            Intent it = new Intent(this, TelaSecaoQuestoesMat.class);
            it.putExtra("conteudoId", conteudoId);
            startActivity(it);
        });
    }

    private void carregarConteudo() {
        db.collection("conteudo")
                .document(conteudoId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        txtTituloConteudo.setText(doc.getString("titulo"));
                        txtResumo.setText(doc.getString("resumo"));

                        videoId = doc.getString("videoId") != null ? doc.getString("videoId") : "";
                        videoId2 = doc.getString("videoId2") != null ? doc.getString("videoId2") : "";
                        videoId3 = doc.getString("videoId3") != null ? doc.getString("videoId3") : "";

                        // Oculta vídeos que não têm ID cadastrado
                        lnlVideo1.setVisibility(videoId.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
                        lnlVideo2.setVisibility(videoId2.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
                        lnlVideo3.setVisibility(videoId3.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);

                        carregarProgresso();
                    }
                });
    }

    private void carregarProgresso() {
        db.collection("progresso_videos")
                .document(userId)
                .collection(conteudoId)
                .get()
                .addOnSuccessListener(query -> {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : query.getDocuments()) {
                        boolean visto = Boolean.TRUE.equals(doc.getBoolean("visto"));
                        switch (doc.getId()) {
                            case "video1": checkVideo1.setChecked(visto); break;
                            case "video2": checkVideo2.setChecked(visto); break;
                            case "video3": checkVideo3.setChecked(visto); break;
                        }
                    }
                    atualizarProgresso();
                });
    }

    private void salvarProgresso(String videoKey, boolean visto) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("visto", visto);

        db.collection("progresso_videos")
                .document(userId)
                .collection(conteudoId)
                .document(videoKey)
                .set(dados);
    }

    private void atualizarProgresso() {
        int total    = 0;
        int vistos   = 0;

        if (!videoId.isEmpty()) { total++; if (checkVideo1.isChecked()) vistos++; }
        if (!videoId2.isEmpty()) { total++; if (checkVideo2.isChecked()) vistos++; }
        if (!videoId3.isEmpty()) { total++; if (checkVideo3.isChecked()) vistos++; }

        int porcentagem = total > 0 ? (vistos * 100) / total : 0;
        progressVideos.setProgress(porcentagem);
        txtProgressoVideos.setText(vistos + " de " + total);
        btnQuestoes.setEnabled(vistos == total && total > 0);
    }

    private void abrirYoutube(String videoId) {
        if (videoId == null || videoId.isEmpty()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        startActivity(intent);
    }
}