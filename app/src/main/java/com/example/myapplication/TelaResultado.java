// TelaResultado.java
package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TelaResultado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_resultado);

        // Recebe os dados vindos da TelaQuestoes
        int acertos      = getIntent().getIntExtra("total_acertos",  0);
        int erros        = getIntent().getIntExtra("total_erros",    0);
        int total        = getIntent().getIntExtra("total_questoes", 0);
        String titulo    = getIntent().getStringExtra("titulo");

        // Linka elementos
        TextView txtTitulo     = findViewById(R.id.txtTituloResultado);
        TextView txtAcertos    = findViewById(R.id.txtAcertos);
        TextView txtErros      = findViewById(R.id.txtErros);
        TextView txtDesempenho = findViewById(R.id.txtDesempenho);
        Button   btnVoltar     = findViewById(R.id.btnVoltarResultado);

        // Preenche os dados
        txtTitulo.setText(titulo != null ? titulo : "Resultado");
        txtAcertos.setText("✅  Acertos: " + acertos + " de " + total);
        txtErros.setText("❌  Erros: "   + erros   + " de " + total);

        // Mensagem de desempenho dinâmica
        int porcentagem = (total > 0) ? (acertos * 100 / total) : 0;
        String msg;
        if (porcentagem == 100) {
            msg = "🏆 Perfeito! Você acertou tudo!";
        } else if (porcentagem >= 60) {
            msg = "👏 Bom trabalho! Continue assim!";
        } else {
            msg = "📚 Não desanime! Revise o conteúdo e tente novamente.";
        }
        txtDesempenho.setText(msg);

        // Voltar para a tela anterior (menu de conteúdos)
        btnVoltar.setOnClickListener(view -> finish());
    }
}