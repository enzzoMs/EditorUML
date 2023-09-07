package RelacoesUML;

import componentes.alteracoes.RelacaoModificada;
import ClassesAuxiliares.RobotoFont;
import componentes.ComponenteUML;
import diagrama.DiagramaUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class RelacaoAgregacao extends RelacaoUML {
    private final JLabel labelSeta = new JLabel();
    private final JLabel labelNome = new JLabel();
    private final JLabel labelMultiplicidadeA = new JLabel();
    private final JLabel labelMultiplicidadeB = new JLabel();
    private String sentidoDaSeta;

    public RelacaoAgregacao(DiagramaUML diagramaUML) {
        super(diagramaUML);
    }

    @Override
    void colocarEstiloDePreview() {
        Rectangle boundsPainelComponenteDestino = getComponenteOrigem().getPainelComponente().getBounds();
        Rectangle boundsPainelLinha;
        Point localizacaoDaLinha = new Point(0,0);

        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;

        for (JComponent painelLinhaRelacao : super.getListaPaineisRelacao()) {
            painelLinhaRelacao.setBackground(new Color(0xa8a8a8));

            boundsPainelLinha = painelLinhaRelacao.getBounds();

            if (boundsPainelLinha.intersects(boundsPainelComponenteDestino)) {
                localizacaoDaLinha = painelLinhaRelacao.getLocation();

                if (painelLinhaRelacao.getWidth() == TAMANHO_LINHA_DE_RELACIONAMENTO) {
                    if (boundsPainelComponenteDestino.contains(painelLinhaRelacao.getLocation())) {
                        sentidoDaSeta = "Norte";
                    } else {
                        sentidoDaSeta = "Sul";
                    }
                } else {
                    if (boundsPainelComponenteDestino.contains(painelLinhaRelacao.getLocation())) {
                        sentidoDaSeta = "Oeste";
                    } else {
                        sentidoDaSeta = "Leste";
                    }
                }
            }
        }

        Point pontoDeConexaoOrigem = super.getPontoDeConexao(super.getAreaDeConexaoOrigem(), super.getComponenteOrigem());
        Point localizacaoDaSeta;

        int TAMANHO_LADO_AREA_DE_CONEXAO = super.getComponenteDestino().getListaAreasDeConexao().get(0).getWidth();

        int larguraSeta;
        int alturaSeta;


        localizacaoDaSeta = switch (sentidoDaSeta) {
            case "Norte" -> {
                larguraSeta = 22;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 + TAMANHO_LINHA_DE_RELACIONAMENTO/2,
                        pontoDeConexaoOrigem.y + TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
            }
            case "Sul" -> {
                alturaSeta = 22;
                larguraSeta = 13;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 - TAMANHO_LINHA_DE_RELACIONAMENTO,
                        pontoDeConexaoOrigem.y - TAMANHO_LADO_AREA_DE_CONEXAO/2 - alturaSeta
                );
            }
            case "Leste" -> {
                alturaSeta = 13;
                larguraSeta = 29;

                pontoDeConexaoOrigem.translate(
                        -TAMANHO_LADO_AREA_DE_CONEXAO/2 - larguraSeta,
                        -(alturaSeta - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                yield pontoDeConexaoOrigem;
            }
            case "Oeste" -> {
                alturaSeta = 13;

                pontoDeConexaoOrigem.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        -(alturaSeta - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                yield pontoDeConexaoOrigem;
            }
            default -> throw new IllegalStateException("Erro");
        };

        labelSeta.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/agregacaoNortePreview.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/agregacaoSulPreview.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/agregacaoLestePreview.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/agregacaoOestePreview.png");
            default -> throw new IllegalStateException("Erro");
        }));


        labelSeta.setLocation(localizacaoDaSeta);
        labelSeta.setSize(labelSeta.getPreferredSize());

        super.getListaPaineisRelacao().add(0, labelSeta);

    }

    @Override
    public void colocarEstiloFinal() {
        for (JComponent componenteDeRelacao : super.getListaPaineisRelacao()) {
            componenteDeRelacao.setBackground(new Color(0x323232));
            componenteDeRelacao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }


        labelSeta.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoNorteFinal.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoSulFinal.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoLesteFinal.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoOesteFinal.png");
            default -> throw new IllegalStateException("Erro");
        }));
    }

    @Override
    public void adicionarComportamentoArelacao() {
        RobotoFont robotoFont = new RobotoFont();

        labelNome.setFont(robotoFont.getRobotoMedium(15f));
        labelMultiplicidadeA.setFont(robotoFont.getRobotoMedium(13f));
        labelMultiplicidadeB.setFont(robotoFont.getRobotoMedium(13f));




        frameGerenciarRelacao = new JFrame();
        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle("Configurar Relação");
        frameGerenciarRelacao.setIconImage(new ImageIcon(ComponenteUML.class.getResource("/images/configurar_48.png")).getImage());


        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(Color.white);

        // ==========================================================================================================

        JLabel labelComposicao = new JLabel("AGREGAÇÃO", JLabel.CENTER);
        labelComposicao.setFont(robotoFont.getRobotoBlack(14f));
        labelComposicao.setPreferredSize(new Dimension(labelComposicao.getPreferredSize().width*2, labelComposicao.getPreferredSize().height));

        JPanel painelLabelAssociacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelAssociacao.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelLabelAssociacao.add(labelComposicao, "align center");
        painelLabelAssociacao.setOpaque(false);



        JLabel labelNomeRelacao = new JLabel("Nome");
        labelNomeRelacao.setFont(robotoFont.getRobotoMedium(15f));

        JTextField textFieldNomeRelacao = new JTextField();
        textFieldNomeRelacao.setBorder(BorderFactory.createCompoundBorder(
                textFieldNomeRelacao.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textFieldNomeRelacao.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomeRelacao.setForeground(new Color(0x484848));


        JLabel labelDirecao = new JLabel("Direção");
        labelDirecao.setFont(robotoFont.getRobotoMedium(15f));

        JComboBox<String> comboBoxDirecao = new JComboBox<>();
        comboBoxDirecao.setForeground(new Color(0x484848));
        comboBoxDirecao.addItem("Nenhuma");
        comboBoxDirecao.addItem("De A até B");
        comboBoxDirecao.addItem("De B até A");
        comboBoxDirecao.setFont(robotoFont.getRobotoMedium(15f));
        comboBoxDirecao.setFocusable(false);



        JLabel labelLadoA = new JLabel("Lado A", JLabel.CENTER);
        labelLadoA.setFont(robotoFont.getRobotoBlack(15f));


        JLabel labelLadoB = new JLabel("Lado B", JLabel.CENTER);
        labelLadoB.setFont(robotoFont.getRobotoBlack(15f));



        JLabel labelMultiplicidade = new JLabel("Multiplicidade", JLabel.CENTER);
        labelMultiplicidade.setFont(robotoFont.getRobotoBlack(14f));


        JTextField textFieldMultiplicidadeA = new JTextField();
        textFieldMultiplicidadeA.setBorder(BorderFactory.createCompoundBorder(
                textFieldMultiplicidadeA.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textFieldMultiplicidadeA.setFont(robotoFont.getRobotoMedium(13f));
        textFieldMultiplicidadeA.setForeground(new Color(0x484848));

        JTextField textFieldMultiplicidadeB = new JTextField();
        textFieldMultiplicidadeB.setBorder(BorderFactory.createCompoundBorder(
                textFieldMultiplicidadeB.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textFieldMultiplicidadeB.setFont(robotoFont.getRobotoMedium(13f));
        textFieldMultiplicidadeB.setForeground(new Color(0x484848));




        JPanel painelExcluirRelacao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelExcluirRelacao.setBackground(Color.white);
        painelExcluirRelacao.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelExcluirRelacao = new JLabel("Excluir Relação");
        labelExcluirRelacao.setFont(robotoFont.getRobotoMedium(14));

        painelExcluirRelacao.add(labelExcluirRelacao, "align center");


        painelExcluirRelacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(new Color(0x282626));
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.white);
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.black);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frameGerenciarRelacao.setVisible(false);
                getDiagramaUML().getAreaDeDiagramas().addAlteracao(new RelacaoModificada((ArrayList<JComponent>) getListaPaineisRelacao().clone(), RelacaoModificada.TipoDaModificao.REMOVIDA,
                        RelacaoAgregacao.this));
                getDiagramaUML().removerRelacao(RelacaoAgregacao.this);
            }
        });



        JPanel painelAplicar = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelAplicar.setBackground(Color.white);
        painelAplicar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelAplicar = new JLabel("Aplicar");
        labelAplicar.setFont(robotoFont.getRobotoMedium(14));

        painelAplicar.add(labelAplicar, "align center");


        painelAplicar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(new Color(0x282626));
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.white);
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.black);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                atualizarRelacao(
                        textFieldNomeRelacao.getText(),
                        (String) comboBoxDirecao.getSelectedItem(),
                        textFieldMultiplicidadeA.getText(),
                        textFieldMultiplicidadeB.getText()
                );
            }
        });

        JPanel painelTrocarSentido = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelTrocarSentido.setBackground(Color.white);
        painelTrocarSentido.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));
        JLabel labelTrocarSentido = new JLabel("Trocar Sentido");

        labelTrocarSentido.setFont(robotoFont.getRobotoMedium(14));
        labelTrocarSentido.setForeground(Color.black);

        painelTrocarSentido.add(labelTrocarSentido, "align center");

        painelTrocarSentido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(new Color(0x282626));
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel) e.getSource()).setBackground(Color.white);
                ((JPanel) e.getSource()).getComponent(0).setForeground(Color.black);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                ComponenteUML tempComponente = getComponenteOrigem();
                setComponenteOrigem(getComponenteDestino());
                setComponenteDestino(tempComponente);

                AreasDeConexao tempAreaDeConexao = getAreaDeConexaoOrigem();
                setAreaDeConexaoOrigem(getAreaDeConexaoDestino());
                setAreaDeConexaoDestino(tempAreaDeConexao);


                getListaPaineisRelacao().remove(labelSeta);

                colocarEstiloDePreview();
                colocarEstiloFinal();
                getDiagramaUML().getAreaDeDiagramas().addRelacionametoAoQuadro(getListaPaineisRelacao());
            }
        });




        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[][grow, fill][grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(Color.white);

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
        painelInteriorGerenciarRelacao.add(painelAplicar, "span 3, wrap, grow, gaptop 8");


        painelGerenciarRelacao.add(painelLabelAssociacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");



        frameGerenciarRelacao.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                atualizarRelacao(
                        textFieldNomeRelacao.getText(),
                        (String) comboBoxDirecao.getSelectedItem(),
                        textFieldMultiplicidadeA.getText(),
                        textFieldMultiplicidadeB.getText()
                );
            }
        });

        frameGerenciarRelacao.add(painelGerenciarRelacao);
        frameGerenciarRelacao.pack();





        // ===========================================================================================================

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frameGerenciarRelacao.setVisible(true);
                frameGerenciarRelacao.setLocationRelativeTo(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelSeta.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/agregacaoNorteGerenciar.png");
                    case "Sul" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/agregacaoSulGerenciar.png");
                    case "Leste" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/agregacaoLesteGerenciar.png");
                    case "Oeste" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/agregacaoOesteGerenciar.png");
                    default -> throw new IllegalStateException("Erro");
                }));

                for (JComponent componenteDeRelacao : RelacaoAgregacao.super.getListaPaineisRelacao()) {
                    componenteDeRelacao.setBackground(Color.red);
                }

                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().revalidate();
                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSeta.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoNorteFinal.png");
                    case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoSulFinal.png");
                    case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoLesteFinal.png");
                    case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/agregacaoOesteFinal.png");
                    default -> throw new IllegalStateException("Erro");
                }));


                for (JComponent componenteDeRelacao : RelacaoAgregacao.super.getListaPaineisRelacao()) {
                    componenteDeRelacao.setBackground(new Color(0x323232));
                }

                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().revalidate();
                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().repaint();

            }
        };

        for (JComponent componenteDeRelacao : super.getListaPaineisRelacao()) {
            componenteDeRelacao.addMouseListener(mouseAdapter);
            componenteDeRelacao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        labelSeta.addMouseListener(mouseAdapter);
    }


    private void atualizarRelacao(String nome, String direcao,
                                  String multiplicidadeA, String multiplicidadeB) {

        getDiagramaUML().getAreaDeDiagramas().removerRelacaoDoQuadro(this);

        super.getListaPaineisRelacao().remove(labelNome);
        super.getListaPaineisRelacao().remove(labelMultiplicidadeA);
        super.getListaPaineisRelacao().remove(labelMultiplicidadeB);


        if (direcao.equals("De A até B")) {
            labelNome.setIcon(new ImageIcon(RelacaoAssociacaoSimples.class.getResource("/imagens/img_seta_direcaoAB.png")));
            labelNome.setHorizontalTextPosition(JLabel.LEFT);
        } else if (direcao.equals("De B até A")) {
            labelNome.setIcon(new ImageIcon(RelacaoAssociacaoSimples.class.getResource("/imagens/img_seta_direcaoBA.png")));
            labelNome.setHorizontalTextPosition(JLabel.RIGHT);
        } else {
            labelNome.setIcon(null);
        }


        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;

        if (!nome.equals("") || !direcao.equals("Nenhuma")) {
            labelNome.setText(nome);

            JPanel painelMaiorTamanho = new JPanel();
            int tamanhoPainel;

            for (JComponent painelLinhaRelacao : super.getListaPaineisRelacao()) {
                if (painelLinhaRelacao instanceof JPanel) {
                    if (painelLinhaRelacao.getWidth() == TAMANHO_LINHA_DE_RELACIONAMENTO) {
                        tamanhoPainel = painelLinhaRelacao.getHeight();
                    } else {
                        tamanhoPainel = painelLinhaRelacao.getWidth();
                    }

                    if (painelMaiorTamanho.getWidth() == TAMANHO_LINHA_DE_RELACIONAMENTO) {
                        painelMaiorTamanho = (painelMaiorTamanho.getHeight() > tamanhoPainel)? painelMaiorTamanho : (JPanel) painelLinhaRelacao;
                    } else {
                        painelMaiorTamanho = (painelMaiorTamanho.getWidth() > tamanhoPainel)? painelMaiorTamanho : (JPanel) painelLinhaRelacao;
                    }
                }
            }

            Point localizaoNome ;

            if (painelMaiorTamanho.getWidth() == TAMANHO_LINHA_DE_RELACIONAMENTO) {
                Point localizaoPainel = painelMaiorTamanho.getLocation();
                localizaoPainel.translate(
                        TAMANHO_LINHA_DE_RELACIONAMENTO + 8,
                        painelMaiorTamanho.getHeight()/2 - labelNome.getPreferredSize().height/2
                );
                localizaoNome = localizaoPainel;
            } else {
                Point localizaoPainel = painelMaiorTamanho.getLocation();
                localizaoPainel.translate(
                        painelMaiorTamanho.getWidth()/2 - labelNome.getPreferredSize().height/2,
                        -labelNome.getPreferredSize().height
                );
                localizaoNome = localizaoPainel;
            }

            labelNome.setLocation(localizaoNome);
            labelNome.setSize(labelNome.getPreferredSize());

            super.getListaPaineisRelacao().add(0, labelNome);
        }


        if (!multiplicidadeA.equals("")) {
            labelMultiplicidadeA.setText(multiplicidadeA);
            mostrarMultiplicidade(labelMultiplicidadeA, getComponenteOrigem(), getAreaDeConexaoOrigem());
        }

        if (!multiplicidadeB.equals("")) {
            labelMultiplicidadeB.setText(multiplicidadeB);
            mostrarMultiplicidade(labelMultiplicidadeB, getComponenteDestino(), getAreaDeConexaoDestino());
        }


        getDiagramaUML().getAreaDeDiagramas().addRelacionametoAoQuadro(super.getListaPaineisRelacao());

    }


    private void mostrarMultiplicidade(JLabel labelMultiplicidade, ComponenteUML componenteUML, AreasDeConexao areasDeConexao) {
        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;

        Rectangle boundsPainelComponente = componenteUML.getPainelComponente().getBounds();
        Rectangle boundsPainelLinha;
        String posicaoNaLinha = null;
        Point localizacaoDaLinha = new Point(0,0);

        for (JComponent painelLinhaRelacao : super.getListaPaineisRelacao()) {
            boundsPainelLinha = painelLinhaRelacao.getBounds();

            if (boundsPainelLinha.intersects(boundsPainelComponente)) {
                localizacaoDaLinha = painelLinhaRelacao.getLocation();

                if (painelLinhaRelacao.getWidth() == TAMANHO_LINHA_DE_RELACIONAMENTO) {
                    if (boundsPainelComponente.contains(painelLinhaRelacao.getLocation())) {
                        posicaoNaLinha = "Norte";
                    } else {
                        posicaoNaLinha = "Sul";
                    }
                } else {
                    if (boundsPainelComponente.contains(painelLinhaRelacao.getLocation())) {
                        posicaoNaLinha = "Oeste";
                    } else {
                        posicaoNaLinha = "Leste";
                    }
                }
            }
        }

        Point pontoDeConexao = super.getPontoDeConexao(areasDeConexao, componenteUML);
        Point localizacaoDoLabel;

        int TAMANHO_LADO_AREA_DE_CONEXAO = componenteUML.getListaAreasDeConexao().get(0).getWidth();
        int MARGEM = 15;

        int larguraSeta;
        int alturaSeta;

        localizacaoDoLabel = switch (posicaoNaLinha) {
            case "Norte" -> {
                larguraSeta = 22;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 + TAMANHO_LINHA_DE_RELACIONAMENTO/2,
                        pontoDeConexao.y + TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
            }
            case "Sul" -> {
                alturaSeta = 22;
                larguraSeta = 13;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 - TAMANHO_LINHA_DE_RELACIONAMENTO,
                        pontoDeConexao.y - TAMANHO_LADO_AREA_DE_CONEXAO/2 - alturaSeta
                );
            }
            case "Leste" -> {
                pontoDeConexao.translate(
                        -TAMANHO_LADO_AREA_DE_CONEXAO/2 - labelMultiplicidade.getPreferredSize().width,
                        -(labelMultiplicidade.getPreferredSize().height - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                pontoDeConexao.translate(
                        -MARGEM,
                        -MARGEM
                );
                yield pontoDeConexao;
            }
            case "Oeste" -> {
                pontoDeConexao.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        -(labelMultiplicidade.getPreferredSize().height - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                pontoDeConexao.translate(
                        MARGEM,
                        -MARGEM
                );
                yield pontoDeConexao;
            }
            default -> throw new IllegalStateException("Erro");
        };


        labelMultiplicidade.setLocation(localizacaoDoLabel);
        labelMultiplicidade.setSize(labelMultiplicidade.getPreferredSize());

        super.getListaPaineisRelacao().add(labelMultiplicidade);
    }
}
