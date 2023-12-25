package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.relacoes.RelacaoRemovida;
import componentes.modelos.relacoes.Relacao;
import interfacegrafica.AreaDeDiagramas;
import componentes.modelos.relacoes.TipoDeRelacao;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Classe que representa uma relação UML do tipo "Generalização". Graficamente se trata de um
 * componente simples, possuindo uma seta "oca" em uma das extremidades.
 */
public class Generalizacao extends RelacaoUML {

    public Generalizacao(AreaDeDiagramas areaDeDiagramas, TipoDeRelacao tipoDeRelacao, Relacao modeloRelacao) {
        super(new ArrayList<>(), areaDeDiagramas, null, null, tipoDeRelacao);
        setModelo(modeloRelacao);
    }

    public Generalizacao(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point primeiroPontoDaRelacao, Point ultimoPontoDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        super(linhasDaRelacao, areaDeDiagramas, primeiroPontoDaRelacao, ultimoPontoDaRelacao, tipoDeRelacao);
        mostrarSetaLadoA(true);
    }

    @Override
    protected void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas) {}

    @Override
    protected void desenharSeta(Graphics2D g2, Point[] pontosDaSeta) {
        int[] pontosXDaSeta = { pontosDaSeta[0].x, pontosDaSeta[1].x, pontosDaSeta[2].x };
        int[] pontosYDaSeta = { pontosDaSeta[0].y, pontosDaSeta[1].y, pontosDaSeta[2].y };

        g2.setStroke(new BasicStroke(8));
        g2.setColor(relacaoEstaSelecionada() ? COR_SELECIONAR : COR_PADRAO);
        g2.drawPolygon(pontosXDaSeta, pontosYDaSeta, 3);

        g2.setColor(GerenciadorDeRecursos.getInstancia().getColor("white"));
        g2.fillPolygon(pontosXDaSeta, pontosYDaSeta, 3);
    }

    @Override
    protected void initFrameGerenciarRelacao() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelGeneralizacao = new JLabel(gerenciadorDeRecursos.getString("relacao_generalizacao_maiuscula"), JLabel.CENTER);
        labelGeneralizacao.setFont(gerenciadorDeRecursos.getRobotoBlack(16));
        labelGeneralizacao.setPreferredSize(new Dimension(
            labelGeneralizacao.getPreferredSize().width * 2,
            labelGeneralizacao.getPreferredSize().height
        ));

        JPanel painelLabelGeneralizacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelGeneralizacao.setBorder(BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")
        ));
        painelLabelGeneralizacao.add(labelGeneralizacao, "align center");
        painelLabelGeneralizacao.setOpaque(false);

        // ----------------------------------------------------------------------------

        JPanel painelTrocarSentido = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelTrocarSentido.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelTrocarSentido.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));

        JLabel labelTrocarSentido = new JLabel(gerenciadorDeRecursos.getString("relacao_trocar_sentido"));
        labelTrocarSentido.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelTrocarSentido.setForeground(gerenciadorDeRecursos.getColor("black"));

        // ----------------------------------------------------------------------------

        JPanel painelExcluirRelacao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelExcluirRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelExcluirRelacao.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));

        JLabel labelExcluirRelacao = new JLabel("Excluir Relação");
        labelExcluirRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelExcluirRelacao.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelExcluirRelacao.add(labelExcluirRelacao, "align center");
        painelTrocarSentido.add(labelTrocarSentido, "align center");

        // ----------------------------------------------------------------------------

        MouseAdapter adapterCorDosBotoes = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(gerenciadorDeRecursos.getColor("raisin_black"));
                ((JPanel) e.getSource()).getComponent(0).setForeground(gerenciadorDeRecursos.getColor("white"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(gerenciadorDeRecursos.getColor("white"));
                ((JPanel) e.getSource()).getComponent(0).setForeground(gerenciadorDeRecursos.getColor("black"));
            }
        };

        painelTrocarSentido.addMouseListener(adapterCorDosBotoes);
        painelExcluirRelacao.addMouseListener(adapterCorDosBotoes);

        // ----------------------------------------------------------------------------

        painelTrocarSentido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inverterSeta();
            }
        });

        painelExcluirRelacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarFrameGerenciarRelacao(false);
                removerRelacaoDoQuadroBranco();
                getAreaDeDiagramas().addAlteracaoDeComponente(new RelacaoRemovida(Generalizacao.this));
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelInteriorGerenciarRelacao.add(painelTrocarSentido, "wrap, gapbottom 8");
        painelInteriorGerenciarRelacao.add(painelExcluirRelacao, "wrap, grow");

        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelGerenciarRelacao.add(painelLabelGeneralizacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");

        getFrameGerenciarRelacao().add(painelGerenciarRelacao);
        getFrameGerenciarRelacao().pack();
    }
}
