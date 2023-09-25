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
import java.util.ArrayList;

public class RelacaoGeneralizacao extends RelacaoUML {
    private final JLabel labelSetaGeneralizacao = new JLabel();
    private String sentidoDaSeta;


    public RelacaoGeneralizacao(DiagramaUML diagramaUML) {
        super(diagramaUML);

    }

    @Override
    void colocarEstiloDePreview() {
        Rectangle boundsPainelComponenteDestino = getComponenteDestino().getPainelComponente().getBounds();
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

        Point pontoDeConexaoDestino = super.getPontoDeConexao(super.getAreaDeConexaoDestino(), super.getComponenteDestino());
        Point localizacaoDaSeta;

        int TAMANHO_LADO_AREA_DE_CONEXAO = 0;

        //int TAMANHO_LADO_AREA_DE_CONEXAO = super.getComponenteDestino().getListaAreasDeConexao().get(0).getWidth();

        int larguraSeta;
        int alturaSeta;


        localizacaoDaSeta = switch (sentidoDaSeta) {
            case "Norte" -> {
                larguraSeta = 13;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 - TAMANHO_LINHA_DE_RELACIONAMENTO,
                        pontoDeConexaoDestino.y + TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
            }
            case "Sul" -> {
                alturaSeta = 22;
                larguraSeta = 13;

                yield new Point(
                        localizacaoDaLinha.x - larguraSeta/2 - TAMANHO_LINHA_DE_RELACIONAMENTO,
                        pontoDeConexaoDestino.y - TAMANHO_LADO_AREA_DE_CONEXAO/2 - alturaSeta
                );

            }
            case "Leste" -> {
                alturaSeta = 22;
                larguraSeta = 23;

                pontoDeConexaoDestino.translate(
                        -TAMANHO_LADO_AREA_DE_CONEXAO/2 - larguraSeta,
                        -(alturaSeta - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                yield pontoDeConexaoDestino;
            }
            case "Oeste" -> {
                alturaSeta = 22;

                pontoDeConexaoDestino.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        -(alturaSeta - TAMANHO_LINHA_DE_RELACIONAMENTO)/2
                );
                yield pontoDeConexaoDestino;
            }
            default -> throw new IllegalStateException("Erro");
        };

        labelSetaGeneralizacao.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaVaziaNortePreview.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaVaziaSulPreview.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaVaziaLestePreview.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaVaziaOestePreview.png");
            default -> throw new IllegalStateException("Erro");
        }));


        labelSetaGeneralizacao.setLocation(localizacaoDaSeta);
        labelSetaGeneralizacao.setSize(labelSetaGeneralizacao.getPreferredSize());

        super.getListaPaineisRelacao().add(0, labelSetaGeneralizacao);
    }

    @Override
    public void colocarEstiloFinal() {
        for (JComponent componenteDeRelacao : super.getListaPaineisRelacao()) {
            if (componenteDeRelacao instanceof JPanel) {
                componenteDeRelacao.setBackground(new Color(0x323232));
            }
        }

        labelSetaGeneralizacao.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaNorteFinal.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaSulFinal.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaLesteFinal.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaOesteFinal.png");
            default -> throw new IllegalStateException("Erro");
        }));

        super.getDiagramaUML().getAreaDeDiagramas().addRelacionametoAoQuadro(super.getListaPaineisRelacao());
    }

    public void adicionarComportamentoArelacao() {

        frameGerenciarRelacao = new JFrame();
        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle("Configurar Relação");
        frameGerenciarRelacao.setIconImage(new ImageIcon(ComponenteUML.class.getResource("/imagens/icones/configurar_48.png")).getImage());

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(Color.white);

        // ==========================================================================================================

        JLabel labelGeneralizacao = new JLabel("GENERALIZAÇÃO", JLabel.CENTER);
        labelGeneralizacao.setFont(robotoFont.getRobotoBlack(14f));
        labelGeneralizacao.setPreferredSize(new Dimension(labelGeneralizacao.getPreferredSize().width*2, labelGeneralizacao.getPreferredSize().height));

        JPanel painelLabelGeneralizacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelGeneralizacao.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelLabelGeneralizacao.add(labelGeneralizacao, "align center");
        painelLabelGeneralizacao.setOpaque(false);



        // !!!!!!!!!!!

        JPanel painelTrocarSentido = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelTrocarSentido.setBackground(Color.white);
        painelTrocarSentido.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelTrocarSentido = new JLabel("Trocar Sentido");
        labelTrocarSentido.setFont(robotoFont.getRobotoMedium(14));
        labelTrocarSentido.setForeground(Color.black);


        JPanel painelExcluirRelacao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelExcluirRelacao.setBackground(Color.white);
        painelExcluirRelacao.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelExcluirRelacao = new JLabel("Excluir Relação");
        labelExcluirRelacao.setFont(robotoFont.getRobotoMedium(14));
        labelExcluirRelacao.setForeground(Color.black);


        painelExcluirRelacao.add(labelExcluirRelacao, "align center");
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


                getListaPaineisRelacao().remove(labelSetaGeneralizacao);

                colocarEstiloDePreview();
                colocarEstiloFinal();
                getDiagramaUML().getAreaDeDiagramas().addRelacionametoAoQuadro(getListaPaineisRelacao());
            }
        });

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
                getDiagramaUML().getAreaDeDiagramas().addAlteracaoDeComponente(new RelacaoModificada((ArrayList<JComponent>) getListaPaineisRelacao().clone(), RelacaoModificada.TipoDaModificao.REMOVIDA,
                        RelacaoGeneralizacao.this));
                getDiagramaUML().removerRelacao(RelacaoGeneralizacao.this);
            }
        });



        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(Color.white);


        painelInteriorGerenciarRelacao.add(painelTrocarSentido, "wrap, gapbottom 8");
        painelInteriorGerenciarRelacao.add(painelExcluirRelacao, "wrap, grow");



        painelGerenciarRelacao.add(painelLabelGeneralizacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");


        frameGerenciarRelacao.add(painelGerenciarRelacao);
        frameGerenciarRelacao.pack();





        MouseAdapter mouseAdapter  = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frameGerenciarRelacao.setVisible(true);
                frameGerenciarRelacao.setLocationRelativeTo(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelSetaGeneralizacao.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/gerenciarRelacoes/setaVaziaNorteGerenciar.png");
                    case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/gerenciarRelacoes/setaVaziaSulGerenciar.png");
                    case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/gerenciarRelacoes/setaVaziaLesteGerenciar.png");
                    case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/gerenciarRelacoes/setaVaziaOesteGerenciar.png");
                    default -> throw new IllegalStateException("Erro");
                }));

                for (JComponent componenteDeRelacao : RelacaoGeneralizacao.super.getListaPaineisRelacao()) {
                    if (componenteDeRelacao instanceof JPanel) {
                        componenteDeRelacao.setBackground(Color.red);
                    }
                }

                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().revalidate();
                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSetaGeneralizacao.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaNorteFinal.png");
                    case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaSulFinal.png");
                    case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaLesteFinal.png");
                    case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaVaziaOesteFinal.png");
                    default -> throw new IllegalStateException("Erro");
                }));

                for (JComponent componenteDeRelacao : RelacaoGeneralizacao.super.getListaPaineisRelacao()) {
                    if (componenteDeRelacao instanceof JPanel) {
                        componenteDeRelacao.setBackground(new Color(0x323232));
                    }
                }

                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().revalidate();
                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().repaint();
            }
        };

        for (JComponent componenteDeRelacao : super.getListaPaineisRelacao()) {
            componenteDeRelacao.addMouseListener(mouseAdapter);
            componenteDeRelacao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public String toString() {
        StringBuilder toStringGeneralizacao = new StringBuilder();

        toStringGeneralizacao.append(sentidoDaSeta);
        toStringGeneralizacao.append("\n");

        toStringGeneralizacao.append(getListaPaineisRelacao().size() - 1);
        toStringGeneralizacao.append("\n");

        for (JComponent componenteDeRelacao : getListaPaineisRelacao()) {
            if (componenteDeRelacao instanceof JPanel) {
                toStringGeneralizacao.append(componenteDeRelacao.getX());
                toStringGeneralizacao.append("\n");
                toStringGeneralizacao.append(componenteDeRelacao.getY());
                toStringGeneralizacao.append("\n");
                toStringGeneralizacao.append(componenteDeRelacao.getWidth());
                toStringGeneralizacao.append("\n");
                toStringGeneralizacao.append(componenteDeRelacao.getHeight());
                toStringGeneralizacao.append("\n");
            }
        }

        return toStringGeneralizacao.toString();
    }

    public void setSentidoDaSeta(String sentidoDaSeta) {
        this.sentidoDaSeta = sentidoDaSeta;
    }
}
