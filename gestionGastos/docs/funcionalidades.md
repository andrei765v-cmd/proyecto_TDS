# Funcionalidades del Sistema

El sistema de gestión de gastos ofrece un conjunto completo de herramientas para el control financiero personal y compartido.

## 1. Gestión de Gastos Personales
- Registro de gastos con monto, fecha, descripción y categoría.
- Edición y eliminación de gastos con actualización en tiempo real.
- Filtrado avanzado por rango de fechas, descripción y categorías.

## 2. Visualización de Datos (Dashboard)
- Gráficos interactivos de distribución por categoría (PieChart).
- Gráfico de gastos recientes para analizar tendencias de consumo.
- Resúmenes financieros (Total mes, promedio diario).
- El filtrado avanzado tambien aplica en los gráficos del dashboard.

## 3. Cuentas Compartidas
- **Cuentas Equitativas**: El gasto se divide a partes iguales entre todos los participantes.
- **Cuentas Porcentuales**: Cada participante tiene un porcentaje asignado de responsabilidad sobre los gastos.
- **Gestión de Saldos**: Cálculo automático de quién debe a quién dentro de la cuenta.
- **Configuración Dinámica**: Posibilidad de renombrar cuentas, ajustar porcentajes o eliminar cuentas existentes.

## 4. Sistema de Alertas y Notificaciones
- Configuración de límites de gasto por categoría o generales.
- Tipos de alerta: Semanales y Mensuales.
- **Notificaciones Inteligentes**: 
    - Aviso inmediato al superar un límite.
    - Notificación de "estabilización" si un gasto se edita/elimina y el usuario vuelve a estar bajo el límite.

## 5. Importación de Datos
- Importación masiva desde ficheros **CSV** (formato estilo extracto bancario) y **JSON**.
- La extensión del fichero decide qué adaptador se usa.
- Si en el fichero aparecen categorías o usuarios que no existen, se crean automáticamente.

## 6. Persistencia Automática
- Los datos se cargan al arrancar y se guardan al cerrar la aplicación (al cerrar la ventana en GUI o al salir del CLI).
- Se guardan en formato JSON en la subcarpeta `datos/`, junto al programa.
- Para empezar de cero, basta con borrar esa carpeta.

## 7. Modo CLI
- Modo por terminal con un menú interactivo.
- Permite registrar, modificar, eliminar y listar gastos, cambiar de usuario, crear y listar alertas, ver notificaciones e importar gastos desde fichero.

