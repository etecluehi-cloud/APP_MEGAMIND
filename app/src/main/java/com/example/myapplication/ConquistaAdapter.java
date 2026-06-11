package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConquistaAdapter extends RecyclerView.Adapter<ConquistaAdapter.ViewHolder> {

    private List<Conquista> lista;

    public ConquistaAdapter(List<Conquista> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conquista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conquista c = lista.get(position);
        holder.txtConquista.setText(c.getTitulo() + (c.isDesbloqueada() ? " ✓" : ""));

        if (c.isDesbloqueada()) {
            holder.txtConquista.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#E8F5E9")));
            holder.txtConquista.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.txtConquista.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#EDE0FF")));
            holder.txtConquista.setTextColor(Color.parseColor("#5c2d91"));
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtConquista;

        ViewHolder(View itemView) {
            super(itemView);
            txtConquista = itemView.findViewById(R.id.txtConquista);
        }
    }
}