package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity
{

    // 1) atributos
    ImageView btnConteudoMat, btnConteudoPort, btnRedacao, btnQuestMat, btnQuestPort;
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;
    TextView txtPontos, txtStreak, lblNomeUsuario;
    Button btnLogout;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // inicializa o sistema de gamificacao
        Gamificacao g = new Gamificacao(this);
        //verifica se o usuario entrou hj e atualiza o streak + pontos
        g.verificarLoginDiario();
        // começa a contar o tempo
        g.registrarEntrada();

        // 2) linkando os elementos
        btnConteudoMat = (ImageView) findViewById(R.id.btnConteudoMat);
        btnConteudoPort = (ImageView) findViewById(R.id.btnConteudoPort);
        btnRedacao = (ImageView) findViewById(R.id.btnRedacao);
        btnQuestMat = (ImageView) findViewById(R.id.btnQuestMat);
        btnQuestPort = (ImageView) findViewById(R.id.btnQuestPort);

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnDesempenho = (ImageButton) findViewById(R.id.btnDesempenho);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
        btnPerfil = (ImageButton) findViewById(R.id.btnPerfil);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        txtPontos = (TextView) findViewById(R.id.txtPontos);
        txtStreak = (TextView) findViewById(R.id.txtStreak);

        lblNomeUsuario = findViewById(R.id.lblNomeUsuario);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Busca nome do usuário para a mensagem de Bem Vindo
        buscarNomeUsuario();

        // mostrar a gamificacao
        txtPontos.setText("⭐ " + g.getPontos() + " pts");
        txtStreak.setText("🔥 " + g.getStreak() + " dias");

        // eventos dos botoes de conteudo
        btnConteudoMat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, ConteudoMat.class);
                startActivity(it);
            }
        });

        btnLogout.setOnClickListener(v ->
        {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(Home.this, TelaLogin.class));
            finish();
        });

        btnConteudoPort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, ConteudoPort.class);
                startActivity(it);
            }
        });

        btnRedacao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, ConteudoRed.class);
                startActivity(it);
            }
        });

        btnQuestMat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaQuestoesMat.class);
                startActivity(it);
            }
        });

        btnQuestPort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaQuestoesPort.class);
                startActivity(it);
            }
        });



        btnDesempenho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, Desempenho.class);
                startActivity(it);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, Buscar.class);
                startActivity(it);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, PerfilUsuario.class);
                startActivity(it);
            }
        });

    }

    private void buscarNomeUsuario()
    {
        if (mAuth.getCurrentUser() == null)
        {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Home.this, TelaLogin.class));
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(uid).get().addOnSuccessListener(document ->
        {
            if (document.exists())
            {
                String nome = document.getString("nome");
                lblNomeUsuario.setText(nome);
            }

            else
            {
                Toast.makeText(this, "Dados do usuário não encontrados", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
        {
            Toast.makeText(this, "Erro ao buscar usuário", Toast.LENGTH_SHORT).show();
        });
    }
}