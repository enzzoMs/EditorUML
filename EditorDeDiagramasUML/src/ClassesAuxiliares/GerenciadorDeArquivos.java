package ClassesAuxiliares;

import ComponentesUML.*;
import DiagramaUML.DiagramaUML;
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

public class GerenciadorDeArquivos {
    /*
        Classe que tem por objetivo prestar serviçõs a AreaDeDiagramas, abrindo, salvando ou exportanto um diagrama de
        acordo com a vontade do usuário.
     */


    // Objeto do tipo File que representa o aquivo no qual o diagrama está salvo.
    private File arquivoDiagrama;
    private final AreaDeDiagramas areaDeDiagramas;

    // JDialog usado para indicar ao usuário que ocorreu um erro ao abrir um diagrama.
    private final JDialog JDialogErroAoAbrir;


    public GerenciadorDeArquivos(AreaDeDiagramas areaDeDiagramas) {
        this.areaDeDiagramas = areaDeDiagramas;

        // Configurando os componentes gráficos do JDialogErroAoAbrir ------------------------------------------------

        RobotoFont robotoFont = new RobotoFont();

        JPanel painelErro = new JPanel(new MigLayout("insets 5 0 10 0"));
        painelErro.setBackground(Color.white);

        JLabel labelImgErro = new JLabel(new ImageIcon(GerenciadorDeArquivos.class.getResource("/imagens/img_erro.png")));

        JPanel painelImgErro = new JPanel(new MigLayout("fill, insets 15 15 15 15"));
        painelImgErro.setBackground(new Color(0x1d2021));
        painelImgErro.add(labelImgErro, "align center");


        JPanel painelMensagem = new JPanel(new MigLayout("insets 10 20 8 20"));
        painelMensagem.setOpaque(false);

        JLabel labelErro = new JLabel("Um erro ocorreu!");
        labelErro.setFont(robotoFont.getRobotoMedium(14));

        JLabel labelErroInformacoes1 = new JLabel("Certifique-se que o arquivo foi gerado pelo próprio", JLabel.CENTER);
        labelErroInformacoes1.setFont(robotoFont.getRobotoMedium(12));

        JLabel labelErroInformacoes2 = new JLabel("aplicativo e não foi modificado.", JLabel.CENTER);
        labelErroInformacoes2.setFont(robotoFont.getRobotoMedium(12));

        painelMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 25, 0, 25),
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)
        ));

        painelMensagem.add(labelErro, "align center, wrap, gapbottom 5");
        painelMensagem.add(labelErroInformacoes1, "align center, wrap");
        painelMensagem.add(labelErroInformacoes2, "align center, wrap");


        JPanel painelRespostaOK = new JPanel(new MigLayout("fill, insets 5 15 5 15"));
        painelRespostaOK.setBackground(Color.white);
        painelRespostaOK.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0x242424)));

        JLabel labelRespostaOK = new JLabel("OK");
        labelRespostaOK.setFont(robotoFont.getRobotoMedium(14));
        labelRespostaOK.setForeground(Color.black);

        painelRespostaOK.add(labelRespostaOK, "align center");
        painelRespostaOK.addMouseListener( new MouseAdapter() {
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

        painelErro.add(painelImgErro, "west");
        painelErro.add(painelMensagem, "wrap");
        painelErro.add(painelRespostaOK, "gaptop 10, align right, gapright 20");

        JDialogErroAoAbrir = new JDialog();
        JDialogErroAoAbrir.setTitle("ERRO");
        JDialogErroAoAbrir.setContentPane(painelErro);
        JDialogErroAoAbrir.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JDialogErroAoAbrir.setResizable(false);
        JDialogErroAoAbrir.pack();


        painelRespostaOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialogErroAoAbrir.setVisible(false);
            }
        });

    }

    public void novoDiagrama() {
        arquivoDiagrama = null;
    }

    public void salvarDiagrama(DiagramaUML diagramaUML) {
        /*
        Salva um determinado diagrama no local especificado pelo atributo arquivoDiagrama, para isso faz uma chamada
        para o método desconstruirDiagrama e escreve o resultado em um arquivo de texto.
         */

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoDiagrama));
            writer.write(desconstruirDiagrama(diagramaUML));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        diagramaUML.setDiagramaSalvo(true);
    }

    public boolean salvarDiagramaComo(DiagramaUML diagramaUML) {
        /* Salva um determinado diagrama em um diretório especificado pelo usuário, modificando o atributo arquivoDiagrama.
        Retorna true caso o procedimento tenha sido bem sucessio e false caso contrário.
         */

        JFileChooser fileChooser = new JFileChooser();

        if (arquivoDiagrama == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setSelectedFile( new File("NovoDiagrama"));
        } else {
            fileChooser.setCurrentDirectory(arquivoDiagrama);
        }

        fileChooser.setDialogTitle("Salvar Diagrama");

        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Diretório";
            }
        });


        int resposta = fileChooser.showSaveDialog(null);

        if (resposta == JFileChooser.APPROVE_OPTION) {
            arquivoDiagrama = new File(fileChooser.getSelectedFile().getAbsolutePath());

            if (Files.exists(Path.of(arquivoDiagrama + ".txt"))) {
                int index = 1;

                while (Files.exists(Path.of(arquivoDiagrama + "(" + index + ").txt"))) {
                    index++;
                }

                arquivoDiagrama = new File(arquivoDiagrama + "(" + index + ").txt");
            } else {
                arquivoDiagrama = new File(arquivoDiagrama + ".txt");
            }


            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoDiagrama));
                writer.write(desconstruirDiagrama(diagramaUML));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            diagramaUML.setDiagramaSalvo(true);

            return true;
        }

        return false;

    }

    public DiagramaUML abrirDiagrama() {
        // Retorna um novo DiagramaUML reconstruido a partir do metodo reconstruirDiagrama com o uso de um aquivo de texto.

        JFileChooser fileChooser = new JFileChooser();

        if (arquivoDiagrama == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setCurrentDirectory(arquivoDiagrama);
        }

        fileChooser.setDialogTitle("Abrir Diagrama");

        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo de texto", "txt"));


        int resposta = fileChooser.showOpenDialog(null);


        if (resposta == JFileChooser.APPROVE_OPTION) {
            arquivoDiagrama = new File(fileChooser.getSelectedFile().getAbsolutePath());

            return reconstruirDiagrama(arquivoDiagrama);
        }

        return null;

    }

    public void exportarDiagrama(JPanel painelComComponentes) {
        /* Exporta o diagrama em formato .png para um diretório de escolha, para fazer isso é necessário receber como
        parametro um JPanel que possui os componentes do diagrama.
         */

        JFileChooser fileChooser = new JFileChooser();

        if (arquivoDiagrama == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setSelectedFile( new File("NovoDiagrama"));
        } else {
            fileChooser.setSelectedFile(new File(arquivoDiagrama.getName().replaceAll("\\.txt", "")));
            fileChooser.setCurrentDirectory(arquivoDiagrama);
        }


        fileChooser.setDialogTitle("Exportar Diagrama");

        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Diretório";
            }
        });


        int resposta = fileChooser.showDialog(null, "Export");

        if (resposta == JFileChooser.APPROVE_OPTION) {
            File aquivoPngDiagrama = new File(fileChooser.getSelectedFile().getAbsolutePath());

            if (Files.exists(Path.of(aquivoPngDiagrama + ".png"))) {
                int index = 1;

                while (Files.exists(Path.of(aquivoPngDiagrama + "(" + index + ").png"))) {
                    index++;
                }

                aquivoPngDiagrama = new File(aquivoPngDiagrama + "(" + index + ").png");
            } else {
                aquivoPngDiagrama = new File(aquivoPngDiagrama + ".png");
            }


            BufferedImage bufferedImage = new BufferedImage(((Container) painelComComponentes).getWidth(), ((Container) painelComComponentes).getHeight(), BufferedImage.TYPE_INT_ARGB);
            ((Container) painelComComponentes).paint(bufferedImage.getGraphics());

            try {
                ImageIO.write(bufferedImage, "PNG", aquivoPngDiagrama);
            } catch (IOException e) {}


        }

    }



    public File getArquivoDiagrama() {
        return arquivoDiagrama;
    }

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
        }
    }
}

