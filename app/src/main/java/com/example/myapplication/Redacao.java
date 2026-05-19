package com.example.myapplication;

public class Redacao {
    private String id;
    private String tema;
    private String texto;
    private String dataHora;

    public Redacao() {}

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
}


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.ViewHolder> {

    public interface OnVerClick { void onVer(Redacao r); }
    public interface OnCopiarClick { void onCopiar(Redacao r); }
    public interface OnExcluirClick { void onExcluir(Redacao r); }

    private List<Redacao> lista;
    private OnVerClick onVer;
    private OnCopiarClick onCopiar;
    private OnExcluirClick onExcluir;

    public NotasAdapter(List<Redacao> lista, OnVerClick onVer, OnCopiarClick onCopiar, OnExcluirClick onExcluir) {
        this.lista = lista;
        this.onVer = onVer;
        this.onCopiar = onCopiar;
        this.onExcluir = onExcluir;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Redacao redacao = lista.get(position);
        holder.txtDataHora.setText(redacao.getDataHora());
        holder.txtTituloNota.setText(redacao.getTema());
        // Preview: primeiros 100 caracteres do texto
        String preview = redacao.getTexto().length() > 100
                ? redacao.getTexto().substring(0, 100) + "..."
                : redacao.getTexto();
        holder.txtPreviewNota.setText(preview);

        holder.btnVer.setOnClickListener(v -> onVer.onVer(redacao));
        holder.btnCopiar.setOnClickListener(v -> onCopiar.onCopiar(redacao));
        holder.btnExcluir.setOnClickListener(v -> onExcluir.onExcluir(redacao));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDataHora, txtTituloNota, txtPreviewNota;
        Button btnVer, btnCopiar, btnExcluir;

        ViewHolder(View itemView) {
            super(itemView);
            txtDataHora = itemView.findViewById(R.id.txtDataHora);
            txtTituloNota = itemView.findViewById(R.id.txtTituloNota);
            txtPreviewNota = itemView.findViewById(R.id.txtPreviewNota);
            btnVer = itemView.findViewById(R.id.btnVer);
            btnCopiar = itemView.findViewById(R.id.btnCopiar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
