package ClassesAuxiliares;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class RobotoFont {
    /*
        Apenas uma pequena classe com o objetivo de criar e retornar as fontes RobotoBlack ou RobotoMedium.
     */
    private Font robotoBlack;
    private Font robotoMedium;

    public RobotoFont() {
        // Criando as fontes Roboto Black e Roboto Medium -----------------------------------------------------------

        try {
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

            InputStream fonte = RobotoFont.class.getResourceAsStream("/fontes/Roboto-Black.ttf");
            this.robotoBlack = Font.createFont(Font.TRUETYPE_FONT, fonte);
            graphicsEnvironment.registerFont(this.robotoBlack);

            fonte = RobotoFont.class.getResourceAsStream("/fontes/Roboto-Medium.ttf");
            robotoMedium = Font.createFont(Font.TRUETYPE_FONT, fonte);
            graphicsEnvironment.registerFont(robotoMedium);

        } catch (IOException | FontFormatException e) {
            System.out.println("Falha ao Carregar Fonte");
        }
    }

    // Retorna a fonte RobotoBlack com o tamanho especificado

    public Font getRobotoBlack(float tamanho) {
        return robotoBlack.deriveFont(tamanho);
    }


    // Retorna a fonte RobotoMedium com o tamanho especificado

    public Font getRobotoMedium(float tamanho) {
        return robotoMedium.deriveFont(tamanho);
    }
}