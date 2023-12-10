package diagrama;

import componentes.estruturas.EstruturaUML;
import componentes.relacoes.RelacaoUML;

import java.io.File;
import java.util.ArrayList;

public class DiagramaUML {
    private final ArrayList<EstruturaUML<?>> componentesUML = new ArrayList<>();
    private final ArrayList<RelacaoUML> relacoesUML = new ArrayList<>();
    private boolean diagramaSalvo = false;
    public File arquivoDiagrama;

    public void addComponente(EstruturaUML<?> novoComponente) {
        componentesUML.add(novoComponente);
    }

    public void addRelacao(RelacaoUML novaRelacao) {
        relacoesUML.add(novaRelacao);
    }

    public void removerComponente(EstruturaUML<?> componente) {
        componentesUML.remove(componente);
    }

    public void removerRelacao(RelacaoUML relacao) {
        relacoesUML.remove(relacao);
    }

    public void setDiagramaSalvo(boolean diagramaSalvo) {
        this.diagramaSalvo = diagramaSalvo;
    }

    public ArrayList<EstruturaUML<?>> getComponentesUML() {
        return componentesUML;
    }

    public ArrayList<RelacaoUML> getRelacoesUML() {
        return relacoesUML;
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
        diagramaEmString.append("// Numero de componentes\n");
        diagramaEmString.append(componentesUML.size()).append("\n");

        for (EstruturaUML<?> componente : componentesUML) {
            diagramaEmString.append(componente.toString());
        }

        return diagramaEmString.toString();
    }
}


