package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DesempenhoAdapter extends RecyclerView.Adapter<DesempenhoAdapter.ViewHolder> {

    private List<ItemDesempenho> lista;

    public DesempenhoAdapter(List<ItemDesempenho> lista) {
        this.lista = lista;
    }

    public void atualizarLista(List<ItemDesempenho> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_desempenho, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDesempenho item = lista.get(position);

        holder.txtIcone.setText(item.getIcone());
        holder.txtNome.setText(item.getNome());
        holder.txtSub.setText(item.getAssistidos() + " de " + item.getTotalVideos() + " vídeos");
        holder.progressBar.setProgress(item.getPorcentagem());

        int pct = item.getPorcentagem();
        holder.txtPct.setText(pct + "%");

        // Verde se 100%, roxo caso contrário
        if (pct == 100) {
            holder.txtPct.setTextColor(Color.parseColor("#4CAF50"));
            holder.progressBar.setProgressTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            holder.txtPct.setTextColor(Color.parseColor("#7B2FBE"));
            holder.progressBar.setProgressTintList(
                    android.content.res.ColorStateList.valueOf(Color.parseColor("#7B2FBE")));
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIcone, txtNome, txtSub, txtPct;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            txtIcone    = itemView.findViewById(R.id.txtIcone);
            txtNome     = itemView.findViewById(R.id.txtNomeConteudo);
            txtSub      = itemView.findViewById(R.id.txtSubConteudo);
            progressBar = itemView.findViewById(R.id.progConteudo);
            txtPct      = itemView.findViewById(R.id.txtPorcentagem);
        }
    }
}