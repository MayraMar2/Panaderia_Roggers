/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import GestorOperaciones.CQManagerProductos;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author rocio
 */
public class CModelosProductos {
    //****************** Atributos***********

    private final CQManagerProductos mngr = new CQManagerProductos();
    private String consulta;
    //****************** Metodos*************

    public ArrayList<String[]> busca_objetos_model() throws SQLException {
        consulta = "SELECT producto.idProducto, "
                + "producto.nombre, "
                + "tipoproducto.tipo, "
                + "producto.precio, "
                + "gestionproductos.cantidad, "
                + "gestionproductos.fecha_elaboracion, "
                + "gestionproductos.fecha_caducidad "
                + "FROM producto "
                + "JOIN tipoproducto  ON producto.idTipoProducto = tipoproducto.idTipoProducto "
                + "JOIN gestionproductos  ON producto.idProducto = gestionproductos.idproducto;";
        return mngr.buscar_objetos(consulta);
    }
    /*

    public ArrayList<String[]> busca_objeto_id_model(int valor) throws SQLException {
        consulta = "SELECT * FROM pasajero WHERE pasajero.id_pasajero = " + valor;
        return mngr.buscar_objetos(consulta);
    }

    public boolean inserta_objeto_model(String nombre, String AP, String AM, String correo, int tipo) throws SQLException {
        consulta = "INSERT INTO `pasajero`(`id_pasajero`, `nombre`, `apellido_p`, `apellido_m`,`correo`,`id_tipo` )"
                + "VALUES (null,'" + nombre + "','" + AP + "','" + AM + "','" + correo + "'," + tipo + ");";
        return mngr.inserta_objeto(consulta);
    }

    public boolean actualiza_objeto_model(int id, String nombre, String AP, String AM, String correo, int tipo) throws SQLException {
        consulta = "UPDATE pasajero SET nombre='" + nombre + "', "
                + " apellido_p = '" + AP + "', "
                + " apellido_m = '" + AM + "', "
                + " correo = '" + correo + "', "
                + " id_tipo = " + tipo + " "
                + "WHERE pasajero.id_pasajero= " + id;
        return mngr.actualiza_objeto(consulta);
    }

    public boolean elimina_objeto_model(int id) throws SQLException {
        consulta = "DELETE  FROM pasajero WHERE pasajero.id_pasajero = " + id;
        return mngr.elimina_objeto(consulta);
    }
*/
    ////////////////////////////////// CARGANDO EL COMBO //////////////////////////////////77

    public ArrayList<String[]> carga_tipo_producto() throws SQLException {
        consulta = "select tipoproducto.idTipoProducto, tipoproducto.tipo from tipoproducto ";
        return mngr.buscar_tipo_producto(consulta);

    }
    
    
    
/*
    ////////////////////////////////// INSERCION A PRODUCTO ////////////////////////////7
    public boolean inserta_producto(String nombre, float precio, int tipo) throws SQLException {
        consulta = "INSERT INTO `producto`(`idProducto`, `nombre`, `precio`, `idTipoProducto`)"
                + "VALUES (null,'" + nombre + "'," + precio + "," + tipo + ");";
        return mngr.inserta_objeto(consulta);
    }

    /////////////////////// INSERCION A GESTION ///////////////////////77
    public boolean inserta_gestion(int idproducto, int cantidad, String elaboracion, String caducidad) throws SQLException {
        consulta = "INSERT INTO `gestionproductos`(`idGestionProductos`, `idProducto`, `cantidad`, `fecha_elaboracion`, `fecha_caducidad`)"
                + "VALUES (null," + idproducto + "," + cantidad + ",'" + elaboracion + "','" + caducidad + "');";
        return mngr.inserta_objeto(consulta);
    }

    //////////////////////////// ACTUALIZA PRODUCTO ////////////////77
    
    public boolean actualiza_producto(int id, String nombre, int tipo, float precio) throws SQLException {
        consulta = "UPDATE producto SET nombre='" + nombre + "', "
                + " precio = '" + precio + "', "
                + " idTipoProducto = '" + tipo + "', "
                + "WHERE producto.idProducto= " + id;
        return mngr.actualiza_objeto(consulta);
    }

    //////////////////// ELIMINAR PRODUCTO ///////////////////////////7
       public boolean elimina_producto(int id) throws SQLException {
        consulta = "DELETE  FROM producto WHERE producto.idProducto = " + id;
        return mngr.elimina_objeto(consulta);
    }
*/
    ////////////////////////////////////////////////////////////

    
    
