package componentes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.ComponenteMovido;
import componentes.alteracoes.ComponenteRemovido;
import interfacegrafica.AreaDeDiagramas;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO
 */
public abstract class ComponenteUML {
    private final JPanel painelComponente = new JPanel(null);
    /**
     * Um painel transparente que intercepta os cliques no painelComponente e garante que o componente consiga ser
     * arrastado pelo mouse do usuário.
     */
    private final JPanel glassPane = new JPanel();
    private final JFrame frameGerenciarComponente = new JFrame();
    private final JPanel painelOpcoesComponente = new JPanel();
    private int largura;
    private int altura;

    public ComponenteUML(AreaDeDiagramas areaDeDiagramas) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        painelComponente.setOpaque(false);
        painelComponente.add(glassPane);
        glassPane.setOpaque(false);
        frameGerenciarComponente.setResizable(false);
        frameGerenciarComponente.setTitle(gerenciadorDeRecursos.getString("componente_configurar"));
        frameGerenciarComponente.setIconImage(gerenciadorDeRecursos.getImagem("icone_configurar_48").getImage());
        frameGerenciarComponente.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // ----------------------------------------------------------------------------

        painelOpcoesComponente.setLayout(new MigLayout("insets 0 n n n n"));
        painelOpcoesComponente.setOpaque(false);

        JButton botaoGerenciar = new JButton(gerenciadorDeRecursos.getImagem("icone_configurar_20"));
        botaoGerenciar.setFocusable(false);
        botaoGerenciar.addActionListener(e -> {
            frameGerenciarComponente.setVisible(true);
            frameGerenciarComponente.setLocationRelativeTo(null);
            frameGerenciarComponente.requestFocusInWindow();

            // Removendo painel opcoes
            if (painelOpcoesComponente.getParent() != null) {
                int LARGURA_PAINEL_OPCOES = 40;

                painelComponente.remove(painelOpcoesComponente);
                painelComponente.setSize(
                    painelComponente.getWidth() - LARGURA_PAINEL_OPCOES,
                    painelComponente.getHeight()
                );
                painelComponente.revalidate();
                painelComponente.repaint();
            }
        });

        JDialog dialogExcluirComponente = getDialogExcluirComponente();

        JButton botaoDeletar = new JButton(gerenciadorDeRecursos.getImagem("icone_excluir"));
        botaoDeletar.setFocusable(false);
        botaoDeletar.addActionListener(e -> {
            dialogExcluirComponente.setLocationRelativeTo(null);
            dialogExcluirComponente.setVisible(true);

            // Removendo painel opcoes
            if (painelOpcoesComponente.getParent() != null) {
                int LARGURA_PAINEL_OPCOES = 40;

                painelComponente.remove(painelOpcoesComponente);
                painelComponente.setSize(
                        painelComponente.getWidth() - LARGURA_PAINEL_OPCOES,
                        painelComponente.getHeight()
                );
                painelComponente.revalidate();
                painelComponente.repaint();
            }
        });

        painelOpcoesComponente.add(botaoGerenciar, "wrap");
        painelOpcoesComponente.add(botaoDeletar);

        // ----------------------------------------------------------------------------

        MouseAdapter esconderPainelOpcoes = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                int xPainelComponente = painelComponente.getLocationOnScreen().x;
                int yPainelComponente = painelComponente.getLocationOnScreen().y;

