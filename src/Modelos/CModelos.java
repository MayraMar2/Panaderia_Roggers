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
    
    public boolean inserta_cliente(int clave, String ap_materno, String ap_paterno, String nombre, String contacto, String email) throws SQLException{
        consulta = "INSERT INTO panaderia.cliente (idCliente, ap_paterno, ap_materno, nombre, contacto, correo_electronico, idTipoCliente) " + 
                   "VALUES (" + clave + ",'" + ap_paterno + "','" + ap_materno + "','" + nombre + "','" + contacto + "','" + email + "',2);";
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
