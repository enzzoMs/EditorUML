package componentes.modelos.relacoes;

import componentes.modelos.ModeloDeComponenteUML;

public class Relacao implements ModeloDeComponenteUML<Relacao> {
    private String nome = "";
    private String multiplicidadeLadoA = "";
    private String multiplicidadeLadoB = "";
    private boolean mostrandoSetaA = false;
    private boolean mostrandoSetaB = false;

    @Override
    public Relacao copiar() {
        return null;
    }

    @Override
    public boolean ehDiferente(Relacao modelo) {
        return false;
    }

    public String getNome() {
        return nome;
    }

    public String getMultiplicidadeLadoA() {
        return multiplicidadeLadoA;
    }

    public String getMultiplicidadeLadoB() {
        return multiplicidadeLadoB;
    }

    public boolean estaMostrandoSetaA() {
        return mostrandoSetaA;
    }

    public boolean estaMostrandoSetaB() {
        return mostrandoSetaB;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMultiplicidadeLadoA(String multiplicidadeLadoA) {
        this.multiplicidadeLadoA = multiplicidadeLadoA;
    }

    public void setMultiplicidadeLadoB(String multiplicidadeLadoB) {
        this.multiplicidadeLadoB = multiplicidadeLadoB;
    }

    public void setMostrandoSetaA(boolean mostrandoSetaA) {
        this.mostrandoSetaA = mostrandoSetaA;
    }

    public void setMostrandoSetaB(boolean mostrandoSetaB) {
        this.mostrandoSetaB = mostrandoSetaB;
    }
}
