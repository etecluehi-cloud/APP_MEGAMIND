package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Questao
{
    private String enunciado;
    private String alternativa_a;
    private String alternativa_b;
    private String alternativa_c;
    private String alternativa_d;
    private String alternativa_e;
    private String resposta_correta;

    // Construtor vazio obrigatório para o Firebase funcionar
    public Questao() {}

    public Questao(String enunciado, String alternativa_a, String alternativa_b,
                   String alternativa_c, String alternativa_d, String alternativa_e,
                   String resposta_correta) {
        this.enunciado = enunciado;
        this.alternativa_a = alternativa_a;
        this.alternativa_b = alternativa_b;
        this.alternativa_c = alternativa_c;
        this.alternativa_d = alternativa_d;
        this.alternativa_e = alternativa_e;
        this.resposta_correta = resposta_correta;
    }

    // Getters — o Firebase precisa deles para ler os dados
    public String getEnunciado() { return enunciado; }
    public String getAlternativa_a() { return alternativa_a; }
    public String getAlternativa_b() { return alternativa_b; }
    public String getAlternativa_c() { return alternativa_c; }
    public String getAlternativa_d() { return alternativa_d; }
    public String getAlternativa_e() { return alternativa_e; }
    public String getResposta_correta() { return resposta_correta; }

}