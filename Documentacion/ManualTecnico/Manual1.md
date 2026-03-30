# Manual Técnico

## 1. Descripción general del sistema  
Este sistema es una aplicación desarrollada en Java que permite visualizar el funcionamiento de distintos algoritmos de ordenamiento. Su objetivo principal es mostrar de forma gráfica cómo se ordenan los datos, mientras se registran estadísticas como comparaciones, intercambios e iteraciones.

Además, el sistema genera automáticamente un reporte en HTML con los resultados de cada ejecución, lo que facilita el análisis del comportamiento de los algoritmos.

---

## 2. Estructura del proyecto  
El proyecto está organizado en paquetes, principalmente enfocados en la interfaz gráfica y la lógica del sistema.  

- **Interfaz:** contiene las ventanas y componentes visuales del sistema.  
- **Lógica:** donde se maneja el comportamiento de los algoritmos y procesos internos.  

La clase principal del sistema es `VisPanelControl`, la cual controla tanto la interfaz como la ejecución de los algoritmos.

![Estructura del proyecto](./Imagenes/Captura%20de%20pantalla%202026-03-29%20225914.png)

---

## 3. Librerías utilizadas  
Para el desarrollo del sistema se utilizaron algunas librerías importantes:

- **JFreeChart:** utilizada para generar la gráfica de barras que representa los datos.
- **Swing:** para la construcción de la interfaz gráfica.
- **AWT:** para el manejo de colores y diseño visual.
- **IO de Java:** para la lectura de archivos y generación del reporte HTML.

Estas librerías permiten que el sistema sea visual, interactivo y funcional.

![Librerías](./Imagenes/Captura%20de%20pantalla%202026-03-29%20225932.png)

---

## 4. Lógica general del sistema  
El funcionamiento del sistema sigue un flujo bastante directo:

1. El usuario ingresa los datos manualmente, los carga desde archivo o los genera aleatoriamente.  
2. Se selecciona el algoritmo de ordenamiento, el tipo de orden (ascendente o descendente) y la velocidad.  
3. Al iniciar, el sistema ejecuta el algoritmo en un hilo independiente.  
4. Durante la ejecución:
   - Se actualiza la gráfica en tiempo real.  
   - Se registran comparaciones e intercambios.  
   - Se muestran los pasos en el log.  
5. Al finalizar, se genera automáticamente un reporte en HTML.

![Ejecución](./Imagenes/Captura%20de%20pantalla%202026-03-29%20230000.png)

---

## 5. Métodos importantes  

### inicializarGraficaCustom()  
Este método se encarga de crear la gráfica de barras utilizando JFreeChart y configurarla dentro del panel de la interfaz.

---

### actualizarGrafica() 
Permite refrescar la gráfica cada vez que los datos cambian, mostrando el estado actual del arreglo.

---

### esperar()
Controla la velocidad de la animación según la opción seleccionada por el usuario (lento, medio o rápido).

---

### ejecutarBubbleSort() 
Implementa el algoritmo Bubble Sort mostrando visualmente cada comparación e intercambio.

---

### ejecutarShellSort()  
Realiza el ordenamiento utilizando el método Shell Sort, aplicando saltos (gaps) y mostrando el proceso en la gráfica.

---

### ejecutarQuickSort()  
Ejecuta el algoritmo Quick Sort de forma recursiva, utilizando el método de partición para ordenar los datos.

---

### particion() 
Forma parte del Quick Sort y se encarga de reorganizar los elementos alrededor de un pivote.

---

### registrarOperacion() 
Registra cada comparación o intercambio realizado durante la ejecución y lo muestra en el log del sistema.

---

### generarReporteHTML() 
Genera un archivo HTML con toda la información de la ejecución, incluyendo estadísticas y el historial de ejecuciones.


---

## 6. Manejo de datos  
El sistema trabaja principalmente con arreglos de tipo entero (`int[]`), donde se almacenan los valores a ordenar.  

También se maneja una copia del arreglo original para poder compararlo con el resultado final en el reporte.

---

## 7. Consideraciones técnicas  
- El uso de hilos permite que la interfaz no se congele durante la ejecución.  
- Se utiliza `SwingUtilities.invokeLater` para asegurar que la interfaz se actualice correctamente.  
- Se implementa un renderer personalizado para cambiar los colores de las barras durante la animación.  

![Colores](./Imagenes/Captura%20de%20pantalla%202026-03-29%20230113.png)