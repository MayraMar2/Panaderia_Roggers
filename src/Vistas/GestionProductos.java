/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;

import GestorOperaciones.CQManagerProductos;
import Modelos.CModelosProductos;
import Utilitarios.CUtilitarios;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yoong
 */
public class GestionProductos extends javax.swing.JFrame {

    //permitir llamar las cconsultas de los modelos
    private CQManagerProductos mngr = new CQManagerProductos();
    CModelosProductos modelos = new CModelosProductos();
    ArrayList<String[]> resultados = new ArrayList<>();
    ArrayList<String[]> tipo = new ArrayList<>();
    int numero;

    //***************************METODOS PROPIOS****************************
    private void combo_box() {
        try {
            jCBTipos.removeAllItems();
            tipo = modelos.carga_tipo_producto();
            for (String[] opciones : tipo) {
                int id = Integer.parseInt(opciones[0]);
                String tipo = opciones[1];
                String cadena = id + "-" + tipo;
                jCBTipos.addItem(cadena);
            }
        } catch (Exception e) {
        }
    }

    private void limpiar_campos() {
        jTFNombre.setText("");
        jTFPrecio.setText("");
        jTFCantidad.setText("");
        jTFElaboracion.setText("");
        jTFCaducidad.setText("");
    }

    private boolean campos_vacios() {
        return (jTFNombre.getText().isEmpty()
                || jTFPrecio.getText().isEmpty()
                || jTFCantidad.getText().isEmpty()
                || jTFElaboracion.getText().isEmpty()
                || jTFCaducidad.getText().isEmpty());

    }

    private void limpiar_tabla() {
        DefaultTableModel modelTabla
                = (DefaultTableModel) jTProducto.getModel();
        for (int i = (modelTabla.getRowCount() - 1); i >= 0; i--) {
            modelTabla.removeRow(i);
        }
    }

    private void lee_datos() {
        //numero = 1;
        //2. obtener el modelo de la tabla de datos 
        DefaultTableModel modelTabla
                = (DefaultTableModel) jTProducto.getModel();
        try {
            //3.leer los datos
            resultados = modelos.busca_objetos_model();
            //4. limpiar tabla
            limpiar_tabla();
            //5.asignar datos a la tabla
            for (String[] resultado : resultados) {
                //añadirle datos al modelo de la tabla
                modelTabla.addRow(new Object[]{
                    resultado[0],
                    resultado[1],
                    resultado[2],
                    resultado[3],
                    resultado[4],
                    resultado[5],
                    resultado[6]
                });

            }
        } catch (SQLException e) {
        }

    }

   /*

    private void inserta_datos_gestion() {
        int id1;
        DefaultTableModel modelTabla = (DefaultTableModel) jTProducto.getModel();

        if (campos_vacios()) {
            CUtilitarios.msg_adver("Hay campos vacíos", "Inserta datos");
        } else {
            // 1. Obtener los datos del cuadro de texto
            id1 = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));
            int cantidad = Integer.parseInt(jTFCantidad.getText());
            String elaboracion = jTFElaboracion.getText();
            String caducidad = jTFCaducidad.getText();

//            // 2. Obtener el tipo seleccionado del ComboBox y separar el primer campo
//            int tipo; // Declarar la variable tip0
            // 3. Insertar datos usando el tipo obtenido
            try {
                if (modelos.inserta_gestion(id1, cantidad, elaboracion, caducidad)) {
                    // Mensaje de inserción correcta si deseas
                    CUtilitarios.msg("Inserción correcta", "Inserta dato");
                } else {
                    CUtilitarios.msg_adver("Problemas al insertar", "Insertar datos");
                }
                limpiar_campos();
                lee_datos();
            } catch (Exception e) {
                // Manejo de excepciones si es necesario
            }
        }
    }
    
    */

