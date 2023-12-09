package componentes.alteracoes.relacoes;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.relacoes.RelacaoUML;

public class RelacaoCriada implements AlteracaoDeComponenteUML {
    private final RelacaoUML relacaoCriada;

    public RelacaoCriada(RelacaoUML relacaoCriada) {
        this.relacaoCriada = relacaoCriada;
    }

    @Override
    public void desfazerAlteracao() {
        relacaoCriada.removerRelacaoDoQuadroBranco();
    }

    @Override
    public void refazerAlteracao() {
        relacaoCriada.adicionarRelacaoAoQuadroBranco();
    }
}
