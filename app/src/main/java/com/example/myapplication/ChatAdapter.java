package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Mensagem> lista;

    public ChatAdapter(List<Mensagem> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensagem_bot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensagem msg = lista.get(position);

        if (msg.isUsuario()) {
            // Mostra o lado direito (usuário)
            holder.layoutBot.setVisibility(View.GONE);
            holder.layoutUser.setVisibility(View.VISIBLE);
            holder.txtMensagemUser.setText(msg.getTexto());
            holder.txtHoraUser.setText(msg.getHora());
        } else {
            // Mostra o lado esquerdo (bot)
            holder.layoutBot.setVisibility(View.VISIBLE);
            holder.layoutUser.setVisibility(View.GONE);
            holder.txtMensagemBot.setText(msg.getTexto());
            holder.txtHoraBot.setText(msg.getHora());
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutBot, layoutUser;
        TextView txtMensagemBot, txtHoraBot;
        TextView txtMensagemUser, txtHoraUser;

        ViewHolder(View itemView) {
            super(itemView);
            layoutBot = itemView.findViewById(R.id.layoutMensagemBot);
            layoutUser = itemView.findViewById(R.id.layoutMensagemUser);
            txtMensagemBot = itemView.findViewById(R.id.txtMensagemBot);
            txtHoraBot = itemView.findViewById(R.id.txtHoraBot);
            txtMensagemUser = itemView.findViewById(R.id.txtMensagemUser);
            txtHoraUser = itemView.findViewById(R.id.txtHoraUser);
        }
    }
}
