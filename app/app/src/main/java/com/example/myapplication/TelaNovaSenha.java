package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TelaNovaSenha extends AppCompatActivity
{
    // 1) atributos
    ImageButton btnVoltar;
    EditText txtNovaSenha, txtConfirmarNovaSenha;
    Button btnConfirmar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_nova_senha);

        // 2) Linkando os elementos
        txtConfirmarNovaSenha = (EditText) findViewById(R.id.txtConfirmarNovaSenha);
        txtNovaSenha = (EditText) findViewById(R.id.txtNovaSenha);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        mAuth = FirebaseAuth.getInstance();

        // Evento button Confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String novaSenha = txtNovaSenha.getText().toString();
                String confirmarSenha = txtConfirmarNovaSenha.getText().toString();


                if (novaSenha.isEmpty() || confirmarSenha.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),
                            "Preencha todos os campos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(!novaSenha.equals(confirmarSenha))
                {
                    Toast.makeText(getApplicationContext(),
                            "As senhas não coincidem",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null)
                {
                    user.updatePassword(novaSenha)
                            .addOnCompleteListener(task ->
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Senhas atualizada com sucesso",
                                            Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Erro ao atualizar senha",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        //3) evento do btnVoltar
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(TelaNovaSenha.this, TelaCodigoSenha.class);
                startActivity(it);
            }
        });
    }
}