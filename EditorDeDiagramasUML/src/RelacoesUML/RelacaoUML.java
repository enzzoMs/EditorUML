package RelacoesUML;

import ComponentesUML.ComponenteUML;
import DiagramaUML.DiagramaUML;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class RelacaoUML {
    private AreasDeConexao areaDeConexaoOrigem;
    private ComponenteUML componenteOrigem;

    private AreasDeConexao areaDeConexaoDestino;
    private ComponenteUML componenteDestino;

    private final DiagramaUML diagramaUML;

    private ArrayList<JComponent> listaPaineisRelacao = new ArrayList<>();
    protected JFrame frameGerenciarRelacao = new JFrame();

    public RelacaoUML(DiagramaUML diagramaUML) {
        this.diagramaUML = diagramaUML;
    }


    public boolean mostrarPreview() {

        Point pontoDeConexaoOrigem = getPontoDeConexao(areaDeConexaoOrigem, componenteOrigem);
        Point pontoDeConexaoDestino = getPontoDeConexao(areaDeConexaoDestino, componenteDestino);

        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;
        int MARGEM_MINIMA = 45;

        // sempre colocar um em referencia ao outro em ordem


        // 1a tentativa =============================================================================================

        // ___
        //    |
        //    X


        JPanel painelLinha1 = new JPanel();
        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                Math.min(pontoDeConexaoOrigem.y, pontoDeConexaoDestino.y),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - pontoDeConexaoDestino.y)
        );

        JPanel painelLinha2 = new JPanel();
        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoDestino.x : painelLinha1.getX(),
                pontoDeConexaoDestino.y,
                Math.abs(pontoDeConexaoDestino.x - pontoDeConexaoOrigem.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        listaPaineisRelacao.add(painelLinha1);
        listaPaineisRelacao.add(painelLinha2);

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 2a tentativa =============================================================================================

        // x __
        //     |

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoOrigem.x
                        - Math.abs(pontoDeConexaoOrigem.x - pontoDeConexaoDestino.x) : pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - pontoDeConexaoDestino.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha1.getX() : painelLinha1.getX() + painelLinha1.getWidth(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)? pontoDeConexaoDestino.y : painelLinha1.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(pontoDeConexaoDestino.y - pontoDeConexaoOrigem.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
                        : Math.abs(pontoDeConexaoDestino.y - pontoDeConexaoOrigem.y)
        );

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 3a tentativa =============================================================================================
        // | _
        //    |


        JPanel painelLinha3 = new JPanel();
        painelLinha3.setBounds(
                Math.min(pontoDeConexaoDestino.x, pontoDeConexaoOrigem.x),
                Math.min(pontoDeConexaoDestino.y, pontoDeConexaoOrigem.y) + Math.abs(pontoDeConexaoDestino.y - pontoDeConexaoOrigem.y)/2,
                Math.abs(pontoDeConexaoDestino.x - pontoDeConexaoOrigem.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                (pontoDeConexaoOrigem.y < pontoDeConexaoDestino.y)? pontoDeConexaoOrigem.y : painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - pontoDeConexaoDestino.y)/2
        );

        painelLinha2.setBounds(
                pontoDeConexaoDestino.x,
                (pontoDeConexaoDestino.y < pontoDeConexaoOrigem.y)? pontoDeConexaoDestino.y : pontoDeConexaoDestino.y -
                        Math.abs(pontoDeConexaoOrigem.y - pontoDeConexaoDestino.y)/2,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - pontoDeConexaoDestino.y)/2
        );



        listaPaineisRelacao.add(painelLinha3);


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 4a tentativa =============================================================================================

        //  _
        // | __
        //     |
        //     X

        painelLinha1.setLocation(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y - Math.abs(componenteDestino.getPainelComponente().getY()
                        + componenteDestino.getAltura() - componenteOrigem.getPainelComponente().getY())/2 - TAMANHO_LINHA_DE_RELACIONAMENTO*2
        );
        painelLinha1.setSize(
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - painelLinha1.getY())
        );

        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA :
                        pontoDeConexaoOrigem.x,
                painelLinha1.getY(),
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? Math.abs(componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA
                        - pontoDeConexaoOrigem.x) : Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                        + MARGEM_MINIMA - pontoDeConexaoOrigem.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX() : painelLinha2.getX() + painelLinha2.getWidth(),
                painelLinha2.getY() - Math.abs(pontoDeConexaoDestino.y - painelLinha2.getY()),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y - painelLinha2.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        JPanel painelLinha4 = new JPanel();
        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha3.getX() :
                        painelLinha3.getX() - Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()),
                painelLinha3.getY(),
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()) :
                        Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        listaPaineisRelacao.add(painelLinha4);


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 5a tentativa =============================================================================================

        //  _X
        // | __
        //     |

        painelLinha1.setBounds(
                componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA,
                pontoDeConexaoOrigem.y,
                Math.abs(componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA - pontoDeConexaoOrigem.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha2.setLocation(
                painelLinha1.getX(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        componenteOrigem.getPainelComponente().getY() - Math.abs(componenteOrigem.getPainelComponente().getY() -
                                (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()))/2
                        : painelLinha1.getY()
        );

        painelLinha2.setSize(
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(painelLinha2.getY() - pontoDeConexaoOrigem.y) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha1.getY() - (painelLinha1.getY() + Math.abs(componenteOrigem.getPainelComponente().getY() +
                                componenteOrigem.getAltura() - componenteDestino.getPainelComponente().getY())/2 +
                                (componenteOrigem.getPainelComponente().getY() + componenteOrigem.getAltura() - painelLinha1.getY()))) -
                                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                painelLinha1.getX(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha2.getY() :
                        painelLinha2.getY() + painelLinha2.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                painelLinha3.getX() + painelLinha3.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        pontoDeConexaoDestino.y
                        : painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y)
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 6a tentativa =============================================================================================

        //    X __
        //   _____|
        //  |

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha2.setLocation(
                painelLinha1.getX() + painelLinha1.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        componenteOrigem.getPainelComponente().getY() - Math.abs(componenteOrigem.getPainelComponente().getY() -
                                (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()))/2
                        : painelLinha1.getY()
        );

        painelLinha2.setSize(
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(painelLinha2.getY() - pontoDeConexaoOrigem.y) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha1.getY() - (componenteOrigem.getPainelComponente().getY() +
                                componenteOrigem.getAltura() + Math.abs(componenteOrigem.getPainelComponente().getY() +
                                componenteOrigem.getAltura() - componenteDestino.getPainelComponente().getY())/2)) -
                                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                pontoDeConexaoDestino.x,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha2.getY() :
                        painelLinha2.getY() + painelLinha2.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                pontoDeConexaoDestino.x,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        pontoDeConexaoDestino.y
                        : painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y)
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }



        // 7a tentativa =============================================================================================
        //  _
        // | |
        //   |
        //   X

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)? pontoDeConexaoDestino.y - MARGEM_MINIMA
                        : pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)? Math.abs(pontoDeConexaoDestino.y - MARGEM_MINIMA
                        - pontoDeConexaoOrigem.y) : MARGEM_MINIMA
        );

        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoDestino.x : painelLinha1.getX(),
                painelLinha1.getY(),
                Math.abs(pontoDeConexaoDestino.x - painelLinha1.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX() : painelLinha2.getX()
                        + painelLinha2.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                painelLinha2.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha2.getY() - pontoDeConexaoDestino.y)
        );


        listaPaineisRelacao.remove(painelLinha4);


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 8a tentativa =============================================================================================
        //  ___X
        // |_
        //
        //

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA :
                        componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA,
                pontoDeConexaoOrigem.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA - pontoDeConexaoOrigem.x) :
                        Math.abs(componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA - pontoDeConexaoOrigem.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha2.setBounds(
                painelLinha1.getX(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)? pontoDeConexaoDestino.y : painelLinha1.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha1.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO

        );


        painelLinha3.setBounds(
                painelLinha2.getX(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha2.getY() : painelLinha2.getY() + painelLinha2.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 9a tentativa =============================================================================================
        //  X___
        //     _|
        //
        //

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO:
                        Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                + MARGEM_MINIMA - pontoDeConexaoOrigem.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha2.setBounds(
                painelLinha1.getX() + painelLinha1.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        pontoDeConexaoDestino.y :
                        painelLinha1.getY(),

                TAMANHO_LINHA_DE_RELACIONAMENTO,

                Math.abs(pontoDeConexaoDestino.y - painelLinha1.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO

        );


        painelLinha3.setBounds(
                pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 10a tentativa =============================================================================================
        //  __
        // |  |
        // | __
        //     |
        //     X


        painelLinha4.setBounds(
                pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                MARGEM_MINIMA
        );

        JPanel painelLinha5 = new JPanel();
        painelLinha5.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?  pontoDeConexaoDestino.x :
                        componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA,
                pontoDeConexaoDestino.y - MARGEM_MINIMA,
                Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                        - pontoDeConexaoDestino.x + MARGEM_MINIMA),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha5.getX() + painelLinha5.getWidth() :
                        painelLinha5.getX(),
                pontoDeConexaoDestino.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs((pontoDeConexaoDestino.y - MARGEM_MINIMA) - (componenteDestino.getPainelComponente().getY()
                        + componenteDestino.getAltura() + Math.abs(componenteDestino.getPainelComponente().getY()
                        + componenteDestino.getAltura() - componenteOrigem.getPainelComponente().getY())/2 -
                        TAMANHO_LINHA_DE_RELACIONAMENTO))
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoOrigem.x : painelLinha2.getX(),
                painelLinha2.getY() + painelLinha2.getHeight(),
                Math.abs(pontoDeConexaoOrigem.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );



        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y -  painelLinha3.getY())
        );


        listaPaineisRelacao.add(painelLinha4);
        listaPaineisRelacao.add(painelLinha5);

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 11a tentativa =============================================================================================
        //  __
        // |  |
        // |  X
        // | __
        //     |


        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                componenteOrigem.getPainelComponente().getY() - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(componenteOrigem.getPainelComponente().getY() - MARGEM_MINIMA - pontoDeConexaoOrigem.y)
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?  pontoDeConexaoOrigem.x :
                        componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA,
                painelLinha1.getY(),
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(pontoDeConexaoOrigem.x - (componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura() +
                                MARGEM_MINIMA)) :
                        Math.abs(pontoDeConexaoOrigem.x - (componenteOrigem.getPainelComponente().getX() -
                                MARGEM_MINIMA)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX() + painelLinha2.getWidth() :
                        painelLinha2.getX(),
                painelLinha2.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha2.getY() - (componenteOrigem.getPainelComponente().getY() +
                        componenteOrigem.getAltura() +
                        Math.abs(componenteOrigem.getPainelComponente().getY()
                                + componenteOrigem.getAltura() - componenteDestino.getPainelComponente().getY())/2)) -
                        TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoDestino.x : painelLinha3.getX(),
                painelLinha3.getY() + painelLinha3.getHeight(),
                Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha5.setBounds(
                pontoDeConexaoDestino.x,
                painelLinha4.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y -  painelLinha4.getY())
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 12a tentativa =============================================================================================
        //    __
        // __|

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoOrigem.x - Math.abs(pontoDeConexaoOrigem.x - pontoDeConexaoDestino.x)/2 :
                        pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - pontoDeConexaoDestino.x)/2,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha1.getX() : painelLinha1.getX() + painelLinha1.getWidth(),
                Math.min(pontoDeConexaoOrigem.y, pontoDeConexaoDestino.y),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoDestino.x > pontoDeConexaoOrigem.x)? pontoDeConexaoDestino.x
                        - Math.abs(pontoDeConexaoOrigem.x - pontoDeConexaoDestino.x)/2 : pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        listaPaineisRelacao.remove(painelLinha4);
        listaPaineisRelacao.remove(painelLinha5);


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 13a tentativa =============================================================================================
        //     __
        // |__|

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoOrigem.x
                        - Math.abs(pontoDeConexaoOrigem.x - (componenteDestino.getPainelComponente().getX()
                        + componenteDestino.getLargura()))/2 : pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - (componenteDestino.getPainelComponente().getX()
                        + componenteDestino.getLargura()))/2,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha1.getX() : painelLinha1.getX() + painelLinha1.getWidth(),
                pontoDeConexaoOrigem.y,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - (componenteDestino.getPainelComponente().getY()
                        + componenteDestino.getAltura() + MARGEM_MINIMA)) - TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y + MARGEM_MINIMA,
                Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                MARGEM_MINIMA
        );

        listaPaineisRelacao.add(painelLinha4);

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 14a tentativa =============================================================================================
        //     __X
        // |__|

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x - MARGEM_MINIMA,
                pontoDeConexaoOrigem.y,
                MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                painelLinha1.getX(),
                painelLinha1.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha1.getY() - (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura() + MARGEM_MINIMA))
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoDestino.x :
                        painelLinha2.getX(),
                painelLinha2.getY() + painelLinha2.getHeight(),
                Math.abs(pontoDeConexaoDestino.x - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() :
                        painelLinha3.getX() + painelLinha3.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                pontoDeConexaoDestino.y,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 15a tentativa =============================================================================================
        //  ______
        // |      |
        // X      | __

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                MARGEM_MINIMA
        );


        painelLinha2.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura() +
                        Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                - componenteOrigem.getPainelComponente().getX())/2 - TAMANHO_LINHA_DE_RELACIONAMENTO: painelLinha1.getX(),
                pontoDeConexaoOrigem.y - MARGEM_MINIMA
        );

        painelLinha2.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? Math.abs(pontoDeConexaoOrigem.x - painelLinha2.getX()) :
                        Math.abs(pontoDeConexaoOrigem.x - (componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura()
                                + Math.abs(componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura() -
                                componenteDestino.getPainelComponente().getX())/2) ),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX(): painelLinha2.getX() + painelLinha2.getWidth(),
                painelLinha2.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha2.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoDestino.x : painelLinha3.getX(),
                pontoDeConexaoDestino.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha3.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha3.getX() - pontoDeConexaoDestino.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 16a tentativa =============================================================================================
        //  ______
        // |      |
        //        | __ X

        painelLinha1.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoOrigem.x -
                        Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                - componenteOrigem.getPainelComponente().getX())/2 - TAMANHO_LINHA_DE_RELACIONAMENTO
                        : pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y
        );

        painelLinha1.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(pontoDeConexaoOrigem.x - painelLinha1.getX()) :
                        Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x +
                                Math.abs(componenteDestino.getPainelComponente().getX()
                                        - (componenteOrigem.getPainelComponente().getX() + componenteDestino.getLargura()))/2)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha1.getX() : painelLinha1.getX() + painelLinha1.getWidth(),
                pontoDeConexaoDestino.y - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y - MARGEM_MINIMA - painelLinha1.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO*2
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha2.getX() - Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x) :
                        painelLinha2.getX(),
                painelLinha2.getY(),
                Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoDestino.x : painelLinha3.getX() + painelLinha3.getWidth(),
                pontoDeConexaoDestino.y - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y - painelLinha3.getY())
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 17a tentativa =============================================================================================
        //  ______
        // |      |
        //  --    | __ X

        painelLinha1.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoOrigem.x -
                                Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                        - componenteOrigem.getPainelComponente().getX())/2 - TAMANHO_LINHA_DE_RELACIONAMENTO
                        : pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO*2,
                pontoDeConexaoOrigem.y
        );

        painelLinha1.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(pontoDeConexaoOrigem.x - painelLinha1.getX()) :
                        Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO*2)),

                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                painelLinha1.getX(),
                componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - painelLinha1.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO*2
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha2.getX() - Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x) - MARGEM_MINIMA
                                - TAMANHO_LINHA_DE_RELACIONAMENTO*2 :
                        painelLinha2.getX(),
                painelLinha2.getY(),
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x) + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO*2 :
                        Math.abs(painelLinha2.getX() - (componenteOrigem.getPainelComponente().getX() +
                                componenteOrigem.getLargura() + Math.abs(componenteOrigem.getPainelComponente().getX() +
                                componenteOrigem.getLargura() - componenteDestino.getPainelComponente().getX())/2)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : painelLinha3.getX() + painelLinha3.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y - painelLinha3.getY())
        );

        painelLinha5.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha4.getX() : painelLinha4.getX() + painelLinha4.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                painelLinha4.getY() + painelLinha4.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()) :
                        Math.abs(painelLinha3.getX() + painelLinha3.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO - pontoDeConexaoDestino.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        listaPaineisRelacao.add(painelLinha5);



        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 18a tentativa =============================================================================================
        //       ______
        //      |      |
        //  x --    __ | ????????????????????????

        painelLinha1.setLocation(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y
        );

        painelLinha1.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO*2 :
                        Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x +
                                Math.abs(componenteDestino.getPainelComponente().getX()
                                        - (componenteOrigem.getPainelComponente().getX() + componenteDestino.getLargura()))/2)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                painelLinha1.getX() + painelLinha1.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - painelLinha1.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO*2
        );


        painelLinha3.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        componenteOrigem.getPainelComponente().getX() - Math.abs(componenteDestino.getPainelComponente().getX() +
                                componenteDestino.getLargura() - componenteOrigem.getPainelComponente().getX())/2 - TAMANHO_LINHA_DE_RELACIONAMENTO:
                        painelLinha2.getX(),
                painelLinha2.getY()
        );

        painelLinha3.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha3.getX() - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x) + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : painelLinha3.getX() + painelLinha3.getWidth(),
                painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoDestino.y - painelLinha3.getY())
        );

        painelLinha5.setBounds(
                pontoDeConexaoDestino.x,
                painelLinha4.getY() + painelLinha4.getHeight(),
                Math.abs(pontoDeConexaoDestino.x - painelLinha4.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 19a tentativa =============================================================================================
        //  ______
        // |_     |
        //        X

        listaPaineisRelacao.remove(painelLinha5);

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                MARGEM_MINIMA
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA :
                        pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA - pontoDeConexaoOrigem.x) :
                        Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura() + MARGEM_MINIMA - pontoDeConexaoOrigem.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX() : painelLinha2.getX() + painelLinha2.getWidth(),
                pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(pontoDeConexaoOrigem.y - MARGEM_MINIMA - pontoDeConexaoDestino.y)
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha3.getX() : pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha3.getX() - pontoDeConexaoDestino.x) :
                        Math.abs(painelLinha3.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        listaPaineisRelacao.remove(painelLinha5);

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 20a tentativa =============================================================================================
        //  ______
        // |_X    |


        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoOrigem.x :
                        pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                pontoDeConexaoOrigem.y,
                MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO*2,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha1.getX() + painelLinha1.getWidth():
                        painelLinha1.getX(),
                componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - pontoDeConexaoOrigem.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoDestino.x : painelLinha2.getX(),
                painelLinha2.getY(),
                Math.abs(painelLinha2.getX() - pontoDeConexaoDestino.x),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : painelLinha3.getX() + painelLinha3.getWidth(),
                painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y)
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }


        // 21a tentativa =============================================================================================
        //  ______
        // |      |
        //        |
        // |______|

        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)? pontoDeConexaoOrigem.y :
                        componenteOrigem.getPainelComponente().getY() - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(componenteOrigem.getPainelComponente().getY() + componenteOrigem.getAltura() + MARGEM_MINIMA
                                - pontoDeConexaoOrigem.y) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(componenteOrigem.getPainelComponente().getY() - MARGEM_MINIMA - pontoDeConexaoOrigem.y)
        );


        painelLinha2.setLocation(
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                                painelLinha1.getX() : componenteOrigem.getPainelComponente().getX() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO
                        : (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        componenteDestino.getPainelComponente().getX() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO : painelLinha1.getX(),

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha1.getY() + painelLinha1.getHeight()
                        : painelLinha1.getY()
        );

        painelLinha2.setSize(
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                                Math.abs(painelLinha2.getX() - (componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura()
                                        + MARGEM_MINIMA) + TAMANHO_LINHA_DE_RELACIONAMENTO) :
                                Math.abs(painelLinha2.getX() - painelLinha1.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO
                        : (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha2.getX() - painelLinha1.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha2.getX() - (componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                + MARGEM_MINIMA) + TAMANHO_LINHA_DE_RELACIONAMENTO),

                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                                painelLinha2.getX() + painelLinha2.getWidth() : painelLinha2.getX()
                        : (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha2.getX() : painelLinha2.getX() + painelLinha2.getWidth(),

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO :
                        painelLinha2.getY(),

                TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(componenteDestino.getPainelComponente().getY() - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO -
                                painelLinha2.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha2.getY() - (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()
                                + MARGEM_MINIMA)) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                                pontoDeConexaoDestino.x : painelLinha3.getX()
                        : (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : pontoDeConexaoDestino.x,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha3.getY() :
                        painelLinha3.getY() + painelLinha3.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,

                Math.abs(pontoDeConexaoDestino.x - painelLinha3.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO,

                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha5.setBounds(
                pontoDeConexaoDestino.x,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha4.getY() : painelLinha4.getY() - Math.abs(painelLinha4.getY() - pontoDeConexaoDestino.y),

                TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(painelLinha4.getY() - pontoDeConexaoDestino.y) :
                        Math.abs(painelLinha4.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO

        );

        listaPaineisRelacao.add(painelLinha5);

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 22a tentativa =============================================================================================
        //  _______
        // |_     _|

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoOrigem.x : pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha1.getX() +  painelLinha1.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO :
                        painelLinha1.getX(),
                painelLinha1.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(painelLinha1.getY() - (componenteOrigem.getPainelComponente().getY() + componenteOrigem.getAltura()
                                + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO)) :
                        Math.abs(painelLinha1.getY() - (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()
                                + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO))
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x) ?
                        pontoDeConexaoDestino.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO :
                        painelLinha2.getX(),

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha2.getY() + painelLinha2.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO:
                        componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()
                                + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha2.getX() - (pontoDeConexaoDestino.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO))
                                + TAMANHO_LINHA_DE_RELACIONAMENTO :
                        Math.abs(painelLinha2.getX() - (pontoDeConexaoDestino.x + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO))
                                + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoDestino.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO :
                        pontoDeConexaoDestino.x + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO,

                painelLinha3.getY() - Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y),

                TAMANHO_LINHA_DE_RELACIONAMENTO,

                Math.abs(pontoDeConexaoDestino.y - painelLinha3.getY()) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha5.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha4.getX() - pontoDeConexaoDestino.x) :
                        Math.abs(painelLinha4.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO

        );

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 23a tentativa =============================================================================================
        //  ______
        // |      |
        //        | __ |


        painelLinha1.setBounds(
                pontoDeConexaoOrigem.x,
                pontoDeConexaoOrigem.y - MARGEM_MINIMA,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                MARGEM_MINIMA
        );


        painelLinha2.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? componenteOrigem.getPainelComponente().getX() -
                        Math.abs(componenteDestino.getPainelComponente().getX() + componenteDestino.getLargura()
                                - componenteOrigem.getPainelComponente().getX())/2 : painelLinha1.getX(),
                painelLinha1.getY()
        );

        painelLinha2.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? Math.abs(painelLinha2.getX() - pontoDeConexaoOrigem.x) :
                        Math.abs(painelLinha2.getX() - (componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura() +
                                Math.abs(componenteDestino.getPainelComponente().getX() -
                                        (componenteOrigem.getPainelComponente().getX() + componenteOrigem.getLargura()))/2)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha3.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? painelLinha2.getX() : painelLinha2.getX()
                        + painelLinha2.getWidth(),
                painelLinha2.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha2.getY() - pontoDeConexaoDestino.y) + MARGEM_MINIMA
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)? pontoDeConexaoDestino.x : painelLinha3.getX(),
                painelLinha3.getY() + painelLinha3.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha3.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha5.setBounds(
                painelLinha4.getX() - Math.abs(painelLinha4.getX() - pontoDeConexaoDestino.x),
                pontoDeConexaoDestino.y,
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha4.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        // 24a tentativa =============================================================================================
        //  --- X
        // |_______
        //         |
        //       --

        painelLinha1.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoOrigem.x : pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO*2,
                pontoDeConexaoOrigem.y,
                Math.abs(pontoDeConexaoOrigem.x - (pontoDeConexaoOrigem.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO*2)),
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        painelLinha2.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x) ?
                        painelLinha1.getX() + painelLinha1.getWidth() - TAMANHO_LINHA_DE_RELACIONAMENTO :
                        painelLinha1.getX(),

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        componenteOrigem.getPainelComponente().getY() - Math.abs(componenteOrigem.getPainelComponente().getY() -
                                (componenteDestino.getPainelComponente().getY() + componenteDestino.getAltura()))/2
                        : painelLinha1.getY()
        );

        painelLinha2.setSize(
                TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        Math.abs(pontoDeConexaoOrigem.y - painelLinha2.getY()) :

                        Math.abs(painelLinha2.getY() - (componenteOrigem.getPainelComponente().getY() + componenteOrigem.getAltura()
                                + Math.abs(componenteDestino.getPainelComponente().getY() - (componenteOrigem.getPainelComponente().getY()
                                + componenteOrigem.getAltura()))/2)) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setLocation(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        pontoDeConexaoDestino.x - MARGEM_MINIMA - TAMANHO_LINHA_DE_RELACIONAMENTO*2
                        : painelLinha2.getX(),
                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        painelLinha2.getY() :
                        painelLinha2.getY() + painelLinha2.getHeight() - TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha3.setSize(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha3.getX() - painelLinha2.getX()) + TAMANHO_LINHA_DE_RELACIONAMENTO  :
                        Math.abs(painelLinha3.getX() - (pontoDeConexaoDestino.x + MARGEM_MINIMA + TAMANHO_LINHA_DE_RELACIONAMENTO*2)) +
                                TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha4.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha3.getX() : painelLinha3.getX() + painelLinha3.getWidth()- TAMANHO_LINHA_DE_RELACIONAMENTO,

                (pontoDeConexaoOrigem.y > pontoDeConexaoDestino.y)?
                        pontoDeConexaoDestino.y : painelLinha3.getY(),
                TAMANHO_LINHA_DE_RELACIONAMENTO,
                Math.abs(painelLinha3.getY() - pontoDeConexaoDestino.y) + TAMANHO_LINHA_DE_RELACIONAMENTO
        );

        painelLinha5.setBounds(
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        painelLinha4.getX() : pontoDeConexaoDestino.x,
                pontoDeConexaoDestino.y,
                (pontoDeConexaoOrigem.x > pontoDeConexaoDestino.x)?
                        Math.abs(painelLinha4.getX() - pontoDeConexaoDestino.x) :
                        Math.abs(painelLinha4.getX() - pontoDeConexaoDestino.x) + TAMANHO_LINHA_DE_RELACIONAMENTO,
                TAMANHO_LINHA_DE_RELACIONAMENTO
        );


        // checagem de erros --------------------------------------------------------------------------------------

        if (verificarErroEmLinhasDeRelacao(listaPaineisRelacao)) {
            colocarEstiloDePreview();
            diagramaUML.getAreaDeDiagramas().addRelacionametoAoQuadro(listaPaineisRelacao);
            return true;
        }

        return false;

    }

    public Point getPontoDeConexao(RelacaoGeneralizacao.AreasDeConexao areaDeConexao, ComponenteUML componenteUML) {

        Point pontoDeConexao = new Point();

        int TAMANHO_LADO_AREA_DE_CONEXAO = componenteUML.getListaAreasDeConexao().get(0).getWidth();

        pontoDeConexao = switch (areaDeConexao) {
            case PONTO_NO -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            case PONTO_N -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        componenteUML.getLargura()/2,
                        TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            case PONTO_NE -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        componenteUML.getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            case PONTO_O -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        componenteUML.getAltura()/2
                );
                yield ponto;
            }
            case PONTO_E -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        componenteUML.getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        componenteUML.getAltura()/2
                );
                yield ponto;
            }
            case PONTO_SO -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        componenteUML.getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            case PONTO_S -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        componenteUML.getLargura()/2,
                        componenteUML.getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            case PONTO_SE -> {
                Point ponto = componenteUML.getPainelComponente().getLocation();
                ponto.translate(
                        componenteUML.getLargura() - TAMANHO_LADO_AREA_DE_CONEXAO/2,
                        componenteUML.getAltura() - TAMANHO_LADO_AREA_DE_CONEXAO/2
                );
                yield ponto;
            }
            default -> throw new IllegalStateException("Area Inválido");
        };

        return pontoDeConexao;
    }


    private int calcularAreaDeIntersecao(JPanel painelLinha, ComponenteUML componenteUML) {
        Rectangle boundsPainelComponente = componenteUML.getPainelComponente().getBounds();
        Rectangle boundsPainelLinha = painelLinha.getBounds();

        if (boundsPainelComponente.intersects(boundsPainelLinha)) {
            return boundsPainelComponente.intersection(boundsPainelLinha).width *
                    boundsPainelComponente.intersection(boundsPainelLinha).height;
        } else {
            return 0;
        }
    }

    private boolean verificarErroEmLinhasDeRelacao(ArrayList<JComponent> listaPaineisRelacao) {
        int TAMANHO_LINHA_DE_RELACIONAMENTO = 4;
        int AREA_ACEITAVEL_DE_INTERSECCAO = (TAMANHO_LINHA_DE_RELACIONAMENTO * componenteOrigem.getListaAreasDeConexao().get(0).getWidth()/2)*2;
        int areaDeIntersecao = 0;

        for (JComponent painelLinha : listaPaineisRelacao) {
            areaDeIntersecao += calcularAreaDeIntersecao((JPanel) painelLinha, componenteOrigem);
        }

        for (JComponent painelLinha : listaPaineisRelacao) {
            areaDeIntersecao += calcularAreaDeIntersecao((JPanel) painelLinha, componenteDestino);
        }

        return (areaDeIntersecao >= AREA_ACEITAVEL_DE_INTERSECCAO-10 && areaDeIntersecao <= AREA_ACEITAVEL_DE_INTERSECCAO);
    }


    public void excluirRelacao() {
        diagramaUML.getAreaDeDiagramas().removerRelacaoDoQuadro(this);
        listaPaineisRelacao.clear();
    }

    public ArrayList<JComponent> getListaPaineisRelacao() {
        return listaPaineisRelacao;
    }

    public DiagramaUML getDiagramaUML() {
        return diagramaUML;
    }

    public void setListaPaineisRelacao(ArrayList<JComponent> listaPaineisRelacao) {
        this.listaPaineisRelacao = listaPaineisRelacao;
    }


    public enum AreasDeConexao {
        PONTO_NO, PONTO_N, PONTO_NE, PONTO_O, PONTO_E, PONTO_SO, PONTO_S, PONTO_SE;
    }

    public AreasDeConexao getAreaDeConexaoOrigem() {
        return areaDeConexaoOrigem;
    }

    public ComponenteUML getComponenteOrigem() {
        return componenteOrigem;
    }

    public AreasDeConexao getAreaDeConexaoDestino() {
        return areaDeConexaoDestino;
    }

    public ComponenteUML getComponenteDestino() {
        return componenteDestino;
    }

    public void setAreaDeConexaoOrigem(AreasDeConexao AreaDeConexaoOrigem) {
        this.areaDeConexaoOrigem = AreaDeConexaoOrigem;
    }

    public void setComponenteOrigem(ComponenteUML componenteOrigem) {
        this.componenteOrigem = componenteOrigem;
    }

    public void setAreaDeConexaoDestino(AreasDeConexao AreaDeConexaoDestino) {
        this.areaDeConexaoDestino = AreaDeConexaoDestino;
    }

    public void setComponenteDestino(ComponenteUML componenteDestino) {
        this.componenteDestino = componenteDestino;
    }

    abstract void colocarEstiloDePreview();

    public abstract void colocarEstiloFinal();

    public abstract void adicionarComportamentoArelacao();

}
