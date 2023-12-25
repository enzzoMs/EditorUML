package componentes.modelos.estruturas;

import auxiliares.GerenciadorDeRecursos;
import componentes.modelos.ModeloDeComponenteUML;

import java.awt.*;

public class Pacote implements ModeloDeComponenteUML<Pacote> {
    private String nome = GerenciadorDeRecursos.getInstancia().getString("estrutura_pacote_nome_default");
    private Rectangle bounds;

    public Pacote() {}

    public Pacote(String nome, Rectangle bounds) {
        this.nome = nome;
        this.bounds = bounds;
    }

    @Override
    public Pacote copiar() {
        return new Pacote(nome, new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
    }

    @Override
    public boolean ehDiferente(Pacote modelo) {
        Rectangle boundsModelo = modelo.bounds;

        return !nome.equals(modelo.nome)
                || bounds.x != boundsModelo.x
                || bounds.y != boundsModelo.y
                || bounds.width != boundsModelo.width
                || bounds.height != boundsModelo.height;
    }

    public String getNome() {
        return nome;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
