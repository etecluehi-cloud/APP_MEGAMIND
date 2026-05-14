package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter da lista de metas
public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder>
{

    // lista de metas
    private List<Meta> listaMetas;

    // construtor (recebe a lista)
    public MetaAdapter(List<Meta> listaMetas)
    {
        this.listaMetas = listaMetas;
    }

    // cria o layout de cada item (item_meta.xml)
    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meta, parent, false);

        return new MetaViewHolder(view);
    }

    // coloca os dados em cada item
    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder holder, int position)
    {
        Meta meta = listaMetas.get(position); // pega uma meta da lista

        holder.checkBox.setText(meta.texto); // coloca o texto
        holder.checkBox.setChecked(meta.concluida); // marca se foi concluída

        // quando clicar no checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            meta.concluida = isChecked; // atualiza o estado da meta
        });
    }

    // quantidade de itens da lista
    @Override
    public int getItemCount()
    {
        return listaMetas.size();
    }

    // classe que representa cada item da tela
    public static class MetaViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;

        public MetaViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // conecta com o checkbox do item_meta.xml
            checkBox = itemView.findViewById(R.id.checkMeta);
        }
    }
}