package com.example.myapplication;

public class Meta {

    private String id;
    private String texto;
    private boolean concluida;

    public Meta(String id, String texto, boolean concluida) {
        this.id        = id;
        this.texto     = texto;
        this.concluida = concluida;
    }

    public String getId()        { return id; }
    public String getTexto()     { return texto; }
    public boolean isConcluida() { return concluida; }
}