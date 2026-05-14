package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MetaDiaria extends AppCompatActivity
{

    // lista (dados)
    RecyclerView recycler;
    FloatingActionButton btnAdd;
    TextView txtVazio;
    ImageButton btnVoltar;

    List<Meta> lista = new ArrayList<>();
    MetaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_diaria);

        // conectar com XML
        recycler = findViewById(R.id.recyclerMetas);
        btnAdd = findViewById(R.id.btnAddMeta);
        txtVazio = findViewById(R.id.txtVazio);
        btnVoltar = findViewById(R.id.btnVoltar);

        // configurar lista
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MetaAdapter(lista);
        recycler.setAdapter(adapter);

        verificarListaVazia();

        // botão voltar
        btnVoltar.setOnClickListener(v -> finish());

        // botão adicionar meta
        btnAdd.setOnClickListener(v ->
        {
            EditText input = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("Nova meta")
                    .setView(input)
                    .setPositiveButton("Adicionar", (dialog, which) ->
                    {
                        String texto = input.getText().toString();

                        if (!texto.isEmpty())
                        {
                            lista.add(new Meta(texto, false));
                            adapter.notifyDataSetChanged();
                            verificarListaVazia();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    // verifica se a lista está vazia
    private void verificarListaVazia()
    {
        if (lista.isEmpty())
        {
            txtVazio.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
        else
        {
            txtVazio.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }
}