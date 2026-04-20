package es.um.tds.gestionGastos.modelo;

public class Categoria {
	private String nombre;

	public Categoria(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	// Para comparar categorías mas adelante y evitar duplicados
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Categoria))
			return false;
		Categoria c = (Categoria) o;
		return nombre.equalsIgnoreCase(c.nombre);
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