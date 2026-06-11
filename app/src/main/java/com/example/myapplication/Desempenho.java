package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Desempenho extends AppCompatActivity {

    PieChart pieChart;
    TabLayout tabLayout;
    RecyclerView recyclerDesempenho, recyclerConquistas;
    TextView txtTotalAssistidos, txtTotalVideos, txtCompletos;
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;

    FirebaseFirestore db;
    String userId;

    // Conteúdos por matéria
    List<ItemDesempenho> listaMatematica  = new ArrayList<>();
    List<ItemDesempenho> listaPortugues   = new ArrayList<>();
    List<ItemDesempenho> listaRedacao     = new ArrayList<>();

    DesempenhoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desempenho);

        db     = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        pieChart            = findViewById(R.id.pieChart);
        tabLayout           = findViewById(R.id.tabLayout);
        recyclerDesempenho  = findViewById(R.id.recyclerDesempenho);
        recyclerConquistas  = findViewById(R.id.recyclerConquistas);
        txtTotalAssistidos  = findViewById(R.id.txtTotalAssistidos);
        txtTotalVideos      = findViewById(R.id.txtTotalVideos);
        txtCompletos        = findViewById(R.id.txtCompletos);
        btnHome             = findViewById(R.id.btnHome);
        btnDesempenho       = findViewById(R.id.btnDesempenho);
        btnBuscar           = findViewById(R.id.btnBuscar);
        btnPerfil           = findViewById(R.id.btnPerfil);

        configurarListas();
        configurarAbas();
        configurarFooter();
        carregarDados();
    }

    // ─────────────────────────────────────────
    // Popula as listas de conteúdos
    // ─────────────────────────────────────────
    private void configurarListas() {
        // Matemática — 10 conteúdos
        listaMatematica.add(new ItemDesempenho("matematica_basica",               "Mat. Básica",       "2+2"));
        listaMatematica.add(new ItemDesempenho("porcentagem",                     "Porcentagem",       "%"));
        listaMatematica.add(new ItemDesempenho("algebra",                         "Álgebra",           "Σ"));
        listaMatematica.add(new ItemDesempenho("funcoes",                         "Funções",           "f(x)"));
        listaMatematica.add(new ItemDesempenho("geometria_plana",                 "Geom. Plana",       "◆"));
        listaMatematica.add(new ItemDesempenho("geometria_espacial",              "Geom. Espacial",    "▲"));
        listaMatematica.add(new ItemDesempenho("unidades_medida_velocidade_escala","Grandezas",        "📏"));
        listaMatematica.add(new ItemDesempenho("estatistica",                     "Estatística",       "📊"));
        listaMatematica.add(new ItemDesempenho("probabilidade",                   "Probabilidade",     "🎲"));
        listaMatematica.add(new ItemDesempenho("matematica_financeira",           "Mat. Financeira",   "$"));

        // Português — 7 conteúdos
        listaPortugues.add(new ItemDesempenho("leitura_interpretacao", "Leitura e Interpretação", "📖"));
        listaPortugues.add(new ItemDesempenho("generos_textuais",      "Gêneros Textuais",        "📝"));
        listaPortugues.add(new ItemDesempenho("variacao_linguistica",  "Variação Linguística",    "🗣"));
        listaPortugues.add(new ItemDesempenho("gramatica",             "Gramática",               "G"));
        listaPortugues.add(new ItemDesempenho("coesao_coerencia",      "Coesão e Coerência",      "🔗"));
        listaPortugues.add(new ItemDesempenho("figuras_linguagem",     "Figuras de Linguagem",    "🎭"));
        listaPortugues.add(new ItemDesempenho("literatura",            "Literatura",              "📚"));

        // Redação — 7 conteúdos
        listaRedacao.add(new ItemDesempenho("estrutura_texto",      "Estrutura do Texto",      "✏️"));
        listaRedacao.add(new ItemDesempenho("tipologia_textual",    "Tipologia Textual",       "📋"));
        listaRedacao.add(new ItemDesempenho("competencias_enem",    "Competências do ENEM",    "⭐"));
        listaRedacao.add(new ItemDesempenho("argumentacao",         "Argumentação",            "💡"));
        listaRedacao.add(new ItemDesempenho("proposta_intervencao", "Proposta de Intervenção", "🎯"));
        listaRedacao.add(new ItemDesempenho("temas_sociais",        "Temas Sociais",           "🌎"));
        listaRedacao.add(new ItemDesempenho("erros_zeram",          "Erros que Zeram",         "❌"));

        // Adapter começa com Matemática
        adapter = new DesempenhoAdapter(listaMatematica);
        recyclerDesempenho.setLayoutManager(new LinearLayoutManager(this));
        recyclerDesempenho.setAdapter(adapter);
    }

    // ─────────────────────────────────────────
    // Abas Matemática / Português / Redação
    // ─────────────────────────────────────────
    private void configurarAbas() {
        tabLayout.addTab(tabLayout.newTab().setText("Matemática"));
        tabLayout.addTab(tabLayout.newTab().setText("Português"));
        tabLayout.addTab(tabLayout.newTab().setText("Redação"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: adapter.atualizarLista(listaMatematica); break;
                    case 1: adapter.atualizarLista(listaPortugues);  break;
                    case 2: adapter.atualizarLista(listaRedacao);    break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // ─────────────────────────────────────────
    // Carrega progresso do Firebase
    // ─────────────────────────────────────────
    private void carregarDados() {
        List<ItemDesempenho> todos = new ArrayList<>();
        todos.addAll(listaMatematica);
        todos.addAll(listaPortugues);
        todos.addAll(listaRedacao);

        final int[] totalAssistidos = {0};
        final int[] totalVideos     = {0};
        final int[] completos       = {0};
        final int[] processados     = {0};
        final int tamanhoTotal      = todos.size();

        for (ItemDesempenho item : todos) {
            db.collection("conteudo")
                    .document(item.getConteudoId())
                    .get()
                    .addOnSuccessListener(doc -> {
                        int total = 0;

                        if (doc.exists()) {
                            // Verifica videoId1 ou videoId (compatibilidade)
                            String v1 = doc.getString("videoId1");
                            if (v1 == null || v1.isEmpty()) v1 = doc.getString("videoId");
                            String v2 = doc.getString("videoId2");
                            String v3 = doc.getString("videoId3");

                            if (v1 != null && !v1.isEmpty()) total++;
                            if (v2 != null && !v2.isEmpty()) total++;
                            if (v3 != null && !v3.isEmpty()) total++;
                        }

                        // Se não encontrou nenhum videoId, assume 3
                        if (total == 0) total = 3;

                        item.setTotalVideos(total);
                        totalVideos[0] += total;

                        db.collection("progresso_videos")
                                .document(userId)
                                .collection(item.getConteudoId())
                                .get()
                                .addOnSuccessListener(query -> {
                                    int vistos = 0;
                                    for (com.google.firebase.firestore.DocumentSnapshot d : query.getDocuments()) {
                                        if (Boolean.TRUE.equals(d.getBoolean("visto"))) vistos++;
                                    }
                                    item.setAssistidos(vistos);
                                    totalAssistidos[0] += vistos;

                                    if (vistos > 0 && vistos >= item.getTotalVideos()) completos[0]++;

                                    processados[0]++;
                                    if (processados[0] == tamanhoTotal) {
                                        runOnUiThread(() ->
                                                atualizarUI(totalAssistidos[0], totalVideos[0], completos[0])
                                        );
                                    }
                                });
                    });
        }
    }

    private void atualizarUI(int assistidos, int total, int completos) {
        txtTotalAssistidos.setText(String.valueOf(assistidos));
        txtTotalVideos.setText(String.valueOf(total));
        txtCompletos.setText(String.valueOf(completos));

        configurarDonut(assistidos, total);
        adapter.notifyDataSetChanged();
        configurarConquistas(assistidos, completos);
    }

    // ─────────────────────────────────────────
    // Donut chart
    // ─────────────────────────────────────────
    private void configurarDonut(int assistidos, int total) {
        int pendentes = total - assistidos;

        List<PieEntry> entradas = new ArrayList<>();
        entradas.add(new PieEntry(assistidos > 0 ? assistidos : 0.01f, ""));
        entradas.add(new PieEntry(pendentes  > 0 ? pendentes  : 0.01f, ""));

        PieDataSet dataSet = new PieDataSet(entradas, "");
        dataSet.setColors(0xFF7B2FBE, 0xFFE0D0FF);
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setHoleRadius(55f);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setHoleColor(android.graphics.Color.TRANSPARENT);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTouchEnabled(false);

        // Texto central com %
        int pct = total > 0 ? (assistidos * 100) / total : 0;
        pieChart.setCenterText(pct + "%");
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(0xFF7B2FBE);
        pieChart.invalidate();
    }

    // ─────────────────────────────────────────
    // Conquistas
    // ─────────────────────────────────────────
    private void configurarConquistas(int assistidos, int completos) {
        List<Conquista> lista = new ArrayList<>();

        // 1 — Primeiro vídeo
        if (assistidos >= 1)
            lista.add(new Conquista("🎬 1º vídeo assistido", true));

        // 2 — 5 vídeos
        if (assistidos >= 5)
            lista.add(new Conquista("⚡ 5 vídeos assistidos", true));

        // 3 — 10 vídeos
        if (assistidos >= 10)
            lista.add(new Conquista("🔥 10 vídeos assistidos", true));

        // 4 — Conteúdo completo (qualquer matéria)
        if (completos >= 1)
            lista.add(new Conquista("✅ Conteúdo completo", true));

        // 5 — Mestre da Mat. (todos os 10 conteúdos de matemática completos)
        boolean mestreMat = listaMatematica.stream()
                .allMatch(item -> item.getAssistidos() >= item.getTotalVideos()
                        && item.getTotalVideos() > 0);
        if (mestreMat)
            lista.add(new Conquista("🏆 Mestre da Mat.", true));

        // 6 — Mestre da Ling. Port.
        boolean mestrePort = listaPortugues.stream()
                .allMatch(item -> item.getAssistidos() >= item.getTotalVideos()
                        && item.getTotalVideos() > 0);
        if (mestrePort)
            lista.add(new Conquista("🏆 Mestre da Ling. Port.", true));

        // 7 — Mestre da Red.
        boolean mestreRed = listaRedacao.stream()
                .allMatch(item -> item.getAssistidos() >= item.getTotalVideos()
                        && item.getTotalVideos() > 0);
        if (mestreRed)
            lista.add(new Conquista("🏆 Mestre da Red.", true));

        // Se não tiver nenhuma conquista ainda, não mostra nada
        if (lista.isEmpty()) {
            recyclerConquistas.setVisibility(android.view.View.GONE);
            return;
        }

        recyclerConquistas.setVisibility(android.view.View.VISIBLE);
        ConquistaAdapter conquistaAdapter = new ConquistaAdapter(lista);
        recyclerConquistas.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerConquistas.setAdapter(conquistaAdapter);
    }

    // ─────────────────────────────────────────
    // Footer
    // ─────────────────────────────────────────
    private void configurarFooter() {
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
            finish();
        });
        btnPerfil.setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilUsuario.class));
        });
    }
}