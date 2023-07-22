package ComponentesUML;

import AlteracoesDeElementos.ComponenteMovido;
import AlteracoesDeElementos.ComponenteRemovido;
import ClassesAuxiliares.RobotoFont;
import DiagramaUML.DiagramaUML;
import RelacoesUML.RelacaoUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ComponenteUML {

    private final DiagramaUML diagramaUML;
    private final JPanel painelComponente;


    private final JPanel painelOpcoesComponenteUML;

    private final JPanel glassPaneComponente;

    private final JFrame frameGerenciarComponente;
    private int largura;
    private int altura;


    private final ArrayList<JPanel> listaAreasDeConexao = new ArrayList<>();

    public ComponenteUML(DiagramaUML diagramaUML) {
        this.diagramaUML = diagramaUML;

        this.frameGerenciarComponente = new JFrame();
        this.frameGerenciarComponente.setResizable(false);
        this.frameGerenciarComponente.setTitle("Configurar Componente");
        this.frameGerenciarComponente.setIconImage(new ImageIcon(ComponenteUML.class.getResource("/imagens/icone_configurar_componente.png")).getImage());

        this.painelComponente = new JPanel(null);
        this.painelComponente.setOpaque(false);
        this.painelComponente.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        this.glassPaneComponente = new JPanel(null);
        this.glassPaneComponente.setOpaque(false);

        // =======================================================================================================

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelExcluirComponente = new JPanel(new MigLayout("debug, insets 5 0 10 0"));
        painelExcluirComponente.setBackground(Color.white);

        JLabel labelImgInterrogacao = new JLabel(new ImageIcon(ComponenteUML.class.getResource("/imagens/img_interrogacao_pequena.png")));
        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgInterrogacao.setBackground(new Color(0x1d2021));
        painelImgInterrogacao.add(labelImgInterrogacao, "align center");

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelMensagem = new JLabel("Deseja realmente excluir o componente?");
        labelMensagem.setFont(robotoFont.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 25, 0, 25),
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)
        ));

        painelMensagem.add(labelMensagem, "align center");


        JPanel painelRespostaSim = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaSim.setBackground(Color.white);
        painelRespostaSim.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaSim = new JLabel("Sim");
        labelRespostaSim.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaSim.setForeground(Color.black);


        JPanel painelRespostaNao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaNao.setBackground(Color.white);
        painelRespostaNao.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaNao = new JLabel("Não");
        labelRespostaNao.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaNao.setForeground(Color.black);


        painelRespostaSim.add(labelRespostaSim, "align center");
        painelRespostaNao.add(labelRespostaNao, "align center");



        painelExcluirComponente.add(painelImgInterrogacao, "west");
        painelExcluirComponente.add(painelMensagem, "wrap");
        painelExcluirComponente.add(painelRespostaSim, "gaptop 15, split 2, gapleft: push, gapright: push");
        painelExcluirComponente.add(painelRespostaNao, "gaptop 15, gapleft: push, gapright: push");



        JDialog jDialogExcluirComponente = new JDialog();
        jDialogExcluirComponente.setTitle("Excluir Componente");
        jDialogExcluirComponente.setContentPane(painelExcluirComponente);
        jDialogExcluirComponente.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialogExcluirComponente.setResizable(false);
        jDialogExcluirComponente.pack();

        MouseAdapter mouseAdapter = new MouseAdapter() {
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
                jDialogExcluirComponente.setVisible(false);
            }

        };

        painelRespostaSim.addMouseListener(mouseAdapter);
        painelRespostaNao.addMouseListener(mouseAdapter);


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                diagramaUML.getAreaDeDiagramas().addAlteracao(new ComponenteRemovido(
                        painelComponente.getX(), painelComponente.getY(),
                        ComponenteUML.this
                ));
                diagramaUML.removerComponente(ComponenteUML.this);
                frameGerenciarComponente.setVisible(false);
            }
        };

        painelRespostaSim.addMouseListener(mouseAdapter);


        // =======================================================================================================

        // tirar magic nubmer e colocar constantes
        int TAMANHO_LADO_AREA_DE_CONEXAO = 12;

        JPanel painelAreaConexaoNO = new JPanel();
        painelAreaConexaoNO.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoNO.setBackground(Color.red);
        painelAreaConexaoNO.setBounds(0, 0, TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoNO.setVisible(false);

        JPanel painelAreaConexaoN = new JPanel();
        painelAreaConexaoN.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoN.setBackground(Color.red);
        painelAreaConexaoN.setBounds(getLargura()/2 - TAMANHO_LADO_AREA_DE_CONEXAO/2, 0, TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoN.setVisible(false);

        JPanel painelAreaConexaoNE = new JPanel();
        painelAreaConexaoNE.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoNE.setBackground(Color.red);
        painelAreaConexaoNE.setBounds(getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO, 0,
                TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoNE.setVisible(false);

        JPanel painelAreaConexaoO = new JPanel();
        painelAreaConexaoO.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoO.setBackground(Color.red);
        painelAreaConexaoO.setBounds(0, getAltura()/2 - TAMANHO_LADO_AREA_DE_CONEXAO/2,
                TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoO.setVisible(false);

        JPanel painelAreaConexaoE = new JPanel();
        painelAreaConexaoE.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoE.setBackground(Color.red);
        painelAreaConexaoE.setBounds(getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO,
                getAltura()/2 - TAMANHO_LADO_AREA_DE_CONEXAO/2, TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoE.setVisible(false);

        JPanel painelAreaConexaoSO = new JPanel();
        painelAreaConexaoSO.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoSO.setBackground(Color.red);
        painelAreaConexaoSO.setBounds(0, getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO,
                TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoSO.setVisible(false);

        JPanel painelAreaConexaoS = new JPanel();
        painelAreaConexaoS.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoS.setBackground(Color.red);
        painelAreaConexaoS.setBounds(getLargura()/2 - TAMANHO_LADO_AREA_DE_CONEXAO/2, getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO,
                TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoS.setVisible(false);

        JPanel painelAreaConexaoSE = new JPanel();
        painelAreaConexaoSE.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelAreaConexaoSE.setBackground(Color.red);
        painelAreaConexaoSE.setBounds(getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO, getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO,
                TAMANHO_LADO_AREA_DE_CONEXAO, TAMANHO_LADO_AREA_DE_CONEXAO);
        painelAreaConexaoSE.setVisible(false);

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel painelAreaDeConexao = ((JPanel) e.getSource());

                RelacaoUML ultimaRelacao =  diagramaUML.getListaRelacaoes().get(diagramaUML.getListaRelacaoes().size() - 1);

                if (painelAreaDeConexao.getBackground() == Color.green) {

                    if (ultimaRelacao.getComponenteOrigem() != null && ultimaRelacao.getComponenteOrigem().equals(ComponenteUML.this)) {
                        ultimaRelacao.setComponenteOrigem(null);
                    } else {
                        ultimaRelacao.setComponenteDestino(null);
                    }

                    JLayeredPane painelCamadasQuadroBranco = ((JLayeredPane) diagramaUML.getAreaDeDiagramas().
                            getPainelAreaDeDiagramas().getComponent(3));

                    painelCamadasQuadroBranco.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)[2].setVisible(false);

                    ultimaRelacao.excluirRelacao();

                    painelAreaDeConexao.setBackground(Color.red);

                } else if (ultimaRelacao.getComponenteOrigem() == null) {
                    ultimaRelacao.setAreaDeConexaoOrigem(RelacaoUML.AreasDeConexao.values()[listaAreasDeConexao.indexOf(painelAreaDeConexao)]);
                    ultimaRelacao.setComponenteOrigem(ComponenteUML.this);

                    painelAreaDeConexao.setBackground(Color.green);

                } else if (ultimaRelacao.getComponenteDestino() == null) {
                    ultimaRelacao.setAreaDeConexaoDestino(RelacaoUML.AreasDeConexao.values()[listaAreasDeConexao.indexOf(painelAreaDeConexao)]);
                    ultimaRelacao.setComponenteDestino(ComponenteUML.this);

                    painelAreaDeConexao.setBackground(Color.green);


                    JLayeredPane painelCamadasQuadroBranco = ((JLayeredPane) diagramaUML.getAreaDeDiagramas().
                            getPainelAreaDeDiagramas().getComponent(3));

                    boolean previewBemSucessido = ultimaRelacao.mostrarPreview();

                    if (!previewBemSucessido) {
                        painelCamadasQuadroBranco.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)[3].setVisible(true);
                    } else {
                        painelCamadasQuadroBranco.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)[2].setVisible(true);
                    }

                }
            }
        };

        painelAreaConexaoNO.addMouseListener(mouseAdapter);
        painelAreaConexaoN.addMouseListener(mouseAdapter);
        painelAreaConexaoNE.addMouseListener(mouseAdapter);
        painelAreaConexaoO.addMouseListener(mouseAdapter);
        painelAreaConexaoE.addMouseListener(mouseAdapter);
        painelAreaConexaoSO.addMouseListener(mouseAdapter);
        painelAreaConexaoS.addMouseListener(mouseAdapter);
        painelAreaConexaoSE.addMouseListener(mouseAdapter);


        listaAreasDeConexao.add(painelAreaConexaoNO);
        listaAreasDeConexao.add(painelAreaConexaoN);
        listaAreasDeConexao.add(painelAreaConexaoNE);
        listaAreasDeConexao.add(painelAreaConexaoO);
        listaAreasDeConexao.add(painelAreaConexaoE);
        listaAreasDeConexao.add(painelAreaConexaoSO);
        listaAreasDeConexao.add(painelAreaConexaoS);
        listaAreasDeConexao.add(painelAreaConexaoSE);


        glassPaneComponente.add(painelAreaConexaoNO);
        glassPaneComponente.add(painelAreaConexaoN);
        glassPaneComponente.add(painelAreaConexaoNE);
        glassPaneComponente.add(painelAreaConexaoO);
        glassPaneComponente.add(painelAreaConexaoE);
        glassPaneComponente.add(painelAreaConexaoSO);
        glassPaneComponente.add(painelAreaConexaoS);
        glassPaneComponente.add(painelAreaConexaoSE);

        // =======================================================================================================

        painelOpcoesComponenteUML = new JPanel(new MigLayout("insets 0 n n n n"));
        painelOpcoesComponenteUML.setOpaque(false);


        JButton botaoOpcoes = new JButton(new ImageIcon(ComponenteUML.class.getResource("/imagens/img_opcoes_componente.png")));
        botaoOpcoes.setFocusable(false);
        botaoOpcoes.addActionListener(e -> {
            frameGerenciarComponente.setVisible(true);
            frameGerenciarComponente.setLocationRelativeTo(null);
            frameGerenciarComponente.requestFocusInWindow();
        });

        JButton botaoDeletar = new JButton(new ImageIcon(DiagramaUML.class.getResource("/imagens/img_excluir_componente.png")));
        botaoDeletar.setFocusable(false);
        botaoDeletar.addActionListener(e -> {
            jDialogExcluirComponente.setLocationRelativeTo(null);
            jDialogExcluirComponente.setVisible(true);
        });

        painelOpcoesComponenteUML.add(botaoOpcoes, "wrap");
        painelOpcoesComponenteUML.add(botaoDeletar);

        mouseAdapter = new MouseAdapter() {
            private int posicaoMouseX, posicaoMouseY, posicaoPainelX, posicaoPainelY;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida() &&
                        !diagramaUML.getAreaDeDiagramas().isSelecaoDeRelacionamentoAcontecendo()) {

                    if (painelOpcoesComponenteUML.getParent() == null) {
                        int LARGURA_PAINEL_OPCOES = 40;

                        painelComponente.add(painelOpcoesComponenteUML);
                        painelOpcoesComponenteUML.setBounds(painelComponente.getWidth(), 0,
                                LARGURA_PAINEL_OPCOES, painelComponente.getHeight());

                        painelComponente.setSize(painelComponente.getWidth() + LARGURA_PAINEL_OPCOES,
                                painelComponente.getHeight());
                        painelComponente.revalidate();
                        painelComponente.repaint();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                int posicaoXpainelComponente = painelComponente.getLocationOnScreen().x;
                int posicaoYpainelComponente = painelComponente.getLocationOnScreen().y;

                //                 if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida()) { ??????????

                if (e.getXOnScreen() <= posicaoXpainelComponente ||
                        e.getXOnScreen() >= posicaoXpainelComponente + painelComponente.getBounds().width ||
                        e.getYOnScreen() <= posicaoYpainelComponente ||
                        e.getYOnScreen() >= posicaoYpainelComponente + painelComponente.getBounds().height) {

                    if (painelOpcoesComponenteUML.getParent() != null) {
                        int LARGURA_PAINEL_OPCOES = 40;

                        painelComponente.remove(painelOpcoesComponenteUML);
                        painelComponente.setSize(painelComponente.getWidth() - LARGURA_PAINEL_OPCOES,
                                painelComponente.getHeight());
                        painelComponente.revalidate();
                        painelComponente.repaint();
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida() &&
                        !diagramaUML.getAreaDeDiagramas().isSelecaoDeRelacionamentoAcontecendo()) {

                    posicaoPainelX = painelComponente.getX();
                    posicaoPainelY = painelComponente.getY();

                    posicaoMouseX = e.getXOnScreen();
                    posicaoMouseY = e.getYOnScreen();

                    painelComponente.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida()) {
                    painelComponente.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    diagramaUML.getAreaDeDiagramas().addAlteracao(new ComponenteMovido(
                            posicaoPainelX, posicaoPainelY,
                            painelComponente.getX(), painelComponente.getY(),
                            ComponenteUML.this));

                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida()  &&
                        !diagramaUML.getAreaDeDiagramas().isSelecaoDeRelacionamentoAcontecendo()) {

                    // diferenca entre a posicao atual do mouse e a posicao antiga
                    int diferencaMouseX = e.getXOnScreen() - posicaoMouseX;
                    int diferencaMouseY = e.getYOnScreen() - posicaoMouseY;

                    Rectangle retangulo = ((JViewport) painelComponente.getParent().getParent()).getViewRect().getBounds();

                    // espaco de tolerancia -> magic numbers

                    int LIMITE_DIREITO_X = 5000 - painelComponente.getWidth();

                    int LIMITE_INFERIOR_Y = 5000 - painelComponente.getHeight();

                    int tempX = posicaoPainelX + diferencaMouseX;
                    int tempY = posicaoPainelY + diferencaMouseY;


                    if (tempX <= 0 || tempX >= LIMITE_DIREITO_X ||
                            tempY <= 0 || tempY >= LIMITE_INFERIOR_Y) {
                    } else {
                        // o painel do componente é movido pela diferenca calculada acima
                        painelComponente.setLocation(posicaoPainelX + diferencaMouseX, posicaoPainelY + diferencaMouseY);
                    }

                }
            }
        };

        this.glassPaneComponente.addMouseListener(mouseAdapter);
        this.glassPaneComponente.addMouseMotionListener(mouseAdapter);


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                int posicaoXpainelComponente = painelComponente.getLocationOnScreen().x;
                int posicaoYpainelComponente = painelComponente.getLocationOnScreen().y;

                if (e.getXOnScreen() <= posicaoXpainelComponente ||
                        e.getXOnScreen() >= posicaoXpainelComponente + painelComponente.getBounds().width ||
                        e.getYOnScreen() <= posicaoYpainelComponente ||
                        e.getYOnScreen() >= posicaoYpainelComponente + painelComponente.getBounds().height) {

                    if (painelOpcoesComponenteUML.getParent() != null) {
                        int LARGURA_PAINEL_OPCOES = 40;

                        painelComponente.remove(painelOpcoesComponenteUML);
                        painelComponente.setSize(painelComponente.getWidth() - LARGURA_PAINEL_OPCOES,
                                painelComponente.getHeight());
                        painelComponente.revalidate();
                        painelComponente.repaint();
                    }
                }
            }
        };

        painelOpcoesComponenteUML.addMouseListener(mouseAdapter);
        botaoDeletar.addMouseListener(mouseAdapter);
        botaoOpcoes.addMouseListener(mouseAdapter);

        this.initFrameGerenciarComponente();

    }

    public JPanel getPainelComponente() {
        return painelComponente;
    }

    public JPanel getGlassPaneComponente() {
        return glassPaneComponente;
    }

    public JFrame getFrameGerenciarComponente() {
        return frameGerenciarComponente;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    abstract void initFrameGerenciarComponente();

    abstract HashMap<String, Object> obterInformacoes();

    public void atualizarAreasDeConexao() {
        // =======================================================================================================

        listaAreasDeConexao.get(0).setBounds(0, 0, 12, 12);

        listaAreasDeConexao.get(1).setBounds(getLargura()/2 - 6, 0, 12, 12);

        listaAreasDeConexao.get(2).setBounds(getLargura() - 12, 0, 12, 12);

        listaAreasDeConexao.get(3).setBounds(0, getAltura()/2 - 6, 12, 12);

        listaAreasDeConexao.get(4).setBounds(getLargura() - 12, getAltura()/2 - 6, 12, 12);

        listaAreasDeConexao.get(5).setBounds(0, getAltura() - 12, 12, 12);

        listaAreasDeConexao.get(6).setBounds(getLargura()/2 - 6, getAltura() - 12, 12, 12);

        listaAreasDeConexao.get(7).setBounds(getLargura() - 12, getAltura() - 12, 12, 12);

        // =======================================================================================================

    }

    public void mostrarAreasDeConexao(boolean mostrar) {
        for (JPanel painelAreaDeConexao : listaAreasDeConexao) {
            painelAreaDeConexao.setVisible(mostrar);
        }
    }
    public JPanel getPainelOpcoesComponenteUML() {
        return painelOpcoesComponenteUML;
    }
    public ArrayList<JPanel> getListaAreasDeConexao() {
        return listaAreasDeConexao;
    }

    public DiagramaUML getDiagramaUML() {
        return diagramaUML;
    }
}


