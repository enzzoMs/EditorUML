package Main;

import InterfaceGrafica.InterfaceGraficaUML;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Configurando alguns aspectos do FlatLaf --------------------------------------------------------------------

        FlatLightLaf.setup();

        UIManager.put( "ScrollBar.trackArc", 999 );
        UIManager.put( "ScrollBar.thumbArc", 999 );
        UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
        UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
        UIManager.put( "ScrollBar.thumb", new Color(0xa1a1a1));

        UIManager.put( "TextComponent.arc", 7);
        UIManager.put( "Component.arc", 7 );

        UIManager.put( "Component.arrowType", "triangle");

        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        // Iniciando o aplicativo. Cria um objeto do tipo InterfaceGraficaUML e mostra o frame principal --------------

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InterfaceGraficaUML interfaceGrafica = new InterfaceGraficaUML();
                interfaceGrafica.mostrarInterfaceGrafica(true);
            }
        });
    }
}