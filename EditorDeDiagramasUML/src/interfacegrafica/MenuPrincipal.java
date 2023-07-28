package interfacegrafica;

import auxiliares.GerenciadorDeArquivos;
import DiagramaUML.DiagramaUML;
import net.miginfocom.swing.MigLayout;
import auxiliares.GerenciadorDeRecursos;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuPrincipal {
    private final JPanel painelMenuPrincipal;

    public MenuPrincipal(GerenciadorInterfaceGrafica gerenciadorInterfaceGrafica) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // Adicionando listener para quando o usuario tentar fechar a aplicacao
        gerenciadorInterfaceGrafica.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (gerenciadorInterfaceGrafica.estaMostrandoMenuPrincipal()) {
                    gerenciadorInterfaceGrafica.mostrarDialogFecharAplicacao();
                }
            }
        });

        painelMenuPrincipal = new JPanel();
        painelMenuPrincipal.setLayout(new MigLayout("insets 30 110 30 110"));
        painelMenuPrincipal.setBackground(gerenciadorDeRecursos.getColor("white"));

        // Criando e adicionado os componentes gráficos do menu ao painelMenuPrincipal

        JPanel painelLogoAplicativo = new JPanel(new MigLayout("fill, insets 60 55 60 55"));
        painelLogoAplicativo.setBackground(gerenciadorDeRecursos.getColor("black"));
        painelLogoAplicativo.setBorder(BorderFactory.createMatteBorder(
                0, 0, 0, 3, gerenciadorDeRecursos.getColor("bright_gray"))
        );

        JLabel logoAplicativo = new JLabel(gerenciadorDeRecursos.getImagem("app_logo_branco"));
        painelLogoAplicativo.add(logoAplicativo);

        // ----------------------------------------------------------------------------

        JPanel painelTituloAplicativo = new JPanel(new MigLayout("insets 15 30 0 30", "[grow, fill]"));
        painelTituloAplicativo.setBackground(gerenciadorDeRecursos.getColor("white"));

        JLabel labelTituloAplicativo = new JLabel(gerenciadorDeRecursos.getString("app_titulo_maiusculo"));
        labelTituloAplicativo.setFont(gerenciadorDeRecursos.getRobotoBlack(28));
        labelTituloAplicativo.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelFaixaTitulo = new JPanel();
        painelFaixaTitulo.setBorder(BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("black"))
        );
        painelFaixaTitulo.setOpaque(false);

        painelTituloAplicativo.add(labelTituloAplicativo, "wrap, gaptop 18");
        painelTituloAplicativo.add(painelFaixaTitulo, "align center");

        // ----------------------------------------------------------------------------

        JLabel labelImgNovoDiagrama = new JLabel(gerenciadorDeRecursos.getImagem("icone_sinal_mais"));

        JLabel labelNovoDiagrama = new JLabel(gerenciadorDeRecursos.getString("diagrama_novo"));
        labelNovoDiagrama.setFont(gerenciadorDeRecursos.getRobotoMedium(16));
        labelNovoDiagrama.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelNovoDiagrama = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelNovoDiagrama.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelNovoDiagrama.add(labelImgNovoDiagrama, "split 2, align center, gapright 5");
        painelNovoDiagrama.add(labelNovoDiagrama, "align center, grow");
        painelNovoDiagrama.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));
        painelNovoDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciadorInterfaceGrafica.mostrarAreaDeDiagramas(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelNovoDiagrama.setBackground(gerenciadorDeRecursos.getColor("light_gray"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelNovoDiagrama.setBackground(gerenciadorDeRecursos.getColor("white"));
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelImgAbrirDiagrama = new JLabel(gerenciadorDeRecursos.getImagem("icone_pasta_fechada"));

        JLabel labelAbrirDiagrama = new JLabel(gerenciadorDeRecursos.getString("diagrama_abrir"));
        labelAbrirDiagrama.setFont(gerenciadorDeRecursos.getRobotoMedium(16));
        labelAbrirDiagrama.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelAbrirDiagrama = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelAbrirDiagrama.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelAbrirDiagrama.add(labelImgAbrirDiagrama, "split 2, align center, gapright 5");
        painelAbrirDiagrama.add(labelAbrirDiagrama, "align center, grow");
        painelAbrirDiagrama.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));
        painelAbrirDiagrama.addMouseListener(new MouseAdapter() {
            final ImageIcon imgPastaAberta = gerenciadorDeRecursos.getImagem("icone_pasta_aberta");
            final ImageIcon imgPastaFechada = gerenciadorDeRecursos.getImagem("icone_pasta_fechada");

            @Override
            public void mouseClicked(MouseEvent e) {
                DiagramaUML diagrama = GerenciadorDeArquivos.getInstancia().abrirDiagrama(null);

                if (diagrama != null) {
                    gerenciadorInterfaceGrafica.mostrarAreaDeDiagramas(diagrama);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelAbrirDiagrama.setBackground(gerenciadorDeRecursos.getColor("light_gray"));
                labelImgAbrirDiagrama.setIcon(imgPastaAberta);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelAbrirDiagrama.setBackground(gerenciadorDeRecursos.getColor("white"));
                labelImgAbrirDiagrama.setIcon(imgPastaFechada);
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelSairAplicativo = new JLabel(gerenciadorDeRecursos.getString("sair"));
        labelSairAplicativo.setFont(gerenciadorDeRecursos.getRobotoMedium(16));
        labelSairAplicativo.setForeground(gerenciadorDeRecursos.getColor("white"));
        labelSairAplicativo.setHorizontalAlignment(JLabel.CENTER);

        JPanel painelSairAplicativo = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelSairAplicativo.setBackground(gerenciadorDeRecursos.getColor("raisin_black"));

        painelSairAplicativo.add(labelSairAplicativo, "align center, grow");
        painelSairAplicativo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciadorInterfaceGrafica.mostrarDialogFecharAplicacao();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                painelSairAplicativo.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                painelSairAplicativo.setBackground(gerenciadorDeRecursos.getColor("black"));
            }
        });

        // ----------------------------------------------------------------------------

        JPanel faixaInferiorMenu = new JPanel();
        faixaInferiorMenu.setBackground(gerenciadorDeRecursos.getColor("raisin_black"));

        // ----------------------------------------------------------------------------

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
