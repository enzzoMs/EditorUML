package componentes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.ComponenteModificado;
import interfacegrafica.AreaDeDiagramas;
import modelos.ModeloDeComponenteUML;
import modelos.Pacote;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe que representa a parte gráfica de um componente UML do tipo "Pacote". Possui um nome e pode
 * ser redimensionado para qualquer tamanho.
 * */

public class PacoteUML extends ComponenteUML<Pacote> {
    private Pacote modeloAtual = new Pacote();
    private Pacote modeloAntesDeAlteracoes;
    private final JPanel painelNomePacote;
    private final JPanel painelAreaPacote;
    private final int larguraMinimaNomePacote;
    private final int alturaMinimaNomePacote;
    private final JPanel[] pontosDeRedimensionamento = new JPanel[4];

    public PacoteUML(AreaDeDiagramas areaDeDiagramas) {
        super(areaDeDiagramas);

        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        painelNomePacote = new JPanel(new MigLayout("fill"));
        painelNomePacote.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelNomePacote.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 2, 0, 2, gerenciadorDeRecursos.getColor("dark_gray")),
            BorderFactory.createEmptyBorder(5, 13, 5, 13)
        ));

        JLabel labelNomePacote = new JLabel(modeloAtual.getNome(), JLabel.CENTER);
        labelNomePacote.setFont(gerenciadorDeRecursos.getRobotoBlack(14));
        labelNomePacote.setOpaque(false);

        painelNomePacote.add(labelNomePacote, "north");

        larguraMinimaNomePacote = painelNomePacote.getPreferredSize().width;
        alturaMinimaNomePacote = painelNomePacote.getPreferredSize().height;

        painelAreaPacote = new JPanel();
        painelAreaPacote.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelAreaPacote.setBorder(
            BorderFactory.createMatteBorder(2, 2, 2, 2, gerenciadorDeRecursos.getColor("dark_gray"))
        );

        // ----------------------------------------------------------------------------

        for (int i = 0; i < pontosDeRedimensionamento.length; i++) {
            JPanel painelRedimensionar = new JPanel();
            painelRedimensionar.setBackground(gerenciadorDeRecursos.getColor("green"));
            painelRedimensionar.setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("black"))
            );
            painelRedimensionar.setVisible(false);

            pontosDeRedimensionamento[i] = painelRedimensionar;

            // Index 0 para que o painel fique por cima dos outros
            adicionarComponenteAoPainel(painelRedimensionar, 0);
        }

        pontosDeRedimensionamento[0].setPreferredSize(new Dimension(8, 15));
        pontosDeRedimensionamento[1].setPreferredSize(new Dimension(8, 15));
        pontosDeRedimensionamento[2].setPreferredSize(new Dimension(15, 8));
        pontosDeRedimensionamento[3].setPreferredSize(new Dimension(15, 8));

        pontosDeRedimensionamento[0].setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        pontosDeRedimensionamento[1].setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        pontosDeRedimensionamento[2].setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        pontosDeRedimensionamento[3].setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));

        // ----------------------------------------------------------------------------

        adicionarEventListenerGlassPane(this::mostrarPontosDeRedimensionamento);

        // ----------------------------------------------------------------------------

        int larguraMinimaAreaPacote = painelNomePacote.getPreferredSize().width + painelNomePacote.getPreferredSize().width/2;
        int alturaMinimaAreaPacote = painelNomePacote.getPreferredSize().height * 3;

        // ==========================================================================================================

        // Adicionando o comportamento de "redimensionar" nos paineis

        // Painel direito
        pontosDeRedimensionamento[0].addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                removerPainelDeOpcoes();

                int diferencaX = e.getXOnScreen() - getPainelComponente().getLocationOnScreen().x;
                int larguraPainelNomePacote = painelNomePacote.getBounds().width;
                int larguraAreaPacote = painelAreaPacote.getBounds().width;

                if (larguraAreaPacote - diferencaX >= Math.max(larguraMinimaAreaPacote, larguraPainelNomePacote)) {
                    setBounds(
                        larguraAreaPacote - diferencaX,
                        getAltura()
                    );

                    getPainelComponente().setBounds(
                        getPainelComponente().getBounds().x + diferencaX,
                        getPainelComponente().getBounds().y,
                        getLargura(),
                        getAltura()
                    );

                    painelAreaPacote.setSize(
                        larguraAreaPacote - diferencaX,
                        painelAreaPacote.getBounds().height
                    );

                    atualizarPontosDeRedimensionamento();
                }
            }
        });

        // ----------------------------------------------------------------------------

        // Painel esquerdo
        pontosDeRedimensionamento[1].addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int diferencaX = e.getXOnScreen() - (getPainelComponente().getLocationOnScreen().x + getLargura());
                int larguraPainelNomePacote = painelNomePacote.getBounds().width;
                int larguraAreaPacote = painelAreaPacote.getBounds().width;

                removerPainelDeOpcoes();

                if (larguraAreaPacote + diferencaX >= Math.max(larguraMinimaAreaPacote, larguraPainelNomePacote)) {
                    painelAreaPacote.setSize(
                        larguraAreaPacote + diferencaX,
                        painelAreaPacote.getBounds().height
                    );

                    setBounds(
                        Math.max(painelAreaPacote.getBounds().width, painelNomePacote.getPreferredSize().width),
                        getAltura()
                    );

                    atualizarPontosDeRedimensionamento();
                }
            }
        });

        // -------------------------------------------------------------

        // Painel superior

        pontosDeRedimensionamento[2].addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int posicaoYAreaPacote = getPainelComponente().getLocationOnScreen().y + painelNomePacote.getBounds().height;
                int diferencaY = e.getYOnScreen() - posicaoYAreaPacote;
                int alturaAreaPacote = painelAreaPacote.getBounds().height;

                removerPainelDeOpcoes();

                if (alturaAreaPacote - diferencaY >= alturaMinimaAreaPacote) {
                    setBounds(
                        getLargura(),
                        getAltura() - diferencaY
                    );

                    getPainelComponente().setBounds(
                        getPainelComponente().getBounds().x,
                        getPainelComponente().getBounds().y + diferencaY,
                        getLargura(),
                        getAltura()
                    );

                    painelAreaPacote.setSize(
                        painelAreaPacote.getBounds().width,
                        alturaAreaPacote - diferencaY
                    );

                    atualizarPontosDeRedimensionamento();
                }
            }
        });

        // ----------------------------------------------------------------------------

        // Painel inferior
        pontosDeRedimensionamento[3].addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int posicaoYAreaPacote = getPainelComponente().getLocationOnScreen().y + getAltura();
                int diferencaY = e.getYOnScreen() - posicaoYAreaPacote;
                int alturaAreaPacote = painelAreaPacote.getBounds().height;

                removerPainelDeOpcoes();

                if (alturaAreaPacote + diferencaY >= alturaMinimaAreaPacote) {
                    painelAreaPacote.setSize(
                        painelAreaPacote.getBounds().width,
                        alturaAreaPacote + diferencaY
                    );

                    setBounds(
                        getLargura(),
                        painelNomePacote.getBounds().height + (alturaAreaPacote + diferencaY)
                    );

                    atualizarPontosDeRedimensionamento();
                }
            }
        });

        // ----------------------------------------------------------------------------

        MouseAdapter registrarAlteracaoTamanhoDePacote = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removerPainelDeOpcoes();

                modeloAntesDeAlteracoes = modeloAtual.copiar();

                Rectangle boundsPainelComponente = getPainelComponente().getBounds();
                modeloAtual.setBounds(new Rectangle(
                    boundsPainelComponente.x, boundsPainelComponente.y,
                    boundsPainelComponente.width, boundsPainelComponente.height
                ));

                if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                    adicionarAlteracaoDeComponente(new ComponenteModificado<>(
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar(),
                        PacoteUML.this
                    ));

                    modeloAntesDeAlteracoes = modeloAtual.copiar();
                }
            }
        };

        for (JPanel ponto : pontosDeRedimensionamento) {
            ponto.addMouseListener(registrarAlteracaoTamanhoDePacote);
        }

        // ----------------------------------------------------------------------------

        int altura = painelNomePacote.getPreferredSize().height * 4;
        int largura = painelNomePacote.getPreferredSize().width + painelNomePacote.getPreferredSize().width/2;

        adicionarComponenteAoPainel(painelNomePacote);
        painelNomePacote.setBounds(
            0, 0,
            larguraMinimaNomePacote, painelNomePacote.getPreferredSize().height
        );

        adicionarComponenteAoPainel(painelAreaPacote);
        painelAreaPacote.setBounds(
            0, painelNomePacote.getBounds().height,
            largura, altura - painelNomePacote.getBounds().height
        );

        setBounds(largura, altura);

        Rectangle boundsPainelComponente = getPainelComponente().getBounds();
        modeloAtual.setBounds(new Rectangle(
                boundsPainelComponente.x, boundsPainelComponente.y,
                boundsPainelComponente.width, boundsPainelComponente.height
        ));

        modeloAntesDeAlteracoes = modeloAtual.copiar();
    }

    public void atualizarComponentesGraficos() {
        JLabel labelNomePacote = (JLabel) painelNomePacote.getComponent(0);
        painelNomePacote.removeAll();

        labelNomePacote.setText(modeloAtual.getNome());

        // Removendo e adicionando de novo para que o PreferredSize seja atualizado
        painelNomePacote.add(labelNomePacote, "north");

        painelNomePacote.setBounds(
            0, 0,
            Math.max(painelNomePacote.getPreferredSize().width, larguraMinimaNomePacote),
            Math.max(painelNomePacote.getPreferredSize().height, alturaMinimaNomePacote)
        );

        int novaLargura = Math.max(painelAreaPacote.getWidth(), painelNomePacote.getBounds().width);
        int novaAltura = painelNomePacote.getBounds().height + painelAreaPacote.getBounds().height;

        painelAreaPacote.setBounds(
            0, painelNomePacote.getBounds().height,
            novaLargura,
            novaAltura - painelNomePacote.getBounds().height
        );

        super.setBounds(novaLargura, novaAltura);

        Rectangle boundsPainelComponente = getPainelComponente().getBounds();
        modeloAtual.setBounds(new Rectangle(
            boundsPainelComponente.x, boundsPainelComponente.y,
            boundsPainelComponente.width, boundsPainelComponente.height
        ));

        mostrarPontosDeRedimensionamento(false);
        atualizarPontosDeRedimensionamento();
    }

    @Override
    public void setModelo(ModeloDeComponenteUML<Pacote> novoModelo) {
        modeloAtual = novoModelo.copiar();

        Rectangle boundsAtual = modeloAtual.getBounds();

        getPainelComponente().setLocation(boundsAtual.x, boundsAtual.y);

        setBounds(boundsAtual.width, boundsAtual.height);
        
        painelAreaPacote.setBounds(
            0, painelNomePacote.getHeight(),
            boundsAtual.width, boundsAtual.height - painelNomePacote.getHeight()
        );

        atualizarComponentesGraficos();
    }

    public void setModeloAntesDeAlteracoes(Pacote modeloAntesDeAlteracoes) {
        this.modeloAntesDeAlteracoes = modeloAntesDeAlteracoes;
    }

    @Override
    protected void initFrameGerenciarComponente() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelPacote = new JLabel(gerenciadorDeRecursos.getString("pacote_maiuscula"), JLabel.CENTER);
        labelPacote.setFont(gerenciadorDeRecursos.getRobotoBlack(14));
        // Aumentando o label para que o frame não fique pequeno demais
        labelPacote.setPreferredSize(new Dimension(labelPacote.getPreferredSize().width*4, labelPacote.getPreferredSize().height));

        JPanel painelLabelPacote = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelPacote.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")));
        painelLabelPacote.add(labelPacote, "align center");
        painelLabelPacote.setOpaque(false);

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarPacote = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelGerenciarPacote.setBackground(gerenciadorDeRecursos.getColor("white"));

        JLabel labelNomePacote = new JLabel(gerenciadorDeRecursos.getString("nome"));
        labelNomePacote.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        JTextField textFieldNomePacote = new JTextField(gerenciadorDeRecursos.getString("pacote_nome_default"));
        textFieldNomePacote.setBorder(BorderFactory.createCompoundBorder(
            textFieldNomePacote.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
        textFieldNomePacote.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomePacote.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomePacote.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomePacote.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomePacote.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomePacote.getText());
            }
        });

        painelGerenciarPacote.add(labelNomePacote, "wrap, gapbottom 8");
        painelGerenciarPacote.add(textFieldNomePacote, "wrap, grow");

        // ----------------------------------------------------------------------------

        JButton botaoAplicar = new JButton(gerenciadorDeRecursos.getString("aplicar_maiuscula"));
        botaoAplicar.setFont(gerenciadorDeRecursos.getRobotoBlack(13));
        botaoAplicar.setForeground(gerenciadorDeRecursos.getColor("white"));
        botaoAplicar.setBackground(gerenciadorDeRecursos.getColor("black"));
        botaoAplicar.setBorder(new EmptyBorder(12, 20, 12, 20));
        botaoAplicar.setOpaque(false);
        botaoAplicar.setFocusable(false);
        botaoAplicar.setHorizontalTextPosition(JButton.LEFT);
        botaoAplicar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botaoAplicar.setBackground(gerenciadorDeRecursos.getColor("raisin_black"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botaoAplicar.setBackground(gerenciadorDeRecursos.getColor("black"));
            }
        });
        botaoAplicar.addActionListener(e -> {
            if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                adicionarAlteracaoDeComponente(new ComponenteModificado<>(
                    modeloAntesDeAlteracoes.copiar(),
                    modeloAtual.copiar(),
                    PacoteUML.this
                ));

                atualizarComponentesGraficos();
                modeloAntesDeAlteracoes = modeloAtual.copiar();
            }
        });

        // ----------------------------------------------------------------------------

        getFrameGerenciarComponente().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                modeloAntesDeAlteracoes = modeloAtual.copiar();

                textFieldNomePacote.setText(modeloAtual.getNome());
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                    adicionarAlteracaoDeComponente(new ComponenteModificado<>(
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar(),
                        PacoteUML.this
                    ));

                    atualizarComponentesGraficos();
                    modeloAntesDeAlteracoes = modeloAtual.copiar();
                }
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarComponente = new JPanel(new MigLayout("insets 0 0 20 0", "","[grow, fill][grow, fill][]"));
        painelGerenciarComponente.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelGerenciarComponente.add(painelLabelPacote, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(painelGerenciarPacote, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(botaoAplicar, "align right, gaptop 0, gapbottom 0");

        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();
    }

    private void atualizarPontosDeRedimensionamento() {
        JPanel painelRedimensionarEsquerdo = pontosDeRedimensionamento[0];
        JPanel painelRedimensionarDireito = pontosDeRedimensionamento[1];
        JPanel painelRedimensionarSuperior = pontosDeRedimensionamento[2];
        JPanel painelRedimensionarInferior = pontosDeRedimensionamento[3];

        painelRedimensionarEsquerdo.setBounds(
            0,
            (getAltura() + painelNomePacote.getBounds().height)/2 - painelRedimensionarEsquerdo.getPreferredSize().height/2,
            painelRedimensionarEsquerdo.getPreferredSize().width,
            painelRedimensionarEsquerdo.getPreferredSize().height
        );


        painelRedimensionarDireito.setBounds(
            getLargura() - painelRedimensionarDireito.getPreferredSize().width,
            (getAltura() + painelNomePacote.getBounds().height)/2 - painelRedimensionarDireito.getPreferredSize().height/2,
            painelRedimensionarDireito.getPreferredSize().width,
           painelRedimensionarDireito.getPreferredSize().height
       );

       painelRedimensionarSuperior.setBounds(
           getLargura()/2 - painelRedimensionarSuperior.getPreferredSize().width/2,
           painelNomePacote.getBounds().height,
           painelRedimensionarSuperior.getPreferredSize().width,
           painelRedimensionarSuperior.getPreferredSize().height
       );


       painelRedimensionarInferior.setBounds(
           getLargura()/2 - painelRedimensionarInferior.getPreferredSize().width/2,
           getAltura() - painelRedimensionarInferior.getPreferredSize().height,
           painelRedimensionarInferior.getPreferredSize().width,
           painelRedimensionarInferior.getPreferredSize().height
       );

       getPainelComponente().revalidate();
       getPainelComponente().repaint();
   }

    private void mostrarPontosDeRedimensionamento(boolean mostrar) {
        for (JPanel painelPontoDeRedimensionamento : pontosDeRedimensionamento) {
            painelPontoDeRedimensionamento.setVisible(mostrar);
        }

        getPainelComponente().revalidate();
        getPainelComponente().repaint();
    }

    @Override
    public String toString() {
        return "PACOTE_UML\n// Posicao X e Y\n" +
                super.getPainelComponente().getX() + "\n" +
                super.getPainelComponente().getY() +
                "\n// Nome\n" +
                modeloAtual.getNome() +
                "\n//Largura\n" +
                getLargura() +
                "\n/Altura\n" +
                getAltura() + "\n";
    }
}
