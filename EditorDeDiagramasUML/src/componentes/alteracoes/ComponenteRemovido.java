package componentes.alteracoes;

import componentes.estruturas.ComponenteUML;

import java.awt.*;

/**
 * Classe que indica uma alteração no diagrama sob a forma de um componente sendo removido.
 * Guarda informações com relação a última posição desse componente no momento em que ele foi removido.
 */
public class ComponenteRemovido implements AlteracaoDeComponenteUML {
    private final Point posicaoAntesDaRemocao;
    private final ComponenteUML<?> componenteRemovido;

    public ComponenteRemovido(Point posicaoAntesDaRemocao, ComponenteUML<?> componenteRemovido) {
        this.posicaoAntesDaRemocao = posicaoAntesDaRemocao;
        this.componenteRemovido = componenteRemovido;
    }

    public void desfazerAlteracao() {
        componenteRemovido.adicionarComponenteAoQuadroBranco();
        componenteRemovido.getPainelComponente().setLocation(posicaoAntesDaRemocao);
    }

    public void refazerAlteracao() {
       componenteRemovido.removerComponenteDoQuadroBranco();
    }
}
