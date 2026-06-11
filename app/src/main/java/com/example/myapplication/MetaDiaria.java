package com.example.myapplication;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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

        carregarMetas();

        // configurar lista
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MetaAdapter(lista,
                new MetaAdapter.OnMetaActionListener()
                {
                    @Override
                    public void onEditar(int position)
                    {
                        EditText input = new EditText(MetaDiaria.this);

                        input.setText(lista.get(position).texto);

                        new AlertDialog.Builder(MetaDiaria.this)
                                .setTitle("Editar meta")
                                .setView(input)
                                .setPositiveButton("Salvar", (dialog, which) ->
                                {
                                    String novoTexto =
                                            input.getText().toString();

                                    if (!novoTexto.isEmpty())
                                    {
                                        lista.get(position)
                                                .setTexto(novoTexto);

                                        salvarMetas();

                                        adapter.notifyItemChanged(position);
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }

                    @Override
                    public void onExcluir(int position)
                    {
                        lista.remove(position);

                        salvarMetas();

                        adapter.notifyItemRemoved(position);

                        verificarListaVazia();
                    }

                    @Override
                    public void onConcluida()
                    {
                        salvarMetas();
                    }
                });

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
                            System.out.println("Quantidade: " + lista.size());

                            salvarMetas();

                            adapter.notifyItemInserted(lista.size() - 1);

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

    // metodo salvarMetas
    private void salvarMetas()
    {
        SharedPreferences prefs =
                getSharedPreferences("metas", MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();

        String json = gson.toJson(lista);

        editor.putString("lista_metas", json);

        editor.apply();
    }

    //metodo carregarMetas
    private void carregarMetas()
    {
        SharedPreferences prefs =
                getSharedPreferences("metas", MODE_PRIVATE);

        String json =
                prefs.getString("lista_metas", null);

        if (json != null)
        {
            Gson gson = new Gson();

            Type type =
                    new TypeToken<ArrayList<Meta>>(){}.getType();

            lista = gson.fromJson(json, type);
        }
    }
}