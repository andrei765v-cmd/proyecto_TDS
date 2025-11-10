package es.um.tds.gestionGastos.modelo;

public class Categoria
    {
        private String nombre;
        private String codigo;

        public Categoria(String nombre,String codigo){
            this.nombre = nombre;
            this.codigo = codigo;
        }
        public void setNombre(String nombre){
            this.nombre = nombre;
        }
        public String getCodigo(){
            return this.codigo;
        }
        public String getNombre(){
            return this.nombre;
        }
}