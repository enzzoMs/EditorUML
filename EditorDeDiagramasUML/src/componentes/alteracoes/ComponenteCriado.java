package componentes.alteracoes;

import componentes.ComponenteUML;

import java.awt.*;

/**
 * Classe que indica uma alteração no diagrama sob a forma de um componente sendo criado.
 * Guarda informações com relação a posição desse componente, implementando as ações de refazer e
 * desfazer a alteração.
 */
public class ComponenteCriado implements AlteracaoDeComponenteUML {

    private final Point posicaoDepoisDaCriacao;
    private final ComponenteUML componenteCriado;

    public ComponenteCriado(Point posicaoDepoisDaCriacao, ComponenteUML componenteCriado) {
        this.posicaoDepoisDaCriacao = posicaoDepoisDaCriacao;
        this.componenteCriado = componenteCriado;
    }

    public void desfazerAlteracao() {
        // Desfazer a criacao = remover o componente.

        componenteCriado.removerComponenteDoQuadroBranco();
    }

    public void refazerAlteracao() {
        // Coloca o componente na posicao especificada

        componenteCriado.adicionarComponenteAoQuadroBranco();
        componenteCriado.getPainelComponente().setLocation(posicaoDepoisDaCriacao.x, posicaoDepoisDaCriacao.y);
    }
}
