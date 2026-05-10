# Guía de Ejecución y Uso

La aplicación puede ejecutarse en dos modos: Interfaz Gráfica (GUI) y Línea de Comandos (CLI).

## Requisitos
- Java JDK 21 o superior.
- JavaFX (incluido en el proyecto mediante dependencias Maven/Gradle).

## Modos de Ejecución

### 1. Interfaz Gráfica (GUI) - Por defecto
Para lanzar la aplicación con la interfaz moderna de escritorio:
```bash
java -jar app-gestion-de-gastos.jar
```
*(O ejecutando la clase `App.java` o `Launcher.java` sin argumentos desde tu IDE)*.

Tambien puedes ejecutarlo haciendo **doble clic** en el .jar desde el explorador de archivos.

### 2. Línea de Comandos (CLI)
Para lanzar la aplicación en modo terminal:
```bash
java -jar nombre_del_archivo.jar --cli
```
*(O pasando el argumento `--cli` al ejecutar la clase `App.java` o `Launcher.java` en tu IDE)*.

Opciones del menú CLI:

| Opción | Acción |
| --- | --- |
| 1 | Registrar gasto |
| 2 | Modificar gasto |
| 3 | Eliminar gasto |
| 4 | Listar mis gastos |
| 5 | Cambiar de usuario |
| 6 | Crear alerta (mensual o semanal) |
| 7 | Listar alertas |
| 8 | Ver notificaciones |
| 9 | Importar gastos desde fichero (CSV o JSON) |
| 0 | Salir (guarda los datos antes de cerrar) |

## Uso Básico

### Primeros Pasos
1. **Seleccionar Usuario**: Al iniciar, selecciona un usuario del combo superior para cargar sus datos y notificaciones.
2. **Dashboard**: Revisa tu estado financiero actual en la primera pestaña.
3. **Añadir Gastos**: Usa el botón "+ Nuevo Gasto" en la cabecera. Puedes marcarlo como personal o asignarlo a una cuenta compartida.

### Gestión de Alertas
- Ve a la pestaña **Alertas**.
- Configura una nueva alerta definiendo un límite y una categoría.
- El sistema te notificará automáticamente en la zona inferior si te excedes.

### Cuentas Compartidas
- En la pestaña **Cuentas Compartidas**, selecciona una cuenta del desplegable.
- Verás el resumen de saldos: quién debe dinero (rojo) y quién debe recibirlo (verde).
- Usa **Configurar Cuenta** para editar la estructura de la cuenta o eliminarla si ya no es necesaria.

### Importar Gastos desde Fichero
La aplicación admite importación masiva en dos formatos. La extensión del fichero determina el adaptador (patrón Adaptador + Factory).

- **GUI**: botón **"Importar CSV/Bancario"** en la cabecera.
- **CLI**: opción **[9] Importar gastos desde fichero**, introduce la ruta absoluta.

#### Formato CSV
Cabecera estilo extracto bancario:
```
Date,Account,Category,Subcategory,Note,Payer,Amount,Currency
3/2/2022 10:11,Personal,con tarjeta,Comida,Desayuno,Me,4.50,EUR
```
- Fecha en `M/d/yyyy H:mm`.
- `Payer="Me"` asigna el gasto al usuario activo; cualquier otro nombre crea/usa ese usuario.

#### Formato JSON
Array de objetos:
```json
[
  {"fecha":"2026-04-01","descripcion":"Desayuno","monto":4.50,"categoria":"Comida","payer":"Me"},
  {"fecha":"2026-04-02","descripcion":"Metro","monto":1.50,"categoria":"Transporte"},
  {"fecha":"2026-04-03","descripcion":"Cine","monto":9.90,"categoria":"Ocio","payer":"Anthony"}
]
```
- `fecha` en `yyyy-MM-dd`.
- `payer` es opcional (si falta o es `"Me"`, se asigna al usuario activo).
- Las categorías inexistentes se crean automáticamente.
- Los usuarios inexistentes se crean automáticamente
### Persistencia de Datos
Los datos (usuarios, categorías, gastos, cuentas, alertas y notificaciones) se guardan automáticamente en la subcarpeta `datos/` ubicada junto al programa (relativa al directorio desde el que se lanza la aplicación), en formato JSON. La carga es automática al arrancar y el guardado se realiza al cerrar la ventana o al salir del CLI con la opción `0`.

Para empezar desde cero, borra la carpeta `datos/`:
```powershell
Remove-Item .\datos -Recurse -Force
```