    /////////////////////////////////////7
        
        
        private void inserta_datos_producto_y_gestion() {
        if (campos_vacios()) {
            CUtilitarios.msg_adver("Hay campos vacíos", "Inserta datos");
            return;
        }

        try {
            String nombre = jTFNombre.getText();
            float precio = Float.parseFloat(jTFPrecio.getText());
            int tipo = jCBTipos.getSelectedIndex()+1;
            int cantidad = Integer.parseInt(jTFCantidad.getText());
            String elaboracion = jTFElaboracion.getText();
            String caducidad = jTFCaducidad.getText();

            boolean exito = modelos.inserta_producto_y_gestion(nombre, precio, tipo, cantidad, elaboracion, caducidad);

            if (exito) {
                CUtilitarios.msg("Producto y gestión insertados correctamente", "Éxito");
                limpiar_campos();
                lee_datos();
            } else {
                CUtilitarios.msg_adver("Error al insertar datos", "Error");
            }

        } catch (Exception e) {
            CUtilitarios.msg_error("Error inesperado: " + e.getMessage(), "Error");
        }
    }

    ////////////////////////////////// ACTUALIZA
       
      private void actualiza_datos_Producto() {
    int id;
    DefaultTableModel modelTabla = (DefaultTableModel) jTProducto.getModel();

    if (campos_vacios()) {
        CUtilitarios.msg_adver("Campos vacíos", "Actualizar datos");
        return;
    }

    try {
        // Obtener el ID desde la tabla
        id = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));

        // Obtener valores de los campos
        String nombre = jTFNombre.getText();
        float precio = Float.parseFloat(jTFPrecio.getText());
        int tipo = jCBTipos.getSelectedIndex() + 1;
        int cantidad = Integer.parseInt(jTFCantidad.getText());
        String elaboracion = jTFElaboracion.getText();
        String caducidad = jTFCaducidad.getText();

        // Llamar al modelo
        boolean actualizado = modelos.actualiza_producto_y_gestion(id, nombre, precio, tipo, cantidad, elaboracion, caducidad);

