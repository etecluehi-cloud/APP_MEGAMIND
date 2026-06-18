package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gamificacao extends AppCompatActivity {

    TextView txtNomeU, txtNivel, txtXpLabel, txtDiasSeguidos;
    ProgressBar progressXP;
    LinearLayout layoutRanking;
    CardView cardBlocoMat, cardBlocoPort;
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;

    FirebaseAuth mAuth;
    FirebaseFirestore db;


    private static final String[] TOPICOS_MAT = {
            "geometria_plana", "geometria_espacial", "funcoes", "estatistica",
            "probabilidade", "analise_combinatoria", "razao_proporcao",
            "regra_de_tres", "porcentagem", "matematica_financeira",
            "trigonometria", "matrizes", "sistemas_lineares"
    };
    private static final String[] TOPICOS_PORT = {
            "leitura_interpretacao", "figuras_linguagem", "funcoes_linguagem",
            "variacao_linguistica", "generos_textuais", "linguagem_verbal_nao_verbal",
            "intertextualidade", "denotacao_conotacao", "literatura", "gramatica"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamificacao);

        txtNomeU        = findViewById(R.id.txtNomeU);
        txtNivel        = findViewById(R.id.txtNivel);
        txtXpLabel      = findViewById(R.id.txtXpLabel);
        txtDiasSeguidos = findViewById(R.id.txtDiasSeguidos);
        progressXP      = findViewById(R.id.progressXP);
        layoutRanking   = findViewById(R.id.layoutRanking);
        cardBlocoMat    = findViewById(R.id.cardBlocoMat);
        cardBlocoPort   = findViewById(R.id.cardBlocoPort);
        btnHome         = findViewById(R.id.btnHome);
        btnDesempenho   = findViewById(R.id.btnDesempenho);
        btnBuscar       = findViewById(R.id.btnBuscar);
        btnPerfil       = findViewById(R.id.btnPerfil);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        // ── Alerta ao voltar da TelaQuestoes ────────────────────────────────
        if (getIntent().getBooleanExtra("bloco_concluido", false)) {
            int acertos = getIntent().getIntExtra("acertos_bloco", 0);
            boolean ganhouXP = acertos >= 3;
            String titulo = ganhouXP ? "🏆 Parabéns!" : "😅 Quase lá!";
            String msg = ganhouXP
                    ? "Você acertou " + acertos + "/5 e ganhou +50 XP! 🔥 Sequência atualizada!"
                    : "Você acertou " + acertos + "/5. Precisa de ao menos 3 acertos para ganhar XP. Tente novamente!";
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(titulo)
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .show();
        }

        // ── Cards de bloco ───────────────────────────────────────────────────
        cardBlocoMat.setOnClickListener(v -> iniciarBloco("questoes_matematica", TOPICOS_MAT));
        cardBlocoPort.setOnClickListener(v -> iniciarBloco("questoes_portugues", TOPICOS_PORT));

        // ── Rodapé ───────────────────────────────────────────────────────────
        btnHome.setOnClickListener(v -> { startActivity(new Intent(this, Home.class)); finish(); });
        btnPerfil.setOnClickListener(v -> startActivity(new Intent(this, PerfilUsuario.class)));

        carregarDadosUsuario();
        carregarRanking();

        //evento dos botoes do footer
        btnDesempenho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Gamificacao.this, Desempenho.class);
                startActivity(it);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Gamificacao.this, PerfilUsuario.class);
                startActivity(it);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Gamificacao.this, Home.class);
                startActivity(it);
            }
        });
    }

    private void carregarDadosUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        DocumentReference ref = db.collection("usuarios").document(user.getUid());
        ref.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) return;

            String nome = doc.getString("nome");
            if (nome != null) txtNomeU.setText(nome);

            Long pontos = doc.getLong("pontos");
            if (pontos == null) {
                // Primeira vez: cria campos de gamificação sem apagar os existentes
                Map<String, Object> init = new HashMap<>();
                init.put("pontos", 0L);
                init.put("streak", 0L);
                init.put("ultimoBlocoData", "");
                ref.update(init);
                pontos = 0L;
            }
            long pts = pontos;

            Long streak = doc.getLong("streak");
            if (streak == null) streak = 0L;
            txtDiasSeguidos.setText(streak + " dias seguidos");

            // Nível: 100 XP por nível
            int nivel   = (int) (pts / 100) + 1;
            int xpAtual = (int) (pts % 100);
            txtNivel.setText("Nível " + nivel);
            txtXpLabel.setText("· " + xpAtual + " / 100 XP");
            progressXP.setMax(100);
            progressXP.setProgress(xpAtual);
            atualizarRankingUsuario(user.getUid(), nome != null ? nome : "Sem nome", pts);
        });
    }

    private void atualizarRankingUsuario(String userId, String nome, long pontos) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", nome);
        dados.put("pontos", pontos);

        db.collection("ranking")
                .document(userId)
                .set(dados, SetOptions.merge());
    }
    private void carregarRanking() {
        FirebaseUser eu = mAuth.getCurrentUser();

        db.collection("ranking")
                .orderBy("pontos", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    layoutRanking.removeAllViews();
                    String[] medalhas = {"1", "2", "3"};

                    List<QueryDocumentSnapshot> usuarios = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        usuarios.add(doc);
                    }

                    int pos = 1;
                    for (QueryDocumentSnapshot doc : usuarios) {
                        if (pos > 10) break;

                        String uid = doc.getId();
                        String nome = doc.getString("nome");
                        Long pontos = doc.getLong("pontos");
                        boolean souEu = eu != null && eu.getUid().equals(uid);
                        String medal = pos <= 3 ? medalhas[pos - 1] : String.valueOf(pos);

                        adicionarLinhaRanking(
                                medal,
                                nome != null ? nome : "Sem nome",
                                pontos != null ? pontos : 0L,
                                souEu
                        );
                        pos++;
                    }

                    if (usuarios.isEmpty()) {
                        TextView tv = new TextView(this);
                        tv.setText("Nenhum dado ainda. Faca o primeiro bloco!");
                        tv.setTextColor(Color.parseColor("#AAAAAA"));
                        tv.setPadding(dp(12), dp(12), dp(12), dp(12));
                        layoutRanking.addView(tv);
                    }
                })
                .addOnFailureListener(e -> carregarRankingUsuarioAtual());
    }

    private void carregarRankingUsuarioAtual() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        db.collection("ranking")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    layoutRanking.removeAllViews();
                    String nome = doc.getString("nome");
                    Long pontos = doc.getLong("pontos");
                    adicionarLinhaRanking(
                            "1",
                            nome != null ? nome : "Voce",
                            pontos != null ? pontos : 0L,
                            true
                    );
                })
                .addOnFailureListener(e -> {
                    layoutRanking.removeAllViews();
                    TextView tv = new TextView(this);
                    tv.setText("Nao foi possivel carregar o ranking agora.");
                    tv.setTextColor(Color.parseColor("#F59E0B"));
                    tv.setPadding(dp(12), dp(12), dp(12), dp(12));
                    layoutRanking.addView(tv);
                });
    }
    private void adicionarLinhaRanking(String posStr, String nome, long pts, boolean souEu) {
        LinearLayout linha = new LinearLayout(this);
        linha.setOrientation(LinearLayout.HORIZONTAL);
        linha.setGravity(Gravity.CENTER_VERTICAL);
        linha.setPadding(dp(12), dp(10), dp(12), dp(10));
        if (souEu) linha.setBackgroundResource(R.drawable.bg_ranking_destaque);

        TextView tvPos = new TextView(this);
        tvPos.setText(posStr);
        tvPos.setTextSize(14);
        tvPos.setTextColor(getResources().getColor(R.color.texto_ranking));
        tvPos.setMinWidth(dp(40));
        linha.addView(tvPos);

        TextView tvNome = new TextView(this);
        tvNome.setText(souEu ? nome + " (você)" : nome);
        tvNome.setTextSize(14);
        tvNome.setTextColor(souEu
                ? Color.parseColor("#7C3AED")
                : getResources().getColor(R.color.texto_ranking));
        if (souEu) tvNome.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvNome.setLayoutParams(lp);
        linha.addView(tvNome);

        TextView tvPts = new TextView(this);
        tvPts.setText(pts + " xp");
        tvPts.setTextSize(12);
        tvPts.setTextColor(getResources().getColor(R.color.texto_ranking));
        linha.addView(tvPts);

        layoutRanking.addView(linha);

        View div = new View(this);
        div.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        div.setBackgroundColor(Color.parseColor("#F0F0F0"));
        layoutRanking.addView(div);
    }

    private void iniciarBloco(String colecao, String[] topicos) {
        int idx = (int) (Math.random() * topicos.length);
        Intent it = new Intent(this, TelaQuestoes.class);
        it.putExtra("conteudoId", topicos[idx]);
        it.putExtra("colecao", colecao);
        it.putExtra("bloco_gamificacao", true);
        startActivity(it);
    }

    private int dp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
