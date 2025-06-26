/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Utilitarios.CUtilitarios;

/**
 *
 * @author rocio
 */
public class CConector {
    
    //********************* Atributos **************
    private Connection conn;
    private final String DB = "panaderia";
    private final String USER = "root";
    private final String PSW = "";
    private final String URL = "jdbc:mysql://localhost:3306/" + DB;

    //************* Metodos **********
    public Connection conectar() {
        conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PSW);
            if (conn == null) {
                CUtilitarios.msg_error("ERROR: CONEXION NO ESTABLECIDA", "Conexion");
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "conexion");
        }
        return conn;
    }

    public void cerrar_conexion(Connection conn) throws SQLException {
        conn.close();
    }

}
