package dao;

import entities.Usuarios;
import interfaces.AdmConexion;
import interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl implements DAO<Usuarios, Integer>, AdmConexion {
  private Connection conn = null;

  // CONSULTAS SQL
  // insert general
  private static final String SQL_INSERT =
      "INSERT INTO usuarios (email, password, curso, carrera, nombreGrupo)" +
          "VALUES (?, ?, ?, ?, ?)";

  // update general
  private static final String SQL_UPDATE =
      "UPDATE usuarios SET " +
          "email = ? , password ? , curso = ? , carrera = ? , nombreGrupo)" +
          "WHERE idUsuario = ?";

  private static final String SQL_DELETE =
      "DELETE * FROM usuarios WHERE idUsuario = ?";

  private static final String SQL_GETALL =
      "SELECT * FROM usuarios ORDER BY idUsuario";

  private static final String SQL_GETBYID =
      "SELECT * FROM usuarios WHERE idUsuario = ?";


  // métodos DAO
  @Override
  public List<Usuarios> getAll() {
    conn = obtenerConexion();

    PreparedStatement pst = null;
    ResultSet rs = null;
    
    List<Usuarios> listaUsuarios = new ArrayList<>();
    
    return listaUsuarios;
  }

  @Override
  public void insert(Usuarios objeto) {

  }

  @Override
  public void update(Usuarios objeto) {

  }

  @Override
  public void delete(Integer id) {

  }

  @Override
  public Usuarios getById(Integer id) {
    return null;
  }

  @Override
  public boolean existsById(Integer id) {
    return false;
  }
}
