package componentes.alteracoes;

import componentes.estruturas.ComponenteUML;
import modelos.ModeloDeComponenteUML;

/**
 * Classe que indica uma alteração no diagrama sob a forma de um componente modificado.
 * Guarda informações com relação ao modelo do componente antes e depois da alteração.
 */
public class ComponenteModificado<T> implements AlteracaoDeComponenteUML {
   private final ModeloDeComponenteUML<T> modeloAntesDaAlteracao;
   private final ModeloDeComponenteUML<T> modeloDepoisDaAlteracao;
   private final ComponenteUML<T> componenteModificado;

    public ComponenteModificado(
        ModeloDeComponenteUML<T> modeloAntes, ModeloDeComponenteUML<T> modeloDepois, ComponenteUML<T> componenteModificado
    ) {
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

