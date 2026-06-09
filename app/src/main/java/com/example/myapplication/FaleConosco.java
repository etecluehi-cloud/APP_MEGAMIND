package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FaleConosco extends AppCompatActivity {

    private EditText edtDuvida;
    private Button btnEnviar;
    private RecyclerView rvHistorico;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String userId;
    private String email;

    private ArrayList<DuvidaHistorico> lista;
    private HistoricoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fale_conosco);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        email = mAuth.getCurrentUser().getEmail();

        edtDuvida = findViewById(R.id.edtDuvida);
        btnEnviar = findViewById(R.id.btnEnviar);
        rvHistorico = findViewById(R.id.rvHistorico);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        lista = new ArrayList<>();

        adapter = new HistoricoAdapter(lista);

        rvHistorico.setLayoutManager(new LinearLayoutManager(this));

        rvHistorico.setAdapter(adapter);

        carregarHistorico();

        btnEnviar.setOnClickListener(v -> enviarDuvida());

    }

    // ─────────────────────────────────────────
    // Enviar dúvida para o Firebase
    // ─────────────────────────────────────────

    private void enviarDuvida() {

        String texto = edtDuvida.getText().toString().trim();

        if (texto.isEmpty()) {

            Toast.makeText(this,
                    "Escreva sua dúvida antes de enviar",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        Map<String, Object> dados = new HashMap<>();

        dados.put("email", email);
        dados.put("userId", userId);
        dados.put("pergunta", texto);

        dados.put("resposta", "");
        dados.put("status", "pendente");

        dados.put("data", FieldValue.serverTimestamp());
        dados.put("dataResposta", null);

        db.collection("duvidas")
                .add(dados)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this,
                            "Dúvida enviada com sucesso!",
                            Toast.LENGTH_SHORT).show();

                    edtDuvida.setText("");

                    carregarHistorico();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show()

                );

    }

    // ─────────────────────────────────────────
    // Carrega histórico
    // ─────────────────────────────────────────

    private void carregarHistorico() {

        db.collection("duvidas")
                .whereEqualTo("userId", userId)
                .orderBy("data", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    lista.clear();

                        for ( int i = querySnapshot.size() - 1; i >= 0; i--) {

                            DocumentSnapshot doc = querySnapshot.getDocuments().get(i);

                            String pergunta = doc.getString("pergunta");
                            String resposta = doc.getString("resposta");

                            if (pergunta == null) pergunta = "";
                            if (resposta == null) resposta = "";

                            lista.add(new DuvidaHistorico(pergunta, resposta));
                        }


                    adapter.notifyDataSetChanged();

                    if (lista.size() > 0) {

                        rvHistorico.scrollToPosition(
                                lista.size() - 1
                        );
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Erro: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                });
    }
    @Override
    protected void onResume() {
        super.onResume();

        carregarHistorico();
    }
}