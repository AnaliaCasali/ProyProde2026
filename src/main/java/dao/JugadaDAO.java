package dao;

import entities.Jugada;
import entities.Partido;
import entities.Usuarios;
import exceptions.JugadaException;
import interfaces.AdmConexion;
import interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JugadaDAO implements DAO<Jugada, Integer>, AdmConexion {

  private static final String SQL_INSERT =
      "INSERT INTO Jugadas (idUsuario, idPartido, golesLocal, golesVisitante) VALUES (?, ?, ?, ?)";
  private static final String SQL_UPDATE =
      "UPDATE Jugadas SET golesLocal = ?, golesVisitante = ? WHERE idJugada = ?";
  private static final String SQL_DELETE =
      "DELETE FROM Jugadas WHERE idJugada = ?";
  private static final String SQL_GETALL =
      "SELECT j.idJugada, j.idUsuario, j.idPartido, j.golesLocal, j.golesVisitante, j.puntaje, " +
          "el.nombre AS nombreLocal, ev.nombre AS nombreVisitante " +
          "FROM Jugadas j " +
          "INNER JOIN Partidos p ON j.idPartido = p.idPartido " +
          "INNER JOIN Equipos el ON p.equipoLocal = el.idEquipo " +
          "INNER JOIN Equipos ev ON p.equipoVisitante = ev.idEquipo";
  private static final String SQL_GETBYID =
      SQL_GETALL + " WHERE j.idJugada = ?";
  //EXISTBYID: Usamos 1 en lugar de * para que el motor de la BD no procese columnas.
  private static final String SQL_EXISTBYID =
      "SELECT 1 FROM Jugadas WHERE idJugada = ? LIMIT 1";

  // SUM_PUNTOS: Obtiene la suma total de puntos de un usuario específico.
  private static final String SQL_GET_TOTAL_PUNTOS =
      "SELECT SUM(puntaje) FROM Jugadas WHERE idUsuario = ?";
  // GET_BY_USER_AND_MATCH: Busca una jugada específica para saber si existe previamente.
  //Reutilizamos SQL_GETALL para tener los nombres de los equipos disponibles.
  private static final String SQL_GET_BY_USER_AND_MATCH =
      SQL_GETALL + " WHERE j.idUsuario = ? AND j.idPartido = ?";
  // GET_BY_ETAPA: Filtra las jugadas del usuario por una jornada/etapa específica.
  private static final String SQL_GET_BY_ETAPA =
      SQL_GETALL + " WHERE j.idUsuario = ? AND p.idEtapa = ?";

  @Override
  public List<Jugada> getAll() {
    List<Jugada> lista = new ArrayList<>(); // Lista local para evitar acumulación de datos
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_GETALL);
         ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Jugada j = new Jugada();
        j.setIdJugada(rs.getInt("idJugada"));

        // Mapeo de Usuario
        Usuarios u = new Usuarios();
        u.setIdUsuario(rs.getInt("idUsuario"));
        j.setUsuario(u);

        // Mapeo de Partido con nombres de equipos
        Partido p = new Partido();
        p.setIdPartido(rs.getInt("idPartido"));
        p.setEquipolocal(rs.getString("nombreLocal"));
        p.setEquipoVisitante(rs.getString("nombreVisitante"));
        j.setPartido(p);

        j.setGolesLocal(rs.getInt("golesLocal"));
        j.setGolesVisitante(rs.getInt("golesVisitante"));
        j.setPuntaje(rs.getInt("puntaje"));

        lista.add(j);
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al listar las jugadas desde la base de datos", e);
    }
    return lista;
  }

  @Override
  public void insert(Jugada objeto) {
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_INSERT, RETURN_GENERATED_KEYS)) {

      ps.setInt(1, objeto.getUsuario().getIdUsuario());
      ps.setInt(2, objeto.getPartido().getIdPartido());
      ps.setInt(3, objeto.getGolesLocal());
      ps.setInt(4, objeto.getGolesVisitante());

      int filasAfectadas = ps.executeUpdate();

      if (filasAfectadas == 0) {
        throw new JugadaException("No se pudo insertar la jugada.");
      }

      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          objeto.setIdJugada(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al intentar guardar el pronóstico", e);
    }
  }

  @Override
  public void update(Jugada objeto) {
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

      ps.setInt(1, objeto.getGolesLocal());
      ps.setInt(2, objeto.getGolesVisitante());
      ps.setInt(3, objeto.getIdJugada());

      int filasAfectadas = ps.executeUpdate();

      if (filasAfectadas == 0) {
        throw new JugadaException("No se pudo actualizar: la jugada con ID " + objeto.getIdJugada() + " no existe.");
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al intentar actualizar el pronóstico", e);
    }
  }

  @Override
  public void delete(Integer id) {
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

      ps.setInt(1, id);
      int filasAfectadas = ps.executeUpdate();

      if (filasAfectadas == 0) {
        throw new JugadaException("No se encontró la jugada con ID " + id + " para eliminar.");
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al intentar eliminar la jugada", e);
    }
  }

  @Override
  public Jugada getById(Integer id) {
    Jugada jugada = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_GETBYID)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          jugada = new Jugada();
          jugada.setIdJugada(rs.getInt("idJugada"));

          Usuarios u = new Usuarios();
          u.setIdUsuario(rs.getInt("idUsuario"));
          jugada.setUsuario(u);

          Partido p = new Partido();
          p.setIdPartido(rs.getInt("idPartido"));
          p.setEquipolocal(rs.getString("nombreLocal"));
          p.setEquipoVisitante(rs.getString("nombreVisitante"));
          jugada.setPartido(p);

          jugada.setGolesLocal(rs.getInt("golesLocal"));
          jugada.setGolesVisitante(rs.getInt("golesVisitante"));
          jugada.setPuntaje(rs.getInt("puntaje"));
        }
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al recuperar la jugada con ID: " + id, e);
    }
    return jugada;
  }

  @Override
  public boolean existsById(Integer id) {
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_EXISTBYID)) {

      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next(); // Con 'SELECT 1' y 'LIMIT 1', si hay un registro, rs.next() es true.
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al verificar existencia de la jugada", e);
    }
  }

  public int getPuntosTotalesPorUsuario(int idUsuario) {
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_GET_TOTAL_PUNTOS)) {

      ps.setInt(1, idUsuario);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1); // Retorna la suma del campo puntaje
        }
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al calcular el puntaje total acumulado", e);
    }
    return 0;
  }

  public Jugada getByUserAndMatch(int idUsuario, int idPartido) {
    Jugada jugada = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_GET_BY_USER_AND_MATCH)) {

      ps.setInt(1, idUsuario);
      ps.setInt(2, idPartido);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          jugada = new Jugada();
          jugada.setIdJugada(rs.getInt("idJugada"));

          Usuarios u = new Usuarios();
          u.setIdUsuario(rs.getInt("idUsuario"));
          jugada.setUsuario(u);

          Partido p = new Partido();
          p.setIdPartido(rs.getInt("idPartido"));
          p.setEquipolocal(rs.getString("nombreLocal"));
          p.setEquipoVisitante(rs.getString("nombreVisitante"));
          jugada.setPartido(p);

          jugada.setGolesLocal(rs.getInt("golesLocal"));
          jugada.setGolesVisitante(rs.getInt("golesVisitante"));
          jugada.setPuntaje(rs.getInt("puntaje"));
        }
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al buscar jugada existente para el partido", e);
    }
    return jugada;
  }

  public List<Jugada> getByEtapa(int idUsuario, int idEtapa) {
    List<Jugada> listaPorEtapa = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(SQL_GET_BY_ETAPA)) {

      ps.setInt(1, idUsuario);
      ps.setInt(2, idEtapa);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Jugada j = new Jugada();
          j.setIdJugada(rs.getInt("idJugada"));

          Usuarios u = new Usuarios();
          u.setIdUsuario(rs.getInt("idUsuario"));
          j.setUsuario(u);

          Partido p = new Partido();
          p.setIdPartido(rs.getInt("idPartido"));
          p.setEquipolocal(rs.getString("nombreLocal"));
          p.setEquipoVisitante(rs.getString("nombreVisitante"));
          j.setPartido(p);

          j.setGolesLocal(rs.getInt("golesLocal"));
          j.setGolesVisitante(rs.getInt("golesVisitante"));
          j.setPuntaje(rs.getInt("puntaje"));

          listaPorEtapa.add(j);
        }
      }
    } catch (SQLException e) {
      throw new JugadaException("Error al filtrar jugadas por etapa", e);
    }
    return listaPorEtapa;
  }
}
