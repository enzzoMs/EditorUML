package utils;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;


public class GerenciadorDeRecursos {
    private static GerenciadorDeRecursos instancia;
    private final Properties propertiesCores;

    private GerenciadorDeRecursos() {
        propertiesCores = new Properties();

        try {
            propertiesCores.load(GerenciadorDeRecursos.class.getResourceAsStream("/cores.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized GerenciadorDeRecursos getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDeRecursos();
        }

        return instancia;
    }

    public Color getColor(String nomeCor) {
        if (propertiesCores.containsKey(nomeCor)) {
            return Color.decode(propertiesCores.getProperty(nomeCor));
        } else {
            throw new IllegalArgumentException(String.format("Não existe cor com nome \"%s\"", nomeCor));
        }
    }
}
