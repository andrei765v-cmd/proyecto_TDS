# Gestión de Gastos

Una aplicación diseñada para el control de gastos, cuenta con soporte para importación de datos CSV, una **interfaz gráfica** con JavaFX y una **interfaz de consola** (CLI).

## Participantes

| Nombre | Correo y subgrupo |
| --- | --- |
| Pedro Romero Ruiz | p.romeroruiz@um.es - 3.4 |
| Alejandro Andrei Vasilache | aa.vasilache@um.es - 3.4 |
| Pablo Ubeda Garcia | pablo.ubedag@um.es - 3.4 |

## Descripción

Este proyecto permite a los usuarios gestionar sus finanzas personales, permitiendo además la gestión de **cuentas compartidas**, configuración de **alertas de gasto** y visualización de datos mediante gráficas.

## Cómo Ejecutar el Proyecto

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

- **[Arquitectura y Diseño](gestionGastos/docs/arquitectura.md)**: Patrones GRASP, patrones de diseño (Singleton, Repositorio, Factory, Adapter, Strategy, Observer), persistencia y decisiones de diseño.
- **[Historias de Usuario](gestionGastos/docs/historias_usuario.md)**: Especificación de las 26 historias de usuario con criterios de aceptación.
- **[Diagrama de Interacción](gestionGastos/docs/diagrama_interaccion.md)**: Diagrama de secuencia de la HU-05 (*Crear Nueva Categoría*).
- **[Funcionalidades Detalladas](gestionGastos/docs/funcionalidades.md)**: Explicación de gastos, cuentas compartidas, alertas e importación.
- **[Guía de Usuario](gestionGastos/docs/guia_usuario.md)**: Manual con capturas de la interfaz, modo CLI, importación de datos y persistencia.
- **Diagrama de Clases**: Disponible en [`gestionGastos/docs/UML.png`](gestionGastos/docs/UML.png) (fuente editable: [`UML.drawio`](gestionGastos/docs/UML.drawio)).

---
Proyecto de **Tecnologías de Diseño de Software (TDS)**.
