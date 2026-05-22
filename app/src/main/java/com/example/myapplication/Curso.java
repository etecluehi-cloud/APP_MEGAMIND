package com.example.myapplication;

// classe que representa um curso/matéria do app
public class Curso
{

    // atributos privados - só acessados pelos getters e setters
    private String nome; // nome do conteúdo
    private String conteudoId;
    private boolean visto;

    // Construtor - usado para criar um novo objeto Curso com os dados
    public Curso(String nome,
                 String conteudoId,
                 boolean visto)
    {
        this.nome = nome;
        this.conteudoId = conteudoId;
        this.visto = visto;
    }

    // getters -> permitem ler os valores de fora da classe
    public String getNome()
        {
            return nome;
        }

    public String getConteudoId()
    {
        return conteudoId;
    }

    public boolean isVisto()
    {
        return visto;
    }

    public void setVisto(boolean visto)
    {
        this.visto = visto;
    }
}