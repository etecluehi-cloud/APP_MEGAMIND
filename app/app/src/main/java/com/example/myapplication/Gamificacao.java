package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Gamificacao
{
    private SharedPreferences prefs; // prefs lê os dados
    private SharedPreferences.Editor editor; // escreve/salva os dados

    //construtor - inicializa a classe
    public Gamificacao(Context context)
    {
        //cria/acessa o banco de dados
        prefs = context.getSharedPreferences("gamificacao",
                Context.MODE_PRIVATE);

        //permite definir valor (putInt), e salvar no cel (apply)
        editor = prefs.edit();
    }


    // pontos!!!
    // adicionar pontos
    public void adicionarPontos (int pontosGanhos)
    {
        int pontos = prefs.getInt("pontos", 0); // pega pontos atuais
        pontos += pontosGanhos; // soma

        editor.putInt("pontos", pontos);
        editor.apply();
    }


    // CONTROLE DE TEMPO DO APP
    // salva o momento que o usuário entrou no app
    public void registrarEntrada()
    {
        long tempoEntrada = System.currentTimeMillis();
        editor.putLong("tempo_entrada", tempoEntrada);
        editor.apply();
    }

    // verifica se ficou pelo menos 20 minutos
    public boolean tempoMinimoAtingido()
    {
        long tempoEntrada = prefs.getLong("tempo_entrada", 0);
        long agora = System.currentTimeMillis();

        long diferenca = agora - tempoEntrada;

        long tempoMinimo = 20 * 60 * 1000; // 20 minutos em milissegundos

        return diferenca >= tempoMinimo;
    }


    //pegar pontos
    public int getPontos()
    {
        return prefs.getInt("pontos", 0);
    }

    // STREAK!!!! (dias seguidos)
    public void  verificarLoginDiario()
    {
        long hoje = System.currentTimeMillis(); // tempo atual
        long ultimoLogin = prefs.getLong("ultimo_login", 0);

        long um_dia = 86400000; // 1 dia em milissegundos
        long diferenca = hoje - ultimoLogin;

        int streak = prefs.getInt("streak", 0);
        int melhorStreak = prefs.getInt("melhor_streak", 0);

        if (ultimoLogin == 0)
        //primeira vez no app
        {
            streak = 1;
        }
        else if (diferenca < um_dia)
        // ja entrou hoje -> nao faz nada
        {
            return;
        }
        else if (diferenca < um_dia * 4)
        // entrou no dia seguinte -> aumenta streak
        {
            streak++;
        }
        else
        //ficou dias sem entrar -> reinicia
        {
            streak = 1;
        }

        //atualiza melhor streak
        if (streak > melhorStreak)
        {
            melhorStreak = streak;
            editor.putInt("melhor_streak", melhorStreak);
        }

        //salva dados
        editor.putInt("streak", streak);
        editor.putLong("ultimo_login", hoje);

        // ganha ponto por entrar
        adicionarPontos(1);

        editor.apply();
    }

    //pegar streak atual
    public int getStreak()
    {
        return prefs.getInt("streak", 0);
    }

    //pegar melhor streak
    public int getMelhorStreak()
    {
        return prefs.getInt("melhor_streak", 0);
    }

    // QUESTOES
    public void responderQuestao(boolean acertou)
    {
        //só por responder
        adicionarPontos(2);

        if (acertou)
        {
            //bonus por acerto
            adicionarPontos(3);
        }
    }

    //desempenho por materia
    public void adicionarAcerto(String materia)
    {
        int acertos = prefs.getInt("acertos_" + materia, 0);
        acertos++;

        editor.putInt("acertos_" + materia, acertos);
        editor.apply();
    }

    public int getAcertos(String materia)
    {
        return prefs.getInt("acertos_" + materia, 0);
    }

    //nível
    public String getNivel()
    {
        int pontos = getPontos();
        if (pontos < 50) return "Iniciante";
        else if (pontos < 150) return "Dedicado";
        else if (pontos < 300) return "Focado";
        else return "Avançado";
    }

}
