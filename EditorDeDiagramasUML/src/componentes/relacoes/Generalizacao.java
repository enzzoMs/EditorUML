package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import interfacegrafica.AreaDeDiagramas;
import modelos.TipoDeRelacao;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe que representa uma relação UML do tipo "Generalização". Graficamente se trata de um
 * componente simples, possuindo uma seta "oca" em uma das extremidades.
 */
public class Generalizacao extends RelacaoSimples {

    public Generalizacao(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point primeiroPontoDaRelacao, Point ultimoPontoDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        super(linhasDaRelacao, areaDeDiagramas, primeiroPontoDaRelacao, ultimoPontoDaRelacao, tipoDeRelacao);
    }

    @Override
    public void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas) {
        for (JPanel linha : linhas) {
            linha.setBackground(GerenciadorDeRecursos.getInstancia().getColor("dark_charcoal"));
        }
    }

    @Override
    public void desenharSeta(Graphics2D g2, int[] pontosXDaSeta, int[] pontosYDaSeta) {
        g2.setStroke(new BasicStroke(8));
        g2.setColor(relacaoEstaSelecionada() ? COR_SELECIONAR : COR_PADRAO);
        g2.drawPolygon(pontosXDaSeta, pontosYDaSeta, 3);

        g2.setColor(GerenciadorDeRecursos.getInstancia().getColor("white"));
        g2.fillPolygon(pontosXDaSeta, pontosYDaSeta, 3);
    }
}
