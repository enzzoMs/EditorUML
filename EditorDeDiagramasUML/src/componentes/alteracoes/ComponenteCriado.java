package componentes.alteracoes;

import componentes.ComponenteUML;

public class ComponenteCriado implements AlteracaoDeComponentesUML {
    /*
        Classe que indica uma alteração no diagrama sob a forma de um componente sendo criado.
        Guarda informações com relação a posição em que esse componente foi criado de forma a ajudar a implementar os
        métodos refazer e desfazer alteração.
     */

    private final int posicaoXdepoisDaCriacao;
    private final int posicaoYdepoisDaCriacao;
    private final ComponenteUML componenteCriado;


    public ComponenteCriado(int posicaoXdepoisDaCriacao, int posicaoYdepoisDaCriacao, ComponenteUML componenteCriado) {
        this.posicaoXdepoisDaCriacao = posicaoXdepoisDaCriacao;
        this.posicaoYdepoisDaCriacao = posicaoYdepoisDaCriacao;
        this.componenteCriado = componenteCriado;
    }


    public void desfazerAlteracao() {
        // Desfazer a criação do componente, ou seja, remove o componente.

        //componenteCriado.getDiagramaUML().removerComponente(componenteCriado);
    }

    public void refazerAlteracao() {
        // Refazer a criação do componente na posição especificada pelos atributos.

       // componenteCriado.getDiagramaUML().addComponente(componenteCriado);
        componenteCriado.getPainelComponente().setLocation(posicaoXdepoisDaCriacao, posicaoYdepoisDaCriacao);
    }
}
