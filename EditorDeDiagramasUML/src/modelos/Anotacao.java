package modelos;

public class Anotacao implements ModeloDeComponenteUML<Anotacao>{
    private String texto = "";
    private int numCharsQuebrarTexto = 20;

    public Anotacao() {}

    public Anotacao(String texto, int numCharsQuebrarTexto) {
        this.texto = texto;
        this.numCharsQuebrarTexto = numCharsQuebrarTexto;
    }

    @Override
    public Anotacao copiar() {
        return new Anotacao(texto, numCharsQuebrarTexto);
    }

    @Override
    public boolean ehDiferente(Anotacao modelo) {
        return !modelo.texto.equals(texto) || modelo.numCharsQuebrarTexto != numCharsQuebrarTexto;
    }
    public String getTexto() {
        return texto;
    }

    public int getNumCharsQuebrarTexto() {
        return numCharsQuebrarTexto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setNumCharsQuebrarTexto(int numCharsQuebrarTexto) {
        this.numCharsQuebrarTexto = numCharsQuebrarTexto;
    }
}
