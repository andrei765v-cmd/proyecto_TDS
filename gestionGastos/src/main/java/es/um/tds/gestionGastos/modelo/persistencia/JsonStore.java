package es.um.tds.gestionGastos.modelo.persistencia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonStore {

    private static final Path DATA_DIR = Paths.get(System.getProperty("user.home"), ".gestionGastos");
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonStore() {}

    public static File ficheroDe(String nombre) {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de datos: " + e.getMessage(), e);
        }
        return DATA_DIR.resolve(nombre).toFile();
    }

    public static <T> T leer(String nombre, TypeReference<T> ref, T porDefecto) {
        File f = ficheroDe(nombre);
        if (!f.exists() || f.length() == 0) return porDefecto;
        try {
            return MAPPER.readValue(f, ref);
        } catch (IOException e) {
            System.err.println("Error leyendo " + nombre + ": " + e.getMessage());
            return porDefecto;
        }
    }

    public static void escribir(String nombre, Object datos) {
        File f = ficheroDe(nombre);
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(f, datos);
        } catch (IOException e) {
            System.err.println("Error escribiendo " + nombre + ": " + e.getMessage());
        }
    }
}
