package modelos;

public interface ModeloDeComponenteUML<T> {
    T copiar();

    boolean ehDiferente(T modelo);
}
