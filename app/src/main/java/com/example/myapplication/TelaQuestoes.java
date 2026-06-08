// TelaQuestoes.java
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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TelaQuestoes extends AppCompatActivity {

    TextView txtTituloConteudo, txtContador, txtEnunciado, txtFeedback;
    Button btnA, btnB, btnC, btnD, btnE, btnProxima;
    ImageButton btnVoltar;

    FirebaseFirestore db;
    List<Questao> listaQuestoes = new ArrayList<>();
    int indiceAtual = 0;
    boolean jaRespondeu = false;

    // ── NOVO: contadores de acerto e erro ──
    int totalAcertos = 0;
    int totalErros   = 0;

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
            Toast.makeText(this, "Erro: conteúdo não identificado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnVoltar.setOnClickListener(view -> finish());

        buscarQuestoesNoFirestore(conteudoId);
    }

    private void buscarQuestoesNoFirestore(String conteudoId) {
        db.collection("questoes_matematica")
                .document(conteudoId)
                .get()
                .addOnSuccessListener(documento -> {

                    if (documento.exists()) {

                        String titulo = documento.getString("titulo");
                        txtTituloConteudo.setText(titulo != null ? titulo : conteudoId);

                        List<Map<String, Object>> questoesRaw =
                                (List<Map<String, Object>>) documento.get("questoes");

                        if (questoesRaw != null && !questoesRaw.isEmpty()) {

                            List<Questao> todasQuestoes = new ArrayList<>();

                            for (Map<String, Object> q : questoesRaw) {  // ✅ itera direto
                                todasQuestoes.add(new Questao(
                                        (String) q.get("enunciado"),
                                        (String) q.get("alternativa_a"),
                                        (String) q.get("alternativa_b"),
                                        (String) q.get("alternativa_c"),
                                        (String) q.get("alternativa_d"),
                                        (String) q.get("alternativa_e"),
                                        (String) q.get("resposta_correta")
                                ));
                            }

                            Collections.shuffle(todasQuestoes);
                            int quantidade = Math.min(MAX_QUESTOES, todasQuestoes.size());
                            listaQuestoes = new ArrayList<>(todasQuestoes.subList(0, quantidade));

                            exibirQuestao(indiceAtual);

                        } else {
                            Toast.makeText(this, "Nenhuma questão encontrada!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Conteúdo não encontrado no banco.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao buscar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private static class AlternativaItem {
        String texto;
        boolean correta;

        AlternativaItem(String texto, boolean correta) {
            this.texto  = texto;
            this.correta = correta;
        }
    }

    private void exibirQuestao(int indice) {
        jaRespondeu = false;
        Questao q = listaQuestoes.get(indice);

        txtContador.setText("Questão " + (indice + 1) + " de " + listaQuestoes.size());
        txtEnunciado.setText(q.getEnunciado());

        List<AlternativaItem> alternativas = new ArrayList<>(Arrays.asList(
                new AlternativaItem(q.getAlternativa_a(), "A".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_b(), "B".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_c(), "C".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_d(), "D".equals(q.getResposta_correta())),
                new AlternativaItem(q.getAlternativa_e(), "E".equals(q.getResposta_correta()))
        ));

        Collections.shuffle(alternativas);

        List<Button> botoes = Arrays.asList(btnA, btnB, btnC, btnD, btnE);
        String[] letras = {"A", "B", "C", "D", "E"};

        String novaLetraCorreta = "A";
        for (int i = 0; i < alternativas.size(); i++) {
            botoes.get(i).setText(letras[i] + ") " + alternativas.get(i).texto);
            if (alternativas.get(i).correta) {
                novaLetraCorreta = letras[i];
            }
        }

        resetarBotoes();
        txtFeedback.setVisibility(View.GONE);
        btnProxima.setVisibility(View.GONE);

        final String letraCorretaFinal = novaLetraCorreta;
        configurarClique(btnA, "A", letraCorretaFinal);
        configurarClique(btnB, "B", letraCorretaFinal);
        configurarClique(btnC, "C", letraCorretaFinal);
        configurarClique(btnD, "D", letraCorretaFinal);
        configurarClique(btnE, "E", letraCorretaFinal);

        btnProxima.setOnClickListener(view -> {
            if (indiceAtual < listaQuestoes.size() - 1) {
                indiceAtual++;
                exibirQuestao(indiceAtual);
            } else {
                // ── NOVO: abre TelaResultado passando os dados ──
                Intent intent = new Intent(TelaQuestoes.this, TelaResultado.class);
                intent.putExtra("total_acertos", totalAcertos);
                intent.putExtra("total_erros",   totalErros);
                intent.putExtra("total_questoes", listaQuestoes.size());
                intent.putExtra("titulo", txtTituloConteudo.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    private void configurarClique(Button botao, String letra, String respostaCorreta) {
        botao.setOnClickListener(view -> {
            if (jaRespondeu) return;
            jaRespondeu = true;

            if (letra.equals(respostaCorreta)) {
                botao.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                txtFeedback.setTextColor(Color.parseColor("#4CAF50"));
                totalAcertos++; // ── NOVO ──
            } else {
                botao.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#F44336")));
                txtFeedback.setTextColor(Color.parseColor("#F44336"));
                totalErros++; // ── NOVO ──

                Button botaoCorreto = getBotaoPorLetra(respostaCorreta);
                if (botaoCorreto != null) {
                    botaoCorreto.setBackgroundTintList(
                            ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                }
            }

            txtFeedback.setVisibility(View.VISIBLE);
            btnProxima.setVisibility(View.VISIBLE);
        });
    }

    private Button getBotaoPorLetra(String letra) {
        switch (letra) {
            case "A": return btnA;
            case "B": return btnB;
            case "C": return btnC;
            case "D": return btnD;
            case "E": return btnE;
            default:  return null;
        }
    }

    private void resetarBotoes() {
        ColorStateList roxo = ColorStateList.valueOf(Color.parseColor("#7B2FBE"));
        btnA.setBackgroundTintList(roxo);
        btnB.setBackgroundTintList(roxo);
        btnC.setBackgroundTintList(roxo);
        btnD.setBackgroundTintList(roxo);
        btnE.setBackgroundTintList(roxo);
    }
}