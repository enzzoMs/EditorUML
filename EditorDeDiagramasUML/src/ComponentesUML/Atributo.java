package ComponentesUML;

public class Atributo {

    private String nome = "";
    private String tipo = "";
    private String valorPadrao = "";
    private String visibilidade = "+";
    private boolean estatico;

    public Atributo() {}

    public Atributo(String nome, String tipo, String valorPadrao, String visibilidade, boolean estatico) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorPadrao = valorPadrao;
        this.visibilidade = visibilidade;
        this.estatico = estatico;
    }


    public void setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
    }

    public String getVisibilidade() {
        return visibilidade;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValorPadrao() {
        return valorPadrao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    public boolean ehEstatico() {
        return estatico;
    }

}
