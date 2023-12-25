package componentes.modelos.relacoes;

import componentes.modelos.ModeloDeComponenteUML;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Relacao implements ModeloDeComponenteUML<Relacao> {
    private String nome = "";
    private OrientacaoDeSeta orientacaoLadoA;
    private OrientacaoDeSeta orientacaoLadoB;
    private String multiplicidadeLadoA = "";
    private String multiplicidadeLadoB = "";
    private boolean mostrandoSetaA = false;
    private boolean mostrandoSetaB = false;
    private Point pontoLadoA;
    private DirecaoDeRelacao direcao = DirecaoDeRelacao.NENHUMA;
    private ArrayList<JPanel> linhasDaRelacao = new ArrayList<>();

    public Relacao() {}

    public Relacao(
        String nome, OrientacaoDeSeta orientacaoLadoA, OrientacaoDeSeta orientacaoLadoB, String multiplicidadeLadoA,
        String multiplicidadeLadoB, boolean mostrandoSetaA, boolean mostrandoSetaB, Point pontoLadoA,
        DirecaoDeRelacao direcao, ArrayList<JPanel> linhasDaRelacao
    ) {
        this.nome = nome;
        this.orientacaoLadoA = orientacaoLadoA;
        this.orientacaoLadoB = orientacaoLadoB;
        this.multiplicidadeLadoA = multiplicidadeLadoA;
        this.multiplicidadeLadoB = multiplicidadeLadoB;
        this.mostrandoSetaA = mostrandoSetaA;
        this.mostrandoSetaB = mostrandoSetaB;
        this.pontoLadoA = pontoLadoA;
        this.direcao = direcao;
        this.linhasDaRelacao = linhasDaRelacao;
    }

    @Override
    public Relacao copiar() {
        ArrayList<JPanel> copiaLinhas = new ArrayList<>();

        for (JPanel linha : linhasDaRelacao) {
            JPanel novaLinha = new JPanel();
            novaLinha.setBackground(linha.getBackground());
            novaLinha.setBounds(linha.getBounds());
            copiaLinhas.add(novaLinha);
        }

        Point pontoLadoACopia = new Point();
        pontoLadoACopia.setLocation(pontoLadoA);

        return new Relacao(
            nome, orientacaoLadoA, orientacaoLadoB, multiplicidadeLadoA, multiplicidadeLadoB, mostrandoSetaA,
            mostrandoSetaB, pontoLadoACopia, direcao, copiaLinhas
        );
    }

    @Override
    public boolean ehDiferente(Relacao modelo) {
        boolean linhasSaoIguais = modelo.linhasDaRelacao.size() == linhasDaRelacao.size();

        if (linhasSaoIguais) {
            for (int i = 0; i < linhasDaRelacao.size(); i++) {
                Rectangle boundsLinha = linhasDaRelacao.get(i).getBounds();
                Rectangle boundsLinhaModelo = modelo.linhasDaRelacao.get(i).getBounds();

                linhasSaoIguais = boundsLinha.x == boundsLinhaModelo.x && boundsLinha.y == boundsLinhaModelo.y &&
                        boundsLinha.width == boundsLinhaModelo.width && boundsLinha.height == boundsLinhaModelo.height;

                if (!linhasSaoIguais) break;
            }
        }

        boolean pontoLadoADiferente = pontoLadoA.x != modelo.pontoLadoA.x && pontoLadoA.y != modelo.pontoLadoA.y;

        return !modelo.nome.equals(nome) ||
                !modelo.orientacaoLadoA.equals(orientacaoLadoA) ||
                !modelo.orientacaoLadoB.equals(orientacaoLadoB) ||
                !modelo.multiplicidadeLadoA.equals(multiplicidadeLadoA) ||
                !modelo.multiplicidadeLadoB.equals(multiplicidadeLadoB) ||
                modelo.mostrandoSetaA != mostrandoSetaA ||
                modelo.mostrandoSetaB != mostrandoSetaB ||
                modelo.direcao != direcao ||
                !linhasSaoIguais || pontoLadoADiferente;
    }

    public String getNome() {
        return nome;
    }

    public OrientacaoDeSeta getOrientacaoLadoA() {
        return orientacaoLadoA;
    }

    public OrientacaoDeSeta getOrientacaoLadoB() {
        return orientacaoLadoB;
    }

    public String getMultiplicidadeLadoA() {
        return multiplicidadeLadoA;
    }

    public String getMultiplicidadeLadoB() {
        return multiplicidadeLadoB;
    }

    public Point getPontoLadoA() {
        return pontoLadoA;
    }

    public DirecaoDeRelacao getDirecao() {
        return direcao;
    }

    public ArrayList<JPanel> getLinhasDaRelacao() {
        return linhasDaRelacao;
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

    public void setOrientacaoLadoA(OrientacaoDeSeta orientacaoLadoA) {
        this.orientacaoLadoA = orientacaoLadoA;
    }

    public void setOrientacaoLadoB(OrientacaoDeSeta orientacaoLadoB) {
        this.orientacaoLadoB = orientacaoLadoB;
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

    public void setPontoLadoA(Point pontoLadoA) {
        this.pontoLadoA = pontoLadoA;
    }

    public void setDirecao(DirecaoDeRelacao direcao) {
        this.direcao = direcao;
    }

    public void setLinhasDaRelacao(ArrayList<JPanel> linhasDaRelacao) {
        this.linhasDaRelacao = linhasDaRelacao;
    }
}
