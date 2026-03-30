/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaz;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.BorderLayout;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class VisPanelControl extends javax.swing.JFrame {
    
    
    private org.jfree.data.category.DefaultCategoryDataset dataset;
    private MyBarRenderer personalizadoRenderer;
    
    private int[] datos;
    
    private String[][] historial = new String[100][8];
    private int contadorEjecuciones = 0;
    private int[] copiaOriginal;
    
    private int comparaciones = 0;
    private int intercambios = 0;
    private int iteraciones = 0;
    private long tiempoInicio;

    public VisPanelControl() {
        initComponents();

        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);

        inicializarGraficaCustom();
    
        //setPreferredSize(new java.awt.Dimension(1200, 800));
        //revalidate();

    }

    
    private void inicializarGraficaCustom() {
        
        dataset = new org.jfree.data.category.DefaultCategoryDataset();

        org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
            null, null, null, dataset, 
            org.jfree.chart.plot.PlotOrientation.VERTICAL, 
            false, false, false
        );

        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        personalizadoRenderer = new MyBarRenderer();
        plot.setRenderer(personalizadoRenderer);
        plot.setBackgroundPaint(java.awt.Color.WHITE);
        chart.setBackgroundPaint(java.awt.Color.WHITE);

        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(chart);
    
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 450));
        chartPanel.setMouseWheelEnabled(true);

        PanelGrafica.setLayout(new java.awt.BorderLayout());
        PanelGrafica.removeAll();
        PanelGrafica.add(chartPanel, java.awt.BorderLayout.CENTER);
    
        PanelGrafica.revalidate();
        PanelGrafica.repaint();
    }
    
    
    private class MyBarRenderer extends org.jfree.chart.renderer.category.BarRenderer {

        private int colA = -1, colB = -1;
        private Color colorActual = new Color(0, 163, 224);

        private Color[] colores; 

        public void setCantidad(int n) {
            colores = new Color[n];
            for (int i = 0; i < n; i++) {
                colores[i] = new Color(173, 216, 230); // azul normal
            }
        }

        public void pintarBarras(int a, int b, Color color) {
            this.colA = a;
            this.colB = b;
            this.colorActual = color;

            if (colores != null) {
                
                for (int i = 0; i < colores.length; i++) {
                    if (colores[i] != Color.GREEN) {
                    colores[i] = new Color(173, 216, 230);
                    }
                }

            if (a >= 0 && a < colores.length) colores[a] = color;
            if (b >= 0 && b < colores.length) colores[b] = color;
            }
        }

        public void pintarOrdenado(int i) {
            if (colores != null && i >= 0 && i < colores.length) {
                colores[i] = Color.GREEN;
            }
        }

        @Override
        public java.awt.Paint getItemPaint(int row, int column) {
            if (colores != null && column < colores.length) {
                return colores[column];
            }

            if (column == colA || column == colB) {
                return colorActual;
            }

            return new Color(173, 216, 230);
        }
    }
    
    
    private void esperar() {
        try {
            int seleccion = JComVelocidad.getSelectedIndex();
            int milisegundos;
    
            switch (seleccion) {
                case 0: milisegundos = 500; break; 
                case 1: milisegundos = 100; break; 
                case 2: milisegundos = 20;  break;
                default: milisegundos = 100;
            }

            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    
    private void actualizarGrafica() {

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (datos == null) return;
            for (int i = 0; i < datos.length; i++) {
                
                dataset.setValue(datos[i], "Valores", "Dato " + i);
            }
        });
    }

    
    
    private void ejecutarQuickSort(int low, int high, String orden) {
        
        iteraciones++;
        lblIteraciones.setText("" + iteraciones);

        if (low < high) {
            int pi = particion(low, high, orden);

            ejecutarQuickSort(low, pi - 1, orden);
            ejecutarQuickSort(pi + 1, high, orden);
        }

    
        if (low == 0 && high == datos.length - 1) {
            for (int i = 0; i < datos.length; i++) {
                personalizadoRenderer.pintarOrdenado(i);
            }
        actualizarGrafica();
        }
    
    }
    

    private void ejecutarShellSort(String orden) {
       
        int n = datos.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            
            iteraciones++;
            lblIteraciones.setText("" + iteraciones);
        
            for (int i = gap; i < n; i++) {
                int temp = datos[i];
                int j;
                
                for (j = i; j >= gap; j -= gap) {
                
                    personalizadoRenderer.pintarBarras(j, j - gap, Color.RED);
                    registrarOperacion("COMP", datos[j - gap], temp);
                    actualizarGrafica(); 
                    esperar();

                    boolean condicion = orden.equals("Ascendente") ? (datos[j - gap] > temp) : (datos[j - gap] < temp);
                
                    if (condicion) {
                        registrarOperacion("SWAP", datos[j], datos[j - gap]);
                        datos[j] = datos[j - gap];
                        actualizarGrafica();
                        esperar();
                    } else {
                        break;
                    }
                }
                
                datos[j] = temp;
                actualizarGrafica(); 
                esperar();
            }
        }
    
    
        for (int i = 0; i < datos.length; i++) {
        personalizadoRenderer.pintarOrdenado(i);
    }
    actualizarGrafica();
    
    }
    
    
    private void ejecutarBubbleSort(String orden) {
        for (int i = 0; i < datos.length - 1; i++) {
            iteraciones++;
            lblIteraciones.setText("" + iteraciones);
            nuevaIteracion(iteraciones);
        

            for (int j = 0; j < datos.length - 1 - i; j++) {

          
                personalizadoRenderer.pintarBarras(j, j + 1, Color.YELLOW);
                registrarOperacion("COMP", datos[j], datos[j+1]);
                actualizarGrafica(); 
                esperar();

            
                personalizadoRenderer.pintarBarras(-1, -1, new Color(173,216,230));
                actualizarGrafica();

                boolean debeCambiar = orden.equals("Ascendente") ? (datos[j] > datos[j+1]) : (datos[j] < datos[j+1]);

                if (debeCambiar) {
           
                    registrarOperacion("SWAP", datos[j], datos[j+1]);
                    personalizadoRenderer.pintarBarras(j, j + 1, Color.RED);
                    actualizarGrafica();
                    esperar();

                    int aux = datos[j];
                    datos[j] = datos[j + 1];
                    datos[j + 1] = aux;
    
               
                    personalizadoRenderer.pintarBarras(-1, -1, new Color(173,216,230));
                    actualizarGrafica();
                }
            }

      
            personalizadoRenderer.pintarOrdenado(datos.length - 1 - i);
            actualizarGrafica();
            }

            for (int i = 0; i < datos.length; i++) {
            personalizadoRenderer.pintarOrdenado(i);
        }
        actualizarGrafica();
    }
    
    
    private int particion(int low, int high, String orden) {
        int pivot = datos[high];
        int i = (low - 1);
    
        for (int j = low; j < high; j++) {
        
            personalizadoRenderer.pintarBarras(j, high, Color.YELLOW);
            registrarOperacion("COMP", datos[j], pivot);
            actualizarGrafica();
            esperar();

            boolean condicion = orden.equals("Ascendente") ? (datos[j] < pivot) : (datos[j] > pivot);
            
            if (condicion) {
                i++;
           
                int temp = datos[i];
                datos[i] = datos[j];
                datos[j] = temp;
                personalizadoRenderer.pintarBarras(i, j, Color.RED);
                registrarOperacion("SWAP", datos[i], datos[j]);
                actualizarGrafica();
                esperar();
            }
        }
  
        int temp = datos[i + 1];
        datos[i + 1] = datos[high];
        datos[high] = temp;
        personalizadoRenderer.pintarBarras(i + 1, high, Color.RED);
        actualizarGrafica();
        return i + 1;
    }
    

    private void resetearEstadisticas() {
        comparaciones = 0;
        intercambios = 0;
        iteraciones = 0;
        lblComparaciones.setText("0");
        lblIntercambios.setText("0");
        lblIteraciones.setText("0");
        jTextArea1.setText(""); 
    }
    
    private void escribirLog(String mensaje) {
        jTextArea1.append(mensaje + "\n");
        
        // Auto-scroll
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }

   
    private void registrarOperacion(String tipo, int v1, int v2) {
        javax.swing.SwingUtilities.invokeLater(() -> {

        String mensaje;

        if (tipo.equals("COMP")) {
            mensaje = "Comparando " + v1 + " y " + v2;
            comparaciones++;
            lblComparaciones.setText(String.valueOf(comparaciones));
        } else {
            mensaje = "Intercambiando " + v1 + " y " + v2;
            intercambios++;
            lblIntercambios.setText(String.valueOf(intercambios));
        }

        jTextArea1.append(mensaje + "\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        });
    }
    
    
    private void nuevaIteracion(int num) {
        escribirLog("\n--- Iteración " + num + " ---");
    }
    

    private void generarReporteHTML(String algoritmo, String orden, long tiempo) {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter("reporte.html", "UTF-8");

            writer.println("<html>");
            writer.println("<head><title>Reporte de Ordenamiento</title></head>");
            writer.println("<body style='font-family: Arial;'>");

            writer.println("<h1>Resumen de Ejecución</h1>");
            writer.println("<p><b>Algoritmo:</b> " + algoritmo + "</p>");
            writer.println("<p><b>Orden:</b> " + orden + "</p>");

        
            writer.println("<p><b>Arreglo Original:</b> " + java.util.Arrays.toString(copiaOriginal) + "</p>");

        
            writer.println("<p><b>Arreglo Ordenado:</b> " + java.util.Arrays.toString(datos) + "</p>");

            writer.println("<p><b>Comparaciones:</b> " + comparaciones + "</p>");
            writer.println("<p><b>Intercambios:</b> " + intercambios + "</p>");
            writer.println("<p><b>Iteraciones:</b> " + iteraciones + "</p>");
            writer.println("<p><b>Tiempo:</b> " + tiempo + " ms</p>");

       
            String velocidad = JComVelocidad.getSelectedItem().toString();
            writer.println("<p><b>Velocidad:</b> " + velocidad + "</p>");

       
            writer.println("<h2>Historial de Ejecuciones</h2>");
            writer.println("<table border='1' cellpadding='5'>");

            writer.println("<tr>");
            writer.println("<th>#</th><th>Algoritmo</th><th>Orden</th><th>n</th><th>Comp</th><th>Inter</th><th>Iter</th><th>Tiempo</th>");
            writer.println("</tr>");

            for (int i = 0; i < contadorEjecuciones; i++) {
                writer.println("<tr>");
                for (int j = 0; j < 8; j++) {
                    writer.println("<td>" + historial[i][j] + "</td>");
                }
                writer.println("</tr>");
            }

            writer.println("</table>");
            writer.println("</body></html>");

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
       }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtPanelControl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtIngresarDatos = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnCargar = new javax.swing.JButton();
        btnAleatorio = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        JComAlgoritmo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        JComOrden = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        JComVelocidad = new javax.swing.JComboBox<>();
        txtEstadisticas = new javax.swing.JLabel();
        PanelComparaciones = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblComparaciones = new javax.swing.JLabel();
        PanelIntercambios = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblIntercambios = new javax.swing.JLabel();
        PanelIteraciones = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblIteraciones = new javax.swing.JLabel();
        PanelGrafica = new javax.swing.JPanel();
        txtVisualización = new javax.swing.JLabel();
        txtLogOperaciones = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnSubirA = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtPanelControl.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        txtPanelControl.setText("Panel de Control");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        txtIngresarDatos.addActionListener(this::txtIngresarDatosActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIngresarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtIngresarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Datos de Entrada:");

        btnCargar.setText("Cargar");
        btnCargar.addActionListener(this::btnCargarActionPerformed);

        btnAleatorio.setText("Aleatorio");
        btnAleatorio.addActionListener(this::btnAleatorioActionPerformed);

        jLabel3.setText("Algoritmo:");

        JComAlgoritmo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bubble Sort", "Shell Sort", "Quick Sort" }));
        JComAlgoritmo.addActionListener(this::JComAlgoritmoActionPerformed);

        jLabel4.setText("Orden:");

        JComOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascendente", "Descendente" }));

        jLabel5.setText("Velocidad:");

        JComVelocidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lento", "Medio", "Rápido" }));

        txtEstadisticas.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        txtEstadisticas.setText("Estadísticas");

        PanelComparaciones.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe Script", 1, 8)); // NOI18N
        jLabel1.setText("Comparaciones");

        lblComparaciones.setText("0");

        javax.swing.GroupLayout PanelComparacionesLayout = new javax.swing.GroupLayout(PanelComparaciones);
        PanelComparaciones.setLayout(PanelComparacionesLayout);
        PanelComparacionesLayout.setHorizontalGroup(
            PanelComparacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelComparacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelComparacionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblComparaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        PanelComparacionesLayout.setVerticalGroup(
            PanelComparacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelComparacionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblComparaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        PanelIntercambios.setBackground(new java.awt.Color(153, 153, 255));

        jLabel6.setFont(new java.awt.Font("Segoe Script", 1, 8)); // NOI18N
        jLabel6.setText("Intercambios");

        lblIntercambios.setText("0");

        javax.swing.GroupLayout PanelIntercambiosLayout = new javax.swing.GroupLayout(PanelIntercambios);
        PanelIntercambios.setLayout(PanelIntercambiosLayout);
        PanelIntercambiosLayout.setHorizontalGroup(
            PanelIntercambiosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelIntercambiosLayout.createSequentialGroup()
                .addGroup(PanelIntercambiosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelIntercambiosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(PanelIntercambiosLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblIntercambios, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelIntercambiosLayout.setVerticalGroup(
            PanelIntercambiosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelIntercambiosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblIntercambios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        PanelIteraciones.setBackground(new java.awt.Color(204, 204, 255));

        jLabel8.setFont(new java.awt.Font("Segoe Script", 1, 8)); // NOI18N
        jLabel8.setText("Iteraciones");

        lblIteraciones.setText("0");

        javax.swing.GroupLayout PanelIteracionesLayout = new javax.swing.GroupLayout(PanelIteraciones);
        PanelIteraciones.setLayout(PanelIteracionesLayout);
        PanelIteracionesLayout.setHorizontalGroup(
            PanelIteracionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelIteracionesLayout.createSequentialGroup()
                .addGroup(PanelIteracionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelIteracionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(PanelIteracionesLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelIteracionesLayout.setVerticalGroup(
            PanelIteracionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelIteracionesLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(lblIteraciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        PanelGrafica.setBackground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout PanelGraficaLayout = new javax.swing.GroupLayout(PanelGrafica);
        PanelGrafica.setLayout(PanelGraficaLayout);
        PanelGraficaLayout.setHorizontalGroup(
            PanelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 567, Short.MAX_VALUE)
        );
        PanelGraficaLayout.setVerticalGroup(
            PanelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        txtVisualización.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        txtVisualización.setText("Visualización");

        txtLogOperaciones.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        txtLogOperaciones.setText("Log de Operaciones");

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        btnSubirA.setText("Subir Archivo");
        btnSubirA.addActionListener(this::btnSubirAActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(JComAlgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(JComOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(114, 114, 114)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(JComVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)))
                        .addComponent(txtEstadisticas)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnIniciar)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(PanelComparaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(PanelIntercambios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(PanelIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtPanelControl)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCargar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAleatorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSubirA))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVisualización)
                    .addComponent(txtLogOperaciones)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtVisualización)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PanelGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPanelControl)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCargar)
                            .addComponent(btnAleatorio)
                            .addComponent(btnSubirA))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JComAlgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JComOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JComVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtLogOperaciones)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(26, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtEstadisticas)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PanelComparaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelIntercambios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIniciar)
                        .addGap(43, 43, 43))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JComAlgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JComAlgoritmoActionPerformed
        
    }//GEN-LAST:event_JComAlgoritmoActionPerformed

    private void btnAleatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAleatorioActionPerformed

        try {
        
            String respuesta = javax.swing.JOptionPane.showInputDialog(this, "¿Cuántos números quieres generar? (5-30)");
        
            if (respuesta != null) {
                int n = Integer.parseInt(respuesta);
            
           
                if (n < 5 || n > 30) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Por favor, elige un número entre 5 y 30.");
                    return;
                }

         
                datos = new int[n];

           
                dataset.clear();
                for (int i = 0; i < n; i++) {
                    datos[i] = (int) (Math.random() * 100) + 1;
              
                    dataset.addValue(datos[i], "Valores", "Dato " + i);
                }

         
                personalizadoRenderer.setCantidad(datos.length);
                PanelGrafica.repaint();
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, " Error: Debes ingresar un número entero.");
        }
        
        copiaOriginal = datos.clone();
    }//GEN-LAST:event_btnAleatorioActionPerformed

    private void txtIngresarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIngresarDatosActionPerformed

        
    }//GEN-LAST:event_txtIngresarDatosActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed

        String entrada = txtIngresarDatos.getText();
    
        String[] partes = entrada.split(",");
    
        datos = new int[partes.length];
    
        dataset.clear();
    
    
        if (entrada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese datos primero");
            return;
        }
    
        for (int i = 0; i < partes.length; i++) {
        
            datos[i] = Integer.parseInt(partes[i].trim());
        
            dataset.addValue(datos[i], "Valores", "Dato " + i);
        }
    
        personalizadoRenderer.setCantidad(datos.length); 
        PanelGrafica.repaint();

        copiaOriginal = datos.clone();
    }//GEN-LAST:event_btnCargarActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed

        if (datos == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "¡Primero carga los datos!");
            return;
        }

        resetearEstadisticas();

        tiempoInicio = System.currentTimeMillis();

        Thread hiloProceso = new Thread() {
            
        @Override
        public void run() {

            String algoritmo = JComAlgoritmo.getSelectedItem().toString();
            String orden = JComOrden.getSelectedItem().toString();

            escribirLog("Iniciando " + algoritmo + " (" + orden + ")\n");

            if (algoritmo.equals("Bubble Sort")) {
                ejecutarBubbleSort(orden);
            } else if (algoritmo.equals("Shell Sort")) {
                ejecutarShellSort(orden);
            } else if (algoritmo.equals("Quick Sort")) {
                ejecutarQuickSort(0, datos.length - 1, orden);
            }

            long tiempoFin = System.currentTimeMillis();
            long total = tiempoFin - tiempoInicio;
            
            
            contadorEjecuciones++;

            String[] fila = new String[] {
                String.valueOf(contadorEjecuciones),
                algoritmo,
                orden,
                String.valueOf(datos.length),
                String.valueOf(comparaciones),
                String.valueOf(intercambios),
                String.valueOf(iteraciones),
                String.valueOf(total)
            };

            historial[contadorEjecuciones - 1] = fila;


            generarReporteHTML(algoritmo, orden, total);

            try {
                java.awt.Desktop.getDesktop().open(new java.io.File("reporte.html"));
            } catch (Exception e) {}

                escribirLog("\nTiempo: " + total + " ms");

                escribirLog("Ordenamiento completado!");

                javax.swing.JOptionPane.showMessageDialog(null, "¡Ordenamiento " + algoritmo + " terminado!");
            }
        };

        hiloProceso.start();
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnSubirAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirAActionPerformed

        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            
            File archivo = fileChooser.getSelectedFile();
            
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea = br.readLine();
                if (linea != null) {
                    txtIngresarDatos.setText(linea); 
                    btnCargarActionPerformed(null);
                }
            } catch (IOException e) {
                
                JOptionPane.showMessageDialog(this, "Error al leer el archivo");
            }
            
        }
    }//GEN-LAST:event_btnSubirAActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JComAlgoritmo;
    private javax.swing.JComboBox<String> JComOrden;
    private javax.swing.JComboBox<String> JComVelocidad;
    private javax.swing.JPanel PanelComparaciones;
    private javax.swing.JPanel PanelGrafica;
    private javax.swing.JPanel PanelIntercambios;
    private javax.swing.JPanel PanelIteraciones;
    private javax.swing.JButton btnAleatorio;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnSubirA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblComparaciones;
    private javax.swing.JLabel lblIntercambios;
    private javax.swing.JLabel lblIteraciones;
    private javax.swing.JLabel txtEstadisticas;
    private javax.swing.JTextField txtIngresarDatos;
    private javax.swing.JLabel txtLogOperaciones;
    private javax.swing.JLabel txtPanelControl;
    private javax.swing.JLabel txtVisualización;
    // End of variables declaration//GEN-END:variables
}
