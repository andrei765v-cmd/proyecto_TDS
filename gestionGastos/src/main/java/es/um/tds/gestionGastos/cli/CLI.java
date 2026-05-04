package es.um.tds.gestionGastos.cli;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.RepositorioAlertas;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.EstrategiaSemanal;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;

public class CLI {

    private final ControladorPrincipal controlador;
    private final Scanner scanner;

    public CLI() {
        this.controlador = ControladorPrincipal.getInstancia();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new CLI().iniciar();
    }

    public void iniciar() {
        System.out.println("+--------------------------------------+");
        System.out.println("|   Gestion de Gastos - Modo CLI       |");
        System.out.println("+--------------------------------------+");

        seleccionarUsuario();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1":
                    registrarGasto();
                    break;
                case "2":
                    modificarGasto();
                    break;
                case "3":
                    eliminarGasto();
                    break;
                case "4":
                    listarGastos();
                    break;
                case "5":
                    cambiarUsuario();
                    break;
                case "6":
                    crearAlerta();
                    break;
                case "7":
                    listarAlertas();
                    break;
                case "8":
                    listarNotificaciones();
                    break;
                case "9":
                    importarGastos();
                    break;
                case "0":
                    controlador.guardarTodo();
                    System.out.println("Datos guardados. Saliendo del modo CLI. Hasta pronto!");
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida. Intentalo de nuevo.");
            }
        }
    }

    // --- Menu y seleccion de usuario ---

    private void mostrarMenu() {
        Usuario activo = controlador.getUsuarioActivo();
        System.out.println("\n--- Usuario activo: " + (activo != null ? activo.getNombre() : "Ninguno") + " ---");
        System.out.println("[1] Registrar gasto");
        System.out.println("[2] Modificar gasto");
        System.out.println("[3] Eliminar gasto");
        System.out.println("[4] Listar mis gastos");
        System.out.println("[5] Cambiar de usuario");
        System.out.println("[6] Crear alerta");
        System.out.println("[7] Listar alertas");
        System.out.println("[8] Ver notificaciones");
        System.out.println("[9] Importar gastos desde fichero");
        System.out.println("[0] Salir");
        System.out.print("Selecciona una opcion: ");
    }

    private void seleccionarUsuario() {
        List<Usuario> usuarios = controlador.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados. Creando uno nuevo...");
            System.out.print("Nombre del nuevo usuario: ");
            String nombre = scanner.nextLine().trim();
            Usuario nuevo = controlador.registrarUsuario(nombre);
            controlador.setUsuarioActivo(nuevo);
            System.out.println("Usuario '" + nombre + "' creado y seleccionado.");
            return;
        }

        System.out.println("\nUsuarios disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println("[" + i + "] " + usuarios.get(i).getNombre());
        }
        System.out.print("Selecciona tu usuario (o 'n' para crear uno nuevo): ");
        String entrada = scanner.nextLine().trim();

        if (entrada.equalsIgnoreCase("n")) {
            System.out.print("Nombre del nuevo usuario: ");
            String nombre = scanner.nextLine().trim();
            Usuario nuevo = controlador.registrarUsuario(nombre);
            controlador.setUsuarioActivo(nuevo);
            System.out.println("Usuario '" + nombre + "' creado y seleccionado.");
        } else {
            try {
                int idx = Integer.parseInt(entrada);
                if (idx >= 0 && idx < usuarios.size()) {
                    controlador.setUsuarioActivo(usuarios.get(idx));
                    System.out.println("Usuario '" + usuarios.get(idx).getNombre() + "' seleccionado.");
                } else {
                    System.out.println("Indice fuera de rango. Se seleccionara el primero por defecto.");
                    controlador.setUsuarioActivo(usuarios.get(0));
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Se seleccionara el primero por defecto.");
                controlador.setUsuarioActivo(usuarios.get(0));
            }
        }
    }

    private void cambiarUsuario() {
        seleccionarUsuario();
    }

    // --- Operaciones CRUD ---

    private void registrarGasto() {
        System.out.println("\n-- Registrar nuevo gasto --");
        try {
            double monto = pedirMonto();
            LocalDate fecha = pedirFecha();
            String descripcion = pedirDescripcion();
            Categoria categoria = pedirOCrearCategoria();

            Usuario activo = controlador.getUsuarioActivo();
            controlador.registrarGastoPersonal(monto, fecha, descripcion, categoria, activo);
            System.out.println("[OK] Gasto registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERR] " + e.getMessage());
        }
    }

    private void listarGastos() {
        Usuario activo = controlador.getUsuarioActivo();
        if (activo == null) {
            System.out.println("No hay usuario activo.");
            return;
        }

        List<Gasto> gastos = controlador.getGastosPersonales(activo);
        if (gastos.isEmpty()) {
            System.out.println("No tienes gastos registrados.");
            return;
        }

        System.out.println("\n-- Tus gastos --");
        for (int i = 0; i < gastos.size(); i++) {
            Gasto g = gastos.get(i);
            System.out.printf("[%d] %s | %s | %.2f EUR | %s%n",
                    i + 1, g.getFecha(), g.getDescripcion(), g.getCantidad(),
                    g.getCategoria() != null ? g.getCategoria().getNombre() : "Sin categoria");
        }
    }

    private void modificarGasto() {
        listarGastos();
        List<Gasto> gastos = controlador.getGastosPersonales(controlador.getUsuarioActivo());
        if (gastos.isEmpty())
            return;

        System.out.print("Indice del gasto a modificar: ");
        int idx = leerEnteroSeguro();
        if (idx < 0 || idx >= gastos.size()) {
            System.out.println("Indice invalido.");
            return;
        }

        Gasto gasto = gastos.get(idx);
        System.out
                .println("Modificando: " + gasto.getDescripcion() + " (deja en blanco para mantener el valor actual)");

        try {
            System.out.print("Nuevo monto [" + gasto.getCantidad() + "]: ");
            String montoStr = scanner.nextLine().trim();
            double monto = montoStr.isEmpty() ? gasto.getCantidad() : Double.parseDouble(montoStr);

            System.out.print("Nueva fecha [" + gasto.getFecha() + "]: ");
            String fechaStr = scanner.nextLine().trim();
            LocalDate fecha = fechaStr.isEmpty() ? gasto.getFecha() : LocalDate.parse(fechaStr);

            System.out.print("Nueva descripcion [" + gasto.getDescripcion() + "]: ");
            String desc = scanner.nextLine().trim();
            if (desc.isEmpty())
                desc = gasto.getDescripcion();

            Categoria cat = gasto.getCategoria();
            System.out.print("Nueva categoria [" + (cat != null ? cat.getNombre() : "ninguna")
                    + "] (deja en blanco para mantener): ");
            String catNombre = scanner.nextLine().trim();
            if (!catNombre.isEmpty()) {
                cat = buscarOCrearCategoria(catNombre);
            }

            controlador.modificarGasto(gasto, monto, fecha, desc, cat, null, null);
            System.out.println("[OK] Gasto modificado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("[ERR] Monto invalido.");
        } catch (DateTimeParseException e) {
            System.out.println("[ERR] Formato de fecha invalido. Usa yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERR] " + e.getMessage());
        }
    }

    private void eliminarGasto() {
        listarGastos();
        List<Gasto> gastos = controlador.getGastosPersonales(controlador.getUsuarioActivo());
        if (gastos.isEmpty())
            return;

        System.out.print("Indice del gasto a eliminar: ");
        int idx = leerEnteroSeguro();
        if (idx < 0 || idx >= gastos.size()) {
            System.out.println("Indice invalido.");
            return;
        }

        Gasto gasto = gastos.get(idx);
        System.out.print("Confirmar eliminacion de '" + gasto.getDescripcion() + "'? (s/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("s")) {
            controlador.eliminarGasto(gasto, null);
            System.out.println("[OK] Gasto eliminado correctamente.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // --- Alertas ---

    private void crearAlerta() {
        Usuario activo = controlador.getUsuarioActivo();
        if (activo == null) {
            System.out.println("No hay usuario activo.");
            return;
        }

        System.out.println("\n-- Crear alerta --");
        System.out.print("Tipo ([m]ensual / [s]emanal): ");
        String tipo = scanner.nextLine().trim().toLowerCase();
        if (!tipo.equals("m") && !tipo.equals("s")) {
            System.out.println("Tipo invalido.");
            return;
        }

        double limite;
        try {
            System.out.print("Limite (EUR): ");
            limite = Double.parseDouble(scanner.nextLine().trim());
            if (limite <= 0) {
                System.out.println("El limite debe ser > 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor no valido.");
            return;
        }

        System.out.print("Categoria (deja en blanco para 'General'): ");
        String catNombre = scanner.nextLine().trim();
        Categoria cat = catNombre.isEmpty() ? null : buscarOCrearCategoria(catNombre);

        if (tipo.equals("m"))
            controlador.registrarAlertaMensual(limite, cat);
        else
            controlador.registrarAlertaSemanal(limite, cat);

        System.out.println("[OK] Alerta creada.");
    }

    private void listarAlertas() {
        Usuario activo = controlador.getUsuarioActivo();
        if (activo == null) {
            System.out.println("No hay usuario activo.");
            return;
        }

        List<Alerta> alertas = RepositorioAlertas.getInstancia().getAlertasDelUsuario(activo);
        if (alertas.isEmpty()) {
            System.out.println("No tienes alertas.");
            return;
        }

        System.out.println("\n-- Tus alertas --");
        for (int i = 0; i < alertas.size(); i++) {
            Alerta a = alertas.get(i);
            String tipo = a.getEstrategia() instanceof EstrategiaSemanal ? "semanal" : "mensual";
            String cat = a.getCategoria() != null ? a.getCategoria().getNombre() : "General";
            double actual = a.calcularGastoActual(controlador.getGastosPersonales(activo));
            System.out.printf("[%d] %s | cat: %s | limite: %.2f EUR | actual: %.2f EUR%n",
                    i, tipo, cat, a.getLimite(), actual);
        }
    }

    private void listarNotificaciones() {
        Usuario activo = controlador.getUsuarioActivo();
        if (activo == null) {
            System.out.println("No hay usuario activo.");
            return;
        }

        List<Notificacion> notis = RepositorioAlertas.getInstancia().getNotificacionesDelUsuario(activo);
        if (notis.isEmpty()) {
            System.out.println("Sin notificaciones.");
            return;
        }

        System.out.println("\n-- Notificaciones --");
        for (Notificacion n : notis) {
            System.out.println("[" + n.getFecha() + "] " + n.getTitulo() + " - " + n.getMensaje());
        }
    }

    // --- Importar ---

    private void importarGastos() {
        if (controlador.getUsuarioActivo() == null) {
            System.out.println("No hay usuario activo.");
            return;
        }

        System.out.print("Ruta del fichero (CSV): ");
        String ruta = scanner.nextLine().trim();
        File f = new File(ruta);
        if (!f.exists() || !f.isFile()) {
            System.out.println("[ERR] Fichero no encontrado.");
            return;
        }

        try {
            int n = controlador.importarGastosDesdeFichero(f);
            System.out.println("[OK] Importados " + n + " gastos.");
        } catch (Exception e) {
            System.out.println("[ERR] Al importar: " + e.getMessage());
        }
    }

    // --- Helpers de entrada ---

    private double pedirMonto() {
        while (true) {
            System.out.print("Monto (EUR): ");
            try {
                double monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto > 0)
                    return monto;
                System.out.println("El monto debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("Valor no valido. Introduce un numero.");
            }
        }
    }

    private LocalDate pedirFecha() {
        while (true) {
            System.out.print("Fecha (yyyy-MM-dd, ENTER para hoy): ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty())
                return LocalDate.now();
            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Usa yyyy-MM-dd.");
            }
        }
    }

    private String pedirDescripcion() {
        while (true) {
            System.out.print("Descripcion: ");
            String desc = scanner.nextLine().trim();
            if (!desc.isEmpty())
                return desc;
            System.out.println("La descripcion no puede estar vacia.");
        }
    }

    private Categoria pedirOCrearCategoria() {
        List<Categoria> categorias = controlador.getCategorias();
        System.out.println("Categorias disponibles:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("[" + i + "] " + categorias.get(i).getNombre());
        }
        System.out.print("Numero de categoria (o nombre nuevo para crearla): ");
        String entrada = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(entrada);
            if (idx >= 0 && idx < categorias.size())
                return categorias.get(idx);
        } catch (NumberFormatException ignored) {
        }
        return buscarOCrearCategoria(entrada);
    }

    private Categoria buscarOCrearCategoria(String nombre) {
        return controlador.getCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> controlador.crearCategoria(nombre));
    }

    private int leerEnteroSeguro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
