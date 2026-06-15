package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.index.IndexEntry;

import java.util.List;

// adapter responsável por conectar a lista de cursos ao RecyclerView
public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.ViewHolder> {

    // lista de cursos que será exibida nos cards
    List<Curso> lista;

    // construtor -> recebe a lista de cursos para exibir
    public CursoAdapter(List<Curso> lista)
    {
        this.lista = lista;
    }

    // ViewHolder -> representa cada card individual na tela
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // componentes visuais de cada card
        TextView titulo; // nome do conteúdo
        CardView cardView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            // ligando cada variável com seu respectivo elemento do XML (item_card.xml)
            titulo = itemView.findViewById(R.id.txtTitulo);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    // cria o layout visual de cada card inflando o item_card.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    // preenche os dados de cada card com as informações do curso correspondente
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        // pega o curso na posição atual da lista
        Curso c = lista.get(position);

        // preenche os textos do card com os dados do curso
        holder.titulo.setText(c.getNome());




        if(c.isVisto())
        {
            holder.cardView.setCardBackgroundColor(
                    Color.parseColor("#7B2FBE"));

            holder.titulo.setTextColor(Color.WHITE);
        }
        else
        {
            holder.cardView.setCardBackgroundColor(
                    Color.WHITE);

            holder.titulo.setTextColor(Color.BLACK);
        }
    }

    // retorna o total de itens na lista (necessário para o RecyclerView funcionar)
    @Override
    public int getItemCount()
    {
        return lista.size();
    }
}