package componentes.estruturas;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.estruturas.EstruturaModificada;
import interfacegrafica.AreaDeDiagramas;
import componentes.modelos.estruturas.Anotacao;
import componentes.modelos.ModeloDeComponenteUML;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Classe que representa a parte gráfica de uma estrutura UML do tipo "Anotação". Possui
 * capacidade para representar um texto qualquer.
 * */

public class AnotacaoUML extends EstruturaUML<Anotacao> {
    private Anotacao modeloAtual = new Anotacao();
    private Anotacao modeloAntesDeAlteracoes;
    private final JPanel painelTextoAnotacao;
    /**
     * Um label com uma imagem de um triângulo. É usado de forma decorativa para dar a impressão
     * de um papel com a ponta dobrada no painel da Anotação.
     */
    private final JLabel labelTriangulo;
    /**
     * Um painel usado em conjunto com o "labelTriangulo" para dar espaçamento entre o triangulo decorativo
     * e o texto da anotação.
     */
    private final JPanel painelSuperiorVazio;
    private static final int LARGURA_MINIMA = 70;
    private static final int ALTURA_MINIMA = 60;

    public AnotacaoUML(AreaDeDiagramas areaDeDiagramas) {
        super(areaDeDiagramas);

        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        painelTextoAnotacao = new JPanel(new MigLayout("insets 10 10 5 10"));
        painelTextoAnotacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelTextoAnotacao.setBorder(BorderFactory.createMatteBorder(
            0, 2, 2, 2, gerenciadorDeRecursos.getColor("dark_gray")
        ));

        painelSuperiorVazio = new JPanel();
        painelSuperiorVazio.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelSuperiorVazio.setBorder(BorderFactory.createMatteBorder(
            2, 2, 0, 0, gerenciadorDeRecursos.getColor("dark_gray")
        ));

        labelTriangulo = new JLabel(gerenciadorDeRecursos.getImagem("misc_triangulo_anotacao"));

        // ----------------------------------------------------------------------------

        super.adicionarComponenteAoPainel(painelSuperiorVazio);
        painelSuperiorVazio.setBounds(
            0, 0,
            LARGURA_MINIMA - labelTriangulo.getPreferredSize().width,
            labelTriangulo.getPreferredSize().height
        );

        super.adicionarComponenteAoPainel(labelTriangulo);
        labelTriangulo.setBounds(
            LARGURA_MINIMA - labelTriangulo.getPreferredSize().width, 0,
            labelTriangulo.getPreferredSize().width, labelTriangulo.getPreferredSize().height
        );

        super.adicionarComponenteAoPainel(painelTextoAnotacao);
        painelTextoAnotacao.setBounds(
            0, painelSuperiorVazio.getBounds().height, LARGURA_MINIMA,
            ALTURA_MINIMA - painelSuperiorVazio.getBounds().height
        );

        super.setBounds(LARGURA_MINIMA, ALTURA_MINIMA);
    }

    public void atualizarComponentesGraficos() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // Realizando as quebras de linha do texto

        painelTextoAnotacao.removeAll();

        if (!modeloAtual.getTexto().isEmpty()) {
            ArrayList<String> linhasComentario = quebrarTextoEmLinhas(modeloAtual.getTexto());

            for (int i = 0; i < linhasComentario.size(); i++) {
                JLabel labelLinha = new JLabel(linhasComentario.get(i), JLabel.CENTER);
                labelLinha.setOpaque(false);
                labelLinha.setFont(gerenciadorDeRecursos.getRobotoMedium(13).deriveFont(Font.ITALIC));
                labelLinha.setForeground(gerenciadorDeRecursos.getColor("dark_charcoal"));

                painelTextoAnotacao.add(labelLinha, String.format("wrap, grow, gaptop %d, gapbottom 0", i == 0 ? 7 : 0));
            }
        }

        // ----------------------------------------------------------------------------

        int novaLargura = Math.max(painelTextoAnotacao.getPreferredSize().width, LARGURA_MINIMA);
        int novaAltura = Math.max(painelTextoAnotacao.getPreferredSize().height + painelSuperiorVazio.getBounds().height, ALTURA_MINIMA);

        painelSuperiorVazio.setBounds(
            0, 0,
            novaLargura - labelTriangulo.getPreferredSize().width,
            labelTriangulo.getPreferredSize().height
        );

        labelTriangulo.setBounds(
            novaLargura - labelTriangulo.getPreferredSize().width, 0,
            labelTriangulo.getPreferredSize().width, labelTriangulo.getPreferredSize().height
        );

        painelTextoAnotacao.setBounds(
            0, painelSuperiorVazio.getBounds().height, novaLargura,
            novaAltura - painelSuperiorVazio.getBounds().height
        );

