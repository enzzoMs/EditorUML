package ComponentesUML;

/*
import AlteracoesDeElementos.ComponenteModificado;
import ClassesAuxiliares.RobotoFont;
import DiagramaUML.DiagramaUML;
import componentes.ComponenteUML;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/*
public class AnotacaoUML extends ComponenteUML {

    private String textoAnotacao = "";
    private final JPanel painelTextoAnotacao;
    private final JLabel labelTriangulo;
    private final JPanel painelSuperiorVazio;
    private JTextArea textAreaAnotacao;

    private int limiteAnotacao;

    public AnotacaoUML(DiagramaUML diagramaUML) {
        super(diagramaUML);

        painelTextoAnotacao = new JPanel(new MigLayout("insets 10 10 5 10"));
        painelTextoAnotacao.setBackground(Color.white);
        painelTextoAnotacao.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, Color.DARK_GRAY));

        painelSuperiorVazio = new JPanel();
        painelSuperiorVazio.setBackground(Color.white);
        painelSuperiorVazio.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, Color.DARK_GRAY));

        labelTriangulo = new JLabel(new ImageIcon(AnotacaoUML.class.getResource("/imagens/img_triangulo_anotacao.png")));

        // =======================================================================================================

        int LARGURA_MINIMA = 70;
        int ALTURA_MINIMA = 60;

        super.setLargura(LARGURA_MINIMA);

        super.setAltura(ALTURA_MINIMA);

        super.getPainelComponente().add(super.getGlassPaneComponente());
        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());


        super.getPainelComponente().add(painelSuperiorVazio);
        painelSuperiorVazio.setBounds(0, 0, LARGURA_MINIMA - labelTriangulo.getPreferredSize().width,
                labelTriangulo.getPreferredSize().height);

        super.getPainelComponente().add(labelTriangulo);
        labelTriangulo.setBounds(LARGURA_MINIMA - labelTriangulo.getPreferredSize().width,
                0, labelTriangulo.getPreferredSize().width, labelTriangulo.getPreferredSize().height);

        super.getPainelComponente().add(painelTextoAnotacao);
        painelTextoAnotacao.setBounds(0, painelSuperiorVazio.getBounds().height, LARGURA_MINIMA,
                ALTURA_MINIMA - painelSuperiorVazio.getBounds().height);


        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();

    }


    public void atualizarComponente(String novaAnotacao, int limiteAnotacao) {
        RobotoFont robotoFont = new RobotoFont();

        int LARGURA_PAINEL_OPCOES = 40;

        try {
            super.getPainelComponente().remove(getPainelComponente().getComponent(4));
            getPainelComponente().setSize(getPainelComponente().getWidth() - LARGURA_PAINEL_OPCOES,
                    getPainelComponente().getHeight());
        } catch (Exception e) {}


        textoAnotacao = novaAnotacao;
        textAreaAnotacao.setText(novaAnotacao);
        this.limiteAnotacao = limiteAnotacao;

        painelTextoAnotacao.removeAll();

        if (!novaAnotacao.equals("")) {
            ArrayList<String> arrayLinhasComentario = new ArrayList<>();

            int numCaracteres = 0;
            boolean fazerQuebraDeLinha = false;
            int inicio = 0;

            for (int i = 0; i < novaAnotacao.length() ; i++) {

                if ((fazerQuebraDeLinha && novaAnotacao.charAt(i) == ' ') || novaAnotacao.charAt(i) == '\n') {
                    arrayLinhasComentario.add(novaAnotacao.substring(inicio, i));
                    inicio = i + 1;
                    fazerQuebraDeLinha = false;
                    numCaracteres = 0;
                }


                if (numCaracteres == this.limiteAnotacao) {
                    fazerQuebraDeLinha = true;
                }

                numCaracteres++;
            }

            if (numCaracteres != 0) {
                arrayLinhasComentario.add(novaAnotacao.substring(inicio));
            }

            for (String linhaComentario : arrayLinhasComentario) {
                JLabel labelComentario = new JLabel(linhaComentario, JLabel.CENTER);
                labelComentario.setOpaque(false);
                labelComentario.setFont(robotoFont.getRobotoMedium(13));

                painelTextoAnotacao.add(labelComentario, "wrap, grow, gaptop 0, gapbottom 0");
            }

        }


        int LARGURA_MINIMA = 70;
        int ALTURA_MINIMA = 60;

        super.setLargura(Math.max(painelTextoAnotacao.getPreferredSize().width, LARGURA_MINIMA));
        super.setAltura(Math.max(painelTextoAnotacao.getPreferredSize().height + painelSuperiorVazio.getBounds().height, ALTURA_MINIMA));


        painelSuperiorVazio.setBounds(0, 0, super.getLargura() - labelTriangulo.getPreferredSize().width,
                labelTriangulo.getPreferredSize().height);

        labelTriangulo.setBounds(super.getLargura() - labelTriangulo.getPreferredSize().width,
                0, labelTriangulo.getPreferredSize().width, labelTriangulo.getPreferredSize().height);


        painelTextoAnotacao.setBounds(0, painelSuperiorVazio.getBounds().height, super.getLargura(),
                super.getAltura() - painelSuperiorVazio.getBounds().height);

        super.getGlassPaneComponente().setBounds(0, 0, super.getLargura(), super.getAltura());

        super.getPainelComponente().setSize(new Dimension(super.getLargura(), super.getAltura()));

        super.atualizarAreasDeConexao();

        super.getPainelComponente().revalidate();
        super.getPainelComponente().repaint();
    }


    @Override
    void initFrameGerenciarComponente() {
        RobotoFont robotoFont = new RobotoFont();

        JPanel painelGerenciarComponente = new JPanel(new MigLayout("insets 0 0 20 0", "","[grow, fill][grow, fill][]"));
        painelGerenciarComponente.setBackground(Color.white);

        // ==========================================================================================================

        JLabel labelAnotacao = new JLabel("ANOTAÇÃO", JLabel.CENTER);
        labelAnotacao.setFont(robotoFont.getRobotoBlack(14f));

        JPanel painelLabelAnotacao = new JPanel(new MigLayout("insets 20 40 20 40","[grow]"));
        painelLabelAnotacao.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelLabelAnotacao.add(labelAnotacao, "align center");
        painelLabelAnotacao.setOpaque(false);



        JPanel painelGerenciarAnotacao = new JPanel(new MigLayout("insets 20 10 15 10", "[grow, fill]"));
        painelGerenciarAnotacao.setBackground(Color.white);

        JLabel labelTextoAnotacao = new JLabel("Texto");
        labelTextoAnotacao.setFont(robotoFont.getRobotoMedium(15f));

        textAreaAnotacao = new JTextArea(5, 7);
        textAreaAnotacao.setFont(robotoFont.getRobotoMedium(14f));
        textAreaAnotacao.setLineWrap(true);
        textAreaAnotacao.setForeground(new Color(0x484848));

        JScrollPane scrollPaneAnotacao = new JScrollPane(textAreaAnotacao);
        scrollPaneAnotacao.setBorder(BorderFactory.createCompoundBorder(
                new JTextField().getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        JLabel labelSeparadorAnotacao = new JLabel();
        labelSeparadorAnotacao.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xf8f8f8)),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2))));


        JLabel labelQuebrarAnotacao = new JLabel("Quebrar texto após este número de caracteres:");
        labelQuebrarAnotacao.setFont(robotoFont.getRobotoMedium(13f));
        labelQuebrarAnotacao.setForeground(new Color(0x454545));


        // spnner so aparece apos comentario e com limite superior em referencia ao comprimento do comentario
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(20, 20, 200, 1);
        JSpinner spinnerAnotacao = new JSpinner(spinnerNumberModel);
        spinnerAnotacao.setFont(robotoFont.getRobotoBlack(13f));
        spinnerAnotacao.setFocusable(false);
        spinnerAnotacao.setForeground(new Color(0x383838));
        ((JSpinner.DefaultEditor) spinnerAnotacao.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) spinnerAnotacao.getEditor()).getTextField().setFocusable(false);


        painelGerenciarAnotacao.add(labelTextoAnotacao, "wrap, gapbottom 8");
        painelGerenciarAnotacao.add(scrollPaneAnotacao, "wrap, grow");
        painelGerenciarAnotacao.add(labelSeparadorAnotacao, "wrap, gaptop 15, gapbottom 15");
        painelGerenciarAnotacao.add(labelQuebrarAnotacao, "split 2, grow, gapright 10");
        painelGerenciarAnotacao.add(spinnerAnotacao, "wrap, grow");


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

            HashMap<String, Object> informacoesAntesDaModificacao = AnotacaoUML.this.obterInformacoes();

            atualizarComponente(textAreaAnotacao.getText(), (Integer) spinnerAnotacao.getValue());

            HashMap<String, Object> informacoesDepoisDaModificacao = AnotacaoUML.this.obterInformacoes();


            if (!informacoesAntesDaModificacao.get("TEXTO_ANOTACAO").equals(informacoesDepoisDaModificacao.get("TEXTO_ANOTACAO"))
                || !informacoesAntesDaModificacao.get("LIMITE_ANOTACAO").equals(informacoesDepoisDaModificacao.get("LIMITE_ANOTACAO"))) {

                AnotacaoUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                        informacoesAntesDaModificacao,
                        obterInformacoes(),
                        AnotacaoUML.this));
            }

        });

        super.getFrameGerenciarComponente().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                HashMap<String, Object> informacoesAntesDaModificacao = AnotacaoUML.this.obterInformacoes();

                atualizarComponente(textAreaAnotacao.getText(), (Integer) spinnerAnotacao.getValue());

                HashMap<String, Object> informacoesDepoisDaModificacao = AnotacaoUML.this.obterInformacoes();


                if (!informacoesAntesDaModificacao.get("TEXTO_ANOTACAO").equals(informacoesDepoisDaModificacao.get("TEXTO_ANOTACAO"))
                        || !informacoesAntesDaModificacao.get("LIMITE_ANOTACAO").equals(informacoesDepoisDaModificacao.get("LIMITE_ANOTACAO"))) {

                    AnotacaoUML.super.getDiagramaUML().getAreaDeDiagramas().addAlteracao(new ComponenteModificado(
                            informacoesAntesDaModificacao,
                            informacoesDepoisDaModificacao,
                            AnotacaoUML.this));
                }
            }
        });



        painelGerenciarComponente.add(painelLabelAnotacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(painelGerenciarAnotacao, "grow, wrap, gapleft 20, gapright 20");
        painelGerenciarComponente.add(botaoAplicar, "align right, gaptop 10, gapbottom 10");


        //painelGerenciarComponente.add(botaoSair, "align right, gaptop:push, gapbottom 0");

        super.getFrameGerenciarComponente().add(painelGerenciarComponente);
        super.getFrameGerenciarComponente().pack();

    }

    @Override
    HashMap<String, Object> obterInformacoes() {

        HashMap<String, Object> mapaInformacoesComponente = new HashMap<>();

        mapaInformacoesComponente.put("POSICAO_X", super.getPainelComponente().getX());
        mapaInformacoesComponente.put("POSICAO_Y", super.getPainelComponente().getY());
        //mapaInformacoesComponente.put("LARGURA", String.valueOf(super.getPainelComponente().getY()));
        mapaInformacoesComponente.put("TEXTO_ANOTACAO", textoAnotacao);
        mapaInformacoesComponente.put("LIMITE_ANOTACAO", limiteAnotacao);

        return  mapaInformacoesComponente;
    }

    @Override
    public String toString() {

        return super.getPainelComponente().getX() +
                "\n" +
                super.getPainelComponente().getY() +
                "\n" +
                textoAnotacao.replaceAll("\n", "(novaLinha)") +
                "\n" +
                limiteAnotacao +
                "\n";
    }
}*/

