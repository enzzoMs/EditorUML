package componentes.alteracoes.estruturas;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.estruturas.EstruturaUML;

import java.awt.*;

public class EstruturaRemovida implements AlteracaoDeComponenteUML {
    private final Point posicaoAntesDaRemocao;
    private final EstruturaUML<?> estruturaRemovida;

    public EstruturaRemovida(Point posicaoAntesDaRemocao, EstruturaUML<?> estruturaRemovida) {
        this.posicaoAntesDaRemocao = posicaoAntesDaRemocao;
        this.estruturaRemovida = estruturaRemovida;
    }

    public void desfazerAlteracao() {
        estruturaRemovida.adicionarComponenteAoQuadroBranco();
        estruturaRemovida.getPainelComponente().setLocation(posicaoAntesDaRemocao);
    }

    public void refazerAlteracao() {
        estruturaRemovida.removerComponenteDoQuadroBranco();
    }
}
