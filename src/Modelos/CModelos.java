package Modelos;

import GestorOperaciones.CCQManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class CModelos {
    // ******************** Atributos ******************** //
    private final CCQManager mngr = new CCQManager();
    private String consulta;
    // ******************** Metodos ******************** //
    public ArrayList<String[]> busca_id_cliente(int valor) throws SQLException{
        consulta = "SELECT idCliente, ap_materno, ap_paterno, nombre, contacto, correo_electronico " + 
                   "FROM panaderia.cliente " + 
                   "WHERE cliente.idCliente = " + valor + ";";
        return mngr.buscar_clientes(consulta);
    }
    
    public ArrayList<String[]> busca_cliente() throws SQLException{
        consulta = "SELECT idCliente, ap_paterno, ap_materno, nombre, contacto, correo_electronico " + 
                   "FROM panaderia.cliente;";
        return mngr.buscar_clientes(consulta);
    }
    
    public ArrayList<String[]> busca_venta() throws SQLException{
        consulta = "SELECT detalleventa.idVenta, CONCAT(cliente.nombre, ' ', cliente.ap_paterno, ' ', cliente.ap_materno) AS nombre_completo, \n" +
                   "producto.nombre, detalleventa.cantidad, detalleventa.precio_unitario, detalleventa.subtotal, venta.total\n" +
                   "FROM detalleventa\n" +
                   "JOIN venta ON detalleventa.idVenta = venta.idVenta\n" +
                   "JOIN cliente ON venta.idCliente = cliente.idCliente\n" +
                   "JOIN producto ON detalleventa.idProducto = producto.idProducto;";
        return mngr.buscarVentas(consulta);
    }
    
    public boolean inserta_cliente(int clave, String ap_materno, String ap_paterno, String nombre, String contacto, String email) throws SQLException{
        consulta = "INSERT INTO panaderia.cliente (idCliente, ap_paterno, ap_materno, nombre, contacto, correo_electronico, idTipoCliente) " + 
                   "VALUES (" + clave + ",'" + ap_paterno + "','" + ap_materno + "','" + nombre + "','" + contacto + "','" + email + "',2);";
        return mngr.insertar_objeto(consulta);
    }
    
    public boolean inserta_venta(int idCliente, int cantidad, float descuento, float subtotal, float total) throws SQLException {
        consulta = "INSERT INTO panaderia.venta (idVenta, idCliente, cantidad, descuento, subtotal, total) " +
                   "VALUES (null," + idCliente + "," + cantidad + "," + descuento + "," + subtotal + "," + total + ");";
        return mngr.insertar_objeto(consulta);
    }
    
    public boolean inserta_detalleVenta(int idVenta, int idProducto, int cantidad, float precio, float subtotal) throws SQLException {
        consulta = "INSERT INTO panaderia.detalleVenta (idDetalleVenta, idVenta, idProducto, cantidad, precio_unitario, subtotal) " +
                   "VALUES (null," + idVenta + "," + idProducto + "," + cantidad + "," + precio + "," + subtotal + ");";
        return mngr.insertar_objeto(consulta);
    }
    
    public boolean elimina_cliente(int id) throws SQLException{
        consulta = "DELETE FROM panaderia.cliente " + 
                   "WHERE cliente.idCliente = " + id + ";";
        return mngr.elimina_objeto(consulta);
    }
    
    public boolean actualiza_cliente(int claveBuscada, int clave, String ap_paterno, String ap_materno, String nombre, String contacto, String email) throws SQLException{
        consulta = "UPDATE panaderia.cliente " + 
                   "SET idCliente = " + clave + ", " + 
                   "ap_paterno = '" + ap_paterno + "', " +
                   "ap_materno = '" + ap_materno + "', " +
                   "nombre = '" + nombre + "', " +
                   "contacto = '" + contacto + "', " +
                   "correo_electronico = '" + email + "' " +
                   "WHERE cliente.idCliente = " + claveBuscada + ";";
        return mngr.actualiza_objeto(consulta);
    }
}
