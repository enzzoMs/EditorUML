package componentes.alteracoes.estruturas;

import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.estruturas.EstruturaUML;
import componentes.modelos.ModeloDeComponenteUML;

public class EstruturaModificada<T> implements AlteracaoDeComponenteUML {
   private final ModeloDeComponenteUML<T> modeloAntesDaAlteracao;
   private final ModeloDeComponenteUML<T> modeloDepoisDaAlteracao;
   private final EstruturaUML<T> estruturaModificada;

    public EstruturaModificada(
            ModeloDeComponenteUML<T> modeloAntes, ModeloDeComponenteUML<T> modeloDepois, EstruturaUML<T> estruturaModificada
    ) {
        modeloAntesDaAlteracao = modeloAntes;
        modeloDepoisDaAlteracao = modeloDepois;
        this.estruturaModificada = estruturaModificada;
    }

   public void desfazerAlteracao() {
       estruturaModificada.setModelo(modeloAntesDaAlteracao);
   }

   public void refazerAlteracao() {
       estruturaModificada.setModelo(modeloDepoisDaAlteracao);
   }
}

