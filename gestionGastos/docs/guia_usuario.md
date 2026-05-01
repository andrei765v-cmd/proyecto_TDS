# Guía de Ejecución y Uso

La aplicación puede ejecutarse en dos modos: Interfaz Gráfica (GUI) y Línea de Comandos (CLI).

## Requisitos
- Java JDK 17 o superior.
- JavaFX (incluido en el proyecto mediante dependencias Maven/Gradle).

## Modos de Ejecución

### 1. Interfaz Gráfica (GUI) - Por defecto
Para lanzar la aplicación con la interfaz moderna de escritorio:
```bash
mvn javafx:run
```
*(O ejecutando la clase `App.java` sin argumentos desde tu IDE)*.

### 2. Línea de Comandos (CLI)
Para lanzar la aplicación en modo terminal (útil para sistemas sin entorno gráfico o gestión rápida):
```bash
java -jar nombre_del_archivo.jar --cli
```
*(O pasando el argumento `--cli` al ejecutar la clase `App.java` en tu IDE)*.

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
