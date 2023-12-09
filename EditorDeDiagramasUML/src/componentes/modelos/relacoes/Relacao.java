package componentes.modelos.relacoes;

import componentes.modelos.ModeloDeComponenteUML;

public class Relacao implements ModeloDeComponenteUML<Relacao> {
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

    public boolean estaMostrandoSetaA() {
        return mostrandoSetaA;
    }

    public boolean estaMostrandoSetaB() {
        return mostrandoSetaB;
    }

    public void setMostrandoSetaA(boolean mostrandoSetaA) {
        this.mostrandoSetaA = mostrandoSetaA;
    }
    public void setMostrandoSetaB(boolean mostrandoSetaB) {
        this.mostrandoSetaB = mostrandoSetaB;
    }
}
