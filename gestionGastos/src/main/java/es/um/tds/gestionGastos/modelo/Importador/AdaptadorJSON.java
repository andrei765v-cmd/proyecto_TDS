package es.um.tds.gestionGastos.modelo.Importador;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.RepositorioCategorias;
import es.um.tds.gestionGastos.modelo.RepositorioGastos;
import es.um.tds.gestionGastos.modelo.RepositorioUsuarios;
import es.um.tds.gestionGastos.modelo.Usuario;

/**
 * AdaptadorJSON (Patrón Adaptador).
 * Formato esperado: array JSON de objetos
 * [
 *   {
 *     "fecha": "2022-03-02",
 *     "descripcion": "Desayuno",
 *     "monto": 4.50,
 *     "categoria": "Comida",
 *     "payer": "Me"
 *   },
 *   ...
 * ]
 * "payer" es opcional: si falta o es "Me", se asigna al usuario activo.
 */
public class AdaptadorJSON implements Importador {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public List<Gasto> importarGastos(File fichero, Usuario usuarioActivo) throws Exception {
        List<Gasto> gastosImportados = new ArrayList<>();
        List<JsonNode> nodos = mapper.readValue(fichero, new TypeReference<List<JsonNode>>() {});

        int idx = 0;
        for (JsonNode n : nodos) {
            idx++;
            if (!n.hasNonNull("fecha") || !n.hasNonNull("descripcion") || !n.hasNonNull("monto") || !n.hasNonNull("categoria")) {
                throw new Exception("Entrada " + idx + " incompleta. Campos requeridos: fecha, descripcion, monto, categoria");
            }

            LocalDate fecha = LocalDate.parse(n.get("fecha").asText(), FORMATO_FECHA);
            String descripcion = n.get("descripcion").asText();
            double monto = n.get("monto").asDouble();
            String nombreCategoria = n.get("categoria").asText();
            String payer = n.hasNonNull("payer") ? n.get("payer").asText() : "Me";

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

        return gastosImportados;
    }
}
