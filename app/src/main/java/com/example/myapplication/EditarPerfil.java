package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {

    ImageButton btnVoltar;
    LinearLayout lnlAlterarFoto, lnlEditarNome, lnlEditarSenha, lnlExcluirConta;
    TextView txtNomeAtual, txtEmailAtual, txtIniciais, txtNomeFoto;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth  = FirebaseAuth.getInstance();
        db     = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        btnVoltar      = findViewById(R.id.btnVoltar);
        lnlAlterarFoto = findViewById(R.id.lnlAlterarFoto);
        lnlEditarNome  = findViewById(R.id.lnlEditarNome);
        lnlEditarSenha = findViewById(R.id.lnlEditarSenha);
        lnlExcluirConta= findViewById(R.id.lnlExcluirConta);
        txtNomeAtual   = findViewById(R.id.txtNomeAtual);
        txtEmailAtual  = findViewById(R.id.txtEmailAtual);
        txtIniciais    = findViewById(R.id.txtIniciais);
        txtNomeFoto    = findViewById(R.id.txtNomeFoto);

        btnVoltar.setOnClickListener(v -> finish());

        // Carrega os dados do usuário
        carregarDados();

        // Alterar foto — por enquanto abre um Toast informativo
        // futuramente pode abrir galeria com Intent.ACTION_PICK
        lnlAlterarFoto.setOnClickListener(v ->
                Toast.makeText(this, "Funcionalidade em breve!", Toast.LENGTH_SHORT).show()
        );

        // Editar nome
        lnlEditarNome.setOnClickListener(v -> dialogEditarNome());

        // Editar senha
        lnlEditarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(EditarPerfil.this, AlterarSenha.class);
                startActivity(it);
            }
        });

        // Excluir conta
        lnlExcluirConta.setOnClickListener(v -> dialogExcluirConta());
    }

    private void carregarDados() {
        db.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String nome  = document.getString("nome");
                        String email = document.getString("email");

                        txtNomeAtual.setText(nome);
                        txtEmailAtual.setText(email);
                        txtNomeFoto.setText(nome);

                        // Iniciais do avatar (primeiras letras do nome e sobrenome)
                        if (nome != null && !nome.isEmpty()) {
                            String[] partes = nome.trim().split(" ");
                            String iniciais = String.valueOf(partes[0].charAt(0));
                            if (partes.length > 1) {
                                iniciais += String.valueOf(partes[partes.length - 1].charAt(0));
                            }
                            txtIniciais.setText(iniciais.toUpperCase());
                        }
                    }
                });
    }

    // ─────────────────────────────────────────
    // Dialog para editar o nome
    // ─────────────────────────────────────────
    private void dialogEditarNome() {
        View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        EditText input = new EditText(this);
        input.setHint("Novo nome");
        input.setText(txtNomeAtual.getText());
        input.setSingleLine(true);
        input.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle("Editar nome")
                .setView(input)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String novoNome = input.getText().toString().trim();
                    if (novoNome.isEmpty()) {
                        Toast.makeText(this, "Nome não pode ser vazio", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    salvarNome(novoNome);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void salvarNome(String novoNome) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", novoNome);

        db.collection("usuarios")
                .document(userId)
                .update(dados)
                .addOnSuccessListener(unused -> {
                    txtNomeAtual.setText(novoNome);
                    txtNomeFoto.setText(novoNome);

                    // Atualiza iniciais
                    String[] partes = novoNome.trim().split(" ");
                    String iniciais = String.valueOf(partes[0].charAt(0));
                    if (partes.length > 1) {
                        iniciais += String.valueOf(partes[partes.length - 1].charAt(0));
                    }
                    txtIniciais.setText(iniciais.toUpperCase());

                    Toast.makeText(this, "Nome atualizado!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
    // ─────────────────────────────────────────
    // Dialog para excluir conta
    // ─────────────────────────────────────────
    private void dialogExcluirConta() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir conta")
                .setMessage("Tem certeza? Todos os seus dados serão apagados permanentemente e essa ação não pode ser desfeita.")
                .setPositiveButton("Excluir", (dialog, which) -> excluirConta())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirConta() {
        // Apaga os dados do Firestore
        db.collection("usuarios")
                .document(userId)
                .delete()
                .addOnSuccessListener(unused -> {
                    // Apaga o progresso do usuário
                    db.collection("progresso")
                            .whereEqualTo("userId", userId)
                            .get()
                            .addOnSuccessListener(query -> {
                                for (com.google.firebase.firestore.DocumentSnapshot doc : query.getDocuments()) {
                                    doc.getReference().delete();
                                }
                            });

                    // Apaga a conta do Firebase Auth
                    mAuth.getCurrentUser().delete()
                            .addOnSuccessListener(unused2 -> {
                                Toast.makeText(this, "Conta excluída.", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(this, MainActivity.class);
                                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(it);
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Erro ao excluir conta: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                });
    }
}