package GestorOperaciones;

import java.sql.*;
import Conector.CConector;
import Utilitarios.CUtilitarios;
import java.util.ArrayList;
import java.util.Arrays;

public class CCQManager {
    // ******************** Atributos ******************** //
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private final CConector conector = new CConector();
    private ArrayList<String[]> resultados; 
            
    // ******************** Metodos ******************** //
    public ArrayList<String[]> buscar_objetos(String consulta) throws SQLException{
        // Paso 1. Abrir la conexión
        conn = conector.conectar();
        // Paso 2. Ejecutar la Query (Consulta)
        try {
            resultados = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
            if (rs == null) {
                CUtilitarios.msg_adver("Elementos no encontrados", "Buscar Objetos");
            } else {
                while(rs.next()){
                    resultados.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getNString(4)});
                }
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "Buscar objetos");
        } finally {
            if (rs != null) {
                // Cerrar los resultados
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                // Cerrar el Statement
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                // Cerrar conexion
                conector.cerrar_conexion(conn);
            }
        }
        return resultados;
    }
    
    public ArrayList<String[]> buscarVentas(String consulta) throws SQLException{
        // Paso 1. Abrir la conexión
        conn = conector.conectar();
        // Paso 2. Ejecutar la Query (Consulta)
        try {
            resultados = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
            if (rs == null) {
                CUtilitarios.msg_adver("Elementos no encontrados", "Buscar Objetos");
            } else {
                while(rs.next()){
                    resultados.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7)});
                }
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "Buscar objetos");
        } finally {
            if (rs != null) {
                // Cerrar los resultados
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                // Cerrar el Statement
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                // Cerrar conexion
                conector.cerrar_conexion(conn);
            }
        }
        return resultados;
    }
    
    public ArrayList<String[]> buscar_clientes(String consulta) throws SQLException{
        // Paso 1. Abrir la conexión
        conn = conector.conectar();
        // Paso 2. Ejecutar la Query (Consulta)
        try {
            resultados = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
            if (rs == null) {
                CUtilitarios.msg_adver("Elementos no encontrados", "Buscar Objetos");
            } else {
                while(rs.next()){
                    resultados.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getNString(4),
                    rs.getNString(5), rs.getNString(6)});
                }
            }
        } catch (SQLException ex) {
            String cadena = "SQLException: " + ex.getMessage() + "\n"
                    + "SQLState: " + ex.getSQLState() + "\n"
                    + "VendorError: " + ex.getErrorCode();
            CUtilitarios.msg_error(cadena, "Buscar objetos");
        } finally {
            if (rs != null) {
                // Cerrar los resultados
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                // Cerrar el Statement
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                // Cerrar conexion
                conector.cerrar_conexion(conn);
            }
        }
        return resultados;
    }
    
    public boolean insertar_objeto(String consulta) throws SQLException {
        // Paso 1. Abrir la conexion
        conn = conector.conectar();
        // Paso 2. Ejecutar la consulta (Query)
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("Error: \n " + e.getMessage(), "Insertar Objeto");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return false;
    }
    
    public boolean elimina_objeto(String consulta) throws SQLException {
        // Paso 1. Abrir la conexion
        conn = conector.conectar();
        // Paso 2. Ejecutar la consulta (Query)
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("Error: \n " + e.getMessage(), "Elimina Objeto");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return false;
    }
    
    public boolean actualiza_objeto(String consulta) throws SQLException {
        // Paso 1. Abrir la conexion
        conn = conector.conectar();
        // Paso 2. Ejecutar la consulta (Query)
        try {
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            CUtilitarios.msg_error("Error: \n " + e.getMessage(), "Elimina Objeto");
        } finally {
            conector.cerrar_conexion(conn);
        }
        return false;
    }
    
    public void imprime_resultados(ArrayList<String[]> resultados){
        for (int i = 0; i < resultados.size(); i++) {
            System.out.println(i + " " + Arrays.toString(resultados.get(i)));
        }
        System.out.println("..............................................");
    }
}
