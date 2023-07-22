package InterfaceGrafica;

import ClassesAuxiliares.RobotoFont;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal {
    /*
        Classe que representa a interface gráfica do menu principal do aplicativo. Possui como atributo principal o
        painelMenuPrincipal que possui todos os componentes gráficos do menu.
     */

    private final JPanel painelMenuPrincipal;

    public MenuPrincipal(InterfaceGraficaUML interfaceGrafica) {

        // Criando e Configurando o painelMenuPrincipal --------------------------------------------------------------

        painelMenuPrincipal = new JPanel();
        painelMenuPrincipal.setLayout(new MigLayout("insets 30 70 30 70"));
        painelMenuPrincipal.setBackground(Color.white);

        // Criando e adicionado os componentes gráficos do menu ao painelMenuPrincipal -------------------------------

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelTituloAplicativo = new JPanel(new MigLayout("insets 30 25 0 25", "[grow, fill]"));
        painelTituloAplicativo.setBackground(Color.white);

        JLabel labelTituloAplicativo = new JLabel("EDITOR DE DIAGRAMAS UML");
        labelTituloAplicativo.setFont(robotoFont.getRobotoBlack(20));
        labelTituloAplicativo.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelFaixaTitulo = new JPanel();
        painelFaixaTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
        painelFaixaTitulo.setOpaque(false);

        painelTituloAplicativo.add(labelTituloAplicativo, "wrap, gaptop 18");
        painelTituloAplicativo.add(painelFaixaTitulo, "align center");

        JPanel painelLogoAplicativo = new JPanel(new MigLayout("fill, insets 60 80 60 80"));
        painelLogoAplicativo.setBackground(Color.black);
        painelLogoAplicativo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(0xeaeaea)));

        JLabel logoAplicativo = new JLabel(new ImageIcon(MenuPrincipal.class.getResource("/imagens/logo/app_logo_branco.png")));

        painelLogoAplicativo.add(logoAplicativo);



        JLabel labelImgNovoDiagrama = new JLabel(new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_novo.png")));

        JLabel labelNovoDiagrama = new JLabel("Novo Diagrama");
        labelNovoDiagrama.setFont(robotoFont.getRobotoMedium(15));
        labelNovoDiagrama.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelNovoDiagrama = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelNovoDiagrama.setBackground(Color.white);
        painelNovoDiagrama.add(labelImgNovoDiagrama, "split 2, align center, gapright 5");
        painelNovoDiagrama.add(labelNovoDiagrama, "align center, grow");
        painelNovoDiagrama.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));
        painelNovoDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                interfaceGrafica.novoDiagrama();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelNovoDiagrama.setBackground(new Color(0xebebeb));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelNovoDiagrama.setBackground(Color.white);
            }
        });


        JLabel labelImgAbrirDiagrama = new JLabel(new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_pasta_fechada.png")));

        JLabel labelAbrirDiagrama = new JLabel("Abrir Diagrama");
        labelAbrirDiagrama.setFont(robotoFont.getRobotoMedium(15));
        labelAbrirDiagrama.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelAbrirDiagrama = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelAbrirDiagrama.setBackground(Color.white);
        painelAbrirDiagrama.add(labelImgAbrirDiagrama, "split 2, align center, gapright 5");
        painelAbrirDiagrama.add(labelAbrirDiagrama, "align center, grow");
        painelAbrirDiagrama.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));
        painelAbrirDiagrama.addMouseListener(new MouseAdapter() {
            final ImageIcon imgPastaAberta = new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_pasta_aberta.png"));
            final ImageIcon imgPastaFechada = new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_pasta_fechada.png"));

            @Override
            public void mouseClicked(MouseEvent e) {
                interfaceGrafica.abrirDiagrama();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelAbrirDiagrama.setBackground(new Color(0xebebeb));

                labelImgAbrirDiagrama.setIcon(imgPastaAberta);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelAbrirDiagrama.setBackground(Color.white);

                labelImgAbrirDiagrama.setIcon(imgPastaFechada);
            }
        });



        JLabel labelSairAplicativo = new JLabel("Sair");
        labelSairAplicativo.setFont(robotoFont.getRobotoBlack(15));
        labelSairAplicativo.setForeground(Color.white);
        labelSairAplicativo.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelSairAplicativo = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelSairAplicativo.setBackground(new Color(0x282626));

        painelSairAplicativo.add(labelSairAplicativo, "align center, grow");
        painelSairAplicativo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                interfaceGrafica.fecharAplicacao();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelSairAplicativo.setBackground(new Color(0x303030));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelSairAplicativo.setBackground(Color.black);
            }
        });


        JPanel faixaInferiorMenu = new JPanel();
        faixaInferiorMenu.setBackground(new Color(0x212121));

        // ------------------------------------------------------------------------------------------------------------

        painelMenuPrincipal.add(faixaInferiorMenu, "south");
        painelMenuPrincipal.add(painelLogoAplicativo, "west");
        painelMenuPrincipal.add(painelTituloAplicativo, "north");
        painelMenuPrincipal.add(painelNovoDiagrama, "wrap, grow, gapbottom 8");
        painelMenuPrincipal.add(painelAbrirDiagrama, "grow, wrap, gapbottom 8");
        painelMenuPrincipal.add(painelSairAplicativo, "grow");
    }


    public JPanel getPainelMenuPrincipal() {
        return painelMenuPrincipal;
    }
}
