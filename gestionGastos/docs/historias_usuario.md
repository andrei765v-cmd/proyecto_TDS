# Historias de Usuario

Listado de las historias de usuario (HU) que hemos seguido para desarrollar el proyecto **Gestión de Gastos**. Cada una se escribe en el formato *Como/Quiero/Para* y lleva sus criterios de aceptación en *Dado/Cuando/Entonces*.

Las hemos agrupado en ocho actividades, una por cada bloque de funcionalidad.

---

## Actividad 1: Gestión de Gastos

### HU-01: Registrar Gasto (Interfaz Gráfica)
- **Como** usuario de la aplicación
- **Quiero** registrar un gasto indicando cantidad, fecha y categoría desde la interfaz gráfica
- **Para** llevar un control detallado de mis gastos personales

**Criterios de aceptación:**

1. *Datos completos*
   - **Dado que** el usuario accede a la ventana de registro de gastos
   - **Cuando** introduce cantidad (numérica), fecha y selecciona una categoría válida
   - **Entonces** el sistema crea el gasto y lo guarda en la base de datos persistente
2. *Datos incompletos*
   - **Dado que** el usuario está registrando un gasto
   - **Cuando** falta algún dato obligatorio (cantidad, fecha o categoría)
   - **Entonces** el sistema muestra un mensaje de error y no permite guardar el gasto

### HU-02: Registrar Gasto (Línea de Comandos)
- **Como** usuario avanzado
- **Quiero** registrar un gasto desde la línea de comandos
- **Para** agilizar el registro de gastos sin usar la interfaz gráfica

**Criterios de aceptación:**

1. *Comando válido*
   - **Dado que** el usuario ejecuta la aplicación desde terminal
   - **Cuando** introduce el comando con el formato correcto (ej.: `add 50.0 2024-11-08 Alimentación`)
   - **Entonces** el sistema registra el gasto y confirma la operación
2. *Comando inválido*
   - **Dado que** el usuario introduce un comando
   - **Cuando** el formato es incorrecto o faltan parámetros
   - **Entonces** el sistema muestra un mensaje de error con el formato correcto

### HU-03: Editar Gasto
- **Como** usuario
- **Quiero** modificar la cantidad, fecha o categoría de un gasto registrado
- **Para** corregir errores o actualizar información

**Criterios de aceptación:**

1. *Edición exitosa*
   - **Dado que** el usuario selecciona un gasto existente
   - **Cuando** modifica algún dato y confirma los cambios
   - **Entonces** el sistema actualiza el gasto y persiste los cambios

### HU-04: Eliminar Gasto
- **Como** usuario
- **Quiero** borrar un gasto registrado
- **Para** eliminar registros incorrectos o no deseados

**Criterios de aceptación:**

1. *Eliminación confirmada*
   - **Dado que** el usuario selecciona un gasto
   - **Cuando** confirma la eliminación
   - **Entonces** el sistema borra el gasto permanentemente
2. *Eliminación cancelada*
   - **Dado que** el usuario intenta eliminar un gasto
   - **Cuando** cancela la operación
   - **Entonces** el sistema mantiene el gasto sin cambios

---

## Actividad 2: Gestión de Categorías

### HU-05: Crear Nueva Categoría
- **Como** usuario
- **Quiero** crear nuevas categorías personalizadas
- **Para** organizar mis gastos según mis necesidades específicas

**Criterios de aceptación:**

1. *Categoría válida*
   - **Dado que** el usuario accede a la gestión de categorías
   - **Cuando** introduce un nombre único para la nueva categoría
   - **Entonces** el sistema crea la categoría y la hace disponible para asignar gastos
2. *Categoría duplicada*
   - **Dado que** el usuario intenta crear una categoría
   - **Cuando** el nombre ya existe
   - **Entonces** el sistema muestra un error indicando que la categoría ya existe

### HU-06: Usar Categorías Predefinidas
- **Como** usuario nuevo
- **Quiero** tener categorías predefinidas (Alimentación, Transporte, Entretenimiento)
- **Para** empezar a registrar gastos inmediatamente sin configuración previa

**Criterios de aceptación:**

1. *Categorías iniciales*
   - **Dado que** un usuario inicia la aplicación por primera vez
   - **Cuando** accede al registro de gastos
   - **Entonces** el sistema muestra las categorías predefinidas disponibles

---

## Actividad 3: Visualización de Gastos

### HU-07: Ver Gastos en Tabla/Lista
- **Como** usuario
- **Quiero** ver todos mis gastos en formato de tabla o lista
- **Para** consultar el detalle de cada gasto registrado

**Criterios de aceptación:**

1. *Visualización completa*
   - **Dado que** el usuario accede a la vista de gastos
   - **Cuando** selecciona visualización en tabla/lista
   - **Entonces** el sistema muestra todos los gastos con cantidad, fecha y categoría

