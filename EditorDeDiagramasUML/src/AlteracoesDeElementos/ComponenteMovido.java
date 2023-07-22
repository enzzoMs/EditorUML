package AlteracoesDeElementos;

import ComponentesUML.ComponenteUML;

public class ComponenteMovido implements AlteracaoDeElementosUML {
    /*
        Classe que indica uma alteração no diagrama sob a forma de um componente sendo movido.
        Guarda informações com relação a posição antes e depois do componente ter sido movimentado de forma a ajudar
        a implementar os métodos refazer e desfazer alteração.
     */

    private final int posicaoXantesDoMovimento;
    private final int posicaoYantesDoMovimento;
    private final int posicaoXdepoisDoMovimento;
    private final int posicaoYdepoisDoMovimento;
    private final ComponenteUML componenteMovido;


    public ComponenteMovido(int posicaoXantesDoMovimento, int posicaoYantesDoMovimento, int posicaoXdepoisDoMovimento,
                            int posicaoYdepoisDoMovimento, ComponenteUML componenteMovido) {

        this.posicaoXantesDoMovimento = posicaoXantesDoMovimento;
        this.posicaoYantesDoMovimento = posicaoYantesDoMovimento;
        this.posicaoXdepoisDoMovimento = posicaoXdepoisDoMovimento;
        this.posicaoYdepoisDoMovimento = posicaoYdepoisDoMovimento;
        this.componenteMovido = componenteMovido;
    }


    @Override
    public void desfazerAlteracao() {
        // Move o componente para a posição antes do movimento.

        componenteMovido.getPainelComponente().setLocation(posicaoXantesDoMovimento, posicaoYantesDoMovimento);
    }

    @Override
    public void refazerAlteracao() {
        // Move o componente para a posição depois do movimento.

        componenteMovido.getPainelComponente().setLocation(posicaoXdepoisDoMovimento, posicaoYdepoisDoMovimento);
    }
}
