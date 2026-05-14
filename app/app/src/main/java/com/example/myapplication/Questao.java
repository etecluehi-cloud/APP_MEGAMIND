package com.example.myapplication;

public class Questao {
    String pergunta;
    String alternativaA;
    String alternativaB;
    String alternativaC;
    String alternativaD;
    String respostaCorreta;

    public Questao(String pergunta, String alternativaA, String alternativaB,
                   String alternativaC, String alternativaD, String respostaCorreta) {
        this.pergunta = pergunta;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
        this.respostaCorreta = respostaCorreta;
    }
}