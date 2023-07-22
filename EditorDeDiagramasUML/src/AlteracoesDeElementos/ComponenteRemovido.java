package AlteracoesDeElementos;

import ComponentesUML.ComponenteUML;

public class ComponenteRemovido implements AlteracaoDeElementosUML {
    /*
        Classe que indica uma alteração no diagrama sob a forma de um componente sendo removido.
        Guarda informações com relação a última posição desse componente no momento em que ele foi removido de forma
        a ajudar a implementar os métodos refazer e desfazer alteração.
     */

    private final int posicaoXantesDaRemocao;
    private final int posicaoYdepoisDaRemocao;
    private final ComponenteUML componenteRemovido;

    public ComponenteRemovido(int posicaoXantesDaRemocao, int posicaoYdepoisDaRemocao, ComponenteUML componenteRemovido) {
        this.posicaoXantesDaRemocao = posicaoXantesDaRemocao;
        this.posicaoYdepoisDaRemocao = posicaoYdepoisDaRemocao;
        this.componenteRemovido = componenteRemovido;
    }

    public void desfazerAlteracao() {
        // Adiciona o componente na última posição antes da remoção.

        componenteRemovido.getDiagramaUML().addComponente(componenteRemovido);
        componenteRemovido.getPainelComponente().setLocation(posicaoXantesDaRemocao, posicaoYdepoisDaRemocao);
    }

    public void refazerAlteracao() {
        // Remove o componente.

        componenteRemovido.getDiagramaUML().removerComponente(componenteRemovido);
    }
}
