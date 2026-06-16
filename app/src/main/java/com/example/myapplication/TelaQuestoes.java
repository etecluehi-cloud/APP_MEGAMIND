package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TelaQuestoes extends AppCompatActivity {

    TextView txtTituloConteudo, txtContador, txtEnunciado, txtFeedback;
    Button btnA, btnB, btnC, btnD, btnE, btnProxima;
    ImageButton btnVoltar;

    FirebaseFirestore db;
    List<Questao> listaQuestoes = new ArrayList<>();
    int indiceAtual  = 0;
    boolean jaRespondeu = false;

    int totalAcertos = 0;
    int totalErros   = 0;

    // ── Gamificação ──────────────────────────────────────────────────────────
    private boolean modoBloco           = false;
    private int     questoesRespondidas = 0;
    private String  colecao             = "questoes_matematica";
    // ────────────────────────────────────────────────────────────────────────

    private static final int MAX_QUESTOES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_questoes);

        db = FirebaseFirestore.getInstance();

        txtTituloConteudo = findViewById(R.id.txtTituloConteudo);
        txtContador       = findViewById(R.id.txtContador);
        txtEnunciado      = findViewById(R.id.txtEnunciado);
        txtFeedback       = findViewById(R.id.txtFeedback);
        btnA              = findViewById(R.id.btnA);
        btnB              = findViewById(R.id.btnB);
        btnC              = findViewById(R.id.btnC);
        btnD              = findViewById(R.id.btnD);
        btnE              = findViewById(R.id.btnE);
        btnProxima        = findViewById(R.id.btnProxima);
        btnVoltar         = findViewById(R.id.btnVoltar);

        String conteudoId = getIntent().getStringExtra("conteudoId");
        if (conteudoId == null || conteudoId.isEmpty()) {
            conteudoId = getIntent().getStringExtra("conteudo_id");
        }

        // ── Detecta modo bloco ───────────────────────────────────────────────
        modoBloco = getIntent().getBooleanExtra("bloco_gamificacao", false);
        String colecaoExtra = getIntent().getStringExtra("colecao");
        if (colecaoExtra != null && !colecaoExtra.isEmpty()) colecao = colecaoExtra;

        if (conteudoId == null || conteudoId.isEmpty()) {
            Toast.makeText(this, "Erro: conteúdo não identificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnVoltar.setOnClickListener(view -> finish());
        buscarQuestoesNoFirestore(conteudoId);
    }

    private void buscarQuestoesNoFirestore(String conteudoId) {
        db.collection(colecao)
                .document(conteudoId)
                .get()
                .addOnSuccessListener(documento -> {
                    if (documento.exists()) {
                        String titulo = documento.getString("titulo");
                        txtTituloConteudo.setText(titulo != null ? titulo : conteudoId);

                        List<Map<String, Object>> questoesRaw =
                                (List<Map<String, Object>>) documento.get("questoes");

                        if (questoesRaw != null && !questoesRaw.isEmpty()) {
                            List<Questao> todas = new ArrayList<>();
                            for (Map<String, Object> q : questoesRaw) {
                                todas.add(new Questao(
                                        (String) q.get("enunciado"),
                                        (String) q.get("alternativa_a"),
                                        (String) q.get("alternativa_b"),
                                        (String) q.get("alternativa_c"),
                                        (String) q.get("alternativa_d"),
                                        (String) q.get("alternativa_e"),
                                        (String) q.get("resposta_correta")
                                ));
                            }
                            Collections.shuffle(todas);
                            int qtd = Math.min(MAX_QUESTOES, todas.size());
                            listaQuestoes = new ArrayList<>(todas.subList(0, qtd));
                            exibirQuestao(indiceAtual);
                        } else {
                            Toast.makeText(this, "Nenhuma questão encontrada!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Conteúdo não encontrado no banco.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao buscar: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private static class AlternativaItem {
        String texto;
        boolean correta;
        AlternativaItem(String texto, boolean correta) { this.texto = texto; this.correta = correta; }
    }

    private void exibirQuestao(int indice) {
        jaRespondeu = false;
        Questao q = listaQuestoes.get(indice);

        txtContador.setText("Questão " + (indice + 1) + " de " + listaQuestoes.size());
        txtEnunciado.setText(q.getEnunciado());

        List<AlternativaItem> alts = new ArrayList<>(Arrays.asList(
                new AlternativaItem(q.getAlternativa_a(), "A".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_b(), "B".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_c(), "C".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_d(), "D".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_e(), "E".equals(q.getResposta_correta()))
        ));
        Collections.shuffle(alts);

        List<Button> botoes = Arrays.asList(btnA, btnB, btnC, btnD, btnE);
        String[] letras = {"A", "B", "C", "D", "E"};
        String novaLetraCorreta = "A";
        for (int i = 0; i < alts.size(); i++) {
            botoes.get(i).setText(letras[i] + ") " + alts.get(i).texto);
            if (alts.get(i).correta) novaLetraCorreta = letras[i];
        }

        resetarBotoes();
        txtFeedback.setVisibility(View.GONE);
        btnProxima.setVisibility(View.GONE);

        final String letraCorreta = novaLetraCorreta;
        configurarClique(btnA, "A", letraCorreta);
        configurarClique(btnB, "B", letraCorreta);
        configurarClique(btnC, "C", letraCorreta);
        configurarClique(btnD, "D", letraCorreta);
        configurarClique(btnE, "E", letraCorreta);

        btnProxima.setOnClickListener(view -> {
            if (modoBloco) {
                questoesRespondidas++;
                if (questoesRespondidas >= listaQuestoes.size()) {
                    // Bloco finalizado → salva XP + streak e volta
                    salvarBlocoGamificacao();
                    return;
                }
            }
            if (indiceAtual < listaQuestoes.size() - 1) {
                indiceAtual++;
                exibirQuestao(indiceAtual);
            } else {
                // Fluxo normal (não bloco)
                Intent intent = new Intent(TelaQuestoes.this, TelaResultado.class);
                intent.putExtra("total_acertos",  totalAcertos);
                intent.putExtra("total_erros",    totalErros);
                intent.putExtra("total_questoes", listaQuestoes.size());
                intent.putExtra("titulo",         txtTituloConteudo.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    // ── Salva XP e streak ao terminar o bloco ────────────────────────────────
    // Regras:
    //   • Só ganha XP e streak se acertou >= 3 questões
    //   • Streak só incrementa 1 vez por dia (campo "ultimoBlocoData")
    //   • Se o dia anterior não foi jogado, o streak é zerado
    private void salvarBlocoGamificacao() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) { finish(); return; }

        String hoje = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        boolean acertouMinimo = totalAcertos >= 3;

        db.collection("usuarios").document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    long pontosAtuais = doc.getLong("pontos") != null ? doc.getLong("pontos") : 0L;
                    long streakAtual  = doc.getLong("streak")  != null ? doc.getLong("streak")  : 0L;
                    String ultimoBloco = doc.getString("ultimoBlocoData");
                    if (ultimoBloco == null) ultimoBloco = "";

                    // Calcular ontem para verificar sequência
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.add(java.util.Calendar.DAY_OF_YEAR, -1);
                    String ontem = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());

                    long novosPontos = pontosAtuais;
                    long novoStreak  = streakAtual;

                    if (acertouMinimo) {
                        // +50 XP sempre que acerta o mínimo
                        novosPontos = pontosAtuais + 50;

                        if (hoje.equals(ultimoBloco)) {
                            // Já jogou hoje: não altera o streak
                        } else if (ontem.equals(ultimoBloco) || ultimoBloco.isEmpty()) {
                            // Jogou ontem (ou é o primeiro): incrementa streak
                            novoStreak = streakAtual + 1;
                        } else {
                            // Pulou um dia: reinicia streak
                            novoStreak = 1;
                        }
                    }

                    // Monta o update
                    java.util.Map<String, Object> update = new java.util.HashMap<>();
                    update.put("pontos", novosPontos);
                    update.put("streak", novoStreak);
                    if (acertouMinimo) update.put("ultimoBlocoData", hoje);
                    String nomeUsuario = doc.getString("nome");
                    if (nomeUsuario == null || nomeUsuario.isEmpty()) {
                        nomeUsuario = user.getDisplayName();
                    }
                    if (nomeUsuario == null || nomeUsuario.isEmpty()) {
                        nomeUsuario = "Sem nome";
                    }

                    java.util.Map<String, Object> ranking = new java.util.HashMap<>();
                    ranking.put("nome", nomeUsuario);
                    ranking.put("pontos", novosPontos);

                    db.collection("usuarios").document(user.getUid())
                            .update(update)
                            .addOnCompleteListener(task -> {
                                db.collection("ranking")
                                        .document(user.getUid())
                                        .set(ranking, SetOptions.merge())
                                        .addOnCompleteListener(rankingTask -> {
                                            Intent it = new Intent(TelaQuestoes.this, Gamificacao.class);
                                            it.putExtra("bloco_concluido", true);
                                            it.putExtra("acertos_bloco", totalAcertos);
                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(it);
                                            finish();
                                        });
                            });
                });
    }

    private void configurarClique(Button botao, String letra, String respostaCorreta) {
        botao.setOnClickListener(view -> {
            if (jaRespondeu) return;
            jaRespondeu = true;

            if (letra.equals(respostaCorreta)) {
                botao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                txtFeedback.setTextColor(Color.parseColor("#4CAF50"));
                totalAcertos++;
            } else {
                botao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
                txtFeedback.setTextColor(Color.parseColor("#F44336"));
                totalErros++;
                Button botaoCorreto = getBotaoPorLetra(respostaCorreta);
                if (botaoCorreto != null)
                    botaoCorreto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }

            txtFeedback.setVisibility(View.VISIBLE);
            btnProxima.setVisibility(View.VISIBLE);
        });
    }

    private Button getBotaoPorLetra(String letra) {
        switch (letra) {
            case "A": return btnA; case "B": return btnB; case "C": return btnC;
            case "D": return btnD; case "E": return btnE; default: return null;
        }
    }

    private void resetarBotoes() {
        ColorStateList roxo = ColorStateList.valueOf(Color.parseColor("#7B2FBE"));
        btnA.setBackgroundTintList(roxo); btnB.setBackgroundTintList(roxo);
        btnC.setBackgroundTintList(roxo); btnD.setBackgroundTintList(roxo);
        btnE.setBackgroundTintList(roxo);
    }
}