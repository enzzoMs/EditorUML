package componentes.alteracoes.estruturas;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.estruturas.EstruturaUML;

import java.awt.*;

public class EstruturaCriada implements AlteracaoDeComponenteUML {
    private final Point posicaoDepoisDaCriacao;
    private final EstruturaUML<?> estruturaCriada;

    public EstruturaCriada(Point posicaoDepoisDaCriacao, EstruturaUML<?> estruturaCriada) {
        this.posicaoDepoisDaCriacao = posicaoDepoisDaCriacao;
        this.estruturaCriada = estruturaCriada;
    }

    public void desfazerAlteracao() {
        // Desfazer a criacao = remover a estrutura.

        estruturaCriada.removerComponenteDoQuadroBranco();
    }

    public void refazerAlteracao() {
        // Coloca a estrutura na posicao especificada

        estruturaCriada.adicionarComponenteAoQuadroBranco();
        estruturaCriada.getPainelComponente().setLocation(posicaoDepoisDaCriacao.x, posicaoDepoisDaCriacao.y);
    }
}
