package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class PerfilUsuario extends AppCompatActivity
{

    // 1) atributos
    ImageButton btnHome, btnDesempenho, btnBuscar, btnPerfil;
    ImageView imgPerfil;
    TextView txtNome, txtEmail, txtPontos, txtDias;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout lnlSairConta, lnlSobreNos, lnlMetaDiaria;
    Switch switchModoEscuro;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // 2) linkando elementos
        lnlSairConta = (LinearLayout) findViewById(R.id.lnlSairConta);
        lnlSobreNos = (LinearLayout) findViewById(R.id.lnlSobreNos);
        lnlMetaDiaria = (LinearLayout) findViewById(R.id.lnlMetaDiaria);

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtNome = (TextView) findViewById(R.id.txtNome);
        txtPontos = (TextView) findViewById(R.id.txtPontos);
        txtDias = (TextView) findViewById(R.id.txtDias);

        imgPerfil = (ImageView) findViewById(R.id.imgPerfil);

        switchModoEscuro = (Switch) findViewById(R.id.switchModoEscuro);

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnDesempenho = (ImageButton) findViewById(R.id.btnDesempenho);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
        btnPerfil = (ImageButton) findViewById(R.id.btnPerfil);

        String userId = mAuth.getCurrentUser().getUid();

        boolean modoEscuroSalvo = getSharedPreferences("config", MODE_PRIVATE)
                .getBoolean("modo_escuro", false);

        switchModoEscuro.setChecked(modoEscuroSalvo);

        if (modoEscuroSalvo) {
            switchModoEscuro.getTrackDrawable().setTint(Color.parseColor("#4CAF50"));
        } else {
            switchModoEscuro.getTrackDrawable().setTint(Color.parseColor("#E53935"));
        }

        // Quando o usuário mexe no switch
        switchModoEscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Salva a preferência
            getSharedPreferences("config", MODE_PRIVATE)
                    .edit()
                    .putBoolean("modo_escuro", isChecked)
                    .apply();

            // Aplica o tema
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Aguarda 400ms antes de recriar — evita piscar se mexer rápido
            switchModoEscuro.postDelayed(() -> recreate(), 400);
        });

        Switch switchNotificacoes = findViewById(R.id.switchNotifica);

        boolean notificacoesAtivas = getSharedPreferences("config", MODE_PRIVATE)
                .getBoolean("notificacoes", false);
        switchNotificacoes.setChecked(notificacoesAtivas);

        if (notificacoesAtivas) {
            switchNotificacoes.getTrackDrawable().setTint(Color.parseColor("#4CAF50"));
        } else {
            switchNotificacoes.getTrackDrawable().setTint(Color.parseColor("#E53935"));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        switchNotificacoes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("config", MODE_PRIVATE)
                    .edit()
                    .putBoolean("notificacoes", isChecked)
                    .apply();

            // Aguarda 400ms antes de recriar
            switchNotificacoes.postDelayed(() -> recreate(), 400);
        });

        // Contagem da Gameficação
        Gamificacao g = new Gamificacao(this);
        txtPontos.setText("⭐ " + g.getPontos() + " pts");
        txtDias.setText("🔥 " + g.getStreak() + " dias");

        db.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()){
                        txtNome.setText(document.getString("nome"));
                        txtEmail.setText(document.getString("email"));

                        // FOTO
                        String foto = document.getString("fotoPerfil");

                        if (foto != null && !foto.isEmpty()) {

                            Glide.with(this)
                                    .load(foto)
                                    .circleCrop()
                                    .into(imgPerfil);
                        }
                    }
                });

        // Butaoo EditarPerfil
        findViewById(R.id.btnEditar).setOnClickListener(v -> {
            startActivity(new Intent(this, EditarPerfil.class));
        });

        // Butao de sair da conta
        lnlSairConta.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Sair da conta")
                    .setMessage("Tem certeza que deseja sair?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        Intent it = new Intent(PerfilUsuario.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                        finish();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

        // Butao sobre nós
        lnlSobreNos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PerfilUsuario.this, SobreNos.class);
                startActivity(it);
            }
        });

        lnlMetaDiaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(PerfilUsuario.this, MetaDiaria.class);
                startActivity(it);
            }
        });

        // eventos do botoes do footer
        btnHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(PerfilUsuario.this, Home.class);
                startActivity(it);
            }
        });

        btnDesempenho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(PerfilUsuario.this, Desempenho.class);
                startActivity(it);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(PerfilUsuario.this, Curso.class);
                startActivity(it);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(PerfilUsuario.this, PerfilUsuario.class);
                startActivity(it);
            }
        });
    }

    private void configurarNotificacoes(boolean ativar) {
        if (ativar) {
            // Agenda para disparar a cada 12 horas
            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                    LembreteWorker.class,
                    12, TimeUnit.HOURS
            ).build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "lembrete_megamind",
                    ExistingPeriodicWorkPolicy.KEEP, // não reagenda se já existir
                    request
            );
        } else {
            // Cancela as notificações
            WorkManager.getInstance(this).cancelUniqueWork("lembrete_megamind");
        }
    }
}