package componentes.alteracoes;

import componentes.alteracoes.estruturas.EstruturaCriada;
import componentes.alteracoes.estruturas.EstruturaMovida;
import componentes.alteracoes.estruturas.EstruturaRemovida;

/**
 * Interface que especifica os comportamentos a serem adotados pelas classes de alteração de Componentes UML,
 * incluindo operações para desfazer ou refazer as alterações.
 * @see EstruturaCriada
 * @see EstruturaMovida
 * @see EstruturaRemovida
 */
public interface AlteracaoDeComponenteUML {
    void desfazerAlteracao();

    void refazerAlteracao();
}
