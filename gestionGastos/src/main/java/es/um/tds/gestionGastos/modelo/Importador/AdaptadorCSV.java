package es.um.tds.gestionGastos.modelo.Importador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.RepositorioCategorias;
import es.um.tds.gestionGastos.modelo.RepositorioGastos;
import es.um.tds.gestionGastos.modelo.RepositorioUsuarios;
import es.um.tds.gestionGastos.modelo.Usuario;

/**
 * AdaptadorCSV (Patrón Adaptador).
 * Formato esperado:
 *   Date,Account,Category,Subcategory,Note,Payer,Amount,Currency
 *   3/2/2022 10:11,Personal,con tarjeta,Comida,Desayuno,Me,4.50,EUR
 * Fecha en formato M/d/yyyy H:mm (estilo US).
 * Si "Payer" es "Me" se asigna al usuario activo;
 * en caso contrario se busca o crea el usuario por nombre.
 * Si la categoría no existe, se crea automáticamente.
 */
public class AdaptadorCSV implements Importador {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    @Override
    public List<Gasto> importarGastos(File fichero, Usuario usuarioActivo) throws Exception {
        List<Gasto> gastosImportados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichero), StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                if (linea.isEmpty() || linea.toLowerCase().startsWith("date,")) {
                    continue;
                }

                String[] partes = linea.split(",", -1);
                if (partes.length < 8) {
                    throw new Exception("Línea " + numeroLinea + " con formato incorrecto. Se esperan 8 campos: Date,Account,Category,Subcategory,Note,Payer,Amount,Currency");
                }

                LocalDate fecha = LocalDate.parse(partes[0].trim(), FORMATO_FECHA);
                String nombreCategoria = partes[2].trim();
                String descripcion = partes[4].trim();
                String payer = partes[5].trim();
                double monto = Double.parseDouble(partes[6].trim());

                Categoria categoria = RepositorioCategorias.getInstancia().getCategorias().stream()
                        .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                        .findFirst()
                        .orElseGet(() -> RepositorioCategorias.getInstancia().crear(nombreCategoria));

                Usuario usuario = payer.equalsIgnoreCase("Me")
                        ? usuarioActivo
                        : RepositorioUsuarios.getInstancia().registrar(payer);

                Gasto gasto = RepositorioGastos.getInstancia()
                        .registrarGastoPersonal(monto, fecha, descripcion, categoria, usuario);
                gastosImportados.add(gasto);
            }
        }

        return gastosImportados;
    }
}
