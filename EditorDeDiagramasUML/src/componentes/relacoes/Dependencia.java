package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.relacoes.RelacaoRemovida;
import componentes.modelos.relacoes.Relacao;
import interfacegrafica.AreaDeDiagramas;
import componentes.modelos.relacoes.TipoDeRelacao;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Classe que representa uma relação UML do tipo "Dependência". Graficamente se trata de um
 * componente simples, possuindo uma seta "aberta" em uma das extremidades e linhas tracejadas.
 */
public class Dependencia extends RelacaoUML {

    public Dependencia(AreaDeDiagramas areaDeDiagramas, TipoDeRelacao tipoDeRelacao, Relacao modeloRelacao) {
        super(new ArrayList<>(), areaDeDiagramas, null, null, tipoDeRelacao);
        setModelo(modeloRelacao);
    }

    public Dependencia(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point primeiroPontoDaRelacao, Point ultimoPontoDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        super(linhasDaRelacao, areaDeDiagramas, primeiroPontoDaRelacao, ultimoPontoDaRelacao, tipoDeRelacao);
        mostrarSetaLadoA(true);
    }

    @Override
    public void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas) {
        float TAMANHO_TRACOS = 3f;
        float ESPACO_ENTRE_TRACOS = 4f;

        for (JPanel linha : linhas) {
            linha.setOpaque(false);

            linha.setBorder(new CompoundBorder(
                // Se a largura for TAMANHO_LINHAS_RELACAO entao a linha eh vertical
                linha.getWidth() == TAMANHO_LINHAS_RELACAO ?
                    // Garantindo que so um dos lados da linha tenha borda
                    BorderFactory.createEmptyBorder(-TAMANHO_LINHAS_RELACAO, 0, -TAMANHO_LINHAS_RELACAO, -TAMANHO_LINHAS_RELACAO) :
                    BorderFactory.createEmptyBorder(0, -TAMANHO_LINHAS_RELACAO, -TAMANHO_LINHAS_RELACAO, -TAMANHO_LINHAS_RELACAO),

                BorderFactory.createDashedBorder(
                    linha.getBackground(), TAMANHO_LINHAS_RELACAO,
                    TAMANHO_TRACOS, ESPACO_ENTRE_TRACOS, false
                )
            ));
        }
    }

    @Override
    public void desenharSeta(Graphics2D g2, Point[] pontosDaSeta) {
        int[] pontosXDaSeta = { pontosDaSeta[0].x, pontosDaSeta[1].x, pontosDaSeta[2].x };
        int[] pontosYDaSeta = { pontosDaSeta[0].y, pontosDaSeta[1].y, pontosDaSeta[2].y };

        g2.setStroke(new BasicStroke(3));
        g2.setColor(relacaoEstaSelecionada() ? COR_SELECIONAR : COR_PADRAO);
        g2.drawLine(pontosXDaSeta[0], pontosYDaSeta[0], pontosXDaSeta[1], pontosYDaSeta[1]);
        g2.drawLine(pontosXDaSeta[1], pontosYDaSeta[1], pontosXDaSeta[2], pontosYDaSeta[2]);
    }

    @Override
    protected void initFrameGerenciarRelacao() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelDependencia = new JLabel(gerenciadorDeRecursos.getString("relacao_dependencia_maiuscula"), JLabel.CENTER);
        labelDependencia.setFont(gerenciadorDeRecursos.getRobotoBlack(16));
        labelDependencia.setPreferredSize(new Dimension(
                labelDependencia.getPreferredSize().width * 2,
                labelDependencia.getPreferredSize().height
        ));

        JPanel painelLabelDependencia = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelDependencia.setBorder(BorderFactory.createMatteBorder(
                0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")
        ));
        painelLabelDependencia.add(labelDependencia, "align center");
        painelLabelDependencia.setOpaque(false);

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
                getAreaDeDiagramas().addAlteracaoDeComponente(new RelacaoRemovida(Dependencia.this));
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelInteriorGerenciarRelacao.add(painelTrocarSentido, "wrap, gapbottom 8");
        painelInteriorGerenciarRelacao.add(painelExcluirRelacao, "wrap, grow");

        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelGerenciarRelacao.add(painelLabelDependencia, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");

        getFrameGerenciarRelacao().add(painelGerenciarRelacao);
        getFrameGerenciarRelacao().pack();
    }
}
