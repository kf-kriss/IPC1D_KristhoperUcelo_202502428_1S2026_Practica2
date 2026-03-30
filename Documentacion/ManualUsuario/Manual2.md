# Manual de Usuario

## 1. Introducción  
Este sistema permite visualizar cómo funcionan distintos algoritmos de ordenamiento de manera interactiva. El usuario puede ingresar datos, seleccionar el algoritmo y observar paso a paso cómo se ordenan.

---

## 2. Interfaz del sistema  
Al iniciar la aplicación, se muestra una ventana principal que contiene el área de ingreso de datos, botones de control, opciones de configuración, la gráfica de visualización, estadísticas y el log de operaciones.

![Interfaz del sistema](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231438.png)

---

## 3. Ingreso de datos  

El sistema permite ingresar datos de tres formas:

### 3.1 Ingreso manual  
El usuario puede escribir números separados por comas en el campo de texto.  


Luego debe presionar el botón **Cargar**.

![Ingreso manual](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231513.png)

---

### 3.2 Generación aleatoria  
Al presionar el botón **Aleatorio**, el sistema solicitará la cantidad de números a generar (entre 5 y 30).  

![Datos aleatorios](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231539.png)

---

### 3.3 Carga desde archivo  
También es posible cargar un archivo de texto con números separados por comas.  

1. Presionar **Subir Archivo**  
2. Seleccionar el archivo  
3. Los datos se cargarán automáticamente  

![Carga de archivo](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231609.png)

---

## 4. Configuración del sistema  

Antes de iniciar el proceso, el usuario debe seleccionar:

- Algoritmo: Bubble Sort, Shell Sort o Quick Sort  
- Orden: Ascendente o Descendente  
- Velocidad: Lento, Medio o Rápido  

![Opciones](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231641.png)

---

## 5. Ejecución  

Al presionar el botón **Iniciar**, el sistema comienza el proceso de ordenamiento.

Durante la ejecución:

- Se actualiza la gráfica en tiempo real  
- Se muestran los pasos en el log  
- Se resaltan los elementos con colores  

Colores:

- Amarillo → Comparación  
- Rojo → Intercambio  
- Verde → Elemento ordenado  

![Ejecución](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231807.png)

---

## 6. Estadísticas  

El sistema muestra en tiempo real:

- Comparaciones  
- Intercambios  
- Iteraciones  

Esto permite analizar el rendimiento del algoritmo seleccionado.

![Estadísticas](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231822.png)

---

## 7. Log de operaciones  

Aquí se registran todas las acciones realizadas durante el proceso, permitiendo ver paso a paso cómo se ejecuta el algoritmo.

![Log](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231831.png)

---

## 8. Reporte  

Al finalizar, el sistema genera automáticamente un archivo `reporte.html` con la información completa de la ejecución.

Incluye:

- Algoritmo utilizado  
- Tipo de orden  
- Datos originales y ordenados  
- Estadísticas  
- Historial  

El archivo se abre automáticamente en el navegador.

![Reporte](./Imagenes2/Captura%20de%20pantalla%202026-03-29%20231842.png)

---

## 9. Ejemplo de uso  

1. Ingresar datos:  
10, 4, 7, 2, 9  

2. Seleccionar:  
- Algoritmo: Bubble Sort  
- Orden: Ascendente  
- Velocidad: Medio  

3. Presionar **Iniciar**  

4. Observar el proceso en la gráfica y el log  

---

## 10. Recomendaciones  

- Ingresar únicamente números válidos  
- No usar más de 30 datos  
- Usar velocidad lenta para observar mejor el proceso  