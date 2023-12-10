package componentes.modelos.estruturas;

import java.util.ArrayList;

public class Metodo {
    private String nome = "";
    private Visibilidade visibilidade = Visibilidade.PUBLICO;
    private String tipo = "";
    private boolean estatico;
    private boolean abstrato;
    private ArrayList<Parametro> parametros = new ArrayList<>();

    public Metodo() {}

    public Metodo(
        String nome, Visibilidade visibilidade, String tipo, boolean estatico, boolean abstrato,
        ArrayList<Parametro> parametros
    ) {
        this.nome = nome;
        this.visibilidade = visibilidade;
        this.tipo = tipo;
        this.estatico = estatico;
        this.abstrato = abstrato;
        this.parametros = parametros;
    }

    public String getRepresentacaoUml() {
        StringBuilder textoUmlMetodo = new StringBuilder();
        textoUmlMetodo.append("<html>")
                .append(estatico ? "<u>" : "")
                .append(abstrato ? "<i>" : "")
                .append(visibilidade.getSimbolo())
                .append(nome).append("(");

        for (int i = 0; i < parametros.size(); i++) {
            textoUmlMetodo.append(parametros.get(i).getRepresentacaoUml());

            if (i < parametros.size() - 1) {
                textoUmlMetodo.append(", ");
            }
        }

        textoUmlMetodo.append(")")
                .append(tipo.isEmpty() ? "" : ": " + tipo)
                .append(abstrato ? "</i>" : "")
                .append(estatico ? "</u>" : "")
                .append("<html>");

        return textoUmlMetodo.toString();
    }

    public Metodo copiar() {
        ArrayList<Parametro> copiaParametros = new ArrayList<>();

        for (Parametro parametro : parametros) {
            copiaParametros.add(parametro.copiar());
        }

        return new Metodo(nome, visibilidade, tipo, estatico, abstrato, copiaParametros);
    }

    public String getNome() {
        return nome;
    }

    public Visibilidade getVisibilidade() {
        return visibilidade;
    }

    public String getTipo() {
        return tipo;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public boolean ehEstatico() {
        return estatico;
    }

    public boolean ehAbstrato() {
        return abstrato;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setVisibilidade(Visibilidade visibilidade) {
        this.visibilidade = visibilidade;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }

    public void setAbstrato(boolean abstrato) {
        this.abstrato = abstrato;
    }
}
