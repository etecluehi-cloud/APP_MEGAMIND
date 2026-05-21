package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity
{
    //mudança

    // 1) atributos
    ImageView btnConteudoMat, btnConteudoPort, btnRedacao, btnQuestMat, btnQuestPort;
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;
    TextView txtPontos, txtStreak, txtNomeU;
    DrawerLayout drawer;  // controla o abrir/fechar do menu
    ImageButton btnMenu;
    NavigationView navView; // mostra oq tem no menu


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
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);


        txtPontos = (TextView) findViewById(R.id.txtPontos);
        txtStreak = (TextView) findViewById(R.id.txtStreak);
        txtNomeU = (TextView) findViewById(R.id.txtNomeU);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);

        // Salva a data de hoje como último acesso
        String hoje = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                .format(new java.util.Date());

        getSharedPreferences("config", MODE_PRIVATE)
                .edit()
                .putString("ultimo_login", hoje)
                .apply();

        // mostrar a gamificacao
        txtPontos.setText("⭐ " + g.getPontos() + " pts");
        txtStreak.setText("🔥 " + g.getStreak() + " dias");


        // eventos do botoes de conteudo
        btnConteudoMat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaSecaoConteudoMat.class);
                startActivity(it);
            }
        });

        btnConteudoPort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaSecaoConteudoPort.class);
                startActivity(it);
            }
        });

        btnRedacao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaRedacao.class);
                startActivity(it);
            }
        });

        // evento dos botoes das questoes
        btnQuestMat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Home.this, TelaSecaoQuestoesMat.class);
                startActivity(it);
            }
        });


        //evento dos botoes do footer
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
                Intent it = new Intent(Home.this, Curso.class);
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

        //evento do botao menu
        btnMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.menu_duvidas) {
                // abrir tela de dúvidas
                Intent it = new Intent(Home.this, Duvidas.class);
                startActivity(it);
            }

            else if (item.getItemId() == R.id.menu_avaliar) {
                // abrir tela ou ação de avaliar
                Intent it = new Intent(Home.this, AvaliarApp.class);
                startActivity(it);
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        if (usuario != null)
        {
            String uid = usuario.getUid();

            FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot ->
                    {
                        if (documentSnapshot.exists())
                        {
                            String nome = documentSnapshot.getString("nome");

                            txtNomeU.setText(nome);
                        }
                    });
        }
    }
}