package modelos;

import componentes.ComponenteUML;

import java.io.File;
import java.util.ArrayList;

public class DiagramaUML {
    private final ArrayList<ComponenteUML<?>> componentesUML = new ArrayList<>();
    private boolean diagramaSalvo = false;
    public File arquivoDiagrama;

    public void addComponente(ComponenteUML<?> novoComponente) {
        componentesUML.add(novoComponente);
    }

    public void removerComponente(ComponenteUML<?> componente) {
        componentesUML.remove(componente);
    }

    public void setDiagramaSalvo(boolean diagramaSalvo) {
        this.diagramaSalvo = diagramaSalvo;
    }

    public ArrayList<ComponenteUML<?>> getComponentesUML() {
        return componentesUML;
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

        for (ComponenteUML<?> componente : componentesUML) {
            diagramaEmString.append(componente.toString());
        }

        return diagramaEmString.toString();
    }
}


