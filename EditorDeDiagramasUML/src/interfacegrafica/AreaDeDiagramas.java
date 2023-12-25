package interfacegrafica;

import componentes.alteracoes.relacoes.RelacaoCriada;
import componentes.relacoes.*;
import auxiliares.GerenciadorDeArquivos;
import auxiliares.GerenciadorDeRecursos;
import componentes.estruturas.AnotacaoUML;
import componentes.estruturas.ClasseUML;
import componentes.estruturas.EstruturaUML;
import componentes.estruturas.PacoteUML;
import componentes.alteracoes.AlteracaoDeComponenteUML;
import componentes.alteracoes.estruturas.EstruturaCriada;
import componentes.modelos.relacoes.TipoDeRelacao;
import diagrama.DiagramaUML;
import componentes.modelos.estruturas.Pacote;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Classe que representa a tela de montagem e modificação dos diagramas, ficando resonsável por gerenciar a adição,
 * remoção e modificação de componentes no quadro branco.
 */
public class AreaDeDiagramas {

    // TODO: arrumar esses atributos e ver comentarios em metodos
    private final JPanel painelAreaDeDiagramas;
    private final JPanel painelQuadroBranco;
    private DiagramaUML diagramaAtual = new DiagramaUML();
    private boolean criacaoDeRelacaoAcontecendo;
    private TipoDeRelacao tipoDeRelacaoSendoCriada;
    private final ArrayList<AlteracaoDeComponenteUML> alteracoesDeComponentes = new ArrayList<>();
    /**
     * Indica a posição atual na lista de alterações de acordo com as ações do usuário.
     * Quando ele desfaz uma ação o index volta uma posição, e se refazer o index avança uma posição, dessa forma
     * é possivel saber qual alteração precisa ser manipulado.
     */
    private int indexAlteracao = -1;
    private boolean movimentacaoPermitida = false;
    private final GerenciadorInterfaceGrafica gerenciadorInterfaceGrafica;
    private final JDialog dialogSalvarAlteracoes = new JDialog();
    private final JPanel painelDiretorioDiagrama = new JPanel();
    private final JPanel painelCriarRelacao = new JPanel();

    private final JPanel painelCancelarRelacao = new JPanel();

    private final JPanel painelInstrucoesRelacao = new JPanel();
    /**
     * Um painel contendo botões para ações como: mover quadro, selecionar componente, desfazer e refazer alterações
     */
    private final JPanel painelAcoesQuadroBranco = new JPanel();
    private final int TAMANHO_QUADRO_BRANCO = 5000;
    private final static int LIMITE_ALTERACOES_DE_COMPONENTES = 25;
    private final static int ALTURA_AREA_DE_DIAGRAMAS = 500;

    public AreaDeDiagramas(GerenciadorInterfaceGrafica gerenciadorInterfaceGrafica) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        painelAreaDeDiagramas = new JPanel();
        painelAreaDeDiagramas.setLayout(new MigLayout());
        this.gerenciadorInterfaceGrafica = gerenciadorInterfaceGrafica;

        // Criando e adicionado os componentes graficos ao painelAreaDeDiagramas

        JPanel menuDeOpcoes = getPainelMenuDeOpcoes();
        inicializarPainelAcoesQuadroBranco();

        // ----------------------------------------------------------------------------

        inicializarDialogSalvarAlteracoes();