### HU-08: Visualizar Gastos en Gráfico de Barras
- **Como** usuario
- **Quiero** ver mis gastos representados en un gráfico de barras
- **Para** identificar rápidamente qué categorías tienen más gasto

**Criterios de aceptación:**

1. *Gráfico generado*
   - **Dado que** existen gastos registrados
   - **Cuando** el usuario selecciona visualización en gráfico de barras
   - **Entonces** el sistema muestra un gráfico de barras agrupado por categorías

### HU-09: Visualizar Gastos en Gráfico Circular
- **Como** usuario
- **Quiero** ver la distribución de gastos en un gráfico circular
- **Para** comprender visualmente el porcentaje de cada categoría respecto al total

**Criterios de aceptación:**

1. *Gráfico circular generado*
   - **Dado que** hay gastos en diferentes categorías
   - **Cuando** el usuario selecciona visualización circular
   - **Entonces** el sistema muestra un gráfico de pastel con porcentajes por categoría

### HU-10: Visualizar Gastos en Calendario
- **Como** usuario
- **Quiero** ver mis gastos en una vista de calendario
- **Para** identificar visualmente en qué días he tenido más gastos

**Criterios de aceptación:**

1. *Vista calendario*
   - **Dado que** existen gastos con fechas
   - **Cuando** el usuario selecciona visualización de calendario
   - **Entonces** el sistema muestra un calendario con los gastos en sus fechas correspondientes

---

## Actividad 4: Filtrado de Gastos

### HU-11: Filtrar Gastos por Meses
- **Como** usuario
- **Quiero** filtrar gastos por uno o varios meses
- **Para** analizar mis gastos en períodos específicos

**Criterios de aceptación:**

1. *Filtro aplicado*
   - **Dado que** el usuario selecciona uno o más meses
   - **Cuando** aplica el filtro
   - **Entonces** el sistema muestra solo los gastos de los meses seleccionados

### HU-12: Filtrar Gastos por Rango de Fechas
- **Como** usuario
- **Quiero** filtrar gastos por un intervalo de fechas personalizado
- **Para** analizar gastos en períodos específicos que no coincidan con meses completos

**Criterios de aceptación:**

1. *Rango válido*
   - **Dado que** el usuario introduce una fecha inicio y una fecha fin
   - **Cuando** aplica el filtro
   - **Entonces** el sistema muestra solo los gastos dentro del rango especificado

### HU-13: Filtrar Gastos por Categorías
- **Como** usuario
- **Quiero** filtrar gastos por una o varias categorías
- **Para** analizar el gasto en áreas específicas

**Criterios de aceptación:**

1. *Filtro de categorías*
   - **Dado que** el usuario selecciona una o más categorías
   - **Cuando** aplica el filtro
   - **Entonces** el sistema muestra solo los gastos de las categorías seleccionadas

### HU-14: Filtrar Gastos con Múltiples Criterios
- **Como** usuario
- **Quiero** combinar filtros de fecha y categoría simultáneamente
- **Para** realizar análisis detallados de mis gastos

**Criterios de aceptación:**

1. *Filtros combinados*
   - **Dado que** el usuario selecciona categorías y rango de fechas
   - **Cuando** aplica ambos filtros
   - **Entonces** el sistema muestra solo los gastos que cumplan todos los criterios

---

## Actividad 5: Sistema de Alertas

### HU-15: Crear Alerta Semanal
- **Como** usuario
- **Quiero** configurar una alerta de gasto semanal con un límite específico
- **Para** recibir notificaciones cuando supere mi presupuesto semanal

**Criterios de aceptación:**

1. *Alerta creada*
   - **Dado que** el usuario accede a configuración de alertas
   - **Cuando** crea una alerta semanal con un límite (ej.: 200€)
   - **Entonces** el sistema guarda la alerta y comienza a monitorear los gastos semanales

### HU-16: Crear Alerta Mensual
- **Como** usuario
- **Quiero** configurar una alerta de gasto mensual
- **Para** controlar mi presupuesto mensual total

**Criterios de aceptación:**

1. *Alerta mensual*
   - **Dado que** el usuario crea una alerta mensual con límite
   - **Cuando** los gastos del mes superan el límite
   - **Entonces** el sistema genera una notificación

### HU-17: Crear Alerta por Categoría
- **Como** usuario
- **Quiero** configurar alertas vinculadas a categorías específicas
- **Para** controlar el gasto en áreas concretas (ej.: 100€ mensuales en videojuegos)

**Criterios de aceptación:**

1. *Alerta con categoría*
   - **Dado que** el usuario crea una alerta mensual para "Videojuegos" con límite de 100€
   - **Cuando** los gastos en esa categoría superan 100€ en el mes
   - **Entonces** el sistema genera una notificación específica de esa categoría

### HU-18: Recibir Notificaciones de Alertas
- **Como** usuario
- **Quiero** recibir notificaciones cuando supere los límites configurados
- **Para** estar informado y poder ajustar mi comportamiento de gasto

