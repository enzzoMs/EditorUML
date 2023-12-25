package diagrama;

import componentes.estruturas.EstruturaUML;
import componentes.relacoes.RelacaoUML;

import java.io.File;
import java.util.ArrayList;

public class DiagramaUML {
    private final ArrayList<EstruturaUML<?>> estruturasUML = new ArrayList<>();
    private final ArrayList<RelacaoUML> relacoesUML = new ArrayList<>();
    private boolean diagramaSalvo = false;
    public File arquivoDiagrama;

    public void addComponente(EstruturaUML<?> novoComponente) {
        estruturasUML.add(novoComponente);
    }

    public void addRelacao(RelacaoUML novaRelacao) {
        relacoesUML.add(novaRelacao);
    }

    public void removerComponente(EstruturaUML<?> componente) {
        estruturasUML.remove(componente);
    }

    public void removerRelacao(RelacaoUML relacao) {
        relacoesUML.remove(relacao);
    }

    public void setDiagramaSalvo(boolean diagramaSalvo) {
        this.diagramaSalvo = diagramaSalvo;
    }

    public ArrayList<EstruturaUML<?>> getEstruturasUML() {
        return estruturasUML;
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
        diagramaEmString.append(estruturasUML.size() + relacoesUML.size()).append("\n");

        for (EstruturaUML<?> componente : estruturasUML) {
            diagramaEmString.append(componente.toString());
        }

        for (RelacaoUML relacao : relacoesUML) {
            diagramaEmString.append(relacao.toString());
        }

        return diagramaEmString.toString();
    }
}