        if (actualizado) {
            CUtilitarios.msg("Datos actualizados correctamente", "Éxito");
            lee_datos();
        } else {
            CUtilitarios.msg_adver("Error al actualizar datos", "Error");
        }

    } catch (Exception e) {
        CUtilitarios.msg_error("Error:\n" + e.getMessage(), "Error al actualizar");
    }

    limpiar_campos();
}



    ////////////////////////
        
        
        
        
        
        
        
        
    
    

    private int lee_fila_seleccionada() {
        int id = -1;
        DefaultTableModel modelTabla
                = (DefaultTableModel) jTProducto.getModel();
        if (modelTabla.getRowCount() != 0) {//tabla con filas
            if (jTProducto.getSelectedRow() != -1) {
                id = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));
                jTFNombre.setText((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 1));
                jTFPrecio.setText((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 3));
                jTFCantidad.setText((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 4));
                jTFElaboracion.setText((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 5));
                jTFCaducidad.setText((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 6));
                //  jTFTipo.setText((String) modelTabla.getValueAt(jTpasajero.getSelectedRow(), 5));

            }

        }
        return id;
    }

    /*
    private void actualiza_datos_Producto23() {
        int id;
        DefaultTableModel modelTabla = (DefaultTableModel) jTProducto.getModel();

        if (campos_vacios()) {
            CUtilitarios.msg_adver("Campos vacíos", "Actualizar datos");
        } else {
            try {
                // Obtener el ID desde la tabla
                id = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));

                // Obtener el nombre, AP, AM, correo desde los campos de texto
                String nombre = jTFNombre.getText();
                float precio = Integer.parseInt(jTFPrecio.getText());

                // Actualizar el modelo con los nuevos datos
                modelos.actualiza_producto(
                        id, nombre, jCBTipos.getSelectedIndex() + 1, precio);

                // Volver a cargar los datos en la tabla
                lee_datos();
            } catch (Exception e) {
                // Manejo de excepciones si es necesario
            }
        }
        limpiar_campos();
    }*/

   /* private void elimina_dato() {
        int idEliminar;
        //obtener el modelo de la tabla
        DefaultTableModel modelTabla
                = (DefaultTableModel) jTProducto.getModel();
        //si la cantidaxd de filas es direfente de 0
        if (modelTabla.getRowCount() != 0) {//TABLA CON FILAS
            if (jTProducto.getSelectedRow() != -1) {
                idEliminar = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));
                System.out.println(" " + idEliminar);//prueba
                try {
                    if (modelos.elimina_producto(idEliminar)) {
                        CUtilitarios.msg("Eliminacion correcta", "Elimina datos");
                    }
                    lee_datos();
                } catch (Exception e) {
                }
            } else {
            }
        } else {//SI LA TABLA NO TIENE FILAS
            CUtilitarios.msg_adver("TABLA VACIA", "ELIMINA DATO");
        }
        limpiar_campos();
    }*/
    
    
    
    
    private void elimina_dato() {
    int idEliminar;
    DefaultTableModel modelTabla = (DefaultTableModel) jTProducto.getModel();

    if (modelTabla.getRowCount() != 0) {
        if (jTProducto.getSelectedRow() != -1) {
            idEliminar = Integer.parseInt((String) modelTabla.getValueAt(jTProducto.getSelectedRow(), 0));
            System.out.println("ID a eliminar: " + idEliminar); // Debug

            try {
                if (modelos.elimina_producto(idEliminar)) {
                    CUtilitarios.msg("Eliminación correcta", "Eliminar datos");
                    lee_datos();
                } else {
                    CUtilitarios.msg_adver("No se pudo eliminar el producto", "Eliminar");
                }
            } catch (Exception e) {
                CUtilitarios.msg_error("Error al eliminar: " + e.getMessage(), "Eliminar");
            }
        } else {
            CUtilitarios.msg_adver("Seleccione una fila para eliminar", "Eliminar");
        }
    } else {
        CUtilitarios.msg_adver("Tabla vacía", "Eliminar dato");
    }

    limpiar_campos();
}

    
    

    public GestionProductos() {
        initComponents();
        CComboBox objCombo = new CComboBox();
        objCombo.mostrarTipo(jCBTipos);
        jTFIDTipo.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTFNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTFPrecio = new javax.swing.JTextField();
        jTFCantidad = new javax.swing.JTextField();
        jTFElaboracion = new javax.swing.JTextField();
        jTFCaducidad = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTProducto = new javax.swing.JTable();
        jCBTipos = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTFIDTipo = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jTree1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(126, 69, 17));
        jLabel1.setText("Menú Principal");

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setText("Cantidad");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(126, 82, 38));

        jPanel1.setBackground(new java.awt.Color(238, 224, 175));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setText("Nombre");

        jTFNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNombreActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setText("Tipo");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setText("Precio");

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setText("Cantidad");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setText("Caducidad");

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel9.setText("Fecha de elaboración");

        jTFCaducidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCaducidadActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 51, 0));
        jButton1.setText("Agregar producto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 51, 0));
        jButton3.setText("Eliminar Producto");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(153, 51, 0));
        jButton4.setText("Modificar producto");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Producto", "Nombre", "Tipo", "Precio", "Cantidad", "Fecha de elaboracion", "Cantidad"
            }
        ));
        jTProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTProductoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTProducto);

        jCBTipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTiposActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Rockwell", 0, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(153, 51, 0));
        jButton5.setText("Consultar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(102, 102, 255));
        jButton7.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Limpiar tabla");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFElaboracion, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(363, 363, 363)
                                        .addComponent(jButton3))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jTFIDTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1)))
                                .addGap(7, 7, 7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton7))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCBTipos, jTFCaducidad, jTFCantidad, jTFElaboracion, jTFNombre, jTFPrecio});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jCBTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTFElaboracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTFCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTFIDTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addGap(256, 256, 256))
        );

        jButton6.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 51, 0));
        jButton6.setText("Regresar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestion de productos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombreActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 elimina_dato();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        actualiza_datos_Producto();    // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        MenuPrincipal gProduc = new MenuPrincipal();
        gProduc.setVisible(true);
        gProduc.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTFCaducidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCaducidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCaducidadActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        lee_datos();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        inserta_datos_producto_y_gestion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        limpiar_tabla();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTProductoMouseClicked
 lee_fila_seleccionada();        // TODO add your handling code here:
    }//GEN-LAST:event_jTProductoMouseClicked

    private void jCBTiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTiposActionPerformed
        CComboBox objCombo = new CComboBox();
        objCombo.mostrarIDTipo(jCBTipos, jTFIDTipo);
    }//GEN-LAST:event_jCBTiposActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jCBTipos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTFCaducidad;
    private javax.swing.JTextField jTFCantidad;
    private javax.swing.JTextField jTFElaboracion;
    private javax.swing.JTextField jTFIDTipo;
    private javax.swing.JTextField jTFNombre;
    private javax.swing.JTextField jTFPrecio;
    private javax.swing.JTable jTProducto;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
