package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import interfacegrafica.AreaDeDiagramas;
import modelos.OrientacaoDeRelacao;
import modelos.TipoDeRelacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * TODO
 */
public abstract class RelacaoUML {
    private final AreaDeDiagramas areaDeDiagramas;
    private final ArrayList<JPanel> linhasDaRelacao;
    private Point pontoInicial;
    private Point pontoFinal;
    private final JPanel pontoDeExtensao;
    private Consumer<RelacaoUML> aoClicarPontoDeExtensao;
    private OrientacaoDeRelacao orientacaoDaRelacao;
    private final TipoDeRelacao tipoDeRelacao;
    private boolean relacaoSelecionada;
    private Consumer<Boolean> emMudancaDeSelecao;
    private final JFrame frameGerenciarRelacao = new JFrame();
    public static final int TAMANHO_LINHAS_RELACAO = 4;
    public static final int TAMANHO_SETA = 14;
    public static final int TAMANHO_PONTO_DE_EXTENSAO = 14;
    public static final Color COR_PADRAO = GerenciadorDeRecursos.getInstancia().getColor("dark_charcoal");
    public static final Color COR_SELECIONAR = GerenciadorDeRecursos.getInstancia().getColor("red");
    public static final Color COR_PONTO_DE_EXTENSAO = GerenciadorDeRecursos.getInstancia().getColor("green");

    public RelacaoUML(
        ArrayList<JPanel> linhasDaRelacao, AreaDeDiagramas areaDeDiagramas,
        Point primeiroPontoDaRelacao, Point ultimoPontoDaRelacao, TipoDeRelacao tipoDeRelacao
    ) {
        this.linhasDaRelacao = linhasDaRelacao;
        this.areaDeDiagramas = areaDeDiagramas;
        this.tipoDeRelacao = tipoDeRelacao;
        this.pontoInicial = primeiroPontoDaRelacao;
        this.pontoFinal = ultimoPontoDaRelacao;

        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle(gerenciadorDeRecursos.getString("configurar_relacao"));
        frameGerenciarRelacao.setIconImage(gerenciadorDeRecursos.getImagem("icone_configurar_48").getImage());

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

        calcularOrientacaoDaRelacao();

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
        areaDeDiagramas.revalidarQuadroBranco();

        initFrameGerenciarRelacao();
    }

    private void calcularOrientacaoDaRelacao() {
        JPanel ultimaLinha = linhasDaRelacao.get(linhasDaRelacao.size() - 1);

        if (ultimaLinha.getWidth() == TAMANHO_LINHAS_RELACAO && ultimaLinha.getY() < pontoFinal.y) {
            orientacaoDaRelacao = OrientacaoDeRelacao.SUL;
        } else if (ultimaLinha.getWidth() == TAMANHO_LINHAS_RELACAO && ultimaLinha.getY() == pontoFinal.y) {
            orientacaoDaRelacao = OrientacaoDeRelacao.NORTE;
        } else if (ultimaLinha.getX() + TAMANHO_LINHAS_RELACAO / 2 == pontoFinal.x) {
            orientacaoDaRelacao = OrientacaoDeRelacao.OESTE;
        } else {
            orientacaoDaRelacao = OrientacaoDeRelacao.LESTE;
        }
    }

    public void inverterSentidoDaRelacao() {
        Point temp = pontoInicial;
        pontoInicial = pontoFinal;
        pontoFinal = temp;

        Collections.reverse(linhasDaRelacao);

        calcularOrientacaoDaRelacao();
        aplicarEstiloDaRelacao();
        atualizarLocalizacaoPontoDeExtensao();
    }

    public void excluirRelacao() {
        areaDeDiagramas.removerRelacaoDoQuadro(this);
        areaDeDiagramas.removerComponenteDoQuadro(pontoDeExtensao);
    }

    public void atualizarLocalizacaoPontoDeExtensao() {
        pontoDeExtensao.setLocation(getLocalizacaoDoPontoDeExtensao());
    }

    public void mostrarPontoDeExtensao(boolean mostrar) {
        pontoDeExtensao.setVisible(mostrar);
    }

    public void mostrarFrameGerenciarRelacao(boolean mostrar) {
        frameGerenciarRelacao.setVisible(mostrar);
        if (mostrar) {
            frameGerenciarRelacao.setLocationRelativeTo(null);
            frameGerenciarRelacao.requestFocusInWindow();
        }
    }

    public void selecionarRelacao(boolean selecionar) {
        for (JPanel linha : linhasDaRelacao) {
            linha.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            linha.setBackground(selecionar ? COR_SELECIONAR : COR_PADRAO);
        }

        relacaoSelecionada = selecionar;

        emMudancaDeSelecao.accept(selecionar);
    }

    public void aoClicarPontoDeExtensao() {
        mostrarPontoDeExtensao(false);
        aoClicarPontoDeExtensao.accept(this);
    }

    public void estenderRelacao(ArrayList<JPanel> novosPaineis, Point ultimoPonto) {
        this.pontoFinal = ultimoPonto;

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

        calcularOrientacaoDaRelacao();

        aplicarEstiloDaRelacao();

        atualizarLocalizacaoPontoDeExtensao();
    }

    public void setEmMudancaDeSelecao(Consumer<Boolean> emMudancaDeSelecao) {
        this.emMudancaDeSelecao = emMudancaDeSelecao;
    }

    public void setAoClicarPontoDeExtensao(Consumer<RelacaoUML> aoClicarPontoDeExtensao) {
        this.aoClicarPontoDeExtensao = aoClicarPontoDeExtensao;
    }

    public boolean relacaoEstaSelecionada() {
        return relacaoSelecionada;
    }

    public boolean relacaoEstaHabilitada() {
        return areaDeDiagramas.componentesEstaoHabilitados();
    }

    public ArrayList<JPanel> getLinhasDaRelacao() {
        return linhasDaRelacao;
    }

    public OrientacaoDeRelacao getOrientacaoDaRelacao() {
        return orientacaoDaRelacao;
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
        JPanel primeiraLinha = getLinhasDaRelacao().get(getLinhasDaRelacao().size() - 1);
        Point localizacaoDeExtensao = primeiraLinha.getLocation();

        switch (getOrientacaoDaRelacao()) {
            case NORTE, OESTE -> localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            case SUL -> {
                localizacaoDeExtensao.y += primeiraLinha.getHeight() - TAMANHO_LINHAS_RELACAO;
                localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            }
        }

        return localizacaoDeExtensao;
    }

    public abstract void aplicarEstiloDaRelacao();

    public abstract Point getLocalizacaoDoPontoDeExtensao();

    protected abstract void initFrameGerenciarRelacao();
}
