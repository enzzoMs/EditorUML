package componentes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.ComponenteModificado;
import interfacegrafica.AreaDeDiagramas;
import modelos.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Classe que representa a parte gráfica de um componente UML do tipo "Classe". Possui
 * capacidade para representar atributos, métodos e características de uma classe, como
 * nome ou se é abstrata. Além disso, é possível utilizar esse mesmo componente para
 * representar uma interface.
 */
public class ClasseUML extends ComponenteUML<Classe> {
    private Classe modeloAtual = new Classe();
    private Classe modeloAntesDeAlteracoes;
    private final JPanel painelNomeClasse = new JPanel();
    private final JList<String> jListAtributos = new JList<>();
    private final JList<String> jListMetodos = new JList<>();
    private static final int LARGURA_MINIMA = 120;

    public ClasseUML(AreaDeDiagramas areaDeDiagramas) {
        super(areaDeDiagramas);

        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelNomeClasse = new JLabel(modeloAtual.getNome(), JLabel.CENTER);
        labelNomeClasse.setFont(gerenciadorDeRecursos.getRobotoBlack(15));
        labelNomeClasse.setOpaque(false);
        labelNomeClasse.setForeground(gerenciadorDeRecursos.getColor("white"));

        painelNomeClasse.setLayout(new MigLayout("fill"));
        painelNomeClasse.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));
        painelNomeClasse.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(2, 2, 0, 2, gerenciadorDeRecursos.getColor("dark_gray")),
            new EmptyBorder(5, 13, 5, 13)
        ));
        painelNomeClasse.add(labelNomeClasse, "north");

        // ----------------------------------------------------------------------------

        DefaultListModel<String> modelAtributos = new DefaultListModel<>();
        // Adicionando um elemento vazio só para que o JList nao tenha tamanho 0
        modelAtributos.add(0, "");

        jListAtributos.setModel(modelAtributos);
        jListAtributos.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        jListAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListAtributos.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(2, 2, 0, 2, gerenciadorDeRecursos.getColor("dark_gray")),
            new EmptyBorder(5, 3, 5, 3)
        ));

        // ----------------------------------------------------------------------------

        DefaultListModel<String> modelMetodos = new DefaultListModel<>();
        // Adicionando um elemento vazio só para que o JList nao tenha tamanho 0
        modelMetodos.add(0, "");

        jListMetodos.setModel(modelMetodos);
        jListMetodos.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        jListMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListMetodos.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(2, 2, 2, 2, gerenciadorDeRecursos.getColor("dark_gray")),
            new EmptyBorder(5, 3, 5, 3)
        ));

        // ----------------------------------------------------------------------------

        int largura = LARGURA_MINIMA;

        int altura = painelNomeClasse.getPreferredSize().height +
                jListAtributos.getPreferredSize().height +
                jListMetodos.getPreferredSize().height;

        super.adicionarComponenteAoPainel(painelNomeClasse);
        painelNomeClasse.setBounds(0, 0, largura, painelNomeClasse.getPreferredSize().height);

        super.adicionarComponenteAoPainel(jListAtributos);
        jListAtributos.setBounds(
            0, painelNomeClasse.getBounds().height,
            largura, jListAtributos.getPreferredSize().height
        );

        super.adicionarComponenteAoPainel(jListMetodos);
        jListMetodos.setBounds(
            0, (jListAtributos.getBounds().y + jListAtributos.getBounds().height),
            largura, jListMetodos.getPreferredSize().height
        );

        super.setBounds(largura, altura);
        // TODO: mudar a implementacao disso para outro lugar
        /*
        JLabel labelUndo = (JLabel) diagramaUML.getAreaDeDiagramas().getPainelOpcoesQuadroBranco().getComponent(3);
        JLabel labelRedo = (JLabel) diagramaUML.getAreaDeDiagramas().getPainelOpcoesQuadroBranco().getComponent(4);

        labelRedo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClasseUML.this.getFrameGerenciarComponente().setVisible(false);
            }
        });

        labelUndo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClasseUML.this.getFrameGerenciarComponente().setVisible(false);
            }
        });*/
    }


    // TODO: isso vem de reconstruir componente e deve ser movido para um metodo melhor
    /*
    public void setArrayListAtributos(ArrayList<Atributo> novaArrayListAtributos) {
        this.arrayListAtributos = novaArrayListAtributos;
    }

    public void setArrayListMetodos(ArrayList<Metodo> novaArrayListMetodos) {
        this.arrayListMetodos = novaArrayListMetodos;
    }*/

    private void atualizarComponentesGraficos() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelNomeClasse = (JLabel) painelNomeClasse.getComponent(0);
        labelNomeClasse.setText(modeloAtual.getNome());

        // ----------------------------------------------------------------------------

        if (modeloAtual.ehAbstrata()) {
            labelNomeClasse.setFont(gerenciadorDeRecursos.getRobotoBlack(15).deriveFont(Font.ITALIC));
            painelNomeClasse.setBackground(gerenciadorDeRecursos.getColor("gray"));
        } else {
            labelNomeClasse.setFont(gerenciadorDeRecursos.getRobotoBlack(15));
            painelNomeClasse.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));
        }

        // ----------------------------------------------------------------------------

        painelNomeClasse.removeAll();

        if (modeloAtual.ehInterface()) {
            JLabel labelInterface = new JLabel(gerenciadorDeRecursos.getString("interface_uml"), JLabel.CENTER);
            labelInterface.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
            labelInterface.setForeground(gerenciadorDeRecursos.getColor("white"));

            painelNomeClasse.add(labelInterface, "north, gapbottom 5");
            painelNomeClasse.add(labelNomeClasse, "north");
        } else {
            painelNomeClasse.add(labelNomeClasse, "north");
        }

        // ----------------------------------------------------------------------------

        // Realizando as quebras de linha do comentario

        if (!modeloAtual.getComentario().isEmpty()) {
            ArrayList<String> linhasComentario = quebrarComentarioEmLinhas(modeloAtual.getComentario());

            for (int i = 0; i < linhasComentario.size(); i++) {
                JLabel labelComentario = new JLabel(linhasComentario.get(i), JLabel.CENTER);
                labelComentario.setOpaque(false);
                labelComentario.setFont(gerenciadorDeRecursos.getRobotoMedium(13).deriveFont(Font.ITALIC));
                labelComentario.setForeground(gerenciadorDeRecursos.getColor("white"));

                painelNomeClasse.add(labelComentario, String.format("wrap, grow, gaptop %d, gapbottom 0", i == 0 ? 7 : 0));
            }
        }

        // ----------------------------------------------------------------------------

        DefaultListModel<String> listModelAtributos = (DefaultListModel<String>) jListAtributos.getModel();
        listModelAtributos.clear();

        for (Atributo atributo : modeloAtual.getAtributos()) {
            String atributoTextoUml = (atributo.ehEstatico() ? "<html><u>" : "") +
                    // em interfaces a visibilidade nao é mostrada
                    (modeloAtual.ehInterface() ? "" : atributo.getVisibilidade().getSimbolo()) +
                    atributo.getNome() +
                    (atributo.getTipo().isEmpty() ? "" : ": " + atributo.getTipo()) +
                    (atributo.getValorPadrao().isEmpty() ? "" : " = " + atributo.getValorPadrao()) +
                    (atributo.ehEstatico() ? "</u></html>" : "");

            listModelAtributos.addElement(atributoTextoUml);
        }

        if (modeloAtual.getAtributos().isEmpty()) {
            // se nao houver atributos adiciona uma string vazia somente para que o tamanho do painel nao seja 0
            listModelAtributos.add(0, "");
        }

        // ----------------------------------------------------------------------------

        DefaultListModel<String> listModelMetodos = (DefaultListModel<String>) jListMetodos.getModel();
        listModelMetodos.clear();

        for (Metodo metodo : modeloAtual.getMetodos()) {
            ArrayList<Parametro> parametros = metodo.getParametros();

            // Se o metodo tiver mais de um parametro então eles serao mostrados em linhas separadas
            boolean quebrarParametrosEmLinhas = parametros.size() > 1;

            StringBuilder metodoTextoUml = new StringBuilder();

            metodoTextoUml.append("<html>")
                .append(metodo.ehEstatico() ? "<u>" : "")
                .append(metodo.ehAbstrato() ? "<i>" : "")
                .append(modeloAtual.ehInterface() ? "" : metodo.getVisibilidade().getSimbolo())
                .append(metodo.getNome())
                .append("(")
                // Se o metodos tiver mais de um parametro eles sao mostrados em linhas separadas
                .append(quebrarParametrosEmLinhas ? "<br>" : "");

            for (int i = 0; i < parametros.size(); i++) {
                metodoTextoUml.append(quebrarParametrosEmLinhas ? "&emsp;" : "")
                    .append(parametros.get(i).getRepresentacaoUml())
                    .append((i == parametros.size() - 1) ? "" : ",")
                    .append(quebrarParametrosEmLinhas ? "<br>" : "");
            }

            metodoTextoUml.append(")")
                .append(metodo.getTipo().isEmpty() ? "" : ": " + metodo.getTipo())
                .append(metodo.ehAbstrato() ? "</i>" : "")
                .append(metodo.ehEstatico() ? "</u>" : "")
                .append("<html>");

            listModelMetodos.addElement(String.valueOf(metodoTextoUml));
        }

        if (modeloAtual.getMetodos().isEmpty()) {
            // se nao houver metodos adiciona uma string vazia somente para que o tamanho do painel nao seja 0
            listModelMetodos.add(0, "");
        }

        // ----------------------------------------------------------------------------

        int novaLargura = Math.max(painelNomeClasse.getPreferredSize().width,
                          Math.max(jListMetodos.getPreferredSize().width, jListAtributos.getPreferredSize().width));

        novaLargura = Math.max(novaLargura, LARGURA_MINIMA);

        int novaAltura = painelNomeClasse.getPreferredSize().height +
                jListAtributos.getPreferredSize().height +
                jListMetodos.getPreferredSize().height;

        painelNomeClasse.setBounds(0, 0, novaLargura, painelNomeClasse.getPreferredSize().height);

        jListAtributos.setBounds(
            0, painelNomeClasse.getBounds().height,
            novaLargura,
            jListAtributos.getPreferredSize().height
        );

        jListMetodos.setBounds(
            0, (jListAtributos.getBounds().y + jListAtributos.getBounds().height),
            novaLargura,
            jListMetodos.getPreferredSize().height
        );

        super.setBounds(novaLargura, novaAltura);
    }

    private ArrayList<String> quebrarComentarioEmLinhas(String novoComentario) {
        ArrayList<String> linhasComentario = new ArrayList<>();

        int numCaracteresSubstring = 0;
        int inicioSubstring = 0;

        for (int i = 0; i < novoComentario.length(); i++) {
            if ((numCaracteresSubstring >= modeloAtual.getNumCharsQuebrarComentario() &&
                    novoComentario.charAt(i) == ' ') || novoComentario.charAt(i) == '\n') {

                linhasComentario.add(novoComentario.substring(inicioSubstring, i));
                inicioSubstring = i + 1;
                numCaracteresSubstring = 0;
            }

            numCaracteresSubstring++;
        }

        // Se ainda tiver sobrado caracteres adiciona o restante a lista
        if (numCaracteresSubstring != 0) {
            linhasComentario.add(novoComentario.substring(inicioSubstring));
        }
        return linhasComentario;
    }

    @Override
    public void setModelo(ModeloDeComponenteUML<Classe> novoModelo) {
        modeloAtual = (Classe) novoModelo;
        atualizarComponentesGraficos();
    }

    @Override
    protected void initFrameGerenciarComponente() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JPanel painelGerenciarComponente = new JPanel(
            new MigLayout("insets 0 0 20 0, fill", "","[grow, fill][grow, fill][]")
        );
        painelGerenciarComponente.setBackground(gerenciadorDeRecursos.getColor("white"));

        // ----------------------------------------------------------------------------

        JPanel[] paineisGerenciarComponente = {
            getPainelGerenciarClasse(),
            getPainelGerenciarAtributos(),
            getPainelGerenciarMetodos()
        };

        // ----------------------------------------------------------------------------

        String[] labelOpcoesNavegacao = {
            gerenciadorDeRecursos.getString("classe_maiuscula"),
            gerenciadorDeRecursos.getString("atributos_maiuscula"),
            gerenciadorDeRecursos.getString("metodos_maiuscula")
        };

        JPanel[] paineisOpcoesNavegacao = new JPanel[labelOpcoesNavegacao.length];

        MatteBorder bordaAtiva = BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")
        );
        MatteBorder bordaDesativada = BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("anti_flash_white")
        );

        for (int i = 0; i < labelOpcoesNavegacao.length; i++) {
            JButton botaoOpcao = new JButton(labelOpcoesNavegacao[i]);
            botaoOpcao.setFont(gerenciadorDeRecursos.getRobotoBlack(14));
            botaoOpcao.setFocusable(false);
            botaoOpcao.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));
            botaoOpcao.setBorder(BorderFactory.createEmptyBorder());
            botaoOpcao.setRolloverEnabled(false);
            botaoOpcao.setContentAreaFilled(false);
            botaoOpcao.addMouseListener(new MouseAdapter() {

                // Se a cor do texto for preta então o texto está selecionado
                @Override
                public void mouseEntered(MouseEvent e) {
                    JButton sourceButton = (JButton) e.getSource();

                    if (sourceButton.getForeground().getRGB() != gerenciadorDeRecursos.getColor("black").getRGB()) {
                        sourceButton.setForeground(gerenciadorDeRecursos.getColor("granite_gray"));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    JButton sourceButton = (JButton) e.getSource();

                    if (sourceButton.getForeground().getRGB() != gerenciadorDeRecursos.getColor("black").getRGB()) {
                        sourceButton.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));
                    }
                }
            });

            JPanel painelBotaoOpcao = new JPanel(new MigLayout("insets 20 40 20 40"));
            painelBotaoOpcao.setBorder(bordaDesativada);
            painelBotaoOpcao.add(botaoOpcao);
            painelBotaoOpcao.setOpaque(false);

            paineisOpcoesNavegacao[i] = (painelBotaoOpcao);
        }

        // A primeira opcao está selecionada por padrão
        paineisOpcoesNavegacao[0].getComponent(0).setForeground(gerenciadorDeRecursos.getColor("black"));
        paineisOpcoesNavegacao[0].setBorder(bordaAtiva);

        // ----------------------------------------------------------------------------

        for (int i = 0; i < paineisOpcoesNavegacao.length; i++) {
            JPanel painelOpcao = paineisOpcoesNavegacao[i];
            JPanel painelGerenciarOpcao = paineisGerenciarComponente[i];
            int indexOpcao = i;

            ((JButton) painelOpcao.getComponent(0)).addActionListener(e -> {
                if (painelGerenciarOpcao.getParent() == null) {

                    for (int j = 0; j < paineisOpcoesNavegacao.length; j++) {
                        paineisOpcoesNavegacao[j].setBorder(
                            j == indexOpcao ? bordaAtiva : bordaDesativada
                        );

                        paineisOpcoesNavegacao[j].getComponent(0).setForeground(
                            gerenciadorDeRecursos.getColor(j == indexOpcao ? "black" : "spanish_gray")
                        );
                    }

                    painelGerenciarComponente.remove(painelGerenciarComponente.getComponent(1));
                    painelGerenciarComponente.add(painelGerenciarOpcao, "grow, wrap", 1);

                    painelGerenciarComponente.revalidate();
                    painelGerenciarComponente.repaint();
                }
            });
        }

        // ----------------------------------------------------------------------------

        JPanel opcoesNavegacao = new JPanel(new MigLayout("insets 0 0 0 0, fill"));
        opcoesNavegacao.setBackground(gerenciadorDeRecursos.getColor("white"));

        opcoesNavegacao.add(paineisOpcoesNavegacao[0], "split 3, gapright 0, grow");
        opcoesNavegacao.add(paineisOpcoesNavegacao[1], "gapright 0, grow");
        opcoesNavegacao.add(paineisOpcoesNavegacao[2], "grow");

        // ----------------------------------------------------------------------------

        JDialog dialogErroNomeClasse = getDialogErroNomeClasse();

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
            if (modeloAtual.getNome().isEmpty()) {
                dialogErroNomeClasse.setLocationRelativeTo(null);
                dialogErroNomeClasse.setVisible(true);
                return;
            }

            if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                adicionarAlteracaoDeComponente(new ComponenteModificado<>(
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar(),
                    ClasseUML.this
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
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (modeloAtual.getNome().isEmpty()) {
                    dialogErroNomeClasse.setLocationRelativeTo(null);
                    dialogErroNomeClasse.setVisible(true);
                    return;
                }

                if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                    adicionarAlteracaoDeComponente(new ComponenteModificado<>(
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar(),
                        ClasseUML.this
                    ));

                    atualizarComponentesGraficos();

                    modeloAntesDeAlteracoes = modeloAtual.copiar();
                }
            }
        });

        // ----------------------------------------------------------------------------

        painelGerenciarComponente.add(opcoesNavegacao, "north");

        // Encontra o maior painel possivel para que a janela nao seja redimensionada depois
        int maiorLarguraPossivel = Math.max(paineisGerenciarComponente[0].getPreferredSize().width,
                Math.max(paineisGerenciarComponente[1].getPreferredSize().width,
                paineisGerenciarComponente[2].getPreferredSize().width));

        int maiotAlturaPossivel = Math.max(paineisGerenciarComponente[0].getPreferredSize().height,
                Math.max(paineisGerenciarComponente[1].getPreferredSize().height,
                paineisGerenciarComponente[2].getPreferredSize().height));


        JPanel painelTamanhoMaximo = new JPanel();
        painelTamanhoMaximo.setPreferredSize(new Dimension(maiorLarguraPossivel, maiotAlturaPossivel));


        painelGerenciarComponente.add(painelTamanhoMaximo, "grow, wrap");

        painelGerenciarComponente.add(botaoAplicar, "align right, gaptop:push, gapbottom 0");

        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();

        painelGerenciarComponente.remove(painelTamanhoMaximo);
        painelGerenciarComponente.add(paineisGerenciarComponente[0], "grow, wrap", 1);
    }

    private JPanel getPainelGerenciarClasse() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelNomeClasse = new JLabel(gerenciadorDeRecursos.getString("nome_da_classe"));
        labelNomeClasse.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        JTextField textFieldNomeClasse = new JTextField(gerenciadorDeRecursos.getString("classe_nome_default"));

        Border bordaTextField = BorderFactory.createCompoundBorder(
                textFieldNomeClasse.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        textFieldNomeClasse.setBorder(bordaTextField);
        textFieldNomeClasse.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomeClasse.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomeClasse.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomeClasse.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomeClasse.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modeloAtual.setNome(textFieldNomeClasse.getText());
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelComentarioClasse = new JLabel(gerenciadorDeRecursos.getString("comentarios"));
        labelComentarioClasse.setFont(gerenciadorDeRecursos.getRobotoMedium(15));

        JTextArea textAreaComentarioClasse = new JTextArea(8, 7);
        textAreaComentarioClasse.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textAreaComentarioClasse.setLineWrap(true);
        textAreaComentarioClasse.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textAreaComentarioClasse.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modeloAtual.setComentario(textAreaComentarioClasse.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modeloAtual.setComentario(textAreaComentarioClasse.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modeloAtual.setComentario(textAreaComentarioClasse.getText());
            }
        });

        JScrollPane scrollPaneComentarioClasse = new JScrollPane(textAreaComentarioClasse);
        scrollPaneComentarioClasse.setBorder(bordaTextField);

        // ----------------------------------------------------------------------------

        JLabel labelQuebrarComentario = new JLabel(gerenciadorDeRecursos.getString("quebrar_texto"));
        labelQuebrarComentario.setFont(gerenciadorDeRecursos.getRobotoMedium(13));
        labelQuebrarComentario.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));

        int MIN_NUM_CARACTERES = 20;
        int MAX_NUM_CARACTERES = 200;

        JSpinner spinnerNumCaracteres = new JSpinner(new SpinnerNumberModel(
            MIN_NUM_CARACTERES, MIN_NUM_CARACTERES, MAX_NUM_CARACTERES, 1
        ));
        spinnerNumCaracteres.setFont(gerenciadorDeRecursos.getRobotoBlack(13));
        spinnerNumCaracteres.setFocusable(false);
        spinnerNumCaracteres.setForeground(gerenciadorDeRecursos.getColor("onyx"));
        ((JSpinner.DefaultEditor) spinnerNumCaracteres.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spinnerNumCaracteres.getEditor()).getTextField().setFocusable(false);
        spinnerNumCaracteres.addChangeListener(e -> {
            modeloAtual.setNumCharsQuebrarComentario((int) spinnerNumCaracteres.getValue());
        });

        // ----------------------------------------------------------------------------

        // Só uma linha para separar duas áreas do layout

        JLabel labelSeparador = new JLabel();
        labelSeparador.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("cultured")),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand"))
        ));

        // ----------------------------------------------------------------------------

        JCheckBox checkBoxAbstrata = new JCheckBox(gerenciadorDeRecursos.getString("abstrata"));
        checkBoxAbstrata.setBorder(bordaTextField);
        checkBoxAbstrata.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        checkBoxAbstrata.setForeground(gerenciadorDeRecursos.getColor("black"));
        checkBoxAbstrata.setFocusable(false);

        // ----------------------------------------------------------------------------

        JCheckBox checkBoxInterface = new JCheckBox(gerenciadorDeRecursos.getString("interface"));
        checkBoxInterface.setBorder(bordaTextField);
        checkBoxInterface.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        checkBoxInterface.setForeground(gerenciadorDeRecursos.getColor("black"));
        checkBoxInterface.setFocusable(false);

        // ----------------------------------------------------------------------------

        checkBoxAbstrata.addActionListener(e -> {
            checkBoxInterface.setSelected(false);
            modeloAtual.setAbstrata(checkBoxAbstrata.isSelected());
            modeloAtual.setInterface(false);
        });

        checkBoxInterface.addActionListener(e -> {
            checkBoxAbstrata.setSelected(false);
            modeloAtual.setInterface(checkBoxInterface.isSelected());
            modeloAtual.setAbstrata(false);
        });

        // ----------------------------------------------------------------------------

        // Quando o frame gerenciar for aberto os text fields e campos devem ser atualizados de acordo com o modelo
        // do componente. Isso deve ser feito por conta da possibilidade do modelo ter sido modificado em algum momento
        getFrameGerenciarComponente().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                textFieldNomeClasse.setText(modeloAtual.getNome());
                textAreaComentarioClasse.setText(modeloAtual.getComentario());
                spinnerNumCaracteres.setValue(modeloAtual.getNumCharsQuebrarComentario());
                checkBoxAbstrata.setSelected(modeloAtual.ehAbstrata());
                checkBoxInterface.setSelected(modeloAtual.ehInterface());
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarClasse = new JPanel(new MigLayout("insets 30 30 15 30", "[grow]"));
        painelGerenciarClasse.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelGerenciarClasse.add(labelNomeClasse, "wrap, gapbottom 8");
        painelGerenciarClasse.add(textFieldNomeClasse, "wrap, grow");
        painelGerenciarClasse.add(labelComentarioClasse, "wrap, gaptop 15, gapbottom 8");
        painelGerenciarClasse.add(scrollPaneComentarioClasse, "wrap, grow, gapbottom 10");
        painelGerenciarClasse.add(labelQuebrarComentario, "split 2, grow, gapright 10");
        painelGerenciarClasse.add(spinnerNumCaracteres, "wrap, grow");
        painelGerenciarClasse.add(labelSeparador, "grow, wrap, gaptop 15, gapbottom 5");
        painelGerenciarClasse.add(checkBoxAbstrata, "split 3, gapleft 25, gapright:push");
        painelGerenciarClasse.add(checkBoxInterface, "wrap, gapright 25");

        return painelGerenciarClasse;
    }

    private JPanel getPainelGerenciarAtributos() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JList<String> jListGerenciarAtributos = new JList<>();
        JTextField textFieldNomeAtributo = new JTextField();
        JTextField textFieldTipoAtributo = new JTextField();
        JTextField textFieldValorAtributo = new JTextField();
        JComboBox<String> comboBoxVisibilidadeAtributo = new JComboBox<>();
        JCheckBox checkBoxAtributoEstatico = new JCheckBox(gerenciadorDeRecursos.getString("estatico"));

        // ----------------------------------------------------------------------------

        JLabel labelNomeAtributo = new JLabel(gerenciadorDeRecursos.getString("nome"));
        labelNomeAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelNomeAtributo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        CompoundBorder bordaTextField = BorderFactory.createCompoundBorder(
            textFieldNomeAtributo.getBorder(),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        );

        textFieldNomeAtributo.setBorder(bordaTextField);
        textFieldNomeAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomeAtributo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomeAtributo.setEnabled(false);

        // ----------------------------------------------------------------------------

        JLabel labelTipoAtributo = new JLabel(gerenciadorDeRecursos.getString("tipo"));
        labelTipoAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelTipoAtributo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldTipoAtributo.setBorder(bordaTextField);
        textFieldTipoAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldTipoAtributo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldTipoAtributo.setEnabled(false);

        // ----------------------------------------------------------------------------

        JLabel labelValorAtributo = new JLabel(gerenciadorDeRecursos.getString("valor_padrao"));
        labelValorAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelValorAtributo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldValorAtributo.setBorder(bordaTextField);
        textFieldValorAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldValorAtributo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldValorAtributo.setEnabled(false);

        // ----------------------------------------------------------------------------

        DocumentListener camposAtributoListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarAtributoSelecionado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarAtributoSelecionado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarAtributoSelecionado();
            }

            private void atualizarAtributoSelecionado() {
                if (jListGerenciarAtributos.getSelectedIndex() != -1) {
                    Atributo atributoSelecionado = modeloAtual.getAtributos().get(jListGerenciarAtributos.getSelectedIndex());

                    atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                    atributoSelecionado.setTipo(textFieldTipoAtributo.getText());
                    atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());

                    ((DefaultListModel<String>) jListGerenciarAtributos.getModel()).setElementAt(
                        atributoSelecionado.getRepresentacaoUml(), jListGerenciarAtributos.getSelectedIndex()
                    );
                }
            }
        };

        textFieldNomeAtributo.getDocument().addDocumentListener(camposAtributoListener);
        textFieldValorAtributo.getDocument().addDocumentListener(camposAtributoListener);
        textFieldTipoAtributo.getDocument().addDocumentListener(camposAtributoListener);

        // ----------------------------------------------------------------------------

        JLabel labelVisibilidadeAtributo = new JLabel(gerenciadorDeRecursos.getString("visibilidade"));
        labelVisibilidadeAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelVisibilidadeAtributo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        comboBoxVisibilidadeAtributo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        comboBoxVisibilidadeAtributo.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        comboBoxVisibilidadeAtributo.setEnabled(false);
        comboBoxVisibilidadeAtributo.setFocusable(false);
        for (Visibilidade visibilidade : Visibilidade.values()) {
            comboBoxVisibilidadeAtributo.addItem(visibilidade.getNome());
        }
        comboBoxVisibilidadeAtributo.addItemListener(e -> {
            if (jListGerenciarAtributos.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = modeloAtual.getAtributos().get(jListGerenciarAtributos.getSelectedIndex());

                atributoSelecionado.setVisibilidade(
                        Visibilidade.getVisibilidadePorNome((String) comboBoxVisibilidadeAtributo.getSelectedItem())
                );

                ((DefaultListModel<String>) jListGerenciarAtributos.getModel()).setElementAt(
                        atributoSelecionado.getRepresentacaoUml(), jListGerenciarAtributos.getSelectedIndex()
                );
            }
        });

        // ----------------------------------------------------------------------------

        checkBoxAtributoEstatico.setBorder(bordaTextField);
        checkBoxAtributoEstatico.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        checkBoxAtributoEstatico.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));
        checkBoxAtributoEstatico.setFocusable(false);
        checkBoxAtributoEstatico.setEnabled(false);
        checkBoxAtributoEstatico.addItemListener(e -> {
            if (jListGerenciarAtributos.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = modeloAtual.getAtributos().get(jListGerenciarAtributos.getSelectedIndex());

                atributoSelecionado.setEstatico(checkBoxAtributoEstatico.isSelected());

                ((DefaultListModel<String>) jListGerenciarAtributos.getModel()).setElementAt(
                        atributoSelecionado.getRepresentacaoUml(), jListGerenciarAtributos.getSelectedIndex()
                );
            }
        });

        // ----------------------------------------------------------------------------

        DefaultListModel<String> listModelAtributos = new DefaultListModel<>();
        jListGerenciarAtributos.setModel(listModelAtributos);
        jListGerenciarAtributos.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        jListGerenciarAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListGerenciarAtributos.setFocusable(false);

        JScrollPane scrollPaneAtributos = new JScrollPane(jListGerenciarAtributos);
        scrollPaneAtributos.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("silver_sand")
        ));

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarAtributos = new JPanel(new MigLayout("insets 30 30 15 30", "[grow, fill][grow, fill]"));
        painelGerenciarAtributos.setBackground(gerenciadorDeRecursos.getColor("white"));

        Consumer<Integer> emExcluirAtributo = (Integer indexAtributoExcluido) -> {
            modeloAtual.getAtributos().remove((int) indexAtributoExcluido);
        };

        Consumer<Void> emNovoAtributo = (Void) -> {
            Atributo novoAtributo = new Atributo();
            modeloAtual.getAtributos().add(novoAtributo);
        };

        Consumer<Void> emAcimaAtributo = (Void) -> {
            Collections.swap(modeloAtual.getAtributos(), jListGerenciarAtributos.getSelectedIndex(), jListGerenciarAtributos.getSelectedIndex() - 1);
        };

        Consumer<Void> emAbaixoAtributo = (Void) -> {
            Collections.swap(modeloAtual.getAtributos(), jListGerenciarAtributos.getSelectedIndex(), jListGerenciarAtributos.getSelectedIndex() + 1);
        };

        JPanel painelOpcoesListaAtributos = getPainelOpcoesListaDeElementos(
            jListGerenciarAtributos, (DefaultListModel<String>) jListGerenciarAtributos.getModel(),
            emExcluirAtributo, emNovoAtributo, emAcimaAtributo, emAbaixoAtributo
        );

        // ----------------------------------------------------------------------------

        Consumer<Boolean> habilitarCamposAtributo = (habilitar) -> {
            Color corForegroundCampos = gerenciadorDeRecursos.getColor(habilitar ? "black" : "spanish_gray");

            textFieldNomeAtributo.setEnabled(habilitar);
            textFieldTipoAtributo.setEnabled(habilitar);
            textFieldValorAtributo.setEnabled(habilitar);
            labelNomeAtributo.setForeground(corForegroundCampos);
            labelTipoAtributo.setForeground(corForegroundCampos);
            labelValorAtributo.setForeground(corForegroundCampos);
            labelVisibilidadeAtributo.setForeground(corForegroundCampos);
            checkBoxAtributoEstatico.setForeground(corForegroundCampos);
            checkBoxAtributoEstatico.setEnabled(habilitar);
            comboBoxVisibilidadeAtributo.setEnabled(habilitar);

            if (!habilitar) {
                textFieldNomeAtributo.setText("");
                textFieldTipoAtributo.setText("");
                textFieldValorAtributo.setText("");
                comboBoxVisibilidadeAtributo.setSelectedIndex(0);
                checkBoxAtributoEstatico.setSelected(false);
            }
        };

        jListGerenciarAtributos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int indexSelecionado = jListGerenciarAtributos.getSelectedIndex();

                if (indexSelecionado != -1) {
                    habilitarCamposAtributo.accept(true);

                    Atributo atributoSelecionado = modeloAtual.getAtributos().get(jListGerenciarAtributos.getSelectedIndex());

                    // Removendo os listener para impedir que o atributo seja modificado de forma incorreta durante
                    // os 'setText' abaixo
                    textFieldNomeAtributo.getDocument().removeDocumentListener(camposAtributoListener);
                    textFieldTipoAtributo.getDocument().removeDocumentListener(camposAtributoListener);
                    textFieldValorAtributo.getDocument().removeDocumentListener(camposAtributoListener);

                    textFieldNomeAtributo.setText(atributoSelecionado.getNome());
                    textFieldTipoAtributo.setText(atributoSelecionado.getTipo());
                    textFieldValorAtributo.setText(atributoSelecionado.getValorPadrao());

                    textFieldNomeAtributo.getDocument().addDocumentListener(camposAtributoListener);
                    textFieldTipoAtributo.getDocument().addDocumentListener(camposAtributoListener);
                    textFieldValorAtributo.getDocument().addDocumentListener(camposAtributoListener);

                    checkBoxAtributoEstatico.setSelected(atributoSelecionado.ehEstatico());
                    // Nao é o ideal, mas desabilitar o combo box impede que o itemListener seja chamado, o que previne
                    // que o atributo selecionado seja modificado de forma incorreta
                    comboBoxVisibilidadeAtributo.setEnabled(false);
                    comboBoxVisibilidadeAtributo.setSelectedItem(atributoSelecionado.getVisibilidade().getNome());
                    comboBoxVisibilidadeAtributo.setEnabled(true);
                } else {
                    habilitarCamposAtributo.accept(false);
                }

                JButton botaoExcluir = (JButton) painelOpcoesListaAtributos.getComponent(1);
                JButton botaoAcima = (JButton) painelOpcoesListaAtributos.getComponent(2);
                JButton botaoAbaixo = (JButton) painelOpcoesListaAtributos.getComponent(3);

                botaoExcluir.setEnabled(indexSelecionado != -1);
                botaoAcima.setEnabled(indexSelecionado > 0);
                botaoAbaixo.setEnabled(indexSelecionado < modeloAtual.getAtributos().size() - 1 && indexSelecionado >= 0);
            }
        });

        // ----------------------------------------------------------------------------

        // Só uma linha para separar duas áreas do layout

        JLabel labelSeparador = new JLabel();
        labelSeparador.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("cultured")),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand"))
        ));

        // ----------------------------------------------------------------------------

        // Quando o frame gerenciar for aberto os text fields e campos devem ser atualizados de acordo com o modelo
        // do componente. Isso deve ser feito por conta da possibilidade do modelo ter sido modificado em algum momento
        getFrameGerenciarComponente().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                DefaultListModel<String> atributosModel = (DefaultListModel<String>) jListGerenciarAtributos.getModel();

                atributosModel.clear();

                for (Atributo atributo : modeloAtual.getAtributos()) {
                    atributosModel.addElement(atributo.getRepresentacaoUml());
                }
            }
        });

        // ----------------------------------------------------------------------------

        painelGerenciarAtributos.add(scrollPaneAtributos, "growy, growx, span 2, split 2, gapright 0");
        painelGerenciarAtributos.add(painelOpcoesListaAtributos, "growy, wrap, gapleft 0");

        painelGerenciarAtributos.add(labelNomeAtributo, "gap 0 8 10 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldNomeAtributo, "grow, gaptop 10, gapbottom 8, wrap");

        painelGerenciarAtributos.add(labelTipoAtributo, "gap 0 8 0 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldTipoAtributo, "grow, gapbottom 8, wrap");

        painelGerenciarAtributos.add(labelValorAtributo, "gap 0 8 0 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldValorAtributo, "wrap, grow, gapbottom 8");

        painelGerenciarAtributos.add(labelSeparador, "span 2, grow, wrap, gaptop 15, gapbottom 15");

        painelGerenciarAtributos.add(labelVisibilidadeAtributo, "gap 0 8 0 8, split 2, growy");
        painelGerenciarAtributos.add(comboBoxVisibilidadeAtributo, "grow, gapbottom 8");

        painelGerenciarAtributos.add(checkBoxAtributoEstatico, "gapbottom 8, gapleft:push, gapright:push");

        return painelGerenciarAtributos;
    }

    private JPanel getPainelGerenciarMetodos() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JList<String> jListGerenciarMetodos = new JList<>();
        jListGerenciarMetodos.setModel(new DefaultListModel<>());
        jListGerenciarMetodos.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        jListGerenciarMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListGerenciarMetodos.setFocusable(false);
        JScrollPane scrollPaneMetodos = new JScrollPane(jListGerenciarMetodos);
        scrollPaneMetodos.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("silver_sand"))
        );

        JTextField textFieldNomeMetodo = new JTextField();
        JTextField textFieldTipoMetodo = new JTextField();
        JComboBox<String> comboBoxVisibilidadeMetodo = new JComboBox<>();
        JCheckBox checkBoxMetodoEstatico = new JCheckBox(gerenciadorDeRecursos.getString("estatico"));
        JCheckBox checkBoxMetodoAbstrato = new JCheckBox(gerenciadorDeRecursos.getString("abstrata"));

        // ----------------------------------------------------------------------------

        JLabel labelNomeMetodo = new JLabel(gerenciadorDeRecursos.getString("nome"));
        labelNomeMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelNomeMetodo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        CompoundBorder bordaTextField = BorderFactory.createCompoundBorder(
            textFieldNomeMetodo.getBorder(),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        );

        textFieldNomeMetodo.setBorder(bordaTextField);
        textFieldNomeMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomeMetodo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomeMetodo.setEnabled(false);

        // ----------------------------------------------------------------------------

        JLabel labelTipoMetodo = new JLabel(gerenciadorDeRecursos.getString("tipo"));
        labelTipoMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelTipoMetodo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldTipoMetodo.setBorder(bordaTextField);
        textFieldTipoMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldTipoMetodo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldTipoMetodo.setEnabled(false);

        // ----------------------------------------------------------------------------

        DocumentListener camposMetodoListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarMetodoSelecionado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarMetodoSelecionado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarMetodoSelecionado();
            }

            private void atualizarMetodoSelecionado() {
                if (jListGerenciarMetodos.getSelectedIndex() != -1) {
                    Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());

                    metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                    metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                    ((DefaultListModel<String>) jListGerenciarMetodos.getModel()).setElementAt(
                        metodoSelecionado.getRepresentacaoUml(), jListGerenciarMetodos.getSelectedIndex()
                    );
                }
            }
        };

        textFieldNomeMetodo.getDocument().addDocumentListener(camposMetodoListener);
        textFieldTipoMetodo.getDocument().addDocumentListener(camposMetodoListener);

        // ----------------------------------------------------------------------------

        JLabel labelVisibilidadeMetodo= new JLabel(gerenciadorDeRecursos.getString("visibilidade"));
        labelVisibilidadeMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelVisibilidadeMetodo.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        comboBoxVisibilidadeMetodo.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        comboBoxVisibilidadeMetodo.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        comboBoxVisibilidadeMetodo.setEnabled(false);
        comboBoxVisibilidadeMetodo.setFocusable(false);
        for (Visibilidade visibilidade : Visibilidade.values()) {
            comboBoxVisibilidadeMetodo.addItem(visibilidade.getNome());
        }
        comboBoxVisibilidadeMetodo.addActionListener(e -> {
            if (jListGerenciarMetodos.getSelectedIndex() != -1) {
                Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());

                metodoSelecionado.setVisibilidade(
                    Visibilidade.getVisibilidadePorNome((String) comboBoxVisibilidadeMetodo.getSelectedItem())
                );

                ((DefaultListModel<String>) jListGerenciarMetodos.getModel()).setElementAt(
                    metodoSelecionado.getRepresentacaoUml(), jListGerenciarMetodos.getSelectedIndex()
                );
            }
        });

        // ----------------------------------------------------------------------------

        checkBoxMetodoAbstrato.setBorder(bordaTextField);
        checkBoxMetodoAbstrato.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        checkBoxMetodoAbstrato.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));
        checkBoxMetodoAbstrato.setFocusable(false);
        checkBoxMetodoAbstrato.setEnabled(false);
        checkBoxMetodoAbstrato.addActionListener(e -> {
            if (jListGerenciarMetodos.getSelectedIndex() != -1) {
                Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());

                metodoSelecionado.setAbstrato(checkBoxMetodoAbstrato.isSelected());

                ((DefaultListModel<String>) jListGerenciarMetodos.getModel()).setElementAt(
                        metodoSelecionado.getRepresentacaoUml(), jListGerenciarMetodos.getSelectedIndex()
                );
            }
        });

        // ----------------------------------------------------------------------------

        checkBoxMetodoEstatico.setBorder(bordaTextField);
        checkBoxMetodoEstatico.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        checkBoxMetodoEstatico.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));
        checkBoxMetodoEstatico.setFocusable(false);
        checkBoxMetodoEstatico.setEnabled(false);
        checkBoxMetodoEstatico.addActionListener(e -> {
            if (jListGerenciarMetodos.getSelectedIndex() != -1) {
                Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());

                metodoSelecionado.setEstatico(checkBoxMetodoEstatico.isSelected());

                ((DefaultListModel<String>) jListGerenciarMetodos.getModel()).setElementAt(
                        metodoSelecionado.getRepresentacaoUml(), jListGerenciarMetodos.getSelectedIndex()
                );
            }
        });

        // ----------------------------------------------------------------------------

        Consumer<Integer> emExcluirMetodo = (Integer indexMetodoExcluido) -> {
            modeloAtual.getMetodos().remove((int) indexMetodoExcluido);
        };

        Consumer<Void> emNovoMetodo = (Void) -> {
            Metodo novoMetodo = new Metodo();
            modeloAtual.getMetodos().add(novoMetodo);
        };

        Consumer<Void> emAcimaMetodo = (Void) -> {
            Collections.swap(modeloAtual.getMetodos(), jListGerenciarMetodos.getSelectedIndex(), jListGerenciarMetodos.getSelectedIndex() - 1);
        };

        Consumer<Void> emAbaixoMetodo = (Void) -> {
            Collections.swap(modeloAtual.getMetodos(), jListGerenciarMetodos.getSelectedIndex(), jListGerenciarMetodos.getSelectedIndex() + 1);
        };

        JPanel painelOpcoesListaMetodos = getPainelOpcoesListaDeElementos(
            jListGerenciarMetodos, (DefaultListModel<String>) jListGerenciarMetodos.getModel(),
            emExcluirMetodo, emNovoMetodo, emAcimaMetodo, emAbaixoMetodo
        );

        // ----------------------------------------------------------------------------

        Consumer<Boolean> habilitarCamposMetodo = (habilitar) -> {
            Color corForegroundCampos = gerenciadorDeRecursos.getColor(habilitar ? "black" : "spanish_gray");

            textFieldNomeMetodo.setEnabled(habilitar);
            textFieldTipoMetodo.setEnabled(habilitar);
            checkBoxMetodoEstatico.setEnabled(habilitar);
            checkBoxMetodoAbstrato.setEnabled(habilitar);
            comboBoxVisibilidadeMetodo.setEnabled(habilitar);

            labelNomeMetodo.setForeground(corForegroundCampos);
            labelTipoMetodo.setForeground(corForegroundCampos);
            labelVisibilidadeMetodo.setForeground(corForegroundCampos);
            checkBoxMetodoEstatico.setForeground(corForegroundCampos);
            checkBoxMetodoAbstrato.setForeground(corForegroundCampos);

            if (!habilitar) {
                textFieldNomeMetodo.setText("");
                textFieldTipoMetodo.setText("");
                checkBoxMetodoEstatico.setSelected(false);
                comboBoxVisibilidadeMetodo.setSelectedIndex(0);
                checkBoxMetodoAbstrato.setSelected(false);
            }
        };

        // ----------------------------------------------------------------------------

        JPanel painelGerenciarMetodos = new JPanel(new MigLayout("insets 30 30 15 30", "[grow, fill, 50%][grow, fill, 50%]"));
        painelGerenciarMetodos.setBackground(gerenciadorDeRecursos.getColor("white"));

        painelGerenciarMetodos.add(scrollPaneMetodos, "growy, growx, span 2, split 2, gapright 0");
        painelGerenciarMetodos.add(painelOpcoesListaMetodos, "growy, wrap, gapleft 0");

        painelGerenciarMetodos.add(labelNomeMetodo,"gap 0 8 10 8, split 2, growy");
        painelGerenciarMetodos.add(textFieldNomeMetodo, "grow, gaptop 10, gapbottom 8, gapright 10");

        painelGerenciarMetodos.add(labelTipoMetodo, "gap 0 8 10 8, split 2, growy");
        painelGerenciarMetodos.add(textFieldTipoMetodo, "wrap, grow, gapbottom 8, gaptop 10");

        painelGerenciarMetodos.add(labelVisibilidadeMetodo, "gap 0 8 0 8, split 2, growy");
        painelGerenciarMetodos.add(comboBoxVisibilidadeMetodo, "grow, gapbottom 8");

        painelGerenciarMetodos.add(checkBoxMetodoEstatico, "split 2, gapbottom 8, gapleft:push, gapright:push");
        painelGerenciarMetodos.add(checkBoxMetodoAbstrato, "split 2, gapbottom 8, gapleft:push, gapright:push, wrap");

        // ============================================================================

        JList<String> jListGerenciarParametros = new JList<>();
        jListGerenciarParametros.setModel(new DefaultListModel<>());
        jListGerenciarParametros.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        jListGerenciarParametros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListGerenciarParametros.setFocusable(false);

        JScrollPane scrollPaneParametros = new JScrollPane(jListGerenciarParametros);
        scrollPaneParametros.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("silver_sand"))
        );

        JTextField textFieldNomeParametro = new JTextField();
        JTextField textFieldTipoParametro = new JTextField();
        JTextField textFieldValorPadraoParametro = new JTextField();

        // ----------------------------------------------------------------------------

        JLabel labelParametros = new JLabel(gerenciadorDeRecursos.getString("parametros"));
        labelParametros.setFont(gerenciadorDeRecursos.getRobotoBlack(15));

        JLabel labelSeparadorParametros = new JLabel();
        labelSeparadorParametros.setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("taupe_gray"))
        );

        // ----------------------------------------------------------------------------

        JLabel labelNomeParametro = new JLabel(gerenciadorDeRecursos.getString("nome"));
        labelNomeParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelNomeParametro.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldNomeParametro.setBorder(bordaTextField);
        textFieldNomeParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldNomeParametro.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldNomeParametro.setEnabled(false);

        // ----------------------------------------------------------------------------

        JLabel labelTipoParametro = new JLabel(gerenciadorDeRecursos.getString("tipo"));
        labelTipoParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelTipoParametro.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldTipoParametro.setBorder(bordaTextField);
        textFieldTipoParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldTipoParametro.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldTipoParametro.setEnabled(false);

        // ----------------------------------------------------------------------------

        JLabel labelValorPadraoParametro = new JLabel(gerenciadorDeRecursos.getString("valor_padrao"));
        labelValorPadraoParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelValorPadraoParametro.setForeground(gerenciadorDeRecursos.getColor("spanish_gray"));

        textFieldValorPadraoParametro.setBorder(bordaTextField);
        textFieldValorPadraoParametro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        textFieldValorPadraoParametro.setForeground(gerenciadorDeRecursos.getColor("outer_space_gray"));
        textFieldValorPadraoParametro.setEnabled(false);

        // ----------------------------------------------------------------------------

        DocumentListener camposParametroListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarParametroSelecionado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarParametroSelecionado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarParametroSelecionado();
            }

            private void atualizarParametroSelecionado() {
                if (jListGerenciarParametros.getSelectedIndex() != -1) {
                    Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());
                    Parametro parametroSelecionado = metodoSelecionado.getParametros().get(jListGerenciarParametros.getSelectedIndex());

                    parametroSelecionado.setNome(textFieldNomeParametro.getText());
                    parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                    parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                    ((DefaultListModel<String>) jListGerenciarParametros.getModel()).setElementAt(
                        parametroSelecionado.getRepresentacaoUml(), jListGerenciarParametros.getSelectedIndex()
                    );

                    ((DefaultListModel<String>) jListGerenciarMetodos.getModel()).setElementAt(
                        metodoSelecionado.getRepresentacaoUml(), jListGerenciarMetodos.getSelectedIndex()
                    );
                }
            }
        };

        textFieldNomeParametro.getDocument().addDocumentListener(camposParametroListener);
        textFieldTipoParametro.getDocument().addDocumentListener(camposParametroListener);
        textFieldValorPadraoParametro.getDocument().addDocumentListener(camposParametroListener);

        // ----------------------------------------------------------------------------

        Consumer<Integer> emExcluirParametro = (Integer indexParametroExcluido) -> {
            Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());
            metodoSelecionado.getParametros().remove((int) indexParametroExcluido);
        };

        Consumer<Void> emNovoParametro = (Void) -> {
            Parametro novoParametro = new Parametro();
            modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex()).getParametros().add(novoParametro);

            DefaultListModel<String> model = (DefaultListModel<String>) jListGerenciarParametros.getModel();
            model.setElementAt(":", model.size() - 1);
        };

        Consumer<Void> emAcimaParametro = (Void) -> {
            int indexMetodoSelecionado = jListGerenciarMetodos.getSelectedIndex();
            int indexParametroSelecionado = jListGerenciarParametros.getSelectedIndex();

            Collections.swap(
                modeloAtual.getMetodos().get(indexMetodoSelecionado).getParametros(),
                indexParametroSelecionado, indexParametroSelecionado - 1
            );
        };

        Consumer<Void> emAbaixoParametro = (Void) -> {
            int indexMetodoSelecionado = jListGerenciarMetodos.getSelectedIndex();
            int indexParametroSelecionado = jListGerenciarParametros.getSelectedIndex();

            Collections.swap(
                modeloAtual.getMetodos().get(indexMetodoSelecionado).getParametros(),
                indexParametroSelecionado, indexParametroSelecionado + 1
            );
        };

        JPanel painelOpcoesListaParametros = getPainelOpcoesListaDeElementos(
                jListGerenciarParametros, (DefaultListModel<String>) jListGerenciarParametros.getModel(),
                emExcluirParametro, emNovoParametro, emAcimaParametro, emAbaixoParametro
        );

        JButton botaoNovoParametro = (JButton) painelOpcoesListaParametros.getComponent(0);
        botaoNovoParametro.setEnabled(false);

        // ----------------------------------------------------------------------------

        Consumer<Boolean> habilitarCamposParametro = (habilitar) -> {
            Color corForegroundCampos = gerenciadorDeRecursos.getColor(habilitar ? "black" : "spanish_gray");

            textFieldNomeParametro.setEnabled(habilitar);
            textFieldTipoParametro.setEnabled(habilitar);
            textFieldValorPadraoParametro.setEnabled(habilitar);

            labelNomeParametro.setForeground(corForegroundCampos);
            labelTipoParametro.setForeground(corForegroundCampos);
            labelValorPadraoParametro.setForeground(corForegroundCampos);

            if (!habilitar) {
                textFieldNomeParametro.setText("");
                textFieldTipoParametro.setText("");
                textFieldValorPadraoParametro.setText("");
            }
        };

        jListGerenciarParametros.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jListGerenciarMetodos.getSelectedIndex() != -1) {
                int indexSelecionado = jListGerenciarParametros.getSelectedIndex();

                Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());
                List<Parametro> parametros = metodoSelecionado.getParametros();

                if (indexSelecionado != -1) {
                    habilitarCamposParametro.accept(true);

                    Parametro parametroSelecionado = parametros.get(jListGerenciarParametros.getSelectedIndex());

                    // Removendo os listener para impedir que o parametro seja modificado de forma incorreta durante
                    // os 'setText' abaixo
                    textFieldNomeParametro.getDocument().removeDocumentListener(camposParametroListener);
                    textFieldTipoParametro.getDocument().removeDocumentListener(camposParametroListener);
                    textFieldValorPadraoParametro.getDocument().removeDocumentListener(camposParametroListener);

                    textFieldNomeParametro.setText(parametroSelecionado.getNome());
                    textFieldTipoParametro.setText(parametroSelecionado.getTipo());
                    textFieldValorPadraoParametro.setText(parametroSelecionado.getValorPadrao());

                    textFieldNomeParametro.getDocument().addDocumentListener(camposParametroListener);
                    textFieldTipoParametro.getDocument().addDocumentListener(camposParametroListener);
                    textFieldValorPadraoParametro.getDocument().addDocumentListener(camposParametroListener);
                } else {
                    habilitarCamposParametro.accept(false);
                }

                JButton botaoExcluir = (JButton) painelOpcoesListaParametros.getComponent(1);
                JButton botaoAcima = (JButton) painelOpcoesListaParametros.getComponent(2);
                JButton botaoAbaixo = (JButton) painelOpcoesListaParametros.getComponent(3);

                botaoExcluir.setEnabled(indexSelecionado != -1);
                botaoAcima.setEnabled(indexSelecionado > 0);
                botaoAbaixo.setEnabled(indexSelecionado < parametros.size() - 1 && indexSelecionado >= 0);
            }
        });

        // ----------------------------------------------------------------------------

        jListGerenciarMetodos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int indexSelecionado = jListGerenciarMetodos.getSelectedIndex();

                if (indexSelecionado != -1) {
                    habilitarCamposMetodo.accept(true);

                    Metodo metodoSelecionado = modeloAtual.getMetodos().get(jListGerenciarMetodos.getSelectedIndex());

                    // Removendo os listener para impedir que o metodo seja modificado de forma incorreta durante
                    // os 'setText' abaixo
                    textFieldNomeMetodo.getDocument().removeDocumentListener(camposMetodoListener);
                    textFieldTipoMetodo.getDocument().removeDocumentListener(camposMetodoListener);

                    textFieldNomeMetodo.setText(metodoSelecionado.getNome());
                    textFieldTipoMetodo.setText(metodoSelecionado.getTipo());

                    textFieldNomeMetodo.getDocument().addDocumentListener(camposMetodoListener);
                    textFieldTipoMetodo.getDocument().addDocumentListener(camposMetodoListener);

                    // Nao é o ideal, mas desabilitar os componentes impede que o actionListener seja
                    // chamado, o que previne que o metodo selecionado seja modificado de forma incorreta
                    checkBoxMetodoAbstrato.setEnabled(false);
                    checkBoxMetodoEstatico.setEnabled(false);
                    comboBoxVisibilidadeMetodo.setEnabled(false);
                    checkBoxMetodoAbstrato.setSelected(metodoSelecionado.ehAbstrato());
                    checkBoxMetodoEstatico.setSelected(metodoSelecionado.ehEstatico());
                    comboBoxVisibilidadeMetodo.setSelectedItem(metodoSelecionado.getVisibilidade().getNome());
                    comboBoxVisibilidadeMetodo.setEnabled(true);
                    checkBoxMetodoEstatico.setEnabled(true);
                    checkBoxMetodoAbstrato.setEnabled(true);

                    DefaultListModel<String> modelParametros = (DefaultListModel<String>) jListGerenciarParametros.getModel();
                    modelParametros.removeAllElements();

                    for (Parametro parametro : metodoSelecionado.getParametros()) {
                        modelParametros.addElement(parametro.getRepresentacaoUml());
                    }
                } else {
                    habilitarCamposMetodo.accept(false);
                }

                JButton botaoExcluir = (JButton) painelOpcoesListaMetodos.getComponent(1);
                JButton botaoAcima = (JButton) painelOpcoesListaMetodos.getComponent(2);
                JButton botaoAbaixo = (JButton) painelOpcoesListaMetodos.getComponent(3);

                botaoExcluir.setEnabled(indexSelecionado != -1);
                botaoAcima.setEnabled(indexSelecionado > 0);
                botaoAbaixo.setEnabled(indexSelecionado < modeloAtual.getMetodos().size() - 1 && indexSelecionado >= 0);
            }

            botaoNovoParametro.setEnabled(jListGerenciarMetodos.getSelectedIndex() != -1);
        });

        // ----------------------------------------------------------------------------

        // Quando o frame gerenciar for aberto os text fields e campos devem ser atualizados de acordo com o modelo
        // do componente. Isso deve ser feito por conta da possibilidade do modelo ter sido modificado em algum momento
        getFrameGerenciarComponente().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                DefaultListModel<String> metodosModel = (DefaultListModel<String>) jListGerenciarMetodos.getModel();
                DefaultListModel<String> parametrosModel = (DefaultListModel<String>) jListGerenciarParametros.getModel();

                parametrosModel.clear();
                metodosModel.clear();

                for (Metodo metodo : modeloAtual.getMetodos()) {
                    metodosModel.addElement(metodo.getRepresentacaoUml());
                }
            }
        });

        // ----------------------------------------------------------------------------

        painelGerenciarMetodos.add(labelParametros, "span 2, split 2, growx 0, gapright 7, gapbottom 8");
        painelGerenciarMetodos.add(labelSeparadorParametros, "wrap, gapbottom 8");

        painelGerenciarMetodos.add(scrollPaneParametros, "growy, growx, split 2, spany 3, gapright 0");
        painelGerenciarMetodos.add(painelOpcoesListaParametros, "growy, gapleft 0");

        JPanel painelDadosParametro = new JPanel(new MigLayout("insets 0 0 0 0", "[grow]", "[grow][grow][grow]"));
        painelDadosParametro.setOpaque(false);

        painelDadosParametro.add(labelNomeParametro, "gap 0 8 8 8, growy, split 2");
        painelDadosParametro.add(textFieldNomeParametro, "grow, wrap, gapbottom 8, gaptop 8");

        painelDadosParametro.add(labelTipoParametro, "gap 0 8 8 8, split 2, growy");
        painelDadosParametro.add(textFieldTipoParametro, "grow, wrap, gapbottom 8, gaptop 8");

        painelDadosParametro.add(labelValorPadraoParametro, "gap 0 8 8 8, split 2, growy");
        painelDadosParametro.add(textFieldValorPadraoParametro, "grow, gapbottom 8, gaptop 8");

        painelGerenciarMetodos.add(painelDadosParametro, "gapleft 8");

        return painelGerenciarMetodos;
    }

    /**
     * Uma função utilitária que retorna um painel com opções comuns à JLists de Atributos, Métodos e Parâmetros.
     * As opções do painel incluem: "Novo", "Acima", "Abaixo" e "Excluir"
     * @param emExcluirElemento Função que recebe o index do elemento e lida com a lógica de exclusão.
     */
    private JPanel getPainelOpcoesListaDeElementos(
        JList<String> jListElementos, DefaultListModel<String> listModelElementos, Consumer<Integer> emExcluirElemento,
        Consumer<Void> emNovoElemento, Consumer<Void> emAcima, Consumer<Void> emAbaixo
    ) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JPanel painelOpcoesListaElementos = new JPanel(new MigLayout("insets 0 0 0 0, flowy"));
        painelOpcoesListaElementos.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("silver_sand"))
        );
        painelOpcoesListaElementos.setOpaque(false);

        // ----------------------------------------------------------------------------

        JButton botaoAcima = new JButton();
        botaoAcima.setIcon(gerenciadorDeRecursos.getImagem("icone_acima"));
        botaoAcima.setFocusable(false);
        botaoAcima.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand")),
            new EmptyBorder(5, 5, 5, 5)
        ));
        botaoAcima.setEnabled(false);
        botaoAcima.addActionListener(e -> {
            int indexElementoSelecionado = jListElementos.getSelectedIndex();
            String elementoSelecionado = listModelElementos.get(indexElementoSelecionado);
            String elementoAcima = listModelElementos.get(indexElementoSelecionado - 1);

            listModelElementos.set(indexElementoSelecionado - 1, elementoSelecionado);
            listModelElementos.set(indexElementoSelecionado, elementoAcima);

            if (emAcima != null) {
                emAcima.accept(null);
            }

            jListElementos.clearSelection();
        });

        // ----------------------------------------------------------------------------

        JButton botaoAbaixo = new JButton();
        botaoAbaixo.setIcon(gerenciadorDeRecursos.getImagem("icone_abaixo"));
        botaoAbaixo.setFocusable(false);
        botaoAbaixo.setBorder(new EmptyBorder(5, 5, 5, 5));
        botaoAbaixo.setEnabled(false);
        botaoAbaixo.addActionListener(e -> {
            int indexElementoSelecionado = jListElementos.getSelectedIndex();
            String elementoSelecionado = listModelElementos.get(indexElementoSelecionado);
            String elementoAbaixo = listModelElementos.get(indexElementoSelecionado + 1);

            listModelElementos.set(indexElementoSelecionado + 1, elementoSelecionado);
            listModelElementos.set(indexElementoSelecionado, elementoAbaixo);

            if (emAbaixo != null) {
                emAbaixo.accept(null);
            }

            jListElementos.clearSelection();
        });

        // ----------------------------------------------------------------------------

        JButton botaoExcluir = new JButton();
        botaoExcluir.setIcon(gerenciadorDeRecursos.getImagem("icone_deletar"));
        botaoExcluir.setFocusable(false);
        botaoExcluir.setEnabled(false);
        botaoExcluir.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand")),
            new EmptyBorder(5, 5, 5, 5)
        ));
        botaoExcluir.addActionListener(e -> {
            int indexSelecionado = jListElementos.getSelectedIndex();

            if (emExcluirElemento != null) {
                emExcluirElemento.accept(indexSelecionado);
            }

            jListElementos.setSelectedIndex(indexSelecionado - 1);

            listModelElementos.remove(indexSelecionado);

        });

        // ----------------------------------------------------------------------------

        JButton botaoNovo = new JButton();
        botaoNovo.setIcon(gerenciadorDeRecursos.getImagem("icone_sinal_mais_25"));
        botaoNovo.setFocusable(false);
        botaoNovo.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("silver_sand")),
            new EmptyBorder(5, 5, 5, 5)
        ));
        botaoNovo.addActionListener(e -> {
            listModelElementos.add(listModelElementos.getSize(), Visibilidade.PUBLICO.getSimbolo());

            if (emNovoElemento != null) {
                emNovoElemento.accept(null);
            }

            jListElementos.setSelectedIndex(listModelElementos.getSize() - 1);
        });

        // ----------------------------------------------------------------------------

        painelOpcoesListaElementos.add(botaoNovo, "split 4, align center, gapbottom 0");
        painelOpcoesListaElementos.add(botaoExcluir, "align center, gapbottom 0");
        painelOpcoesListaElementos.add(botaoAcima, "align center, gapbottom 0");
        painelOpcoesListaElementos.add(botaoAbaixo, "align center, gapbottom 0");

        return painelOpcoesListaElementos;
    }

    private JDialog getDialogErroNomeClasse() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JDialog dialogErroNomeClasse = new JDialog();
        dialogErroNomeClasse.setTitle(gerenciadorDeRecursos.getString("erro_maiusculo"));
        dialogErroNomeClasse.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogErroNomeClasse.setResizable(false);

        // ----------------------------------------------------------------------------

        JLabel labelImgErro = new JLabel(gerenciadorDeRecursos.getImagem("icone_erro"));

        JPanel painelImgErro = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgErro.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));
        painelImgErro.add(labelImgErro, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelErro = new JLabel(gerenciadorDeRecursos.getString("erro_nome_classe"));
        labelErro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        painelMensagem.setBorder(new CompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        painelMensagem.add(labelErro, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelRespostaOK = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaOK.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaOK.setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black"))
        );

        JLabel labelRespostaOK = new JLabel(gerenciadorDeRecursos.getString("ok_maiusculo"));
        labelRespostaOK.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelRespostaOK.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaOK.add(labelRespostaOK, "align center");
        painelRespostaOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialogErroNomeClasse.setVisible(false);
            }

            // O componente de index 0 é o labelRespostaOK
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
        });


        // ----------------------------------------------------------------------------

        JPanel painelErroNomeClasse = new JPanel(new MigLayout("insets 5 0 10 0"));
        painelErroNomeClasse.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelErroNomeClasse.add(painelImgErro, "west");
        painelErroNomeClasse.add(painelMensagem, "wrap");
        painelErroNomeClasse.add(painelRespostaOK, "gaptop 10, align right, gapright 20");

        dialogErroNomeClasse.setContentPane(painelErroNomeClasse);
        dialogErroNomeClasse.pack();

        return dialogErroNomeClasse;
    }

    @Override
    public String toString() {
        StringBuilder informacoesClasse = new StringBuilder();

        informacoesClasse.append("CLASSE_UML\n// Posicao X e Y\n");
        informacoesClasse.append(super.getPainelComponente().getX()).append("\n");
        informacoesClasse.append(super.getPainelComponente().getY());
        informacoesClasse.append("\n// Nome\n");
        informacoesClasse.append(modeloAtual.getNome());
        informacoesClasse.append("\n// Comentario\n");
        informacoesClasse.append(modeloAtual.getComentario().replaceAll("\n", "(novaLinha)"));
        informacoesClasse.append("\n// Numero de caracteres para quebra de linha\n");
        informacoesClasse.append(modeloAtual.getNumCharsQuebrarComentario());
        informacoesClasse.append("\n// Eh abstrata?\n");
        informacoesClasse.append(modeloAtual.ehAbstrata());
        informacoesClasse.append("\n// Eh Interface?\n");
        informacoesClasse.append(modeloAtual.ehInterface());

        informacoesClasse.append("\n// Numero de atributos\n");
        informacoesClasse.append(modeloAtual.getAtributos().size());

        for (Atributo atributo : modeloAtual.getAtributos()) {
            informacoesClasse.append("\n// Atributo - Nome\n");
            informacoesClasse.append(atributo.getNome());
            informacoesClasse.append("\n// Atributo - Visibilidade\n");
            informacoesClasse.append(atributo.getVisibilidade().getNome());
            informacoesClasse.append("\n// Atributo - Tipo\n");
            informacoesClasse.append(atributo.getTipo());
            informacoesClasse.append("\n// Atributo - Valor Padrao\n");
            informacoesClasse.append(atributo.getValorPadrao());
            informacoesClasse.append("\n// Atributo - Eh Estatico?\n");
            informacoesClasse.append(atributo.ehEstatico());
        }

        informacoesClasse.append("\n// Numero de metodos\n");
        informacoesClasse.append(modeloAtual.getMetodos().size());
        informacoesClasse.append("\n");

        for (Metodo metodo : modeloAtual.getMetodos()) {
            informacoesClasse.append("// Metodo - Nome\n");
            informacoesClasse.append(metodo.getNome());
            informacoesClasse.append("\n// Metodo - Visibilidade\n");
            informacoesClasse.append(metodo.getVisibilidade().getNome());
            informacoesClasse.append("\n// Metodo - Tipo\n");
            informacoesClasse.append(metodo.getTipo());
            informacoesClasse.append("\n// Metodo - Eh Abstrato?\n");
            informacoesClasse.append(metodo.ehAbstrato());
            informacoesClasse.append("\n// Metodo - Eh Estatico?\n");
            informacoesClasse.append(metodo.ehEstatico());

            informacoesClasse.append("\n// Numero de parametros\n");
            informacoesClasse.append(metodo.getParametros().size());
            informacoesClasse.append("\n");

            for (Parametro parametro : metodo.getParametros()) {
                informacoesClasse.append("// Parametro - Nome\n");
                informacoesClasse.append(parametro.getNome());
                informacoesClasse.append("\n// Parametro - Tipo\n");
                informacoesClasse.append(parametro.getTipo());
                informacoesClasse.append("\n// Parametro - Valor Padrao\n");
                informacoesClasse.append(parametro.getValorPadrao());
                informacoesClasse.append("\n");
            }
        }

        return informacoesClasse.toString();
    }
}