                // Verificando se o evento ocorreu dentro dos limites do painelComponente
                if ((e.getXOnScreen() <= xPainelComponente ||
                        e.getXOnScreen() >= xPainelComponente + painelComponente.getBounds().width ||
                        e.getYOnScreen() <= yPainelComponente ||
                        e.getYOnScreen() >= yPainelComponente + painelComponente.getBounds().height) &&
                        painelOpcoesComponente.getParent() != null
                ) {
                    int LARGURA_PAINEL_OPCOES = 40;

                    painelComponente.remove(painelOpcoesComponente);
                    painelComponente.setSize(
                            painelComponente.getWidth() - LARGURA_PAINEL_OPCOES,
                            painelComponente.getHeight()
                    );
                    painelComponente.revalidate();
                    painelComponente.repaint();
                }
            }
        };

        painelOpcoesComponente.addMouseListener(esconderPainelOpcoes);
        botaoDeletar.addMouseListener(esconderPainelOpcoes);
        botaoGerenciar.addMouseListener(esconderPainelOpcoes);

        // ----------------------------------------------------------------------------

        glassPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados() && painelOpcoesComponente.getParent() == null) {

                    int LARGURA_PAINEL_OPCOES = 40;

                    painelOpcoesComponente.setBounds(
                        painelComponente.getWidth(), 0,
                        LARGURA_PAINEL_OPCOES, painelComponente.getHeight()
                    );

                    painelComponente.setSize(
                        painelComponente.getWidth() + LARGURA_PAINEL_OPCOES,
                        painelComponente.getHeight()
                    );

                    painelComponente.add(painelOpcoesComponente);
                    painelComponente.revalidate();
                    painelComponente.repaint();
                }
            }
        });

        glassPane.addMouseListener(esconderPainelOpcoes);

        MouseAdapter adapterMoverComponente = (new MouseAdapter() {
            private int posicaoMouseX, posicaoMouseY, posicaoPainelX, posicaoPainelY;

            @Override
            public void mousePressed(MouseEvent e) {
                    posicaoPainelX = painelComponente.getX();
                    posicaoPainelY = painelComponente.getY();

                    posicaoMouseX = e.getXOnScreen();
                    posicaoMouseY = e.getYOnScreen();

                    painelComponente.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    int componentePosicaoFinalX = posicaoPainelX + (e.getXOnScreen() - posicaoMouseX);
                    int componentePosicaoFinalY = posicaoPainelY + (e.getYOnScreen() - posicaoMouseY);

                    int tamanhoQuadroBranco = areaDeDiagramas.getTamanhoQuadroBranco();

                    // Calcula um limite para onde o componente pode ser movido
                    int limiteDireitoX = tamanhoQuadroBranco - painelComponente.getWidth();
                    int limiteInferiorY = tamanhoQuadroBranco - painelComponente.getHeight();

                    if (componentePosicaoFinalX > 0 && componentePosicaoFinalX < limiteDireitoX &&
                        componentePosicaoFinalY > 0 && componentePosicaoFinalY < limiteInferiorY) {

                        painelComponente.setLocation(componentePosicaoFinalX, componentePosicaoFinalY);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    painelComponente.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    // TODO
                    areaDeDiagramas.addAlteracao(new ComponenteMovido(
                        posicaoPainelX, posicaoPainelY,
                        painelComponente.getX(), painelComponente.getY(),
                        ComponenteUML.this
                    ));
                }
            }
        });

        glassPane.addMouseListener(adapterMoverComponente);
        glassPane.addMouseMotionListener(adapterMoverComponente);

        // ----------------------------------------------------------------------------

        initFrameGerenciarComponente();
    }

    public void adicionarComponenteAoPainel(JComponent componente) {
        painelComponente.add(componente);
    }

    // TODO: tirar isso
    public JPanel getPainelComponente() {
        return painelComponente;
    }

    public JFrame getFrameGerenciarComponente() {
        return frameGerenciarComponente;
    }

    public void setBounds(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;

        glassPane.setBounds(0, 0, largura, altura);

        painelComponente.setSize(new Dimension(largura, altura));

        painelComponente.revalidate();
        painelComponente.repaint();
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    // TODO: tirar isso
    public JPanel getPainelOpcoesComponenteUML() {
        return painelOpcoesComponente;
    }

    // TODO: ver esse metodo
    public ArrayList<JPanel> getListaAreasDeConexao() {
        return null;
    }

    private JDialog getDialogExcluirComponente() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JDialog dialogExcluirComponente = new JDialog();

        // ----------------------------------------------------------------------------

        JLabel labelImgInterrogacao = new JLabel(gerenciadorDeRecursos.getImagem("icone_interrogacao_pequena"));

        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgInterrogacao.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));
        painelImgInterrogacao.add(labelImgInterrogacao, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelMensagem = new JLabel(gerenciadorDeRecursos.getString("excluir_componente_pergunta"));
        labelMensagem.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        painelMensagem.add(labelMensagem, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelRespostaSim = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaSim.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaSim.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black"))
        );

        JLabel labelRespostaSim = new JLabel(gerenciadorDeRecursos.getString("sim"));
        labelRespostaSim.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelRespostaSim.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaSim.add(labelRespostaSim, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelRespostaNao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaNao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaNao.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black"))
        );

        JLabel labelRespostaNao = new JLabel(gerenciadorDeRecursos.getString("nao"));
        labelRespostaNao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelRespostaNao.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaNao.add(labelRespostaNao, "align center");

        // ----------------------------------------------------------------------------

        MouseAdapter adapterPainelResposta = new MouseAdapter() {
            // O componente de index 0 são os labels resposta Sim e Não

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

            @Override
            public void mouseClicked(MouseEvent e) {
                dialogExcluirComponente.setVisible(false);
            }

        };

        painelRespostaSim.addMouseListener(adapterPainelResposta);
        painelRespostaNao.addMouseListener(adapterPainelResposta);

        // ----------------------------------------------------------------------------

        // TODO
       /* mouseAdapter = new MouseAdapter() {
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

        painelRespostaSim.addMouseListener(mouseAdapter);*/

        // ----------------------------------------------------------------------------

        JPanel painelExcluirComponente = new JPanel(new MigLayout("debug, insets 5 0 10 0"));
        painelExcluirComponente.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelExcluirComponente.add(painelImgInterrogacao, "west");
        painelExcluirComponente.add(painelMensagem, "wrap");
        painelExcluirComponente.add(painelRespostaSim, "gaptop 15, split 2, gapleft: push, gapright: push");
        painelExcluirComponente.add(painelRespostaNao, "gaptop 15, gapleft: push, gapright: push");

        // ----------------------------------------------------------------------------

        dialogExcluirComponente.setTitle(gerenciadorDeRecursos.getString("excluir_componente"));
        dialogExcluirComponente.setContentPane(painelExcluirComponente);
        dialogExcluirComponente.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogExcluirComponente.setResizable(false);
        dialogExcluirComponente.pack();

        return dialogExcluirComponente;
    }

    /**
     * Representação do componente em formato de String. Cada informação deve estar em uma linha diferente.
     */
    public abstract String toString();

    protected abstract void initFrameGerenciarComponente();
}


