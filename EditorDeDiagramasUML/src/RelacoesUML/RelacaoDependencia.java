package RelacoesUML;

import componentes.alteracoes.RelacaoModificada;
import ClassesAuxiliares.RobotoFont;
import componentes.ComponenteUML;
import diagrama.DiagramaUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RelacaoDependencia extends RelacaoUML {
    private final JLabel labelSetaDependencia = new JLabel();
    private String sentidoDaSeta;

    public RelacaoDependencia(DiagramaUML diagramaUML) {
        super(diagramaUML);

    }

    @Override
    void colocarEstiloDePreview() {
        Rectangle boundsPainelComponenteDestino = getComponenteDestino().getPainelComponente().getBounds();
        Rectangle boundsPainelLinha;

        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;
        float TAMANHO_TRACOS = 2f;
        float ESPACO_ENTRE_TRACOS = 3.7f;

        for (JComponent painelLinhaRelacao : super.getListaPaineisRelacao()) {
            painelLinhaRelacao.setOpaque(false);
            painelLinhaRelacao.setBorder(BorderFactory.createDashedBorder(new Color(0xa8a8a8), TAMANHO_LINHA_DE_RELACIONAMENTO,
                    TAMANHO_TRACOS, ESPACO_ENTRE_TRACOS, false));

            boundsPainelLinha = painelLinhaRelacao.getBounds();

            if (boundsPainelLinha.intersects(boundsPainelComponenteDestino)) {
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
                alturaSeta = 22;

                pontoDeConexaoDestino.translate(
                        -(alturaSeta - TAMANHO_LADO_AREA_DE_CONEXAO),
                        TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield pontoDeConexaoDestino;
            }
            case "Sul" -> {
                alturaSeta = 22;

                pontoDeConexaoDestino.translate(
                        -(alturaSeta - TAMANHO_LADO_AREA_DE_CONEXAO),
                        -TAMANHO_LADO_AREA_DE_CONEXAO/2 - alturaSeta
                );
                yield pontoDeConexaoDestino;
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

        labelSetaDependencia.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaAbertaNortePreview.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaAbertaSulPreview.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaAbertaLestePreview.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/previewDeRelacoes/setaAbertaOestePreview.png");
            default -> throw new IllegalStateException("Erro");
        }));

        System.out.println(sentidoDaSeta);
        labelSetaDependencia.setLocation(localizacaoDaSeta);
        labelSetaDependencia.setSize(labelSetaDependencia.getPreferredSize());

        super.getListaPaineisRelacao().add(0, labelSetaDependencia);
    }

    @Override
    public void colocarEstiloFinal() {
        for (JComponent componenteDeRelacao : super.getListaPaineisRelacao()) {
            if (componenteDeRelacao instanceof JPanel) {
                int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;
                float TAMANHO_TRACOS = 2f;
                float ESPACO_ENTRE_TRACOS = 3.7f;

                componenteDeRelacao.setOpaque(false);
                componenteDeRelacao.setBorder(BorderFactory.createDashedBorder(new Color(0x323232), TAMANHO_LINHA_DE_RELACIONAMENTO,
                        TAMANHO_TRACOS, ESPACO_ENTRE_TRACOS, false));
            }
        }

        labelSetaDependencia.setIcon(new ImageIcon( switch (sentidoDaSeta) {
            case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaNorteFinal.png");
            case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaSulFinal.png");
            case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaLesteFinal.png");
            case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaOesteFinal.png");
            default -> throw new IllegalStateException("Erro");
        }));

        super.getDiagramaUML().getAreaDeDiagramas().addRelacionametoAoQuadro(super.getListaPaineisRelacao());
    }

    @Override
    public void adicionarComportamentoArelacao() {

        frameGerenciarRelacao = new JFrame();
        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle("Configurar Relação");
        frameGerenciarRelacao.setIconImage(new ImageIcon(RelacaoDependencia.class.getResource("/imagens/icones/configurar_48.png")).getImage());

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelGerenciarRelacao = new JPanel(new MigLayout("insets 20 0 10 0", "","[grow, fill]"));
        painelGerenciarRelacao.setBackground(Color.white);

        // ==========================================================================================================

        JLabel labelDependencia = new JLabel("DEPENDÊNCIA", JLabel.CENTER);
        labelDependencia.setFont(robotoFont.getRobotoBlack(14f));
        labelDependencia.setPreferredSize(new Dimension(labelDependencia.getPreferredSize().width*2, labelDependencia.getPreferredSize().height));

        JPanel painelLabelDependencia = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelDependencia.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelLabelDependencia.add(labelDependencia, "align center");
        painelLabelDependencia.setOpaque(false);



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


                getListaPaineisRelacao().remove(labelSetaDependencia);

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
                        RelacaoDependencia.this));
                getDiagramaUML().removerRelacao(RelacaoDependencia.this);
            }
        });







        JPanel painelInteriorGerenciarRelacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelInteriorGerenciarRelacao.setBackground(Color.white);


        painelInteriorGerenciarRelacao.add(painelTrocarSentido, "wrap, gapbottom 8");
        painelInteriorGerenciarRelacao.add(painelExcluirRelacao, "wrap, grow");



        painelGerenciarRelacao.add(painelLabelDependencia, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarRelacao.add(painelInteriorGerenciarRelacao, "grow, wrap, gapleft 20, gapright 20");


        frameGerenciarRelacao.add(painelGerenciarRelacao);
        frameGerenciarRelacao.pack();







        MouseAdapter mouseAdapter = new MouseAdapter() {
            final int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;
            final float TAMANHO_TRACOS = 2f;
            final float ESPACO_ENTRE_TRACOS = 3.7f;
            Border bordaNaoAtiva = BorderFactory.createDashedBorder(new Color(0x323232), TAMANHO_LINHA_DE_RELACIONAMENTO,
                    TAMANHO_TRACOS, ESPACO_ENTRE_TRACOS, false);

            Border bordaAtiva = BorderFactory.createDashedBorder(Color.red, TAMANHO_LINHA_DE_RELACIONAMENTO,
                    TAMANHO_TRACOS, ESPACO_ENTRE_TRACOS, false);

            @Override
            public void mouseClicked(MouseEvent e) {
                frameGerenciarRelacao.setVisible(true);
                frameGerenciarRelacao.setLocationRelativeTo(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelSetaDependencia.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/setaAbertaNorteGerenciar.png");
                    case "Sul" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/setaAbertaSulGerenciar.png");
                    case "Leste" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/setaAbertaLesteGerenciar.png");
                    case "Oeste" -> RelacaoDependencia.class.getResource("/imagens/gerenciarRelacoes/setaAbertaOesteGerenciar.png");
                    default -> throw new IllegalStateException("Erro");
                }));

                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().revalidate();
                getDiagramaUML().getAreaDeDiagramas().getPainelQuadroBranco().repaint();

                for (JComponent componenteDeRelacao : RelacaoDependencia.super.getListaPaineisRelacao()) {
                    if (componenteDeRelacao instanceof JPanel) {
                        componenteDeRelacao.setBorder(bordaAtiva);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelSetaDependencia.setIcon(new ImageIcon( switch (sentidoDaSeta) {
                    case "Norte" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaNorteFinal.png");
                    case "Sul" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaSulFinal.png");
                    case "Leste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaLesteFinal.png");
                    case "Oeste" -> RelacaoGeneralizacao.class.getResource("/imagens/relacoesFinalizada/setaAbertaOesteFinal.png");
                    default -> throw new IllegalStateException("Erro");
                }));

                for (JComponent componenteDeRelacao : RelacaoDependencia.super.getListaPaineisRelacao()) {
                    if (componenteDeRelacao instanceof JPanel) {
                        componenteDeRelacao.setBorder(bordaNaoAtiva);
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


}
