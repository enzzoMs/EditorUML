package componentes.modelos.relacoes;

import auxiliares.GerenciadorDeRecursos;

public enum DirecaoDeRelacao {
    NENHUMA(GerenciadorDeRecursos.getInstancia().getString("nenhuma")),
    A_ATE_B(GerenciadorDeRecursos.getInstancia().getString("direcao_a_ate_b")),
    B_ATE_A(GerenciadorDeRecursos.getInstancia().getString("direcao_b_ate_a"));

    private final String nome;

    DirecaoDeRelacao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static DirecaoDeRelacao getDirecaoPorNome(String nome) {
        if (nome == null) {
            return null;
        } else if (nome.equals(A_ATE_B.nome)) {
            return A_ATE_B;
        } else if (nome.equals(B_ATE_A.nome)) {
            return B_ATE_A;
        } else if (nome.equals(NENHUMA.nome)) {
            return NENHUMA;
        } else {
            return null;
        }
    }
}
