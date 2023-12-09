package modelos;

public class Parametro {
    private String nome = "";
    private String tipo = "";
    private String valorPadrao = "";

    public Parametro() {}

    public Parametro(String nome, String tipo, String valorPadrao) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorPadrao = valorPadrao;
    }

    public Parametro copiar() {
        return new Parametro(nome, tipo, valorPadrao);
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    public String getRepresentacaoUml() {
        return
            nome +
            ":" +
            (tipo.isEmpty() ? "" : " " + tipo) +
            (valorPadrao.isEmpty() ? "" : " = " + valorPadrao);
    }
}
