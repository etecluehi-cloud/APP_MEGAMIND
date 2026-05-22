package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaSecaoConteudoMat extends AppCompatActivity {

    //1) atributos
    RecyclerView recyclerView;
    List<Curso> lista;
    CursoAdapter adapter;
    ImageButton btnVoltar;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_secao_conteudo_mat);

        //2) linkando os elementos

        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();


        // lista
        lista = new ArrayList<>();

        lista.add(new Curso(
                "Matemática Básica",
                "matematica_basica",
                false));

        lista.add(new Curso(
                "Porcentagem",
                "porcentagem",
                false));

        lista.add(new Curso(
                "Álgebra",
                "algebra",
                false));

        lista.add(new Curso(
                "Funções",
                "funcoes",
                false));

        lista.add(new Curso(
                "Geometria Plana",
                "geometria_plana",
                false));

        lista.add(new Curso(
                "Geometria Espacial",
                "geometria_espacial",
                false));

        lista.add(new Curso(
                "Grandezas e Medidas",
                "grandezas_medidas",
                false));

        lista.add(new Curso(
                "Estatística",
                "estatistica",
                false));

        lista.add(new Curso(
                "Probabilidade",
                "probabilidade",
                false));

        lista.add(new Curso(
                "Matemática Financeira",
                "matematica_financeira",
                false));

        //adapter
        adapter = new CursoAdapter(lista);

        // grid 2 colunas
        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);

        carregarProgresso();

        //evento do btnVoltar
        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        carregarProgresso();
    }

    private void carregarProgresso()
    {
        for (Curso c : lista)
        {
            String progressoId =
                    userId + "_" + c.getConteudoId();

            db.collection("progresso")
                    .document(progressoId)
                    .get()
                    .addOnSuccessListener(document -> {

                        boolean visto =
                                document.exists()
                                        && Boolean.TRUE.equals(
                                        document.getBoolean("visto"));

                        c.setVisto(visto);

                        adapter.notifyDataSetChanged();
                    });
        }
    }
}