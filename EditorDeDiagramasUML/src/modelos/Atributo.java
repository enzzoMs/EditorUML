package modelos;

public class Atributo {
    private String nome = "";
    private String tipo = "";
    private String valorPadrao = "";
    private Visibilidade visibilidade = Visibilidade.PUBLICO;
    private boolean estatico;

    public Atributo() {}

    public Atributo(String nome, String tipo, String valorPadrao, Visibilidade visibilidade, boolean estatico) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorPadrao = valorPadrao;
        this.visibilidade = visibilidade;
        this.estatico = estatico;
    }

    public String getRepresentacaoUml() {
        return (estatico ? "<html><u>" : "") +
            visibilidade.getSimbolo() +
            nome +
            (tipo.isEmpty() ? "" : ": " + tipo) +
            (valorPadrao.isEmpty() ? "" : " = " + valorPadrao) +
            (estatico ? "</u></html>" : "");
    }

    public Visibilidade getVisibilidade() {
        return visibilidade;
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

    public void setVisibilidade(Visibilidade visibilidade) {
        this.visibilidade = visibilidade;
    }

    public boolean ehEstatico() {
        return estatico;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }

}
