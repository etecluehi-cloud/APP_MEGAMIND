package com.example.myapplication;

// classe que representa um curso/matéria do app
public class Curso {

    // atributos privados - só acessados pelos getters e setters
    private String nome; // nome do conteúdo
    private int totalAulas; // total de aulas desse conteúdo
    private int aulasAssistidas; // quantas aulas o usuário já assistiu

    // Construtor - usado para criar um novo objeto Curso com os dados
    public Curso(String nome, int totalAulas, int aulasAssistidas)
    {
        this.nome = nome;
        this.totalAulas = totalAulas;
        this.aulasAssistidas = aulasAssistidas;
    }

    // getters -> permitem ler os valores de fora da classe
    public String getNome()
        {
            return nome;
        }
    public int getTotalAulas()
        {
            return totalAulas;
        }
    public int getAulasAssistidas()
        {
            return aulasAssistidas;
        }

    // setter - permite atualizar as aulas assistidas
    public void setAulasAssistidas(int aulasAssistidas)
        {
            this.aulasAssistidas = aulasAssistidas;
        }
}