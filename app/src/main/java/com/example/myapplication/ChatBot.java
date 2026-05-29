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

public class ChatBot extends AppCompatActivity {

    private RecyclerView recyclerMensagens;
    private EditText edtMensagem;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private List<Mensagem> listaMensagens = new ArrayList<>();

    // Gemini IA
    private GeminiManager geminiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        geminiManager = new GeminiManager();

        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        edtMensagem = findViewById(R.id.edtMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);

        recyclerMensagens.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(listaMensagens);
        recyclerMensagens.setAdapter(chatAdapter);

        // Mensagens iniciais do bot
        adicionarMensagemBot("🎉 Olá! Sou o Corretor IA. Cole sua redação abaixo e vou analisar com base nos critérios do ENEM! 📝");

        adicionarMensagemBot("Vou avaliar: Competência 1 (gramática), Competência 2 (tema), Competência 3 (argumentação), Competência 4 (coesão) e Competência 5 (proposta de intervenção). 🎯");

        btnEnviar.setOnClickListener(v -> enviarMensagem());

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    // ============================================================
    // ENVIO DE MENSAGEM E CHAMADA DA IA
    // ============================================================

    private void enviarMensagem() {

        String textoUsuario = edtMensagem.getText().toString().trim();

        if (textoUsuario.isEmpty()) return;

        // Adiciona mensagem do usuário
        adicionarMensagemUsuario(textoUsuario);

        edtMensagem.setText("");

        // Mostra digitando...
        adicionarMensagemBot("✍️ Corrigindo sua redação...");

        int posicaoDigitando = listaMensagens.size() - 1;

        // ============================================================
        // PROMPT DA IA
        // ============================================================

        String prompt =
                "Você é um corretor especialista do ENEM. " +
                        "Analise a redação abaixo e dê um feedback detalhado seguindo EXATAMENTE as 5 competências do ENEM.\n\n" +

                        "REDAÇÃO DO ALUNO:\n" +
                        textoUsuario +

                        "\n\nForneça o feedback no seguinte formato:\n\n" +

                        "📊 NOTA ESTIMADA: X/1000\n\n" +

                        "✅ COMPETÊNCIA 1 - Domínio da norma culta (0-200)\n" +
                        "Nota: X/200\n" +
                        "Análise: Avalie gramática, ortografia e pontuação.\n\n" +

                        "🎯 COMPETÊNCIA 2 - Compreensão do tema (0-200)\n" +
                        "Nota: X/200\n" +
                        "Análise: Avalie se o aluno entendeu e desenvolveu o tema.\n\n" +

                        "💡 COMPETÊNCIA 3 - Capacidade de argumentação (0-200)\n" +
                        "Nota: X/200\n" +
                        "Análise: Avalie a qualidade dos argumentos.\n\n" +

                        "🔗 COMPETÊNCIA 4 - Coesão e coerência (0-200)\n" +
                        "Nota: X/200\n" +
                        "Análise: Avalie conectivos e progressão textual.\n\n" +

                        "🌍 COMPETÊNCIA 5 - Proposta de intervenção (0-200)\n" +
                        "Nota: X/200\n" +
                        "Análise: Avalie ação, agente, modo, finalidade e detalhamento.\n\n" +

                        "💬 FEEDBACK GERAL:\n" +
                        "Dê um parecer geral motivador e construtivo com sugestões de melhoria.";

        // ============================================================
        // CHAMADA DO GEMINI
        // ============================================================

        geminiManager.corrigirRedacao(prompt)
                .addListener(() -> {

                    try {

                        String resposta = geminiManager
                                .corrigirRedacao(prompt)
                                .get()
                                .getText();
                        runOnUiThread(() -> {

                            // Remove mensagem digitando
                            listaMensagens.remove(posicaoDigitando);
                            chatAdapter.notifyItemRemoved(posicaoDigitando);

                            adicionarMensagemBot(
                                    resposta != null
                                            ? resposta
                                            : " Não consegui corrigir sua redação."
                            );

                            recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
                        });

                    } catch (Exception e) {

                        runOnUiThread(() -> {

                            listaMensagens.remove(posicaoDigitando);
                            chatAdapter.notifyItemRemoved(posicaoDigitando);

                            adicionarMensagemBot(
                                    "❌ Erro ao processar redação:\n" +
                                            e.getMessage()
                            );
                        });
                    }

                }, Runnable::run);
    }

    // ============================================================
    // MENSAGEM BOT
    // ============================================================

    private void adicionarMensagemBot(String texto) {

        String hora = new SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
        ).format(new Date());

        listaMensagens.add(
                new Mensagem(texto, hora, false)
        );

        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);

        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }

    // ============================================================
    // MENSAGEM USUÁRIO
    // ============================================================

    private void adicionarMensagemUsuario(String texto) {

        String hora = new SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
        ).format(new Date());

        listaMensagens.add(
                new Mensagem(texto, hora, true)
        );

        chatAdapter.notifyItemInserted(listaMensagens.size() - 1);

        recyclerMensagens.scrollToPosition(listaMensagens.size() - 1);
    }
}