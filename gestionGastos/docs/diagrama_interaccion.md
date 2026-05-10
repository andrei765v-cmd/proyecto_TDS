# Diagrama de Interacción

Hemos elegido la historia de usuario **HU-05 — Crear Nueva Categoría** porque es sencilla y se entiende bien: muestra el recorrido completo desde que el usuario interactúa con la vista hasta que se crea el objeto en el modelo, sin ramificaciones que compliquen el diagrama.

## Escenario

El usuario abre el modal **"Nueva Categoría"**, escribe el nombre `"Restaurantes"` y pulsa *Guardar*. La categoría se añade al sistema y queda disponible para usarla en gastos futuros.

## Participantes

- **Usuario**: persona que usa la interfaz gráfica.
- **`AgregarCategoriaController`**: controlador FXML del modal.
- **`ControladorPrincipal`**: fachada que coordina los casos de uso.
- **`RepositorioCategorias`**: repositorio que guarda las categorías en memoria y las persiste en JSON.

## Diagrama de secuencia

[![](https://mermaid.ink/img/pako:eNqFkk1v2zAMhv-KwNMKuIHrJHbsQ4AixXYqMBTYDoMvnMU6Qm3Jo2R0aZD_PskfWbNsmC82qfchX9E8QmUkQQGWfvSkK3pQWDO2pRb-wcoZFl9sj6zMmOqQnapUh9qJr8o6FGjFfc1UI-_QUW1Y4c5ox6ZpiK-hj1jtUQ7YJENp-DMrHQTNNfBEnQnq8LbKG1Lm3MiWegQmj7fb7WCqEGQrVt9JlPBEPtGzL0W2BHEQXd9Y9AefPCKRSxhLDKAvMBksRMX07k4f_qx0M2KT3IPB4ET9SxwkZ6WmV_Hf8gNx4Womhk4SL138HsDfdfMlp3l5mSJmFNSI1sh5_BBBzUpC4binCFriFkMIx3BegttTSyUU_tMP8CVM8OQZ_7u-GdPOGJu-3kPxjI31Ud9J72har3OWSUvinem1gyJZDzWgOMLPEK0XqzxLl8k6XaXxMo_g4LNxtkjzzSZdbZZZEudpdorgbegaL7Ikzzd5nN6lWbKM8ywCkmFjHscdH1b99AudKPo6?type=png)](https://mermaid.live/edit#pako:eNqFkk1v2zAMhv-KwNMKuIHrJHbsQ4AixXYqMBTYDoMvnMU6Qm3Jo2R0aZD_PskfWbNsmC82qfchX9E8QmUkQQGWfvSkK3pQWDO2pRb-wcoZFl9sj6zMmOqQnapUh9qJr8o6FGjFfc1UI-_QUW1Y4c5ox6ZpiK-hj1jtUQ7YJENp-DMrHQTNNfBEnQnq8LbKG1Lm3MiWegQmj7fb7WCqEGQrVt9JlPBEPtGzL0W2BHEQXd9Y9AefPCKRSxhLDKAvMBksRMX07k4f_qx0M2KT3IPB4ET9SxwkZ6WmV_Hf8gNx4Womhk4SL138HsDfdfMlp3l5mSJmFNSI1sh5_BBBzUpC4binCFriFkMIx3BegttTSyUU_tMP8CVM8OQZ_7u-GdPOGJu-3kPxjI31Ud9J72har3OWSUvinem1gyJZDzWgOMLPEK0XqzxLl8k6XaXxMo_g4LNxtkjzzSZdbZZZEudpdorgbegaL7Ikzzd5nN6lWbKM8ywCkmFjHscdH1b99AudKPo6)

## Explicación paso a paso

1. **Acción del usuario**: escribe el nombre en el campo de texto y pulsa *Guardar*.
2. **La vista llama a la fachada**: el controlador FXML llama a `crearCategoria` sobre `ControladorPrincipal`. Así la vista no necesita conocer al repositorio (bajo acoplamiento).
3. **La fachada llama al repositorio**: `ControladorPrincipal` llama a `crear` en `RepositorioCategorias`, que es el que se encarga de las categorías (experto en información).
4. **Se crea el objeto**: el repositorio crea una nueva `Categoria` y la añade a su lista observable. Como la lista está enlazada con la UI, los desplegables se actualizan solos (patrón Observer).
5. **Confirmación**: la respuesta vuelve por las llamadas y la vista cierra el modal.

> **Nota**: el JSON no se escribe a disco aquí, sino al cerrar la aplicación con `ControladorPrincipal.guardarTodo()`. De esta forma evitamos escribir cada vez que se hace una operación.
