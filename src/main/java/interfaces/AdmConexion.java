package interfaces;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public interface AdmConexion {

  default Connection obtenerConexion() {

    	/*
		# Comentarios
		db.driver=com.mysql.cj.jdbc.Driver
		db.url=jdbc:jdbc:mysql://localhost:3306/prode
		db.user=root
		db.pass=root */
    Connection conn =null;

    Properties dbProperties=new Properties();
    try {
      // cargamos el archivo utilizando la ruta relativa donde esta el proyecto
      dbProperties.load(Thread.currentThread().
          getContextClassLoader()
          .getResourceAsStream("database.properties"));

      // leemos las propiedades
      String driver = dbProperties.getProperty("db.driver");
      String cadenaConexion= dbProperties.getProperty("db.url");
      String usuario= dbProperties.getProperty("db.user", "root");
      String pass= dbProperties.getProperty("db.pass");

      Class.forName(driver);
      conn = DriverManager.
          getConnection(cadenaConexion,usuario, pass);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }		 catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();}

    return conn;

  }
}
