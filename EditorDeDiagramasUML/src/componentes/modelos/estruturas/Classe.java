package componentes.modelos.estruturas;

import auxiliares.GerenciadorDeRecursos;
import componentes.modelos.ModeloDeComponenteUML;
import modelos.Atributo;
import modelos.Metodo;

import java.util.ArrayList;

public class Classe implements ModeloDeComponenteUML<Classe> {
    private String nome = GerenciadorDeRecursos.getInstancia().getString("classe_nome_default");
    private String comentario = "";
    private boolean ehAbstrata;
    private boolean ehInterface;
    private int numCharsQuebrarComentario = 20;
    private ArrayList<Atributo> atributos = new ArrayList<>();
    private ArrayList<Metodo> metodos = new ArrayList<>();

    public Classe() {}

    public Classe(
        String nome, String comentario, boolean ehAbstrata, boolean ehInterface,
        int numCharsQuebrarComentario, ArrayList<Atributo> atributos, ArrayList<Metodo> metodos
    ) {
        this.nome = nome;
        this.comentario = comentario;
        this.ehAbstrata = ehAbstrata;
        this.ehInterface = ehInterface;
        this.numCharsQuebrarComentario = numCharsQuebrarComentario;
        this.atributos = atributos;
        this.metodos = metodos;
    }

    public boolean ehDiferente(Classe classe) {
        boolean metodosSaoIguais = metodos.size() == classe.metodos.size();

        if (metodosSaoIguais) {
            for (int i = 0; i < metodos.size(); i++) {
                metodosSaoIguais = metodos.get(i).getRepresentacaoUml().equals(classe.metodos.get(i).getRepresentacaoUml());
                if (!metodosSaoIguais) break;
            }
        }

        boolean atributosSaoIguais = atributos.size() == classe.atributos.size();

        if (atributosSaoIguais) {
            for (int i = 0; i < atributos.size(); i++) {
                atributosSaoIguais = atributos.get(i).getRepresentacaoUml().equals(classe.atributos.get(i).getRepresentacaoUml());
                if (!atributosSaoIguais) break;
            }
        }

        return
            !nome.equals(classe.nome) ||
            !comentario.equals(classe.comentario) ||
            ehAbstrata != classe.ehAbstrata ||
            ehInterface != classe.ehInterface ||
            numCharsQuebrarComentario != classe.numCharsQuebrarComentario ||
            !metodosSaoIguais || !atributosSaoIguais;
    }

    public Classe copiar() {
        ArrayList<Atributo> copiaAtributos = new ArrayList<>();

        for (Atributo atributo : atributos) {
            copiaAtributos.add(atributo.copiar());
        }

        ArrayList<Metodo> copiaMetodos = new ArrayList<>();

        for (Metodo metodo : metodos) {
            copiaMetodos.add(metodo.copiar());
        }

        return new Classe(
            nome,
            comentario,
            ehAbstrata,
            ehInterface,
            numCharsQuebrarComentario,
            copiaAtributos,
            copiaMetodos
        );
    }

    public String getNome() {
        return nome;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean ehAbstrata() {
        return ehAbstrata;
    }

    public boolean ehInterface() {
        return ehInterface;
    }

    public int getNumCharsQuebrarComentario() {
        return numCharsQuebrarComentario;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setAbstrata(boolean ehAbstrata) {
        this.ehAbstrata = ehAbstrata;
    }

    public void setInterface(boolean ehInterface) {
        this.ehInterface = ehInterface;
    }

    public void setNumCharsQuebrarComentario(int numCharsQuebrarComentario) {
        this.numCharsQuebrarComentario = numCharsQuebrarComentario;
    }
}
