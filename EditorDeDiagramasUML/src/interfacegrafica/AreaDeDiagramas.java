package interfacegrafica;

import AlteracoesDeElementos.*;
import ClassesAuxiliares.*;
import ComponentesUML.*;
import DiagramaUML.DiagramaUML;
import RelacoesUML.*;
import auxiliares.GerenciadorDeArquivos;
import auxiliares.GerenciadorDeRecursos;
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

/**
 * Classe que representa a tela de montagem e modificação dos diagramas, ficando resonsável por gerenciar a adição,
 * remoção e modificação de componentes no quadro branco.
 */
public class AreaDeDiagramas {

    // TODO: arrumar esses atributos e ver comentarios em metodos
    private final JPanel painelAreaDeDiagramas;
    private final JPanel painelQuadroBranco;
    private final JPanel painelOpcoesQuadroBranco;

    // O diagrama que está sendo modificado pelo usuário.
    private DiagramaUML diagramaAtual;

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


    // TODO: se possivel tentar remover isso
    private final GerenciadorInterfaceGrafica gerenciadorInterfaceGrafica;

    private final JDialog dialogSalvarAlteracoes = new JDialog();
    private final JPanel painelDiretorioDiagrama = new JPanel();

    public AreaDeDiagramas(GerenciadorInterfaceGrafica gerenciadorInterfaceGrafica) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        painelAreaDeDiagramas = new JPanel();
        painelAreaDeDiagramas.setLayout(new MigLayout());
        this.gerenciadorInterfaceGrafica = gerenciadorInterfaceGrafica;

        painelOpcoesQuadroBranco = new JPanel(new MigLayout("insets 5 10 5 10"));
        painelOpcoesQuadroBranco.setBackground(gerenciadorDeRecursos.getColor("white"));

        // Criando e adicionado os componentes graficos ao painelAreaDeDiagramas

        JPanel menuDeOpcoes = getPainelMenuDeOpcoes();
        JPanel acoesQuadroBranco = getPainelAcoesQuadroBranco();

        // ----------------------------------------------------------------------------

        inicializarDialogSalvarAlteracoes();

