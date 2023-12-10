package auxiliares;

import componentes.estruturas.AnotacaoUML;
import componentes.estruturas.ClasseUML;
import componentes.estruturas.PacoteUML;
import componentes.modelos.estruturas.*;
import diagrama.DiagramaUML;
import interfacegrafica.AreaDeDiagramas;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Singleton que lida com o acesso a arquivos, fornecendo recursos especificamente para abrir, salvar ou
 * exportar DiagramasUML.
 */
public class GerenciadorDeArquivos {
    private static GerenciadorDeArquivos instancia;

    private final JDialog dialogErro = new JDialog();

    private GerenciadorDeArquivos() {
        inicializarDialogErroAoAbrir();
    }

    public static synchronized GerenciadorDeArquivos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDeArquivos();
        }

        return instancia;
    }

    /**
     * Salva o diagrama no mesmo arquivo especificado pelo atributo "arquivoDiagrama" do argumento.
     */
    public void salvarDiagrama(DiagramaUML diagrama) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(diagrama.arquivoDiagrama));
            writer.write(diagrama.desconstruirDiagrama());
            writer.close();
            diagrama.setDiagramaSalvo(true);
        } catch (IOException e) {
            mostrarDialogErro(GerenciadorDeRecursos.getInstancia().getString("erro_salvar"));
        }
    }

    /**
     * Inicializa um FileChooser, deixa o usuário escolher um diretório e salva o diagrama.
     */
    public void salvarDiagramaComo(DiagramaUML diagrama) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JFileChooser fileChooser = new JFileChooser();

        if (diagrama.arquivoDiagrama == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setSelectedFile(new File(gerenciadorDeRecursos.getString("diagrama_nome_default")));
        } else {
            fileChooser.setCurrentDirectory(diagrama.arquivoDiagrama);
            fileChooser.setSelectedFile(new File(diagrama.arquivoDiagrama.getName().replace(".txt", "")));
        }

        fileChooser.setDialogTitle(gerenciadorDeRecursos.getString("salvar"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }

            @Override
            public String getDescription() {
                return gerenciadorDeRecursos.getString("diretorio");
            }
        });

        int chooserResposta = fileChooser.showSaveDialog(null);

        if (chooserResposta == JFileChooser.APPROVE_OPTION) {
            String caminhoArquivo = fileChooser.getSelectedFile().getAbsolutePath();

            if (Files.exists(Path.of(caminhoArquivo + ".txt"))) {
                // Se ja existe um arquivo ".txt" com mesmo nome no diretorio entao adiciona uma terminacao do tipo
                // "(n)". Por exemplo: "NovoDiagrama(1)" se ja existe um arquivo de nome "NovoDiagrama" no local

                int numeroArquivo = 1;

                while (Files.exists(Path.of(caminhoArquivo + "(" + numeroArquivo + ").txt"))) {
                    numeroArquivo++;
                }

                caminhoArquivo = caminhoArquivo + "(" + numeroArquivo + ").txt";
            } else {
                caminhoArquivo = caminhoArquivo + ".txt";
            }

            File arquivoDiagrama = new File(caminhoArquivo);

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoDiagrama));
                writer.write(diagrama.desconstruirDiagrama());
                writer.close();

                diagrama.arquivoDiagrama = arquivoDiagrama;
                diagrama.setDiagramaSalvo(true);
            } catch (IOException e) {
                mostrarDialogErro(GerenciadorDeRecursos.getInstancia().getString("erro_salvar"));
            }
        }
    }

    /**
     * Inicializa um FileChooser para que um arquivo de texto seja escolhido e reconstrói um DiagramaUML a partir dele.
     * @return O Diagrama reconstruído. Null caso nenhum arquivo seja escolhido ou um erro ocorreu durante o processo.
     * @see DiagramaUML
     */
    public DiagramaUML abrirDiagrama(File diretorioAtual, AreaDeDiagramas areaDeDiagramas) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(
            Objects.requireNonNullElseGet(
                diretorioAtual, () -> new File(System.getProperty("user.home"))
            )
        );

        fileChooser.setDialogTitle(gerenciadorDeRecursos.getString("diagrama_abrir"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            gerenciadorDeRecursos.getString("arquivo_de_texto"), "txt")
        );

        int chooserResposta = fileChooser.showOpenDialog(null);

        if (chooserResposta == JFileChooser.APPROVE_OPTION) {
            File arquivoDiagrama = new File(fileChooser.getSelectedFile().getAbsolutePath());

            return reconstruirDiagrama(arquivoDiagrama, areaDeDiagramas);
        }

        return null;
    }

    /**
     * Inicializa um FileChooser, deixa o usuário escolher um diretório e salva o conteúdo do painel como uma
     * imagem ".png".
     */
    public void exportarDiagrama(JPanel painel, File arquivoDiagrama) {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        JFileChooser fileChooser = new JFileChooser();

        if (arquivoDiagrama == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setSelectedFile( new File(gerenciadorDeRecursos.getString("diagrama_nome_default")));
        } else {
            fileChooser.setSelectedFile(new File(arquivoDiagrama.getName().replaceAll("\\.txt", "")));
            fileChooser.setCurrentDirectory(arquivoDiagrama);
        }

        fileChooser.setDialogTitle(gerenciadorDeRecursos.getString("diagrama_exportar"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }

            @Override
            public String getDescription() {
                return gerenciadorDeRecursos.getString("diretorio");
            }
        });

        int respostaChooser = fileChooser.showDialog(null, gerenciadorDeRecursos.getString("exportar"));

        if (respostaChooser == JFileChooser.APPROVE_OPTION) {
            String caminhoArquivo = fileChooser.getSelectedFile().getAbsolutePath();

            // Se ja existe um arquivo ".png" com mesmo nome no diretorio entao adiciona uma terminacao do tipo
            // "(n)". Por exemplo: "NovoDiagrama(1)" se ja existe um arquivo de nome "NovoDiagrama" no local
            if (Files.exists(Path.of(caminhoArquivo + ".png"))) {
                int numeroArquivo = 1;

                while (Files.exists(Path.of(caminhoArquivo + "(" + numeroArquivo + ").png"))) {
                    numeroArquivo++;
                }

                caminhoArquivo = caminhoArquivo + "(" + numeroArquivo + ").png";
            } else {
                caminhoArquivo =caminhoArquivo + ".png";
            }

            BufferedImage bufferedImage = new BufferedImage(
                    painel.getWidth(),
                    painel.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            painel.paint(bufferedImage.getGraphics());

            try {
                ImageIO.write(bufferedImage, "PNG", new File(caminhoArquivo));
            } catch (IOException e) {
                mostrarDialogErro(GerenciadorDeRecursos.getInstancia().getString("erro_exportar"));
            }
        }
    }

    /**
     * Reconstroi um diagrama a partir de um aquivo de texto, criando e inicializando todos os objetos necessários no processo.
     * O método retorna um DiagramaUML contendo todos os objetos necessários, porém caso qualquer erro ocorra o usuário
     * será notificado através de um Dialog e retornará null.
     */
    private DiagramaUML reconstruirDiagrama(File arquivo, AreaDeDiagramas areaDeDiagramas) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));

            if (!reader.readLine().equals("UML APP")) {
                throw new IllegalArgumentException();
            }

            DiagramaUML novoDiagramaUML = new DiagramaUML();
            novoDiagramaUML.arquivoDiagrama = arquivo;
            novoDiagramaUML.setDiagramaSalvo(true);

            reader.readLine();
            int numComponentesUML = Integer.parseInt(reader.readLine());

            for (int indexComponente = 0; indexComponente < numComponentesUML; indexComponente++) {
                switch (reader.readLine()) {
                    case "CLASSE_UML" -> {
                        ClasseUML novaClasseUML = new ClasseUML(areaDeDiagramas);

                        reader.readLine();
                        int posicaoX = Integer.parseInt(reader.readLine());
                        int posicaoY = Integer.parseInt(reader.readLine());

                        novaClasseUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                        );

                        reader.readLine();
                        String nomeClasse = reader.readLine();
                        reader.readLine();
                        String comentario = reader.readLine();
                        reader.readLine();
                        int limiteComentario = Integer.parseInt(reader.readLine());
                        reader.readLine();
                        boolean ehAbstrata = Boolean.parseBoolean(reader.readLine());
                        reader.readLine();
                        boolean ehInterface = Boolean.parseBoolean(reader.readLine());

                        reader.readLine();
                        int numAtributos = Integer.parseInt(reader.readLine());

                        ArrayList<Atributo> atributos = new ArrayList<>();

                        for (int indexAtributo = 0; indexAtributo < numAtributos; indexAtributo++) {
                            reader.readLine();
                            String nomeAtributo = reader.readLine();
                            reader.readLine();
                            Visibilidade visibilidadeAtributo = Visibilidade.getVisibilidadePorNome(reader.readLine().trim());
                            reader.readLine();
                            String tipoAtributo = reader.readLine();
                            reader.readLine();
                            String valorPadraoAtributo = reader.readLine();
                            reader.readLine();
                            boolean atributoEhEstatico = Boolean.parseBoolean(reader.readLine());

                            atributos.add(new Atributo(
                                nomeAtributo, tipoAtributo, valorPadraoAtributo,
                                visibilidadeAtributo, atributoEhEstatico
                            ));
                        }

                        reader.readLine();
                        int numMetodos = Integer.parseInt(reader.readLine());

                        ArrayList<Metodo> metodos = new ArrayList<>();

                        for (int indexMetodo = 0; indexMetodo < numMetodos; indexMetodo++) {
                            reader.readLine();
                            String nomeMetodo = reader.readLine();
                            reader.readLine();
                            Visibilidade visibilidadeMetodo = Visibilidade.getVisibilidadePorNome(reader.readLine());
                            reader.readLine();
                            String tipoMetodo = reader.readLine();
                            reader.readLine();
                            boolean metodoEhAbstrato = Boolean.parseBoolean(reader.readLine());
                            reader.readLine();
                            boolean metodoEhEstatico = Boolean.parseBoolean(reader.readLine());

                            ArrayList<Parametro> parametros = new ArrayList<>();

                            reader.readLine();
                            int numParametros = Integer.parseInt(reader.readLine());

                            for (int indexParametro = 0; indexParametro < numParametros; indexParametro++) {
                                reader.readLine();
                                String nomeParametro = reader.readLine();
                                reader.readLine();
                                String tipoParametro = reader.readLine();
                                reader.readLine();
                                String valorPadraoParametro = reader.readLine();

                                parametros.add(new Parametro(nomeParametro, tipoParametro, valorPadraoParametro));
                            }

                            Metodo novoMetodo = new Metodo(
                                nomeMetodo, visibilidadeMetodo, tipoMetodo,
                                metodoEhEstatico, metodoEhAbstrato, parametros
                            );

                            metodos.add(novoMetodo);
                        }

                        novaClasseUML.setModelo(new Classe(
                                nomeClasse,
                                comentario,
                                ehAbstrata,
                                ehInterface,
                                limiteComentario,
                                atributos,
                                metodos
                            )
                        );

                        novoDiagramaUML.addComponente(novaClasseUML);
                    }
                    case "ANOTACAO_UML" -> {
                        AnotacaoUML novaAnotacaoUML = new AnotacaoUML(areaDeDiagramas);

                        reader.readLine();
                        int posicaoX = Integer.parseInt(reader.readLine());
                        int posicaoY = Integer.parseInt(reader.readLine());

                        novaAnotacaoUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                        );

                        reader.readLine();
                        String textoAnotacao = reader.readLine().replaceAll("\\(novaLinha\\)", "\n");
                        reader.readLine();
                        int limiteAnotacao = Integer.parseInt(reader.readLine());

                        novaAnotacaoUML.setModelo(new Anotacao(textoAnotacao, limiteAnotacao));

                        novoDiagramaUML.addComponente(novaAnotacaoUML);
                    }
                    case "PACOTE_UML" -> {
                        PacoteUML novoPacoteUML = new PacoteUML(areaDeDiagramas);

                        reader.readLine();
                        int posicaoX = Integer.parseInt(reader.readLine());
                        int posicaoY = Integer.parseInt(reader.readLine());

                        novoPacoteUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                        );

                        reader.readLine();
                        String nome = reader.readLine();
                        reader.readLine();
                        int larguraPacote = Integer.parseInt(reader.readLine());
                        reader.readLine();
                        int alturaPacote = Integer.parseInt(reader.readLine());

                        novoPacoteUML.setModelo(new Pacote(
                            nome,
                            new Rectangle(posicaoX, posicaoY, larguraPacote, alturaPacote)
                        ));

                        novoDiagramaUML.addComponente(novoPacoteUML);
                    }
                }
            }

            reader.close();

            return novoDiagramaUML;

        } catch (Exception e) {
            dialogErro.setLocationRelativeTo(null);
            dialogErro.setVisible(true);

            return null;
        }
    }

    private void mostrarDialogErro(String mensagemErro) {
        JLabel labelErroMensagem = (JLabel) ((JPanel) dialogErro.getContentPane().getComponent(1)).getComponent(0);

        labelErroMensagem.setText(mensagemErro);

        dialogErro.pack();
        dialogErro.setLocationRelativeTo(null);
        dialogErro.setVisible(true);
    }

    private void inicializarDialogErroAoAbrir() {
        GerenciadorDeRecursos gerenciadorDeRecursos = GerenciadorDeRecursos.getInstancia();

        // ----------------------------------------------------------------------------

        JLabel labelImgErro = new JLabel(gerenciadorDeRecursos.getImagem("icone_erro"));

        JPanel painelImgErro = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgErro.setBackground(gerenciadorDeRecursos.getColor("dark_jungle_green"));
        painelImgErro.add(labelImgErro, "align center");

        // ----------------------------------------------------------------------------

        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelErroMensagem = new JLabel(gerenciadorDeRecursos.getString("erro_ocorreu"), JLabel.CENTER);
        labelErroMensagem.setFont(gerenciadorDeRecursos.getRobotoBlack(16));

        JLabel labelErroDescricao = new JLabel(gerenciadorDeRecursos.getString("erro_abrir_arquivo_explicacao"), JLabel.CENTER);
        labelErroDescricao.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        painelMensagem.add(labelErroMensagem, "align center, wrap, gapbottom 5");
        painelMensagem.add(labelErroDescricao, "align center, wrap, gapbottom 5, gaptop 8");

        // ----------------------------------------------------------------------------

        JPanel painelRespostaOK = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaOK.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelRespostaOK.setBorder(BorderFactory.createMatteBorder(
            1, 1, 1, 1, gerenciadorDeRecursos.getColor("raisin_black")
        ));

        JLabel labelRespostaOK = new JLabel(gerenciadorDeRecursos.getString("ok_maiusculo"));
        labelRespostaOK.setFont(gerenciadorDeRecursos.getRobotoMedium(14));
        labelRespostaOK.setForeground(gerenciadorDeRecursos.getColor("black"));

        painelRespostaOK.add(labelRespostaOK, "align center");
        painelRespostaOK.addMouseListener( new MouseAdapter() {
            // O componente de index 0 eh o labelRespostaOK

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
                dialogErro.setVisible(false);
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelErro = new JPanel(new MigLayout("insets 5 0 10 0"));
        painelErro.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelErro.add(painelImgErro, "west");
        painelErro.add(painelMensagem, "wrap");
        painelErro.add(painelRespostaOK, "gaptop 10, align right, gapright 20");

        dialogErro.setTitle(gerenciadorDeRecursos.getString("erro_maiusculo"));
        dialogErro.setContentPane(painelErro);
        dialogErro.pack();
        dialogErro.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogErro.setResizable(false);
    }
}

