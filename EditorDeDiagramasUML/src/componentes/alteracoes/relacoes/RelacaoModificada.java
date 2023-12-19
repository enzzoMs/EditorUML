package componentes.alteracoes.relacoes;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.modelos.relacoes.Relacao;
import componentes.relacoes.RelacaoUML;

public class RelacaoModificada implements AlteracaoDeComponenteUML {

    private final Relacao modeloAntesDaAlteracao;
    private final Relacao modeloDepoisDaAlteracao;
    private final RelacaoUML relacaoModificada;

    public RelacaoModificada(RelacaoUML relacaoModificada, Relacao modeloAntesDaAlteracao, Relacao modeloDepoisDaAlteracao) {
        this.relacaoModificada = relacaoModificada;
        this.modeloAntesDaAlteracao = modeloAntesDaAlteracao;
        this.modeloDepoisDaAlteracao = modeloDepoisDaAlteracao;
    }

    @Override
    public void desfazerAlteracao() {
        relacaoModificada.setModelo(modeloAntesDaAlteracao);
    }

    @Override
    public void refazerAlteracao() {
        relacaoModificada.setModelo(modeloDepoisDaAlteracao);
    }
}
