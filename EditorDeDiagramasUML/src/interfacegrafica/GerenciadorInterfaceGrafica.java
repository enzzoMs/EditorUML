package interfacegrafica;

import modelos.DiagramaUML;
import auxiliares.GerenciadorDeRecursos;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;

/**
 * Classe que representa a interface gráfica do aplicativo.
 * Através de seus métodos pode gerenciar o frame principal e decidir qual dos componentes gráficos devem ser
 * mostrados no momento.
 */

public class GerenciadorInterfaceGrafica {

    /**
     * Frame principal que contém toda a interface grafica do aplicativo. É gerenciado para exibir diferentes
     * componentes ao longo da execução.
     * @see MenuPrincipal
     * @see AreaDeDiagramas
     */
    private final JFrame framePrincipal;
    private final MenuPrincipal menuPrincipal;
    private final AreaDeDiagramas areaDeDiagramas;
    private boolean mostrandoMenuPrincipal = false;
    private boolean mostrandoAreaDeDiagramas = false;
    private final JDialog JDialogSairDoAplicativo = new JDialog();

    public GerenciadorInterfaceGrafica() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        framePrincipal = new JFrame();
        framePrincipal.setResizable(false);
        framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePrincipal.setLayout(new MigLayout());
        framePrincipal.setTitle(gerenciadorDeRecursos.getString("app_titulo"));
        framePrincipal.setIconImage(gerenciadorDeRecursos.getImagem("app_icone").getImage());
        framePrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        menuPrincipal = new MenuPrincipal(this);
        areaDeDiagramas = new AreaDeDiagramas(this);

        inicializarDialogSairDoAplicativo();
    }

    /**
     * @param diagrama O diagramaUML a ser mostrado pela area de diagramas.
     * Caso seja Null um novo diagrama será criado.
     */
    public void mostrarAreaDeDiagramas(DiagramaUML diagrama) {
        framePrincipal.setVisible(false);
        framePrincipal.remove(menuPrincipal.getPainelMenuPrincipal());
        framePrincipal.add(areaDeDiagramas.getPainelAreaDeDiagramas(), "north");
        framePrincipal.pack();
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setVisible(true);
        mostrandoAreaDeDiagramas = true;
        mostrandoMenuPrincipal = false;

        if (diagrama != null) {
            areaDeDiagramas.carregarDiagrama(diagrama);
        } else {
            areaDeDiagramas.novoDiagrama();
        }
    }

    public void mostrarMenuPrincipal() {
        framePrincipal.setVisible(false);
        setWindowTitle(GerenciadorDeRecursos.getInstancia().getString("app_titulo"));
        framePrincipal.remove(areaDeDiagramas.getPainelAreaDeDiagramas());
        framePrincipal.add(menuPrincipal.getPainelMenuPrincipal(), "north");
        framePrincipal.pack();
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setVisible(true);
        mostrandoMenuPrincipal = true;
        mostrandoAreaDeDiagramas = false;
    }

    public void mostrarInterfaceGrafica(boolean mostrar) {
        framePrincipal.setVisible(mostrar);
    }

    public void mostrarDialogFecharAplicacao() {
        JDialogSairDoAplicativo.setLocationRelativeTo(null);
        JDialogSairDoAplicativo.setVisible(true);
    }

    public void addWindowListener(WindowAdapter windowAdapter) {
        framePrincipal.addWindowListener(windowAdapter);
    }

    public void setWindowTitle(String titulo) {
        framePrincipal.setTitle(titulo);
    }

    public AreaDeDiagramas getAreaDeDiagramas() {
        return areaDeDiagramas;
    }

    public boolean estaMostrandoMenuPrincipal() {
        return mostrandoMenuPrincipal;
    }

    public boolean estaMostrandoAreaDeDiagramas() {
        return mostrandoAreaDeDiagramas;
    }

    private void inicializarDialogSairDoAplicativo() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // Criando e adicionado os componentes gráficos do dialog

        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 20 20 20 20"));
        painelImgInterrogacao.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));

        JLabel imgInterrogacao = new JLabel(gerenciadorDeRecursos.getImagem("icone_interrogacao_grande"));
        painelImgInterrogacao.add(imgInterrogacao, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelPerguntaSair = new JPanel(new MigLayout("insets 15 25 15 25"));
        painelPerguntaSair.setOpaque(false);
        painelPerguntaSair.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 2, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        JLabel labelPerguntaSair = new JLabel(gerenciadorDeRecursos.getString("sair_pergunta"));
        labelPerguntaSair.setFont(gerenciadorDeRecursos.getRobotoMedium(16));

        painelPerguntaSair.add(labelPerguntaSair, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelRespostaSim = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelRespostaSim.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaSim.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));

        JLabel labelRespostaSim = new JLabel(gerenciadorDeRecursos.getString("sim"));
        labelRespostaSim.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelRespostaSim.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaSim.add(labelRespostaSim, "align center");
        painelRespostaSim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelRespostaNao = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelRespostaNao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaNao.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));
        painelRespostaNao.setOpaque(true);

        JLabel labelRespostaNao = new JLabel(gerenciadorDeRecursos.getString("nao"));
        labelRespostaNao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelRespostaNao.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaNao.add(labelRespostaNao, "align center");
        painelRespostaNao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialogSairDoAplicativo.setVisible(false);
            }
        });

        // ----------------------------------------------------------------------------

        MouseAdapter mouseAdapterPaineisReposta = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // O componente de index 0 eh o JLabel "labelRespostaNao"
                ((JPanel) e.getSource()).setBackground(gerenciadorDeRecursos.getColor("raisin_black"));
                ((JPanel) e.getSource()).getComponent(0).setForeground(gerenciadorDeRecursos.getColor("white"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // O componente de index 0 eh o JLabel "labelRespostaSim"
                ((JPanel) e.getSource()).setBackground(gerenciadorDeRecursos.getColor("white"));
                ((JPanel) e.getSource()).getComponent(0).setForeground(gerenciadorDeRecursos.getColor("black"));
            }
        };

        painelRespostaNao.addMouseListener(mouseAdapterPaineisReposta);
        painelRespostaSim.addMouseListener(mouseAdapterPaineisReposta);

        // ----------------------------------------------------------------------------

        JPanel painelConfirmacao = new JPanel(new MigLayout("fill, insets 25 40 30 40"));
        painelConfirmacao.setBackground(Color.white);
        painelConfirmacao.add(painelPerguntaSair, "north");
        painelConfirmacao.add(painelRespostaSim, "wrap, align center, grow, gapbottom 5");
        painelConfirmacao.add(painelRespostaNao, "align center, grow");

        JPanel painelPrincipalJDialogSair = new JPanel(new MigLayout("fill, insets 0 15 0 15"));
        painelPrincipalJDialogSair.add(painelImgInterrogacao, "north");
        painelPrincipalJDialogSair.add(painelConfirmacao, "south");

        JDialogSairDoAplicativo.setTitle(gerenciadorDeRecursos.getString("sair"));
        JDialogSairDoAplicativo.setContentPane(painelPrincipalJDialogSair);
        JDialogSairDoAplicativo.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JDialogSairDoAplicativo.setResizable(false);
        JDialogSairDoAplicativo.setIconImage(gerenciadorDeRecursos.getImagem("icone_sair").getImage());
        JDialogSairDoAplicativo.pack();
    }
}