        this.gerenciadorInterfaceGrafica.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (gerenciadorInterfaceGrafica.estaMostrandoAreaDeDiagramas()) {
                    if (!diagramaAtual.isDiagramaSalvo() && diagramaAtual.arquivoDiagrama != null) {
                        mostrarDialogSalvarAlteracoes();
                    } else {
                        gerenciadorInterfaceGrafica.mostrarDialogFecharAplicacao();
                    }
                }
            }
        });

        // ----------------------------------------------------------------------------

        RobotoFont robotoFont = new RobotoFont();
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
                    ClasseUML novaClasse = new ClasseUML(diagramaAtual);
                    diagramaAtual.addComponente(novaClasse);

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
                    AnotacaoUML novaAnotacao = new AnotacaoUML(diagramaAtual);
                    diagramaAtual.addComponente(novaAnotacao);

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
                    PacoteUML novoPacote = new PacoteUML(diagramaAtual);
                    diagramaAtual.addComponente(novoPacote);


                    AreaDeDiagramas.this.addAlteracao(new ComponenteCriado(
                            novoPacote.getPainelComponente().getX(),
                            novoPacote.getPainelComponente().getY(),
                            novoPacote));

                }
            }
        });


        MouseAdapter mouseAdapter = new MouseAdapter() {
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
                diagramaAtual.getListaRelacaoes().get(diagramaAtual.getListaRelacaoes().size() - 1).colocarEstiloFinal();
                diagramaAtual.getListaRelacaoes().get(diagramaAtual.getListaRelacaoes().size() - 1).adicionarComportamentoArelacao();

                selecaoDeRelacionamentoAcontecendo = false;

                RelacaoUML ultimaRelacao = diagramaAtual.getListaRelacaoes().get(diagramaAtual.getListaRelacaoes().size() - 1);

                addAlteracao(new RelacaoModificada((ArrayList<JComponent>) ultimaRelacao.getListaPaineisRelacao().clone(), RelacaoModificada.TipoDaModificao.CRIADA, ultimaRelacao));

                ComponenteUML componenteOrigem = ultimaRelacao.getComponenteOrigem();
                ComponenteUML componenteDestino = ultimaRelacao.getComponenteDestino();

                componenteOrigem.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoOrigem().ordinal()).setBackground(Color.red);

                componenteDestino.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoDestino().ordinal()).setBackground(Color.red);

                for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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

                RelacaoUML ultimaRelacao = diagramaAtual.getListaRelacaoes().get(diagramaAtual.getListaRelacaoes().size() - 1);


                diagramaAtual.getListaRelacaoes().remove(ultimaRelacao);
                removerRelacaoDoQuadro(ultimaRelacao);

                ComponenteUML componenteOrigem = ultimaRelacao.getComponenteOrigem();
                ComponenteUML componenteDestino = ultimaRelacao.getComponenteDestino();

                if (componenteOrigem != null) {
                    componenteOrigem.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoOrigem().ordinal()).setBackground(Color.red);
                }

                if (componenteDestino != null) {
                    componenteDestino.getListaAreasDeConexao().get(ultimaRelacao.getAreaDeConexaoDestino().ordinal()).setBackground(Color.red);
                }

                for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoGeneralizacao(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoRealizacao(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoDependencia(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoAssociacaoSimples(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoAgregacao(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                    diagramaAtual.getListaRelacaoes().add(new RelacaoComposicao(diagramaAtual));
                    setSelecaoDeRelacionamentoAcontecendo(true);

                    for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
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
                menuDeOpcoes.getPreferredSize().width - menuComponentesDiagramas.getPreferredSize().width,
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

        painelAreaDeDiagramas.add(menuDeOpcoes, "north");

        //painelAreaDeDiagramas.add(painelDiretorioSalvo, "north");

        painelAreaDeDiagramas.add(scrollPaneMenuDiagramas, "west, height 500");
        painelAreaDeDiagramas.add(painelCamadasQuadroBranco, "grow");

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
        /*
        if (diagramaAtual.isDiagramaSalvo()) {
            gerenciadorInterfaceGrafica.getFramePrincipal().setTitle(gerenciadorInterfaceGrafica.getFramePrincipal().getTitle() + "*");

            if (gerenciadorArquivos.getArquivoDiagrama() != null) {
                ((JPanel) ((JPanel) painelAreaDeDiagramas.getComponent(1)).getComponent(0)).getComponent(1).setVisible(true);
            }

            diagramaAtual.setDiagramaSalvo(false);
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
        }*/

    }

    private void resetarAreaDeDiagramas() {
        if (diagramaAtual != null) {
            for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
                this.removerComponenteDoQuadro(componenteUML);
            }
        }

        listaDeAlteracoes.clear();
        indexAlteracao = -1;

        if (diagramaAtual != null) {
            for (ComponenteUML componenteUML : diagramaAtual.getListaComponentesUML()) {
                removerComponenteDoQuadro(componenteUML);
            }

            for (RelacaoUML relacaoUML : diagramaAtual.getListaRelacaoes()) {
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

    public void novoDiagrama() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // TODO: talvez retirar isso porque carregarDiagrama ja deve fazer
        //resetarAreaDeDiagramas();

        diagramaAtual = new DiagramaUML(this);
        gerenciadorInterfaceGrafica.setWindowTitle(gerenciadorDeRecursos.getString("area_diagramas_titulo_default"));

        setLabelDiretorioDiagrama(null);
    }


    private void salvarDiagrama(boolean salvarComo) {
        GerenciadorDeArquivos gerenciadorDeArquivos = GerenciadorDeArquivos.getInstancia();
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        if (salvarComo || diagramaAtual.arquivoDiagrama == null) {
            gerenciadorDeArquivos.salvarDiagramaComo(diagramaAtual);
        } else {
            gerenciadorDeArquivos.salvarDiagrama(diagramaAtual);
        }

        if (diagramaAtual.isDiagramaSalvo()) {
            setLabelDiretorioDiagrama(diagramaAtual.arquivoDiagrama.getAbsolutePath());

            setVisibilidadeDiagramaNaoSalvo(false);

            gerenciadorInterfaceGrafica.setWindowTitle(
                gerenciadorDeRecursos.getString("app_titulo") + " - " + diagramaAtual.arquivoDiagrama.getName()
            );
        }
    }

    public void carregarDiagrama(DiagramaUML diagrama) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        diagramaAtual = diagrama;

        // TODO: adicionar ResetarArea

        // TODO: mudar isso
        for (ComponenteUML componenteUML : diagrama.getListaComponentesUML()) {
            addComponenteAoQuadro(componenteUML, false);
        }

        setLabelDiretorioDiagrama(diagramaAtual.arquivoDiagrama.getAbsolutePath());

        gerenciadorInterfaceGrafica.setWindowTitle(
            gerenciadorDeRecursos.getString("app_titulo") + " - " + diagramaAtual.arquivoDiagrama.getName()
        );
    }


    /**
     * Exporta os componentes do quadro branco para uma imagem ".png".
     */
    private void exportarDiagrama() {
        // Para que que seja transformado em uma imagem é necessario fornecer o painel com todos os componentes
        // do diagrama. Para isso sera feito primeiramente uma copia do painelQuadroBranco

        JPanel painelCopiaQuadroBranco = new JPanel();
        painelCopiaQuadroBranco.setBackground(GerenciadorDeRecursos.getInstancia().getColor("white"));

        // O painelQuadroBranco tem um tamanho muito grande, mas a maior parte dele provalvemente vai estar vazia.
        // Os componentes do diagrama vao estar concentrados em uma area pequena, entao o que vamos exportar como
        // imagem é so essa parte e nao o painel inteiro.
        // Para descobrir qual é essa parte primeiro encontramos o maior e menor (X, Y) ocupado por um componente,
        // essas coordenadas vao delimitar a area a ser exportada.

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

        // Uma margem para adicionar um espaco ao redor do diagrama copiado (de modo que os componentes nao fiquem
        // encostados nas bordas da imagem)
        int MARGEM_MINIMA = 50;

        // Adicionada os componentes de painelQuadroBranco na sua copia,
        // sendo que cada um vai ter sua posicao normalizada com relacao ao menor (X, Y) encontrado,
        // de modo que eles fiquem contidos dentro da area a ser exportada
        for (Component componente : painelQuadroBranco.getComponents()) {
            painelCopiaQuadroBranco.add(componente);
            componente.setLocation(
                componente.getX() - menorPontoX + MARGEM_MINIMA,
                componente.getY() - menorPontoY + MARGEM_MINIMA
            );
        }

        // Define o tamanho do painel a ser exportado como a area que contem os componentes + margem (* 2 para
        // adicionar margem em ambos os lados: direita e esquerda, cima e baixo)
        painelCopiaQuadroBranco.setSize(new Dimension(
            (maiorPontoX - menorPontoX) + MARGEM_MINIMA * 2,
            (maiorPontoY - menorPontoY) + MARGEM_MINIMA * 2)
        );
        GerenciadorDeArquivos.getInstancia().exportarDiagrama(painelCopiaQuadroBranco, diagramaAtual.arquivoDiagrama);

        // Agora que o diagrama foi exportado, retorna os componentes para suas posicoes originais
        // no painelQuadroBranco
        for (Component componente : painelCopiaQuadroBranco.getComponents()) {
            painelQuadroBranco.add(componente);
            componente.setLocation(
                componente.getX() + menorPontoX - MARGEM_MINIMA,
                componente.getY() + menorPontoY - MARGEM_MINIMA
            );
        }

        painelQuadroBranco.revalidate();
        painelQuadroBranco.repaint();
    }

    private void voltar() {
        if (!diagramaAtual.isDiagramaSalvo() && diagramaAtual.arquivoDiagrama != null) {
            if (mostrarDialogSalvarAlteracoes()) {
                gerenciadorInterfaceGrafica.mostrarMenuPrincipal();
            }
        } else {
            gerenciadorInterfaceGrafica.mostrarMenuPrincipal();
        }
    }

    private void setLabelDiretorioDiagrama(String diretorio) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        String textoDiretorio;

        if (diretorio == null) {
            textoDiretorio = gerenciadorDeRecursos.getString("diagrama_nao_salvo");
        } else {
            textoDiretorio = gerenciadorDeRecursos.getString("diagrama_salvo_em") + diretorio;
        }

        ((JLabel) painelDiretorioDiagrama.getComponent(0)).setText(textoDiretorio);
    }

    private void setVisibilidadeDiagramaNaoSalvo(boolean visivel) {
        // O componente de index 1 do painelDiretorioDiagrama é um JLabel de texto:
        // "(* O Diagrama possui alterações não salvas)"

        painelDiretorioDiagrama.getComponent(1).setVisible(visivel);
    }

    /**
     * @return False caso "Cancelar" tenho sido escolhido. True caso contrário.
     */
    private boolean mostrarDialogSalvarAlteracoes() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        String nomeArquivo;

        if (diagramaAtual.arquivoDiagrama != null) {
            nomeArquivo = diagramaAtual.arquivoDiagrama.getName().replace(".txt", "");
        } else {
            nomeArquivo = gerenciadorDeRecursos.getString("diagrama_nome_default");
        }

        JLabel labelMensagem = (JLabel) ((JPanel) dialogSalvarAlteracoes.getContentPane().getComponent(1)).getComponent(0);

        labelMensagem.setText(
            gerenciadorDeRecursos.getString("salvar_alteracoes_em") + nomeArquivo + "?"
        );

        dialogSalvarAlteracoes.pack();
        dialogSalvarAlteracoes.setLocationRelativeTo(null);
        dialogSalvarAlteracoes.setVisible(true);

        JPanel painelSalvar = (JPanel) dialogSalvarAlteracoes.getContentPane().getComponent(2);
        JPanel painelNaoSalvar = (JPanel) dialogSalvarAlteracoes.getContentPane().getComponent(3);
        Color white = gerenciadorDeRecursos.getColor("white");

        // Se os paineis tiverem a cor branca entao eles nao foram escolhidos
        return !painelSalvar.getBackground().equals(white) || !painelNaoSalvar.getBackground().equals(white);
    }

    private JPanel getPainelMenuDeOpcoes() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JPanel menuDiagramas = new JPanel(new MigLayout(
            "insets 10 15 10 15", "[sizegroup main][sizegroup main]" +
            "[sizegroup main][sizegroup main][sizegroup main][sizegroup main][]"
        ));
        menuDiagramas.setBackground(gerenciadorDeRecursos.getColor("black"));
        menuDiagramas.setOpaque(true);

        // ----------------------------------------------------------------------------

        // Essas bordas sao usadas por todas as opcoes do menu
        // Borda ativa significa a borda quando o mouse esta sobre a opcao

        Border bordaDesativada = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, gerenciadorDeRecursos.getColor("black")),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        Border bordaAtiva = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, gerenciadorDeRecursos.getColor("white")),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // ----------------------------------------------------------------------------

        String[] textoOpcoes = {
            gerenciadorDeRecursos.getString("diagrama_novo"),
            gerenciadorDeRecursos.getString("salvar"),
            gerenciadorDeRecursos.getString("salvar_como"),
            gerenciadorDeRecursos.getString("diagrama_abrir"),
            gerenciadorDeRecursos.getString("diagrama_exportar"),
            gerenciadorDeRecursos.getString("voltar"),
        };

        ImageIcon[] iconesOpcoes = {
            gerenciadorDeRecursos.getImagem("menu_novo_arquivo"),
            gerenciadorDeRecursos.getImagem("menu_salvar"),
            gerenciadorDeRecursos.getImagem("menu_salvar_como"),
            gerenciadorDeRecursos.getImagem("menu_abrir"),
            gerenciadorDeRecursos.getImagem("menu_exportar"),
            gerenciadorDeRecursos.getImagem("menu_voltar")
        };

        JLabel[] opcoes = new JLabel[textoOpcoes.length];

        for (int i = 0; i < opcoes.length; i++) {
            JLabel opcao = new JLabel(textoOpcoes[i], JLabel.CENTER);
            opcao.setFont(gerenciadorDeRecursos.getRobotoBlack(13));
            opcao.setForeground(gerenciadorDeRecursos.getColor("white"));
            opcao.setOpaque(true);
            opcao.setBorder(bordaDesativada);
            opcao.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));
            opcao.setIcon(iconesOpcoes[i]);
            opcao.setVerticalTextPosition(JLabel.BOTTOM);
            opcao.setHorizontalTextPosition(JLabel.CENTER);
            opcoes[i] = opcao;
        }

        // ----------------------------------------------------------------------------

        // Opcao "Novo Diagrama"
        opcoes[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!diagramaAtual.isDiagramaSalvo() && diagramaAtual.arquivoDiagrama != null) {
                    if (mostrarDialogSalvarAlteracoes()) {
                        novoDiagrama();
                    }
                } else {
                    novoDiagrama();
                }
            }
        });

        // Opcao "Salvar Diagrama"
        opcoes[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!diagramaAtual.isDiagramaSalvo()) {
                    salvarDiagrama(false);
                }
            }
        });

        // Opcao "Salvar Como"
        opcoes[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                salvarDiagrama(true);
            }
        });

        // Opcao "Abrir Diagrama"
        opcoes[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GerenciadorDeArquivos gerenciadorDeArquivos = GerenciadorDeArquivos.getInstancia();

                DiagramaUML diagrama = gerenciadorDeArquivos.abrirDiagrama(diagramaAtual.arquivoDiagrama);

                if (diagrama != null) {
                    carregarDiagrama(diagrama);
                }
            }
        });

        // Opcao "Exportar"
        opcoes[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportarDiagrama();
            }
        });

        // Opcao "Voltar"
        opcoes[5].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                voltar();
            }
        });

        // ----------------------------------------------------------------------------

        for (JLabel opcao : opcoes) {
            opcao.addMouseListener(new MouseAdapter() {
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
            });
        }

        // ----------------------------------------------------------------------------

        JPanel painelDiretorio = new JPanel(new MigLayout("insets 0 15 13 15, fill"));
        painelDiretorio.setBackground(gerenciadorDeRecursos.getColor("black"));
        painelDiretorio.setBorder(BorderFactory.createMatteBorder(
            0, 0, 3, 0, gerenciadorDeRecursos.getColor("platinum"))
        );

        painelDiretorioDiagrama.setLayout(new MigLayout("insets 5 8 5 8", "[grow, fill][]"));
        painelDiretorioDiagrama.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));

        JLabel labelDiretorioDiagrama = new JLabel();
        labelDiretorioDiagrama.setFont(gerenciadorDeRecursos.getRobotoMedium(13));
        labelDiretorioDiagrama.setForeground(gerenciadorDeRecursos.getColor("white"));

        JLabel labelDiagramaNaoSalvo = new JLabel(gerenciadorDeRecursos.getString("diagrama_nao_salvo_alteracoes"));
        labelDiagramaNaoSalvo.setFont(gerenciadorDeRecursos.getRobotoMedium(13));
        labelDiagramaNaoSalvo.setForeground(gerenciadorDeRecursos.getColor("white"));
        labelDiagramaNaoSalvo.setVisible(false);

        painelDiretorioDiagrama.add(labelDiretorioDiagrama);
        painelDiretorioDiagrama.add(labelDiagramaNaoSalvo);

        painelDiretorio.add(painelDiretorioDiagrama, "growx");

        // ----------------------------------------------------------------------------

        JPanel painelSeparador = new JPanel();
        painelSeparador.setBackground(gerenciadorDeRecursos.getColor("black"));

        menuDiagramas.add(opcoes[0], "grow");
        menuDiagramas.add(opcoes[1], "gapleft 10, grow");
        menuDiagramas.add(opcoes[2], "gapleft 10, grow");
        menuDiagramas.add(opcoes[3], "gapleft 10, grow");
        menuDiagramas.add(opcoes[4], "gapleft 10, grow");
        menuDiagramas.add(painelSeparador, "gapleft 10, grow");
        menuDiagramas.add(opcoes[5], "gapleft 80, grow");
        menuDiagramas.add(painelDiretorio, "south");

        return menuDiagramas;
    }

    private JPanel getPainelAcoesQuadroBranco() {
        /*

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
    }*/
        return null;
    }

    private void inicializarDialogSalvarAlteracoes() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        dialogSalvarAlteracoes.setTitle(gerenciadorDeRecursos.getString("salvar_alteracoes"));
        dialogSalvarAlteracoes.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogSalvarAlteracoes.setResizable(false);

        // ----------------------------------------------------------------------------

        JPanel painelDialog = new JPanel(new MigLayout("insets 10 0 15 0"));
        painelDialog.setBackground(gerenciadorDeRecursos.getColor("white"));

        // ----------------------------------------------------------------------------

        JLabel labelImgInterrogacao = new JLabel(gerenciadorDeRecursos.getImagem("icone_interrogacao_pequena"));
        JPanel painelImgInterrogacao = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgInterrogacao.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));
        painelImgInterrogacao.add(labelImgInterrogacao, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelMensagem = new JLabel();
        labelMensagem.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        painelMensagem.add(labelMensagem, "align center");

        // ----------------------------------------------------------------------------

        String[] salvarAlteracoesOpcoesTexto = {
            gerenciadorDeRecursos.getString("salvar"),
            gerenciadorDeRecursos.getString("salvar_nao"),
            gerenciadorDeRecursos.getString("cancelar")
        };

        JPanel[] salvarAlteracoesOpcoes = new JPanel[salvarAlteracoesOpcoesTexto.length];

        for (int i = 0; i < salvarAlteracoesOpcoes.length; i++) {
            JPanel painelOpcao = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
            painelOpcao.setBackground(gerenciadorDeRecursos.getColor("white"));
            painelOpcao.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
            ));

            JLabel labelOpcao = new JLabel(salvarAlteracoesOpcoesTexto[i]);
            labelOpcao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
            labelOpcao.setForeground(gerenciadorDeRecursos.getColor("black"));

            painelOpcao.add(labelOpcao, "align center");

            salvarAlteracoesOpcoes[i] = painelOpcao;
        }

        // ----------------------------------------------------------------------------

        for (JPanel opcao : salvarAlteracoesOpcoes) {
            opcao.addMouseListener(new MouseAdapter() {
                // O componente de index 0 é o JLabel representando o texto da opcao

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

                @Override
                public void mouseClicked(MouseEvent e) {
                    dialogSalvarAlteracoes.setVisible(false);
                }
            });
        }

        // ----------------------------------------------------------------------------

        // Opcao de index 0 é a salvar diagrama
        salvarAlteracoesOpcoes[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                salvarDiagrama(diagramaAtual.arquivoDiagrama == null);
            }
        });

        // ----------------------------------------------------------------------------

        painelDialog.add(painelImgInterrogacao, "west");
        painelDialog.add(painelMensagem, "wrap");
        painelDialog.add(salvarAlteracoesOpcoes[0], "gaptop 15, split 3, gapleft: push, gapright: push");
        painelDialog.add(salvarAlteracoesOpcoes[1], "gaptop 15, gapleft: push, gapright: push");
        painelDialog.add(salvarAlteracoesOpcoes[2], "gaptop 15, gapleft: push, gapright: push");

        dialogSalvarAlteracoes.setContentPane(painelDialog);
        dialogSalvarAlteracoes.pack();
    }
}
