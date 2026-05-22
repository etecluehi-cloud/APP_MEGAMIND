package com.example.myapplication;

public class Mensagem
{
    private String texto;
    private String hora;
    private boolean isUsuario; // true = usuário, false = bot

    public Mensagem(String texto, String hora, boolean isUsuario) {
        this.texto = texto;
        this.hora = hora;
        this.isUsuario = isUsuario;
    }

    public String getTexto() { return texto; }
    public String getHora() { return hora; }
    public boolean isUsuario() { return isUsuario; }
}
