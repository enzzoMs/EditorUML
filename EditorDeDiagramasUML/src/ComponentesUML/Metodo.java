package ComponentesUML;

import java.util.ArrayList;

public class Metodo {
    private String nome = "";
    private String visibilidade = "";
    private String tipo = "";
    private boolean estatico;
    private boolean abstrato;
    private ArrayList<Parametro> parametros = new ArrayList<>();

    public Metodo() {
    }

    public Metodo(String nome, String visibilidade, String tipo, boolean estatico, boolean abstrato, ArrayList<Parametro> parametros) {
        this.nome = nome;
        this.visibilidade = visibilidade;
        this.tipo = tipo;
        this.estatico = estatico;
        this.abstrato = abstrato;
        this.parametros = parametros;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean ehEstatico() {
        return estatico;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public String toStringParametros() {
        StringBuilder stringParametros = new StringBuilder();

        for (Parametro parametro : this.parametros) {
            stringParametros.append(parametro.getNome());
            stringParametros.append(":");
            stringParametros.append(parametro.getTipo().equals("") ? "" : " " + parametro.getTipo());
            stringParametros.append(parametro.getValorPadrao().equals("") ? "" : " = " + parametro.getValorPadrao());

            if (parametros.indexOf(parametro) != parametros.size()-1) {
                stringParametros.append(", ");
            }
        }

        return stringParametros.toString();
    }

    public boolean ehAbstrato() {
        return abstrato;
    }

    public void setAbstrato(boolean abstrato) {
        this.abstrato = abstrato;
    }

}
