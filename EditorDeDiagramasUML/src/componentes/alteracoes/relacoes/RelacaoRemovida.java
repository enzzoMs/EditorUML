package componentes.alteracoes.relacoes;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.relacoes.RelacaoUML;

public class RelacaoRemovida implements AlteracaoDeComponenteUML {
    private final RelacaoUML relacaoRemovida;

    public RelacaoRemovida(RelacaoUML relacaoRemovida) {
        this.relacaoRemovida = relacaoRemovida;
    }

    @Override
    public void desfazerAlteracao() {
        relacaoRemovida.adicionarRelacaoAoQuadroBranco();
    }

    @Override
    public void refazerAlteracao() {
        relacaoRemovida.removerRelacaoDoQuadroBranco();
    }
}
