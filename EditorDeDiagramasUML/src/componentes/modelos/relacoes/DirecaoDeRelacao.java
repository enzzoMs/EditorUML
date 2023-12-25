package componentes.modelos.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.modelos.estruturas.Visibilidade;

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
        for (DirecaoDeRelacao direcao : DirecaoDeRelacao.values()) {
            if (direcao.nome.equalsIgnoreCase(nome)) {
                return direcao;
            }
        }

        return null;
    }
}
