package ComponentesUML;

import AlteracoesDeElementos.ComponenteModificado;
import ClassesAuxiliares.RobotoFont;
import DiagramaUML.DiagramaUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PacoteUML extends ComponenteUML {
    private String nome;

    private final JPanel painelNomePacote;


    private final JPanel painelAreaPacote;

    private final JLabel labelNomePacote;
    private JTextField textFieldNomePacote;
    private final int LARGURA_MINIMA_NOME_PACOTE;
    private final int ALTURA_MINIMA_NOME_PACOTE;
    private final JPanel[] listaPontosDeRedimensionamento = new JPanel[4];

    public PacoteUML(DiagramaUML diagramaUML) {
        super(diagramaUML);

        RobotoFont robotoFont = new RobotoFont();

        painelNomePacote = new JPanel(new MigLayout("fill"));
        painelNomePacote.setBackground(Color.white);
        painelNomePacote.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 0, 2, Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 13, 5, 13)
        ));


        this.nome = "Novo Pacote";

        labelNomePacote = new JLabel("Novo Pacote", JLabel.CENTER);
        labelNomePacote.setFont(robotoFont.getRobotoBlack(14));
        labelNomePacote.setOpaque(false);

        painelNomePacote.add(labelNomePacote, "north");

        LARGURA_MINIMA_NOME_PACOTE = painelNomePacote.getPreferredSize().width;
        ALTURA_MINIMA_NOME_PACOTE = painelNomePacote.getPreferredSize().height;

        painelAreaPacote = new JPanel();
        painelAreaPacote.setBackground(Color.white);
        painelAreaPacote.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));

        // ===========================================================================================================

        JPanel painelRedimensionarEsquerdo = new JPanel();
        painelRedimensionarEsquerdo.setBackground(Color.green);
        painelRedimensionarEsquerdo.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelRedimensionarEsquerdo.setPreferredSize(new Dimension(8, 15));
        painelRedimensionarEsquerdo.setVisible(false);

        JPanel painelRedimensionarDireito = new JPanel();
        painelRedimensionarDireito.setBackground(Color.green);
        painelRedimensionarDireito.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelRedimensionarDireito.setPreferredSize(new Dimension(8, 15));
        painelRedimensionarDireito.setVisible(false);

        JPanel painelRedimensionarSuperior = new JPanel();
        painelRedimensionarSuperior.setBackground(Color.green);
        painelRedimensionarSuperior.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelRedimensionarSuperior.setPreferredSize(new Dimension(15, 8));
        painelRedimensionarSuperior.setVisible(false);

        JPanel painelRedimensionarInferior = new JPanel();
        painelRedimensionarInferior.setBackground(Color.green);
        painelRedimensionarInferior.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        painelRedimensionarInferior.setPreferredSize(new Dimension(15, 8));
        painelRedimensionarInferior.setVisible(false);

        PacoteUML.super.getPainelComponente().add(painelRedimensionarEsquerdo, 0);
        PacoteUML.super.getPainelComponente().add(painelRedimensionarDireito, 0);
        PacoteUML.super.getPainelComponente().add(painelRedimensionarSuperior, 0);
        PacoteUML.super.getPainelComponente().add(painelRedimensionarInferior, 0);

        listaPontosDeRedimensionamento[0] = painelRedimensionarEsquerdo;
        listaPontosDeRedimensionamento[1] = painelRedimensionarDireito;
        listaPontosDeRedimensionamento[2] = painelRedimensionarSuperior;
        listaPontosDeRedimensionamento[3] = painelRedimensionarInferior;


        boolean[] movimentacaoAcontecendo = { false };

        super.getGlassPaneComponente().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!diagramaUML.getAreaDeDiagramas().isMovimentacaoPermitida() && !movimentacaoAcontecendo[0] &&
                        !diagramaUML.getAreaDeDiagramas().isSelecaoDeRelacionamentoAcontecendo()) {

                    atualizarPontosDeRedimensionamento();
                    mostrarPontosDeRedimensionamento(true);

                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!movimentacaoAcontecendo[0]) {
                    int posicaoXpainelComponente = PacoteUML.super.getPainelComponente().getLocationOnScreen().x;
                    int posicaoYpainelComponente = PacoteUML.super.getPainelComponente().getLocationOnScreen().y;

                    if (e.getXOnScreen() <= posicaoXpainelComponente ||
                            e.getXOnScreen() >= posicaoXpainelComponente +
                                    painelAreaPacote.getBounds().width ||
                            e.getYOnScreen() <= posicaoYpainelComponente ||
                            e.getYOnScreen() >= posicaoYpainelComponente + PacoteUML.super.getPainelComponente().getBounds().height) {


                        mostrarPontosDeRedimensionamento(false);
                    }
                }
            }
        });


        painelRedimensionarEsquerdo.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        painelRedimensionarDireito.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        painelRedimensionarSuperior.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        painelRedimensionarInferior.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (!movimentacaoAcontecendo[0]) {

                    try {
                        int LARGURA_PAINEL_OPCOES = 40;

                        PacoteUML.super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                        getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                                getPainelComponente().getHeight());

                    } catch (Exception exception) {}


                    int posicaoXpainelComponente = PacoteUML.super.getPainelComponente().getLocationOnScreen().x;
                    int posicaoYpainelComponente = PacoteUML.super.getPainelComponente().getLocationOnScreen().y;

                    if (e.getXOnScreen() <= posicaoXpainelComponente ||
                            e.getXOnScreen() >= posicaoXpainelComponente +
                                    painelAreaPacote.getBounds().width ||
                            e.getYOnScreen() <= posicaoYpainelComponente ||
                            e.getYOnScreen() >= posicaoYpainelComponente + PacoteUML.super.getPainelComponente().getBounds().height) {

                        mostrarPontosDeRedimensionamento(false);

                    }
                }
            }
        };

        painelRedimensionarEsquerdo.addMouseListener(mouseAdapter);
        painelRedimensionarDireito.addMouseListener(mouseAdapter);
        painelRedimensionarInferior.addMouseListener(mouseAdapter);

        // ==========================================================================================================

        int LARGURA_MINIMA_AREA_PACOTE = painelNomePacote.getPreferredSize().width + painelNomePacote.getPreferredSize().width/2;

        int ALTURA = painelNomePacote.getPreferredSize().height*4;

        int ALTURA_MINIMA_AREA_PACOTE = painelNomePacote.getPreferredSize().height*3;

        // ==========================================================================================================

        //int[] posicaoMouseX = { 0 };

        mouseAdapter = new MouseAdapter() {
            private int largura;
            boolean primeiraVez = true;
            private int posicaoMouseX;
            private int larguraPainelNomePacote;
            private HashMap<String, Object> informacoesAntesDaModificacao;

            @Override
            public void mouseReleased(MouseEvent e) {
                movimentacaoAcontecendo[0] = false;
                primeiraVez = true;

                PacoteUML.super.atualizarAreasDeConexao();

                PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                        informacoesAntesDaModificacao,
                        obterInformacoes(),
                        PacoteUML.this));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                movimentacaoAcontecendo[0] = true;

                if (primeiraVez) {
                    informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

                    primeiraVez = false;
                }

                posicaoMouseX = PacoteUML.super.getPainelComponente().getLocationOnScreen().x + PacoteUML.super.getLargura();

                largura = painelAreaPacote.getBounds().width;

                larguraPainelNomePacote = painelNomePacote.getBounds().width;

                try {
                    int LARGURA_PAINEL_OPCOES = 40;

                    PacoteUML.super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                    getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                            getPainelComponente().getHeight());
                } catch (Exception exception) {}


                // diferenca entre a posicao atual do mouse e a posicao antiga
                int diferencaMouseX = e.getXOnScreen() - posicaoMouseX;

                if (largura + diferencaMouseX >= Math.max(LARGURA_MINIMA_AREA_PACOTE, larguraPainelNomePacote)) {
                    painelAreaPacote.setSize(
                            largura + diferencaMouseX,
                            painelAreaPacote.getBounds().height);


                    PacoteUML.super.setLargura(Math.max(painelAreaPacote.getBounds().width, painelNomePacote.getPreferredSize().width));

                    PacoteUML.super.getPainelComponente().setSize(new Dimension(PacoteUML.super.getLargura(), PacoteUML.super.getAltura()));
                    PacoteUML.super.getGlassPaneComponente().setSize(PacoteUML.super.getLargura(), PacoteUML.super.getAltura());

                    atualizarPontosDeRedimensionamento();

                    PacoteUML.super.getPainelComponente().revalidate();
                    PacoteUML.super.getPainelComponente().repaint();

                }
            }
        };

        painelRedimensionarDireito.addMouseListener(mouseAdapter);
        painelRedimensionarDireito.addMouseMotionListener(mouseAdapter);

        // -------------------------------------------------------------

        mouseAdapter = new MouseAdapter() {
            private int largura;
            boolean primeiraVez = true;
            private int posicaoMouseX;
            private int larguraPainelNomePacote;
            private HashMap<String, Object> informacoesAntesDaModificacao;

            @Override
            public void mouseReleased(MouseEvent e) {
                movimentacaoAcontecendo[0] = false;
                primeiraVez = true;

                PacoteUML.super.atualizarAreasDeConexao();

                if (informacoesAntesDaModificacao != null) {
                    PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                            informacoesAntesDaModificacao,
                            obterInformacoes(),
                            PacoteUML.this));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                movimentacaoAcontecendo[0] = true;

                if (primeiraVez) {
                    informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

                    primeiraVez = false;
                }

                // ???????????????????????????????????????

                posicaoMouseX = PacoteUML.super.getPainelComponente().getLocationOnScreen().x;

                largura = painelAreaPacote.getBounds().width;

                larguraPainelNomePacote = painelNomePacote.getBounds().width;


                try {
                    int LARGURA_PAINEL_OPCOES = 40;

                    PacoteUML.super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                    getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                            getPainelComponente().getHeight());
                } catch (Exception exception) {}

                // =================================================================================================

                // diferenca entre a posicao atual do mouse e a posicao antiga
                int diferencaMouseX = e.getXOnScreen() - posicaoMouseX;

                if (largura - diferencaMouseX >= Math.max(LARGURA_MINIMA_AREA_PACOTE, larguraPainelNomePacote)) {
                    PacoteUML.super.setLargura(Math.max(largura - diferencaMouseX, painelNomePacote.getPreferredSize().width));

                    PacoteUML.super.getPainelComponente().setBounds(
                            PacoteUML.super.getPainelComponente().getBounds().x + diferencaMouseX,
                            PacoteUML.super.getPainelComponente().getBounds().y,
                            PacoteUML.super.getLargura(),
                            PacoteUML.super.getAltura());

                    painelAreaPacote.setSize(
                            largura - diferencaMouseX,
                            painelAreaPacote.getBounds().height);


                    PacoteUML.super.getGlassPaneComponente().setBounds(0, 0, PacoteUML.super.getLargura(), PacoteUML.super.getAltura());

                    atualizarPontosDeRedimensionamento();

                    PacoteUML.super.getPainelComponente().revalidate();
                    PacoteUML.super.getPainelComponente().repaint();

                }
            }
        };

        painelRedimensionarEsquerdo.addMouseListener(mouseAdapter);
        painelRedimensionarEsquerdo.addMouseMotionListener(mouseAdapter);

        // -------------------------------------------------------------

        mouseAdapter = new MouseAdapter() {
            private int altura;
            boolean primeiraVez = true;
            private int posicaoMouseY;
            private HashMap<String, Object> informacoesAntesDaModificacao;


            @Override
            public void mouseReleased(MouseEvent e) {
                movimentacaoAcontecendo[0] = false;
                primeiraVez = true;

                PacoteUML.super.atualizarAreasDeConexao();

                if (informacoesAntesDaModificacao != null) {
                    PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                            informacoesAntesDaModificacao,
                            obterInformacoes(),
                            PacoteUML.this));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                movimentacaoAcontecendo[0] = true;

                if (primeiraVez) {
                    informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

                    primeiraVez = false;

                }

                posicaoMouseY = PacoteUML.super.getPainelComponente().getLocationOnScreen().y + PacoteUML.super.getAltura();

                altura = painelAreaPacote.getBounds().height;


                try {
                    int LARGURA_PAINEL_OPCOES = 40;

                    PacoteUML.super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                    getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                            getPainelComponente().getHeight());
                } catch (Exception exception) {}

                // ===================================================================================================

                // diferenca entre a posicao atual do mouse e a posicao antiga
                int diferencaMouseY = e.getYOnScreen() - posicaoMouseY;

                if (altura + diferencaMouseY >= ALTURA_MINIMA_AREA_PACOTE) {
                    painelAreaPacote.setSize(
                            painelAreaPacote.getBounds().width,
                            altura + diferencaMouseY);


                    PacoteUML.super.setAltura(painelNomePacote.getBounds().height + painelAreaPacote.getBounds().height);

                    PacoteUML.super.getPainelComponente().setSize(new Dimension(PacoteUML.super.getLargura(), PacoteUML.super.getAltura()));
                    PacoteUML.super.getGlassPaneComponente().setSize(PacoteUML.super.getLargura(), PacoteUML.super.getAltura());

                    atualizarPontosDeRedimensionamento();

                    PacoteUML.super.getPainelComponente().revalidate();
                    PacoteUML.super.getPainelComponente().repaint();

                }
            }
        };

        painelRedimensionarInferior.addMouseListener(mouseAdapter);
        painelRedimensionarInferior.addMouseMotionListener(mouseAdapter);

        // -------------------------------------------------------------

        mouseAdapter = new MouseAdapter() {
            private int altura;
            boolean primeiraVez = true;
            private int posicaoMouseY;
            private HashMap<String, Object> informacoesAntesDaModificacao;

            @Override
            public void mouseReleased(MouseEvent e) {
                movimentacaoAcontecendo[0] = false;
                primeiraVez = true;

                PacoteUML.super.atualizarAreasDeConexao();

                if (informacoesAntesDaModificacao != null) {
                    PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                            informacoesAntesDaModificacao,
                            obterInformacoes(),
                            PacoteUML.this));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                movimentacaoAcontecendo[0] = true;

                if (primeiraVez) {
                    informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

                    primeiraVez = false;
                }

                posicaoMouseY = PacoteUML.super.getPainelComponente().getLocationOnScreen().y + painelNomePacote.getBounds().height;

                altura = painelAreaPacote.getBounds().height;


                try {
                    int LARGURA_PAINEL_OPCOES = 40;

                    PacoteUML.super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                    getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                            getPainelComponente().getHeight());
                } catch (Exception exception) {}

                // ====================================================================================================

                // diferenca entre a posicao atual do mouse e a posicao antiga
                int diferencaMouseY = e.getYOnScreen() - posicaoMouseY;

                if (altura - diferencaMouseY >= ALTURA_MINIMA_AREA_PACOTE) {
                    PacoteUML.super.setAltura(painelNomePacote.getBounds().height + altura - diferencaMouseY);

                    PacoteUML.super.getPainelComponente().setBounds(
                            PacoteUML.super.getPainelComponente().getBounds().x,
                            PacoteUML.super.getPainelComponente().getBounds().y + diferencaMouseY,
                            PacoteUML.super.getLargura(),
                            PacoteUML.super.getAltura());

                    painelAreaPacote.setSize(
                            painelAreaPacote.getBounds().width,
                            altura - diferencaMouseY);

                    PacoteUML.super.getGlassPaneComponente().setBounds(0, 0, PacoteUML.super.getLargura(), PacoteUML.super.getAltura());

                    atualizarPontosDeRedimensionamento();

                    PacoteUML.super.getPainelComponente().revalidate();
                    PacoteUML.super.getPainelComponente().repaint();

                }
            }
        };

        painelRedimensionarSuperior.addMouseListener(mouseAdapter);
        painelRedimensionarSuperior.addMouseMotionListener(mouseAdapter);

        // ==========================================================================================================


        super.setLargura(LARGURA_MINIMA_AREA_PACOTE);

        super.setAltura(ALTURA);

        super.getPainelComponente().add(super.getGlassPaneComponente());
        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());


        super.getPainelComponente().add(painelNomePacote);
        painelNomePacote.setBounds(0, 0, LARGURA_MINIMA_NOME_PACOTE, painelNomePacote.getPreferredSize().height);

        super.getPainelComponente().add(painelAreaPacote);
        painelAreaPacote.setBounds(0, painelNomePacote.getBounds().height, LARGURA_MINIMA_AREA_PACOTE,
                ALTURA - painelNomePacote.getBounds().height);

        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();

    }


    public void atualizarComponente(String novoNome) {
        try {
            int LARGURA_PAINEL_OPCOES = 40;

                super.getPainelComponente().remove(getPainelComponente().getComponent(7));
                getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                        getPainelComponente().getHeight());

        } catch (Exception e) {}

        System.out.println(painelNomePacote.getPreferredSize());

        this.nome = novoNome;
        labelNomePacote.setText(novoNome);
        textFieldNomePacote.setText(novoNome);

        painelNomePacote.remove(labelNomePacote);
        painelNomePacote.add(labelNomePacote, "north");

        // ============================================================================================================
        System.out.println(painelNomePacote.getPreferredSize());


        painelNomePacote.setBounds(0, 0,
                Math.max(painelNomePacote.getPreferredSize().width, LARGURA_MINIMA_NOME_PACOTE),
                Math.max(painelNomePacote.getPreferredSize().height, ALTURA_MINIMA_NOME_PACOTE));

        super.setLargura(Math.max(painelAreaPacote.getWidth(), Math.max(super.getLargura(), painelNomePacote.getBounds().width)));
        super.setAltura(painelNomePacote.getBounds().height + painelAreaPacote.getBounds().height);


        painelAreaPacote.setBounds(0, painelNomePacote.getBounds().height, super.getLargura(),
                super.getAltura() - painelNomePacote.getBounds().height);

        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());

        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();

        mostrarPontosDeRedimensionamento(false);
        atualizarPontosDeRedimensionamento();
    }


    @Override
    void initFrameGerenciarComponente() {
        RobotoFont robotoFont = new RobotoFont();

        JPanel painelGerenciarComponente = new JPanel(new MigLayout("insets 0 0 20 0", "","[grow, fill][grow, fill][]"));
        painelGerenciarComponente.setBackground(Color.white);

        // ==========================================================================================================

        JLabel labelPacote = new JLabel("PACOTE", JLabel.CENTER);
        labelPacote.setFont(robotoFont.getRobotoBlack(14f));
        labelPacote.setPreferredSize(new Dimension(labelPacote.getPreferredSize().width*4, labelPacote.getPreferredSize().height));

        JPanel painelLabelPacote = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelPacote.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelLabelPacote.add(labelPacote, "align center");
        painelLabelPacote.setOpaque(false);



        JPanel painelGerenciarPacote = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelGerenciarPacote.setBackground(Color.white);

        JLabel labelNomePacote = new JLabel("Nome");
        labelNomePacote.setFont(robotoFont.getRobotoMedium(15f));

        textFieldNomePacote = new JTextField("Novo Pacote");
        textFieldNomePacote.setBorder(BorderFactory.createCompoundBorder(
                textFieldNomePacote.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textFieldNomePacote.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomePacote.setForeground(new Color(0x484848));


        painelGerenciarPacote.add(labelNomePacote, "wrap, gapbottom 8");
        painelGerenciarPacote.add(textFieldNomePacote, "wrap, grow");



        JButton botaoAplicar = new JButton("APLICAR");
        botaoAplicar.setFont(robotoFont.getRobotoBlack(13f));
        botaoAplicar.setForeground(Color.white);
        botaoAplicar.setBackground(Color.black);
        botaoAplicar.setBorder(new EmptyBorder(12, 20, 12, 20));
        botaoAplicar.setOpaque(false);
        botaoAplicar.setFocusable(false);
        botaoAplicar.setHorizontalTextPosition(JButton.LEFT);
        botaoAplicar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botaoAplicar.setBackground(new Color(0x262626));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botaoAplicar.setBackground(Color.black);
            }
        });
        botaoAplicar.addActionListener(e -> {
            HashMap<String, Object> informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

            atualizarComponente(textFieldNomePacote.getText());

            HashMap<String, Object> informacoesDepoisDaModificacao = PacoteUML.this.obterInformacoes();


            if (!informacoesAntesDaModificacao.get("NOME").equals(informacoesDepoisDaModificacao.get("NOME"))) {
                PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                        informacoesAntesDaModificacao,
                        informacoesDepoisDaModificacao,
                        PacoteUML.this));
            }
        });

        super.getFrameGerenciarComponente().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HashMap<String, Object> informacoesAntesDaModificacao = PacoteUML.this.obterInformacoes();

                atualizarComponente(textFieldNomePacote.getText());

                HashMap<String, Object> informacoesDepoisDaModificacao = PacoteUML.this.obterInformacoes();


                if (!informacoesAntesDaModificacao.get("NOME").equals(informacoesDepoisDaModificacao.get("NOME"))) {
                    PacoteUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                            informacoesAntesDaModificacao,
                            informacoesDepoisDaModificacao,
                            PacoteUML.this));
                }

            }
        });


        painelGerenciarComponente.add(painelLabelPacote, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(painelGerenciarPacote, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(botaoAplicar, "align right, gaptop 0, gapbottom 0");


        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();




    }


    public JPanel getPainelAreaPacote() {
        return painelAreaPacote;
    }

    private void atualizarPontosDeRedimensionamento() {

        JPanel painelRedimensionarEsquerdo = listaPontosDeRedimensionamento[0];
        JPanel painelRedimensionarDireito = listaPontosDeRedimensionamento[1];
        JPanel painelRedimensionarSuperior = listaPontosDeRedimensionamento[2];
        JPanel painelRedimensionarInferior = listaPontosDeRedimensionamento[3];

        painelRedimensionarEsquerdo.setBounds(
                0,
                (PacoteUML.super.getAltura() + painelNomePacote.getBounds().height)/2 -
                        painelRedimensionarEsquerdo.getPreferredSize().height/2,
                painelRedimensionarEsquerdo.getPreferredSize().width,
                painelRedimensionarEsquerdo.getPreferredSize().height);


        painelRedimensionarDireito.setBounds(
                PacoteUML.super.getLargura() - painelRedimensionarDireito.getPreferredSize().width,
                (PacoteUML.super.getAltura() + painelNomePacote.getBounds().height)/2 -
                        painelRedimensionarDireito.getPreferredSize().height/2,
                painelRedimensionarDireito.getPreferredSize().width,
                painelRedimensionarDireito.getPreferredSize().height);


        painelRedimensionarSuperior.setBounds(
                PacoteUML.super.getLargura()/2 - painelRedimensionarSuperior.getPreferredSize().width/2,
                painelNomePacote.getBounds().height,
                painelRedimensionarSuperior.getPreferredSize().width,
                painelRedimensionarSuperior.getPreferredSize().height);


        painelRedimensionarInferior.setBounds(
                PacoteUML.super.getLargura()/2 - painelRedimensionarInferior.getPreferredSize().width/2,
                PacoteUML.super.getAltura() - painelRedimensionarInferior.getPreferredSize().height,
                painelRedimensionarInferior.getPreferredSize().width,
                painelRedimensionarInferior.getPreferredSize().height);

        PacoteUML.super.getPainelComponente().revalidate();
        PacoteUML.super.getPainelComponente().repaint();
    }

    private void mostrarPontosDeRedimensionamento(boolean mostrar) {
        for (JPanel painelPontoDeRedimensionamento : listaPontosDeRedimensionamento) {
            painelPontoDeRedimensionamento.setVisible(mostrar);
        }

        PacoteUML.super.getPainelComponente().revalidate();
        PacoteUML.super.getPainelComponente().repaint();
    }

    @Override
    HashMap<String, Object> obterInformacoes() {

        HashMap<String, Object> mapaInformacoesComponente = new HashMap<>();

        mapaInformacoesComponente.put("POSICAO_X", super.getPainelComponente().getX());
        mapaInformacoesComponente.put("POSICAO_Y", super.getPainelComponente().getY());
        mapaInformacoesComponente.put("LARGURA_AREA_PACOTE", painelAreaPacote.getWidth());
        mapaInformacoesComponente.put("ALTURA_AREA_PACOTE", painelAreaPacote.getHeight());
        mapaInformacoesComponente.put("NOME", nome);

        return  mapaInformacoesComponente;
    }

    public JPanel getPainelNomePacote() {
        return painelNomePacote;
    }


    @Override
    public String toString() {

        return super.getPainelComponente().getX() +
                "\n" +
                super.getPainelComponente().getY() +
                "\n" +
                nome +
                "\n" +
                painelAreaPacote.getWidth() +
                "\n" +
                painelAreaPacote.getHeight() +
                "\n";
    }
}
