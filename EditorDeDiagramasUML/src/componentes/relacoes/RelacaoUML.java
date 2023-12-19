package componentes.relacoes;

import auxiliares.GerenciadorDeRecursos;
import componentes.alteracoes.estruturas.EstruturaModificada;
import componentes.alteracoes.relacoes.RelacaoModificada;
import componentes.estruturas.AnotacaoUML;
import componentes.modelos.relacoes.DirecaoDeRelacao;
import componentes.modelos.relacoes.Relacao;
import interfacegrafica.AreaDeDiagramas;
import componentes.modelos.relacoes.OrientacaoDeSeta;
import componentes.modelos.relacoes.TipoDeRelacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private final JLabel labelNome = new JLabel();
    private final JLabel labelMultiplicidadeA = new JLabel();
    private final JLabel labelMultiplicidadeB = new JLabel();

    // TODO - o que eh lado A e B
    private final JPanel painelSetaA;
    private final JPanel painelSetaB;
    private final Point[] pontosDaSetaA = { new Point(), new Point(), new Point() };
    private final Point[] pontosDaSetaB = { new Point(), new Point(), new Point() };
    private OrientacaoDeSeta orientacaoLadoA;
    private final OrientacaoDeSeta orientacaoLadoB;
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
        modeloAtual.setLinhasDaRelacao(linhasDaRelacao);
        modeloAtual.setPontoLadoA(pontoFinalDaRelacao);
        this.areaDeDiagramas = areaDeDiagramas;
        this.tipoDeRelacao = tipoDeRelacao;

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

        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
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

        orientacaoLadoA = calcularOrientacaoDeLinha(pontoFinalDaRelacao, modeloAtual.getLinhasDaRelacao().getLast());
        orientacaoLadoB = calcularOrientacaoDeLinha(pontoInicialDaRelacao, modeloAtual.getLinhasDaRelacao().getFirst());

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

        labelNome.setFont(GerenciadorDeRecursos.getInstancia().getRobotoBlack(15));
        labelMultiplicidadeA.setFont(GerenciadorDeRecursos.getInstancia().getRobotoBlack(15));
        labelMultiplicidadeB.setFont(GerenciadorDeRecursos.getInstancia().getRobotoBlack(15));

        areaDeDiagramas.addComponenteAoQuadro(pontoDeExtensao, 0);
        // index 1 para que fiquem embaixo do ponto de extensao
        areaDeDiagramas.addComponenteAoQuadro(painelSetaA, 1);
        areaDeDiagramas.addComponenteAoQuadro(painelSetaB, 1);
        areaDeDiagramas.addComponenteAoQuadro(labelNome, 1);
        areaDeDiagramas.addComponenteAoQuadro(labelMultiplicidadeA, 1);
        areaDeDiagramas.addComponenteAoQuadro(labelMultiplicidadeB, 1);

        aplicarEstiloDasLinhas(modeloAtual.getLinhasDaRelacao());

        frameGerenciarRelacao.setResizable(false);
        frameGerenciarRelacao.setTitle(GerenciadorDeRecursos.getInstancia().getString("configurar_relacao"));
        frameGerenciarRelacao.setIconImage(GerenciadorDeRecursos.getInstancia().getImagem("icone_configurar_48").getImage());
        frameGerenciarRelacao.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frameGerenciarRelacao.addComponentListener(new ComponentAdapter() {
            Relacao modeloAntesDeAlteracoes;

            @Override
            public void componentShown(ComponentEvent e) {
                modeloAntesDeAlteracoes = modeloAtual.copiar();
                for (JPanel linha : modeloAntesDeAlteracoes.getLinhasDaRelacao()) {
                    linha.setBackground(GerenciadorDeRecursos.getInstancia().getColor("dark_charcoal"));
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (modeloAntesDeAlteracoes.ehDiferente(modeloAtual)) {
                    areaDeDiagramas.addAlteracaoDeComponente(new RelacaoModificada(
                        RelacaoUML.this,
                        modeloAntesDeAlteracoes.copiar(),
                        modeloAtual.copiar()
                    ));
                }
            }
        });

        initFrameGerenciarRelacao();
    }

    private OrientacaoDeSeta calcularOrientacaoDeLinha(Point pontoFinalDaLinha, JPanel linha) {
        OrientacaoDeSeta orientacaoDaLinha;

        if (linha.getWidth() == TAMANHO_LINHAS_RELACAO && linha.getY() < pontoFinalDaLinha.y) {
            orientacaoDaLinha = OrientacaoDeSeta.SUL;
        } else if (linha.getWidth() == TAMANHO_LINHAS_RELACAO && linha.getY() == pontoFinalDaLinha.y) {
            orientacaoDaLinha = OrientacaoDeSeta.NORTE;
        } else if (linha.getX() + TAMANHO_LINHAS_RELACAO / 2 == pontoFinalDaLinha.x) {
            orientacaoDaLinha = OrientacaoDeSeta.OESTE;
        } else {
            orientacaoDaLinha = OrientacaoDeSeta.LESTE;
        }

        return orientacaoDaLinha;
    }

    private void selecionarRelacao(boolean selecionar) {
        relacaoSelecionada = selecionar;

        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
            linha.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            linha.setBackground(selecionar ? COR_SELECIONAR : COR_PADRAO);
        }

        painelSetaA.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        painelSetaB.setCursor(Cursor.getPredefinedCursor(selecionar ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));

        aplicarEstiloDasLinhas(modeloAtual.getLinhasDaRelacao());

        areaDeDiagramas.revalidarQuadroBranco();
    }

    private void atualizarLocalizacaoPontoDeExtensao() {
        Point localizacaoPontoDeExtensao = new Point();
        Rectangle ancoraDoPonto = modeloAtual.estaMostrandoSetaA() ? painelSetaA.getBounds() : modeloAtual.getLinhasDaRelacao().getLast().getBounds();

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
        if (modeloAtual.estaMostrandoSetaA()) {
            painelSetaA.setVisible(false);
        }

        mostrarPontoDeExtensao(false);
        aoClicarPontoDeExtensao.accept(this);
    }

    private void atualizarPontosDeSeta(OrientacaoDeSeta orientacaoDaSeta, Point[] pontosDaSeta) {
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

    private void atualizarLocalizacaoDeSeta(JPanel painelSeta, JPanel linhaDaSeta, OrientacaoDeSeta orientacaoDaSeta) {
        Point localizacaoDaSeta = new Point();

        switch (orientacaoDaSeta) {
            case SUL -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
                localizacaoDaSeta.y = linhaDaSeta.getY() + linhaDaSeta.getHeight() - TAMANHO_SETA;
            }
            case NORTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
                localizacaoDaSeta.y = linhaDaSeta.getY();
            }
            case OESTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX();
                localizacaoDaSeta.y = linhaDaSeta.getY() - ((TAMANHO_SETA - TAMANHO_LINHAS_RELACAO)/2);
            }
            case LESTE -> {
                localizacaoDaSeta.x = linhaDaSeta.getX() + linhaDaSeta.getWidth() - TAMANHO_SETA;
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
            atualizarLocalizacaoDeSeta(painelSetaA, modeloAtual.getLinhasDaRelacao().getLast(), orientacaoLadoA);
        }
        painelSetaA.setVisible(mostrar);
        modeloAtual.setMostrandoSetaA(mostrar);
    }

    public void mostrarSetaLadoB(boolean mostrar) {
        if (mostrar) {
            atualizarPontosDeSeta(orientacaoLadoB, pontosDaSetaB);
            atualizarLocalizacaoDeSeta(painelSetaB, modeloAtual.getLinhasDaRelacao().getFirst(), orientacaoLadoB);
        }
        painelSetaB.setVisible(mostrar);
        modeloAtual.setMostrandoSetaB(mostrar);
    }

    public void mostrarNome(boolean mostrar) {
        if (mostrar) {
            atualizarLocalizacaoDoNome();
        }

        labelNome.setVisible(mostrar);
    }

    public void setNome(String nome) {
        modeloAtual.setNome(nome);
        labelNome.setText(nome);
    }

    private void atualizarLocalizacaoDoNome() {
        JPanel maiorLinha = modeloAtual.getLinhasDaRelacao().getFirst();

        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
            int tamanhoDaLinha = 0;

            if (linha.getWidth() == TAMANHO_LINHAS_RELACAO) {
                // Se a linha for uma linha vertical
                tamanhoDaLinha = linha.getHeight();
            } else {
                // Se a linha for horizontal
                tamanhoDaLinha = linha.getWidth();
            }

            if (maiorLinha.getWidth() == TAMANHO_LINHAS_RELACAO) {
                maiorLinha = (maiorLinha.getHeight() > tamanhoDaLinha)? maiorLinha : linha;
            } else {
                maiorLinha = (maiorLinha.getWidth() > tamanhoDaLinha)? maiorLinha : linha;
            }
        }

        Point localizaoNome;

        int MARGEM = 12;

        if (maiorLinha.getWidth() == TAMANHO_LINHAS_RELACAO) {
            // Se a linha do meio for uma linha vertical
            localizaoNome = maiorLinha.getLocation();
            localizaoNome.translate(
                TAMANHO_LINHAS_RELACAO + MARGEM,
                maiorLinha.getHeight() / 2 - labelNome.getPreferredSize().height / 2
            );

        } else {
            // Se a linha for horizontal
            localizaoNome = maiorLinha.getLocation();
            localizaoNome.translate(
                maiorLinha.getWidth() / 2 - labelNome.getPreferredSize().height / 2,
                -labelNome.getPreferredSize().height
            );
        }

        labelNome.setLocation(localizaoNome);
        labelNome.setSize(labelNome.getPreferredSize());
    }

    public void mostrarMultiplicidadeLadoA(boolean mostrar) {
        if (mostrar) {
            atualizarLocalizacaoDeMultiplicidade(labelMultiplicidadeA, modeloAtual.getLinhasDaRelacao().getLast(), orientacaoLadoA);
        }

        labelMultiplicidadeA.setVisible(mostrar);
    }

    public void setMultiplicidadeLadoA(String multiplicidade) {
        modeloAtual.setMultiplicidadeLadoA(multiplicidade);
        labelMultiplicidadeA.setText(multiplicidade);
    }

    public void mostrarMultiplicidadeLadoB(boolean mostrar) {
        if (mostrar) {
            atualizarLocalizacaoDeMultiplicidade(labelMultiplicidadeB, modeloAtual.getLinhasDaRelacao().getFirst(), orientacaoLadoB);
        }

        labelMultiplicidadeB.setVisible(mostrar);
    }

    public void setMultiplicidadeLadoB(String multiplicidade) {
        modeloAtual.setMultiplicidadeLadoB(multiplicidade);
        labelMultiplicidadeB.setText(multiplicidade);
    }

    private void atualizarLocalizacaoDeMultiplicidade(
        JLabel labelMultiplicidade, JPanel linhaDaMultiplicidade, OrientacaoDeSeta orientacaoDaLinha
    ) {
        Point localizacaoMultiplicidade = linhaDaMultiplicidade.getLocation();

        int MARGEM_VERTICAL = 2;
        int MARGEM_HORIZONTAL = 14;

        switch (orientacaoDaLinha) {
            case NORTE -> localizacaoMultiplicidade.translate(
                TAMANHO_LINHAS_RELACAO + MARGEM_HORIZONTAL,
                MARGEM_VERTICAL
            );
            case SUL -> localizacaoMultiplicidade.translate(
                TAMANHO_LINHAS_RELACAO + MARGEM_HORIZONTAL,
                linhaDaMultiplicidade.getHeight() - labelMultiplicidade.getPreferredSize().height - MARGEM_VERTICAL
            );
            case OESTE -> localizacaoMultiplicidade.translate(
                MARGEM_HORIZONTAL,
                labelMultiplicidade.getPreferredSize().height + MARGEM_VERTICAL
            );
            case LESTE -> localizacaoMultiplicidade.translate(
                linhaDaMultiplicidade.getWidth() - labelMultiplicidade.getPreferredSize().width - MARGEM_HORIZONTAL,
                labelMultiplicidade.getPreferredSize().height + MARGEM_VERTICAL
            );
        }

        labelMultiplicidade.setLocation(localizacaoMultiplicidade);
        labelMultiplicidade.setSize(labelMultiplicidade.getPreferredSize());
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
        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
            areaDeDiagramas.addComponenteAoQuadro(linha);
        }

        areaDeDiagramas.addComponenteAoQuadro(painelSetaA, 0);
        areaDeDiagramas.addComponenteAoQuadro(painelSetaB, 0);
        areaDeDiagramas.addComponenteAoQuadro(pontoDeExtensao, 0);
        areaDeDiagramas.addComponenteAoQuadro(labelNome);
        areaDeDiagramas.addComponenteAoQuadro(labelMultiplicidadeA);
        areaDeDiagramas.addComponenteAoQuadro(labelMultiplicidadeB);
        areaDeDiagramas.removerRelacaoDoDiagrama(this);

        areaDeDiagramas.addRelacaoAoDiagrama(this);
    }

    public void removerRelacaoDoQuadroBranco() {
        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
            areaDeDiagramas.removerComponenteDoQuadro(linha);
        }

        areaDeDiagramas.removerComponenteDoQuadro(pontoDeExtensao);
        areaDeDiagramas.removerComponenteDoQuadro(painelSetaA);
        areaDeDiagramas.removerComponenteDoQuadro(painelSetaB);
        areaDeDiagramas.removerComponenteDoQuadro(labelNome);
        areaDeDiagramas.removerComponenteDoQuadro(labelMultiplicidadeA);
        areaDeDiagramas.removerComponenteDoQuadro(labelMultiplicidadeB);
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
        Relacao modeloAntigo = modeloAtual.copiar();

        if (ultimoPonto != null) {
            modeloAtual.setPontoLadoA(ultimoPonto);
        }

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

        if (novosPaineis != null) {
            for (JPanel linha : novosPaineis) {
                linha.addMouseListener(adapterGerenciarRelacao);
            }

            modeloAtual.getLinhasDaRelacao().addAll(novosPaineis);

            orientacaoLadoA = calcularOrientacaoDeLinha(modeloAtual.getPontoLadoA(), modeloAtual.getLinhasDaRelacao().getLast());
        }

        mostrarSetaLadoA(modeloAtual.estaMostrandoSetaA());

        atualizarLocalizacaoPontoDeExtensao();

        if (!modeloAtual.getNome().isEmpty()) {
            atualizarLocalizacaoDoNome();
        }

        if (!modeloAtual.getMultiplicidadeLadoA().isEmpty()) {
            atualizarLocalizacaoDeMultiplicidade(labelMultiplicidadeA, modeloAtual.getLinhasDaRelacao().getLast(), orientacaoLadoA);
        }

        if (!modeloAtual.getMultiplicidadeLadoB().isEmpty()) {
            atualizarLocalizacaoDeMultiplicidade(labelMultiplicidadeB, modeloAtual.getLinhasDaRelacao().getFirst(), orientacaoLadoB);
        }

        aplicarEstiloDasLinhas(modeloAtual.getLinhasDaRelacao());

        Relacao modeloAtualCopia = modeloAtual.copiar();

        if (modeloAtualCopia.ehDiferente(modeloAntigo)) {
            areaDeDiagramas.addAlteracaoDeComponente(new RelacaoModificada(this, modeloAntigo, modeloAtualCopia));
        }
    }

    public void setAoClicarPontoDeExtensao(Consumer<RelacaoUML> aoClicarPontoDeExtensao) {
        this.aoClicarPontoDeExtensao = aoClicarPontoDeExtensao;
    }

    public void mostrarDirecao(DirecaoDeRelacao direacao) {
        modeloAtual.setDirecao(direacao);

        switch (direacao) {
            case A_ATE_B -> {
                labelNome.setIcon(GerenciadorDeRecursos.getInstancia().getImagem("misc_seta_direcaoAB"));
                labelNome.setHorizontalTextPosition(JLabel.LEFT);
                labelNome.setSize(labelNome.getPreferredSize());
            }
            case B_ATE_A -> {
                labelNome.setIcon(GerenciadorDeRecursos.getInstancia().getImagem("misc_seta_direcaoBA"));
                labelNome.setHorizontalTextPosition(JLabel.RIGHT);
                labelNome.setSize(labelNome.getPreferredSize());
            }
            default -> labelNome.setIcon(null);
        }
    }

    public boolean relacaoEstaSelecionada() {
        return relacaoSelecionada;
    }

    public AreaDeDiagramas getAreaDeDiagramas() {
        return areaDeDiagramas;
    }

    public TipoDeRelacao getTipoDeRelacao() {
        return tipoDeRelacao;
    }

    public void setModelo(Relacao novoModelo) {
        for (JPanel linha : modeloAtual.getLinhasDaRelacao()) {
            areaDeDiagramas.removerComponenteDoQuadro(linha);
        }

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

        for (JPanel linha : novoModelo.getLinhasDaRelacao()) {
            areaDeDiagramas.addComponenteAoQuadro(linha);
            linha.addMouseListener(adapterGerenciarRelacao);
        }

        modeloAtual = novoModelo;

        orientacaoLadoA = calcularOrientacaoDeLinha(novoModelo.getPontoLadoA(), novoModelo.getLinhasDaRelacao().getLast());

        mostrarNome(!novoModelo.getNome().isEmpty());
        mostrarDirecao(novoModelo.getDirecao());
        mostrarSetaLadoA(novoModelo.estaMostrandoSetaA());
        mostrarSetaLadoB(novoModelo.estaMostrandoSetaB());

        setMultiplicidadeLadoA(novoModelo.getMultiplicidadeLadoA());
        setMultiplicidadeLadoB(novoModelo.getMultiplicidadeLadoB());

        mostrarMultiplicidadeLadoA(!novoModelo.getMultiplicidadeLadoA().isEmpty());
        mostrarMultiplicidadeLadoB(!novoModelo.getMultiplicidadeLadoB().isEmpty());

        aplicarEstiloDasLinhas(modeloAtual.getLinhasDaRelacao());
    }

    protected JFrame getFrameGerenciarRelacao() {
        return frameGerenciarRelacao;
    }

    /**
     * Retorna o ponto de inicio onde a relacao deve ser estendida. Esse ponto corresponde mais ou menos
     * com o inicio da primeira linha.
     */
    public Point getLocalizacaoDeExtensao() {
        Point localizacaoDeExtensao = modeloAtual.getLinhasDaRelacao().getLast().getLocation();

        switch (orientacaoLadoA) {
            case NORTE, OESTE -> localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            case SUL -> {
                localizacaoDeExtensao.y += modeloAtual.getLinhasDaRelacao().getLast().getHeight() - TAMANHO_LINHAS_RELACAO;
                localizacaoDeExtensao.x += TAMANHO_LINHAS_RELACAO;
            }
        }

        return localizacaoDeExtensao;
    }

    protected abstract void aplicarEstiloDasLinhas(ArrayList<JPanel> linhas);

    protected abstract void desenharSeta(Graphics2D g2, Point[] pontosDaSeta);

    protected abstract void initFrameGerenciarRelacao();
}
