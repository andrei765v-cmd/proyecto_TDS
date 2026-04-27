package es.um.tds.gestionGastos.modelo.Importador;

/**
 * ImportadorFactory (Patrón Factory Method).
 * Devuelve el Importador concreto apropiado en función de la extensión del fichero.
 * Permite añadir nuevos formatos (JSON, XML, etc.) sin modificar el código existente.
 */
public class ImportadorFactory {

    /**
     * Crea un Importador adecuado para la extensión del fichero indicado.
     * @param extension Extensión del fichero en minúsculas (ej. "csv").
     * @return La instancia de Importador concreta.
     * @throws IllegalArgumentException Si el formato no está soportado.
     */
    public static Importador crear(String extension) {
        switch (extension.toLowerCase()) {
            case "csv":
                return new AdaptadorCSV();
            default:
                throw new IllegalArgumentException("Formato de fichero no soportado: " + extension);
        }
    }
}
