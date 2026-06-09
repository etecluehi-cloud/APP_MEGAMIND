package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.ViewHolder>{

    List<DuvidaHistorico> lista;

    public HistoricoAdapter(List<DuvidaHistorico> lista){
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DuvidaHistorico msg = lista.get(position);

        holder.pergunta.setText("❓ " + msg.getPergunta());

        if(msg.getResposta()!=null && !msg.getResposta().isEmpty()){

            holder.resposta.setText("💬 "+msg.getResposta());

        }else{

            holder.resposta.setText("⏳ Aguardando resposta de algum Admin.");

        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView pergunta,resposta;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            pergunta=itemView.findViewById(R.id.txtPergunta);
            resposta=itemView.findViewById(R.id.txtResposta);

        }

    }

}
