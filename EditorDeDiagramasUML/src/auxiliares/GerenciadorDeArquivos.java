package auxiliares;

import ComponentesUML.*;
import DiagramaUML.DiagramaUML;
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
import java.util.Objects;

/**
 * Singleton que lida com o acesso a arquivos, fornecendo recursos especificamente para abrir, salvar ou
 * exportar DiagramasUML.
 */
public class GerenciadorDeArquivos {
    // Objeto do tipo File que representa o aquivo no qual o diagrama está salvo.
    //private File arquivoDiagrama;
    //private final AreaDeDiagramas areaDeDiagramas;

    // JDialog usado para indicar ao usuário que ocorreu um erro ao abrir um diagrama.

    private static GerenciadorDeArquivos instancia;

    private final JDialog dialogErroAoAbrir = new JDialog();


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
            writer.write(desconstruirDiagrama(diagrama));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        diagrama.setDiagramaSalvo(true);
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
            fileChooser.setSelectedFile(diagrama.arquivoDiagrama);
        }

        fileChooser.setDialogTitle(gerenciadorDeRecursos.getString("diagrama_salvar"));
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
                writer.write(desconstruirDiagrama(diagrama));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            diagrama.arquivoDiagrama = arquivoDiagrama;
            diagrama.setDiagramaSalvo(true);
        }
    }

    /**
     * Inicializa um FileChooser para que um arquivo de texto seja escolhido e reconstrói um DiagramaUML a partir dele.
     * @return O Diagrama reconstruído. Null caso nenhum arquivo seja escolhido ou um erro ocorreu durante o processo.
     * @see DiagramaUML
     */
    public DiagramaUML abrirDiagrama(File diretorioAtual) {
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

            // TODO: este metodo deve lidar com mostrar o dialog de erro
            return reconstruirDiagrama(arquivoDiagrama);
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
                e.printStackTrace();
            }
        }
    }

    // TODO: mover para outro lugar tipo utils ou no diagrama mesmo
    private String desconstruirDiagrama(DiagramaUML diagramaUML) {
        /*
            Retorna uma string contendo as informações do diagrama para ser salvo em um arquivo de texto.
            A string é construida de tal forma que cada informação aparece em uma linha diferente.
         */

        StringBuilder diagramaEmString = new StringBuilder();

        diagramaEmString.append("UML APP\n");
        diagramaEmString.append(diagramaUML.getListaComponentesUML().size());
        diagramaEmString.append("\n");


        for (ComponenteUML componenteUML : diagramaUML.getListaComponentesUML()) {
            if (componenteUML instanceof ClasseUML) {
                diagramaEmString.append("CLASSE_UML\n");
            } else if (componenteUML instanceof AnotacaoUML) {
                diagramaEmString.append("ANOTACAO_UML\n");
            } else {
                diagramaEmString.append("PACOTE_UML\n");
            }

            diagramaEmString.append(componenteUML.toString());
        }


        return diagramaEmString.toString();


    }


    private DiagramaUML reconstruirDiagrama(File arquivo) {
        /*
            Reconstroi um diagrama a partir de um aquivo de texto, criando e inicializando todos os objetos necessários no processo.
            O método retorna um DiagramaUML contendo todos os objetos necessários, porém caso qualquer erro ocorra o usuário
            será notificado através do JDialogErroAoAbrir e o método returna null.

         */
        /*

        try {
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));

            if (!reader.readLine().equals("UML APP")) {
                return null;
            }

            DiagramaUML novoDiagramaUML = new DiagramaUML(areaDeDiagramas);



            int numComponentesUML = Integer.parseInt(reader.readLine());

            for (int i = 0; i < numComponentesUML; i++) {
                String tipoDeComponente = reader.readLine();

                if (tipoDeComponente.equals("CLASSE_UML")) {
                    ClasseUML novaClasseUML = new ClasseUML(novoDiagramaUML);

                    int posicaoX = Integer.parseInt(reader.readLine());
                    int posicaoY = Integer.parseInt(reader.readLine());

                    novaClasseUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                    );

                    String nomeClasse = reader.readLine();
                    String comentario = reader.readLine();
                    int limiteComentario = Integer.parseInt(reader.readLine());
                    boolean ehAbstrata = Boolean.parseBoolean(reader.readLine());
                    boolean ehInterface = Boolean.parseBoolean(reader.readLine());

                    int numAtributos = Integer.parseInt(reader.readLine());

                    ArrayList<Atributo> atributoArrayList = new ArrayList<>();
                    Object[] arrayStringAtributos = new Object[numAtributos];


                    for (int j = 0; j < numAtributos; j++) {
                        String visibilidadeAtributo = reader.readLine();
                        String nomeAtributo = reader.readLine();
                        String tipoAtributo = reader.readLine();
                        String valorPadraoAtributo = reader.readLine();
                        boolean atributoEhEstatico = Boolean.parseBoolean(reader.readLine());

                        Atributo novoAtributo = new Atributo(nomeAtributo, tipoAtributo, valorPadraoAtributo,
                                visibilidadeAtributo, atributoEhEstatico);

                        atributoArrayList.add(novoAtributo);


                        StringBuilder atributoEmString = new StringBuilder();

                        atributoEmString.append(novoAtributo.ehEstatico() ? "<html><u>" : "");
                        atributoEmString.append(novoAtributo.getVisibilidade());
                        atributoEmString.append(novoAtributo.getNome());
                        atributoEmString.append(novoAtributo.getTipo().equals("") ? "" : ": " + novoAtributo.getTipo());
                        atributoEmString.append(novoAtributo.getValorPadrao().equals("") ? "" : " = " + novoAtributo.getValorPadrao());
                        atributoEmString.append(novoAtributo.ehEstatico() ? "</u></html>" : "");

                        arrayStringAtributos[j] = atributoEmString.toString();
                    }

                    novaClasseUML.setArrayListAtributos(atributoArrayList);


                    int numMetodos = Integer.parseInt(reader.readLine());

                    ArrayList<Metodo> metodoArrayList = new ArrayList<>();
                    Object[] arrayStringMetodo = new Object[numMetodos];


                    for (int j = 0; j < numMetodos; j++) {
                        String visibilidadeMetodo = reader.readLine();
                        String nomeMetodo = reader.readLine();
                        String tipoMetodo = reader.readLine();
                        boolean metodoEhAbstrato = Boolean.parseBoolean(reader.readLine());
                        boolean metodoEhEstatico = Boolean.parseBoolean(reader.readLine());

                        ArrayList<Parametro> parametroArrayList = new ArrayList<>();

                        int numParametros = Integer.parseInt(reader.readLine());

                        for (int k = 0; k < numParametros; k++) {
                            String nomeParametro = reader.readLine();
                            String tipoParametro = reader.readLine();
                            String valorPadraoParametro = reader.readLine();

                            parametroArrayList.add(new Parametro(nomeParametro, tipoParametro, valorPadraoParametro));
                        }

                        Metodo novoMetodo = new Metodo(nomeMetodo, visibilidadeMetodo, tipoMetodo,
                                metodoEhEstatico, metodoEhAbstrato, parametroArrayList);

                        metodoArrayList.add(novoMetodo);


                        StringBuilder metodoEmString = new StringBuilder();

                        metodoEmString.append("<html>");
                        metodoEmString.append(novoMetodo.ehEstatico() ? "<u>" : "");
                        metodoEmString.append(novoMetodo.ehAbstrato() ? "<i>" : "");
                        metodoEmString.append(novoMetodo.getVisibilidade());
                        metodoEmString.append(novoMetodo.getNome());
                        metodoEmString.append("(");
                        metodoEmString.append(novoMetodo.toStringParametros());
                        metodoEmString.append(")");
                        metodoEmString.append(novoMetodo.getTipo().equals("") ? "" : ": " + novoMetodo.getTipo());
                        metodoEmString.append(novoMetodo.ehAbstrato() ? "</i>" : "");
                        metodoEmString.append(novoMetodo.ehEstatico() ? "</u>" : "");
                        metodoEmString.append("<html>");

                        arrayStringMetodo[j] = metodoEmString.toString();
                    }

                    novaClasseUML.setArrayListMetodos(metodoArrayList);


                    novaClasseUML.atualizarComponente(
                            nomeClasse,
                            comentario,
                            ehAbstrata,
                            arrayStringAtributos,
                            arrayStringMetodo,
                            limiteComentario,
                            ehInterface
                    );

                    novoDiagramaUML.getListaComponentesUML().add(novaClasseUML);

                } else if (tipoDeComponente.equals("ANOTACAO_UML")) {
                    AnotacaoUML novaAnotacaoUML = new AnotacaoUML(novoDiagramaUML);

                    int posicaoX = Integer.parseInt(reader.readLine());
                    int posicaoY = Integer.parseInt(reader.readLine());

                    novaAnotacaoUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                    );

                    String textoAnotacao = reader.readLine().replaceAll("\\(novaLinha\\)", "\n");
                    int limiteAnotacao = Integer.parseInt(reader.readLine());

                    novaAnotacaoUML.atualizarComponente(textoAnotacao, limiteAnotacao);

                    novoDiagramaUML.getListaComponentesUML().add(novaAnotacaoUML);
                } else if (tipoDeComponente.equals("PACOTE_UML")) {
                    PacoteUML novoPacoteUML = new PacoteUML(novoDiagramaUML);

                    int posicaoX = Integer.parseInt(reader.readLine());
                    int posicaoY = Integer.parseInt(reader.readLine());

                    novoPacoteUML.getPainelComponente().setLocation(
                            posicaoX,
                            posicaoY
                    );

                    String nome = reader.readLine();
                    int larguraAreaPacote = Integer.parseInt(reader.readLine());
                    int alturaAreaPacote = Integer.parseInt(reader.readLine());

                    novoPacoteUML.getPainelAreaPacote().setSize(larguraAreaPacote, alturaAreaPacote);

                    novoPacoteUML.atualizarComponente(nome);

                    novoDiagramaUML.getListaComponentesUML().add(novoPacoteUML);

                }

            }

            reader.close();

            return novoDiagramaUML;

        } catch (Exception e) {
            JDialogErroAoAbrir.setLocationRelativeTo(null);
            JDialogErroAoAbrir.setVisible(true);

            return null;
        }*/
        return null;
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

        JLabel labelErro = new JLabel(gerenciadorDeRecursos.getString("erro_ocorreu"));
        labelErro.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        JLabel labelErroInformacoes = new JLabel(gerenciadorDeRecursos.getString("erro_imagem_explicacao"), JLabel.CENTER);
        labelErroInformacoes.setFont(gerenciadorDeRecursos.getRobotoMedium(14));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 25, 0, 25),
            BorderFactory.createMatteBorder(0, 0, 1, 0, gerenciadorDeRecursos.getColor("black"))
        ));

        painelMensagem.add(labelErro, "align center, wrap, gapbottom 5");
        painelMensagem.add(labelErroInformacoes, "align center, wrap");

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
                dialogErroAoAbrir.setVisible(false);
            }
        });

        // ----------------------------------------------------------------------------

        JPanel painelErro = new JPanel(new MigLayout("insets 5 0 10 0"));
        painelErro.setBackground(gerenciadorDeRecursos.getColor("white"));
        painelErro.add(painelImgErro, "west");
        painelErro.add(painelMensagem, "wrap");
        painelErro.add(painelRespostaOK, "gaptop 10, align right, gapright 20");

        dialogErroAoAbrir.setTitle(gerenciadorDeRecursos.getString("erro_maiusculo"));
        dialogErroAoAbrir.setContentPane(painelErro);
        dialogErroAoAbrir.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialogErroAoAbrir.setResizable(false);
        dialogErroAoAbrir.pack();
    }
}

