package componentes.alteracoes;

import componentes.estruturas.ComponenteUML;

import java.awt.*;

/**
 * Classe que indica uma alteração no diagrama sob a forma de um componente sendo movido.
 * Guarda informações com relação a posição antes e depois do componente ter sido movimentado.
 */

public class ComponenteMovido implements AlteracaoDeComponenteUML {
    private final Point posicaoAntesDoMovimento;
    private final Point posicaoDepoisDoMovimento;
    private final ComponenteUML<?> componenteMovido;

    public ComponenteMovido(Point posicaoAntesDoMovimento, Point posicaoDepoisDoMovimento, ComponenteUML<?> componenteMovido) {
        this.posicaoAntesDoMovimento = posicaoAntesDoMovimento;
        this.posicaoDepoisDoMovimento = posicaoDepoisDoMovimento;
        this.componenteMovido = componenteMovido;
    }

    @Override
    public void desfazerAlteracao() {
        componenteMovido.getPainelComponente().setLocation(posicaoAntesDoMovimento);
    }

    @Override
    public void refazerAlteracao() {
        componenteMovido.getPainelComponente().setLocation(posicaoDepoisDoMovimento);
    }
}
