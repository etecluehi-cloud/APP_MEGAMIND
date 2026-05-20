package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.functions.FirebaseFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatBot extends AppCompatActivity {

    private RecyclerView recyclerMensagens;
    private EditText edtMensagem;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private List<Mensagem> listaMensagens = new ArrayList<>();

    // Firebase Functions (que chama a IA)
    private FirebaseFunctions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        functions = FirebaseFunctions.getInstance();

        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        edtMensagem = findViewById(R.id.edtMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);

        recyclerMensagens.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(listaMensagens);
        recyclerMensagens.setAdapter(chatAdapter);

        // Mensagens iniciais do bot (apresentação)
        adicionarMensagemBot("🎉 Olá! Sou o Corretor IA. Cole sua redação abaixo e vou analisar com base nos critérios do ENEM! 📝");
        adicionarMensagemBot("Vou avaliar: **Competência 1** (gramática), **C2** (tema), **C3** (argumentação), **C4** (coesão) e **C5** (proposta de intervenção). 🎯");

        btnEnviar.setOnClickListener(v -> enviarMensagem());

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    // ============================================================
    // ENVIO DE MENSAGEM E CHAMADA DA IA
    // ============================================================
    private void enviarMensagem() {
        String textoUsuario = edtMensagem.getText().toString().trim();
        if (textoUsuario.isEmpty()) return;

        // Adiciona mensagem do usuário na tela
        adicionarMensagemUsuario(textoUsuario);
        edtMensagem.setText("");

        // Mostra indicador "digitando..."
        adicionarMensagemBot("...");
        int posicaoDigitando = listaMensagens.size() - 1;

        // Monta o objeto para enviar à Firebase Function
        Map<String, Object> dados = new HashMap<>();
        dados.put("redacao", textoUsuario);

        // Chama a Firebase Cloud Function chamada "corrigirRedacao"
        functions
                .getHttpsCallable("corrigirRedacao")
                .call(dados)
                .addOnSuccessListener(result -> {
                    // Remove o "..."
                    listaMensagens.remove(posicaoDigitando);
                    chatAdapter.notifyItemRemoved(posicaoDigitando);

                    // Pega a resposta da IA
                    String resposta = (String) result.getData();
                    adicionarMensagemBot(resposta != null ? resposta : "Não consegui processar sua redação.");

                    // Rola para o fim
                    recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
                })
                .addOnFailureListener(e -> {
                    listaMensagens.remove(posicaoDigitando);
                    chatAdapter.notifyItemRemoved(posicaoDigitando);
                    adicionarMensagemBot("❌ Erro ao processar: " + e.getLocalizedMessage());
                });
    }

    private void adicionarMensagemBot(String texto) {
        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        listaMensagens.add(new Mensagem(texto, hora, false)); // false = mensagem do bot
        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);
        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }

    private void adicionarMensagemUsuario(String texto) {
        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        listaMensagens.add(new Mensagem(texto, hora, true)); // true = mensagem do usuário
        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);
        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }
}



