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
- **Repositorio**: Cada entidad del modelo (`Usuario`, `Categoria`, `Gasto`, `CuentaCompartida`, `Alerta`) tiene una interfaz `IRepositorioX` y una implementación concreta. El controlador depende solo de las interfaces, lo que aísla la persistencia del resto del código.
- **Factory Method**: Utilizado en `ImportadorFactory` para crear el importador adecuado según la extensión del fichero.
- **Adapter**: `AdaptadorCSV` y `AdaptadorJSON` integran datos externos (CSV bancario y JSON) al formato interno de `Gasto`.
- **Strategy**: Las alertas utilizan estrategias (`EstrategiaSemanal`, `EstrategiaMensual`) para calcular los límites de tiempo de forma flexible.
- **Observer**: Se utilizan `ObservableList` de JavaFX para que la UI se actualice automáticamente cuando los datos cambian en el modelo.

## Persistencia

La persistencia se basa en el **patrón Repositorio + Jackson/JSON**:

- Cada repositorio expone los métodos `cargar()` y `guardar()`, que leen y escriben un fichero JSON dedicado.
- `JsonStore` centraliza el `ObjectMapper` (con `JavaTimeModule` para `LocalDate` y *pretty printing*).
- `ControladorPrincipal.cargarTodo()` se invoca al arrancar, y `guardarTodo()` se dispara al cerrar la ventana JavaFX o al salir del modo CLI.
- Los datos se almacenan junto al programa, en la subcarpeta `datos/` (relativa al directorio desde el que se lanza la aplicación): `datos/{usuarios,categorias,gastos,cuentas,alertas,notificaciones}.json`.
- Las relaciones polimórficas (`CuentaCompartida → CuentaEquitativa/CuentaPorcentual`, `EstrategiaEvaluacion → Mensual/Semanal`) se serializan con `@JsonTypeInfo` + `@JsonSubTypes`.

## Estructura de Paquetes

- `es.um.tds.gestionGastos`: Clases principales `App` y `Launcher`.
- `es.um.tds.gestionGastos.Controladores`: Fachada `ControladorPrincipal`.
- `es.um.tds.gestionGastos.modelo`: Entidades de negocio y repositorios concretos.
- `es.um.tds.gestionGastos.modelo.persistencia`: Interfaces `IRepositorioX` y utilidad `JsonStore`.
- `es.um.tds.gestionGastos.modelo.CuentaCompartida`: Jerarquía de cuentas (`equitativa`/`porcentual`).
- `es.um.tds.gestionGastos.modelo.Alertas`: Alertas y estrategias de evaluación.
- `es.um.tds.gestionGastos.modelo.Importador`: Adaptadores CSV/JSON y factoría.
- `es.um.tds.gestionGastos.interfaz`: Controladores de la interfaz JavaFX.
- `es.um.tds.gestionGastos.cli`: Interfaz de línea de comandos.

