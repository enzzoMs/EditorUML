package AlteracoesDeElementos;

public interface AlteracaoDeElementosUML {

    /*
        Interface que especifica os comportamentos a serem adotados pelas classes de alteracao de elementos UML,
        incluindo as alterações de componentes e relações.
     */

    void desfazerAlteracao();

    void refazerAlteracao();
}
