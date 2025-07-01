package Vistas;

import Conector.CConector;
import javax.swing.JComboBox;
import Modelos.CModelos;
import Utilitarios.CUtilitarios;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.Connection;

public class puntoVenta extends javax.swing.JFrame {

    public puntoVenta() {
        initComponents();
        CComboBox objCombo = new CComboBox();
        objCombo.mostrarClientes(jCBClientes);
        objCombo.mostrarProductos(jCBProductos);
        jTFIDClientes.setVisible(false);
        jTFIDProductos.setVisible(false);
        jLSubtotal.setText("0");
        jLTotal.setText("0");
    }
    
    CModelos modelos = new CModelos();
    
    private void limpiar_campos_venta() {
        jTFCantidad.setText("");
        jTFDescuento.setText("");
        jLSubtotal.setText("");
        jLTotal.setText("");
        DefaultTableModel modelTabla = (DefaultTableModel) jTVenta.getModel();
        for (int i = (modelTabla.getRowCount() - 1); i >= 0; i--) {
            modelTabla.removeRow(i);
        }
    }

    private boolean campos_vacios() {
        return jTFCantidad.getText().isEmpty() || jTFDescuento.getText().isEmpty();
    }

    private void recalcularTotales() {
        DefaultTableModel model = (DefaultTableModel) jTVenta.getModel();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object valorSubtotal = model.getValueAt(i, 4);
            if (valorSubtotal != null) {
                try {
                    subtotal = subtotal.add(new BigDecimal(valorSubtotal.toString()));
                } catch (NumberFormatException ex) {
                    CUtilitarios.msg_error("Error en subtotal en fila " + i, "Formato inválido");
                }
            }
        }
        jLSubtotal.setText(subtotal.toString());
        BigDecimal descuento = BigDecimal.ZERO;
        try {
            if (!jTFDescuento.getText().isEmpty()) {
                descuento = new BigDecimal(jTFDescuento.getText());
            }
        } catch (NumberFormatException ex) {
            CUtilitarios.msg_error("Descuento inválido. Usa solo números", "Calcular totales");
        }

