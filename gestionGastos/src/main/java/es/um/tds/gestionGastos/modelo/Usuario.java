package es.um.tds.gestionGastos.modelo;

public class Usuario {
    private String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
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
