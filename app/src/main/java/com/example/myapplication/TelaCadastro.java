package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity
{
    ImageButton btnVoltar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button btnCadastrar;
    EditText txtNome, txtEmail, txtSenha, txtConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        // 2) linkando os elementos
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfirmarSenha = findViewById(R.id.txtConfirmarSenha);

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());

        // 3) evento do btnVoltar
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaCadastro.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    private void cadastrarUsuario()
    {
        String nome = txtNome.getText().toString();
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        String confirmarSenha = txtConfirmarSenha.getText().toString();

        //Validação dos campos
        if (nome.isEmpty())
        {
            txtNome.setError("Informe o nome");
            txtNome.requestFocus();
            return;
        }
        if (email.isEmpty())
        {
            txtEmail.setError("Informe o email");
            txtEmail.requestFocus();
            return;
        }

        if (senha.isEmpty())
        {
            txtSenha.setError("Informe a senha");
            txtSenha.requestFocus();
            return;
        }


        if (!senha.equals(confirmarSenha))
        {
            Toast.makeText(this, "Senhas não conhecidem", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                String userId = mAuth.getCurrentUser().getUid();

                Map<String, Object> usuario = new HashMap<>();
                usuario.put("nome", nome);
                usuario.put("email", email);

                db.collection("usuarios").document(userId).set(usuario).addOnSuccessListener(aVoid ->
                {
                   Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(this, TelaLogin.class));
                   finish();
                })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
            else
            {
                String erroMsg = task.getException().getMessage();

                if (erroMsg != null && erroMsg.contains("already in use"))
                {
                    txtEmail.setError("Este email já está cadastrado");
                    txtSenha.requestFocus();
                }
                else
                {
                    Toast.makeText(this, "Erro: " + erroMsg, Toast.LENGTH_SHORT).show();
                }
            }
            });
        }
}