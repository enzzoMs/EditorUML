package componentes.alteracoes;

import componentes.alteracoes.estruturas.EstruturaCriada;
import componentes.alteracoes.estruturas.EstruturaMovida;
import componentes.alteracoes.estruturas.EstruturaRemovida;
import componentes.alteracoes.relacoes.RelacaoCriada;
import componentes.alteracoes.relacoes.RelacaoRemovida;
import componentes.alteracoes.relacoes.RelacaoModificada;

/**
 * Interface que especifica os comportamentos a serem adotados pelas classes de alteração de Componentes UML,
 * incluindo operações para desfazer ou refazer as alterações.
 * @see EstruturaCriada
 * @see EstruturaMovida
 * @see EstruturaRemovida
 * @see RelacaoCriada
 * @see RelacaoRemovida
 * @see RelacaoModificada
 */
public interface AlteracaoDeComponenteUML {
    void desfazerAlteracao();

    void refazerAlteracao();
}
