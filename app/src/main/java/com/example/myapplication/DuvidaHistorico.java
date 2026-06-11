package com.example.myapplication;

public class DuvidaHistorico {

    private String pergunta;
    private String resposta;

    public DuvidaHistorico() {
    }

    public DuvidaHistorico(String pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
