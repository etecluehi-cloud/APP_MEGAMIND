package com.example.myapplication;

public class ItemDesempenho {

    private String conteudoId;
    private String nome;
    private String icone;
    private int totalVideos;
    private int assistidos;

    public ItemDesempenho(String conteudoId, String nome, String icone) {
        this.conteudoId  = conteudoId;
        this.nome        = nome;
        this.icone       = icone;
        this.totalVideos = 3;
        this.assistidos  = 0;
    }

    public String getConteudoId()  { return conteudoId; }
    public String getNome()        { return nome; }
    public String getIcone()       { return icone; }
    public int getTotalVideos()    { return totalVideos; }
    public int getAssistidos()     { return assistidos; }

    public void setTotalVideos(int totalVideos) { this.totalVideos = totalVideos; }
    public void setAssistidos(int assistidos)   { this.assistidos  = assistidos; }

    public int getPorcentagem() {
        return totalVideos > 0 ? (assistidos * 100) / totalVideos : 0;
    }
}