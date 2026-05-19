package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotasActivity extends AppCompatActivity {

    private RecyclerView recyclerNotas;
    private TextView txtListaVazia;
    private NotasAdapter adapter;
    private List<Redacao> listaRedacoes = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerNotas = findViewById(R.id.recyclerNotas);
        txtListaVazia = findViewById(R.id.txtListaVazia);

        // Configura o RecyclerView com layout vertical
        recyclerNotas.setLayoutManager(new LinearLayoutManager(this));

        // Cria o adapter com callbacks para os botões
        adapter = new NotasAdapter(listaRedacoes,
                // Callback do botão VER
                redacao -> {
                    // Abre um dialog ou nova tela mostrando o texto completo
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle(redacao.getTema())
                            .setMessage(redacao.getTexto())
                            .setPositiveButton("Fechar", null)
                            .show();
                },
                // Callback do botão COPIAR
                redacao -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("redacao", redacao.gettex());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "📋 Redação copiada! Agora cole no Corretor IA.", Toast.LENGTH_SHORT).show();
                },
                // Callback do botão EXCLUIR
                redacao -> confirmarExclusao(redacao)
        );

        recyclerNotas.setAdapter(adapter);

        // Carrega as redações do Firestore
        carregarRedacoes();

        // Botões
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        findViewById(R.id.btnChatBot).setOnClickListener(v -> {
            startActivity(new Intent(this, ChatBot.class));
        });

        configurarFooter();
    }

    // ============================================================
    // MÉTODO QUE BUSCA AS REDAÇÕES DO FIRESTORE
    // ============================================================
    private void carregarRedacoes() {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .collection("redacoes")
                .orderBy("timestamp", Query.Direction.DESCENDING) // mais recentes primeiro
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaRedacoes.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Monta o objeto Redacao a partir dos dados do Firestore
                        Redacao r = new Redacao();
                        r.setId(doc.getId());
                        r.setTema(doc.getString("tema"));
                        r.setTexto(doc.getString("texto"));
                        r.setDataHora(doc.getString("dataHora"));
                        listaRedacoes.add(r);
                    }

                    adapter.notifyDataSetChanged();

                    // Mostra mensagem se lista estiver vazia
                    if (listaRedacoes.isEmpty()) {
                        txtListaVazia.setVisibility(View.VISIBLE);
                        recyclerNotas.setVisibility(View.GONE);
                    } else {
                        txtListaVazia.setVisibility(View.GONE);
                        recyclerNotas.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar notas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // ============================================================
    // EXCLUIR COM CONFIRMAÇÃO
    // ============================================================
    private void confirmarExclusao(Redacao redacao) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Excluir redação?")
                .setMessage("Tem certeza que deseja excluir \"" + redacao.getTema() + "\"?")
                .setPositiveButton("Excluir", (dialog, which) -> excluirRedacao(redacao))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirRedacao(Redacao redacao) {
        if (auth.getCurrentUser() == null) return;
        String userId = auth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .collection("redacoes")
                .document(redacao.getId()) // ID do documento no Firestore
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Redação excluída.", Toast.LENGTH_SHORT).show();
                    listaRedacoes.remove(redacao);
                    adapter.notifyDataSetChanged();

                    if (listaRedacoes.isEmpty()) {
                        txtListaVazia.setVisibility(View.VISIBLE);
                        recyclerNotas.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void configurarFooter() {
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}


// ============================================================
// MODELO: Redacao.java
// ONDE SALVAR: app/src/main/java/com/seu/pacote/Redacao.java
// ============================================================
// (Crie este arquivo separado, não junto com o Activity)

/*
package com.seu.pacote;

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
*/


// ============================================================
// ADAPTER: NotasAdapter.java
// ONDE SALVAR: app/src/main/java/com/seu/pacote/NotasAdapter.java
// ============================================================

/*
package com.seu.pacote;

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
}
*/