        this.gerenciadorInterfaceGrafica.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (gerenciadorInterfaceGrafica.estaMostrandoAreaDeDiagramas()) {
                    if (!diagramaAtual.diagramaEstaSalvo() &&
                            (diagramaAtual.arquivoDiagrama != null || !alteracoesDeComponentes.isEmpty())
                    ) {
                        mostrarDialogSalvarAlteracoes();
                    }
                    gerenciadorInterfaceGrafica.mostrarDialogFecharAplicacao();
                }
            }
        });

        // ----------------------------------------------------------------------------

        painelQuadroBranco = new JPanel(null);
        painelQuadroBranco.setPreferredSize(new Dimension(TAMANHO_QUADRO_BRANCO,TAMANHO_QUADRO_BRANCO));
        painelQuadroBranco.setBackground(gerenciadorDeRecursos.getColor("gainsboro"));

        JScrollPane scrollPaneQuadroBranco = new JScrollPane(painelQuadroBranco);
        scrollPaneQuadroBranco.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneQuadroBranco.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        Rectangle viewportRect = scrollPaneQuadroBranco.getViewport().getViewRect();

        // Movendo o viewport para o centro do quadro brannco
        viewportRect.x += (TAMANHO_QUADRO_BRANCO/2) - viewportRect.x/2;
        viewportRect.y += (TAMANHO_QUADRO_BRANCO/2) - viewportRect.y/2;

        painelQuadroBranco.scrollRectToVisible(viewportRect);

        // ----------------------------------------------------------------------------

        painelInstrucoesRelacao.setLayout(new MigLayout("fill, insets 10 15 15 15"));
        painelInstrucoesRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelInstrucoesRelacao.setBorder(BorderFactory.createMatteBorder(
            2, 2, 2, 2, gerenciadorDeRecursos.getColor("dark_gray"))
        );
        painelInstrucoesRelacao.setVisible(false);

        JLabel labelInstrucao = new JLabel(gerenciadorDeRecursos.getString("instrucoes_maiusculo"), JLabel.CENTER);
        labelInstrucao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelInstrucao.setForeground(gerenciadorDeRecursos.getColor("white"));

        JPanel painelErro = new JPanel(new MigLayout("fill, insets 5 5 5 5"));
        painelErro.add(labelInstrucao, "align center, grow");
        painelErro.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));

        JLabel labelDescricaoInstrucao = new JLabel(gerenciadorDeRecursos.getString("instrucoes_relacoes") , JLabel.CENTER);
        labelDescricaoInstrucao.setFont(gerenciadorDeRecursos.getRobotoMedium(12));

        painelInstrucoesRelacao.add(painelErro, "north");
        painelInstrucoesRelacao.add(labelDescricaoInstrucao, "align center, wrap");

        // ----------------------------------------------------------------------------

        painelCriarRelacao.setLayout(new MigLayout("fill, insets 5 10 5 10"));
        painelCriarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelCriarRelacao.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black"))
        );
        painelCriarRelacao.setVisible(false);

        JLabel labelCriarRelacionamento = new JLabel(gerenciadorDeRecursos.getString("finalizar"));
        labelCriarRelacionamento.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        labelCriarRelacionamento.setForeground(gerenciadorDeRecursos.getColor("black"));
        painelCriarRelacao.add(labelCriarRelacionamento, "align center");

        // ----------------------------------------------------------------------------

        painelCancelarRelacao.setLayout(new MigLayout("fill, insets 5 10 5 10"));
        painelCancelarRelacao.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelCancelarRelacao.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black"))
        );
        painelCancelarRelacao.setVisible(false);

        JLabel cancelarRelacao = new JLabel(gerenciadorDeRecursos.getString("cancelar"));
        cancelarRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(15));
        cancelarRelacao.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelCancelarRelacao.add(cancelarRelacao, "align center");

        // ----------------------------------------------------------------------------

        MouseAdapter adapterBotoesRelacao = new MouseAdapter() {
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
        };

        painelCriarRelacao.addMouseListener(adapterBotoesRelacao);
        painelCancelarRelacao.addMouseListener(adapterBotoesRelacao);

        // ----------------------------------------------------------------------------

        MouseAdapter adapterComportamentoQuadroBranco = new MouseAdapter() {
            Point origemMovimento;

            @Override
            public void mousePressed(MouseEvent e) {
                origemMovimento = new Point(e.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle viewportRect = scrollPaneQuadroBranco.getViewport().getViewRect();

                int limiteEsquerdoX = viewportRect.x;
                int limiteDireitoX = viewportRect.x + viewportRect.width;
                int limiteSuperiorY = viewportRect.y;
                int limiteInferiorY = viewportRect.y + viewportRect.height;

                int xAtual = e.getX();
                int yAtual = e.getY();

                if (movimentacaoPermitida && xAtual > limiteEsquerdoX && xAtual < limiteDireitoX &&
                        yAtual > limiteSuperiorY && yAtual < limiteInferiorY ) {
                    int deltaX = origemMovimento.x - xAtual;
                    int deltaY = origemMovimento.y - yAtual;

                    viewportRect.x += deltaX;
                    viewportRect.y += deltaY;

                    painelQuadroBranco.scrollRectToVisible(viewportRect);
                }
            }
        };


        painelQuadroBranco.setAutoscrolls(true);
        painelQuadroBranco.addMouseListener(adapterComportamentoQuadroBranco);
        painelQuadroBranco.addMouseMotionListener(adapterComportamentoQuadroBranco);

        adicionarComportamentoCriarRelacao();

        // ----------------------------------------------------------------------------

        JLayeredPane painelCamadasQuadroBranco = new JLayeredPane();
        painelCamadasQuadroBranco.setOpaque(false);

        JPanel painelMenuComponentes = getPainelMenuComponentes();

        painelCamadasQuadroBranco.add(scrollPaneQuadroBranco, JLayeredPane.DEFAULT_LAYER);
        scrollPaneQuadroBranco.setBounds(
            0, 0,
            menuDeOpcoes.getPreferredSize().width - painelMenuComponentes.getPreferredSize().width,
            ALTURA_AREA_DE_DIAGRAMAS
        );

        painelCamadasQuadroBranco.add(painelAcoesQuadroBranco, JLayeredPane.PALETTE_LAYER);
        painelAcoesQuadroBranco.setBounds(
            // Resumindo a aritmetica abaixo: o painel de acoes vai ficar no meio do quadro branco
            (scrollPaneQuadroBranco.getWidth()/2) - (painelAcoesQuadroBranco.getPreferredSize().width/2) - painelMenuComponentes.getPreferredSize().width/4,
            8, painelAcoesQuadroBranco.getPreferredSize().width, painelAcoesQuadroBranco.getPreferredSize().height
        );

        int MARGEM_DIREITA_E_INFERIOR = 15;

        painelCamadasQuadroBranco.add(painelCriarRelacao, JLayeredPane.PALETTE_LAYER);
        painelCriarRelacao.setBounds(
            MARGEM_DIREITA_E_INFERIOR,
            painelAcoesQuadroBranco.getBounds().y,
            painelCriarRelacao.getPreferredSize().width,
            painelCriarRelacao.getPreferredSize().height
        );

        painelCamadasQuadroBranco.add(painelCancelarRelacao, JLayeredPane.PALETTE_LAYER);
        painelCancelarRelacao.setBounds(
            MARGEM_DIREITA_E_INFERIOR,
            painelAcoesQuadroBranco.getBounds().y,
            painelCancelarRelacao.getPreferredSize().width,
            painelCancelarRelacao.getPreferredSize().height
        );

        painelCamadasQuadroBranco.add(painelInstrucoesRelacao, JLayeredPane.PALETTE_LAYER);
        painelInstrucoesRelacao.setBounds(
            MARGEM_DIREITA_E_INFERIOR,
            ALTURA_AREA_DE_DIAGRAMAS - painelInstrucoesRelacao.getPreferredSize().height - MARGEM_DIREITA_E_INFERIOR,
            painelInstrucoesRelacao.getPreferredSize().width,
            painelInstrucoesRelacao.getPreferredSize().height
        );

        // ----------------------------------------------------------------------------

        JScrollPane scrollPaneMenuDiagramas = new JScrollPane(painelMenuComponentes);
        scrollPaneMenuDiagramas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        painelAreaDeDiagramas.add(menuDeOpcoes, "north");

        painelAreaDeDiagramas.add(scrollPaneMenuDiagramas, "west, height " + ALTURA_AREA_DE_DIAGRAMAS);
        painelAreaDeDiagramas.add(painelCamadasQuadroBranco, "grow");
    }

    public void addComponenteAoQuadro(EstruturaUML<?> componente, boolean centralizar) {
        diagramaAtual.addComponente(componente);

        if (componente instanceof PacoteUML) {
            // Os pacote ficam embaixo de todos os outros componentes
            painelQuadroBranco.add(componente.getPainelComponente());
        } else {
            painelQuadroBranco.add(componente.getPainelComponente(), 0);
        }

        if (centralizar) {
            // Centraliza o componente com relacao ao viewPort

            JViewport viewPortQuadroBranco = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, painelQuadroBranco);

            Rectangle viewRectQuadroBranco = viewPortQuadroBranco.getViewRect();
            Point posicaoDoNovoComponente = viewPortQuadroBranco.getViewPosition();

            // O 'componente.getLargura()/5' é um tanto arbitrario, ele existe para deixar o componente mais ou menos
            // centralizado com relacao ao menu de opcoes do quadro branco

            posicaoDoNovoComponente.move(
                posicaoDoNovoComponente.x + viewRectQuadroBranco.width/2 - componente.getLargura()/2 - componente.getLargura()/5 ,
                posicaoDoNovoComponente.y + viewRectQuadroBranco.height/2 - componente.getAltura()/2
            );

            componente.getPainelComponente().setLocation(posicaoDoNovoComponente);
        }

        revalidarQuadroBranco();
    }

    public void addComponenteAoQuadro(JComponent componente, int index) {
        painelQuadroBranco.add(componente, index);
        revalidarQuadroBranco();
    }

    public void addComponenteAoQuadro(JComponent componente) {
        painelQuadroBranco.add(componente);
        revalidarQuadroBranco();
    }

    public void addRelacaoAoDiagrama(RelacaoUML relacao) {
        if (!diagramaAtual.getRelacoesUML().contains(relacao)) {
            diagramaAtual.addRelacao(relacao);
        }
    }

    public void removerEstruturaDoQuadro(EstruturaUML<?> componente) {
        diagramaAtual.removerComponente(componente);
        painelQuadroBranco.remove(componente.getPainelComponente());
        revalidarQuadroBranco();
    }

    public void removerComponenteDoQuadro(JComponent componente) {
        painelQuadroBranco.remove(componente);
        revalidarQuadroBranco();
    }

    public void removerRelacaoDoDiagrama(RelacaoUML relacao) {
        diagramaAtual.removerRelacao(relacao);
    }

    public void addAlteracaoDeComponente(AlteracaoDeComponenteUML novaAlteracao) {
        diagramaAtual.setDiagramaSalvo(false);

        if (diagramaAtual.arquivoDiagrama == null) {
            setLabelDiretorioDiagrama(null);
        } else {
            setVisibilidadeDiagramaNaoSalvo(true);

            gerenciadorInterfaceGrafica.setWindowTitle(
                    GerenciadorDeRecursos.getInstancia().getString("app_titulo") + " - " + diagramaAtual.arquivoDiagrama.getName() + "*"
            );
        }

        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelUndo = (JLabel) painelAcoesQuadroBranco.getComponent(3);
        JLabel labelRedo = (JLabel) painelAcoesQuadroBranco.getComponent(4);

        // Se o usuario fez algumas operacoes de "desfazer", mas em algum ponto ao longo do caminho
        // criou mais uma alteração de componente, então as alteracoes depois desse ponto sao descartadas,
        // e essa alteracao vira o fim da lista
        if (alteracoesDeComponentes.size() >= indexAlteracao + 1) {
            alteracoesDeComponentes.subList(indexAlteracao + 1, alteracoesDeComponentes.size()).clear();
            labelRedo.setEnabled(false);
            labelRedo.setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
        }

        if (alteracoesDeComponentes.size() >= LIMITE_ALTERACOES_DE_COMPONENTES) {
            alteracoesDeComponentes.remove(0);
            alteracoesDeComponentes.add(novaAlteracao);
        } else {
            alteracoesDeComponentes.add(novaAlteracao);
            indexAlteracao++;
        }

        if (!labelUndo.isEnabled()) {
            labelUndo.setBackground(gerenciadorDeRecursos.getColor("white"));
            labelUndo.setEnabled(true);
        }
    }

    // TODO
    // Por "habilitados" entende-se que o usuario pode interagir com eles
    public boolean componentesEstaoHabilitados() {
        return !movimentacaoPermitida && !criacaoDeRelacaoAcontecendo;
    }

    public void revalidarQuadroBranco() {
        painelQuadroBranco.revalidate();
        painelQuadroBranco.repaint();
    }

    private void resetarAreaDeDiagramas() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        if (diagramaAtual != null) {
            while (!diagramaAtual.getEstruturasUML().isEmpty()) {
                removerEstruturaDoQuadro(diagramaAtual.getEstruturasUML().get(0));
            }
            while (!diagramaAtual.getRelacoesUML().isEmpty()) {
                diagramaAtual.getRelacoesUML().get(0).removerRelacaoDoQuadroBranco();
            }
        }

        setLabelDiretorioDiagrama(null);
        setVisibilidadeDiagramaNaoSalvo(false);

        alteracoesDeComponentes.clear();
        indexAlteracao = -1;

        criacaoDeRelacaoAcontecendo = false;

        // selecionado o item cursor e tirando a seleção de mover
        painelAcoesQuadroBranco.getComponent(0).setBackground(gerenciadorDeRecursos.getColor("platinum"));
        painelAcoesQuadroBranco.getComponent(1).setBackground(gerenciadorDeRecursos.getColor("white"));
        movimentacaoPermitida = false;

        // desabilitando o undo e redo
        painelAcoesQuadroBranco.getComponent(2).setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
        painelAcoesQuadroBranco.getComponent(2).setEnabled(false);

        painelAcoesQuadroBranco.getComponent(3).setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
        painelAcoesQuadroBranco.getComponent(3).setEnabled(false);

        painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void novoDiagrama() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        resetarAreaDeDiagramas();

        diagramaAtual = new DiagramaUML();
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

        if (diagramaAtual.diagramaEstaSalvo()) {
            setLabelDiretorioDiagrama(diagramaAtual.arquivoDiagrama.getAbsolutePath());

            setVisibilidadeDiagramaNaoSalvo(false);

            gerenciadorInterfaceGrafica.setWindowTitle(
                gerenciadorDeRecursos.getString("app_titulo") + " - " + diagramaAtual.arquivoDiagrama.getName()
            );
        }
    }

    public void carregarDiagrama(DiagramaUML diagrama) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        resetarAreaDeDiagramas();

        diagramaAtual = diagrama;

        for (EstruturaUML<?> componente : diagrama.getEstruturasUML()) {
            if (componente instanceof PacoteUML) {
                // Os pacote ficam embaixo de todos os outros componentes
                painelQuadroBranco.add(componente.getPainelComponente());
            } else {
                painelQuadroBranco.add(componente.getPainelComponente(), 0);
            }
        }

        for (RelacaoUML relacaoUML : diagrama.getRelacoesUML()) {
            relacaoUML.adicionarRelacaoAoQuadroBranco();
        }

        revalidarQuadroBranco();

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

        for (EstruturaUML<?> estruturaUML : diagramaAtual.getEstruturasUML()) {
            Rectangle boundsEstrutura = estruturaUML.getPainelComponente().getBounds();

            menorPontoX = Math.min(boundsEstrutura.x, menorPontoX);
            menorPontoY = Math.min(boundsEstrutura.y, menorPontoY);

            maiorPontoX = Math.max(boundsEstrutura.x + boundsEstrutura.width, maiorPontoX);
            maiorPontoY = Math.max(boundsEstrutura.y + boundsEstrutura.height, maiorPontoY);
        }

        for (RelacaoUML relacaoUML : diagramaAtual.getRelacoesUML()) {
            for (JPanel linha : relacaoUML.getLinhasDaRelacao()) {
                Rectangle boundsLinha = linha.getBounds();

                menorPontoX = Math.min(boundsLinha.x, menorPontoX);
                menorPontoY = Math.min(boundsLinha.y, menorPontoY);

                maiorPontoX = Math.max(boundsLinha.x + boundsLinha.width, maiorPontoX);
                maiorPontoY = Math.max(boundsLinha.y + boundsLinha.height, maiorPontoY);
            }
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

        revalidarQuadroBranco();
    }

    private void voltar() {
        if (!diagramaAtual.diagramaEstaSalvo() && (diagramaAtual.arquivoDiagrama != null || !alteracoesDeComponentes.isEmpty())) {
            if (mostrarDialogSalvarAlteracoes()) {
                gerenciadorInterfaceGrafica.mostrarMenuPrincipal();
            }
        } else {
            gerenciadorInterfaceGrafica.mostrarMenuPrincipal();
        }
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

    private void setCriacaoDeRelacaoAcontecendo(boolean criacaoAcontecendo) {
        criacaoDeRelacaoAcontecendo = criacaoAcontecendo;
        painelInstrucoesRelacao.setVisible(criacaoAcontecendo);
        painelCriarRelacao.setVisible(criacaoAcontecendo);
    }

    public JPanel getPainelAreaDeDiagramas() {
        return this.painelAreaDeDiagramas;
    }

    public int getTamanhoQuadroBranco() {
        return TAMANHO_QUADRO_BRANCO;
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
                if (!diagramaAtual.diagramaEstaSalvo() && diagramaAtual.arquivoDiagrama != null) {
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
                if (!diagramaAtual.diagramaEstaSalvo()) {
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

                DiagramaUML diagrama = gerenciadorDeArquivos.abrirDiagrama(diagramaAtual.arquivoDiagrama, AreaDeDiagramas.this);

                if (diagrama != null) {
                    if (!diagramaAtual.diagramaEstaSalvo() &&
                            (diagramaAtual.arquivoDiagrama != null || !alteracoesDeComponentes.isEmpty())
                    ) {
                        mostrarDialogSalvarAlteracoes();
                    }

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
                if (!criacaoDeRelacaoAcontecendo) {
                    voltar();
                }
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

    private JPanel getPainelMenuComponentes() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelNovaClasse = new JLabel(gerenciadorDeRecursos.getImagem("componente_classe"), JLabel.CENTER);
        labelNovaClasse.setOpaque(true);
        labelNovaClasse.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelNovaClasse.setBackground(gerenciadorDeRecursos.getColor("platinum"));

        labelNovaClasse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    ClasseUML novaClasse = new ClasseUML(AreaDeDiagramas.this);

                    addComponenteAoQuadro(novaClasse, true);

                    addAlteracaoDeComponente(
                        new EstruturaCriada(
                            novaClasse.getPainelComponente().getLocation(),
                            novaClasse
                        )
                    );
                }
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelNovaAnotacao = new JLabel(gerenciadorDeRecursos.getImagem("componente_anotacao"), JLabel.CENTER);
        labelNovaAnotacao.setOpaque(true);
        labelNovaAnotacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelNovaAnotacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));

        labelNovaAnotacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    AnotacaoUML novaAnotacao = new AnotacaoUML(AreaDeDiagramas.this);

                    addComponenteAoQuadro(novaAnotacao, true);

                    addAlteracaoDeComponente(
                        new EstruturaCriada(
                            novaAnotacao.getPainelComponente().getLocation(),
                            novaAnotacao
                        )
                    );
                }
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelNovoPacote = new JLabel(gerenciadorDeRecursos.getImagem("componente_pacote"), JLabel.CENTER);
        labelNovoPacote.setOpaque(true);
        labelNovoPacote.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelNovoPacote.setBackground(gerenciadorDeRecursos.getColor("platinum"));

        labelNovoPacote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    PacoteUML novoPacote = new PacoteUML(AreaDeDiagramas.this);

                    addComponenteAoQuadro(novoPacote, true);

                    Rectangle boundsPainelComponente = novoPacote.getPainelComponente().getBounds();
                    Pacote modeloPacote = new Pacote();
                    modeloPacote.setBounds(new Rectangle(
                        boundsPainelComponente.x, boundsPainelComponente.y,
                        boundsPainelComponente.width, boundsPainelComponente.height)
                    );
                    novoPacote.setModelo(modeloPacote);
                    novoPacote.setModeloAntesDeAlteracoes(modeloPacote.copiar());

                    addAlteracaoDeComponente(
                        new EstruturaCriada(
                            novoPacote.getPainelComponente().getLocation(),
                            novoPacote
                        )
                    );
                }
            }
        });

        // ----------------------------------------------------------------------------

        JLabel labelNovaGeneralizacao = getLabelDeRelacao("relacao_generalizacao", "relacao_generalizacao", TipoDeRelacao.GENERALIZACAO);
        JLabel labelNovaRealizacao = getLabelDeRelacao("relacao_realizacao", "relacao_realizacao", TipoDeRelacao.REALIZACAO);
        JLabel labelNovaDependencia = getLabelDeRelacao("relacao_dependencia", "relacao_dependencia", TipoDeRelacao.DEPENDENCIA);
        JLabel labelNovaAssociacao = getLabelDeRelacao("relacao_associacao", "relacao_associacao", TipoDeRelacao.ASSOCIACAO);
        JLabel labelNovaAgregacao = getLabelDeRelacao("relacao_agregacao", "relacao_agregacao", TipoDeRelacao.AGREGACAO);
        JLabel labelNovaComposicao = getLabelDeRelacao("relacao_composicao", "relacao_composicao", TipoDeRelacao.COMPOSICAO);

        // ----------------------------------------------------------------------------

        painelCriarRelacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelNovaGeneralizacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                labelNovaRealizacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                labelNovaDependencia.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                labelNovaAssociacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                labelNovaAgregacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                labelNovaComposicao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
            }
        });

        // ----------------------------------------------------------------------------

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();
                    source.setBackground(gerenciadorDeRecursos.getColor("silver_sand"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    JLabel source = (JLabel) e.getSource();
                    source.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                }
            }
        };

        labelNovaClasse.addMouseListener(mouseAdapter);
        labelNovaAnotacao.addMouseListener(mouseAdapter);
        labelNovoPacote.addMouseListener(mouseAdapter);
        labelNovaGeneralizacao.addMouseListener(mouseAdapter);
        labelNovaRealizacao.addMouseListener(mouseAdapter);
        labelNovaDependencia.addMouseListener(mouseAdapter);
        labelNovaAssociacao.addMouseListener(mouseAdapter);
        labelNovaAgregacao.addMouseListener(mouseAdapter);
        labelNovaComposicao.addMouseListener(mouseAdapter);

        // ----------------------------------------------------------------------------

        CompoundBorder bordaSeparadores = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xe5e5e5)),
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xc2c2c2))
        );

        JPanel menuComponentes = new JPanel(new MigLayout("insets 15 15 15 15"));
        menuComponentes.setBackground(gerenciadorDeRecursos.getColor("white"));
        menuComponentes.setBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, gerenciadorDeRecursos.getColor("platinum"))
        );

        JLabel[] labelComponentes = {
            labelNovaClasse, labelNovaAnotacao, labelNovoPacote, labelNovaGeneralizacao, labelNovaRealizacao,
            labelNovaDependencia, labelNovaAssociacao, labelNovaAgregacao, labelNovaComposicao
        };

        for (int i = 0; i < labelComponentes.length; i++) {
            if (i + 1 < labelComponentes.length) {
                menuComponentes.add(labelComponentes[i], "wrap, gapbottom 5, growx");
                menuComponentes.add(new JLabel() {
                    { setBorder(bordaSeparadores); }
                }, "wrap, grow, gapbottom 5, growx");
            } else {
                menuComponentes.add(labelComponentes[i], "wrap, growx");
            }
        }
        return menuComponentes;
    }

    private JLabel getLabelDeRelacao(String keyImagem, String keyNomeRelacao, TipoDeRelacao tipoDeRelacao) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelRelacao = new JLabel(gerenciadorDeRecursos.getImagem(keyImagem));
        labelRelacao.setFont(gerenciadorDeRecursos.getRobotoMedium(12));
        labelRelacao.setText(gerenciadorDeRecursos.getString(keyNomeRelacao));
        labelRelacao.setVerticalTextPosition(JLabel.BOTTOM);
        labelRelacao.setHorizontalTextPosition(JLabel.CENTER);
        labelRelacao.setOpaque(true);
        labelRelacao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelRelacao.setBackground(gerenciadorDeRecursos.getColor("platinum"));
        labelRelacao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!criacaoDeRelacaoAcontecendo) {
                    setCriacaoDeRelacaoAcontecendo(true);
                    tipoDeRelacaoSendoCriada = tipoDeRelacao;

                    for (RelacaoUML relacao : diagramaAtual.getRelacoesUML()) {
                        if (relacao.getTipoDeRelacao() == tipoDeRelacaoSendoCriada) {
                            relacao.mostrarPontoDeExtensao(true);
                        }
                    }
                }
            }
        });
        return labelRelacao;
    }

    private void adicionarComportamentoCriarRelacao() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // O indicador de relacao é so um circulo indicando a posicao do mouse
        int TAMANHO_INDICADOR = 10;

        JPanel indicadorRelacao = new JPanel() {
            private final Color corDoIndicador = gerenciadorDeRecursos.getColor("dark_charcoal");

            public void paintComponent(Graphics g) {
                g.setColor(corDoIndicador);
                g.fillOval(0, 0, TAMANHO_INDICADOR, TAMANHO_INDICADOR);
            }
        };

        MouseAdapter adapterCriarRelacao = new MouseAdapter() {
            JPanel linhaHorizontalRelacao;
            JPanel linhaVerticalRelacao;
            RelacaoUML relacaoParaEstender;
            Point primeiroClique;
            boolean permitirCriarRelacao;
            final int TAMANHO_LINHAS_RELACAO = RelacaoUML.TAMANHO_LINHAS_RELACAO;
            final int TAMANHO_MINIMO_LINHAS_RELACAO = 10;

            {
                painelCancelarRelacao.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        painelQuadroBranco.remove(linhaHorizontalRelacao);
                        painelQuadroBranco.remove(linhaVerticalRelacao);
                        painelCriarRelacao.setVisible(true);
                        painelCancelarRelacao.setVisible(false);
                        permitirCriarRelacao = false;

                        if (relacaoParaEstender != null) {
                            relacaoParaEstender.estenderRelacao(null, null);
                            relacaoParaEstender = null;
                        }

                        for (RelacaoUML relacao : diagramaAtual.getRelacoesUML()) {
                            if (relacao.getTipoDeRelacao() == tipoDeRelacaoSendoCriada) {
                                relacao.mostrarPontoDeExtensao(true);
                            }
                        }

                        revalidarQuadroBranco();
                    }
                });

                painelCriarRelacao.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        setCriacaoDeRelacaoAcontecendo(false);
                        for (RelacaoUML relacao : diagramaAtual.getRelacoesUML()) {
                            relacao.mostrarPontoDeExtensao(false);
                        }
                    }
                });
            }

            private void criarRelacao(Point ultimoClique) {
                if (tipoDeRelacaoSendoCriada != null) {
                    ArrayList<JPanel> linhasDaRelacao = new ArrayList<>();

                    if (linhaHorizontalRelacao.getParent() != null) {
                        linhasDaRelacao.add(linhaHorizontalRelacao);
                    }
                    if (linhaVerticalRelacao.getParent() != null) {
                        linhasDaRelacao.add(linhaVerticalRelacao);
                    }

                    if (!linhasDaRelacao.isEmpty() && relacaoParaEstender == null) {
                        RelacaoUML novaRelacao = switch (tipoDeRelacaoSendoCriada) {
                            case GENERALIZACAO -> new Generalizacao(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                            case REALIZACAO -> new Realizacao(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                            case DEPENDENCIA -> new Dependencia(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                            case ASSOCIACAO -> new Associacao(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                            case AGREGACAO -> new Agregacao(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                            case COMPOSICAO -> new Composicao(
                                linhasDaRelacao, AreaDeDiagramas.this,
                                primeiroClique, ultimoClique, tipoDeRelacaoSendoCriada
                            );
                        };

                        novaRelacao.setAoClicarPontoDeExtensao(relacaoParaEstender -> {
                            criarPaineisDaRelacao(relacaoParaEstender.getLocalizacaoDeExtensao());
                            this.relacaoParaEstender = relacaoParaEstender;

                            for (RelacaoUML relacao : diagramaAtual.getRelacoesUML()) {
                                relacao.mostrarPontoDeExtensao(false);
                            }
                        });

                        diagramaAtual.addRelacao(novaRelacao);

                        addAlteracaoDeComponente(new RelacaoCriada(novaRelacao));

                        novaRelacao.mostrarPontoDeExtensao(true);
                    }

                    if (relacaoParaEstender != null) {
                        if (!linhasDaRelacao.isEmpty()) {
                            relacaoParaEstender.estenderRelacao(linhasDaRelacao, ultimoClique);
                        } else {
                            relacaoParaEstender.estenderRelacao(null, null);
                        }

                        relacaoParaEstender.mostrarPontoDeExtensao(true);
                        relacaoParaEstender = null;
                    }
                }
            }

            private void criarPaineisDaRelacao(Point origemClique) {
                primeiroClique = origemClique;

                painelCancelarRelacao.setVisible(true);
                painelCriarRelacao.setVisible(false);

                linhaHorizontalRelacao = new JPanel();
                linhaHorizontalRelacao.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));

                linhaVerticalRelacao = new JPanel();
                linhaVerticalRelacao.setBackground(gerenciadorDeRecursos.getColor("dark_charcoal"));

                painelQuadroBranco.add(linhaHorizontalRelacao);
                painelQuadroBranco.add(linhaVerticalRelacao);

                linhaHorizontalRelacao.setBounds(primeiroClique.x, primeiroClique.y, 0, TAMANHO_LINHAS_RELACAO);
                linhaVerticalRelacao.setBounds(primeiroClique.x, primeiroClique.y, TAMANHO_LINHAS_RELACAO, 0);

                permitirCriarRelacao = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (criacaoDeRelacaoAcontecendo) {
                    indicadorRelacao.setVisible(false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (criacaoDeRelacaoAcontecendo && !movimentacaoPermitida) {
                    indicadorRelacao.setVisible(true);
                    indicadorRelacao.setBounds(
                        e.getX() - TAMANHO_INDICADOR/2, e.getY() - TAMANHO_INDICADOR/2,
                        TAMANHO_INDICADOR, TAMANHO_INDICADOR
                    );
                    indicadorRelacao.repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (criacaoDeRelacaoAcontecendo && !movimentacaoPermitida) {
                    indicadorRelacao.setVisible(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (criacaoDeRelacaoAcontecendo) {
                    indicadorRelacao.setVisible(false);
                    revalidarQuadroBranco();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (permitirCriarRelacao) {
                    permitirCriarRelacao = false;
                    painelCancelarRelacao.setVisible(false);
                    painelCriarRelacao.setVisible(true);

                    if (linhaVerticalRelacao.getHeight() <= TAMANHO_MINIMO_LINHAS_RELACAO) {
                        painelQuadroBranco.remove(linhaVerticalRelacao);
                    }

                    if (linhaHorizontalRelacao.getWidth() <= TAMANHO_MINIMO_LINHAS_RELACAO) {
                        painelQuadroBranco.remove(linhaHorizontalRelacao);
                    }

                    criarRelacao(e.getPoint());
                    revalidarQuadroBranco();

                } else if (criacaoDeRelacaoAcontecendo && !movimentacaoPermitida) {
                    criarPaineisDaRelacao(e.getPoint());
                }

                if (criacaoDeRelacaoAcontecendo && !movimentacaoPermitida) {
                    for (RelacaoUML relacao : diagramaAtual.getRelacoesUML()) {
                        if (relacao.getTipoDeRelacao() == tipoDeRelacaoSendoCriada) {
                            relacao.mostrarPontoDeExtensao(!permitirCriarRelacao);
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (criacaoDeRelacaoAcontecendo && !movimentacaoPermitida && permitirCriarRelacao) {
                    linhaHorizontalRelacao.setSize(
                        Math.abs(primeiroClique.x - e.getX()), TAMANHO_LINHAS_RELACAO
                    );

                    if (e.getX() < primeiroClique.x) {
                        linhaHorizontalRelacao.setLocation(e.getX() - TAMANHO_LINHAS_RELACAO/2, primeiroClique.y);
                        linhaVerticalRelacao.setLocation(e.getX() - TAMANHO_LINHAS_RELACAO/2, primeiroClique.y);
                    } else {
                        linhaHorizontalRelacao.setLocation(primeiroClique.x - TAMANHO_LINHAS_RELACAO/2, primeiroClique.y);
                        linhaHorizontalRelacao.setSize(
                            Math.abs(e.getX() - linhaHorizontalRelacao.getX()), TAMANHO_LINHAS_RELACAO
                        );
                        linhaVerticalRelacao.setLocation(
                            linhaHorizontalRelacao.getX() + linhaHorizontalRelacao.getWidth() - TAMANHO_LINHAS_RELACAO/2,
                            primeiroClique.y
                        );
                    }

                    linhaVerticalRelacao.setLocation(linhaVerticalRelacao.getX(), Math.min(e.getY(), primeiroClique.y));
                    linhaVerticalRelacao.setSize(
                        TAMANHO_LINHAS_RELACAO, Math.abs(primeiroClique.y - e.getY())+ TAMANHO_LINHAS_RELACAO
                    );
                }

                if (criacaoDeRelacaoAcontecendo) {
                    indicadorRelacao.setBounds(
                        e.getX() - TAMANHO_INDICADOR/2, e.getY() - TAMANHO_INDICADOR/2,
                        TAMANHO_INDICADOR, TAMANHO_INDICADOR
                    );
                    revalidarQuadroBranco();
                }
            }
        };

        painelQuadroBranco.add(indicadorRelacao);
        painelQuadroBranco.addMouseListener(adapterCriarRelacao);
        painelQuadroBranco.addMouseMotionListener(adapterCriarRelacao);
    }

    private void inicializarPainelAcoesQuadroBranco() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JLabel labelCursor = new JLabel();
        labelCursor.setIcon(gerenciadorDeRecursos.getImagem("icone_cursor"));
        labelCursor.setOpaque(true);
        labelCursor.setBackground(gerenciadorDeRecursos.getColor("gainsboro"));
        labelCursor.setToolTipText(gerenciadorDeRecursos.getString("tooltip_modificar_componentes"));

        // ----------------------------------------------------------------------------

        JLabel labelMover = new JLabel();
        labelMover.setIcon(gerenciadorDeRecursos.getImagem("icone_mover"));
        labelMover.setOpaque(true);
        labelMover.setBackground(gerenciadorDeRecursos.getColor("white"));
        labelMover.setToolTipText(gerenciadorDeRecursos.getString("tooltip_mover_quadro"));

        // ----------------------------------------------------------------------------

        JLabel labelUndo = new JLabel();
        labelUndo.setIcon(gerenciadorDeRecursos.getImagem("icone_undo_ativado"));
        labelUndo.setDisabledIcon(gerenciadorDeRecursos.getImagem("icone_undo_desativado"));
        labelUndo.setOpaque(true);
        labelUndo.setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
        labelUndo.setEnabled(false);
        labelUndo.setToolTipText(gerenciadorDeRecursos.getString("tooltip_desfazer_acao"));

        // ----------------------------------------------------------------------------

        JLabel labelRedo = new JLabel();
        labelRedo.setIcon(gerenciadorDeRecursos.getImagem("icone_redo_ativado"));
        labelRedo.setDisabledIcon(gerenciadorDeRecursos.getImagem("icone_redo_desativado"));
        labelRedo.setOpaque(true);
        labelRedo.setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
        labelRedo.setEnabled(false);
        labelRedo.setToolTipText(gerenciadorDeRecursos.getString("tooltip_refazer_acao"));

        // ----------------------------------------------------------------------------

        JLabel labelSeparadorOpcoes = new JLabel("");
        labelSeparadorOpcoes.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, gerenciadorDeRecursos.getColor("platinum")),
            BorderFactory.createMatteBorder(0, 0, 0, 1, gerenciadorDeRecursos.getColor("silver_sand")))
        );

        painelAcoesQuadroBranco.setLayout(new MigLayout("insets 5 10 5 10"));
        painelAcoesQuadroBranco.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelAcoesQuadroBranco.add(labelCursor, "gapright 5");
        painelAcoesQuadroBranco.add(labelMover, "gapright 5");
        painelAcoesQuadroBranco.add(labelSeparadorOpcoes, "grow, gapright 5");
        painelAcoesQuadroBranco.add(labelUndo, "gapright 5");
        painelAcoesQuadroBranco.add(labelRedo, "gapright 5");
        painelAcoesQuadroBranco.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                for (EstruturaUML<?> componente : diagramaAtual.getEstruturasUML()) {
                    componente.removerPainelDeOpcoes();
                }
            }
        });

        // ----------------------------------------------------------------------------

        labelCursor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelCursor.setBackground(gerenciadorDeRecursos.getColor("gainsboro"));
                labelMover.setBackground(gerenciadorDeRecursos.getColor("white"));

                movimentacaoPermitida = false;

                painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (movimentacaoPermitida) {
                    labelCursor.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (movimentacaoPermitida) {
                    labelCursor.setBackground(gerenciadorDeRecursos.getColor("white"));
                }
            }
        });

        labelMover.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labelMover.setBackground(gerenciadorDeRecursos.getColor("gainsboro"));
                labelCursor.setBackground(gerenciadorDeRecursos.getColor("white"));

                movimentacaoPermitida = true;

                painelQuadroBranco.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!movimentacaoPermitida) {
                    labelMover.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!movimentacaoPermitida) {
                    labelMover.setBackground(gerenciadorDeRecursos.getColor("white"));
                }
            }

        });

        labelUndo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int numeroDeFramesVisiveis = 0;

                for (Frame frame : Frame.getFrames()) {
                    if (frame.isVisible()) {
                        numeroDeFramesVisiveis++;
                    }
                }

                if (labelUndo.isEnabled() && !criacaoDeRelacaoAcontecendo && numeroDeFramesVisiveis == 1) {
                    alteracoesDeComponentes.get(indexAlteracao).desfazerAlteracao();
                    indexAlteracao--;

                    diagramaAtual.setDiagramaSalvo(false);
                    setVisibilidadeDiagramaNaoSalvo(true);

                    if (indexAlteracao == -1) {
                        labelUndo.setEnabled(false);
                        labelUndo.setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
                    }

                    if (!labelRedo.isEnabled()) {
                        labelRedo.setEnabled(true);
                        labelRedo.setBackground(gerenciadorDeRecursos.getColor("white"));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (labelUndo.isEnabled()) {
                    labelUndo.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (labelUndo.isEnabled()) {
                    labelUndo.setBackground(gerenciadorDeRecursos.getColor("white"));
                }
            }

        });

        labelRedo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int numeroDeFramesVisiveis = 0;

                for (Frame frame : Frame.getFrames()) {
                    if (frame.isVisible()) {
                        numeroDeFramesVisiveis++;
                    }
                }

                if (labelRedo.isEnabled() && !criacaoDeRelacaoAcontecendo && numeroDeFramesVisiveis == 1) {
                    indexAlteracao++;
                    alteracoesDeComponentes.get(indexAlteracao).refazerAlteracao();

                    diagramaAtual.setDiagramaSalvo(false);
                    setVisibilidadeDiagramaNaoSalvo(true);

                    if (indexAlteracao == alteracoesDeComponentes.size() - 1) {
                        labelRedo.setEnabled(false);
                        labelRedo.setBackground(gerenciadorDeRecursos.getColor("anti_flash_white"));
                    }

                    if (!labelUndo.isEnabled()) {
                        labelUndo.setBackground(gerenciadorDeRecursos.getColor("white"));
                        labelUndo.setEnabled(true);
                    }

                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (labelRedo.isEnabled()) {
                    labelRedo.setBackground(gerenciadorDeRecursos.getColor("platinum"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (labelRedo.isEnabled()) {
                    labelRedo.setBackground(gerenciadorDeRecursos.getColor("white"));
                }
            }

        });
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
