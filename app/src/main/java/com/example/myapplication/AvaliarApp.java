package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AvaliarApp extends AppCompatActivity {

    ImageButton btnVoltar;
    TextView cerebro1, cerebro2, cerebro3, cerebro4, cerebro5;
    TextView txtNota, btnEnviar;

    int notaSelecionada = 0;

    // Emojis de cérebro
    final String CEREBRO_CHEIO  = "🧠";
    final String CEREBRO_VAZIO  = "🩶"; // cinza para indicar não selecionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar_app);

        btnVoltar = findViewById(R.id.btnVoltar);
        cerebro1  = findViewById(R.id.cerebro1);
        cerebro2  = findViewById(R.id.cerebro2);
        cerebro3  = findViewById(R.id.cerebro3);
        cerebro4  = findViewById(R.id.cerebro4);
        cerebro5  = findViewById(R.id.cerebro5);
        txtNota   = findViewById(R.id.txtNota);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnVoltar.setOnClickListener(v -> finish());

        // Clique em cada cérebro seleciona a nota
        cerebro1.setOnClickListener(v -> selecionarNota(1));
        cerebro2.setOnClickListener(v -> selecionarNota(2));
        cerebro3.setOnClickListener(v -> selecionarNota(3));
        cerebro4.setOnClickListener(v -> selecionarNota(4));
        cerebro5.setOnClickListener(v -> selecionarNota(5));

        btnEnviar.setOnClickListener(v -> enviarAvaliacao());
    }

    private void selecionarNota(int nota) {
        notaSelecionada = nota;

        // Atualiza os cérebros visualmente
        TextView[] cerebros = {cerebro1, cerebro2, cerebro3, cerebro4, cerebro5};
        for (int i = 0; i < 5; i++) {
            if (i < nota) {
                cerebros[i].setText(CEREBRO_CHEIO);
                cerebros[i].setAlpha(1.0f);
            } else {
                cerebros[i].setText(CEREBRO_CHEIO);
                cerebros[i].setAlpha(0.3f); // escurece os não selecionados
            }
        }

        // Mostra o texto da nota
        String[] frases = {
                "Péssimo não consegui utiliza-lo ",
                "Ainda tem o que melhorar ",
                "Razoável, mas pode ser melhor ",
                "Gostei bastante! ",
                "Adorei o app! "
        };
        txtNota.setText(frases[nota - 1]);
        txtNota.setVisibility(View.VISIBLE);
    }

    private void enviarAvaliacao() {
        if (notaSelecionada == 0) {
            Toast.makeText(this, "Selecione uma nota antes de enviar!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> avaliacao = new HashMap<>();
        avaliacao.put("nota", notaSelecionada);
        avaliacao.put("timestamp", com.google.firebase.Timestamp.now());

        // Se o usuário estiver logado, salva o uid também
        if (usuario != null) {
            avaliacao.put("uid", usuario.getUid());
        }

        // Salva na coleção "avaliacoes" do Firestore
        db.collection("avaliacoes")
                .add(avaliacao)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Obrigado pela sua avaliação! 🧠", Toast.LENGTH_LONG).show();
                    // Desabilita o botão para evitar múltiplas avaliações na mesma sessão
                    btnEnviar.setEnabled(false);
                    btnEnviar.setAlpha(0.5f);
                    btnEnviar.setText("Avaliação enviada ✓");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao enviar avaliação. Tente novamente.", Toast.LENGTH_SHORT).show();
                });
    }
}