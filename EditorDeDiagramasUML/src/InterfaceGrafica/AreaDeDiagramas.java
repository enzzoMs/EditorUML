package InterfaceGrafica;

import AlteracoesDeElementos.*;
import ClassesAuxiliares.*;
import ComponentesUML.*;
import DiagramaUML.DiagramaUML;
import RelacoesUML.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AreaDeDiagramas {
    /*
        Classe que representa a tela de montagem e modificação dos diagramas, ficando resonsável por gerenciar a adição,
        remoção e modificação de componentes no quadro branco e no objeto do diagrama.
     */

    // Atributos referentes a aspectos gráficos da area de diagramas.
    private final JPanel painelAreaDeDiagramas;
    private final JPanel painelQuadroBranco;
    private final JPanel painelOpcoesQuadroBranco;

    // O diagrama que está sendo modificado pelo usuário.
    private DiagramaUML diagramaUMLatual;

    /*
        Os dois atributos abaixo servem para regular as ações do usário na area de diagramas, sendo que quando algum deles
        for true certas ações não poderão ser feitas pelo usuário.
     */

    private boolean selecaoDeRelacionamentoAcontecendo;
    private boolean movimentacaoPermitida;

    /*
        ArrayList contendo os objetos de alteração de componente que serão usados para os recursos de desfazer e refazer
        alteração no diagrama. O indexAlteracao indica a posição atual nessa lista de acordo com as ações do usuário,
        sendo que quando o usuário desfaz uma ação o index volta uma posição, e se refizer avança uma posição, dessa forma
        é possivel saber a partir desse index qual objeto dessa lista precisa ser manipulado.
     */
    private final ArrayList<AlteracaoDeElementosUML> listaDeAlteracoes = new ArrayList<>();

    private int indexAlteracao = -1;


    private final GerenciadorDeArquivos gerenciadorArquivos = new GerenciadorDeArquivos(this);
    private final InterfaceGraficaUML interfaceGraficaUML;

    public AreaDeDiagramas(InterfaceGraficaUML interfaceGraficaUML) {
        // Criando e configurando o painelAreaDeDiagramas -------------------------------------------------------------

        this.painelAreaDeDiagramas = new JPanel();
        this.painelAreaDeDiagramas.setLayout(new MigLayout());
        this.interfaceGraficaUML = interfaceGraficaUML;

        // Criando e adicionado os componentes gráficos ao painelAreaDeDiagramas -------------------------------------

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelDiretorioSalvo = new JPanel(new MigLayout("insets 0 15 13 15, fill"));
        painelDiretorioSalvo.setBackground(Color.black);
        painelDiretorioSalvo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0xe5e5e5)));

        JPanel painelLabelDiretorio = new JPanel(new MigLayout("insets 5 8 5 8", "[grow, fill][]"));
        painelLabelDiretorio.setBackground(new Color(0x1d2021));

        JLabel labelDiretorio = new JLabel("* O Diagrama ainda não foi salvo");
        labelDiretorio.setFont(robotoFont.getRobotoMedium(13f));
        labelDiretorio.setForeground(Color.white);

        JLabel labelDiretorioNaoSalvo = new JLabel("(* O Diagrama possui alterações não salvas)");
        labelDiretorioNaoSalvo.setFont(robotoFont.getRobotoMedium(13f));
        labelDiretorioNaoSalvo.setForeground(Color.white);
        labelDiretorioNaoSalvo.setVisible(false);

        painelLabelDiretorio.add(labelDiretorio);
        painelLabelDiretorio.add(labelDiretorioNaoSalvo);

        painelDiretorioSalvo.add(painelLabelDiretorio, "growx");

        // ------------------------------------------------------------------------------------------------------------

        JPanel menuDiagramas = new JPanel(new MigLayout("insets 10 15 10 15", "[sizegroup main][sizegroup main]" +
                "[sizegroup main][sizegroup main][sizegroup main][sizegroup main][]"));
        menuDiagramas.setBackground(Color.black);
        menuDiagramas.setOpaque(true);


        Border bordaDesativada = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        Border bordaAtiva = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Color.white),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );


        JLabel novoDiagrama = new JLabel("Novo Diagrama", JLabel.CENTER);
        novoDiagrama.setFont(robotoFont.getRobotoBlack(13f));
        novoDiagrama.setForeground(Color.white);
        novoDiagrama.setOpaque(true);
        novoDiagrama.setBorder(bordaDesativada);
        novoDiagrama.setBackground(new Color(0x1d2021));
        novoDiagrama.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_novo_diagrama.png")));
        novoDiagrama.setVerticalTextPosition(JLabel.BOTTOM);
        novoDiagrama.setHorizontalTextPosition(JLabel.CENTER);

        JLabel salvarDiagrama = new JLabel("Salvar", JLabel.CENTER);
        salvarDiagrama.setFont(robotoFont.getRobotoBlack(13));
        salvarDiagrama.setForeground(Color.white);
        salvarDiagrama.setOpaque(true);
        salvarDiagrama.setBorder(bordaDesativada);
        salvarDiagrama.setBackground(new Color(0x1d2021));
        salvarDiagrama.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_salvar.png")));
        salvarDiagrama.setVerticalTextPosition(JLabel.BOTTOM);
        salvarDiagrama.setHorizontalTextPosition(JLabel.CENTER);

        JLabel salvarDiagramaComo = new JLabel("Salvar Como", JLabel.CENTER);
        salvarDiagramaComo.setFont(robotoFont.getRobotoBlack(13));
        salvarDiagramaComo.setForeground(Color.white);
        salvarDiagramaComo.setOpaque(true);
        salvarDiagramaComo.setBorder(bordaDesativada);
        salvarDiagramaComo.setBackground(new Color(0x1d2021));
        salvarDiagramaComo.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_salvar_como.png")));
        salvarDiagramaComo.setVerticalTextPosition(JLabel.BOTTOM);
        salvarDiagramaComo.setHorizontalTextPosition(JLabel.CENTER);

        JLabel abrirDiagrama = new JLabel("Abrir Diagrama", JLabel.CENTER);
        abrirDiagrama.setFont(robotoFont.getRobotoBlack(13));
        abrirDiagrama.setForeground(Color.white);
        abrirDiagrama.setOpaque(true);
        abrirDiagrama.setBorder(bordaDesativada);
        abrirDiagrama.setBackground(new Color(0x1d2021));
        abrirDiagrama.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_abrir_diagrama.png")));
        abrirDiagrama.setVerticalTextPosition(JLabel.BOTTOM);
        abrirDiagrama.setHorizontalTextPosition(JLabel.CENTER);

        JLabel exportarDiagrama = new JLabel("Exportar Diagrama", JLabel.CENTER);
        exportarDiagrama.setFont(robotoFont.getRobotoBlack(13f));
        exportarDiagrama.setForeground(Color.white);
        exportarDiagrama.setOpaque(true);
        exportarDiagrama.setBorder(bordaDesativada);
        exportarDiagrama.setBackground(new Color(0x1d2021));
        exportarDiagrama.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_exportar.png")));
        exportarDiagrama.setVerticalTextPosition(JLabel.BOTTOM);
        exportarDiagrama.setHorizontalTextPosition(JLabel.CENTER);

        JLabel separador  = new JLabel("Separar", JLabel.CENTER);
        separador.setFont(robotoFont.getRobotoBlack(13f));
        separador.setForeground(new Color(0x1d2021));
        separador.setOpaque(false);
        separador.setBorder(bordaDesativada);
        separador.setBackground(new Color(0x1d2021));

        JLabel voltarDiagrama = new JLabel("Voltar", JLabel.CENTER);
        voltarDiagrama.setFont(robotoFont.getRobotoBlack(13f));
        voltarDiagrama.setForeground(Color.white);
        voltarDiagrama.setOpaque(true);
        voltarDiagrama.setBorder(bordaDesativada);
        voltarDiagrama.setBackground(new Color(0x1d2021));
        voltarDiagrama.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_voltar.png")));
        voltarDiagrama.setVerticalTextPosition(JLabel.BOTTOM);
        voltarDiagrama.setHorizontalTextPosition(JLabel.CENTER);

        // ------------------------------------------------------------------------------------------------------------


        JDialog jDialogVoltar = new JDialog();
        jDialogVoltar.setTitle("Salvar Alterações");
        jDialogVoltar.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialogVoltar.setResizable(false);


        JPanel painelExcluirComponente = new JPanel(new MigLayout("insets 10 0 15 0"));
        painelExcluirComponente.setBackground(Color.white);

        JLabel labelImgInterrogacao = new JLabel(new ImageIcon(MenuPrincipal.class.getResource("/imagens/img_interrogacao_pequena.png")));
        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgInterrogacao.setBackground(new Color(0x1d2021));
        painelImgInterrogacao.add(labelImgInterrogacao, "align center");

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);


        JLabel labelMensagem = new JLabel();
        labelMensagem.setText("Deseja salvar as alterações em NovoDiagrama?");
        labelMensagem.setFont(robotoFont.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 25, 0, 25),
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)
        ));

        painelMensagem.add(labelMensagem, "align center");


        JPanel painelRespostaSalvar = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaSalvar.setBackground(Color.white);
        painelRespostaSalvar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaSalvar = new JLabel("Salvar");
        labelRespostaSalvar.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaSalvar.setForeground(Color.black);


        JPanel painelRespostaNaoSalvar = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaNaoSalvar.setBackground(Color.white);
        painelRespostaNaoSalvar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaNaoSalvar = new JLabel("Não Salvar");
        labelRespostaNaoSalvar.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaNaoSalvar.setForeground(Color.black);


        JPanel painelRespostaCancelar = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaCancelar.setBackground(Color.white);
        painelRespostaCancelar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaCancelar = new JLabel("Cancelar");
        labelRespostaCancelar.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaCancelar.setForeground(Color.black);

        int[] destinoDoJDialog = { 0 };
        // 0 = mostrar menu principal , 1 = area de diagramas, 2 = sair

        painelRespostaSalvar.add(labelRespostaSalvar, "align center");
        painelRespostaSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gerenciadorArquivos.getArquivoDiagrama() == null) {
                    gerenciadorArquivos.salvarDiagramaComo(diagramaUMLatual);
                } else {
                    gerenciadorArquivos.salvarDiagrama(diagramaUMLatual);
                }

                if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                    labelDiretorioNaoSalvo.setVisible(false);

                    if (destinoDoJDialog[0] == 0) {
                        interfaceGraficaUML.mostrarMenuPrincipal();

                        resetarAreaDeDiagramas();

                        voltarDiagrama.setBorder(bordaDesativada);
                    } else if (destinoDoJDialog[0] == 1) {
                        novoDiagrama();
                    } else if (destinoDoJDialog[0] == 2) {
                        System.exit(0);
                    } else {
                        labelDiretorio.setText("Diagrama salvo em:     " + gerenciadorArquivos.getArquivoDiagrama().getAbsolutePath());
                        interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - " + gerenciadorArquivos.getArquivoDiagrama().getName());
                    }
                }
            }
        });

        painelRespostaNaoSalvar.add(labelRespostaNaoSalvar, "align center");
        painelRespostaNaoSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (destinoDoJDialog[0] == 0) {
                    interfaceGraficaUML.mostrarMenuPrincipal();

                    labelDiretorioNaoSalvo.setVisible(false);

                    resetarAreaDeDiagramas();

                    voltarDiagrama.setBorder(bordaDesativada);
                } else if (destinoDoJDialog[0] == 1) {
                    labelDiretorioNaoSalvo.setVisible(false);

                    novoDiagrama();
                } else if (destinoDoJDialog[0] == 2) {
                    System.exit(0);
                }
            }
        });

        painelRespostaCancelar.add(labelRespostaCancelar, "align center");


        painelExcluirComponente.add(painelImgInterrogacao, "west");
        painelExcluirComponente.add(painelMensagem, "wrap");
        painelExcluirComponente.add(painelRespostaSalvar, "gaptop 15, split 3, gapleft: push, gapright: push");
        painelExcluirComponente.add(painelRespostaNaoSalvar, "gaptop 15, gapleft: push, gapright: push");
        painelExcluirComponente.add(painelRespostaCancelar, "gaptop 15, gapleft: push, gapright: push");

        jDialogVoltar.setContentPane(painelExcluirComponente);


        MouseAdapter mouseAdapter = new MouseAdapter() {
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

            @Override
            public void mouseClicked(MouseEvent e) {
                jDialogVoltar.setVisible(false);
            }
        };

        painelRespostaSalvar.addMouseListener(mouseAdapter);
        painelRespostaNaoSalvar.addMouseListener(mouseAdapter);
        painelRespostaCancelar.addMouseListener(mouseAdapter);


        // ===========================================================================================================

        salvarDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gerenciadorArquivos.getArquivoDiagrama() == null) {
                    gerenciadorArquivos.salvarDiagramaComo(diagramaUMLatual);

                    if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                        labelDiretorio.setText("Diagrama salvo em:     " + gerenciadorArquivos.getArquivoDiagrama().getAbsolutePath());
                    }
                } else {
                    gerenciadorArquivos.salvarDiagrama(diagramaUMLatual);
                }

                if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                    labelDiretorioNaoSalvo.setVisible(false);
                    AreaDeDiagramas.this.interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - " + gerenciadorArquivos.getArquivoDiagrama().getName());
                }
            }
        });


        voltarDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (diagramaUMLatual.isDiagramaSalvo()) {
                    interfaceGraficaUML.mostrarMenuPrincipal();
                } else {
                    if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                        labelMensagem.setText("Deseja salvar as alterações em " +
                                gerenciadorArquivos.getArquivoDiagrama().getName().substring(0,
                                        gerenciadorArquivos.getArquivoDiagrama().getName().lastIndexOf(".txt")) + "?");
                    }

                    destinoDoJDialog[0] = 0;

                    jDialogVoltar.pack();
                    jDialogVoltar.setLocationRelativeTo(null);
                    jDialogVoltar.setVisible(true);
                }
            }
        });

        novoDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (diagramaUMLatual.isDiagramaSalvo()) {
                    novoDiagrama();
                } else {
                    if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                        labelMensagem.setText("Deseja salvar as alterações em " +
                                gerenciadorArquivos.getArquivoDiagrama().getName().substring(0,
                                        gerenciadorArquivos.getArquivoDiagrama().getName().lastIndexOf(".txt")) + "?");
                    }

                    destinoDoJDialog[0] = 1;

                    jDialogVoltar.pack();
                    jDialogVoltar.setLocationRelativeTo(null);
                    jDialogVoltar.setVisible(true);
                }
            }
        });

        salvarDiagramaComo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gerenciadorArquivos.salvarDiagramaComo(diagramaUMLatual)) {
                    labelDiretorio.setText("Diagrama salvo em:     " + gerenciadorArquivos.getArquivoDiagrama().getAbsolutePath());

                    labelDiretorioNaoSalvo.setVisible(false);
                    AreaDeDiagramas.this.interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - " + gerenciadorArquivos.getArquivoDiagrama().getName());
                }
            }
        });

        abrirDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!diagramaUMLatual.isDiagramaSalvo()) {
                    if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                        labelMensagem.setText("Deseja salvar as alterações em " +
                                gerenciadorArquivos.getArquivoDiagrama().getName().substring(0,
                                        gerenciadorArquivos.getArquivoDiagrama().getName().lastIndexOf(".txt")) + "?");
                    }

                    destinoDoJDialog[0] = 3;

                    jDialogVoltar.pack();
                    jDialogVoltar.setLocationRelativeTo(null);
                    jDialogVoltar.setVisible(true);
                }

                DiagramaUML novoDiagramaUML = gerenciadorArquivos.abrirDiagrama();

                if (novoDiagramaUML != null) {
                    resetarAreaDeDiagramas();

                    diagramaUMLatual = novoDiagramaUML;

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        addComponenteAoQuadro(componenteUML, false);
                    }

                    labelDiretorioNaoSalvo.setVisible(false);
                    labelDiretorio.setText("Diagrama salvo em:     " + gerenciadorArquivos.getArquivoDiagrama().getAbsolutePath());
                    AreaDeDiagramas.this.interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - " + gerenciadorArquivos.getArquivoDiagrama().getName());
                }
            }
        });

        exportarDiagrama.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel painelCopiaQuadroBranco = new JPanel();
                painelCopiaQuadroBranco.setBackground(Color.white);

                int MARGEM = 50;

                int menorPontoX = painelQuadroBranco.getWidth();
                int menorPontoY = painelQuadroBranco.getHeight();

                int maiorPontoX = 0;
                int maiorPontoY = 0;

                for (Component componente : painelQuadroBranco.getComponents()) {
                    menorPontoX = Math.min(componente.getX(), menorPontoX);
                    menorPontoY = Math.min(componente.getY(), menorPontoY);

                    maiorPontoX = Math.max(componente.getX() + componente.getWidth(), maiorPontoX);
                    maiorPontoY = Math.max(componente.getY() + componente.getHeight(), maiorPontoY);
                }

                for (Component componente : painelQuadroBranco.getComponents()) {
                    painelCopiaQuadroBranco.add(componente);
                    componente.setLocation(
                            componente.getX() - menorPontoX + MARGEM,
                            componente.getY() - menorPontoY + MARGEM
                    );
                }

                painelCopiaQuadroBranco.setSize(new Dimension((maiorPontoX - menorPontoX) + MARGEM*2, (maiorPontoY - menorPontoY) + MARGEM*2));
                gerenciadorArquivos.exportarDiagrama(painelCopiaQuadroBranco);


                for (Component componente : painelCopiaQuadroBranco.getComponents()) {
                    painelQuadroBranco.add(componente);
                    componente.setLocation(
                            componente.getX() + menorPontoX - MARGEM,
                            componente.getY() + menorPontoY - MARGEM
                    );
                }

                painelQuadroBranco.revalidate();
                painelQuadroBranco.repaint();

            }
        });


        AreaDeDiagramas.this.interfaceGraficaUML.getFramePrincipal().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (painelAreaDeDiagramas.getParent() != null) {
                    if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                        labelMensagem.setText("Deseja salvar as alterações em " +
                                gerenciadorArquivos.getArquivoDiagrama().getName().substring(0,
                                        gerenciadorArquivos.getArquivoDiagrama().getName().lastIndexOf(".txt")) + "?");
                    }

                    destinoDoJDialog[0] = 2;

                    jDialogVoltar.pack();
                    jDialogVoltar.setLocationRelativeTo(null);
                    jDialogVoltar.setVisible(true);
                }
            }
        });

        // -----------------------------------------------------------------------------------------------------------


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();

                source.setBorder(bordaAtiva);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();

                source.setBorder(bordaDesativada);
            }
        };

        novoDiagrama.addMouseListener(mouseAdapter);
        salvarDiagrama.addMouseListener(mouseAdapter);
        salvarDiagramaComo.addMouseListener(mouseAdapter);
        abrirDiagrama.addMouseListener(mouseAdapter);
        exportarDiagrama.addMouseListener(mouseAdapter);
        voltarDiagrama.addMouseListener(mouseAdapter);


        menuDiagramas.add(novoDiagrama, "grow");
        menuDiagramas.add(salvarDiagrama, "gapleft 10, grow");
        menuDiagramas.add(salvarDiagramaComo, "gapleft 10, grow");
        menuDiagramas.add(abrirDiagrama, "gapleft 10, grow");
        menuDiagramas.add(exportarDiagrama, "gapleft 10, grow");
        menuDiagramas.add(separador, "gapleft 10, grow");
        menuDiagramas.add(voltarDiagrama, "gapleft 80, grow");


        // ===========================================================================================================


        painelOpcoesQuadroBranco = new JPanel(new MigLayout("insets 5 10 5 10"));
        painelOpcoesQuadroBranco.setBackground(Color.white);

        JLabel labelCursor = new JLabel();
        labelCursor.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_cursor.png")));
        labelCursor.setOpaque(true);
        labelCursor.setBackground(new Color(0xc7c7c7));
        labelCursor.setToolTipText("Modificar Componente(s)");

        JLabel labelMover = new JLabel();
        labelMover.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_mover.png")));
        labelMover.setOpaque(true);
        labelMover.setBackground(Color.white);
        labelMover.setToolTipText("Movimentar Quadro");

        JLabel labelUndo = new JLabel();
        labelUndo.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_undo_ativado.png")));
        labelUndo.setDisabledIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_undo_desativado.png")));
        labelUndo.setOpaque(true);
        labelUndo.setBackground(new Color(0xf2f2f2));
        labelUndo.setEnabled(false);
        labelUndo.setToolTipText("Desfazer");

        JLabel labelRedo = new JLabel();
        labelRedo.setIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_redo_ativado.png")));
        labelRedo.setDisabledIcon(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_redo_desativado.png")));
        labelRedo.setOpaque(true);
        labelRedo.setBackground(new Color(0xf2f2f2));
        labelRedo.setEnabled(false);
        labelRedo.setToolTipText("Refazer");


        JLabel labelSeparadorOpcoes1 = new JLabel("");
        labelSeparadorOpcoes1.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xe5e5e5)),
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xc2c2c2))));


        painelOpcoesQuadroBranco.add(labelCursor, "gapright 5");
        painelOpcoesQuadroBranco.add(labelMover, "gapright 5");
        painelOpcoesQuadroBranco.add(labelSeparadorOpcoes1, "grow, gapright 5");
        painelOpcoesQuadroBranco.add(labelUndo, "gapright 5");
        painelOpcoesQuadroBranco.add(labelRedo, "gapright 5");


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelCursor.setBackground(new Color(0xc7c7c7));
                labelMover.setBackground(Color.white);

                movimentacaoPermitida = false;

                painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (movimentacaoPermitida) {
                    labelCursor.setBackground(new Color(0xe5e5e5));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (movimentacaoPermitida) {
                    labelCursor.setBackground(Color.white);
                }
            }
        };

        labelCursor.addMouseListener(mouseAdapter);

        mouseAdapter = new MouseAdapter() {
            boolean selecionado = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                labelMover.setBackground(new Color(0xc7c7c7));
                labelCursor.setBackground(Color.white);

                movimentacaoPermitida = true;

                painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!movimentacaoPermitida) {
                    labelMover.setBackground(new Color(0xe5e5e5));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!movimentacaoPermitida) {
                    labelMover.setBackground(Color.white);
                }
            }

        };

        labelMover.addMouseListener(mouseAdapter);


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (labelUndo.isEnabled()) {
                    listaDeAlteracoes.get(indexAlteracao).desfazerAlteracao();
                    indexAlteracao--;

                    if (indexAlteracao == -1) {
                        labelUndo.setEnabled(false);
                        labelUndo.setBackground(new Color(0xf2f2f2));
                    }

                    if (!labelRedo.isEnabled()) {
                        labelRedo.setEnabled(true);
                        labelRedo.setBackground(Color.white);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (labelUndo.isEnabled()) {
                    labelUndo.setBackground(new Color(0xe5e5e5));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (labelUndo.isEnabled()) {
                    labelUndo.setBackground(Color.white);
                }
            }

        };

        labelUndo.addMouseListener(mouseAdapter);


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (labelRedo.isEnabled()) {
                    indexAlteracao++;
                    listaDeAlteracoes.get(indexAlteracao).refazerAlteracao();

                    if (indexAlteracao == listaDeAlteracoes.size() - 1) {
                        labelRedo.setEnabled(false);
                        labelRedo.setBackground(new Color(0xf2f2f2));
                    }

                    if (!labelUndo.isEnabled()) {
                        labelUndo.setBackground(Color.white);
                        labelUndo.setEnabled(true);
                    }

                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (labelRedo.isEnabled()) {
                    labelRedo.setBackground(new Color(0xe5e5e5));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (labelRedo.isEnabled()) {
                    labelRedo.setBackground(Color.white);
                }
            }

        };

        labelRedo.addMouseListener(mouseAdapter);



        // Configurando o painel lateral com o componentes para os diagramas ========================================

        JPanel menuComponentesDiagramas = new JPanel(new MigLayout("insets 15 15 15 15"));
        menuComponentesDiagramas.setBackground(Color.white);
        menuComponentesDiagramas.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(0xe5e5e5)));


        JLabel labelNovaClasse = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_classe.png")), JLabel.CENTER);
        labelNovaClasse.setOpaque(true);
        labelNovaClasse.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelNovaClasse.setBackground(new Color(0xe6e6e6));

        labelNovaClasse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    ClasseUML novaClasse = new ClasseUML(diagramaUMLatual);
                    diagramaUMLatual.addComponente(novaClasse);

                    AreaDeDiagramas.this.addAlteracao(new ComponenteCriado(
                            novaClasse.getPainelComponente().getX(),
                            novaClasse.getPainelComponente().getY(),
                            novaClasse));
                }
            }
        });


        JLabel labelNovaAnotacao = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_anotacao.png")), JLabel.CENTER);
        labelNovaAnotacao.setOpaque(true);
        labelNovaAnotacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelNovaAnotacao.setBackground(new Color(0xe6e6e6));

        labelNovaAnotacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    AnotacaoUML novaAnotacao = new AnotacaoUML(diagramaUMLatual);
                    diagramaUMLatual.addComponente(novaAnotacao);

                    AreaDeDiagramas.this.addAlteracao(new ComponenteCriado(
                            novaAnotacao.getPainelComponente().getX(),
                            novaAnotacao.getPainelComponente().getY(),
                            novaAnotacao));
                }
            }
        });


        JLabel labelnovoPacote = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_novo_pacote.png")), JLabel.CENTER);
        labelnovoPacote.setOpaque(true);
        labelnovoPacote.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelnovoPacote.setBackground(new Color(0xe6e6e6));

        labelnovoPacote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    PacoteUML novoPacote = new PacoteUML(diagramaUMLatual);
                    diagramaUMLatual.addComponente(novoPacote);


                    AreaDeDiagramas.this.addAlteracao(new ComponenteCriado(
                            novoPacote.getPainelComponente().getX(),
                            novoPacote.getPainelComponente().getY(),
                            novoPacote));

                }
            }
        });


        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        };

        labelNovaClasse.addMouseListener(mouseAdapter);
        labelNovaAnotacao.addMouseListener(mouseAdapter);
        labelnovoPacote.addMouseListener(mouseAdapter);




        JLabel labelRelacaoGeneralizacao = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_generalizacao.png")));
        labelRelacaoGeneralizacao.setFont(robotoFont.getRobotoMedium(12));
        labelRelacaoGeneralizacao.setText("Generalização");
        labelRelacaoGeneralizacao.setVerticalTextPosition(JLabel.BOTTOM);
        labelRelacaoGeneralizacao.setHorizontalTextPosition(JLabel.CENTER);
        labelRelacaoGeneralizacao.setOpaque(true);
        labelRelacaoGeneralizacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelRelacaoGeneralizacao.setBackground(new Color(0xe6e6e6));


        JLabel labelRelacaoRealizacao = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_realizacao.png")));
        labelRelacaoRealizacao.setFont(robotoFont.getRobotoMedium(12));
        labelRelacaoRealizacao.setText("Realização");
        labelRelacaoRealizacao.setVerticalTextPosition(JLabel.BOTTOM);
        labelRelacaoRealizacao.setHorizontalTextPosition(JLabel.CENTER);
        labelRelacaoRealizacao.setOpaque(true);
        labelRelacaoRealizacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelRelacaoRealizacao.setBackground(new Color(0xe6e6e6));


        JLabel labelRelacaoDependencia = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_dependencia.png")));
        labelRelacaoDependencia.setFont(robotoFont.getRobotoMedium(12));
        labelRelacaoDependencia.setText("Dependência");
        labelRelacaoDependencia.setVerticalTextPosition(JLabel.BOTTOM);
        labelRelacaoDependencia.setHorizontalTextPosition(JLabel.CENTER);
        labelRelacaoDependencia.setOpaque(true);
        labelRelacaoDependencia.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelRelacaoDependencia.setBackground(new Color(0xe6e6e6));

        JLabel labelAssociacaoSimples = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_associacao.png")));
        labelAssociacaoSimples.setFont(robotoFont.getRobotoMedium(12));
        labelAssociacaoSimples.setText("Associação");
        labelAssociacaoSimples.setVerticalTextPosition(JLabel.BOTTOM);
        labelAssociacaoSimples.setHorizontalTextPosition(JLabel.CENTER);
        labelAssociacaoSimples.setOpaque(true);
        labelAssociacaoSimples.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelAssociacaoSimples.setBackground(new Color(0xe6e6e6));


        JLabel labelAgregacao = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_agregacao.png")));
        labelAgregacao.setFont(robotoFont.getRobotoMedium(12));
        labelAgregacao.setText("Agregação");
        labelAgregacao.setVerticalTextPosition(JLabel.BOTTOM);
        labelAgregacao.setHorizontalTextPosition(JLabel.CENTER);
        labelAgregacao.setOpaque(true);
        labelAgregacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelAgregacao.setBackground(new Color(0xe6e6e6));


        JLabel labelComposicao = new JLabel(new ImageIcon(AreaDeDiagramas.class.getResource("/imagens/img_nova_composicao.png")));
        labelComposicao.setFont(robotoFont.getRobotoMedium(12));
        labelComposicao.setText("Composição");
        labelComposicao.setVerticalTextPosition(JLabel.BOTTOM);
        labelComposicao.setHorizontalTextPosition(JLabel.CENTER);
        labelComposicao.setOpaque(true);
        labelComposicao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelComposicao.setBackground(new Color(0xe6e6e6));




        JPanel painelErroRelacao = new JPanel(new MigLayout("fill, insets 10 15 15 15"));
        painelErroRelacao.setBackground(Color.white);
        painelErroRelacao.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0x404040)));
        painelErroRelacao.setVisible(false);

        JLabel labelErro = new JLabel("     ERRO", JLabel.CENTER);
        labelErro.setFont(robotoFont.getRobotoMedium(16));
        labelErro.setForeground(Color.white);

        JLabel labelFechar = new JLabel("  X  ", JLabel.CENTER);
        labelFechar.setFont(robotoFont.getRobotoMedium(16));
        labelFechar.setForeground(Color.white);
        labelFechar.setBackground(new Color(0x545454));
        labelFechar.setOpaque(true);
        labelFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                painelErroRelacao.setVisible(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                labelFechar.setBackground(new Color(0xc42b1c));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelFechar.setBackground(new Color(0x545454));
            }
        });

        JPanel painelErro = new JPanel(new MigLayout("fill, insets 5 5 5 5"));
        painelErro.add(labelErro, "align center, grow");
        painelErro.add(labelFechar, "east");
        painelErro.setBackground(new Color(0x323232));

        JLabel labelDescricaoErro1 = new JLabel("Um erro ocorreu durante a criação da relação." , JLabel.CENTER);
        labelDescricaoErro1.setFont(robotoFont.getRobotoMedium(13));

        JLabel labelDescricaoErro2 = new JLabel("Selecione outros pontos ou tente novamente", JLabel.CENTER);
        labelDescricaoErro2.setFont(robotoFont.getRobotoMedium(13));

        JLabel labelDescricaoErro3 = new JLabel("com os componentes em posições diferentes.", JLabel.CENTER);
        labelDescricaoErro3.setFont(robotoFont.getRobotoMedium(13));

        painelErroRelacao.add(painelErro, "north");
        painelErroRelacao.add(labelDescricaoErro1, "align center, wrap");
        painelErroRelacao.add(labelDescricaoErro2, "align center, wrap");
        painelErroRelacao.add(labelDescricaoErro3, "align center");





        JPanel painelCancelarRelacionamento = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelCancelarRelacionamento.setBackground(Color.white);
        painelCancelarRelacionamento.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));
        painelCancelarRelacionamento.setVisible(false);

        JLabel cancelarRelacionamento = new JLabel("Cancelar");
        cancelarRelacionamento.setFont(robotoFont.getRobotoMedium(15));
        cancelarRelacionamento.setForeground(Color.black);

        painelCancelarRelacionamento.add(cancelarRelacionamento, "align center");


        JPanel painelCriarRelacionamento = new JPanel(new MigLayout("fill, insets 5 10 5 10"));
        painelCriarRelacionamento.setBackground(Color.white);
        painelCriarRelacionamento.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));
        painelCriarRelacionamento.setVisible(false);

        JLabel labelCriarRelacionamento = new JLabel("Finalizar");
        labelCriarRelacionamento.setFont(robotoFont.getRobotoMedium(15));
        labelCriarRelacionamento.setForeground(Color.black);

        painelCriarRelacionamento.add(labelCriarRelacionamento, "align center");

        painelCriarRelacionamento.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                diagramaUMLatual.getListaRelacaoes().get(diagramaUMLatual.getListaRelacaoes().size() - 1).colocarEstiloFinal();
                diagramaUMLatual.getListaRelacaoes().get(diagramaUMLatual.getListaRelacaoes().size() - 1).adicionarComportamentoArelacao();

                selecaoDeRelacionamentoAcontecendo = false;

                RelacaoUML ultimaRelacao = diagramaUMLatual.getListaRelacaoes().get(diagramaUMLatual.getListaRelacaoes().size() - 1);

                addAlteracao(new RelacaoModificada((ArrayList<JComponent>) ultimaRelacao.getListaPaineisRelacao().clone(), RelacaoModificada.TipoDaModificao.CRIADA, ultimaRelacao));

                ComponenteUML componenteOrigem = ultimaRelacao.getComponenteOrigem();
                ComponenteUML componenteDestino = ultimaRelacao.getComponenteDestino();

                componenteOrigem.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoOrigem().ordinal()).setBackground(Color.red);

                componenteDestino.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoDestino().ordinal()).setBackground(Color.red);

                for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                    componenteUML.mostrarAreasDeConexao(false);
                }

                labelRelacaoGeneralizacao.setBackground(new Color(0xe6e6e6));
                labelRelacaoRealizacao.setBackground(new Color(0xe6e6e6));
                labelRelacaoDependencia.setBackground(new Color(0xe6e6e6));
                labelAssociacaoSimples.setBackground(new Color(0xe6e6e6));
                labelAgregacao.setBackground(new Color(0xe6e6e6));
                labelComposicao.setBackground(new Color(0xe6e6e6));

                painelCriarRelacionamento.setVisible(false);
                painelCancelarRelacionamento.setVisible(false);
            }

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
        });


        painelCancelarRelacionamento.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecaoDeRelacionamentoAcontecendo = false;

                RelacaoUML ultimaRelacao = diagramaUMLatual.getListaRelacaoes().get(diagramaUMLatual.getListaRelacaoes().size() - 1);


                diagramaUMLatual.getListaRelacaoes().remove(ultimaRelacao);
                removerRelacaoDoQuadro(ultimaRelacao);

                ComponenteUML componenteOrigem = ultimaRelacao.getComponenteOrigem();
                ComponenteUML componenteDestino = ultimaRelacao.getComponenteDestino();

                if (componenteOrigem != null) {
                    componenteOrigem.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoOrigem().ordinal()).setBackground(Color.red);
                }

                if (componenteDestino != null) {
                    componenteDestino.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoDestino().ordinal()).setBackground(Color.red);
                }

                for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                    componenteUML.mostrarAreasDeConexao(false);
                }


                labelRelacaoGeneralizacao.setBackground(new Color(0xe6e6e6));
                labelRelacaoRealizacao.setBackground(new Color(0xe6e6e6));
                labelRelacaoDependencia.setBackground(new Color(0xe6e6e6));
                labelAssociacaoSimples.setBackground(new Color(0xe6e6e6));
                labelAgregacao.setBackground(new Color(0xe6e6e6));
                labelComposicao.setBackground(new Color(0xe6e6e6));

                painelCriarRelacionamento.setVisible(false);
                painelCancelarRelacionamento.setVisible(false);
            }

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
        });





        labelRelacaoGeneralizacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoGeneralizacao(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });


        labelRelacaoRealizacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoRealizacao(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });

        labelRelacaoDependencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoDependencia(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });

        labelAssociacaoSimples.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoAssociacaoSimples(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });

        labelAgregacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoAgregacao(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });


        labelComposicao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    diagramaUMLatual.getListaRelacaoes().add(new RelacaoComposicao(diagramaUMLatual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                        componenteUML.mostrarAreasDeConexao(true);
                    }

                    painelCancelarRelacionamento.setVisible(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xcccccc));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selecaoDeRelacionamentoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();

                    source.setBackground(new Color(0xe6e6e6));
                }
            }
        });



        CompoundBorder bordaSeparadores = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xe5e5e5)),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2)));

        JLabel labelSeparador1 = new JLabel("");
        labelSeparador1.setBorder(bordaSeparadores);

        JLabel labelSeparador2 = new JLabel("");
        labelSeparador2.setBorder(bordaSeparadores);

        JLabel labelSeparador3 = new JLabel("");
        labelSeparador3.setBorder(bordaSeparadores);

        JLabel labelSeparador4 = new JLabel("");
        labelSeparador4.setBorder(bordaSeparadores);

        JLabel labelSeparador5 = new JLabel("");
        labelSeparador5.setBorder(bordaSeparadores);

        JLabel labelSeparador6 = new JLabel("");
        labelSeparador6.setBorder(bordaSeparadores);

        JLabel labelSeparador7 = new JLabel("");
        labelSeparador7.setBorder(bordaSeparadores);

        JLabel labelSeparador8 = new JLabel("");
        labelSeparador8.setBorder(bordaSeparadores);

        menuComponentesDiagramas.add(labelNovaClasse, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador1, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelNovaAnotacao, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador2, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelnovoPacote, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador3, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelRelacaoGeneralizacao, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador4, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelRelacaoRealizacao, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador5, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelRelacaoDependencia, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador6, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelAssociacaoSimples, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador7, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelAgregacao, "wrap, gapbottom 5, growx");
        menuComponentesDiagramas.add(labelSeparador8, "wrap, grow, gapbottom 5, growx");

        menuComponentesDiagramas.add(labelComposicao, "wrap, growx");

        // ==========================================================================================================

        // ======================================================================================

        painelQuadroBranco = new JPanel(null);
        painelQuadroBranco.setPreferredSize(new Dimension(5000,5000));
        painelQuadroBranco.setBackground(new Color(0xdfdfdf));

        JScrollPane scrollPaneDiagramas = new JScrollPane(painelQuadroBranco);
        scrollPaneDiagramas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDiagramas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        Rectangle retanguloView = scrollPaneDiagramas.getViewport().getViewRect();

        retanguloView.x += 2500 - retanguloView.x/2;
        retanguloView.y += 2500 - retanguloView.y/2;

        painelQuadroBranco.scrollRectToVisible(retanguloView);


        MouseAdapter mouse = new MouseAdapter() {
            int mouseX, mouseY;
            Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                origin = new Point(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, painelQuadroBranco);
                Rectangle view = viewPort.getViewRect();

                int LIMITE_ESQUERDO_X = view.x;
                int LIMITE_DIREITO_X = view.x + view.width;

                int LIMITE_SUPERIOR_Y = view.y;
                int LIMITE_INFERIOR_Y = view.y + view.height;

                double x = e.getPoint().getX();
                double y = e.getPoint().getY();

                if (movimentacaoPermitida && x > LIMITE_ESQUERDO_X && x < LIMITE_DIREITO_X && y > LIMITE_SUPERIOR_Y && y < LIMITE_INFERIOR_Y ) {
                    //System.out.println(scrollPaneDiagramas.getViewport().getView().getX());

                    int deltaX = origin.x - e.getX();
                    int deltaY = origin.y - e.getY();

                    view.x += deltaX;
                    view.y += deltaY;

                    painelQuadroBranco.scrollRectToVisible(view);
                }
            }
        };
        painelQuadroBranco.setAutoscrolls(true);
        painelQuadroBranco.addMouseListener(mouse);
        painelQuadroBranco.addMouseMotionListener(mouse);

        // ============================================================================================================


        JLayeredPane painelCamadasQuadroBranco = new JLayeredPane();
        painelCamadasQuadroBranco.setOpaque(false);

        // mudar hard code e add magic number



        painelCamadasQuadroBranco.add(scrollPaneDiagramas, JLayeredPane.DEFAULT_LAYER);
        scrollPaneDiagramas.setBounds(0, 0,
                menuDiagramas.getPreferredSize().width - menuComponentesDiagramas.getPreferredSize().width,
                500);

        painelCamadasQuadroBranco.add(painelOpcoesQuadroBranco, JLayeredPane.PALETTE_LAYER);
        painelOpcoesQuadroBranco.setBounds((scrollPaneDiagramas.getWidth()/2)
                        - (painelOpcoesQuadroBranco.getPreferredSize().width/2)-menuComponentesDiagramas.getPreferredSize().width/4,
                8, painelOpcoesQuadroBranco.getPreferredSize().width,
                painelOpcoesQuadroBranco.getPreferredSize().height);

        painelCamadasQuadroBranco.add(painelCancelarRelacionamento, JLayeredPane.PALETTE_LAYER);
        painelCancelarRelacionamento.setBounds(
                15,
                painelOpcoesQuadroBranco.getBounds().y,
                painelCancelarRelacionamento.getPreferredSize().width,
                painelCancelarRelacionamento.getPreferredSize().height);


        painelCamadasQuadroBranco.add(painelCriarRelacionamento, JLayeredPane.PALETTE_LAYER);
        painelCriarRelacionamento.setBounds(
                15,
                painelCancelarRelacionamento.getY() + painelCancelarRelacionamento.getHeight() + 15,
                painelCriarRelacionamento.getPreferredSize().width,
                painelCriarRelacionamento.getPreferredSize().height);


        painelCamadasQuadroBranco.add(painelErroRelacao, JLayeredPane.PALETTE_LAYER);
        painelErroRelacao.setBounds(
                30,
                500 - painelErroRelacao.getPreferredSize().height - 30,
                painelErroRelacao.getPreferredSize().width,
                painelErroRelacao.getPreferredSize().height);

        // ==============================================================================

        // mudar hard code

        JScrollPane scrollPaneMenuDiagramas = new JScrollPane(menuComponentesDiagramas);
        scrollPaneMenuDiagramas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        painelAreaDeDiagramas.add(menuDiagramas, "north");

        painelAreaDeDiagramas.add(painelDiretorioSalvo, "north");

        painelAreaDeDiagramas.add(scrollPaneMenuDiagramas, "west, height 500");
        painelAreaDeDiagramas.add(painelCamadasQuadroBranco, "grow");

    }


    public void novoDiagrama() {
        resetarAreaDeDiagramas();

        if (diagramaUMLatual != null) {
            for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                this.removerComponenteDoQuadro(componenteUML);
            }
        }

        this.diagramaUMLatual = new DiagramaUML(this);
        this.interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - NovoDiagrama");

        ((JLabel) ((JPanel) ((JPanel) painelAreaDeDiagramas.getComponent(1)).getComponent(0))
                .getComponent(0)).setText("* O Diagrama ainda não foi salvo");

        gerenciadorArquivos.novoDiagrama();




    }

    public boolean abrirDiagrama() {
        DiagramaUML novoDiagramaUML = gerenciadorArquivos.abrirDiagrama();

        if (novoDiagramaUML != null) {
            diagramaUMLatual = novoDiagramaUML;

            for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                addComponenteAoQuadro(componenteUML, false);
            }

            JLabel labelDiretorio = ((JLabel) ((JPanel) ((JPanel) painelAreaDeDiagramas.getComponent(1)).getComponent(0))
                    .getComponent(0));

            labelDiretorio.setText("Diagrama salvo em:     " + gerenciadorArquivos.getArquivoDiagrama().getAbsolutePath());
            interfaceGraficaUML.getFramePrincipal().setTitle("Editor de Diagramas UML - " + gerenciadorArquivos.getArquivoDiagrama().getName());

            return true;
        }

        return false;
    }

    public void addComponenteAoQuadro(ComponenteUML componente, boolean centralizar) {
        if (!(componente instanceof PacoteUML)) {
            painelQuadroBranco.add(componente.getPainelComponente(), 0);
        } else {
            painelQuadroBranco.add(componente.getPainelComponente());
        }

        if (centralizar) {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, painelQuadroBranco);
            Rectangle view = viewPort.getViewRect();

            int LARGURA_QUADRO_BRANCO = view.width;
            int ALTURA_QUADRO_BRANCO = view.height;


            Point posicaoDoNovoComponente = ((JViewport) painelQuadroBranco.getParent()).getViewPosition();
            posicaoDoNovoComponente.move(posicaoDoNovoComponente.x + LARGURA_QUADRO_BRANCO/2 - componente.getLargura()/2 - componente.getLargura()/5 ,
                    posicaoDoNovoComponente.y + ALTURA_QUADRO_BRANCO/2 - componente.getAltura()/2);

            componente.getPainelComponente().setLocation(posicaoDoNovoComponente);
        }


        painelQuadroBranco.revalidate();
        painelQuadroBranco.repaint();

    }


    public void removerComponenteDoQuadro(ComponenteUML componente) {
        this.painelQuadroBranco.remove(componente.getPainelComponente());
        this.painelQuadroBranco.revalidate();
        this.painelQuadroBranco.repaint();
    }

    public void removerRelacaoDoQuadro(RelacaoUML relacaoGeneralizacao) {
        for (JComponent componenteDeRelacao : relacaoGeneralizacao.getListaPaineisRelacao()) {
            this.painelQuadroBranco.remove(componenteDeRelacao);
        }

        this.painelQuadroBranco.revalidate();
        this.painelQuadroBranco.repaint();
    }

    public void desfazerAcao() {
    }

    public void refazerAcao() {
    }

    public JPanel getPainelAreaDeDiagramas() {
        return this.painelAreaDeDiagramas;
    }


    public boolean isMovimentacaoPermitida() {
        return movimentacaoPermitida;
    }


    public void addRelacionametoAoQuadro(ArrayList<JComponent> listaPaineisRelacionamento) {
        for (JComponent componenteDeRelacionamento : listaPaineisRelacionamento) {
            painelQuadroBranco.add(componenteDeRelacionamento);
        }

        painelQuadroBranco.revalidate();
        painelQuadroBranco.repaint();
    }

    public boolean isSelecaoDeRelacionamentoAcontecendo() {
        return selecaoDeRelacionamentoAcontecendo;
    }

    public void setSelecaoDeRelacionamentoAcontecendo(boolean selecaoDeRelacionamentoAcontecendo) {
        this.selecaoDeRelacionamentoAcontecendo = selecaoDeRelacionamentoAcontecendo;
    }

    public void addAlteracao(AlteracaoDeElementosUML novaAlteracao) {
        if (diagramaUMLatual.isDiagramaSalvo()) {
            interfaceGraficaUML.getFramePrincipal().setTitle(interfaceGraficaUML.getFramePrincipal().getTitle() + "*");

            if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                ((JPanel) ((JPanel) painelAreaDeDiagramas.getComponent(1)).getComponent(0)).getComponent(1).setVisible(true);
            }

            diagramaUMLatual.setDiagramaSalvo(false);
        }


        JLabel labelUndo = (JLabel) painelOpcoesQuadroBranco.getComponent(3);
        JLabel labelRedo = (JLabel) painelOpcoesQuadroBranco.getComponent(4);


        if (listaDeAlteracoes.size() >= indexAlteracao + 1) {
            listaDeAlteracoes.subList(indexAlteracao + 1, listaDeAlteracoes.size()).clear();
            labelRedo.setEnabled(false);
            labelRedo.setBackground(new Color(0xf2f2f2));
        }


        int LIMITE_DE_ALTERACOES = 25;

        if (listaDeAlteracoes.size() >= LIMITE_DE_ALTERACOES) {
            listaDeAlteracoes.remove(0);
            listaDeAlteracoes.add(novaAlteracao);
        } else {
            listaDeAlteracoes.add(novaAlteracao);
            indexAlteracao++;
        }


        if (!labelUndo.isEnabled()) {
            labelUndo.setBackground(Color.white);
            labelUndo.setEnabled(true);
        }

    }

    private void resetarAreaDeDiagramas() {
        listaDeAlteracoes.clear();
        indexAlteracao = -1;

        if (diagramaUMLatual != null) {
            for (ComponenteUML componenteUML : diagramaUMLatual.getListaComponentesUML()) {
                removerComponenteDoQuadro(componenteUML);
            }

            for (RelacaoUML relacaoUML : diagramaUMLatual.getListaRelacaoes()) {
                removerRelacaoDoQuadro(relacaoUML);
            }
        }

        selecaoDeRelacionamentoAcontecendo = false;

        // cursor e mover
        painelOpcoesQuadroBranco.getComponent(0).setBackground(new Color(0xc7c7c7));
        painelOpcoesQuadroBranco.getComponent(1).setBackground(Color.white);

        //undo e redo
        painelOpcoesQuadroBranco.getComponent(2).setBackground(new Color(0xf2f2f2));
        painelOpcoesQuadroBranco.getComponent(2).setEnabled(false);

        painelOpcoesQuadroBranco.getComponent(3).setBackground(new Color(0xf2f2f2));
        painelOpcoesQuadroBranco.getComponent(3).setEnabled(false);


        movimentacaoPermitida = false;

        painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));



    }

    public JPanel getPainelOpcoesQuadroBranco() {
        return painelOpcoesQuadroBranco;
    }

    public JPanel getPainelQuadroBranco() {
        return painelQuadroBranco;
    }
}