      /////////////////////// INSERCION A GESTION ///////////////////////77
    public boolean inserta_gestion(int idproducto, int cantidad, String elaboracion, String caducidad) throws SQLException {
        consulta = "INSERT INTO `gestionproductos`(`idGestionProductos`, `idProducto`, `cantidad`, `fecha_elaboracion`, `fecha_caducidad`)"
                + "VALUES (null," + idproducto + "," + cantidad + ",'" + elaboracion + "','" + caducidad + "');";
        return mngr.inserta_objeto(consulta);
    }

    // Inserta solo producto (no retorna id)
    public boolean inserta_producto1(String nombre, float precio, int tipo) throws SQLException {
        String sql = "INSERT INTO producto(nombre, precio, idTipoProducto) VALUES (?, ?, ?)";
        return mngr.inserta_objeto(sql, nombre, precio, tipo);
    }

    // Inserta y RETORNA ID del producto generado
    public int inserta_producto_y_retorna_id(String nombre, float precio, int tipo) throws SQLException {
        String sql = "INSERT INTO producto(nombre, precio, idTipoProducto) VALUES (?, ?, ?)";
        return mngr.inserta_y_retorna_id(sql, nombre, precio, tipo);
    }

    // Inserta en gestionproductos
    public boolean inserta_gestion1(int idProducto, int cantidad, String elaboracion, String caducidad) throws SQLException {
        String sql = "INSERT INTO gestionproductos(idProducto, cantidad, fecha_elaboracion, fecha_caducidad) VALUES (?, ?, ?, ?)";
        return mngr.inserta_objeto(sql, idProducto, cantidad, elaboracion, caducidad);
    }

    // Inserta ambos: producto y gestion
    public boolean inserta_producto_y_gestion(String nombre, float precio, int tipo, int cantidad, String elaboracion, String caducidad) throws SQLException {
        int idProducto = inserta_producto_y_retorna_id(nombre, precio, tipo);
        if (idProducto == -1) {
            return false;
        }
        return inserta_gestion1(idProducto, cantidad, elaboracion, caducidad);
    }

    ////////////////////////////////////// ACTUALIZAR
   
 public boolean actualiza_producto_y_gestion(int id, String nombre, float precio, int tipo, int cantidad, String elaboracion, String caducidad) throws SQLException {
    boolean actualizadoProducto = false;
    boolean actualizadoGestion = false;

    // Actualiza tabla producto
    String consultaProducto = "UPDATE producto SET nombre = '" + nombre + "', precio = " + precio + ", idTipoProducto = " + tipo + " WHERE idProducto = " + id;
    actualizadoProducto = mngr.actualiza_objeto(consultaProducto);

    // Actualiza tabla gestionproducto
    String consultaGestion = "UPDATE gestionproductos SET cantidad = " + cantidad + ", fecha_elaboracion = '" + elaboracion + "', fecha_caducidad = '" + caducidad + "' WHERE idProducto = " + id;
    actualizadoGestion = mngr.actualiza_objeto(consultaGestion);

    return actualizadoProducto && actualizadoGestion;
}

    
    
    
    
    
    public boolean elimina_producto(int id) throws SQLException {
    boolean eliminado = false;

    // Primero eliminamos las relaciones en gestionproducto
    String consultaGestion = "DELETE FROM gestionproductos WHERE idProducto = " + id;
    if (mngr.elimina_objeto(consultaGestion)) {
        // Luego eliminamos el producto
        String consultaProducto = "DELETE FROM producto WHERE idProducto = " + id;
        eliminado = mngr.elimina_objeto(consultaProducto);
    }

    return eliminado;
}

    
  /*  
    
        //////////////////// ELIMINAR PRODUCTO ///////////////////////////7
       public boolean elimina_producto(int id) throws SQLException {
        consulta = "DELETE  FROM producto WHERE producto.idProducto = " + id;
        return mngr.elimina_objeto(consulta);
    }
       
       */
}
