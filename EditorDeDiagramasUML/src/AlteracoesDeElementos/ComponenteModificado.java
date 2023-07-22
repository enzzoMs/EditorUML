package AlteracoesDeElementos;

import ComponentesUML.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ComponenteModificado implements AlteracaoDeElementosUML {

   private final HashMap<String, Object> informacoesComponenteAntesDaAlterecao;
   private final HashMap<String, Object> informacoesComponenteDepoisDaAlterecao;
   private final ComponenteUML componenteModificado;

    public ComponenteModificado(HashMap<String, Object> informacoesComponenteAntesDaAlterecao,
                                HashMap<String, Object> informacoesComponenteDepoisDaAlterecao,
                                ComponenteUML componenteModificado) {

        this.informacoesComponenteAntesDaAlterecao = informacoesComponenteAntesDaAlterecao;
        this.informacoesComponenteDepoisDaAlterecao = informacoesComponenteDepoisDaAlterecao;
        this.componenteModificado = componenteModificado;
    }


    public void desfazerAlteracao() {
        if (componenteModificado instanceof ClasseUML) {

            ((ClasseUML) componenteModificado).setArrayListAtributos(
                    (ArrayList<Atributo>) informacoesComponenteAntesDaAlterecao.get("LISTA_OBJETOS_ATRIBUTOS"));

            ((ClasseUML) componenteModificado).setArrayListMetodos(
                    (ArrayList<Metodo>) informacoesComponenteAntesDaAlterecao.get("LISTA_OBJETOS_METODOS"));

            ((ClasseUML) componenteModificado).atualizarComponente(
                    (String) informacoesComponenteAntesDaAlterecao.get("NOME"),
                    (String) informacoesComponenteAntesDaAlterecao.get("COMENTARIO"),
                    (Boolean) informacoesComponenteAntesDaAlterecao.get("ABSTRATA"),
                    (Object[]) informacoesComponenteAntesDaAlterecao.get("LISTA_STRING_ATRIBUTOS"),
                    (Object[]) informacoesComponenteAntesDaAlterecao.get("LISTA_STRING_METODOS"),
                    (Integer) informacoesComponenteAntesDaAlterecao.get("LIMITE_COMENTARIO"),
                    (Boolean) informacoesComponenteAntesDaAlterecao.get("INTERFACE")
            );

        } else if (componenteModificado instanceof PacoteUML) {
            ((PacoteUML) componenteModificado).getPainelAreaPacote().setSize(
                    (Integer) informacoesComponenteAntesDaAlterecao.get("LARGURA_AREA_PACOTE"),
                    (Integer) informacoesComponenteAntesDaAlterecao.get("ALTURA_AREA_PACOTE"));


            ((JLabel) ((PacoteUML) componenteModificado).getPainelNomePacote().getComponent(0))
                    .setText((String) informacoesComponenteAntesDaAlterecao.get("NOME"));

            componenteModificado.setLargura(Math.max(((PacoteUML) componenteModificado).getPainelAreaPacote().getWidth(),
                    ((PacoteUML) componenteModificado).getPainelNomePacote().getPreferredSize().width));


            componenteModificado.getPainelComponente().setLocation(
                    (Integer) informacoesComponenteAntesDaAlterecao.get("POSICAO_X"),
                    (Integer) informacoesComponenteAntesDaAlterecao.get("POSICAO_Y"));

            ((PacoteUML) componenteModificado).atualizarComponente((String) informacoesComponenteAntesDaAlterecao.get("NOME"));

        } else {
            ((AnotacaoUML) componenteModificado).atualizarComponente(
                    (String) informacoesComponenteAntesDaAlterecao.get("TEXTO_ANOTACAO"),
                    (Integer) informacoesComponenteAntesDaAlterecao.get("LIMITE_ANOTACAO"));
        }

    }

    public void refazerAlteracao() {
        if (componenteModificado instanceof ClasseUML) {

            ((ClasseUML) componenteModificado).setArrayListAtributos(
                   (ArrayList<Atributo>) informacoesComponenteDepoisDaAlterecao.get("LISTA_OBJETOS_ATRIBUTOS"));

            ((ClasseUML) componenteModificado).setArrayListMetodos(
                    (ArrayList<Metodo>) informacoesComponenteDepoisDaAlterecao.get("LISTA_OBJETOS_METODOS"));


            ((ClasseUML) componenteModificado).atualizarComponente(
                    (String) informacoesComponenteDepoisDaAlterecao.get("NOME"),
                    (String) informacoesComponenteDepoisDaAlterecao.get("COMENTARIO"),
                    (Boolean) informacoesComponenteDepoisDaAlterecao.get("ABSTRATA"),
                    (Object[]) informacoesComponenteDepoisDaAlterecao.get("LISTA_STRING_ATRIBUTOS"),
                    (Object[]) informacoesComponenteDepoisDaAlterecao.get("LISTA_STRING_METODOS"),
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("LIMITE_COMENTARIO"),
                    (Boolean) informacoesComponenteDepoisDaAlterecao.get("INTERFACE")
            );


        } else if (componenteModificado instanceof PacoteUML) {
            ((PacoteUML) componenteModificado).getPainelAreaPacote().setSize(
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("LARGURA_AREA_PACOTE"),
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("ALTURA_AREA_PACOTE"));


            ((JLabel) ((PacoteUML) componenteModificado).getPainelNomePacote().getComponent(0))
                    .setText((String) informacoesComponenteDepoisDaAlterecao.get("NOME"));

            componenteModificado.setLargura(Math.max(((PacoteUML) componenteModificado).getPainelAreaPacote().getWidth(),
                    ((PacoteUML) componenteModificado).getPainelNomePacote().getPreferredSize().width));


            componenteModificado.getPainelComponente().setLocation(
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("POSICAO_X"),
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("POSICAO_Y")
            );

            ((PacoteUML) componenteModificado).atualizarComponente((String) informacoesComponenteDepoisDaAlterecao.get("NOME"));
        } else {
            ((AnotacaoUML) componenteModificado).atualizarComponente(
                    (String) informacoesComponenteDepoisDaAlterecao.get("TEXTO_ANOTACAO"),
                    (Integer) informacoesComponenteDepoisDaAlterecao.get("LIMITE_ANOTACAO"));
        }

    }
}

