package diagrama;

import componentes.ComponenteUML;
import interfacegrafica.AreaDeDiagramas;
import RelacoesUML.RelacaoUML;

import java.io.File;
import java.util.ArrayList;

public class DiagramaUML {

    private final AreaDeDiagramas areaDeDiagramas;

    private final ArrayList<ComponenteUML> listaComponentesUML = new ArrayList<>();

    private final ArrayList<RelacaoUML> listaRelacaoes = new ArrayList<>(); // colocar relacoes !!!!!!!!!!!!!!!

    private boolean diagramaSalvo = false;

    public File arquivoDiagrama;


    public DiagramaUML(AreaDeDiagramas areaDeDiagramas) {
        this.areaDeDiagramas = areaDeDiagramas;
    }

    public void addComponente(ComponenteUML novoComponente) {
        listaComponentesUML.add(novoComponente);
        areaDeDiagramas.addComponenteAoQuadro(novoComponente, true);
    }

    public void addRelacao(RelacaoUML novaRelacao) {
        listaRelacaoes.add(novaRelacao);
        areaDeDiagramas.addRelacionametoAoQuadro(novaRelacao.getListaPaineisRelacao());
    }

    public void removerComponente(ComponenteUML componente) {
        listaComponentesUML.remove(componente);
        areaDeDiagramas.removerComponenteDoQuadro(componente);
    }

    public void removerRelacao(RelacaoUML relacaoUML) {
        /*
        listaRelacaoes.remove(relacaoUML);
        areaDeDiagramas.removerRelacaoDoQuadro(relacaoUML);*/
    }

    public void setDiagramaSalvo(boolean diagramaSalvo) {
        this.diagramaSalvo = diagramaSalvo;
    }


    // TODO: Remover isso
    public AreaDeDiagramas getAreaDeDiagramas() {
        return areaDeDiagramas;
    }

    public ArrayList<ComponenteUML> getListaComponentesUML() {
        return listaComponentesUML;
    }


    public ArrayList<RelacaoUML> getListaRelacaoes() {
        return listaRelacaoes;
    }

    public boolean diagramaEstaSalvo() {
        return diagramaSalvo;
    }

    /**
     * Desconstrói o diagrama em uma String contendo as informações de cada componente.
     */
    public String desconstruirDiagrama() {
        StringBuilder diagramaEmString = new StringBuilder();

        diagramaEmString.append("UML APP\n");
        diagramaEmString.append("// Numero de componentes");
        diagramaEmString.append(listaComponentesUML.size()).append("\n");

        for (ComponenteUML componente : listaComponentesUML) {
            /*if (componente instanceof ClasseUML) {
                diagramaEmString.append("CLASSE_UML\n");
            } else if (componenteUML instanceof AnotacaoUML) {
                diagramaEmString.append("ANOTACAO_UML\n");
            } else {
                diagramaEmString.append("PACOTE_UML\n");
            }*/

            diagramaEmString.append(componente.toString());
        }

        return diagramaEmString.toString();
    }
}


