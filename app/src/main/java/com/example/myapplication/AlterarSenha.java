package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenha extends AppCompatActivity {

    EditText edtSenhaAtual, edtNovaSenha, edtConfirmarSenha;
    ImageButton btnVoltar, btnOlhoAtual, btnOlhoNova, btnOlhoConfirmar;
    Button btnSalvarSenha;
    TextView txtForcaSenha;
    View bar1, bar2, bar3, bar4;

    // controla se cada campo está visível ou oculto
    boolean senhaAtualVisivel = false;
    boolean novaSenhaVisivel  = false;
    boolean confirmarVisivel  = false;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        mAuth = FirebaseAuth.getInstance();

        btnVoltar          = findViewById(R.id.btnVoltar);
        edtSenhaAtual      = findViewById(R.id.edtSenhaAtual);
        edtNovaSenha       = findViewById(R.id.edtNovaSenha);
        edtConfirmarSenha  = findViewById(R.id.edtConfirmarSenha);
        btnOlhoAtual       = findViewById(R.id.btnOlhoAtual);
        btnOlhoNova        = findViewById(R.id.btnOlhoNova);
        btnOlhoConfirmar   = findViewById(R.id.btnOlhoConfirmar);
        btnSalvarSenha     = findViewById(R.id.btnSalvarSenha);
        txtForcaSenha      = findViewById(R.id.txtForcaSenha);
        bar1               = findViewById(R.id.bar1);
        bar2               = findViewById(R.id.bar2);
        bar3               = findViewById(R.id.bar3);
        bar4               = findViewById(R.id.bar4);

        btnVoltar.setOnClickListener(v -> finish());

        // Olhinhos
        btnOlhoAtual.setOnClickListener(v -> {
            senhaAtualVisivel = !senhaAtualVisivel;
            toggleVisibilidade(edtSenhaAtual, senhaAtualVisivel);
        });

        btnOlhoNova.setOnClickListener(v -> {
            novaSenhaVisivel = !novaSenhaVisivel;
            toggleVisibilidade(edtNovaSenha, novaSenhaVisivel);
        });

        btnOlhoConfirmar.setOnClickListener(v -> {
            confirmarVisivel = !confirmarVisivel;
            toggleVisibilidade(edtConfirmarSenha, confirmarVisivel);
        });

        //  Barra de força em tempo real
        edtNovaSenha.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                atualizarForcaSenha(s.toString());
            }
        });

        //  Salvar
        btnSalvarSenha.setOnClickListener(v -> salvarSenha());
    }

    private void toggleVisibilidade(EditText campo, boolean visivel) {
        int cursor = campo.getSelectionEnd();
        if (visivel) {
            campo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            campo.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        campo.setSelection(cursor); // mantém o cursor na posição
    }

    private void atualizarForcaSenha(String senha) {
        int forca = calcularForca(senha);

        switch (forca) {
            case 0:
                resetarBarras();
                txtForcaSenha.setText("");
                break;
            case 1:
                colorirBarras(1, "#F44336");
                txtForcaSenha.setText("Senha fraca");
                txtForcaSenha.setTextColor(Color.parseColor("#F44336"));
                break;
            case 2:
                colorirBarras(2, "#FF9800");
                txtForcaSenha.setText("Senha razoável");
                txtForcaSenha.setTextColor(Color.parseColor("#FF9800"));
                break;
            case 3:
                colorirBarras(3, "#2196F3");
                txtForcaSenha.setText("Senha boa");
                txtForcaSenha.setTextColor(Color.parseColor("#2196F3"));
                break;
            case 4:
                colorirBarras(4, "#4CAF50");
                txtForcaSenha.setText("Senha forte");
                txtForcaSenha.setTextColor(Color.parseColor("#4CAF50"));
                break;
        }
    }

    private int calcularForca(String senha) {
        if (senha.isEmpty()) return 0;
        int pontos = 0;
        if (senha.length() >= 6)                        pontos++; // tamanho mínimo
        if (senha.matches(".*[A-Z].*"))                 pontos++; // maiúscula
        if (senha.matches(".*[0-9].*"))                 pontos++; // número
        if (senha.matches(".*[!@#$%^&*()_+\\-=].*"))   pontos++; // especial
        return pontos;
    }

    private void colorirBarras(int quantidade, String cor) {
        resetarBarras();
        View[] barras = {bar1, bar2, bar3, bar4};
        for (int i = 0; i < quantidade; i++) {
            barras[i].setBackgroundColor(Color.parseColor(cor));
        }
    }

    private void resetarBarras() {
        bar1.setBackgroundColor(Color.parseColor("#DDDDDD"));
        bar2.setBackgroundColor(Color.parseColor("#DDDDDD"));
        bar3.setBackgroundColor(Color.parseColor("#DDDDDD"));
        bar4.setBackgroundColor(Color.parseColor("#DDDDDD"));
    }

    private void salvarSenha() {
        String senhaAtual    = edtSenhaAtual.getText().toString();
        String novaSenha     = edtNovaSenha.getText().toString();
        String confirmarSenha = edtConfirmarSenha.getText().toString();

        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!novaSenha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }
        if (novaSenha.length() < 6) {
            Toast.makeText(this, "A nova senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credencial = EmailAuthProvider.getCredential(
                user.getEmail(), senhaAtual);

        // Reautentica para confirmar a senha atual
        user.reauthenticate(credencial)
                .addOnSuccessListener(unused ->
                        user.updatePassword(novaSenha)
                                .addOnSuccessListener(unused2 -> {
                                    Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                )
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Senha atual incorreta", Toast.LENGTH_SHORT).show()
                );
    }
}