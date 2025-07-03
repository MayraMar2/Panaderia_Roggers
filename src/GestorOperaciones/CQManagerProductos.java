/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GestorOperaciones;

import Conector.CConector;
import Utilitarios.CUtilitarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rocio
 */
public class CQManagerProductos {
    
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private final CConector conector = new CConector();
    private ArrayList<String[]> resultados;

    //***************** METODOS **************
    public ArrayList<String[]> buscar_objetos(String consulta) throws SQLException {
        //1. Abrir la conexion 
        conn = conector.conectar();
        //2. Ejecutar la query(consulta)
        try {
            resultados = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
            if (rs == null) {
                CUtilitarios.msg_adver("Elementos no encontrados", "Buscar objeto");
            } else {
                while (rs.next()) {
                    resultados.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7)
                    });
                }
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "conexion");
        } finally {
            //CERRAR RESULTADOS
            try {
                rs.close();
            } catch (SQLException e) {
            }
            //cerrar statement
            try {
                stmt.close();
            } catch (SQLException e) {
            }
            //cerrar conexion
            conector.cerrar_conexion(conn);
        }
        return resultados;
    }

    
    /*
    public boolean inserta_objeto(String consulta) throws SQLException {
        //1. abrir la conexion
        conn = conector.conectar();
        //2.- ejecutar la query
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("ERROR: \n" + e.getMessage(), "inserta objeto");
        } finally {
            //3. Cerrar la conexion
            conector.cerrar_conexion(conn);
        }
        return false;
    }

    public boolean elimina_objeto(String consulta) throws SQLException {
        //1.- abrir conexion
        conn = conector.conectar();
        //2.- Ejecutar la Query
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("ERROR: " + e.getMessage(), "Elimina objeto");
        } finally {
            //3.- cerrar la conexion
            conector.cerrar_conexion(conn);
        }
        return false;

    }

    public boolean actualiza_objeto(String consulta) throws SQLException {
        conn = conector.conectar();
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("Error: " + e.getMessage(), "actualiza objeto");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return false;
    }

*/

    public void imprime_resultados(ArrayList<String[]> resultados) {
        for (int i = 0; i < resultados.size(); i++) {
            System.out.println(i + " " + Arrays.toString(resultados.get(i)));
        }
        System.out.println("...................");
    }
    /////////////////////////////////////// BUSCANDO EL TIPO DE PRODUCTO //////////////////////////////////////////////////////////
        public ArrayList<String[]> buscar_tipo_producto(String consulta) throws SQLException {
        //1. Abrir la conexion 
        conn = conector.conectar();
        //2. Ejecutar la query(consulta)
        try {
            resultados = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
            if (rs == null) {
                CUtilitarios.msg_adver("Elementos no encontrados", "Buscar objeto");
            } else {
                while (rs.next()) {
                    resultados.add(new String[]{
                        rs.getString(1),
                        rs.getString(2),});
                }
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "conexion");
        } finally {
            //CERRAR RESULTADOS
            try {
                rs.close();
            } catch (SQLException e) {
            }
            //cerrar statement
            try {
                stmt.close();
            } catch (SQLException e) {
            }
            //cerrar conexion
            conector.cerrar_conexion(conn);
        }
        return resultados;
    }
    
        
        
        
        
        /////////////////////////////////////////////////////////////////////////////////////////


    // Inserta sin recuperar ID
    public boolean inserta_objeto(String sql, Object... parametros) throws SQLException {
        conn = conector.conectar();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < parametros.length; i++) {
                pstmt.setObject(i + 1, parametros[i]);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("ERROR:\n" + e.getMessage(), "inserta objeto");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return false;
    }

    // Inserta y retorna el ID generado
    public int inserta_y_retorna_id(String sql, Object... parametros) throws SQLException {
        int idGenerado = -1;
        conn = conector.conectar();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < parametros.length; i++) {
                pstmt.setObject(i + 1, parametros[i]);
            }
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            CUtilitarios.msg_error("ERROR:\n" + e.getMessage(), "insertar con ID");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return idGenerado;
    }


        
        
    ///////////////////////////////////// ACTUALIZAR
    ///
public boolean actualiza_objeto(String consulta) throws SQLException {
    conn = conector.conectar();
    try {
        PreparedStatement pstmt = conn.prepareStatement(consulta);
        pstmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        CUtilitarios.msg_error("ERROR: " + e.getMessage(), "Actualizar objeto");
    } finally {
        conector.cerrar_conexion(conn);
    }
    return false;
}





public boolean elimina_objeto(String consulta) throws SQLException {
    conn = conector.conectar();
    try {
        PreparedStatement pstmt = conn.prepareStatement(consulta);
        pstmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        CUtilitarios.msg_error("ERROR: " + e.getMessage(), "Elimina objeto");
    } finally {
        conector.cerrar_conexion(conn);
    }
    return false;
}

    
    
    
    
    
    
    
    
    
    
        
        
        
}