        BigDecimal montoDescuento = subtotal.multiply(descuento).divide(new BigDecimal("100"));
        BigDecimal total = subtotal.subtract(montoDescuento);
        jLTotal.setText(total.toString());
    }

    private void agregar_productos_venta() {
        try {
            int idProducto = Integer.parseInt(jTFIDProductos.getText());
            String nombre = (String) jCBProductos.getSelectedItem();
            int cantidad = Integer.parseInt(jTFCantidad.getText());

            if (cantidad <= 0) {
                CUtilitarios.msg_adver("La cantidad debe ser mayor a cero.", "Agregar Productos Venta");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) jTVenta.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                Object valor = model.getValueAt(i, 0);
                if (valor != null) {
                    int idEnTabla = Integer.parseInt(valor.toString());
                    if (idEnTabla == idProducto) {
                        CUtilitarios.msg_adver("El producto ya está agregado a la lista.", "Agregar Productos Venta");
                        return;
                    }
                }
            }
            BigDecimal precioUnitario = BigDecimal.ZERO;
            Connection conn = new CConector().conectar();
            String sql = "SELECT precio FROM producto WHERE idProducto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                precioUnitario = rs.getBigDecimal("precio");
            }
            rs.close();
            ps.close();
            conn.close();
            BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
            model.addRow(new Object[]{idProducto, nombre, cantidad, precioUnitario, subtotal});
            recalcularTotales();
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al agregar producto: " + e.getMessage(), "Agregar Productos Venta");
        }
    }

    private void inserta_venta() {
        if (jTVenta.getRowCount() == 0 || jLTotal.getText().isEmpty()) {
            CUtilitarios.msg_adver("No hay productos agregados o total vacío.", "Generar venta");
            return;
        }

        try {
            int idCliente = 0;
            if (!jTFIDClientes.getText().isEmpty()) {
                idCliente = Integer.parseInt(jTFIDClientes.getText());
            }

            int cantidadTotal = 0;
            DefaultTableModel model = (DefaultTableModel) jTVenta.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                cantidadTotal += Integer.parseInt(model.getValueAt(i, 2).toString());
            }

            BigDecimal descuento = jTFDescuento.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(jTFDescuento.getText());
            BigDecimal subtotal = new BigDecimal(jLSubtotal.getText());
            BigDecimal total = new BigDecimal(jLTotal.getText());

            String[] opciones = {"Sí", "No"};
            int respuesta = JOptionPane.showOptionDialog(
                    null,
                    "¿Seguro que deseas registrar esta venta?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (respuesta == 0) {
                Connection conn = new CConector().conectar();
                conn.setAutoCommit(false); // Transacción

                // Insertar en Venta
                String sqlVenta = "INSERT INTO venta (idCliente, cantidad, descuento, subtotal, total) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
                psVenta.setInt(1, idCliente);
                psVenta.setInt(2, cantidadTotal);
                psVenta.setBigDecimal(3, descuento);
                psVenta.setBigDecimal(4, subtotal);
                psVenta.setBigDecimal(5, total);
                psVenta.executeUpdate();

                ResultSet rs = psVenta.getGeneratedKeys();
                int idVentaGenerada = -1;
                if (rs.next()) {
                    idVentaGenerada = rs.getInt(1);
                }
                rs.close();
                psVenta.close();

                if (idVentaGenerada == -1) {
                    conn.rollback();
                    CUtilitarios.msg_error("Error al obtener ID de venta", "Error");
                    return;
                }

                // Insertar productos (DetalleVenta)
                String sqlDetalle = "INSERT INTO detalleventa (idVenta, idProducto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);

                for (int i = 0; i < model.getRowCount(); i++) {
                    int idProducto = Integer.parseInt(model.getValueAt(i, 0).toString());
                    int cantidad = Integer.parseInt(model.getValueAt(i, 2).toString());
                    BigDecimal precio = new BigDecimal(model.getValueAt(i, 3).toString());
                    BigDecimal subtotalDetalle = new BigDecimal(model.getValueAt(i, 4).toString());

                    psDetalle.setInt(1, idVentaGenerada);
                    psDetalle.setInt(2, idProducto);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setBigDecimal(4, precio);
                    psDetalle.setBigDecimal(5, subtotalDetalle);
                    psDetalle.executeUpdate();
                }

                psDetalle.close();
                conn.commit();
                conn.close();

                CUtilitarios.msg("Venta registrada correctamente", "Venta");

                limpiar_campos_venta(); // <- debes implementar este método
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al registrar venta:\n" + e.getMessage(), "Venta");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jCBClientes = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTFIDClientes = new javax.swing.JTextField();
        jCBProductos = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jTFIDProductos = new javax.swing.JTextField();
        jBAgregarProducto = new javax.swing.JButton();
        jBQuitarProducto = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTFDescuento = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFCantidad = new javax.swing.JTextField();
        jLTotal = new javax.swing.JLabel();
        jLSubtotal = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTVenta = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jBGenerarVenta = new javax.swing.JButton();
        jBVerVentas = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setText("Nombre");

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
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(126, 82, 38));

        jPanel2.setBackground(new java.awt.Color(238, 224, 175));

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));

        jCBClientes.setForeground(new java.awt.Color(153, 51, 0));
        jCBClientes.setMaximumRowCount(50);
        jCBClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBClientesActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel8.setText("Clientes frecuentes:");

        jTFIDClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFIDClientesActionPerformed(evt);
            }
        });

        jCBProductos.setForeground(new java.awt.Color(153, 51, 0));
        jCBProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBProductosActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel9.setText("Productos:");

        jBAgregarProducto.setForeground(new java.awt.Color(153, 51, 0));
        jBAgregarProducto.setText("Agregar Producto");
        jBAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAgregarProductoActionPerformed(evt);
            }
        });

        jBQuitarProducto.setForeground(new java.awt.Color(153, 51, 0));
        jBQuitarProducto.setText("Quitar Producto");

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel5.setText("Descuento:");

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel6.setText("Subtotal:");

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel7.setText("Total:");

        jTFDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFDescuentoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel10.setText("Cantidad:");

        jTFCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCantidadActionPerformed(evt);
            }
        });

        jLTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jTVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Nombre", "Cantidad", "Precio", "Subotal"
            }
        ));
        jScrollPane4.setViewportView(jTVenta);

        jButton3.setFont(new java.awt.Font("Roboto Black", 0, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 51, 0));
        jButton3.setText("Regresar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jBGenerarVenta.setForeground(new java.awt.Color(153, 51, 0));
        jBGenerarVenta.setText("Realizar Venta");
        jBGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGenerarVentaActionPerformed(evt);
            }
        });

        jBVerVentas.setForeground(new java.awt.Color(153, 51, 0));
        jBVerVentas.setText("Ver las ventas realizadas");
        jBVerVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVerVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFIDProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jCBProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFIDClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCBClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTFCantidad)
                                    .addComponent(jTFDescuento, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLSubtotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jBAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jBQuitarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4)
                            .addComponent(jBGenerarVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBVerVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(32, 32, 32))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTFIDClientes, jTFIDProductos});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jCBClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jTFIDClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jCBProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jTFIDProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBAgregarProducto)
                            .addComponent(jBQuitarProducto))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTFCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTFDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLSubtotal)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jBGenerarVenta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLTotal)
                    .addComponent(jBVerVentas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(36, 36, 36))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTFIDClientes, jTFIDProductos});

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Punto de venta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(375, 375, 375))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(306, 306, 306))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFIDClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFIDClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFIDClientesActionPerformed

    private void jCBClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBClientesActionPerformed
        CComboBox objCombo = new CComboBox();
        objCombo.mostrarIDClientes(jCBClientes, jTFIDClientes);
    }//GEN-LAST:event_jCBClientesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        MenuPrincipal gProduc = new MenuPrincipal();
        gProduc.setVisible(true);
        gProduc.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTFDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFDescuentoActionPerformed

    private void jCBProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBProductosActionPerformed
        CComboBox objCombo = new CComboBox();
        objCombo.mostrarIDProductos(jCBProductos, jTFIDProductos);
    }//GEN-LAST:event_jCBProductosActionPerformed

    private void jTFCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCantidadActionPerformed

    private void jBAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAgregarProductoActionPerformed
        agregar_productos_venta();
    }//GEN-LAST:event_jBAgregarProductoActionPerformed

    private void jBGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGenerarVentaActionPerformed
        inserta_venta();
    }//GEN-LAST:event_jBGenerarVentaActionPerformed

    private void jBVerVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVerVentasActionPerformed
        Document doc = new Document();
        try {
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(doc, new FileOutputStream(ruta + "/Desktop/Ventas_Generadas.pdf"));
            doc.open();
            PdfPTable tabla = new PdfPTable(7);
            tabla.addCell("Clave de venta");
            tabla.addCell("Nombre del cliente");
            tabla.addCell("Nombre del producto");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio");
            tabla.addCell("Subtotal");
            tabla.addCell("Total");
            try {
                // Recuperar datos de las ventas
                ArrayList<String[]> transferencias = modelos.busca_venta();
                if (transferencias.isEmpty()) {
                    CUtilitarios.msg("No se encontraron ventas", "Generación de PDF");
                } else {
                    for (String[] fila : transferencias) {
                        tabla.addCell(fila[0]); 
                        tabla.addCell(fila[1]); 
                        tabla.addCell(fila[2]); 
                        tabla.addCell(fila[3]); 
                        tabla.addCell(fila[4]); 
                        tabla.addCell(fila[5]); 
                        tabla.addCell(fila[6]); 
                    }
                    doc.add(tabla);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al obtener datos de transferencia: " + e.getMessage());
            }

            doc.close();
            JOptionPane.showMessageDialog(null, "Recibo generado");
        } catch (DocumentException | HeadlessException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
        }
    }//GEN-LAST:event_jBVerVentasActionPerformed

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
            java.util.logging.Logger.getLogger(puntoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(puntoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(puntoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(puntoVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new puntoVenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAgregarProducto;
    private javax.swing.JButton jBGenerarVenta;
    private javax.swing.JButton jBQuitarProducto;
    private javax.swing.JButton jBVerVentas;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jCBClientes;
    private javax.swing.JComboBox<String> jCBProductos;
    private javax.swing.JLabel jLSubtotal;
    private javax.swing.JLabel jLTotal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTFCantidad;
    private javax.swing.JTextField jTFDescuento;
    private javax.swing.JTextField jTFIDClientes;
    private javax.swing.JTextField jTFIDProductos;
    private javax.swing.JTable jTVenta;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
