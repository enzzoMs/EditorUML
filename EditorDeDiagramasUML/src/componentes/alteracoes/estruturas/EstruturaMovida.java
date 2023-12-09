package componentes.alteracoes.estruturas;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.estruturas.EstruturaUML;

import java.awt.*;

public class EstruturaMovida implements AlteracaoDeComponenteUML {
    private final Point posicaoAntesDoMovimento;
    private final Point posicaoDepoisDoMovimento;
    private final EstruturaUML<?> estruturaMovida;

    public EstruturaMovida(Point posicaoAntesDoMovimento, Point posicaoDepoisDoMovimento, EstruturaUML<?> estruturaMovida) {
        this.posicaoAntesDoMovimento = posicaoAntesDoMovimento;
        this.posicaoDepoisDoMovimento = posicaoDepoisDoMovimento;
        this.estruturaMovida = estruturaMovida;
    }

    @Override
    public void desfazerAlteracao() {
        estruturaMovida.getPainelComponente().setLocation(posicaoAntesDoMovimento);
    }

    @Override
    public void refazerAlteracao() {
        estruturaMovida.getPainelComponente().setLocation(posicaoDepoisDoMovimento);
    }
}
