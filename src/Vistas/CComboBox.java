package Vistas;

import Conector.CConector;
import Utilitarios.CUtilitarios;
import javax.swing.JComboBox;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import javax.swing.JTextField;

public class CComboBox {

    CConector objConexion = new CConector();

    public void mostrarClientes(JComboBox combo) {
        String consulta = "";
        consulta = "select concat(nombre, ' ', ap_paterno, ' ', ap_materno) as nombre_completo from cliente";
        Statement st;
        Connection conn;
        try {
            st = objConexion.conectar().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            combo.removeAllItems();
            while (rs.next()) {
                combo.addItem(rs.getString("nombre_completo"));
            }
            combo.addItem("No es cliente frecuente");
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }

    public void mostrarIDClientes(JComboBox combo, JTextField id) {
        String consulta = "select idCliente from cliente where concat(nombre, ' ', ap_paterno, ' ', ap_materno) = ?";
        try {
            CallableStatement cs = objConexion.conectar().prepareCall(consulta);
            cs.setString(1, combo.getSelectedItem().toString());
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                id.setText(rs.getString("idCliente"));
            } else {
                id.setText("");
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }
    
    public void mostrarProductos(JComboBox combo) {
        String consulta = "";
        consulta = "select producto.nombre from producto";
        Statement st;
        Connection conn;
        try {
            st = objConexion.conectar().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            combo.removeAllItems();
            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }

    public void mostrarIDProductos(JComboBox combo, JTextField id) {
        String consulta = "select producto.idProducto from producto where producto.nombre = ?";
        try {
            CallableStatement cs = objConexion.conectar().prepareCall(consulta);
            cs.setString(1, combo.getSelectedItem().toString());
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                id.setText(rs.getString("idProducto"));
            } else {
                id.setText("");
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }
    
    public void mostrarTipo(JComboBox combo) {
        String consulta = "";
        consulta = "select tipoproducto.tipo from tipoproducto";
        Statement st;
        Connection conn;
        try {
            st = objConexion.conectar().createStatement();
            ResultSet rs = st.executeQuery(consulta);
            combo.removeAllItems();
            while (rs.next()) {
                combo.addItem(rs.getString("tipo"));
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }
    
    public void mostrarIDTipo(JComboBox combo, JTextField id) {
        String consulta = "select idTipoProducto from tipoproducto where tipoproducto.tipo = ?";
        try {
            CallableStatement cs = objConexion.conectar().prepareCall(consulta);
            cs.setString(1, combo.getSelectedItem().toString());
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                id.setText(rs.getString("idTipoProducto"));
            } else {
                id.setText("");
            }
        } catch (Exception e) {
            CUtilitarios.msg_error("Error al mostrar", "Combo Box");
        }
    }
}
