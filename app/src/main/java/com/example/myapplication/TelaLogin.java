package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class TelaLogin extends AppCompatActivity {
    // 1) atributos
    ImageButton btnVoltar;
    EditText txtEmail, txtSenha;
    FirebaseAuth mAuth;
    Button btnEntrar;
    TextView lblEsqueceuSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        // 2) linkando os elementos

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(this, Home.class));
            finish();
            return;
        }

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        lblEsqueceuSenha = (TextView) findViewById(R.id.lblEsqueceuSenha);

        btnEntrar.setOnClickListener(v -> loginUsuario());

        //3) evento do btnVoltar
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaLogin.this, MainActivity.class);
                startActivity(it);
            }
        });

        // 4) evento do esqueceu senha
        lblEsqueceuSenha.setOnClickListener(v ->
        {
            Intent intent = new Intent(TelaLogin.this, TelaRecuperarSenhaEmail.class) ;
            startActivity(intent);
        });

    }

    private void loginUsuario()
    {
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

        // Validação dos campos vazios
        if (email.isEmpty())
        {
            txtEmail.setError("Informe o e-mail");
            txtEmail.requestFocus();
            return;
        }

        if (senha.isEmpty())
        {
            txtSenha.setError("Informe a senha");
            txtSenha.requestFocus();
            return;
        }

        //Autenticação no Firebase
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TelaLogin.this, Home.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // impede voltar ao login
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "Erro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
