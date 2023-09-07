package componentes.alteracoes;

/**
 * Interface que especifica os comportamentos a serem adotados pelas classes de alteração de Componentes UML,
 * incluindo operações para desfazer ou refazer as alterações.
 * @see ComponenteCriado
 * @see ComponenteMovido
 * @see ComponenteRemovido
 * @see RelacaoModificada
 */
public interface AlteracaoDeComponentesUML {
    void desfazerAlteracao();

    void refazerAlteracao();
}
