package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaQuestoesMat extends AppCompatActivity {
    TextView txtPergunta;
    Button btnA, btnB, btnC, btnD, btnVoltarQues;

    List<Questao> listaQuestoes = new ArrayList<>();
    String respostaCorretaTexto;
    int indiceAtual = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_questoes_mat);

        txtPergunta = findViewById(R.id.txtPergunta);
        btnVoltarQues = findViewById(R.id.btnVoltarQues);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        buscarQuestoes();

        // Evento bntVoltarQues para tela home
        btnVoltarQues.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaQuestoesMat.this, Home.class);
                startActivity(it);
            }
        });
    }

    private void buscarQuestoes() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("matematica_teste")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        Questao q = new Questao(
                                doc.getString("pergunta"),
                                doc.getString("alternativaA"),
                                doc.getString("alternativaB"),
                                doc.getString("alternativaC"),
                                doc.getString("alternativaD"),
                                doc.getString("respostaCorreta")
                        );

                        listaQuestoes.add(q);
                    }

                    Collections.shuffle(listaQuestoes);

                    mostrarQuestao(listaQuestoes.get(0));
                });
    }

    private void mostrarQuestao(Questao q) {

        txtPergunta.setText(q.pergunta);

        List<String> alternativas = new ArrayList<>();
        alternativas.add(q.alternativaA);
        alternativas.add(q.alternativaB);
        alternativas.add(q.alternativaC);
        alternativas.add(q.alternativaD);

        // pega resposta correta antes de embaralhar
        switch (q.respostaCorreta) {
            case "A": respostaCorretaTexto = q.alternativaA; break;
            case "B": respostaCorretaTexto = q.alternativaB; break;
            case "C": respostaCorretaTexto = q.alternativaC; break;
            case "D": respostaCorretaTexto = q.alternativaD; break;
        }

        Collections.shuffle(alternativas);

        btnA.setText(alternativas.get(0));
        btnB.setText(alternativas.get(1));
        btnC.setText(alternativas.get(2));
        btnD.setText(alternativas.get(3));

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;
            String respostaUsuario = btn.getText().toString();

            if (respostaUsuario.equals(respostaCorretaTexto)) {
                // Acertou - botão verde e passa pra proxima
                btn.setBackgroundColor(Color.GREEN);
                btn.postDelayed(() -> {
                    btn.setBackgroundColor(Color.parseColor("#6200EE"));
                    indiceAtual++;
                    if (indiceAtual < listaQuestoes.size())
                    {
                        mostrarQuestao(listaQuestoes.get(indiceAtual));
                    }
                    else
                    {
                        Toast.makeText(this, "Fim das questões!", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
            else
            {
                // Errou - botão vermelho e mostra a mesma questão embaralhada
                btn.setBackgroundColor(Color.RED);
                btn.postDelayed(() -> {
                    btn.setBackgroundColor(Color.parseColor("#6200EE"));
                    mostrarQuestao(listaQuestoes.get(indiceAtual)); // Mesma questão embaralha novamente
                }, 1000);
            }
        };

        btnA.setOnClickListener(listener);
        btnB.setOnClickListener(listener);
        btnC.setOnClickListener(listener);
        btnD.setOnClickListener(listener);
    }
}