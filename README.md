# Gestión de Gastos

Una aplicación diseñada para el control de gastos, cuenta con soporte para importación de datos CSV, una **interfaz gráfica** con JavaFX y una **interfaz de consola** (CLI).

## Descripción

Este proyecto permite a los usuarios gestionar sus finanzas personales, permitiendo además la gestión de **cuentas compartidas**, configuración de **alertas de gasto** y visualización de datos mediante gráficas.

## Cómo Ejecutar el Proyecto

Asegurate de estar en la carpeta `target` para ejecutar los siguientes comandos:

### Modo Gráfico (GUI)
Arranca la interfaz visual por defecto:
```bash
java -jar app-gestion-de-gastos.jar
```

*(O ejecutando la clase `App.java` o `Launcher.java` sin argumentos desde tu IDE)*.

Tambien puedes ejecutarlo haciendo **doble clic** en el .jar desde el explorador de archivos.

### Modo Consola (CLI)
Arranca el modo interactivo por terminal:
```bash
java -jar app-gestion-de-gastos.jar --cli
```

*(O añade el argumento `--cli` al ejecutar la clase `App.java` o `Launcher.java` en tu IDE)*.

## Documentación Relevante

Puedes encontrar detalles más específicos en los siguientes documentos:

- **[Arquitectura y Diseño](gestionGastos/docs/arquitectura.md)**: Patrones GRASP, Singleton, Factory, Adapter y estructura del proyecto.
- **[Funcionalidades Detalladas](gestionGastos/docs/funcionalidades.md)**: Explicación de gastos, cuentas compartidas, alertas e importación.
- **[Guía de Usuario](gestionGastos/docs/guia_usuario.md)**: Instrucciones paso a paso para configurar y usar la aplicación.

---
Proyecto de **Tecnologías de Diseño de Software (TDS)**.
