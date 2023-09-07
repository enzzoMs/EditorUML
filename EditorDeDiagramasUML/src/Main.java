import interfacegrafica.GerenciadorInterfaceGrafica;
import com.formdev.flatlaf.FlatLightLaf;
import auxiliares.GerenciadorDeRecursos;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Configurando alguns aspectos do FlatLaf

        FlatLightLaf.setup();
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ));
        UIManager.put("ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ));
        UIManager.put("ScrollBar.thumb", GerenciadorDeRecursos.getInstancia().getColor("quick_silver"));
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Component.arc", 7 );
        UIManager.put("Component.arrowType", "triangle");
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        // Iniciando o aplicativo e mostrando o frame principal com menu

        SwingUtilities.invokeLater(() -> {
            GerenciadorInterfaceGrafica interfaceGrafica = new GerenciadorInterfaceGrafica();
            interfaceGrafica.mostrarMenuPrincipal();
            interfaceGrafica.mostrarInterfaceGrafica(true);
        });
    }
}