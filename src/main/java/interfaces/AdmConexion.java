package interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface AdmConexion {

  default Connection obtenerConexion() {

    // driver jdbc
    String dbDriver = "com.mysql.cj.jdbc.Driver";
    // cadena conexion a base de datos
    String dbCadenaConexion = "jdbc:mysql://localhost:3306/prode";

    // usuario
    String dbUsuario = "root";
    // password
    String dbPass = "root";

    Connection conn = null;

    try {
      Class.forName(dbDriver);
      conn = DriverManager.getConnection(dbCadenaConexion, dbUsuario, dbPass);
    } catch (ClassNotFoundException e) {
      System.out.println("No se encontro el driver de la BD");
      throw new RuntimeException(e);
    } catch (SQLException e) {
      System.out.println("No se pudo conectar a la BD");
      throw new RuntimeException(e);
    }
    System.out.println("Conexión exitosa a la BD");
    return conn;
  }
}
