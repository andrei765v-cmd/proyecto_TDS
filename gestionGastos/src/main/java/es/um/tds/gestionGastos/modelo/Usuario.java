package es.um.tds.gestionGastos.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario {
    private String nombre;

    public Usuario() {}

    @JsonCreator
    public Usuario(@JsonProperty("nombre") String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario u = (Usuario) o;
        return nombre.equalsIgnoreCase(u.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return nombre.substring(0, 1).toUpperCase() +
                nombre.substring(1).toLowerCase();
    }
}
