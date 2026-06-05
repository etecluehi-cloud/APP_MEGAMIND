package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

public class ChatBot extends AppCompatActivity {

    private RecyclerView recyclerMensagens;
    private EditText edtMensagem;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private List<Mensagem> listaMensagens = new ArrayList<>();

    private GroqManager groqManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        groqManager = new GroqManager();

        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        edtMensagem = findViewById(R.id.edtMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);

        recyclerMensagens.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(listaMensagens);
        recyclerMensagens.setAdapter(chatAdapter);

        adicionarMensagemBot("🎉 Olá! Sou o Corretor IA. Cole sua redação abaixo e vou analisar com base nos critérios do ENEM! 📝");
        adicionarMensagemBot("Vou avaliar: Competência 1 (gramática), Competência 2 (tema), Competência 3 (argumentação), Competência 4 (coesão) e Competência 5 (proposta de intervenção). 🎯");

        btnEnviar.setOnClickListener(v -> enviarMensagem());
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    private void enviarMensagem() {

        String textoUsuario = edtMensagem.getText().toString().trim();
        if (textoUsuario.isEmpty()) return;

        adicionarMensagemUsuario(textoUsuario);
        edtMensagem.setText("");
        adicionarMensagemBot("✍️ Corrigindo sua redação...");

        int posicaoDigitando = listaMensagens.size() - 1;

        String prompt =
                "Você é um corretor especialista do ENEM. " +
                        "Analise a redação abaixo e dê um feedback detalhado seguindo EXATAMENTE as 5 competências do ENEM.\n\n" +
                        "REDAÇÃO DO ALUNO:\n" + textoUsuario +
                        "\n\nForneça o feedback no seguinte formato:\n\n" +
                        "📊 NOTA ESTIMADA: X/1000\n\n" +
                        "✅ COMPETÊNCIA 1 - Domínio da norma culta (0-200)\n" +
                        "Nota: X/200\nAnálise: Avalie gramática, ortografia e pontuação.\n\n" +
                        "🎯 COMPETÊNCIA 2 - Compreensão do tema (0-200)\n" +
                        "Nota: X/200\nAnálise: Avalie se o aluno entendeu e desenvolveu o tema.\n\n" +
                        "💡 COMPETÊNCIA 3 - Capacidade de argumentação (0-200)\n" +
                        "Nota: X/200\nAnálise: Avalie a qualidade dos argumentos.\n\n" +
                        "🔗 COMPETÊNCIA 4 - Coesão e coerência (0-200)\n" +
                        "Nota: X/200\nAnálise: Avalie conectivos e progressão textual.\n\n" +
                        "🌍 COMPETÊNCIA 5 - Proposta de intervenção (0-200)\n" +
                        "Nota: X/200\nAnálise: Avalie ação, agente, modo, finalidade e detalhamento.\n\n" +
                        "💬 FEEDBACK GERAL:\nDê um parecer geral motivador e construtivo com sugestões de melhoria.";

        new Thread(() -> {
            try {
                Future<String> future = groqManager.corrigirRedacao(prompt);
                String resposta = future.get();

                runOnUiThread(() -> {
                    listaMensagens.remove(posicaoDigitando);
                    chatAdapter.notifyItemRemoved(posicaoDigitando);
                    adicionarMensagemBot(resposta);
                    recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    listaMensagens.remove(posicaoDigitando);
                    chatAdapter.notifyItemRemoved(posicaoDigitando);
                    adicionarMensagemBot("❌ Erro: " + e.getMessage());
                });
            }
        }).start();
    }

    private void adicionarMensagemBot(String texto) {
        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        listaMensagens.add(new Mensagem(texto, hora, false));
        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);
        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }

    private void adicionarMensagemUsuario(String texto) {
        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        listaMensagens.add(new Mensagem(texto, hora, true));
        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);
        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }
}