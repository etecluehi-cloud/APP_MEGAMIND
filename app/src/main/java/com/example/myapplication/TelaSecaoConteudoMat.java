package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TelaSecaoConteudoMat extends AppCompatActivity {

    ImageButton btnVoltar;
    LinearLayout lnlMatBasica, lnlPorcentagem, lnlAlgebra, lnlFuncoes,
            lnlGeoPlana, lnlGeoEsp, lnlGrandezasMedidas,
            lnlUnidMed, lnlVeloMedia, lnlEscala, lnlAreaPeri,
            lnlEstatistica, lnlProbabilidade, lnlMatFinanceira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_mat);

        btnVoltar        = findViewById(R.id.btnVoltar);
        lnlMatBasica     = findViewById(R.id.lnlMatBasica);
        lnlPorcentagem   = findViewById(R.id.lnlPorcentagem);
        lnlAlgebra       = findViewById(R.id.lnlAlgebra);
        lnlFuncoes       = findViewById(R.id.lnlFuncoes);
        lnlGeoPlana      = findViewById(R.id.lnlGeoPlana);
        lnlGeoEsp        = findViewById(R.id.lnlGeoEsp);
        lnlGrandezasMedidas = findViewById(R.id.lnlGrandezasMedidas);
        lnlUnidMed       = findViewById(R.id.lnlUnidMed);
        lnlVeloMedia     = findViewById(R.id.lnlVeloMedia);
        lnlEscala        = findViewById(R.id.lnlEscala);
        lnlAreaPeri      = findViewById(R.id.lnlAreaPeri);
        lnlEstatistica   = findViewById(R.id.lnlEstatistica);
        lnlProbabilidade = findViewById(R.id.lnlProbabilidade);
        lnlMatFinanceira = findViewById(R.id.lnlMatFinanceira);

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });

        lnlMatBasica.setOnClickListener(v -> abrirDetalhe("matematica_basica"));
        lnlPorcentagem.setOnClickListener(v -> abrirDetalhe("porcentagem"));
        lnlAlgebra.setOnClickListener(v -> abrirDetalhe("algebra"));
        lnlFuncoes.setOnClickListener(v -> abrirDetalhe("funcoes"));
        lnlGeoPlana.setOnClickListener(v -> abrirDetalhe("geometria_plana"));
        lnlGeoEsp.setOnClickListener(v -> abrirDetalhe("geometria_espacial"));
        lnlGrandezasMedidas.setOnClickListener(v -> abrirDetalhe("unidades_medida_velocidade_escala"));
        lnlUnidMed.setOnClickListener(v -> abrirDetalhe("unidades_medida_velocidade_escala"));
        lnlVeloMedia.setOnClickListener(v -> abrirDetalhe("unidades_medida_velocidade_escala"));
        lnlEscala.setOnClickListener(v -> abrirDetalhe("unidades_medida_velocidade_escala"));
        lnlAreaPeri.setOnClickListener(v -> abrirDetalhe("area_perimetro"));
        lnlEstatistica.setOnClickListener(v -> abrirDetalhe("estatistica"));
        lnlProbabilidade.setOnClickListener(v -> abrirDetalhe("probabilidade"));
        lnlMatFinanceira.setOnClickListener(v -> abrirDetalhe("matematica_financeira"));
    }

    private void abrirDetalhe(String conteudoId) {
        Intent intent = new Intent(this, detalhes_conteudos.class);
        intent.putExtra("conteudoId", conteudoId);
        startActivity(intent);
    }
}