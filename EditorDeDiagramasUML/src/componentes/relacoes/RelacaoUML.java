package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.modelos.relacoes.Relacao;
import interfacegrafica.AreaDeDiagramas;
import modelos.OrientacaoDeRelacao;
import modelos.TipoDeRelacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * TODO
 */
public abstract class RelacaoUML {
    private Relacao modeloAtual = new Relacao();
    private final AreaDeDiagramas areaDeDiagramas;
    private final ArrayList<JPanel> linhasDaRelacao;
    // TODO - o que eh lado A e B
    private final JPanel painelSetaA;
    private final JPanel painelSetaB;
    private final Point[] pontosDaSetaA = { new Point(), new Point(), new Point() };
    private final Point[] pontosDaSetaB = { new Point(), new Point(), new Point() };
    private Point pontoLadoA;
    private OrientacaoDeRelacao orientacaoLadoA;
    private final OrientacaoDeRelacao orientacaoLadoB;
    private final JPanel pontoDeExtensao;
    private Consumer<RelacaoUML> aoClicarPontoDeExtensao;
    private final TipoDeRelacao tipoDeRelacao;
    private final JFrame frameGerenciarRelacao = new JFrame();
    private boolean relacaoSelecionada;
    public static final int TAMANHO_LINHAS_RELACAO = 4;
    public static final int TAMANHO_SETA = 14;
    public static final int TAMANHO_PONTO_DE_EXTENSAO = 14;
    public static final Color COR_PADRAO = GerenciadorDeRecursos.getInstancia().getColor("dark_charcoal");
    public static final Color COR_SELECIONAR = GerenciadorDeRecursos.getInstancia().getColor("red");
    private static final Color COR_PONTO_DE_EXTENSAO = GerenciadorDeRecursos.getInstancia().getColor("green");