**Criterios de aceptación:**

1. *Notificación generada*
   - **Dado que** existe una alerta activa y el gasto supera el límite
   - **Cuando** se registra el gasto que excede el límite
   - **Entonces** el sistema muestra una notificación al usuario

### HU-19: Ver Historial de Notificaciones
- **Como** usuario
- **Quiero** consultar todas las notificaciones pasadas
- **Para** revisar cuándo y por qué he recibido alertas anteriormente

**Criterios de aceptación:**

1. *Historial disponible*
   - **Dado que** se han generado notificaciones en el pasado
   - **Cuando** el usuario accede al historial
   - **Entonces** el sistema muestra todas las notificaciones con fecha y detalles

---

## Actividad 6: Cuentas Compartidas

### HU-20: Crear Cuenta Compartida Equitativa
- **Como** usuario
- **Quiero** crear una cuenta de gasto compartida con otras personas con distribución equitativa
- **Para** dividir gastos de forma igualitaria (viajes, piso compartido, etc.)

**Criterios de aceptación:**

1. *Cuenta creada*
   - **Dado que** el usuario introduce nombres de las personas
   - **Cuando** crea la cuenta compartida equitativa
   - **Entonces** el sistema crea la cuenta con todos los saldos a 0 y distribución equitativa

### HU-21: Crear Cuenta Compartida con Porcentajes
- **Como** usuario
- **Quiero** crear una cuenta compartida definiendo el porcentaje de cada persona
- **Para** dividir gastos de forma proporcional según acuerdo (ej.: 40%-30%-30%)

**Criterios de aceptación:**

1. *Porcentajes válidos*
   - **Dado que** el usuario introduce porcentajes para cada persona
   - **Cuando** la suma es 100%
   - **Entonces** el sistema crea la cuenta con la distribución personalizada
2. *Porcentajes inválidos*
   - **Dado que** el usuario introduce porcentajes
   - **Cuando** la suma no es 100%
   - **Entonces** el sistema muestra un error y no crea la cuenta

### HU-22: Registrar Gasto en Cuenta Compartida
- **Como** miembro de una cuenta compartida
- **Quiero** registrar un gasto indicando quién lo pagó
- **Para** actualizar los saldos de forma automática

**Criterios de aceptación:**

1. *Saldos actualizados*
   - **Dado que** existe una cuenta compartida con 3 personas
   - **Cuando** se registra un gasto de 30€ pagado por una persona
   - **Entonces** el sistema actualiza los saldos (pagador: +20€, otros: -10€ cada uno)

### HU-23: Ver Saldos de Cuenta Compartida
- **Como** miembro de una cuenta compartida
- **Quiero** consultar el saldo de cada persona
- **Para** saber quién debe dinero y quién le deben

**Criterios de aceptación:**

1. *Saldos visibles*
   - **Dado que** una cuenta compartida tiene gastos registrados
   - **Cuando** el usuario consulta la cuenta
   - **Entonces** el sistema muestra el saldo de cada persona (positivo o negativo)

---

## Actividad 7: Importación de Datos

### HU-24: Importar Gastos desde Archivo
- **Como** usuario
- **Quiero** importar gastos desde un archivo de texto plano
- **Para** incorporar datos de mi banco u otras fuentes sin introducirlos manualmente

**Criterios de aceptación:**

1. *Importación exitosa*
   - **Dado que** el usuario selecciona un archivo con formato válido
   - **Cuando** ejecuta la importación
   - **Entonces** el sistema lee el archivo y crea los gastos correspondientes
2. *Formato incorrecto*
   - **Dado que** el archivo no tiene el formato esperado
   - **Cuando** se intenta importar
   - **Entonces** el sistema muestra un error indicando el problema

### HU-25: Soportar Múltiples Formatos de Importación
- **Como** usuario
- **Quiero** que el sistema pueda importar diferentes formatos de archivo
- **Para** utilizar datos de diferentes fuentes bancarias o aplicaciones

**Criterios de aceptación:**

1. *Formato adaptado*
   - **Dado que** existen diferentes adaptadores de formato
   - **Cuando** el usuario importa un archivo
   - **Entonces** el sistema detecta automáticamente el formato o permite seleccionarlo

---

## Actividad 8: Persistencia de Datos

### HU-26: Guardar Datos Automáticamente
- **Como** usuario
- **Quiero** que todos mis datos se guarden automáticamente
- **Para** no perder información al cerrar la aplicación

**Criterios de aceptación:**

1. *Persistencia automática*
   - **Dado que** el usuario ha realizado cambios (gastos, categorías, alertas)
   - **Cuando** cierra la aplicación
   - **Entonces** el sistema guarda todos los datos en formato JSON
2. *Datos recuperados*
   - **Dado que** la aplicación se cierra con datos guardados
   - **Cuando** el usuario vuelve a abrir la aplicación
   - **Entonces** el sistema carga todos los datos previos correctamente
