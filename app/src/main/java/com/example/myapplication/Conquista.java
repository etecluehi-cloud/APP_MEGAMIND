package com.example.myapplication;

public class Conquista {

    private String titulo;
    private boolean desbloqueada;

    public Conquista(String titulo, boolean desbloqueada) {
        this.titulo       = titulo;
        this.desbloqueada = desbloqueada;
    }

    public String getTitulo()        { return titulo; }
    public boolean isDesbloqueada()  { return desbloqueada; }
}