        super.setBounds(novaLargura, novaAltura);
    }

    private ArrayList<String> quebrarTextoEmLinhas(String texto) {
        ArrayList<String> linhasTexto = new ArrayList<>();

        int numCaracteresSubstring = 0;
        int inicioSubstring = 0;

        for (int i = 0; i < texto.length(); i++) {
            if ((numCaracteresSubstring >= modeloAtual.getNumCharsQuebrarTexto() &&
                    texto.charAt(i) == ' ') || texto.charAt(i) == '\n') {

                linhasTexto.add(texto.substring(inicioSubstring, i));
                inicioSubstring = i + 1;
                numCaracteresSubstring = 0;
            }

            numCaracteresSubstring++;
        }

        // Se ainda tiver sobrado caracteres adiciona o restante a lista
        if (numCaracteresSubstring != 0) {
            linhasTexto.add(texto.substring(inicioSubstring));
        }
        return linhasTexto;
    }

    @Override
    public void setModelo(ModeloDeComponenteUML<Anotacao> novoModelo) {
        modeloAtual = (Anotacao) novoModelo;
        atualizarComponentesGraficos();
    }

    @Override
    protected void initFrameGerenciarComponente() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelAnotacao = new JLabel(gerenciadorDeRecursos.getString("anotacao_maiuscula"), JLabel.CENTER);
        labelAnotacao.setFont(gerenciadorDeRecursos.getRobotoBlack(14));

        JPanel painelLabelAnotacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelAnotacao.setBorder(BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("black"))
        );
        painelLabelAnotacao.add(labelAnotacao, "align center");
        painelLabelAnotacao.setOpaque(false);

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarAnotacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelGerenciarAnotacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        JLabel labelTextoAnotacao = new JLabel(gerenciadorDeRecursos.getString("texto"));
        labelTextoAnotacao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        // ----------------------------------------------------------------------------

        JTextArea textAreaAnotacao = new JTextArea(5, 7);
        textAreaAnotacao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textAreaAnotacao.setLineWrap(true);
        textAreaAnotacao.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textAreaAnotacao.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modeloAtual.setTexto(textAreaAnotacao.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modeloAtual.setTexto(textAreaAnotacao.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modeloAtual.setTexto(textAreaAnotacao.getText());
            }
        });

        JScrollPane scrollPaneAnotacao = new JScrollPane(textAreaAnotacao);
        scrollPaneAnotacao.setBorder(BorderFactory.createCompoundBorder(
            new JTextField().getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        // ----------------------------------------------------------------------------

        JLabel labelSeparadorAnotacao = new JLabel();
        labelSeparadorAnotacao.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("cultured")),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand"))
        ));

        JLabel labelQuebrarAnotacao = new JLabel(gerenciadorDeRecursos.getString("quebrar_texto"));
        labelQuebrarAnotacao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelQuebrarAnotacao.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));

        int MIN_NUM_CARACTERES = 20;
        int MAX_NUM_CARACTERES = 200;

        JSpinner spinnerAnotacao = new JSpinner(new SpinnerNumberModel(
            MIN_NUM_CARACTERES, MIN_NUM_CARACTERES, MAX_NUM_CARACTERES, 1
        ));
        spinnerAnotacao.setFont(gerenciadorDeRecursos.getRobotoBlack(13));
        spinnerAnotacao.setFocusable(false);
        spinnerAnotacao.setForeground(gerenciadorDeRecursos.getColor("dark_gray"));
        ((JSpinner.DefaultEditor) spinnerAnotacao.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spinnerAnotacao.getEditor()).getTextField().setFocusable(false);
        spinnerAnotacao.addChangeListener(e -> modeloAtual.setNumCharsQuebrarTexto((int) spinnerAnotacao.getValue()));

        // ----------------------------------------------------------------------------

        painelGerenciarAnotacao.add(labelTextoAnotacao, "wrap, gapbottom 8");
        painelGerenciarAnotacao.add(scrollPaneAnotacao, "wrap, grow");
        painelGerenciarAnotacao.add(labelSeparadorAnotacao, "wrap, gaptop 15, gapbottom 15");
        painelGerenciarAnotacao.add(labelQuebrarAnotacao, "split 2, grow, gapright 10");
        painelGerenciarAnotacao.add(spinnerAnotacao, "wrap, grow");

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
                adicionarAlteracaoDeComponente(new EstruturaModificada<>(
                    modeloAntesDeAlteracoes.copiar(),
                    modeloAtual.copiar(),
                    AnotacaoUML.this
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

                textAreaAnotacao.setText(modeloAtual.getTexto());
                spinnerAnotacao.setValue(modeloAtual.getNumCharsQuebrarTexto());
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                    adicionarAlteracaoDeComponente(new EstruturaModificada<>(
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar(),
                        AnotacaoUML.this
                    ));

                    atualizarComponentesGraficos();
                    modeloAntesDeAlteracoes = modeloAtual.copiar();
                }
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarComponente = new JPanel(new MigLayout("insets 0 0 20 0", "","[grow, fill][grow, fill][]"));
        painelGerenciarComponente.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelGerenciarComponente.add(painelLabelAnotacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(painelGerenciarAnotacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(botaoAplicar, "align right, gaptop 10, gapbottom 10");

        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();
    }

    @Override
    public String toString() {
        return "ANOTACAO_UML\n// Posicao X e Y\n" +
                super.getPainelComponente().getX() + "\n" +
                super.getPainelComponente().getY() +
                "\n// Texto\n" +
                modeloAtual.getTexto().replaceAll("\n", "(novaLinha)") +
                "\n// Numero de caracteres para quebra de linha\n" +
                modeloAtual.getNumCharsQuebrarTexto() + "\n";
    }
}

