package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MetaDiaria extends AppCompatActivity {

    RecyclerView recyclerPendentes, recyclerConcluidas;
    TextView txtProgressoMetas, txtSemPendentes, txtSemConcluidas;
    ProgressBar progressMetas;
    EditText edtNovaMeta;
    FloatingActionButton fabAdicionarMeta;

    FirebaseFirestore db;
    String userId;
    String dataHoje;

    List<Meta> listaPendentes  = new ArrayList<>();
    List<Meta> listaConcluidas = new ArrayList<>();

    MetaAdapter adapterPendentes;
    MetaAdapter adapterConcluidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_diaria);

        db     = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Data de hoje no formato dd-MM-yyyy (usada como ID do documento)
        dataHoje = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(new Date());

        recyclerPendentes  = findViewById(R.id.recyclerPendentes);
        recyclerConcluidas = findViewById(R.id.recyclerConcluidas);
        txtProgressoMetas  = findViewById(R.id.txtProgressoMetas);
        txtSemPendentes    = findViewById(R.id.txtSemPendentes);
        txtSemConcluidas   = findViewById(R.id.txtSemConcluidas);
        progressMetas      = findViewById(R.id.progressMetas);
        edtNovaMeta        = findViewById(R.id.edtNovaMeta);
        fabAdicionarMeta   = findViewById(R.id.fabAdicionarMeta);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        // Configura os dois RecyclerViews
        adapterPendentes = new MetaAdapter(listaPendentes, false);
        adapterConcluidas = new MetaAdapter(listaConcluidas, true);

        recyclerPendentes.setLayoutManager(new LinearLayoutManager(this));
        recyclerConcluidas.setLayoutManager(new LinearLayoutManager(this));
        recyclerPendentes.setAdapter(adapterPendentes);
        recyclerConcluidas.setAdapter(adapterConcluidas);

        carregarMetas();

        fabAdicionarMeta.setOnClickListener(v -> adicionarMeta());
    }

    // ─────────────────────────────────────────
    // Carregar metas do Firebase
    // ─────────────────────────────────────────
    private void carregarMetas() {
        db.collection("metas")
                .document(userId)
                .collection(dataHoje)
                .get()
                .addOnSuccessListener(query -> {
                    listaPendentes.clear();
                    listaConcluidas.clear();

                    for (QueryDocumentSnapshot doc : query) {
                        Meta meta = new Meta(
                                doc.getId(),
                                doc.getString("texto"),
                                Boolean.TRUE.equals(doc.getBoolean("concluida"))
                        );

                        if (meta.isConcluida()) {
                            listaConcluidas.add(meta);
                        } else {
                            listaPendentes.add(meta);
                        }
                    }

                    adapterPendentes.notifyDataSetChanged();
                    adapterConcluidas.notifyDataSetChanged();
                    atualizarProgresso();
                    atualizarMensagensVazias();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao carregar metas", Toast.LENGTH_SHORT).show()
                );
    }

    // ─────────────────────────────────────────
    // Adicionar nova meta
    // ─────────────────────────────────────────
    private void adicionarMeta() {
        String texto = edtNovaMeta.getText().toString().trim();

        if (texto.isEmpty()) {
            Toast.makeText(this, "Escreva uma meta primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> dados = new HashMap<>();
        dados.put("texto", texto);
        dados.put("concluida", false);

        db.collection("metas")
                .document(userId)
                .collection(dataHoje)
                .add(dados)
                .addOnSuccessListener(ref -> {
                    edtNovaMeta.setText("");
                    carregarMetas();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao salvar meta", Toast.LENGTH_SHORT).show()
                );
    }

    // ─────────────────────────────────────────
    // Marcar/desmarcar como concluída
    // ─────────────────────────────────────────
    private void alterarStatusMeta(Meta meta, boolean concluida) {
        db.collection("metas")
                .document(userId)
                .collection(dataHoje)
                .document(meta.getId())
                .update("concluida", concluida)
                .addOnSuccessListener(unused -> carregarMetas())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao atualizar meta", Toast.LENGTH_SHORT).show()
                );
    }

    // ─────────────────────────────────────────
    // Deletar meta
    // ─────────────────────────────────────────
    private void deletarMeta(Meta meta) {
        db.collection("metas")
                .document(userId)
                .collection(dataHoje)
                .document(meta.getId())
                .delete()
                .addOnSuccessListener(unused -> carregarMetas())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao deletar meta", Toast.LENGTH_SHORT).show()
                );
    }

    // ─────────────────────────────────────────
    // Atualizar barra de progresso
    // ─────────────────────────────────────────
    private void atualizarProgresso() {
        int total     = listaPendentes.size() + listaConcluidas.size();
        int concluidas = listaConcluidas.size();

        if (total == 0) {
            progressMetas.setProgress(0);
            txtProgressoMetas.setText("Nenhuma meta adicionada ainda");
        } else {
            int porcentagem = (concluidas * 100) / total;
            progressMetas.setProgress(porcentagem);
            txtProgressoMetas.setText(concluidas + " de " + total + " concluídas");
        }
    }

    // ─────────────────────────────────────────
    // Mostrar/ocultar mensagens de lista vazia
    // ─────────────────────────────────────────
    private void atualizarMensagensVazias() {
        txtSemPendentes.setVisibility(
                listaPendentes.isEmpty() ? View.VISIBLE : View.GONE);
        txtSemConcluidas.setVisibility(
                listaConcluidas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // ─────────────────────────────────────────
    // Adapter interno
    // ─────────────────────────────────────────
    class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder> {

        List<Meta> lista;
        boolean isConcluida;

        MetaAdapter(List<Meta> lista, boolean isConcluida) {
            this.lista       = lista;
            this.isConcluida = isConcluida;
        }

        @Override
        public MetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meta, parent, false);
            return new MetaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MetaViewHolder holder, int position) {
            Meta meta = lista.get(position);

            holder.txtMeta.setText(meta.getTexto());

            // Riscado se concluída
            if (meta.isConcluida()) {
                holder.txtMeta.setPaintFlags(
                        holder.txtMeta.getPaintFlags() |
                                android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.txtMeta.setPaintFlags(
                        holder.txtMeta.getPaintFlags() &
                                (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // Limpa listener antes de setar o estado para evitar loop
            holder.checkMeta.setOnCheckedChangeListener(null);
            holder.checkMeta.setChecked(meta.isConcluida());

            // Checkbox com delay para evitar travamento
            holder.checkMeta.setOnCheckedChangeListener((btn, isChecked) -> {
                holder.checkMeta.setEnabled(false);

                holder.checkMeta.postDelayed(() -> {
                    alterarStatusMeta(meta, isChecked);
                    holder.checkMeta.setEnabled(true);
                }, 200);
            });

            // Botão deletar
            holder.btnDeletar.setOnClickListener(v -> deletarMeta(meta));
        }

        @Override
        public int getItemCount() { return lista.size(); }

        class MetaViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkMeta;
            TextView txtMeta;
            ImageButton btnDeletar;

            MetaViewHolder(View itemView) {
                super(itemView);
                checkMeta  = itemView.findViewById(R.id.checkMeta);
                txtMeta    = itemView.findViewById(R.id.txtMeta);
                btnDeletar = itemView.findViewById(R.id.btnDeletarMeta);
            }
        }
    }
}