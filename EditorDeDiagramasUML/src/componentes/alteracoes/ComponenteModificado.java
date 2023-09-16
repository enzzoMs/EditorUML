package componentes.alteracoes;

import componentes.ComponenteUML;

/**
 * Classe que indica uma alteração no diagrama sob a forma de um componente modificado.
 * Guarda informações com relação ao modelo do componente antes e depois da alteração.
 */
public class ComponenteModificado<T> implements AlteracaoDeComponenteUML {
   private final T modeloAntesDaAlteracao;
   private final T modeloDepoisDaAlteracao;
   private final ComponenteUML componenteModificado;

    public ComponenteModificado(T modeloAntes, T modeloDepois, ComponenteUML componenteModificado) {
        modeloAntesDaAlteracao = modeloAntes;
        modeloDepoisDaAlteracao = modeloDepois;
        this.componenteModificado = componenteModificado;
    }

   public void desfazerAlteracao() {
      componenteModificado.setModelo(modeloAntesDaAlteracao);
   }

   public void refazerAlteracao() {
       componenteModificado.setModelo(modeloDepoisDaAlteracao);
   }
}

