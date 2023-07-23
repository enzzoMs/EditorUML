package auxiliares;

import ClassesAuxiliares.RobotoFont;
import interfacegrafica.MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Singleton que gerencia o acesso a Strings, Cores, Imagens e Fontes, possuindo métodos para garantir a recuperação
 * de qualquer um deles.
 */

public class GerenciadorDeRecursos {
    private static GerenciadorDeRecursos instancia;
    private final ResourceBundle bundleCores;
    private final ResourceBundle bundleStrings;
    private final ResourceBundle bundleCaminhos;
    private Font robotoBlack;
    private Font robotoMedium;

    private GerenciadorDeRecursos() {
        bundleStrings = ResourceBundle.getBundle("strings");
        bundleCores = ResourceBundle.getBundle("cores");
        bundleCaminhos = ResourceBundle.getBundle("caminhos");

        // Criando as fontes Roboto Black e Roboto Medium

        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

            InputStream fonte = RobotoFont.class.getResourceAsStream("/fontes/Roboto-Black.ttf");
            this.robotoBlack = Font.createFont(Font.TRUETYPE_FONT, fonte);
            graphicsEnvironment.registerFont(this.robotoBlack);

            fonte = RobotoFont.class.getResourceAsStream("/fontes/Roboto-Medium.ttf");
            robotoMedium = Font.createFont(Font.TRUETYPE_FONT, fonte);
            graphicsEnvironment.registerFont(robotoMedium);

        } catch (IOException | FontFormatException e) {
            System.out.println("Falha ao carregar fonte");
            e.printStackTrace();
        }
    }

    public static synchronized GerenciadorDeRecursos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDeRecursos();
        }

        return instancia;
    }

    /**
     * @param nome Nome associado a propriedade da Cor (Veja cores.properties).
     */
    public Color getColor(String nome) {
        if (bundleCores.containsKey(nome)) {
            return Color.decode(bundleCores.getString(nome));
        } else {
            throw new IllegalArgumentException(String.format("Não existe Cor com nome \"%s\"", nome));
        }
    }

    /**
     * @param nome Nome associado a propriedade da String (Veja strings.properties).
     */
    public String getString(String nome) {
        if (bundleStrings.containsKey(nome)) {
            return bundleStrings.getString(nome);
        } else {
            throw new IllegalArgumentException(String.format("Não existe String com nome \"%s\"", nome));
        }
    }

    /**
     * @param nome Nome associado ao caminho da Imagem (Veja caminhos.properties).
     */
    public ImageIcon getImagem(String nome) {
        if (bundleCaminhos.containsKey(nome)) {
            URL urlImagem = Objects.requireNonNull(MenuPrincipal.class.getResource(bundleCaminhos.getString(nome)));
            return new ImageIcon(urlImagem);
        } else {
            throw new IllegalArgumentException(String.format("Não existe Imagem com nome \"%s\"", nome));
        }
    }

    public Font getRobotoBlack(float tamanho) {
        return robotoBlack.deriveFont(tamanho);
    }

    public Font getRobotoMedium(float tamanho) {
        return robotoMedium.deriveFont(tamanho);
    }
}