    public RelacaoUML(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point pontoInicialDaRelacao, Point pontoFinalDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        this.linhasDaRelacao = linhasDaRelacao;
        this.areaDeDiagramas = areaDeDiagramas;
        this.tipoDeRelacao = tipoDeRelacao;
        this.pontoLadoA = pontoFinalDaRelacao;

        MouseAdapter adapterGerenciarRelacao = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    selecionarRelacao(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    selecionarRelacao(false);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    mostrarFrameGerenciarRelacao(true);
                }
            }
        };

        for (JPanel linha : linhasDaRelacao) {
            linha.addMouseListener(adapterGerenciarRelacao);
        }

        painelSetaA = new JPanel() {
            public void paintComponent(Graphics g) {
                desenharSeta((Graphics2D) g, pontosDaSetaA);
            }
        };

        painelSetaB = new JPanel() {
            public void paintComponent(Graphics g) {
                desenharSeta((Graphics2D) g, pontosDaSetaB);
            }
        };

        painelSetaA.addMouseListener(adapterGerenciarRelacao);
        painelSetaB.addMouseListener(adapterGerenciarRelacao);

        // index 1 para que a seta fique embaixo do ponto de extensao
        areaDeDiagramas.addComponenteAoQuadro(painelSetaA, 1);
        areaDeDiagramas.addComponenteAoQuadro(painelSetaB, 1);

        orientacaoLadoA = calcularOrientacaoDeLinha(pontoLadoA, linhasDaRelacao.getLast());
        orientacaoLadoB = calcularOrientacaoDeLinha(pontoInicialDaRelacao, linhasDaRelacao.getFirst());

        pontoDeExtensao = new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(COR_PONTO_DE_EXTENSAO);
                g.fillOval(0, 0, TAMANHO_PONTO_DE_EXTENSAO, TAMANHO_PONTO_DE_EXTENSAO);
            }
        };
        pontoDeExtensao.setVisible(false);
        pontoDeExtensao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pontoDeExtensao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                aoClicarPontoDeExtensao();
            }
        });

        pontoDeExtensao.setSize(TAMANHO_PONTO_DE_EXTENSAO, TAMANHO_PONTO_DE_EXTENSAO);

        areaDeDiagramas.addComponenteAoQuadro(pontoDeExtensao, 0);

        aplicarEstiloDasLinhas(linhasDaRelacao);

        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle(GerenciadorDeRecursos.getInstancia().getString("configurar_relacao"));
        frameGerenciarRelacao.setIconImage(GerenciadorDeRecursos.getInstancia().getImagem("icone_configurar_48").getImage());
        frameGerenciarRelacao.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        initFrameGerenciarRelacao();
    }

    private OrientacaoDeRelacao calcularOrientacaoDeLinha(Point pontoFinalDaLinha, JPanel linha) {
        OrientacaoDeRelacao orientacaoDaLinha;

        if (linha.getWidth() == TAMANHO_LINHAS_RELACAO && linha.getY() < pontoFinalDaLinha.y) {
            orientacaoDaLinha = OrientacaoDeRelacao.SUL;
        } else if (linha.getWidth() == TAMANHO_LINHAS_RELACAO && linha.getY() == pontoFinalDaLinha.y) {
            orientacaoDaLinha = OrientacaoDeRelacao.NORTE;
        } else if (linha.getX() + TAMANHO_LINHAS_RELACAO / 2 == pontoFinalDaLinha.x) {
            orientacaoDaLinha = OrientacaoDeRelacao.OESTE;
        } else {
            orientacaoDaLinha = OrientacaoDeRelacao.LESTE;
        }

        return orientacaoDaLinha;
    }

    private void selecionarRelacao(boolean selecionar) {
        relacaoSelecionada = selecionar;

        for (JPanel linha : linhasDaRelacao) {
            linha.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            linha.setBackground(selecionar ? COR_SELECIONAR : COR_PADRAO);
        }

        painelSetaA.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        painelSetaB.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));

        aplicarEstiloDasLinhas(linhasDaRelacao);

        areaDeDiagramas.revalidarQuadroBranco();
    }

    private void atualizarLocalizacaoPontoDeExtensao() {
        Point localizacaoPontoDeExtensao = new Point();
        Rectangle ancoraDoPonto = painelSetaA.isVisible() ? painelSetaA.getBounds() : linhasDaRelacao.getLast().getBounds();

        switch (orientacaoLadoA) {
            case SUL -> {
                localizacaoPontoDeExtensao.x = ancoraDoPonto.x + ((ancoraDoPonto.width - TAMANHO_PONTO_DE_EXTENSAO)/2);
                localizacaoPontoDeExtensao.y = ancoraDoPonto.y + ancoraDoPonto.height - TAMANHO_PONTO_DE_EXTENSAO/2;
            }
            case NORTE -> {
                localizacaoPontoDeExtensao.x = ancoraDoPonto.x + ((ancoraDoPonto.width - TAMANHO_PONTO_DE_EXTENSAO)/2);
                localizacaoPontoDeExtensao.y = ancoraDoPonto.y - TAMANHO_PONTO_DE_EXTENSAO/2;
            }
            case OESTE -> {
                localizacaoPontoDeExtensao.x = ancoraDoPonto.x - TAMANHO_PONTO_DE_EXTENSAO/2;
                localizacaoPontoDeExtensao.y = ancoraDoPonto.y + ((ancoraDoPonto.height - TAMANHO_PONTO_DE_EXTENSAO)/2);
            }
            case LESTE -> {
                localizacaoPontoDeExtensao.x = ancoraDoPonto.x + ancoraDoPonto.width - TAMANHO_PONTO_DE_EXTENSAO/2;
                localizacaoPontoDeExtensao.y = ancoraDoPonto.y + ((ancoraDoPonto.height - TAMANHO_PONTO_DE_EXTENSAO)/2);
            }
        }

        pontoDeExtensao.setLocation(localizacaoPontoDeExtensao);
    }

    private void aoClicarPontoDeExtensao() {
        if (painelSetaA.isVisible()) {
            // Como o ponto de extensao fica na ponta da seta A, se ela estiver visivel eh necessario estender a
            // linha da seta pelo tamanho da seta

            JPanel linhaSetaA = linhasDaRelacao.getLast();
            int TAMANHO_EXTRA = TAMANHO_SETA + TAMANHO_PONTO_DE_EXTENSAO/2;

            switch (orientacaoLadoA) {
                case SUL -> linhaSetaA.setSize(linhaSetaA.getWidth(), linhaSetaA.getHeight() + TAMANHO_EXTRA);
                case NORTE -> linhaSetaA.setBounds(
                    linhaSetaA.getX(), linhaSetaA.getY() - TAMANHO_EXTRA,
                    linhaSetaA.getWidth(), linhaSetaA.getHeight() + TAMANHO_EXTRA
                );
                case OESTE -> linhaSetaA.setBounds(
                    linhaSetaA.getX() - TAMANHO_EXTRA, linhaSetaA.getY(),
                    linhaSetaA.getWidth() + TAMANHO_EXTRA, linhaSetaA.getHeight()
                );
                case LESTE -> linhaSetaA.setSize(linhaSetaA.getWidth() + TAMANHO_EXTRA, linhaSetaA.getHeight());
            }

            painelSetaA.setVisible(false);
        }

        mostrarPontoDeExtensao(false);
        aoClicarPontoDeExtensao.accept(this);
    }

    private void atualizarPontosDeSeta(OrientacaoDeRelacao orientacaoDaSeta, Point[] pontosDaSeta) {
        switch (orientacaoDaSeta) {
            case SUL -> {
                pontosDaSeta[0].x = 0; pontosDaSeta[1].x = TAMANHO_SETA/2; pontosDaSeta[2].x = TAMANHO_SETA;
                pontosDaSeta[0].y = 0; pontosDaSeta[1].y = TAMANHO_SETA; pontosDaSeta[2].y = 0;
            }
            case NORTE -> {
                pontosDaSeta[0].x = 0; pontosDaSeta[1].x = TAMANHO_SETA/2; pontosDaSeta[2].x = TAMANHO_SETA;
                pontosDaSeta[0].y = TAMANHO_SETA; pontosDaSeta[1].y = 0; pontosDaSeta[2].y = TAMANHO_SETA;
            }
            case OESTE -> {
                pontosDaSeta[0].x = TAMANHO_SETA; pontosDaSeta[1].x = 0; pontosDaSeta[2].x = TAMANHO_SETA;
                pontosDaSeta[0].y = TAMANHO_SETA; pontosDaSeta[1].y = TAMANHO_SETA/2; pontosDaSeta[2].y = 0;
            }
            case LESTE -> {
                pontosDaSeta[0].x = 0; pontosDaSeta[1].x  = TAMANHO_SETA; pontosDaSeta[2].x  = 0;
                pontosDaSeta[0].y = TAMANHO_SETA; pontosDaSeta[1].y = TAMANHO_SETA/2; pontosDaSeta[2].y = 0;
            }
        }

        int MARGEM = 10;
        for (Point point : pontosDaSeta) {
            point.x += MARGEM;
            point.y += MARGEM;
        }
    }

    private void atualizarLocalizacaoDeSeta(JPanel painelSeta, JPanel linhaDaSeta, OrientacaoDeRelacao orientacaoDaSeta) {
        Point localizacaoDaSeta = new Point();

        switch (orientacaoDaSeta) {
            case SUL -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
                localizacaoDaSeta.y = linhaDaSeta.getY() + linhaDaSeta.getHeight();
            }
            case NORTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
                localizacaoDaSeta.y = linhaDaSeta.getY() - TAMANHO_SETA;
            }
            case OESTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() - TAMANHO_SETA;
                localizacaoDaSeta.y = linhaDaSeta.getY() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
            }
            case LESTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() + linhaDaSeta.getWidth();
                localizacaoDaSeta.y = linhaDaSeta.getY() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
            }
        }

        int MARGEM = 10;

        painelSeta.setBounds(
            localizacaoDaSeta.x - MARGEM, localizacaoDaSeta.y - MARGEM,
            TAMANHO_SETA + MARGEM * 2, TAMANHO_SETA + MARGEM * 2
        );
    }

    public void mostrarSetaLadoA(boolean mostrar) {
        if (mostrar) {
            atualizarPontosDeSeta(orientacaoLadoA, pontosDaSetaA);
            atualizarLocalizacaoDeSeta(painelSetaA, linhasDaRelacao.getLast(), orientacaoLadoA);
        }
        painelSetaA.setVisible(mostrar);
        modeloAtual.setMostrandoSetaA(mostrar);
    }

    public void mostrarSetaLadoB(boolean mostrar) {
        if (mostrar) {
            atualizarPontosDeSeta(orientacaoLadoB, pontosDaSetaB);
            atualizarLocalizacaoDeSeta(painelSetaB, linhasDaRelacao.getFirst(), orientacaoLadoB);
        }
        painelSetaB.setVisible(mostrar);
        modeloAtual.setMostrandoSetaB(mostrar);
    }

    public void inverterSeta() {
       if (modeloAtual.estaMostrandoSetaA()) {
           mostrarSetaLadoA(false);
           mostrarSetaLadoB(true);

       } else if (modeloAtual.estaMostrandoSetaB()) {
           mostrarSetaLadoA(true);
           mostrarSetaLadoB(false);
       }
    }

    public void adicionarRelacaoAoQuadroBranco() {
        areaDeDiagramas.addRelacaoAoQuadro(this);
        areaDeDiagramas.addComponenteAoQuadro(pontoDeExtensao, 0);
    }

    public void removerRelacaoDoQuadroBranco() {
        for (JPanel linha : linhasDaRelacao) {
            areaDeDiagramas.removerComponenteDoQuadro(linha);
        }

        areaDeDiagramas.removerComponenteDoQuadro(pontoDeExtensao);
        areaDeDiagramas.removerComponenteDoQuadro(painelSetaA);
        areaDeDiagramas.removerComponenteDoQuadro(painelSetaB);
        areaDeDiagramas.removerRelacaoDoDiagrama(this);
    }

    public void mostrarFrameGerenciarRelacao(boolean mostrar) {
        frameGerenciarRelacao.setVisible(mostrar);
        if (mostrar) {
            frameGerenciarRelacao.setLocationRelativeTo(null);
            frameGerenciarRelacao.requestFocusInWindow();
        }
    }

    public void mostrarPontoDeExtensao(boolean mostrar) {
        if (mostrar) {
            atualizarLocalizacaoPontoDeExtensao();
        }
        pontoDeExtensao.setVisible(mostrar);
    }

    public void estenderRelacao(ArrayList<JPanel> novosPaineis, Point ultimoPonto) {
        pontoLadoA = ultimoPonto;

        MouseAdapter adapterGerenciarRelacao = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    selecionarRelacao(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    selecionarRelacao(false);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (areaDeDiagramas.componentesEstaoHabilitados()) {
                    mostrarFrameGerenciarRelacao(true);
                }
            }
        };

        for (JPanel linha : novosPaineis) {
            linha.addMouseListener(adapterGerenciarRelacao);
        }

        linhasDaRelacao.addAll(novosPaineis);

        orientacaoLadoA = calcularOrientacaoDeLinha(pontoLadoA, linhasDaRelacao.getLast());

        mostrarSetaLadoA(modeloAtual.estaMostrandoSetaA());

        atualizarLocalizacaoPontoDeExtensao();

        aplicarEstiloDasLinhas(linhasDaRelacao);
    }

    public void setAoClicarPontoDeExtensao(Consumer<RelacaoUML> aoClicarPontoDeExtensao) {
        this.aoClicarPontoDeExtensao = aoClicarPontoDeExtensao;
    }

    public boolean relacaoEstaSelecionada() {
        return relacaoSelecionada;
    }

    public ArrayList<JPanel> getLinhasDaRelacao() {
        return linhasDaRelacao;
    }

    public AreaDeDiagramas getAreaDeDiagramas() {
        return areaDeDiagramas;
    }

    public TipoDeRelacao getTipoDeRelacao() {
        return tipoDeRelacao;
    }

    protected JFrame getFrameGerenciarRelacao() {
        return frameGerenciarRelacao;
    }

    /**
     * Retorna o ponto de inicio onde a relacao deve ser estendida. Esse ponto corresponde mais ou menos
     * com o inicio da primeira linha.
     */
    public Point getLocalizacaoDeExtensao() {
        Point localizacaoDeExtensao = linhasDaRelacao.getLast().getLocation();

        switch (orientacaoLadoA) {
            case NORTE, OESTE -> localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            case SUL -> {
                localizacaoDeExtensao.y += linhasDaRelacao.getLast().getHeight() - TAMANHO_LINHAS_RELACAO;
                localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            }
        }

        return localizacaoDeExtensao;
    }

    /*
    private mostrarMultiplicidadeLadoA()
    private mostrarMultiplicidadeLadoB()

    private mostrarNome()

    private mostrarDirecao()*/

    protected abstract void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas);

    protected abstract void desenharSeta(Graphics2D g2, Point[] pontosDaSeta);

    protected abstract void initFrameGerenciarRelacao();
}
