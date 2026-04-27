package es.um.tds.gestionGastos.modelo.Importador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.RepositorioCategorias;
import es.um.tds.gestionGastos.modelo.RepositorioGastos;
import es.um.tds.gestionGastos.modelo.Usuario;

/**
 * AdaptadorCSV (Patrón Adaptador).
 * Adapta un fichero CSV con el formato:
 *   fecha(yyyy-MM-dd), descripcion, monto, nombreCategoria
 * a una lista de objetos Gasto del dominio.
 * Si la categoría no existe, se crea automáticamente.
 */
public class AdaptadorCSV implements Importador {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<Gasto> importarGastos(File fichero, Usuario usuarioActivo) throws Exception {
        List<Gasto> gastosImportados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                // Ignorar líneas vacías o cabecera (si empieza por texto no numérico)
                if (linea.isEmpty() || linea.toLowerCase().startsWith("fecha")) {
                    continue;
                }

                String[] partes = linea.split(",", -1);
                if (partes.length < 4) {
                    throw new Exception("Línea " + numeroLinea + " con formato incorrecto. Se esperan 4 campos: fecha,descripción,monto,categoría");
                }

                LocalDate fecha = LocalDate.parse(partes[0].trim(), FORMATO_FECHA);
                String descripcion = partes[1].trim();
                double monto = Double.parseDouble(partes[2].trim());
                String nombreCategoria = partes[3].trim();

                // Obtener o crear categoría
                Categoria categoria = RepositorioCategorias.getInstancia().getCategorias().stream()
                        .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                        .findFirst()
                        .orElseGet(() -> RepositorioCategorias.getInstancia().crear(nombreCategoria));

                Gasto gasto = RepositorioGastos.getInstancia()
                        .registrarGastoPersonal(monto, fecha, descripcion, categoria, usuarioActivo);
                gastosImportados.add(gasto);
            }
        }

        return gastosImportados;
    }
}
