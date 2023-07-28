package ComponentesUML;

import AlteracoesDeElementos.ComponenteModificado;
import ClassesAuxiliares.RobotoFont;
import DiagramaUML.DiagramaUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ClasseUML extends ComponenteUML {

    private String nomeClasse = "Nova Classe";
    private String comentarioClasse = "";
    private final JPanel painelNomeClasse;
    private final JList<String> jListAtributos;
    private final JList<String> jListMetodos;

    private boolean abstrata;
    private boolean ehInterface;
    private int limiteComentario = 20;

    private ArrayList<Atributo> arrayListAtributos = new ArrayList<>();
    private ArrayList<Metodo> arrayListMetodos = new ArrayList<>();
    private HashMap<String, Object> informacoesAntesDaModificacao;

    public ClasseUML(DiagramaUML diagramaUML) {
        super(diagramaUML);

        RobotoFont robotoFont = new RobotoFont();

        Border linha = BorderFactory.createMatteBorder(2, 2, 0, 2, Color.DARK_GRAY);
        Border margem = new EmptyBorder(5, 13, 5, 13);
        CompoundBorder borda = new CompoundBorder(linha, margem);

        painelNomeClasse = new JPanel(new MigLayout("fill"));
        painelNomeClasse.setBackground(new Color(0x323232));
        painelNomeClasse.setBorder(borda);

        JLabel labelNomeClasse = new JLabel("Nova Classe", JLabel.CENTER);
        labelNomeClasse.setFont(robotoFont.getRobotoBlack(15));
        labelNomeClasse.setOpaque(false);
        labelNomeClasse.setForeground(Color.white);

        painelNomeClasse.add(labelNomeClasse, "north");


        DefaultListModel<String> listModelAtributos = new DefaultListModel<>();
        listModelAtributos.add(0, "");

        jListAtributos = new JList<>();
        jListAtributos.setModel(listModelAtributos);
        jListAtributos.setFont(robotoFont.getRobotoMedium(14f));
        jListAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        margem = new EmptyBorder(5, 3, 5, 3);
        borda = new CompoundBorder(linha, margem);
        jListAtributos.setBorder(borda);

        DefaultListModel<String> listModelMetodos = new DefaultListModel<>();
        listModelMetodos.add(0, "");

        jListMetodos = new JList<>();
        jListMetodos.setModel(listModelMetodos);
        jListMetodos.setFont(robotoFont.getRobotoMedium(14f));
        jListMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        linha = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY);
        borda = new CompoundBorder(linha, margem);
        jListMetodos.setBorder(borda);

        // =======================================================================================================

        super.setLargura(painelNomeClasse.getPreferredSize().width);

        super.setAltura(painelNomeClasse.getPreferredSize().height + jListAtributos.getPreferredSize().height +
                        jListMetodos.getPreferredSize().height);

        super.getPainelComponente().add(super.getGlassPaneComponente());
        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());


        super.getPainelComponente().add(painelNomeClasse);
        painelNomeClasse.setBounds(0, 0, super.getLargura(), painelNomeClasse.getPreferredSize().height);

        super.getPainelComponente().add(jListAtributos);
        jListAtributos.setBounds(0, painelNomeClasse.getBounds().height, super.getLargura(),
                                    jListAtributos.getPreferredSize().height);

        super.getPainelComponente().add(jListMetodos);
        jListMetodos.setBounds(0, (jListAtributos.getBounds().height + jListAtributos.getBounds().y),
                                    super.getLargura(),
                                    jListMetodos.getPreferredSize().height);

        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();



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
        });

    }


    public void setArrayListAtributos(ArrayList<Atributo> novaArrayListAtributos) {
        this.arrayListAtributos = novaArrayListAtributos;
    }

    public void setArrayListMetodos(ArrayList<Metodo> novaArrayListMetodos) {
        this.arrayListMetodos = novaArrayListMetodos;
    }

    public void atualizarComponente(String novoNome, String novoComentario, boolean ehAbstrata,
                                    Object[] listaAtributos, Object[] listaMetodos, int limiteComentario,
                                    boolean ehInterface) {



        RobotoFont robotoFont = new RobotoFont();

        int LARGURA_PAINEL_OPCOES = 40;

        try {
            super.getPainelComponente().remove(getPainelComponente().getComponent(4));
            getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                                          getPainelComponente().getHeight());
        } catch (Exception e) {}


        // ===========================================================================================================

        JLabel labelNomeClasse = (JLabel) painelNomeClasse.getComponent(0);

        this.nomeClasse = novoNome;
        labelNomeClasse.setText(nomeClasse);

        this.abstrata = ehAbstrata;

        if (ehAbstrata) {
            labelNomeClasse.setFont(robotoFont.getRobotoBlack(15).deriveFont(Font.ITALIC));
            painelNomeClasse.setBackground(new Color(0x575757));
        } else {
            labelNomeClasse.setFont(robotoFont.getRobotoBlack(15));
            painelNomeClasse.setBackground(new Color(0x323232));
        }

        this.ehInterface = ehInterface;


        painelNomeClasse.removeAll();

        if (ehInterface) {
            JLabel labelInterface = new JLabel("<< interface >>", JLabel.CENTER);
            labelInterface.setFont(robotoFont.getRobotoMedium(14));
            labelInterface.setForeground(Color.white);

            painelNomeClasse.add(labelInterface, "north, gapbottom 5");
            painelNomeClasse.add(labelNomeClasse, "north");

        } else {
            painelNomeClasse.add(labelNomeClasse, "north");
        }


        this.limiteComentario = limiteComentario;

        if (!novoComentario.equals("")) {
            ArrayList<String> arrayLinhasComentario = new ArrayList<>();

            int numCaracteres = 0;
            boolean fazerQuebraDeLinha = false;
            int inicio = 0;

            for (int i = 0; i < novoComentario.length() ; i++) {

                if ((fazerQuebraDeLinha && novoComentario.charAt(i) == ' ') || novoComentario.charAt(i) == '\n') {
                    arrayLinhasComentario.add(novoComentario.substring(inicio, i));
                    inicio = i + 1;
                    fazerQuebraDeLinha = false;
                    numCaracteres = 0;
                }


                if (numCaracteres == limiteComentario) {
                    fazerQuebraDeLinha = true;
                }

                numCaracteres++;
            }

            if (numCaracteres != 0) {
                arrayLinhasComentario.add(novoComentario.substring(inicio));
            }

            boolean primeiraLinha = true;
            for (String linhaComentario : arrayLinhasComentario) {
                JLabel labelComentario = new JLabel(linhaComentario, JLabel.CENTER);
                labelComentario.setOpaque(false);
                labelComentario.setFont(robotoFont.getRobotoMedium(13).deriveFont(Font.ITALIC));
                labelComentario.setForeground(Color.white);

                if (primeiraLinha) {
                    painelNomeClasse.add(labelComentario, "wrap, grow, gaptop 7, gapbottom 0");
                    primeiraLinha = false;
                } else {
                    painelNomeClasse.add(labelComentario, "wrap, grow, gaptop 0, gapbottom 0");
                }
            }

        }

        comentarioClasse = novoComentario;


        DefaultListModel<String> listModelAtributos = (DefaultListModel<String>) jListAtributos.getModel();
        listModelAtributos.clear();

        if (listaAtributos.length != 0 && !listaAtributos[0].equals("")) {
            for (int i = 0; i < listaAtributos.length; i++) {
                if (ehInterface && !((String) listaAtributos[i]).contains("<html>")) {
                    listModelAtributos.add(i, ((String) listaAtributos[i]).substring(1));

                } else if ((ehInterface && ((String) listaAtributos[i]).contains("<html>"))) {
                    listModelAtributos.add(i, ((String) listaAtributos[i]).substring(0, 9) +
                            ((String) listaAtributos[i]).substring(10));
                } else {
                    listModelAtributos.add(i, (String) listaAtributos[i]);
                }
            }
        } else {
            listModelAtributos.add(0, "");
        }



        DefaultListModel<String> listModelMetodos = (DefaultListModel<String>) jListMetodos.getModel();
        listModelMetodos.clear();

        if (listaMetodos.length != 0 && !listaMetodos[0].equals("")) {
            for (int i = 0; i < listaMetodos.length; i++) {
                Metodo metodo = arrayListMetodos.get(i);

                StringBuilder novoMetodo = new StringBuilder();

                novoMetodo.append("<html>");
                novoMetodo.append(metodo.ehAbstrato() ? "<i>" : "");
                novoMetodo.append(metodo.ehEstatico() ? "<u>" : "");
                novoMetodo.append(ehInterface ? "" : metodo.getVisibilidade());
                novoMetodo.append(metodo.getNome());
                novoMetodo.append("(");

                listModelMetodos.add(i, "");

                String[] listaParametros = arrayListMetodos.get(i).toStringParametros().split(", ");

                for (int j = 0; j < listaParametros.length; j++) {
                    novoMetodo.append(listaParametros[j]);

                    if (j != listaParametros.length - 1) {
                        novoMetodo.append(",");
                    }

                    listModelMetodos.set(i, novoMetodo.toString());

                    if ((jListMetodos.getPreferredSize().width > Math.max(jListAtributos.getPreferredSize().width,
                            painelNomeClasse.getPreferredSize().width)) &&
                            j != listaParametros.length - 1) {

                        novoMetodo.append("<br>");
                        novoMetodo.append("<font color='white'>");

                        novoMetodo.append(ehInterface ? "" : metodo.getVisibilidade());
                        novoMetodo.append(metodo.getNome());
                        novoMetodo.append("(");

                        novoMetodo.append("</font>");
                    } else {
                        novoMetodo.append(" ");
                    }
                }

                novoMetodo.append(")");
                novoMetodo.append(metodo.getTipo().equals("") ? "" : ": " + metodo.getTipo());
                novoMetodo.append(metodo.ehAbstrato() ? "</i>" : "");
                novoMetodo.append(metodo.ehEstatico() ? "</u>" : "");
                novoMetodo.append("<html>");
                listModelMetodos.set(i, novoMetodo.toString());

            }
        } else {
            listModelMetodos.add(0, "");
        }


        // ==========================================================================================================


        int LARGURA_MINIMA = 120;

        int novaLargura = Math.max(jListMetodos.getPreferredSize().width,
                          Math.max(painelNomeClasse.getPreferredSize().width, jListAtributos.getPreferredSize().width));

        if (painelNomeClasse.getComponents().length > 1) {
            super.setLargura(Math.max(novaLargura, LARGURA_MINIMA));
        } else {
            super.setLargura(novaLargura);
        }

        painelNomeClasse.setBounds(0, 0, super.getLargura(), painelNomeClasse.getPreferredSize().height);

        jListAtributos.setBounds(0, painelNomeClasse.getBounds().height,
                                    super.getLargura(),
                                    jListAtributos.getPreferredSize().height);

        jListMetodos.setBounds(0, (jListAtributos.getBounds().height +
                                    jListAtributos.getBounds().y),
                                    super.getLargura(),
                                    jListMetodos.getPreferredSize().height);

        super.setAltura(painelNomeClasse.getBounds().height + jListAtributos.getBounds().height +
                                    jListMetodos.getBounds().height);

        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());

        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();

        super.getPainelComponente().revalidate();
        super.getPainelComponente().repaint();
    }

    @Override
    void initFrameGerenciarComponente() {
        RobotoFont robotoFont = new RobotoFont();

        JPanel painelGerenciarComponente = new JPanel(new MigLayout("insets 0 0 20 0, fill", "","[grow, fill][grow, fill][]"));
        painelGerenciarComponente.setBackground(Color.white);

        // ==========================================================================================================

        JPanel painelGerenciarClasse = new JPanel(new MigLayout("insets 30 30 15 30", "[grow]"));
        painelGerenciarClasse.setBackground(Color.white);

        Border linha = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0x979797));
        Border margem = new EmptyBorder(8, 8, 8, 8);
        CompoundBorder borda = new CompoundBorder(linha, margem);


        JLabel labelNomeClasse = new JLabel("Nome da Classe");
        labelNomeClasse.setFont(robotoFont.getRobotoMedium(15f));
        //labelNomeClasse.setForeground(new Color(0x464958));

        JTextField textFieldNomeClasse = new JTextField("Nova Classe");
        textFieldNomeClasse.setBorder(BorderFactory.createCompoundBorder(
                textFieldNomeClasse.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textFieldNomeClasse.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomeClasse.setForeground(new Color(0x484848));

        borda = BorderFactory.createCompoundBorder(
                textFieldNomeClasse.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JLabel labelComentarioClasse = new JLabel("Comentários");
        labelComentarioClasse.setFont(robotoFont.getRobotoMedium(15f));

        JTextArea textAreaComentarioClasse = new JTextArea(8, 7);
        textAreaComentarioClasse.setFont(robotoFont.getRobotoMedium(14f));
        textAreaComentarioClasse.setLineWrap(true);
        textAreaComentarioClasse.setForeground(new Color(0x484848));

        JScrollPane scrollPaneComentarioClasse = new JScrollPane(textAreaComentarioClasse);
        scrollPaneComentarioClasse.setBorder(borda);

        JLabel labelQuebrarComentario = new JLabel("Quebrar comentário após este número de caracteres:");
        labelQuebrarComentario.setFont(robotoFont.getRobotoMedium(13f));
        labelQuebrarComentario.setForeground(new Color(0x454545));

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(20, 20, 200, 1);
        JSpinner spinnerComentario = new JSpinner(spinnerNumberModel);
        spinnerComentario.setFont(robotoFont.getRobotoBlack(13f));
        spinnerComentario.setFocusable(false);
        spinnerComentario.setForeground(new Color(0x383838));
        ((JSpinner.DefaultEditor) spinnerComentario.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spinnerComentario.getEditor()).getTextField().setFocusable(false);


        JLabel labelSeparadorClasse = new JLabel();
        labelSeparadorClasse.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xf8f8f8)),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2))));


        JCheckBox checkBoxAbstrata = new JCheckBox("Abstrata");
        checkBoxAbstrata.setBorder(borda);
        checkBoxAbstrata.setFont(robotoFont.getRobotoMedium(15f));
        checkBoxAbstrata.setForeground(Color.black);
        checkBoxAbstrata.setFocusable(false);


        JCheckBox checkBoxInterface = new JCheckBox("Interface");
        checkBoxInterface.setBorder(borda);
        checkBoxInterface.setFont(robotoFont.getRobotoMedium(15f));
        checkBoxInterface.setForeground(Color.black);
        checkBoxInterface.setFocusable(false);

        checkBoxAbstrata.addActionListener(e -> checkBoxInterface.setSelected(false));

        checkBoxInterface.addActionListener(e -> checkBoxAbstrata.setSelected(false));

        painelGerenciarClasse.add(labelNomeClasse, "wrap, gapbottom 8");
        painelGerenciarClasse.add(textFieldNomeClasse, "wrap, grow");
        painelGerenciarClasse.add(labelComentarioClasse, "wrap, gaptop 15, gapbottom 8");
        painelGerenciarClasse.add(scrollPaneComentarioClasse, "wrap, grow, gapbottom 10");
        painelGerenciarClasse.add(labelQuebrarComentario, "split 2, grow, gapright 10");
        painelGerenciarClasse.add(spinnerComentario, "wrap, grow");

        painelGerenciarClasse.add(labelSeparadorClasse, "grow, wrap, gaptop 15, gapbottom 5");
        painelGerenciarClasse.add(checkBoxAbstrata, "split 3, gapleft 25, gapright:push");
        painelGerenciarClasse.add(checkBoxInterface, "wrap, gapright 25");

        // ==========================================================================================================

        JPanel painelGerenciarAtributos = new JPanel(new MigLayout("insets 30 30 15 30", "[grow, fill][grow, fill]"));
        painelGerenciarAtributos.setBackground(Color.white);

        JList<String> jListAtributosGerenciar = new JList<>();
        jListAtributosGerenciar.setModel(new DefaultListModel<String>());
        jListAtributosGerenciar.setFont(robotoFont.getRobotoMedium(14f));
        jListAtributosGerenciar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListAtributosGerenciar.setFocusable(false);

        JScrollPane scrollPanejListAtributos = new JScrollPane(jListAtributosGerenciar);
        scrollPanejListAtributos.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));


        linha = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x979797));
        margem = new EmptyBorder(4, 8, 4, 8);
        borda = new CompoundBorder(linha, margem);



        JLabel labelNomeAtributo = new JLabel("Nome");
        labelNomeAtributo.setFont(robotoFont.getRobotoMedium(15f));
        labelNomeAtributo.setForeground(new Color(0x9c9c9c));

        JTextField textFieldNomeAtributo = new JTextField();

        CompoundBorder bordaTextField = BorderFactory.createCompoundBorder(
                textFieldNomeAtributo.getBorder(),
                BorderFactory.createEmptyBorder(2, 3, 2, 3)
        );

        textFieldNomeAtributo.setBorder(bordaTextField);
        textFieldNomeAtributo.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomeAtributo.setForeground(new Color(0x484848));
        textFieldNomeAtributo.setEnabled(false);


        JLabel labelTipoAtributo = new JLabel("Tipo");
        labelTipoAtributo.setFont(robotoFont.getRobotoMedium(15f));
        labelTipoAtributo.setForeground(new Color(0x9c9c9c));

        JTextField textFieldTipoAtributo = new JTextField();
        textFieldTipoAtributo.setBorder(bordaTextField);
        textFieldTipoAtributo.setFont(robotoFont.getRobotoMedium(14f));
        textFieldTipoAtributo.setForeground(new Color(0x484848));
        textFieldTipoAtributo.setEnabled(false);


        JLabel labelValorAtributo = new JLabel("Valor Padrão");
        labelValorAtributo.setFont(robotoFont.getRobotoMedium(15f));
        labelValorAtributo.setForeground(new Color(0x9c9c9c));

        JTextField textFieldValorAtributo = new JTextField();
        textFieldValorAtributo.setBorder(bordaTextField);
        textFieldValorAtributo.setFont(robotoFont.getRobotoMedium(14f));
        textFieldValorAtributo.setForeground(new Color(0x484848));
        textFieldValorAtributo.setEnabled(false);


        FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                    Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                    atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                    atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                    atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                    StringBuilder atributoModificado = new StringBuilder();

                    atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                    atributoModificado.append(atributoSelecionado.getVisibilidade());
                    atributoModificado.append(atributoSelecionado.getNome());
                    atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                    atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                    atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                    ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                            .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
                }
            }
        };

        textFieldNomeAtributo.addFocusListener(focusAdapter);
        textFieldValorAtributo.addFocusListener(focusAdapter);
        textFieldTipoAtributo.addFocusListener(focusAdapter);



        JLabel labelVisibilidadeAtributo = new JLabel("Visibilidade");
        labelVisibilidadeAtributo.setFont(robotoFont.getRobotoMedium(15f));
        labelVisibilidadeAtributo.setForeground(new Color(0x9c9c9c));

        JComboBox<String> comboBoxVisibilidadeAtributo = new JComboBox<>();
        comboBoxVisibilidadeAtributo.setForeground(new Color(0x484848));
        comboBoxVisibilidadeAtributo.addItem("Público");
        comboBoxVisibilidadeAtributo.addItem("Privado");
        comboBoxVisibilidadeAtributo.addItem("Protegido");
        comboBoxVisibilidadeAtributo.setFont(robotoFont.getRobotoMedium(15f));
        comboBoxVisibilidadeAtributo.setEnabled(false);
        comboBoxVisibilidadeAtributo.setFocusable(false);
        comboBoxVisibilidadeAtributo.addActionListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() == -1) {
                return;
            }

            Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

            atributoSelecionado.setVisibilidade(switch ((String) comboBoxVisibilidadeAtributo.getSelectedItem()) {
                case "Público" -> "+";
                case "Privado" -> "-";
                case "Protegido" -> "#";
                default -> throw new IllegalStateException("Visibilidade Inválida");
            });

            atributoSelecionado.setNome(textFieldNomeAtributo.getText());
            atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
            atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

            StringBuilder atributoModificado = new StringBuilder();

            atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
            atributoModificado.append(atributoSelecionado.getVisibilidade());
            atributoModificado.append(atributoSelecionado.getNome());
            atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
            atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
            atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

            ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                    .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());

        });


        JCheckBox checkBoxAtributoEstatico = new JCheckBox("Estático");
        checkBoxAtributoEstatico.setBorder(borda);
        checkBoxAtributoEstatico.setFont(robotoFont.getRobotoMedium(15f));
        checkBoxAtributoEstatico.setForeground(new Color(0x9c9c9c));
        checkBoxAtributoEstatico.setFocusable(false);
        checkBoxAtributoEstatico.setEnabled(false);
        checkBoxAtributoEstatico.addItemListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() == -1) {
                return;
            }

            Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());
            atributoSelecionado.setEstatico(checkBoxAtributoEstatico.isSelected());

            atributoSelecionado.setNome(textFieldNomeAtributo.getText());
            atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
            atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

            StringBuilder atributoModificado = new StringBuilder();

            atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
            atributoModificado.append(atributoSelecionado.getVisibilidade());
            atributoModificado.append(atributoSelecionado.getNome());
            atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
            atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
            atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

            ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                    .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());

        });




        JPanel painelOpcoesListaAtributos = new JPanel(new MigLayout("insets 0 0 0 0, flowy"));
        painelOpcoesListaAtributos.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));
        painelOpcoesListaAtributos.setOpaque(false);

        boolean[] modificarJListGerenciarAtributos = { false };

        JButton botaoAcimaAtributos = new JButton();
        botaoAcimaAtributos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_acima.png")));
        botaoAcimaAtributos.setFocusable(false);
        botaoAcimaAtributos.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2)),
                new EmptyBorder(5, 5, 5, 5)));
        botaoAcimaAtributos.setEnabled(false);
        botaoAcimaAtributos.addActionListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                StringBuilder atributoModificado = new StringBuilder();

                atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                atributoModificado.append(atributoSelecionado.getVisibilidade());
                atributoModificado.append(atributoSelecionado.getNome());
                atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                        .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
            }

            Collections.swap(arrayListAtributos, jListAtributosGerenciar.getSelectedIndex(), jListAtributosGerenciar.getSelectedIndex() - 1);

            int indexElementoSelecionado = jListAtributosGerenciar.getSelectedIndex();

            String elementoSelecionado = ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).get(indexElementoSelecionado);
            String elementoAcima = ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).get(indexElementoSelecionado - 1);

            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).set(indexElementoSelecionado - 1, elementoSelecionado);
            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).set(indexElementoSelecionado, elementoAcima);

            modificarJListGerenciarAtributos[0]= false;
            jListAtributosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);

        });

        JButton botaoAbaixoAtributos = new JButton();
        botaoAbaixoAtributos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_abaixo.png")));
        botaoAbaixoAtributos.setFocusable(false);
        botaoAbaixoAtributos.setBorder(new EmptyBorder(5, 5, 5, 5));
        botaoAbaixoAtributos.setEnabled(false);
        botaoAbaixoAtributos.addActionListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                StringBuilder atributoModificado = new StringBuilder();

                atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                atributoModificado.append(atributoSelecionado.getVisibilidade());
                atributoModificado.append(atributoSelecionado.getNome());
                atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                        .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
            }

            Collections.swap(arrayListAtributos, jListAtributosGerenciar.getSelectedIndex(), jListAtributosGerenciar.getSelectedIndex() + 1);

            int indexElementoSelecionado = jListAtributosGerenciar.getSelectedIndex();

            String elementoAbaixo = ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).get(indexElementoSelecionado + 1);
            String elementoSelecionado = ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).get(indexElementoSelecionado);

            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).set(indexElementoSelecionado, elementoAbaixo);
            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).set(indexElementoSelecionado + 1, elementoSelecionado);

            modificarJListGerenciarAtributos[0]= false;
            jListAtributosGerenciar.setSelectedIndex(indexElementoSelecionado + 1);

        });


        JButton botaoExcluirAtributos = new JButton();
        botaoExcluirAtributos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_deletar.png")));
        botaoExcluirAtributos.setFocusable(false);
        botaoExcluirAtributos.setEnabled(false);
        botaoExcluirAtributos.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2)),
                new EmptyBorder(5, 5, 5, 5)));
        botaoExcluirAtributos.addActionListener(e -> {
            int indexElementoSelecionado = jListAtributosGerenciar.getSelectedIndex();

            modificarJListGerenciarAtributos[0]= false;
            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).remove(indexElementoSelecionado);

            arrayListAtributos.remove(indexElementoSelecionado);

            if (((DefaultListModel<String>) jListAtributosGerenciar.getModel()).isEmpty()) {
                jListAtributosGerenciar.clearSelection();
            } else {
                modificarJListGerenciarAtributos[0]= false;
                jListAtributosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);
            }

        });

        JButton botaoNovoAtributo = new JButton();
        botaoNovoAtributo.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_mais.png")));
        botaoNovoAtributo.setFocusable(false);
        botaoNovoAtributo.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2)),
                                    new EmptyBorder(5, 5, 5, 5)));
        botaoNovoAtributo.addActionListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                StringBuilder atributoModificado = new StringBuilder();

                atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                atributoModificado.append(atributoSelecionado.getVisibilidade());
                atributoModificado.append(atributoSelecionado.getNome());
                atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                        .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
            }

            if (((DefaultListModel<String>) jListAtributosGerenciar.getModel()).isEmpty()) {
                botaoExcluirAtributos.setEnabled(true);
                botaoAcimaAtributos.setEnabled(true);
                botaoAbaixoAtributos.setEnabled(true);
                textFieldNomeAtributo.setEnabled(true);
                textFieldTipoAtributo.setEnabled(true);
                textFieldValorAtributo.setEnabled(true);
                labelNomeAtributo.setForeground(Color.black);
                labelTipoAtributo.setForeground(Color.black);
                labelValorAtributo.setForeground(Color.black);
                labelVisibilidadeAtributo.setForeground(Color.black);
                checkBoxAtributoEstatico.setForeground(Color.black);
                checkBoxAtributoEstatico.setEnabled(true);
                comboBoxVisibilidadeAtributo.setEnabled(true);
            }

            Atributo novoAtributo = new Atributo();
            this.arrayListAtributos.add(novoAtributo);
            novoAtributo.setVisibilidade("+");

            ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).add(jListAtributosGerenciar.getModel().getSize(), "+");

            modificarJListGerenciarAtributos[0]= false;
            jListAtributosGerenciar.setSelectedIndex(jListAtributosGerenciar.getModel().getSize() - 1);
        });


        painelOpcoesListaAtributos.add(botaoNovoAtributo, "split 4, align center, gapbottom 0");
        painelOpcoesListaAtributos.add(botaoExcluirAtributos, "align center, gapbottom 0");
        painelOpcoesListaAtributos.add(botaoAcimaAtributos, "align center, gapbottom 0");
        painelOpcoesListaAtributos.add(botaoAbaixoAtributos, "align center, gapbottom 0");


        jListAtributosGerenciar.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (modificarJListGerenciarAtributos[0]) {
                    int indexModificado1 = e.getFirstIndex();
                    int indexModificado2 = e.getLastIndex();
                    int indexSelecionado = jListAtributosGerenciar.getSelectedIndex();

                    int ultimoIndexSelecionadoAtributo = indexSelecionado == indexModificado1? indexModificado2 : indexModificado1;


                    if (ultimoIndexSelecionadoAtributo != -1 && indexModificado1 != indexModificado2) {
                        Atributo atributoSelecionado = arrayListAtributos.get(ultimoIndexSelecionadoAtributo);

                        atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                        atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                        atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                        StringBuilder atributoModificado = new StringBuilder();

                        atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                        atributoModificado.append(atributoSelecionado.getVisibilidade());
                        atributoModificado.append(atributoSelecionado.getNome());
                        atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                        atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                        atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                        ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                                .setElementAt(atributoModificado.toString(), ultimoIndexSelecionadoAtributo);
                    }


                }

                modificarJListGerenciarAtributos[0]= true;

                if (jListAtributosGerenciar.getSelectedIndex() == -1) {
                    botaoExcluirAtributos.setEnabled(false);
                    botaoAcimaAtributos.setEnabled(false);
                    botaoAbaixoAtributos.setEnabled(false);
                    textFieldNomeAtributo.setEnabled(false);
                    textFieldTipoAtributo.setEnabled(false);
                    textFieldValorAtributo.setEnabled(false);
                    labelNomeAtributo.setForeground(new Color(0x9c9c9c));
                    labelTipoAtributo.setForeground(new Color(0x9c9c9c));
                    labelValorAtributo.setForeground(new Color(0x9c9c9c));
                    labelVisibilidadeAtributo.setForeground(new Color(0x9c9c9c));
                    checkBoxAtributoEstatico.setForeground(new Color(0x9c9c9c));
                    checkBoxAtributoEstatico.setEnabled(false);
                    comboBoxVisibilidadeAtributo.setEnabled(false);
                    textFieldNomeAtributo.setText("");
                    textFieldTipoAtributo.setText("");
                    textFieldValorAtributo.setText("");
                    checkBoxAtributoEstatico.setSelected(false);
                    comboBoxVisibilidadeAtributo.setSelectedIndex(0);
                    return;
                } else {
                    botaoExcluirAtributos.setEnabled(true);
                    botaoAcimaAtributos.setEnabled(true);
                    botaoAbaixoAtributos.setEnabled(true);
                    textFieldNomeAtributo.setEnabled(true);
                    textFieldTipoAtributo.setEnabled(true);
                    textFieldValorAtributo.setEnabled(true);
                    labelNomeAtributo.setForeground(Color.black);
                    labelTipoAtributo.setForeground(Color.black);
                    labelValorAtributo.setForeground(Color.black);
                    labelVisibilidadeAtributo.setForeground(Color.black);
                    checkBoxAtributoEstatico.setForeground(Color.black);
                    checkBoxAtributoEstatico.setEnabled(true);
                    comboBoxVisibilidadeAtributo.setEnabled(true);
                }

                Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());
                textFieldNomeAtributo.setText(atributoSelecionado.getNome());
                textFieldTipoAtributo.setText(atributoSelecionado.getTipo());
                textFieldValorAtributo.setText(atributoSelecionado.getValorPadrao());

                comboBoxVisibilidadeAtributo.setSelectedIndex(switch (atributoSelecionado.getVisibilidade()) {
                    case "+" -> 0;
                    case "-" -> 1;
                    case "#" -> 2;
                    default -> throw new IllegalStateException("Visibilidade Inválida");
                });

                checkBoxAtributoEstatico.setSelected(atributoSelecionado.ehEstatico());

                botaoAcimaAtributos.setEnabled(true);
                botaoAbaixoAtributos.setEnabled(true);

                if (jListAtributosGerenciar.getSelectedIndex() == 0) {
                    botaoAcimaAtributos.setEnabled(false);
                }

                if (jListAtributosGerenciar.getSelectedIndex() == jListAtributosGerenciar.getModel().getSize() - 1) {
                    botaoAbaixoAtributos.setEnabled(false);
                }
            }
        });


        JLabel labelSeparadorAtributos = new JLabel();
        labelSeparadorAtributos.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xf8f8f8)),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2))));


        painelGerenciarAtributos.add(scrollPanejListAtributos, "growy, growx, span 2, split 2, gapright 0");
        painelGerenciarAtributos.add(painelOpcoesListaAtributos, "growy, wrap, gapleft 0");

        painelGerenciarAtributos.add(labelNomeAtributo, "gap 0 8 10 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldNomeAtributo, "grow, gaptop 10, gapbottom 8, wrap");

        painelGerenciarAtributos.add(labelTipoAtributo, "gap 0 8 0 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldTipoAtributo, "grow, gapbottom 8, wrap");

        painelGerenciarAtributos.add(labelValorAtributo, "gap 0 8 0 8, span 2, split 2, growy");
        painelGerenciarAtributos.add(textFieldValorAtributo, "wrap, grow, gapbottom 8");


        painelGerenciarAtributos.add(labelSeparadorAtributos, "span 2, grow, wrap, gaptop 15, gapbottom 15");

        painelGerenciarAtributos.add(labelVisibilidadeAtributo, "gap 0 8 0 8, split 2, growy");
        painelGerenciarAtributos.add(comboBoxVisibilidadeAtributo, "grow, gapbottom 8");

        painelGerenciarAtributos.add(checkBoxAtributoEstatico, "gapbottom 8, gapleft:push, gapright:push");




        // ==========================================================================================================

        JPanel painelGerenciarMetodos = new JPanel(new MigLayout("insets 30 30 15 30", "[grow, fill, 50%][grow, fill, 50%]"));
        painelGerenciarMetodos.setBackground(Color.white);

        //DefaultListModel<String> ((DefaultListModel<String)  jListMetodosGerenciar.getModel()= new DefaultListModel<>();
        DefaultListModel<String> listModeltParametrosGerenciar = new DefaultListModel<>();

        JList<String> jListMetodosGerenciar = new JList<>();
        jListMetodosGerenciar.setModel(new DefaultListModel<>());
        jListMetodosGerenciar.setFont(robotoFont.getRobotoMedium(14f));
        jListMetodosGerenciar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListMetodosGerenciar.setFocusable(false);

        JList<String> jListParametrosGerenciar = new JList<>();
        jListParametrosGerenciar.setModel(listModeltParametrosGerenciar);
        jListParametrosGerenciar.setFont(robotoFont.getRobotoMedium(14f));
        jListParametrosGerenciar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListParametrosGerenciar.setFocusable(false);

        JScrollPane scrollPaneMetodos = new JScrollPane(jListMetodosGerenciar);
        scrollPaneMetodos.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));



        linha = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x979797));
        margem = new EmptyBorder(4, 8, 4, 8);
        borda = new CompoundBorder(linha, margem);



        JLabel labelNomeMetodo = new JLabel("Nome");
        labelNomeMetodo.setFont(robotoFont.getRobotoMedium(14f));
        labelNomeMetodo.setForeground(new Color(0x9c9c9c));

        JTextField textFieldNomeMetodo = new JTextField();
        textFieldNomeMetodo.setBorder(bordaTextField);
        textFieldNomeMetodo.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomeMetodo.setForeground(new Color(0x484848));
        textFieldNomeMetodo.setEnabled(false);

        JLabel labelTipoMetodo = new JLabel("Tipo");
        labelTipoMetodo.setFont(robotoFont.getRobotoMedium(14f));
        labelTipoMetodo.setForeground(new Color(0x9c9c9c));

        JTextField textFieldTipoMetodo = new JTextField();
        textFieldTipoMetodo.setBorder(bordaTextField);
        textFieldTipoMetodo.setFont(robotoFont.getRobotoMedium(14f));
        textFieldTipoMetodo.setForeground(new Color(0x484848));
        textFieldTipoMetodo.setEnabled(false);


        focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (jListMetodosGerenciar.getSelectedIndex() != -1) {
                    Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                    metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                    metodoSelecionado.setTipo(textFieldTipoMetodo.getText());


                    StringBuilder metodoModificado = new StringBuilder();

                    metodoModificado.append("<html>");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                    metodoModificado.append(metodoSelecionado.getVisibilidade());
                    metodoModificado.append(metodoSelecionado.getNome());
                    metodoModificado.append("(");
                    metodoModificado.append(metodoSelecionado.toStringParametros());
                    metodoModificado.append(")");
                    metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                    metodoModificado.append("<html>");

                    ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
                }
            }
        };


        textFieldNomeMetodo.addFocusListener(focusAdapter);
        textFieldTipoMetodo.addFocusListener(focusAdapter);



        JLabel labelVisibilidadeMetodo= new JLabel("Visibilidade");
        labelVisibilidadeMetodo.setFont(robotoFont.getRobotoMedium(14f));
        labelVisibilidadeMetodo.setForeground(new Color(0x9c9c9c));


        JComboBox<String> comboBoxVisibilidadeMetodo = new JComboBox<>();
        comboBoxVisibilidadeMetodo.setForeground(new Color(0x484848));
        comboBoxVisibilidadeMetodo.addItem("Público");
        comboBoxVisibilidadeMetodo.addItem("Privado");
        comboBoxVisibilidadeMetodo.addItem("Protegido");
        comboBoxVisibilidadeMetodo.setFont(robotoFont.getRobotoMedium(14f));
        comboBoxVisibilidadeMetodo.setEnabled(false);
        comboBoxVisibilidadeMetodo.setFocusable(false);

        JCheckBox checkBoxMetodoAbstrato = new JCheckBox("Abstrato");
        checkBoxMetodoAbstrato.setBorder(borda);
        checkBoxMetodoAbstrato.setFont(robotoFont.getRobotoMedium(15f));
        checkBoxMetodoAbstrato.setForeground(new Color(0x464958));
        checkBoxMetodoAbstrato.setFocusable(false);
        checkBoxMetodoAbstrato.setEnabled(false);


        JCheckBox checkBoxMetodoEstatico = new JCheckBox("Estático");
        checkBoxMetodoEstatico.setBorder(borda);
        checkBoxMetodoEstatico.setFont(robotoFont.getRobotoMedium(15f));
        checkBoxMetodoEstatico.setForeground(new Color(0x464958));
        checkBoxMetodoEstatico.setFocusable(false);
        checkBoxMetodoEstatico.setEnabled(false);






        JPanel painelOpcoesListaMetodos = new JPanel(new MigLayout("insets 0 0 0 0, flowy"));
        painelOpcoesListaMetodos.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));
        painelOpcoesListaMetodos.setOpaque(false);


        CompoundBorder bordaBotoes = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        boolean[] modificarJListMetodo = { false };


        JButton botaoAcimaMetodos = new JButton();
        botaoAcimaMetodos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_acima.png")));
        botaoAcimaMetodos.setFocusable(false);
        botaoAcimaMetodos.setBorder(bordaBotoes);
        botaoAcimaMetodos.setEnabled(false);
        botaoAcimaMetodos.addActionListener(e -> {
            Collections.swap(arrayListMetodos, jListMetodosGerenciar.getSelectedIndex(), jListMetodosGerenciar.getSelectedIndex() - 1);

            int indexElementoSelecionado = jListMetodosGerenciar.getSelectedIndex();

            String elementoSelecionado = ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).get(indexElementoSelecionado);
            String elementoAcima = ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).get(indexElementoSelecionado - 1);

            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).set(indexElementoSelecionado - 1, elementoSelecionado);
            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).set(indexElementoSelecionado, elementoAcima);

            modificarJListMetodo[0]= false;
            jListMetodosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);

        });




        JButton botaoAbaixoMetodos = new JButton();
        botaoAbaixoMetodos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_abaixo.png")));
        botaoAbaixoMetodos.setFocusable(false);
        botaoAbaixoMetodos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        botaoAbaixoMetodos.setEnabled(false);
        botaoAbaixoMetodos.addActionListener(e -> {
            Collections.swap(arrayListMetodos, jListMetodosGerenciar.getSelectedIndex(), jListMetodosGerenciar.getSelectedIndex() + 1);

            int indexElementoSelecionado = jListMetodosGerenciar.getSelectedIndex();

            String elementoAbaixo = ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).get(indexElementoSelecionado + 1);
            String elementoSelecionado = ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).get(indexElementoSelecionado);

            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).set(indexElementoSelecionado, elementoAbaixo);
            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).set(indexElementoSelecionado + 1, elementoSelecionado);

            modificarJListMetodo[0]= false;
            jListMetodosGerenciar.setSelectedIndex(indexElementoSelecionado + 1);

        });




        JButton botaoExcluirMetodos = new JButton();
        botaoExcluirMetodos.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_deletar.png")));
        botaoExcluirMetodos.setFocusable(false);
        botaoExcluirMetodos.setBorder(bordaBotoes);
        botaoExcluirMetodos.setEnabled(false);
        botaoExcluirMetodos.addActionListener(e -> {
            int indexElementoSelecionado = jListMetodosGerenciar.getSelectedIndex();

            modificarJListMetodo[0]= false;
            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).remove(indexElementoSelecionado);

            arrayListMetodos.remove(indexElementoSelecionado);

            if (((DefaultListModel<String>) jListMetodosGerenciar.getModel()).isEmpty()) {
                jListMetodosGerenciar.clearSelection();
            } else {
                modificarJListMetodo[0]= false;
                jListMetodosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);
            }

        });


        JButton botaoNovoMetodo = new JButton();
        botaoNovoMetodo.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_mais.png")));
        botaoNovoMetodo.setFocusable(false);
        botaoNovoMetodo.setBorder(bordaBotoes);
        botaoNovoMetodo.addActionListener(e -> {
            if (((DefaultListModel<String>) jListMetodosGerenciar.getModel()).isEmpty()) {
                botaoExcluirMetodos.setEnabled(true);
                botaoAcimaMetodos.setEnabled(true);
                botaoAbaixoMetodos.setEnabled(true);
                textFieldNomeMetodo.setEnabled(true);
                textFieldTipoMetodo.setEnabled(true);
                checkBoxMetodoEstatico.setEnabled(true);
                checkBoxMetodoAbstrato.setEnabled(true);
                comboBoxVisibilidadeMetodo.setEnabled(true);
                labelNomeMetodo.setForeground(Color.black);
                labelTipoMetodo.setForeground(Color.black);
                labelVisibilidadeMetodo.setForeground(Color.black);
                checkBoxMetodoEstatico.setForeground(Color.black);
                checkBoxMetodoAbstrato.setForeground(Color.black);
            }

            Metodo novoMetodo = new Metodo();
            this.arrayListMetodos.add(novoMetodo);
            novoMetodo.setVisibilidade("+");

            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).add(((DefaultListModel<String>) jListMetodosGerenciar.getModel()).getSize(), "+");

            modificarJListMetodo[0]= false;
            jListMetodosGerenciar.setSelectedIndex(((DefaultListModel<String>) jListMetodosGerenciar.getModel()).getSize() - 1);

        });


        painelOpcoesListaMetodos.add(botaoNovoMetodo, "split 4, align center, gapbottom 0");
        painelOpcoesListaMetodos.add(botaoExcluirMetodos, "align center, gapbottom 0");
        painelOpcoesListaMetodos.add(botaoAcimaMetodos, "align center, gapbottom 0");
        painelOpcoesListaMetodos.add(botaoAbaixoMetodos, "align center, gapbottom 0");




        jListMetodosGerenciar.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (modificarJListMetodo[0]) {
                    int indexModificado1 = e.getFirstIndex();
                    int indexModificado2 = e.getLastIndex();
                    int indexSelecionado = jListMetodosGerenciar.getSelectedIndex();

                    int ultimoIndexSelecionadoMetodo = indexSelecionado == indexModificado1? indexModificado2 : indexModificado1;

                    if (ultimoIndexSelecionadoMetodo != -1) {
                        Metodo metodoSelecionado = arrayListMetodos.get(ultimoIndexSelecionadoMetodo);

                        metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                        metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                        StringBuilder metodoModificado = new StringBuilder();

                        metodoModificado.append("<html>");
                        metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                        metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                        metodoModificado.append(metodoSelecionado.getVisibilidade());
                        metodoModificado.append(metodoSelecionado.getNome());
                        metodoModificado.append("(");
                        metodoModificado.append(metodoSelecionado.toStringParametros());
                        metodoModificado.append(")");
                        metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                        metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                        metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                        metodoModificado.append("<html>");

                        ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), ultimoIndexSelecionadoMetodo);
                    }
                }

                modificarJListMetodo[0]= true;


                if (jListMetodosGerenciar.getSelectedIndex() == -1) {
                    botaoExcluirMetodos.setEnabled(false);
                    botaoAcimaMetodos.setEnabled(false);
                    botaoAbaixoMetodos.setEnabled(false);
                    textFieldNomeMetodo.setEnabled(false);
                    textFieldTipoMetodo.setEnabled(false);
                    checkBoxMetodoEstatico.setEnabled(false);
                    checkBoxMetodoAbstrato.setEnabled(false);
                    comboBoxVisibilidadeMetodo.setEnabled(false);

                    labelNomeMetodo.setForeground(new Color(0x9c9c9c));
                    labelTipoMetodo.setForeground(new Color(0x9c9c9c));
                    labelVisibilidadeMetodo.setForeground(new Color(0x9c9c9c));
                    checkBoxMetodoEstatico.setForeground(new Color(0x9c9c9c));
                    checkBoxMetodoAbstrato.setForeground(new Color(0x9c9c9c));

                    textFieldNomeMetodo.setText("");
                    textFieldTipoMetodo.setText("");
                    checkBoxMetodoEstatico.setSelected(false);
                    checkBoxMetodoAbstrato.setSelected(false);
                    comboBoxVisibilidadeMetodo.setSelectedIndex(0);
                    return;
                } else {
                    botaoExcluirMetodos.setEnabled(true);
                    botaoAcimaMetodos.setEnabled(true);
                    botaoAbaixoMetodos.setEnabled(true);
                    textFieldNomeMetodo.setEnabled(true);
                    textFieldTipoMetodo.setEnabled(true);
                    checkBoxMetodoEstatico.setEnabled(true);
                    checkBoxMetodoAbstrato.setEnabled(true);
                    comboBoxVisibilidadeMetodo.setEnabled(true);

                    labelNomeMetodo.setForeground(Color.black);
                    labelTipoMetodo.setForeground(Color.black);
                    labelVisibilidadeMetodo.setForeground(Color.black);
                    checkBoxMetodoEstatico.setForeground(Color.black);
                    checkBoxMetodoAbstrato.setForeground(Color.black);
                }

                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                textFieldNomeMetodo.setText(metodoSelecionado.getNome());
                textFieldTipoMetodo.setText(metodoSelecionado.getTipo());

                comboBoxVisibilidadeMetodo.setSelectedIndex(switch (metodoSelecionado.getVisibilidade()) {
                    case "+" -> 0;
                    case "-" -> 1;
                    case "#" -> 2;
                    default -> throw new IllegalStateException("Visibilidade Inválida");
                });

                checkBoxMetodoEstatico.setSelected(metodoSelecionado.ehEstatico());
                checkBoxMetodoAbstrato.setSelected(metodoSelecionado.ehAbstrato());

                botaoAcimaMetodos.setEnabled(true);
                botaoAbaixoMetodos.setEnabled(true);

                if (jListMetodosGerenciar.getSelectedIndex() == 0) {
                    botaoAcimaMetodos.setEnabled(false);
                }

                if (jListMetodosGerenciar.getSelectedIndex() == ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).getSize() - 1) {
                    botaoAbaixoMetodos.setEnabled(false);
                }
            }
        });


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

        // -------------------------------------------------------------------------------------------------------------------

        JScrollPane scrollPaneParametros = new JScrollPane(jListParametrosGerenciar);
        scrollPaneParametros.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));


        JLabel labelParametros = new JLabel("Parâmetros");
        labelParametros.setFont(robotoFont.getRobotoBlack(15f));

        JLabel labelSeparadorParametros = new JLabel();
        labelSeparadorParametros.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x888888)));



        linha = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x979797));
        margem = new EmptyBorder(4, 8, 4, 8);
        borda = new CompoundBorder(linha, margem);



        JLabel labelNomeParametro = new JLabel("Nome");
        labelNomeParametro.setFont(robotoFont.getRobotoMedium(14f));
        labelNomeParametro.setForeground(new Color(0x9c9c9c));

        JTextField textFieldNomeParametro = new JTextField();
        textFieldNomeParametro.setBorder(bordaTextField);
        textFieldNomeParametro.setFont(robotoFont.getRobotoMedium(14f));
        textFieldNomeParametro.setForeground(new Color(0x484848));
        textFieldNomeParametro.setEnabled(false);

        JLabel labelTipoPametro = new JLabel("Tipo");
        labelTipoPametro.setFont(robotoFont.getRobotoMedium(14f));
        labelTipoPametro.setForeground(new Color(0x9c9c9c));

        JTextField textFieldTipoParametro = new JTextField();
        textFieldTipoParametro.setBorder(bordaTextField);
        textFieldTipoParametro.setFont(robotoFont.getRobotoMedium(14f));
        textFieldTipoParametro.setForeground(new Color(0x484848));
        textFieldTipoParametro.setEnabled(false);

        JLabel labelValorPadraoParametro = new JLabel("Valor Padrão");
        labelValorPadraoParametro.setFont(robotoFont.getRobotoMedium(14f));
        labelValorPadraoParametro.setForeground(new Color(0x9c9c9c));

        JTextField textFieldValorPadraoParametro = new JTextField();
        textFieldValorPadraoParametro.setBorder(bordaTextField);
        textFieldValorPadraoParametro.setFont(robotoFont.getRobotoMedium(14f));
        textFieldValorPadraoParametro.setForeground(new Color(0x484848));
        textFieldValorPadraoParametro.setEnabled(false);

        focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                    Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                            .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                    parametroSelecionado.setNome(textFieldNomeParametro.getText());
                    parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                    parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                    StringBuilder parametroModificado = new StringBuilder();

                    parametroModificado.append(parametroSelecionado.getNome());
                    parametroModificado.append(":");
                    parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                    parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());



                    Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                    metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                    metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                    StringBuilder metodoModificado = new StringBuilder();

                    metodoModificado.append("<html>");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                    metodoModificado.append(metodoSelecionado.getVisibilidade());
                    metodoModificado.append(metodoSelecionado.getNome());
                    metodoModificado.append("(");
                    metodoModificado.append(metodoSelecionado.toStringParametros());
                    metodoModificado.append(")");
                    metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                    metodoModificado.append("<html>");


                    ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
                    listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

                }
            }
        };

        textFieldNomeParametro.addFocusListener(focusAdapter);
        textFieldTipoParametro.addFocusListener(focusAdapter);
        textFieldValorPadraoParametro.addFocusListener(focusAdapter);



        JPanel painelOpcoesListaParametros = new JPanel(new MigLayout("insets 0 0 0 0, flowy"));
        painelOpcoesListaParametros.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xc2c2c2)));
        painelOpcoesListaParametros.setOpaque(false);

        boolean[] modificarJListParametros = { false };


        JButton botaoAcimaParametros = new JButton();
        botaoAcimaParametros.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_acima.png")));
        botaoAcimaParametros.setFocusable(false);
        botaoAcimaParametros.setBorder(bordaBotoes);
        botaoAcimaParametros.setEnabled(false);
        botaoAcimaParametros.addActionListener(e -> {
            if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                parametroSelecionado.setNome(textFieldNomeParametro.getText());
                parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                StringBuilder parametroModificado = new StringBuilder();

                parametroModificado.append(parametroSelecionado.getNome());
                parametroModificado.append(":");
                parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

            }

            Collections.swap(arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex()).getParametros(),
                    jListParametrosGerenciar.getSelectedIndex(), jListParametrosGerenciar.getSelectedIndex() - 1);

            int indexElementoSelecionado = jListParametrosGerenciar.getSelectedIndex();

            String elementoSelecionado = listModeltParametrosGerenciar.get(indexElementoSelecionado);
            String elementoAcima = listModeltParametrosGerenciar.get(indexElementoSelecionado - 1);

            listModeltParametrosGerenciar.set(indexElementoSelecionado - 1, elementoSelecionado);
            listModeltParametrosGerenciar.set(indexElementoSelecionado, elementoAcima);

            modificarJListParametros[0]= false;
            jListParametrosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);



            Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

            metodoSelecionado.setNome(textFieldNomeMetodo.getText());
            metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

            StringBuilder metodoModificado = new StringBuilder();

            metodoModificado.append("<html>");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
            metodoModificado.append(metodoSelecionado.getVisibilidade());
            metodoModificado.append(metodoSelecionado.getNome());
            metodoModificado.append("(");
            metodoModificado.append(metodoSelecionado.toStringParametros());
            metodoModificado.append(")");
            metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
            metodoModificado.append("<html>");


            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());

        });




        JButton botaoAbaixoParametros = new JButton();
        botaoAbaixoParametros.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_abaixo.png")));
        botaoAbaixoParametros.setFocusable(false);
        botaoAbaixoParametros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        botaoAbaixoParametros.setEnabled(false);
        botaoAbaixoParametros.addActionListener(e -> {
            if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                parametroSelecionado.setNome(textFieldNomeParametro.getText());
                parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                StringBuilder parametroModificado = new StringBuilder();

                parametroModificado.append(parametroSelecionado.getNome());
                parametroModificado.append(":");
                parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

            }

            Collections.swap(arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex()).getParametros(),
                    jListParametrosGerenciar.getSelectedIndex(), jListParametrosGerenciar.getSelectedIndex() + 1);

            int indexElementoSelecionado = jListParametrosGerenciar.getSelectedIndex();

            String elementoAbaixo = listModeltParametrosGerenciar.get(indexElementoSelecionado + 1);
            String elementoSelecionado = listModeltParametrosGerenciar.get(indexElementoSelecionado);

            listModeltParametrosGerenciar.set(indexElementoSelecionado, elementoAbaixo);
            listModeltParametrosGerenciar.set(indexElementoSelecionado + 1, elementoSelecionado);

            modificarJListParametros[0]= false;
            jListParametrosGerenciar.setSelectedIndex(indexElementoSelecionado + 1);



            Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

            metodoSelecionado.setNome(textFieldNomeMetodo.getText());
            metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

            StringBuilder metodoModificado = new StringBuilder();

            metodoModificado.append("<html>");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
            metodoModificado.append(metodoSelecionado.getVisibilidade());
            metodoModificado.append(metodoSelecionado.getNome());
            metodoModificado.append("(");
            metodoModificado.append(metodoSelecionado.toStringParametros());
            metodoModificado.append(")");
            metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
            metodoModificado.append("<html>");


            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());

        });


        JButton botaoExcluirParametros = new JButton();
        botaoExcluirParametros.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_deletar.png")));
        botaoExcluirParametros.setFocusable(false);
        botaoExcluirParametros.setBorder(bordaBotoes);
        botaoExcluirParametros.setEnabled(false);
        botaoExcluirParametros.addActionListener(e -> {
            int indexElementoSelecionado = jListParametrosGerenciar.getSelectedIndex();

            modificarJListParametros[0]= false;
            listModeltParametrosGerenciar.remove(indexElementoSelecionado);

            arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex()).getParametros().remove(indexElementoSelecionado);

            if (listModeltParametrosGerenciar.isEmpty()) {
                jListParametrosGerenciar.clearSelection();
            } else {
                modificarJListParametros[0]= false;
                jListParametrosGerenciar.setSelectedIndex(indexElementoSelecionado - 1);
            }

            Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

            metodoSelecionado.setNome(textFieldNomeMetodo.getText());
            metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

            StringBuilder metodoModificado = new StringBuilder();

            metodoModificado.append("<html>");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
            metodoModificado.append(metodoSelecionado.getVisibilidade());
            metodoModificado.append(metodoSelecionado.getNome());
            metodoModificado.append("(");
            metodoModificado.append(metodoSelecionado.toStringParametros());
            metodoModificado.append(")");
            metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
            metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
            metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
            metodoModificado.append("<html>");


            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());

        });


        JButton botaoNovoParametro = new JButton();
        botaoNovoParametro.setIcon(new ImageIcon(ClasseUML.class.getResource("/imagens/img_mais.png")));
        botaoNovoParametro.setFocusable(false);
        botaoNovoParametro.setBorder(bordaBotoes);
        botaoNovoParametro.setEnabled(false);
        botaoNovoParametro.addActionListener(e -> {
            Parametro novoParametro = new Parametro();
            arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex()).getParametros().add(novoParametro);
            novoParametro.setNome("");

            if (listModeltParametrosGerenciar.isEmpty()) {
                listModeltParametrosGerenciar.add(listModeltParametrosGerenciar.getSize(), novoParametro.getNome() + ":");

                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                StringBuilder metodoModificado = new StringBuilder();

                metodoModificado.append("<html>");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                metodoModificado.append(metodoSelecionado.getVisibilidade());
                metodoModificado.append(metodoSelecionado.getNome());
                metodoModificado.append("(");
                metodoModificado.append(metodoSelecionado.toStringParametros());
                metodoModificado.append(")");
                metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                metodoModificado.append("<html>");

                ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
            } else {
                listModeltParametrosGerenciar.add(listModeltParametrosGerenciar.getSize(), novoParametro.getNome() + ":");

            }


            if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {
                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                parametroSelecionado.setNome(textFieldNomeParametro.getText());
                parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                StringBuilder parametroModificado = new StringBuilder();

                parametroModificado.append(parametroSelecionado.getNome());
                parametroModificado.append(":");
                parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());


                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                StringBuilder metodoModificado = new StringBuilder();

                metodoModificado.append("<html>");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                metodoModificado.append(metodoSelecionado.getVisibilidade());
                metodoModificado.append(metodoSelecionado.getNome());
                metodoModificado.append("(");
                metodoModificado.append(metodoSelecionado.toStringParametros());
                metodoModificado.append(")");
                metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                metodoModificado.append("<html>");


                ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
            }

            modificarJListParametros[0]= false;
            jListParametrosGerenciar.setSelectedIndex(listModeltParametrosGerenciar.getSize() - 1);

        });



        painelOpcoesListaParametros.add(botaoNovoParametro, "split 4, align center, gapbottom 0");
        painelOpcoesListaParametros.add(botaoExcluirParametros, "align center, gapbottom 0");
        painelOpcoesListaParametros.add(botaoAcimaParametros, "align center, gapbottom 0");
        painelOpcoesListaParametros.add(botaoAbaixoParametros, "align center, gapbottom 0");



        jListParametrosGerenciar.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (modificarJListParametros[0]) {
                    int indexModificado1 = e.getFirstIndex();
                    int indexModificado2 = e.getLastIndex();
                    int indexSelecionado = jListParametrosGerenciar.getSelectedIndex();

                    int ultimoIndexSelecionadoParametro = indexSelecionado == indexModificado1? indexModificado2 : indexModificado1;

                    if (ultimoIndexSelecionadoParametro != -1) {
                        Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                                .getParametros().get(ultimoIndexSelecionadoParametro);

                        parametroSelecionado.setNome(textFieldNomeParametro.getText());
                        parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                        parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                        StringBuilder parametroModificado = new StringBuilder();

                        parametroModificado.append(parametroSelecionado.getNome());
                        parametroModificado.append(":");
                        parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                        parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                        listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), ultimoIndexSelecionadoParametro);


                        Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                        metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                        metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                        StringBuilder metodoModificado = new StringBuilder();

                        metodoModificado.append("<html>");
                        metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                        metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                        metodoModificado.append(metodoSelecionado.getVisibilidade());
                        metodoModificado.append(metodoSelecionado.getNome());
                        metodoModificado.append("(");
                        metodoModificado.append(metodoSelecionado.toStringParametros());
                        metodoModificado.append(")");
                        metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                        metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                        metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                        metodoModificado.append("<html>");


                        ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
                    }
                }

                modificarJListParametros[0]= true;

                if (jListParametrosGerenciar.getSelectedIndex() == -1) {
                    botaoExcluirParametros.setEnabled(false);
                    botaoAcimaParametros.setEnabled(false);
                    botaoAbaixoParametros.setEnabled(false);
                    textFieldNomeParametro.setEnabled(false);
                    textFieldTipoParametro.setEnabled(false);
                    textFieldValorPadraoParametro.setEnabled(false);
                    textFieldNomeParametro.setText("");
                    textFieldTipoParametro.setText("");
                    textFieldValorPadraoParametro.setText("");
                    labelNomeParametro.setForeground(new Color(0x9c9c9c));
                    labelTipoPametro.setForeground(new Color(0x9c9c9c));
                    labelValorPadraoParametro.setForeground(new Color(0x9c9c9c));
                    return;
                } else {
                    botaoExcluirParametros.setEnabled(true);
                    botaoAcimaParametros.setEnabled(true);
                    botaoAbaixoParametros.setEnabled(true);
                    textFieldNomeParametro.setEnabled(true);
                    textFieldTipoParametro.setEnabled(true);
                    textFieldValorPadraoParametro.setEnabled(true);
                    labelNomeParametro.setForeground(Color.black);
                    labelTipoPametro.setForeground(Color.black);
                    labelValorPadraoParametro.setForeground(Color.black);
                }

                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                textFieldNomeParametro.setText(parametroSelecionado.getNome());
                textFieldTipoParametro.setText(parametroSelecionado.getTipo());
                textFieldValorPadraoParametro.setText(parametroSelecionado.getValorPadrao());

                botaoAcimaParametros.setEnabled(true);
                botaoAbaixoParametros.setEnabled(true);

                if (jListParametrosGerenciar.getSelectedIndex() == 0) {
                    botaoAcimaParametros.setEnabled(false);
                }

                if (jListParametrosGerenciar.getSelectedIndex() == listModeltParametrosGerenciar.getSize() - 1) {
                    botaoAbaixoParametros.setEnabled(false);
                }
            }
        });


        jListMetodosGerenciar.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                modificarJListParametros[0] = false;
                listModeltParametrosGerenciar.removeAllElements();

                //jListParametrosGerenciar.clearSelection();

                if (jListMetodosGerenciar.getSelectedIndex() == -1) {
                    botaoNovoParametro.setEnabled(false);
                    return;
                } else {
                    botaoNovoParametro.setEnabled(true);
                }

                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                for (Parametro parametro : metodoSelecionado.getParametros()) {

                    StringBuilder parametroJList = new StringBuilder();

                    parametroJList.append(parametro.getNome());
                    parametroJList.append(":");
                    parametroJList.append(parametro.getTipo().equals("") ? "" : " " + parametro.getTipo());
                    parametroJList.append( parametro.getValorPadrao().equals("") ? "" : " = " + parametro.getValorPadrao());

                    modificarJListParametros[0] = false;
                    listModeltParametrosGerenciar.addElement(parametroJList.toString());
                }

                modificarJListParametros[0] = false;
            }
        });


        ActionListener actionListenerAtualizarParametroMetodo = e -> {
            if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                parametroSelecionado.setNome(textFieldNomeParametro.getText());
                parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                StringBuilder parametroModificado = new StringBuilder();

                parametroModificado.append(parametroSelecionado.getNome());
                parametroModificado.append(":");
                parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

            }

            if (jListMetodosGerenciar.getSelectedIndex() != -1) {
                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                metodoSelecionado.setVisibilidade(switch ((String) comboBoxVisibilidadeMetodo.getSelectedItem()) {
                    case "Público" -> "+";
                    case "Privado" -> "-";
                    case "Protegido" -> "#";
                    default -> throw new IllegalStateException("Visibilidade Inválida");
                });

                metodoSelecionado.setAbstrato(checkBoxMetodoAbstrato.isSelected());
                metodoSelecionado.setEstatico(checkBoxMetodoEstatico.isSelected());
                metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                StringBuilder metodoModificado = new StringBuilder();

                metodoModificado.append("<html>");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                metodoModificado.append(metodoSelecionado.getVisibilidade());
                metodoModificado.append(metodoSelecionado.getNome());
                metodoModificado.append("(");
                metodoModificado.append(metodoSelecionado.toStringParametros());
                metodoModificado.append(")");
                metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                metodoModificado.append("<html>");

                ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
            }
        };

        botaoAcimaMetodos.addActionListener(actionListenerAtualizarParametroMetodo);
        botaoAbaixoMetodos.addActionListener(actionListenerAtualizarParametroMetodo);
        botaoNovoMetodo.addActionListener(actionListenerAtualizarParametroMetodo);
        comboBoxVisibilidadeMetodo.addActionListener(actionListenerAtualizarParametroMetodo);
        checkBoxMetodoEstatico.addActionListener(actionListenerAtualizarParametroMetodo);
        checkBoxMetodoAbstrato.addActionListener(actionListenerAtualizarParametroMetodo);

        checkBoxMetodoAbstrato.addActionListener(e -> checkBoxMetodoEstatico.setSelected(false));

        checkBoxMetodoEstatico.addActionListener(e -> checkBoxMetodoAbstrato.setSelected(false));





        painelGerenciarMetodos.add(labelParametros, "span 2, split 2, growx 0, gapright 7, gapbottom 8");
        painelGerenciarMetodos.add(labelSeparadorParametros, "wrap, gapbottom 8");

        painelGerenciarMetodos.add(scrollPaneParametros, "growy, growx, split 2, spany 3, gapright 0");
        painelGerenciarMetodos.add(painelOpcoesListaParametros, "growy, gapleft 0");


        JPanel painelDadosParametro = new JPanel(new MigLayout("insets 0 0 0 0", "[grow]", "[grow][grow][grow]"));
        painelDadosParametro.setOpaque(false);

        painelDadosParametro.add(labelNomeParametro, "gap 0 8 8 8, growy, split 2");
        painelDadosParametro.add(textFieldNomeParametro, "grow, wrap, gapbottom 8, gaptop 8");

        painelDadosParametro.add(labelTipoPametro, "gap 0 8 8 8, split 2, growy");
        painelDadosParametro.add(textFieldTipoParametro, "grow, wrap, gapbottom 8, gaptop 8");

        painelDadosParametro.add(labelValorPadraoParametro, "gap 0 8 8 8, split 2, growy");
        painelDadosParametro.add(textFieldValorPadraoParametro, "grow, gapbottom 8, gaptop 8");


        painelGerenciarMetodos.add(painelDadosParametro, "gapleft 8");

        // =========================================================================================================

        JPanel painelOpcoes = new JPanel(new MigLayout("insets 0 0 0 0, fill"));
        painelOpcoes.setBackground(Color.white);

        linha = BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(0xe5e9ed));
        margem = new EmptyBorder(0, 15, 0, 15);
        borda = new CompoundBorder(linha, margem);


        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (((JButton) e.getSource()).getForeground() != Color.black) {
                    ((JButton) e.getSource()).setForeground(new Color(0x676767));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (((JButton) e.getSource()).getForeground() != Color.black) {
                    ((JButton) e.getSource()).setForeground(new Color(0x9c9c9c));
                }
            }
        };

        JButton botaoClasse = new JButton("CLASSE");
        botaoClasse.setFont(robotoFont.getRobotoBlack(14f));
        botaoClasse.setFocusable(false);
        botaoClasse.setForeground(Color.black);
        botaoClasse.setBorder(BorderFactory.createEmptyBorder());
        botaoClasse.setRolloverEnabled(false);
        botaoClasse.setContentAreaFilled(false);
        botaoClasse.addMouseListener(mouseAdapter);

        JButton botaoAtributos = new JButton("ATRIBUTOS");
        botaoAtributos.setFont(robotoFont.getRobotoBlack(14f));
        botaoAtributos.setForeground(new Color(0x9c9c9c));
        botaoAtributos.setFocusable(false);
        botaoAtributos.setBorder(BorderFactory.createEmptyBorder());
        botaoAtributos.setContentAreaFilled(false);
        botaoAtributos.addMouseListener(mouseAdapter);
        botaoAtributos.setRolloverEnabled(false);

        JButton botaoMetodos = new JButton("MÉTODOS");
        botaoMetodos.setFont(robotoFont.getRobotoBlack(14f));
        botaoMetodos.setForeground(new Color(0x9c9c9c));
        botaoMetodos.setFocusable(false);
        botaoMetodos.setBorder(BorderFactory.createEmptyBorder());
        botaoMetodos.setContentAreaFilled(false);
        botaoMetodos.addMouseListener(mouseAdapter);
        botaoMetodos.setRolloverEnabled(false);

        MatteBorder bordaAtiva = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black);
        MatteBorder bordaDesativa = BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0xf0f0f0));


        JPanel painelBotaoClasse = new JPanel(new MigLayout("insets 20 40 20 40"));
        painelBotaoClasse.setBorder(bordaAtiva);
        painelBotaoClasse.add(botaoClasse);
        painelBotaoClasse.setOpaque(false);

        JPanel painelBotaoAtributos = new JPanel(new MigLayout("insets 20 40 20 40"));
        painelBotaoAtributos.setBorder(bordaDesativa);
        painelBotaoAtributos.add(botaoAtributos);
        painelBotaoAtributos.setOpaque(false);

        JPanel painelBotaoMetodos = new JPanel(new MigLayout("insets 20 40 20 40"));
        painelBotaoMetodos.setBorder(bordaDesativa);
        painelBotaoMetodos.add(botaoMetodos);
        painelBotaoMetodos.setOpaque(false);

        botaoClasse.addActionListener(e -> {
            botaoClasse.setForeground(Color.black);
            botaoMetodos.setForeground(new Color(0x9c9c9c));
            botaoAtributos.setForeground(new Color(0x9c9c9c));

            if (painelGerenciarClasse.getParent() == null) {
                painelGerenciarComponente.remove(painelGerenciarComponente.getComponent(1));
                painelGerenciarComponente.add(painelGerenciarClasse, "grow, wrap", 1);

                painelBotaoClasse.setBorder(bordaAtiva);
                painelBotaoAtributos.setBorder(bordaDesativa);
                painelBotaoMetodos.setBorder(bordaDesativa);

                painelGerenciarComponente.revalidate();
                painelGerenciarComponente.repaint();
            }
        });

        botaoAtributos.addActionListener(e -> {
            botaoAtributos.setForeground(Color.black);
            botaoMetodos.setForeground(new Color(0x9c9c9c));
            botaoClasse.setForeground(new Color(0x9c9c9c));
            if (painelGerenciarAtributos.getParent() == null) {
                painelGerenciarComponente.remove(painelGerenciarComponente.getComponent(1));
                painelGerenciarComponente.add(painelGerenciarAtributos, "grow, wrap", 1);

                painelBotaoAtributos.setBorder(bordaAtiva);
                painelBotaoClasse.setBorder(bordaDesativa);
                painelBotaoMetodos.setBorder(bordaDesativa);

                painelGerenciarComponente.revalidate();
                painelGerenciarComponente.repaint();
            }
        });

        botaoMetodos.addActionListener(e -> {
            botaoMetodos.setForeground(Color.black);
            botaoClasse.setForeground(new Color(0x9c9c9c));
            botaoAtributos.setForeground(new Color(0x9c9c9c));
            if (painelGerenciarMetodos.getParent() == null) {
                painelGerenciarComponente.remove(painelGerenciarComponente.getComponent(1));
                painelGerenciarComponente.add(painelGerenciarMetodos, "grow, wrap", 1);

                painelBotaoMetodos.setBorder(bordaAtiva);
                painelBotaoClasse.setBorder(bordaDesativa);
                painelBotaoAtributos.setBorder(bordaDesativa);

                painelGerenciarComponente.revalidate();
                painelGerenciarComponente.repaint();
            }
        });

        //============================================================================================================

        painelOpcoes.add(painelBotaoClasse, "split 3, gapright 0, grow");
        painelOpcoes.add(painelBotaoAtributos, "gapright 0, grow");
        painelOpcoes.add(painelBotaoMetodos, "grow");

        // ==========================================================================================================

        JPanel painelErroNomeClasse = new JPanel(new MigLayout("insets 5 0 10 0"));
        painelErroNomeClasse.setBackground(Color.white);

        JLabel labelImgErro = new JLabel(new ImageIcon(ClasseUML.class.getResource("/imagens/icones/erro.png")));

        JPanel painelImgErro = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgErro.setBackground(new Color(0x1d2021));
        painelImgErro.add(labelImgErro, "align center");


        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelErro = new JLabel("A Classe deve possuir um nome!");
        labelErro.setFont(robotoFont.getRobotoMedium(14));

        Border empty = BorderFactory.createEmptyBorder(0, 25, 0, 25);
        Border dashed = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black);
        Border compound = new CompoundBorder(empty, dashed);

        painelMensagem.setBorder(compound);

        painelMensagem.add(labelErro, "align center");


        JPanel painelRespostaOK = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaOK.setBackground(Color.white);
        painelRespostaOK.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaOK = new JLabel("OK");
        labelRespostaOK.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaOK.setForeground(Color.black);

        painelRespostaOK.add(labelRespostaOK, "align center");

        mouseAdapter = new MouseAdapter() {
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
        };

        painelRespostaOK.addMouseListener(mouseAdapter);


        painelErroNomeClasse.add(painelImgErro, "west");
        painelErroNomeClasse.add(painelMensagem, "wrap");
        painelErroNomeClasse.add(painelRespostaOK, "gaptop 10, align right, gapright 20");

        JDialog jDialogErroNomeClasse = new JDialog();
        jDialogErroNomeClasse.setTitle("ERRO");
        jDialogErroNomeClasse.setContentPane(painelErroNomeClasse);
        jDialogErroNomeClasse.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialogErroNomeClasse.setResizable(false);
        jDialogErroNomeClasse.pack();


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jDialogErroNomeClasse.setVisible(false);
            }
        };

        painelRespostaOK.addMouseListener(mouseAdapter);

        // ==========================================================================================================


        JButton botaoSair = new JButton("APLICAR");
        botaoSair.setFont(robotoFont.getRobotoBlack(13f));
        botaoSair.setForeground(Color.white);
        botaoSair.setBackground(Color.black);
        botaoSair.setBorder(new EmptyBorder(12, 20, 12, 20));
        botaoSair.setOpaque(false);
        botaoSair.setFocusable(false);
        botaoSair.setHorizontalTextPosition(JButton.LEFT);
        botaoSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botaoSair.setBackground(new Color(0x262626));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botaoSair.setBackground(Color.black);
            }
        });
        botaoSair.addActionListener(e -> {
            if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                StringBuilder atributoModificado = new StringBuilder();

                atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                atributoModificado.append(atributoSelecionado.getVisibilidade());
                atributoModificado.append(atributoSelecionado.getNome());
                atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                        .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
            }


            if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                        .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                parametroSelecionado.setNome(textFieldNomeParametro.getText());
                parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                StringBuilder parametroModificado = new StringBuilder();

                parametroModificado.append(parametroSelecionado.getNome());
                parametroModificado.append(":");
                parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

            }

            if (jListMetodosGerenciar.getSelectedIndex() != -1) {
                Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                metodoSelecionado.setEstatico(checkBoxMetodoEstatico.isSelected());
                metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                StringBuilder metodoModificado = new StringBuilder();

                metodoModificado.append("<html>");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                metodoModificado.append(metodoSelecionado.getVisibilidade());
                metodoModificado.append(metodoSelecionado.getNome());
                metodoModificado.append("(");
                metodoModificado.append(metodoSelecionado.toStringParametros());
                metodoModificado.append(")");
                metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                metodoModificado.append("<html>");

                ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
            }

            if (textFieldNomeClasse.getText().equals("")) {
                jDialogErroNomeClasse.setLocationRelativeTo(null);
                jDialogErroNomeClasse.setVisible(true);
                return;
            }

            this.atualizarComponente(textFieldNomeClasse.getText(), textAreaComentarioClasse.getText(),
                    checkBoxAbstrata.isSelected(), ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).toArray(),
                    ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).toArray(), (Integer) spinnerComentario.getValue(),
                    checkBoxInterface.isSelected());



            HashMap<String, Object> informacoesDepoisDaModificacao = ClasseUML.this.obterInformacoes();



            Object[] arrayStringInformacoesDepoisDaModificacao = {
                    informacoesDepoisDaModificacao.get("NOME"),
                    informacoesDepoisDaModificacao.get("COMENTARIO"),
                    informacoesDepoisDaModificacao.get("ABSTRATA"),
                    informacoesDepoisDaModificacao.get("INTERFACE"),
                    informacoesDepoisDaModificacao.get("LIMITE_COMENTARIO"),
                    informacoesDepoisDaModificacao.get("LISTA_STRING_METODOS"),
                    informacoesDepoisDaModificacao.get("LISTA_STRING_ATRIBUTOS")
            };

            Object[] arrayStringInformacoesAntesDaModificacao = {
                    informacoesAntesDaModificacao.get("NOME"),
                    informacoesAntesDaModificacao.get("COMENTARIO"),
                    informacoesAntesDaModificacao.get("ABSTRATA"),
                    informacoesAntesDaModificacao.get("INTERFACE"),
                    informacoesAntesDaModificacao.get("LIMITE_COMENTARIO"),
                    informacoesAntesDaModificacao.get("LISTA_STRING_METODOS"),
                    informacoesAntesDaModificacao.get("LISTA_STRING_ATRIBUTOS")
            };

            if (!Arrays.deepToString(arrayStringInformacoesAntesDaModificacao).equals(Arrays.deepToString(arrayStringInformacoesDepoisDaModificacao))) {
                ClasseUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                        informacoesAntesDaModificacao,
                        informacoesDepoisDaModificacao,
                        ClasseUML.this));
            }

            informacoesAntesDaModificacao = ClasseUML.this.obterInformacoes();

        });


        super.getFrameGerenciarComponente().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.getFrameGerenciarComponente().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (jListAtributosGerenciar.getSelectedIndex() != -1) {
                    Atributo atributoSelecionado = arrayListAtributos.get(jListAtributosGerenciar.getSelectedIndex());

                    atributoSelecionado.setNome(textFieldNomeAtributo.getText());
                    atributoSelecionado.setValorPadrao(textFieldValorAtributo.getText());
                    atributoSelecionado.setTipo(textFieldTipoAtributo.getText());

                    StringBuilder atributoModificado = new StringBuilder();

                    atributoModificado.append(atributoSelecionado.ehEstatico() ? "<html><u>" : "");
                    atributoModificado.append(atributoSelecionado.getVisibilidade());
                    atributoModificado.append(atributoSelecionado.getNome());
                    atributoModificado.append(atributoSelecionado.getTipo().equals("") ? "" : ": " + atributoSelecionado.getTipo());
                    atributoModificado.append(atributoSelecionado.getValorPadrao().equals("") ? "" : " = " + atributoSelecionado.getValorPadrao());
                    atributoModificado.append(atributoSelecionado.ehEstatico() ? "</u></html>" : "");

                    ((DefaultListModel<String>) jListAtributosGerenciar.getModel())
                            .setElementAt(atributoModificado.toString(), jListAtributosGerenciar.getSelectedIndex());
                }


                if (jListMetodosGerenciar.getSelectedIndex() != -1 && jListParametrosGerenciar.getSelectedIndex() != -1) {

                    Parametro parametroSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex())
                            .getParametros().get(jListParametrosGerenciar.getSelectedIndex());

                    parametroSelecionado.setNome(textFieldNomeParametro.getText());
                    parametroSelecionado.setTipo(textFieldTipoParametro.getText());
                    parametroSelecionado.setValorPadrao(textFieldValorPadraoParametro.getText());

                    StringBuilder parametroModificado = new StringBuilder();

                    parametroModificado.append(parametroSelecionado.getNome());
                    parametroModificado.append(":");
                    parametroModificado.append(parametroSelecionado.getTipo().equals("") ? "" : " " + parametroSelecionado.getTipo());
                    parametroModificado.append( parametroSelecionado.getValorPadrao().equals("") ? "" : " = " + parametroSelecionado.getValorPadrao());

                    listModeltParametrosGerenciar.setElementAt(parametroModificado.toString(), jListParametrosGerenciar.getSelectedIndex());

                }

                if (jListMetodosGerenciar.getSelectedIndex() != -1) {
                    Metodo metodoSelecionado = arrayListMetodos.get(jListMetodosGerenciar.getSelectedIndex());

                    metodoSelecionado.setEstatico(checkBoxMetodoEstatico.isSelected());
                    metodoSelecionado.setNome(textFieldNomeMetodo.getText());
                    metodoSelecionado.setTipo(textFieldTipoMetodo.getText());

                    StringBuilder metodoModificado = new StringBuilder();

                    metodoModificado.append("<html>");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "<u>" : "");
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "<i>" : "");
                    metodoModificado.append(metodoSelecionado.getVisibilidade());
                    metodoModificado.append(metodoSelecionado.getNome());
                    metodoModificado.append("(");
                    metodoModificado.append(metodoSelecionado.toStringParametros());
                    metodoModificado.append(")");
                    metodoModificado.append(metodoSelecionado.getTipo().equals("") ? "" : ": " + metodoSelecionado.getTipo());
                    metodoModificado.append(metodoSelecionado.ehAbstrato() ? "</i>" : "");
                    metodoModificado.append(metodoSelecionado.ehEstatico() ? "</u>" : "");
                    metodoModificado.append("<html>");

                    ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).setElementAt(metodoModificado.toString(), jListMetodosGerenciar.getSelectedIndex());
                }

                if (textFieldNomeClasse.getText().equals("")) {
                    jDialogErroNomeClasse.setLocationRelativeTo(null);
                    jDialogErroNomeClasse.setVisible(true);
                } else {

                    atualizarComponente(textFieldNomeClasse.getText(), textAreaComentarioClasse.getText(),
                            checkBoxAbstrata.isSelected(), ((DefaultListModel<String>) jListAtributosGerenciar.getModel()).toArray(),
                            ((DefaultListModel<String>) jListMetodosGerenciar.getModel()).toArray(), (Integer) spinnerComentario.getValue(),
                            checkBoxInterface.isSelected());


                    HashMap<String, Object> informacoesDepoisDaModificacao = ClasseUML.this.obterInformacoes();



                    Object[] arrayStringInformacoesDepoisDaModificacao = {
                            informacoesDepoisDaModificacao.get("NOME"),
                            informacoesDepoisDaModificacao.get("COMENTARIO"),
                            informacoesDepoisDaModificacao.get("ABSTRATA"),
                            informacoesDepoisDaModificacao.get("INTERFACE"),
                            informacoesDepoisDaModificacao.get("LIMITE_COMENTARIO"),
                            informacoesDepoisDaModificacao.get("LISTA_STRING_METODOS"),
                            informacoesDepoisDaModificacao.get("LISTA_STRING_ATRIBUTOS")
                    };

                    Object[] arrayStringInformacoesAntesDaModificacao = {
                            informacoesAntesDaModificacao.get("NOME"),
                            informacoesAntesDaModificacao.get("COMENTARIO"),
                            informacoesAntesDaModificacao.get("ABSTRATA"),
                            informacoesAntesDaModificacao.get("INTERFACE"),
                            informacoesAntesDaModificacao.get("LIMITE_COMENTARIO"),
                            informacoesAntesDaModificacao.get("LISTA_STRING_METODOS"),
                            informacoesAntesDaModificacao.get("LISTA_STRING_ATRIBUTOS")
                    };

                    if (!Arrays.deepToString(arrayStringInformacoesAntesDaModificacao).equals(Arrays.deepToString(arrayStringInformacoesDepoisDaModificacao))) {
                        ClasseUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                                informacoesAntesDaModificacao,
                                informacoesDepoisDaModificacao,
                                ClasseUML.this));
                    }


                    ClasseUML.super.getFrameGerenciarComponente().setVisible(false);

                }
            }
        });

        //============================================================================================================

        painelGerenciarComponente.add(painelOpcoes, "north");


        JPanel painelTamanhoMaximo = new JPanel();
        painelTamanhoMaximo.setPreferredSize(new Dimension(
                Math.max(painelGerenciarClasse.getPreferredSize().width,
                        Math.max(painelGerenciarAtributos.getPreferredSize().width,
                        painelGerenciarMetodos.getPreferredSize().width)),
                Math.max(painelGerenciarClasse.getPreferredSize().height,
                        Math.max(painelGerenciarAtributos.getPreferredSize().height,
                        painelGerenciarMetodos.getPreferredSize().height))
        ));


        painelGerenciarComponente.add(painelTamanhoMaximo, "grow, wrap");


        painelGerenciarComponente.add(botaoSair, "align right, gaptop:push, gapbottom 0");

        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();

        painelGerenciarComponente.remove(painelTamanhoMaximo);
        painelGerenciarComponente.add(painelGerenciarClasse, "grow, wrap", 1);




        ((JButton) super.getPainelOpcoesComponenteUML().getComponent(0)).addActionListener(e -> {
            if (!ClasseUML.super.getFrameGerenciarComponente().isVisible()) {

                informacoesAntesDaModificacao = obterInformacoes();

                textFieldNomeClasse.setText(ClasseUML.this.nomeClasse);
                textAreaComentarioClasse.setText(ClasseUML.this.comentarioClasse);
                spinnerComentario.setValue(ClasseUML.this.limiteComentario);
                checkBoxAbstrata.setSelected(ClasseUML.this.abstrata);
                checkBoxInterface.setSelected(ClasseUML.this.ehInterface);


                DefaultListModel<String> novoListModelAtributosGerenciar = new DefaultListModel<>();

                for (Atributo atributo : arrayListAtributos) {
                    StringBuilder novoAtributo = new StringBuilder();

                    novoAtributo.append(atributo.ehEstatico() ? "<html><u>" : "");
                    novoAtributo.append(atributo.getVisibilidade());
                    novoAtributo.append(atributo.getNome());
                    novoAtributo.append(atributo.getTipo().equals("") ? "" : ": " + atributo.getTipo());
                    novoAtributo.append(atributo.getValorPadrao().equals("") ? "" : " = " + atributo.getValorPadrao());
                    novoAtributo.append(atributo.ehEstatico() ? "</u></html>" : "");

                    novoListModelAtributosGerenciar.addElement(String.valueOf(novoAtributo));
                }

                modificarJListGerenciarAtributos[0] = false;
                jListAtributosGerenciar.setModel(novoListModelAtributosGerenciar);
                modificarJListGerenciarAtributos[0] = false;



                DefaultListModel<String> novoListModelMetodosGerenciar = new DefaultListModel<>();

                for (Metodo metodo : arrayListMetodos) {
                    StringBuilder novoMetodo = new StringBuilder();

                    novoMetodo.append("<html>");
                    novoMetodo.append(metodo.ehEstatico() ? "<u>" : "");
                    novoMetodo.append(metodo.ehAbstrato() ? "<i>" : "");
                    novoMetodo.append(metodo.getVisibilidade());
                    novoMetodo.append(metodo.getNome());
                    novoMetodo.append("(");
                    novoMetodo.append(metodo.toStringParametros());
                    novoMetodo.append(")");
                    novoMetodo.append(metodo.getTipo().equals("") ? "" : ": " + metodo.getTipo());
                    novoMetodo.append(metodo.ehAbstrato() ? "</i>" : "");
                    novoMetodo.append(metodo.ehEstatico() ? "</u>" : "");
                    novoMetodo.append("<html>");

                    novoListModelMetodosGerenciar.addElement(String.valueOf(novoMetodo));
                }

                modificarJListMetodo[0] = false;
                jListMetodosGerenciar.setModel(novoListModelMetodosGerenciar);
                modificarJListMetodo[0] = false;
            }
        });


    }

    @Override
    HashMap<String, Object> obterInformacoes() {

        HashMap<String, Object> mapaInformacoesComponente = new HashMap<>();


        // String novoNome, String novoComentario, boolean ehAbstrata,
        //                                    Object[] listaAtributos, Object[] listaMetodos, int limiteComentario,
        //                                    boolean ehInterface

        mapaInformacoesComponente.put("POSICAO_X", super.getPainelComponente().getX());
        mapaInformacoesComponente.put("POSICAO_Y", super.getPainelComponente().getY());
        mapaInformacoesComponente.put("NOME", nomeClasse);
        mapaInformacoesComponente.put("COMENTARIO", comentarioClasse);
        mapaInformacoesComponente.put("ABSTRATA", abstrata);
        mapaInformacoesComponente.put("INTERFACE", ehInterface);
        mapaInformacoesComponente.put("LIMITE_COMENTARIO", limiteComentario);
        mapaInformacoesComponente.put("LISTA_STRING_METODOS", ((DefaultListModel<String>) jListMetodos.getModel()).toArray());
        mapaInformacoesComponente.put("LISTA_STRING_ATRIBUTOS", ((DefaultListModel<String>) jListAtributos.getModel()).toArray());


        ArrayList<Atributo> novaArrayListAtributos = new ArrayList<>();

        for (Atributo atributo : this.arrayListAtributos) {
            novaArrayListAtributos.add(new Atributo(atributo.getNome(), atributo.getTipo(), atributo.getValorPadrao(),
                    atributo.getVisibilidade(), atributo.ehEstatico()));
        }

        mapaInformacoesComponente.put("LISTA_OBJETOS_ATRIBUTOS", novaArrayListAtributos);


        ArrayList<Metodo> novaArrayListMetodos = new ArrayList<>();

        for (Metodo metodo : this.arrayListMetodos) {

            ArrayList<Parametro> novaArrayListParametros = new ArrayList<>();

            for (Parametro parametro : metodo.getParametros()) {
                novaArrayListParametros.add(new Parametro(parametro.getNome(), parametro.getTipo(), parametro.getValorPadrao()));
            }

            novaArrayListMetodos.add(new Metodo(metodo.getNome(), metodo.getVisibilidade(), metodo.getTipo(),
                    metodo.ehEstatico(), metodo.ehAbstrato(), novaArrayListParametros));
        }

        mapaInformacoesComponente.put("LISTA_OBJETOS_METODOS", novaArrayListMetodos);

        return  mapaInformacoesComponente;
    }

    @Override
    public String toString() {
        StringBuilder toStringClasse = new StringBuilder();

        toStringClasse.append(super.getPainelComponente().getX());
        toStringClasse.append("\n");

        toStringClasse.append(super.getPainelComponente().getY());
        toStringClasse.append("\n");

        toStringClasse.append(nomeClasse);
        toStringClasse.append("\n");

        toStringClasse.append(comentarioClasse.replaceAll("\n", "(novaLinha)"));
        toStringClasse.append("\n");

        toStringClasse.append(limiteComentario);
        toStringClasse.append("\n");

        toStringClasse.append(abstrata);
        toStringClasse.append("\n");

        toStringClasse.append(ehInterface);
        toStringClasse.append("\n");

        toStringClasse.append(arrayListAtributos.size());
        toStringClasse.append("\n");

        for (Atributo atributo : arrayListAtributos) {
            toStringClasse.append(atributo.getVisibilidade());
            toStringClasse.append("\n");
            toStringClasse.append(atributo.getNome());
            toStringClasse.append("\n");
            toStringClasse.append(atributo.getTipo());
            toStringClasse.append("\n");
            toStringClasse.append(atributo.getValorPadrao());
            toStringClasse.append("\n");
            toStringClasse.append(atributo.ehEstatico());
            toStringClasse.append("\n");
        }

        toStringClasse.append(arrayListMetodos.size());
        toStringClasse.append("\n");

        for (Metodo metodo : arrayListMetodos) {
            toStringClasse.append(metodo.getVisibilidade());
            toStringClasse.append("\n");
            toStringClasse.append(metodo.getNome());
            toStringClasse.append("\n");
            toStringClasse.append(metodo.getTipo());
            toStringClasse.append("\n");
            toStringClasse.append(metodo.ehAbstrato());
            toStringClasse.append("\n");
            toStringClasse.append(metodo.ehEstatico());
            toStringClasse.append("\n");

            toStringClasse.append(metodo.getParametros().size());
            toStringClasse.append("\n");

            for (Parametro parametro : metodo.getParametros()) {
                toStringClasse.append(parametro.getNome());
                toStringClasse.append("\n");
                toStringClasse.append(parametro.getTipo());
                toStringClasse.append("\n");
                toStringClasse.append(parametro.getValorPadrao());
                toStringClasse.append("\n");
            }
        }


        return toStringClasse.toString();
    }
}
