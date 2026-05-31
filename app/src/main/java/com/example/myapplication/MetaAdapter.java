package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// adapter da lista de metas
public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder>
{
    // lista de metas
    private List<Meta> listaMetas;

    // comunicação com a activity
    private OnMetaActionListener listener;

    // ações de editar, excluir e salvar
    public interface OnMetaActionListener
    {
        void onEditar(int position);

        void onExcluir(int position);

        void onConcluida();
    }

    // construtor
    public MetaAdapter(List<Meta> listaMetas,
                       OnMetaActionListener listener)
    {
        this.listaMetas = listaMetas;
        this.listener = listener;
    }

    // cria o layout de cada item
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
        Meta meta = listaMetas.get(position);

        // evita bug ao reciclar itens
        holder.checkMeta.setOnCheckedChangeListener(null);

        // mostra os dados da meta
        holder.checkMeta.setText(meta.texto);
        holder.checkMeta.setChecked(meta.concluida);

        // marca ou desmarca a meta
        holder.checkMeta.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            meta.concluida = isChecked;

            if (listener != null)
            {
                listener.onConcluida();
            }
        });

        // botão editar
        holder.btnEditar.setOnClickListener(v ->
        {
            if (listener != null)
            {
                listener.onEditar(position);
            }
        });

        // botão excluir
        holder.btnExcluir.setOnClickListener(v ->
        {
            if (listener != null)
            {
                listener.onExcluir(position);
            }
        });
    }

    // quantidade de metas
    @Override
    public int getItemCount()
    {
        return listaMetas.size();
    }

    // representa um item da lista
    public static class MetaViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkMeta;

        TextView btnEditar;
        TextView btnExcluir;

        public MetaViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // conecta com os componentes do XML
            checkMeta = itemView.findViewById(R.id.checkMeta);

            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}