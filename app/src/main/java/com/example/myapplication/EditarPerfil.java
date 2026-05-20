package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {

    ImageButton btnVoltar;
    LinearLayout lnlAlterarFoto, lnlEditarNome, lnlEditarSenha, lnlExcluirConta;
    TextView txtNomeAtual, txtEmailAtual, txtIniciais, txtNomeFoto;
    ImageView imgFotoPerfil;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userId;

    // =========================
    // GALERIA
    // =========================

    ActivityResultLauncher<Intent> galeriaLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),

                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Uri imagemSelecionada =
                                    result.getData().getData();

                            abrirCrop(imagemSelecionada);
                        }
                    }
            );

    // =========================
    // CROP
    // =========================

    ActivityResultLauncher<Intent> cropLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),

                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            Uri imagemCropada =
                                    UCrop.getOutput(result.getData());

                            if (imagemCropada != null) {

                                // mostra imagem imediatamente

                                Glide.with(this)
                                        .load(imagemCropada)
                                        .circleCrop()
                                        .into(imgFotoPerfil);

                                imgFotoPerfil.setVisibility(View.VISIBLE);
                                txtIniciais.setVisibility(View.GONE);

                                // SALVA URI NO FIRESTORE
                                salvarFotoLocal(imagemCropada);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);

        lnlAlterarFoto = (LinearLayout) findViewById(R.id.lnlAlterarFoto);
        lnlEditarNome = (LinearLayout) findViewById(R.id.lnlEditarNome);
        lnlEditarSenha = (LinearLayout) findViewById(R.id.lnlEditarSenha);
        lnlExcluirConta = (LinearLayout) findViewById(R.id.lnlExcluirConta);

        txtNomeAtual = (TextView) findViewById(R.id.txtNomeAtual);
        txtEmailAtual = (TextView) findViewById(R.id.txtEmailAtual);
        txtIniciais = (TextView) findViewById(R.id.txtIniciais);
        txtNomeFoto = (TextView) findViewById(R.id.txtNomeFoto);

        imgFotoPerfil = (ImageView) findViewById(R.id.imgFotoPerfil);

        btnVoltar.setOnClickListener(v -> finish());

        carregarDados();

        // alterar foto

        lnlAlterarFoto.setOnClickListener(v ->
                solicitarPermissaoGaleria());

        // editar nome

        lnlEditarNome.setOnClickListener(v ->
                dialogEditarNome());

        // editar senha

        lnlEditarSenha.setOnClickListener(v -> {

            Intent it =
                    new Intent(
                            EditarPerfil.this,
                            AlterarSenha.class
                    );

            startActivity(it);
        });

        // excluir conta

        lnlExcluirConta.setOnClickListener(v ->
                dialogExcluirConta());
    }

    // =====================================
    // PERMISSÃO
    // =====================================

    private void solicitarPermissaoGaleria() {

        String permissao;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            permissao = Manifest.permission.READ_MEDIA_IMAGES;

        } else {

            permissao = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(
                this,
                permissao
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permissao},
                    100
            );

        } else {

            abrirGaleria();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == 100) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                abrirGaleria();

            } else {

                Toast.makeText(
                        this,
                        "Permissão negada",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    // =====================================
    // ABRIR GALERIA
    // =====================================

    private void abrirGaleria() {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        galeriaLauncher.launch(intent);
    }

    // =====================================
    // UCROP
    // =====================================

    private void abrirCrop(Uri imagemOriginal) {

        Uri destino = Uri.fromFile(
                new File(
                        getCacheDir(),
                        "foto_perfil_" + userId + ".jpg"
                )
        );

        UCrop.Options opcoes = new UCrop.Options();

        opcoes.setCircleDimmedLayer(true);
        opcoes.setShowCropFrame(false);
        opcoes.setShowCropGrid(false);

        opcoes.setToolbarColor(0xFF7B2FBE);
        opcoes.setStatusBarColor(0xFF7B2FBE);

        opcoes.setToolbarTitle("Ajustar foto");

        opcoes.setCompressionFormat(
                Bitmap.CompressFormat.JPEG
        );

        opcoes.setCompressionQuality(90);

        Intent cropIntent = UCrop.of(
                        imagemOriginal,
                        destino
                )
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .withOptions(opcoes)
                .getIntent(this);

        cropLauncher.launch(cropIntent);
    }

    // =====================================
    // SALVAR FOTO LOCAL (SEM STORAGE)
    // =====================================

    private void salvarFotoLocal(Uri imagemCropada) {

        String caminhoImagem =
                imagemCropada.toString();

        db.collection("usuarios")
                .document(userId)
                .update("fotoPerfil", caminhoImagem)

                .addOnSuccessListener(unused ->

                        Toast.makeText(
                                this,
                                "Foto atualizada!",
                                Toast.LENGTH_SHORT
                        ).show()
                )

                .addOnFailureListener(e ->

                        Toast.makeText(
                                this,
                                "Erro: " + e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    // =====================================
    // CARREGAR DADOS
    // =====================================

    private void carregarDados() {

        db.collection("usuarios")
                .document(userId)
                .get()

                .addOnSuccessListener(document -> {

                    if (document.exists()) {

                        String nome =
                                document.getString("nome");

                        String email =
                                document.getString("email");

                        String foto =
                                document.getString("fotoPerfil");

                        txtNomeAtual.setText(nome);
                        txtEmailAtual.setText(email);
                        txtNomeFoto.setText(nome);

                        // iniciais

                        if (nome != null && !nome.isEmpty()) {

                            String[] partes =
                                    nome.trim().split(" ");

                            String iniciais =
                                    String.valueOf(
                                            partes[0].charAt(0)
                                    );

                            if (partes.length > 1) {

                                iniciais += String.valueOf(
                                        partes[
                                                partes.length - 1
                                                ].charAt(0)
                                );
                            }

                            txtIniciais.setText(
                                    iniciais.toUpperCase()
                            );
                        }

                        // carregar foto

                        if (foto != null && !foto.isEmpty()) {

                            Glide.with(this)
                                    .load(foto)
                                    .circleCrop()
                                    .into(imgFotoPerfil);

                            imgFotoPerfil.setVisibility(View.VISIBLE);

                            txtIniciais.setVisibility(View.GONE);
                        }
                    }
                });
    }

    // =====================================
    // EDITAR NOME
    // =====================================

    private void dialogEditarNome() {

        EditText input = new EditText(this);

        input.setHint("Novo nome");

        input.setText(txtNomeAtual.getText());

        input.setSingleLine(true);

        new AlertDialog.Builder(this)

                .setTitle("Editar nome")

                .setView(input)

                .setPositiveButton("Salvar",
                        (dialog, which) -> {

                            String novoNome =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (novoNome.isEmpty()) {

                                Toast.makeText(
                                        this,
                                        "Nome vazio",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            salvarNome(novoNome);
                        })

                .setNegativeButton("Cancelar", null)

                .show();
    }

    private void salvarNome(String novoNome) {

        Map<String, Object> dados =
                new HashMap<>();

        dados.put("nome", novoNome);

        db.collection("usuarios")
                .document(userId)
                .update(dados)

                .addOnSuccessListener(unused -> {

                    txtNomeAtual.setText(novoNome);

                    txtNomeFoto.setText(novoNome);

                    Toast.makeText(
                            this,
                            "Nome atualizado!",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    // =====================================
    // EXCLUIR CONTA
    // =====================================

    private void dialogExcluirConta() {

        new AlertDialog.Builder(this)

                .setTitle("Excluir conta")

                .setMessage(
                        "Tem certeza que deseja excluir sua conta?"
                )

                .setPositiveButton(
                        "Excluir",
                        (dialog, which) -> excluirConta()
                )

                .setNegativeButton("Cancelar", null)

                .show();
    }

    private void excluirConta() {

        db.collection("usuarios")
                .document(userId)
                .delete()

                .addOnSuccessListener(unused -> {

                    mAuth.getCurrentUser()
                            .delete()

                            .addOnSuccessListener(unused2 -> {

                                Toast.makeText(
                                        this,
                                        "Conta excluída",
                                        Toast.LENGTH_SHORT
                                ).show();

                                Intent it =
                                        new Intent(
                                                this,
                                                MainActivity.class
                                        );

                                it.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                );

                                startActivity(it);

                                finish();
                            });
                });
    }
}