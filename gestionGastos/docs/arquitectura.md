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

Para guardar los datos usamos la librería **Jackson** y el patrón **Repositorio**:

- Cada repositorio tiene los métodos `cargar()` y `guardar()`, que leen y escriben un fichero JSON.
- La clase `JsonStore` agrupa la configuración del `ObjectMapper` (soporte de `LocalDate` y salida en formato bonito).
- `ControladorPrincipal.cargarTodo()` se llama al arrancar la aplicación. `guardarTodo()` se llama al cerrar la ventana en GUI o al salir del CLI.
- Los datos se guardan en la subcarpeta `datos/`, junto al programa: `usuarios.json`, `categorias.json`, `gastos.json`, etc.
- Para las clases con subclases (`CuentaCompartida` y `EstrategiaEvaluacion`) usamos las anotaciones `@JsonTypeInfo` y `@JsonSubTypes`. Así Jackson sabe qué subclase tiene que crear al leer el JSON.

## Decisiones de Diseño

El enunciado ya nos fija algunas cosas: usar JavaFX, Jackson para guardar en JSON, el patrón Repositorio, y gestionar el proyecto con Maven y Git. Estas son las decisiones que hemos tomado nosotros a partir de ahí.

### Compartir el modelo entre la GUI y la CLI
El enunciado pide que también haya una línea de comandos para registrar, modificar y borrar gastos. Para no escribir la lógica dos veces, tanto `App` (GUI) como `cli.CLI` (consola) llaman a la misma fachada `ControladorPrincipal` y usan los mismos repositorios. Si en algún momento añadimos otra interfaz, solo tendría que llamar a la fachada.

### Repositorios como Singleton accedidos por interfaz
Los repositorios son Singletons (como pide el enunciado), pero en `ControladorPrincipal` los declaramos por su interfaz `IRepositorioX`, no por la clase concreta. Si más adelante quisiéramos cambiar la forma de guardar los datos, no haría falta tocar la fachada.

### Guardar los datos al cerrar
El enunciado pide persistir la información, pero no dice cada cuánto. Hemos decidido escribir los JSON solo al cerrar la aplicación: al cerrar la ventana en GUI o al salir con la opción `0` en CLI. Así no estamos escribiendo a disco cada vez que el usuario hace algo, y los repositorios se quedan más sencillos porque solo trabajan en memoria. La pega es que si la aplicación se cierra de forma anormal se pueden perder los cambios desde el último arranque.

### Estrategia en las alertas
Como pide el enunciado, las alertas usan el patrón Estrategia. La interfaz `EstrategiaEvaluacion` define cómo se calcula el gasto del periodo, y tenemos dos implementaciones: `EstrategiaSemanal` y `EstrategiaMensual`. Si más adelante quisiéramos añadir, por ejemplo, una anual, solo habría que crear una clase nueva.

### Adapter y Factoría para importar
Para importar datos externos usamos los patrones Adapter y Factoría. La interfaz `Importador` representa la operación de importar; `AdaptadorCSV` y `AdaptadorJSON` se encargan de cada formato, y `ImportadorFactory` decide cuál crear según la extensión del fichero. Para añadir otro formato basta con escribir una clase nueva y registrarla en la factoría.

### La lista de personas de una cuenta compartida no se puede cambiar
El enunciado dice que, una vez creada una cuenta, la lista de participantes no se puede modificar. Para que esto se cumpla, devolvemos la lista con `Collections.unmodifiableList(...)`, así no se puede añadir ni quitar a nadie desde fuera. Lo único que sí se puede cambiar es el nombre de la cuenta y, si es porcentual, los porcentajes (siempre que sigan sumando 100%).

### Cómo guardamos las clases con subclases en JSON
Tanto `CuentaCompartida` como `EstrategiaEvaluacion` tienen subclases. Para que Jackson sepa qué subclase tiene que crear al leer el fichero, hemos anotado las clases padre con `@JsonTypeInfo` y `@JsonSubTypes`. Esto añade un campo `"tipo"` en el JSON que indica de qué subclase se trata, y nos ahorra escribir deserializadores a mano.

### Lista observable para que la UI se entere de los cambios
La fachada tiene una `ObservableList<Gasto>` que las vistas usan directamente. Cuando se añade, modifica o borra un gasto, la lista se actualiza y JavaFX refresca la tabla de *Mis Gastos* y los gráficos del *Dashboard* sin que tengamos que avisar a la UI a mano.

### Streams y lambdas
El enunciado nos recomienda usar *streams* y expresiones lambda. Las hemos usado en los filtros de gastos, en los cálculos de saldos, en las búsquedas y en la evaluación de alertas. Queda más corto que con bucles `for`.

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

