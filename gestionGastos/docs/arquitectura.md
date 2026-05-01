# Arquitectura del Proyecto

Este proyecto ha sido diseñado siguiendo principios de diseño de software para asegurar la mantenibilidad y escalabilidad.

## Patrones GRASP Aplicados

Se han utilizado los patrones **GRASP (General Responsibility Assignment Software Patterns)** para la asignación de responsabilidades:

- **Controlador**: La clase `ControladorPrincipal` actúa como el punto de entrada para todas las operaciones del sistema, separando la interfaz de usuario de la lógica de negocio.
- **Experto en Información**: Cada una de las clases del modelo es responsable de la lógica que involucra sus propios datos.
- **Alta Cohesión**: Las funcionalidades están separadas en clases específicas (repositorios para datos, controladores para UI, modelos para lógica).
- **Bajo Acoplamiento**: Los controladores de UI no se comunican directamente con los repositorios, sino a través de la fachada del `ControladorPrincipal`.

## Patrones de Diseño Utilizados

Además del patron experto y creador, se han puesto en práctica diversos patrones de diseño como:

- **Singleton**: `ControladorPrincipal` y los repositorios utilizan el patrón Singleton para asegurar una única instancia global de los datos durante la ejecución.
- **Factory Method**: Utilizado en `ImportadorFactory` para crear el importador adecuado según la extensión del fichero.
- **Adapter**: El `AdaptadorCSV` permite integrar datos externos en el formato interno de `Gasto`.
- **Strategy**: Las alertas utilizan estrategias (`EstrategiaSemanal`, `EstrategiaMensual`) para calcular los límites de tiempo de forma flexible.
- **Observer**: Se utilizan `ObservableList` de JavaFX para que la UI se actualice automáticamente cuando los datos cambian en el modelo.

## Estructura de Paquetes

- `es.um.tds.gestionGastos`: Clase principal `App`.
- `es.um.tds.gestionGastos.Controladores`: Lógica de control y orquestación.
- `es.um.tds.gestionGastos.modelo`: Clases de negocio y repositorios.
- `es.um.tds.gestionGastos.interfaz`: Controladores de la interfaz JavaFX.
- `es.um.tds.gestionGastos.cli`: Interfaz de línea de comandos.

