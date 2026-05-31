package com.example.myapplication;

public class Meta
{
    String texto;
    boolean concluida;

    public Meta(String texto, boolean concluida)
    {
        this.texto = texto;
        this.concluida = concluida;
    }

    //permite editar o texto
    public void setTexto(String texto)
    {
        this.texto = texto;
    }
}
