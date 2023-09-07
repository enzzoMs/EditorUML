package modelos;

import auxiliares.GerenciadorDeRecursos;

import java.util.Objects;

public enum Visibilidade {
    PUBLICO("+", GerenciadorDeRecursos.getInstancia().getString("publico")),
    PRIVADO("-", GerenciadorDeRecursos.getInstancia().getString("privado")),
    PROTEGIDO("#", GerenciadorDeRecursos.getInstancia().getString("protegido"));

    private final String simbolo;
    private final String nome;

    Visibilidade(String simbolo, String nome) {
        this.simbolo = simbolo;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public static Visibilidade getVisibilidadePorNome(String nome) {
        for (Visibilidade visibilidade : Visibilidade.values()) {
            if (Objects.equals(visibilidade.getNome(), nome)) return visibilidade;
        }

        return null;
    }
}