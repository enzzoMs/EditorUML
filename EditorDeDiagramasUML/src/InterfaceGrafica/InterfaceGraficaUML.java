package InterfaceGrafica;

import ClassesAuxiliares.RobotoFont;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InterfaceGraficaUML {
    /*
        Classe principal da interface gráfica do aplicativo. É composta pelas classes MenuPrincpal e AreaDeDiagramas.
        Possui como atributo um framePrincipal que é o JFrame onde todos os componentes gráficos são adicionados.
        Através de seus métodos a classe gerencia esse frame decidindo qual dos componentes gráficos devem ser
        mostrados em qual momento.
     */

    private final JFrame framePrincipal;
    private final MenuPrincipal menuPrincipal;
    private final AreaDeDiagramas areaDeDiagramas;

    /* O JDialog abaixo é mostrado ao usuário sempre que ele tentar sair do aplicativo, pedindo uma confirmação se ele
    realmente deseja sair.
     */
    private final JDialog JDialogSairDoAplicativo;


    public InterfaceGraficaUML() {

        // Criando e configurando o framePrincipal --------------------------------------------------------------------

        this.framePrincipal = new JFrame();
        this.framePrincipal.setResizable(false);
        this.framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.framePrincipal.setLayout(new MigLayout());
        this.framePrincipal.setTitle("Editor de Diagramas UML");
        framePrincipal.setIconImage(new ImageIcon(MenuPrincipal.class.getResource("/imagens/logo/app_icone.png")).getImage());

        // Criando um menuPrincipal e uma AreaDeDiagramas a partir das classes relacionadas ---------------------------

        this.menuPrincipal = new MenuPrincipal(this);
        this.areaDeDiagramas = new AreaDeDiagramas(this);

        // Adicionado primeiramente o painel do menuPrincipal ao frame ------------------------------------------------
        this.framePrincipal.add(menuPrincipal.getPainelMenuPrincipal(), "north");

        this.framePrincipal.pack();
        this.framePrincipal.setLocationRelativeTo(null);

        // Criando a parte gráfica do JDialogSairDoAplicativo ---------------------------------------------------------

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelPrincipalJDialogSair = new JPanel(new MigLayout("fill, insets 0 15 0 15"));


        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 20 20 20 20"));
        painelImgInterrogacao.setBackground(new Color(0x1d2021));

        JLabel imgInterrogacao = new JLabel(new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_interrogacao_grande.png")));
        painelImgInterrogacao.add(imgInterrogacao, "align center");


        JPanel painelPerguntaSair = new JPanel(new MigLayout("insets 15 25 15 25"));
        painelPerguntaSair.setOpaque(false);
        painelPerguntaSair.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 25, 0, 25),
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black)
        ));

        JLabel labelPerguntaSair = new JLabel("Deseja realmente sair?");
        labelPerguntaSair.setFont(robotoFont.getRobotoMedium(16));

        painelPerguntaSair.add(labelPerguntaSair, "align center");


        JPanel painelRespostaSim = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelRespostaSim.setBackground(Color.white);
        painelRespostaSim.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaSim = new JLabel("Sim");
        labelRespostaSim.setFont(robotoFont.getRobotoMedium(15));
        labelRespostaSim.setForeground(Color.black);

        painelRespostaSim.add(labelRespostaSim, "align center");
        painelRespostaSim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });


        JPanel painelRespostaNao = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelRespostaNao.setBackground(Color.white);
        painelRespostaNao.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaNao = new JLabel("Não");
        labelRespostaNao.setFont(robotoFont.getRobotoMedium(15));
        labelRespostaNao.setForeground(Color.black);

        painelRespostaNao.add(labelRespostaNao, "align center");
        painelRespostaNao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialogSairDoAplicativo.setVisible(false);
            }
        });


        MouseAdapter mouseAdapterPaineisReposta = new MouseAdapter() {
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

        painelRespostaNao.addMouseListener(mouseAdapterPaineisReposta);
        painelRespostaSim.addMouseListener(mouseAdapterPaineisReposta);



        JPanel painelConfirmacao = new JPanel(new MigLayout("fill, insets 25 40 30 40"));
        painelConfirmacao.setBackground(Color.white);
        painelConfirmacao.add(painelPerguntaSair, "north");
        painelConfirmacao.add(painelRespostaSim, "wrap, align center, grow, gapbottom 5");
        painelConfirmacao.add(painelRespostaNao, "align center, grow");

        painelPrincipalJDialogSair.add(painelImgInterrogacao, "north");
        painelPrincipalJDialogSair.add(painelConfirmacao, "south");
        painelRespostaNao.setOpaque(true);

        JDialogSairDoAplicativo = new JDialog();
        JDialogSairDoAplicativo.setTitle("Sair");
        JDialogSairDoAplicativo.setContentPane(painelPrincipalJDialogSair);
        JDialogSairDoAplicativo.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JDialogSairDoAplicativo.setResizable(false);
        JDialogSairDoAplicativo.setIconImage(new ImageIcon(MenuPrincipal.class.getResource("/imagens/icone_sair.png")).getImage());
        JDialogSairDoAplicativo.pack();


        framePrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        framePrincipal.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (menuPrincipal.getPainelMenuPrincipal().getParent() != null) {
                    JDialogSairDoAplicativo.setLocationRelativeTo(null);
                    JDialogSairDoAplicativo.setVisible(true);
                }
            }
        });

    }


    public void novoDiagrama() {
        /*
            Método responsável por realizar as configurações para criar um novo diagrama. O método remove do frame
            o painel do menu principal e coloca o painel da area de diagramas.
         */

        framePrincipal.setVisible(false);
        framePrincipal.remove(menuPrincipal.getPainelMenuPrincipal());
        framePrincipal.add(areaDeDiagramas.getPainelAreaDeDiagramas(), "north");
        framePrincipal.pack();
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setVisible(true);

        areaDeDiagramas.novoDiagrama();

    }

    public void abrirDiagrama() {
        /*
            Método responsável abrir um diagrama. O método primeiramente faz uma chamada para abrirDiagrama() da
            classe AreaDeDiagramas que retorna true caso o usuário tenha aberto com sucesso um diagrama e false
            caso contrário. Se o usário conseguiu abrir um diagrama o método realiza as configurações necessárias.
         */

        if (areaDeDiagramas.abrirDiagrama()) {
            framePrincipal.setVisible(false);
            framePrincipal.remove(menuPrincipal.getPainelMenuPrincipal());
            framePrincipal.add(areaDeDiagramas.getPainelAreaDeDiagramas(), "north");
            framePrincipal.pack();
            framePrincipal.setLocationRelativeTo(null);
            framePrincipal.setVisible(true);
        }

    }

    public void mostrarMenuPrincipal() {

        //O método remove do frame o painel da area de diagramas e coloca o painel do menu principal.

        framePrincipal.setVisible(false);
        framePrincipal.remove(areaDeDiagramas.getPainelAreaDeDiagramas());
        framePrincipal.add(menuPrincipal.getPainelMenuPrincipal(), "north");
        framePrincipal.setTitle("Editor de Diagramas UML");
        framePrincipal.pack();
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setVisible(true);
    }

    public void fecharAplicacao() {

        //Mostra na tela o JDialog que pergunta ao usuário se ele deseja realmente sair.

        JDialogSairDoAplicativo.setLocationRelativeTo(null);
        JDialogSairDoAplicativo.setVisible(true);
    }


    public void mostrarInterfaceGrafica(boolean mostrar) {

        // De acordo com o argumento mostra ou não o frame principal do aplicativo.

        framePrincipal.setVisible(mostrar);
    }

    public JFrame getFramePrincipal() {
        return framePrincipal;
    }
}

