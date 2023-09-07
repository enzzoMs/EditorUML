package componentes.alteracoes;

import RelacoesUML.RelacaoUML;

import javax.swing.*;
import java.util.ArrayList;

public class RelacaoModificada implements AlteracaoDeComponentesUML {

    private final ArrayList<JComponent> listaComponentesDaRelacao;
    private final RelacaoUML relacaoUML;
    private final TipoDaModificao tipoDaModificao;

    public RelacaoModificada(ArrayList<JComponent> listaComponentesDaRelacao, TipoDaModificao tipoDaModificao, RelacaoUML relacaoUML) {
        this.listaComponentesDaRelacao = listaComponentesDaRelacao;
        this.tipoDaModificao = tipoDaModificao;
        this.relacaoUML = relacaoUML;
    }

    @Override
    public void desfazerAlteracao() {
        if (tipoDaModificao == TipoDaModificao.CRIADA) {
            relacaoUML.getDiagramaUML().removerRelacao(relacaoUML);
        } else if (tipoDaModificao == TipoDaModificao.REMOVIDA) {
            relacaoUML.getDiagramaUML().addRelacao(relacaoUML);
        }
    }

    @Override
    public void refazerAlteracao() {
        if (tipoDaModificao == TipoDaModificao.CRIADA) {
            relacaoUML.getDiagramaUML().addRelacao(relacaoUML);
        } else if (tipoDaModificao == TipoDaModificao.REMOVIDA) {
            relacaoUML.getDiagramaUML().removerRelacao(relacaoUML);
        }
    }

    public enum TipoDaModificao {
        CRIADA, REMOVIDA, MODIFICADA
    }
}
