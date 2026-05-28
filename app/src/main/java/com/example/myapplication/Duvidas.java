package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Duvidas extends AppCompatActivity {

    ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duvidas);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // volta para a tela anterior
            }
        });

        // Configura o clique em cada pergunta para expandir/recolher a resposta
        configurarFaq(R.id.pergunta1, R.id.resposta1);
        configurarFaq(R.id.pergunta2, R.id.resposta2);
        configurarFaq(R.id.pergunta3, R.id.resposta3);
        configurarFaq(R.id.pergunta4, R.id.resposta4);
        configurarFaq(R.id.pergunta5, R.id.resposta5);
    }

    private void configurarFaq(int idPergunta, int idResposta) {
        LinearLayout pergunta = findViewById(idPergunta);
        TextView resposta = findViewById(idResposta);
        TextView icone = pergunta.findViewById(getIconeId(idPergunta));

        pergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resposta.getVisibility() == View.GONE) {
                    resposta.setVisibility(View.VISIBLE);
                    if (icone != null) icone.setText("▲");
                } else {
                    resposta.setVisibility(View.GONE);
                    if (icone != null) icone.setText("▼");
                }
            }
        });
    }

    // Retorna o id do ícone de seta dentro de cada pergunta
    private int getIconeId(int idPergunta) {
        if (idPergunta == R.id.pergunta1) return R.id.icone1;
        if (idPergunta == R.id.pergunta2) return R.id.icone2;
        if (idPergunta == R.id.pergunta3) return R.id.icone3;
        if (idPergunta == R.id.pergunta4) return R.id.icone4;
        if (idPergunta == R.id.pergunta5) return R.id.icone5;
        return 0;
    }
}