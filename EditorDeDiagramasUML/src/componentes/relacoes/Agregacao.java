package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.modelos.relacoes.DirecaoDeRelacao;
import componentes.modelos.relacoes.TipoDeRelacao;
import interfacegrafica.AreaDeDiagramas;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Agregacao extends RelacaoUML {
    public Agregacao(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point primeiroPontoDaRelacao, Point ultimoPontoDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        super(linhasDaRelacao, areaDeDiagramas, primeiroPontoDaRelacao, ultimoPontoDaRelacao, tipoDeRelacao);
        mostrarSetaLadoB(true);
    }

    @Override
    protected void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas) {}

    @Override
    protected void desenharSeta(Graphics2D g2, Point[] pontosDaSeta) {
        // A seta nesse caso na verdade vai ser um diamente
        
        Point[] pontosDoDiamante = {
            new Point(), pontosDaSeta[0].getLocation(), pontosDaSeta[1].getLocation(), pontosDaSeta[2].getLocation()
        };

        int MARGEM = 2;

        if (pontosDaSeta[2].y < pontosDaSeta[0].y) {
            // os pontos fazem parte de uma seta que aponta para o LESTE ou OESTE
            pontosDoDiamante[0].setLocation(pontosDaSeta[0].x, pontosDaSeta[1].y);
            pontosDoDiamante[3].translate(-((pontosDaSeta[2].x - pontosDaSeta[1].x) / 2), MARGEM);
            pontosDoDiamante[1].translate(-((pontosDaSeta[0].x - pontosDaSeta[1].x) / 2), -MARGEM);
        } else {
            // a seta aponta para o SUL ou NORTE
            pontosDoDiamante[0].setLocation(pontosDaSeta[1].x, pontosDaSeta[0].y);
            pontosDoDiamante[3].translate(-MARGEM, -((pontosDaSeta[2].y - pontosDaSeta[1].y) / 2));
            pontosDoDiamante[1].translate(MARGEM, -((pontosDaSeta[0].y - pontosDaSeta[1].y) / 2));
        }

        int[] pontosXDoDiamente = { pontosDoDiamante[0].x, pontosDoDiamante[1].x, pontosDoDiamante[2].x, pontosDoDiamante[3].x };
        int[] pontosYDoDiamante = { pontosDoDiamante[0].y, pontosDoDiamante[1].y, pontosDoDiamante[2].y, pontosDoDiamante[3].y };

        g2.setStroke(new BasicStroke(8));
        g2.setColor(relacaoEstaSelecionada() ? COR_SELECIONAR : COR_PADRAO);
        g2.drawPolygon(pontosXDoDiamente, pontosYDoDiamante, 4);

        g2.setColor(GerenciadorDeRecursos.getInstancia().getColor("white"));
        g2.fillPolygon(pontosXDoDiamente, pontosYDoDiamante, 4);
    }

    @Override
    protected void initFrameGerenciarRelacao() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelAgregacao = new JLabel(gerenciadorDeRecursos.getString("relacao_agregacao_maiuscula"), JLabel.CENTER);
        labelAgregacao.setFont(gerenciadorDeRecursos.getRobotoBlack(16));
        labelAgregacao.setPreferredSize(new Dimension(
                labelAgregacao.getPreferredSize().width * 2,
                labelAgregacao.getPreferredSize().height
        ));

        JPanel painelAgregacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelAgregacao.setBorder(BorderFactory.createMatteBorder(
                0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")
        ));
        painelAgregacao.add(labelAgregacao, "align center");
        painelAgregacao.setOpaque(false);

        // ----------------------------------------------------------------------------

        JLabel labelDirecao = new JLabel(gerenciadorDeRecursos.getString("relacao_direcao"));
        labelDirecao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        JComboBox<String> comboBoxDirecao = new JComboBox<>();
        comboBoxDirecao.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        for (DirecaoDeRelacao direcao : DirecaoDeRelacao.values()) {
            comboBoxDirecao.addItem(direcao.getNome());
        }
        comboBoxDirecao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        comboBoxDirecao.setFocusable(false);
        comboBoxDirecao.addItemListener(e -> mostrarDirecao(DirecaoDeRelacao.getDirecaoPorNome((String) e.getItem())));

        // ----------------------------------------------------------------------------

        JLabel labelNomeRelacao = new JLabel(gerenciadorDeRecursos.getString("nome"));
        labelNomeRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        JTextField textFieldNomeRelacao = new JTextField();
        textFieldNomeRelacao.setBorder(BorderFactory.createCompoundBorder(
                textFieldNomeRelacao.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textFieldNomeRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomeRelacao.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomeRelacao.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarNomeRelacao();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarNomeRelacao();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarNomeRelacao();
            }

            private void atualizarNomeRelacao() {
                String novoNome = textFieldNomeRelacao.getText();

                if (!novoNome.isEmpty()) {
                    setNome(novoNome);
                    mostrarNome(true);
                } else {
                    mostrarNome(false);
                }
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelLadoA = new JLabel(gerenciadorDeRecursos.getString("lado_a"), JLabel.CENTER);
        labelLadoA.setFont(gerenciadorDeRecursos.getRobotoBlack(15));

        JLabel labelLadoB = new JLabel(gerenciadorDeRecursos.getString("lado_b"), JLabel.CENTER);
        labelLadoB.setFont(gerenciadorDeRecursos.getRobotoBlack(15));

        // ----------------------------------------------------------------------------

        JLabel labelMultiplicidade = new JLabel(gerenciadorDeRecursos.getString("multiplicidade"), JLabel.CENTER);
        labelMultiplicidade.setFont(gerenciadorDeRecursos.getRobotoBlack(14));

        JTextField textFieldMultiplicidadeA = new JTextField();
        textFieldMultiplicidadeA.setFont(gerenciadorDeRecursos.getRobotoMedium(13));
        textFieldMultiplicidadeA.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldMultiplicidadeA.setBorder(BorderFactory.createCompoundBorder(
                textFieldMultiplicidadeA.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textFieldMultiplicidadeA.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarMultiplicidadeA();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarMultiplicidadeA();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarMultiplicidadeA();
            }

            private void atualizarMultiplicidadeA() {
                String multiplicidade = textFieldMultiplicidadeA.getText();

                if (!multiplicidade.isEmpty()) {
                    setMultiplicidadeLadoA(multiplicidade);
                    mostrarMultiplicidadeLadoA(true);
                } else {
                    mostrarMultiplicidadeLadoA(false);
                }
            }
        });

        JTextField textFieldMultiplicidadeB = new JTextField();
        textFieldMultiplicidadeB.setFont(gerenciadorDeRecursos.getRobotoMedium(13));
        textFieldMultiplicidadeB.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldMultiplicidadeB.setBorder(BorderFactory.createCompoundBorder(
                textFieldMultiplicidadeB.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textFieldMultiplicidadeB.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarMultiplicidadeB();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarMultiplicidadeB();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarMultiplicidadeB();
            }

            private void atualizarMultiplicidadeB() {
                String multiplicidade = textFieldMultiplicidadeB.getText();

                if (!multiplicidade.isEmpty()) {
                    setMultiplicidadeLadoB(multiplicidade);
                    mostrarMultiplicidadeLadoB(true);
                } else {
                    mostrarMultiplicidadeLadoB(false);
                }
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelTrocarSentido = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelTrocarSentido.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelTrocarSentido.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));

        JLabel labelTrocarSentido = new JLabel(gerenciadorDeRecursos.getString("relacao_trocar_sentido"));
        labelTrocarSentido.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelTrocarSentido.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelTrocarSentido.add(labelTrocarSentido, "align center");
        painelTrocarSentido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inverterSeta();
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelExcluirRelacao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelExcluirRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelExcluirRelacao.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));
        painelExcluirRelacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarFrameGerenciarRelacao(false);
                removerRelacaoDoQuadroBranco();
            }
        });

        JLabel labelExcluirRelacao = new JLabel(gerenciadorDeRecursos.getString("excluir_relacao"));
        labelExcluirRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelExcluirRelacao.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelExcluirRelacao.add(labelExcluirRelacao, "align center");

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

        painelExcluirRelacao.addMouseListener(adapterCorDosBotoes);
        painelTrocarSentido.addMouseListener(adapterCorDosBotoes);

        // ----------------------------------------------------------------------------

        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[][grow, fill][grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelInteriorGerenciarRelacao.add(labelNomeRelacao, "span 3, split 2, gapbottom 8, gapright 12");
        painelInteriorGerenciarRelacao.add(textFieldNomeRelacao, "gapbottom 8, wrap, grow");
        painelInteriorGerenciarRelacao.add(labelDirecao, "span 3, split 2, gapbottom 8, gapright 12");
        painelInteriorGerenciarRelacao.add(comboBoxDirecao, "gapbottom 8, wrap, grow");
        painelInteriorGerenciarRelacao.add(labelLadoA, "skip 1, growx 0, gapbottom 8, gaptop 8, align center");
        painelInteriorGerenciarRelacao.add(labelLadoB, "gapbottom 8, wrap, growx 0,  gaptop 8, align center");
        painelInteriorGerenciarRelacao.add(labelMultiplicidade, "gapbottom 8, gapright 18, growx 0");
        painelInteriorGerenciarRelacao.add(textFieldMultiplicidadeA, "gapbottom 8");
        painelInteriorGerenciarRelacao.add(textFieldMultiplicidadeB, "gapbottom 8, wrap");

        painelInteriorGerenciarRelacao.add(painelTrocarSentido, "span 3, wrap, grow, gaptop 8");
        painelInteriorGerenciarRelacao.add(painelExcluirRelacao, "span 3, wrap, grow, gaptop 8");

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelGerenciarRelacao.add(painelAgregacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");

        getFrameGerenciarRelacao().add(painelGerenciarRelacao);
        getFrameGerenciarRelacao().pack();
    }
}
