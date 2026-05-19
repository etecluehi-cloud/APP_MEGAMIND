package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TelaRedacao extends AppCompatActivity {

    // Declaração das variáveis de tela
    private EditText edtRedacao;
    private TextView txtLinhasContagem;
    private Button btnSalvarRedacao;
    private Button btnNotas;
    private ImageButton btnVoltar;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redacao);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //linkando
        edtRedacao = findViewById(R.id.edtRedacao);
        txtLinhasContagem = findViewById(R.id.txtLinhasContagem);
        btnSalvarRedacao = findViewById(R.id.btnSalvarRedacao);
        btnNotas = findViewById(R.id.btnNotas);
        btnVoltar = findViewById(R.id.btnVoltar);

        // evento contador de linhas
        edtRedacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Conta quantas linhas o usuário digitou
                String texto = edtRedacao.getText().toString();
                int totalLinhas = texto.isEmpty() ? 0 : texto.split("\n", -1).length;
                txtLinhasContagem.setText(totalLinhas + " / 30 linhas");

                // Muda cor se estiver fora do mínimo
                if (totalLinhas < 7) {
                    txtLinhasContagem.setTextColor(getColor(android.R.color.holo_red_light));
                } else {
                    txtLinhasContagem.setTextColor(getColor(R.color.purple_dark)); // ou sua cor roxa
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnVoltar.setOnClickListener(v -> finish());

        btnNotas.setOnClickListener(v -> {
            Intent intent = new Intent(TelaRedacao.this, NotasActivity.class);
            startActivity(intent);
        });

        btnSalvarRedacao.setOnClickListener(v -> salvarRedacao());

        // Footer Navigation
        configurarFooter();
    }

    // Método que esta salvando a redação no firestore
    private void salvarRedacao() {
        String textoRedacao = edtRedacao.getText().toString().trim();

        // Validação básica
        if (textoRedacao.isEmpty()) {
            Toast.makeText(this, "Escreva sua redação antes de salvar!", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalLinhas = textoRedacao.split("\n", -1).length;
        if (totalLinhas < 7) {
            Toast.makeText(this, "Sua redação precisa ter pelo menos 7 linhas!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pega o usuário logado
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Você precisa estar logado!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = auth.getCurrentUser().getUid();

        // Pega o tema atual
        String temaAtual = "A importância da educação financeira nas escolas brasileiras";

        // Data e hora atual formatada
        String dataHora = new SimpleDateFormat("dd/MM/yyyy • HH'h'mm", Locale.getDefault()).format(new Date());

        // Monta o objeto (Map) que será salvo no Firestore
        Map<String, Object> redacao = new HashMap<>();
        redacao.put("tema", temaAtual);
        redacao.put("texto", textoRedacao);
        redacao.put("dataHora", dataHora);
        redacao.put("timestamp", System.currentTimeMillis()); // para ordenar por data

        // Desabilita o botão enquanto salva (evitando o duplo clique)
        btnSalvarRedacao.setEnabled(false);
        btnSalvarRedacao.setText("Salvando sua redação  MegaMind...");

        // aq é onde está acontecendo o salvamento no firestore
        db.collection("usuarios")
                .document(userId)
                .collection("redacoes")     // sub-coleção "redacoes" dentro desse usuário
                .add(redacao)               // .add() gera um ID automático para cada redação
                .addOnSuccessListener(documentReference -> {
                    // if der certo
                    Toast.makeText(this, "Redação salva com sucesso MegaMinder!", Toast.LENGTH_SHORT).show();
                    edtRedacao.setText(""); // limpa o campo
                    btnSalvarRedacao.setEnabled(true);
                    btnSalvarRedacao.setText("💾 Salvar Redação");
                })
                .addOnFailureListener(e -> {
                    // if der erro
                    Toast.makeText(this, " Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    btnSalvarRedacao.setEnabled(true);
                    btnSalvarRedacao.setText("💾 Salvar Redação");
                });
    }

    // nav do footer
    private void configurarFooter() {
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnDesempenho = findViewById(R.id.btnDesempenho);
        ImageButton btnBuscar = findViewById(R.id.btnBuscar);
        ImageButton btnPerfil = findViewById(R.id.btnPerfil);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, Home.class); // ⚠️ troque "Home" pelo nome da sua Activity home
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }
}
