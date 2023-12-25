package componentes.modelos.relacoes;

import auxiliares.GerenciadorDeRecursos;

public enum DirecaoDeRelacao {
    NENHUMA(GerenciadorDeRecursos.getInstancia().getString("relacao_direcao_nenhuma")),
    A_ATE_B(GerenciadorDeRecursos.getInstancia().getString("relacao_direcao_a_ate_b")),
    B_ATE_A(GerenciadorDeRecursos.getInstancia().getString("relacao_direcao_b_ate_a"));

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
