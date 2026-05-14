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

public class TelaRecuperarSenhaEmail extends AppCompatActivity
{
    // 1) atributos
    ImageButton btnVoltar;
    TextView lblRecupNum;
    Button btnRecupSenhaEmail2;
    EditText txtEmailCad;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_recuperar_senha_email);

        // 3) linkando os elementos
        mAuth =FirebaseAuth.getInstance();

        txtEmailCad = findViewById(R.id.txtEmailCad);
        btnRecupSenhaEmail2 = findViewById(R.id.btnRecupSenhaEmail2);
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        lblRecupNum = (TextView) findViewById(R.id.lblRecupNum);

        // Evento do lblRecupNum
        lblRecupNum.setOnClickListener(v ->
        {
            Intent intent = new Intent(TelaRecuperarSenhaEmail.this, TelaRecuperarSenhaNumero.class) ;
            startActivity(intent);
        });

        // 4) evento do btnVoltar
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaRecuperarSenhaEmail.this, TelaLogin.class);
                startActivity(it);
            }
        });




        btnRecupSenhaEmail2.setOnClickListener(v ->
        {
            String email = txtEmailCad.getText().toString().trim();
            if (email.isEmpty())
            {
                txtEmailCad.setError("Digite seu e-mail!");
                return;
            }

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task ->
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(this, "E-mail de redefinição enviado!", Toast.LENGTH_LONG).show();
                    //Volta para o login automaticamente
                    Intent intent = new Intent(this, TelaLogin.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Erro ao enviar o e-mail", Toast.LENGTH_LONG).show();
                }
            });
        });

    }
}