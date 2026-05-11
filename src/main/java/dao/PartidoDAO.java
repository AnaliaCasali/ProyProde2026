package dao;


import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import entities.Partido;
import interfaces.AdmConexion;
import interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAO implements AdmConexion, DAO<Partido, Integer> {
  private Connection conn = null;

  private static final String SQL_INSERT =
      "INSERT INTO partidos (equipoLocal, equipoVisitante, idEtapa, idEstadio, fechaHora, golesLocal, golesVisitante, finalizado) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_UPDATE =
      "UPDATE partidos SET " +
          " equipoLocal = ?, " +
          " equipoVisitante = ?, " +
          " idEtapa = ?, " +
          " idEstadio = ?, " +
          " fechaHora = ?, " +
          " golesLocal = ?, " +
          " golesVisitante = ?, " +
          " finalizado = ? " +
          "WHERE idPartido = ?";

  private static final String SQL_DELETE = "DELETE FROM partidos WHERE idPartido = ?";

  //Tengo que usar varios JOINS para el GetAll debido a que tiene muchos elementos de otras tablas (Equipos, Etapa y Estadio).
  private static final String SQL_GETALL =
      "SELECT p.*, " +
          "el.nombre AS local_nombre, el.icono AS local_icono, el.grupo AS local_grupo, " +
          "ev.nombre AS visitante_nombre, ev.icono AS visitante_icono, ev.grupo AS visitante_grupo, " +
          "et.nombreEtapa, es.estadio AS estadio_nombre, es.ciudad, es.pais " +
          "FROM partidos p " +
          "JOIN equipos el ON p.equipoLocal = el.idEquipo " +
          "JOIN equipos ev ON p.equipoVisitante = ev.idEquipo " +
          "JOIN etapas et ON p.idEtapa = et.idEtapa " +
          "JOIN estadios es ON p.idEstadio = es.idEstadio " +
          "ORDER BY p.fechaHora ASC";

  //Como vuelvo a traer los mismos elementos la consulta es similar.
  private static final String SQL_GETBYID =
      "SELECT p.*, " +
          "el.nombre AS local_nombre, el.icono AS local_icono, el.grupo AS local_grupo, " +
          "ev.nombre AS visitante_nombre, ev.icono AS visitante_icono, ev.grupo AS visitante_grupo, " +
          "et.nombreEtapa, es.estadio AS estadio_nombre, es.ciudad, es.pais " +
          "FROM partidos p " +
          "JOIN equipos el ON p.equipoLocal = el.idEquipo " +
          "JOIN equipos ev ON p.equipoVisitante = ev.idEquipo " +
          "JOIN etapas et ON p.idEtapa = et.idEtapa " +
          "JOIN estadios es ON p.idEstadio = es.idEstadio " +
          "WHERE p.idPartido = ?";

  private static final String SQL_EXISTSBYID = "SELECT * FROM partidos WHERE idPartido = ?";

  @Override
  public List<Partido> getAll() {
    conn = obtenerConexion();
    PreparedStatement pst = null;
    ResultSet rs = null;
    List<Partido> lista = new ArrayList<>();

    try {
      pst = conn.prepareStatement(SQL_GETALL);
      rs = pst.executeQuery();

      while (rs.next()) {
        //Uso el constructor vacío de Equipo para setear al equipo local.
        Equipo local = new Equipo();
        local.setIdEquipo(rs.getInt("equipoLocal"));
        local.setNombre(rs.getString("local_nombre"));
        local.setIcono(rs.getString("local_icono"));
        local.setGrupo(rs.getString("local_grupo"));

        //Vuelvo a usar ese constructor pero esta vez para el equipo visitante
        Equipo visitante = new Equipo();
        visitante.setIdEquipo(rs.getInt("equipoVisitante"));
        visitante.setNombre(rs.getString("visitante_nombre"));
        visitante.setIcono(rs.getString("visitante_icono"));
        visitante.setGrupo(rs.getString("visitante_grupo"));

        //Uso el constructor de etapas y seteo.
        Etapa etapa = new Etapa();
        etapa.setIdEtapa(rs.getInt("idEtapa"));
        etapa.setNombreEtapa(rs.getString("nombreEtapa"));

        //Lo mismo de usar y setear pero en Estadio
        Estadio estadio = new Estadio();
        estadio.setIdEstadio(rs.getInt("idEstadio"));
        estadio.setEstadio(rs.getString("estadio_nombre"));
        estadio.setCiudad(rs.getString("ciudad"));
        estadio.setPais(rs.getString("pais"));

        //Por ultimo instacio Partido y seteo lo que contiene Partido.
        Partido partido = new Partido();
        partido.setIdPartido(rs.getInt("idPartido"));
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setEtapa(etapa);
        partido.setEstadio(estadio);
        partido.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());
        partido.setGolesLocal(rs.getInt("golesLocal"));
        partido.setGolesVisitante(rs.getInt("golesVisitante"));
        partido.setFinalizado(rs.getBoolean("finalizado"));

        //Una vez creado el partido lo agrego a la lista
        lista.add(partido);
      }

      pst.close();
      rs.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println("Error al obtener todos los partidos.");
      throw new RuntimeException(e);
    }
    return lista;
  }

  @Override
  public void insert(Partido objeto) {
    conn = obtenerConexion();
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
      pst = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

      //Para setear un nuevo partido al tener los elementos en su clase puedo usar los Get
      pst.setInt(1, objeto.getEquipoLocal().getIdEquipo());
      pst.setInt(2, objeto.getEquipoVisitante().getIdEquipo());
      pst.setInt(3, objeto.getEtapa().getIdEtapa());
      pst.setInt(4, objeto.getEstadio().getIdEstadio());
      //Timestamp: Convierte el LocalDateTime de Java a SQL
      pst.setTimestamp(5, Timestamp.valueOf(objeto.getFechaHora()));
      pst.setInt(6, objeto.getGolesLocal());
      pst.setInt(7, objeto.getGolesVisitante());
      pst.setBoolean(8, objeto.isFinalizado());

      int resultado = pst.executeUpdate();

      if (resultado == 1) {
        System.out.println("Partido agregado correctamente.");
      } else {
        System.out.println("No se pudo agregar el partido.");
      }

      rs = pst.getGeneratedKeys();

      if (rs.next()) {
        objeto.setIdPartido(rs.getInt(1));
        System.out.println("El id asignado es: " + objeto.getIdPartido());
      }

      pst.close();
      rs.close();
      conn.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(Partido objeto) {
    conn = obtenerConexion();
    //Pruebo si existe un partido por la ID
    if (this.existsById(objeto.getIdPartido())) {
      try {
        PreparedStatement pst = conn.prepareStatement(SQL_UPDATE);

        //Si existe lo actualizo
        pst.setInt(1, objeto.getEquipoLocal().getIdEquipo());
        pst.setInt(2, objeto.getEquipoVisitante().getIdEquipo());
        pst.setInt(3, objeto.getEtapa().getIdEtapa());
        pst.setInt(4, objeto.getEstadio().getIdEstadio());
        pst.setTimestamp(5, Timestamp.valueOf(objeto.getFechaHora()));
        pst.setInt(6, objeto.getGolesLocal());
        pst.setInt(7, objeto.getGolesVisitante());
        pst.setBoolean(8, objeto.isFinalizado());
        pst.setInt(9, objeto.getIdPartido());

        int resultado = pst.executeUpdate();

        if (resultado == 1) {
          System.out.println("Partido actualizado correctamente");
        } else {
          System.out.println("No se pudo actualizar el partido");
        }
        pst.close();
        conn.close();
      } catch (SQLException e) {
        System.out.println("Error al actualizar el partido.");
      }
    }
  }

  @Override
  public void delete(Integer id) {
    conn = obtenerConexion();
    try {
      PreparedStatement pst = conn.prepareStatement(SQL_DELETE);
      pst.setInt(1, id);
      int resultado = pst.executeUpdate();

      if (resultado == 1) {
        System.out.println("Partido eliminado correctamente");
      } else {
        System.out.println("No se pudo eliminar el partido");
      }

      pst.close();
      conn.close();
    } catch (SQLException e) {
      System.out.println("No se pudo eliminar el partido. Error: " + e.getMessage());
    }
  }

  @Override
  public Partido getById(Integer id) {
    conn = obtenerConexion();
    Partido partido = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    //Como el getByID es parecido al getAll tengo que volver a instanciar todos los elementos de Partido
    try {
      pst = conn.prepareStatement(SQL_GETBYID);
      pst.setInt(1, id);
      rs = pst.executeQuery();

      if (rs.next()) {
        //Instancio y seteo el equipo Local.
        Equipo local = new Equipo();
        local.setIdEquipo(rs.getInt("equipoLocal"));
        local.setNombre(rs.getString("local_nombre"));
        local.setIcono(rs.getString("local_icono"));
        local.setGrupo(rs.getString("local_grupo"));

        //Instancio y seteo el equipo pero de visitante
        Equipo visitante = new Equipo();
        visitante.setIdEquipo(rs.getInt("equipoVisitante"));
        visitante.setNombre(rs.getString("visitante_nombre"));
        visitante.setIcono(rs.getString("visitante_icono"));
        visitante.setGrupo(rs.getString("visitante_grupo"));

        //Instancio y seteo la etapa
        Etapa etapa = new Etapa();
        etapa.setIdEtapa(rs.getInt("idEtapa"));
        etapa.setNombreEtapa(rs.getString("nombreEtapa"));

        //Instancio y seteo el Estadio
        Estadio estadio = new Estadio();
        estadio.setIdEstadio(rs.getInt("idEstadio"));
        estadio.setEstadio(rs.getString("estadio_nombre"));
        estadio.setCiudad(rs.getString("ciudad"));
        estadio.setPais(rs.getString("pais"));

        //Una vez tengo lo que necesito para el partido lo seteo.
        partido = new Partido();
        partido.setIdPartido(rs.getInt("idPartido"));
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setEtapa(etapa);
        partido.setEstadio(estadio);
        partido.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());
        partido.setGolesLocal(rs.getInt("golesLocal"));
        partido.setGolesVisitante(rs.getInt("golesVisitante"));
        partido.setFinalizado(rs.getBoolean("finalizado"));
      }

      pst.close();
      rs.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return partido;
  }

  @Override
  public boolean existsById(Integer id) {
    conn = obtenerConexion();
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean existe = false;

    try {
      pst = conn.prepareStatement(SQL_EXISTSBYID);
      pst.setInt(1, id);
      rs = pst.executeQuery();

      if (rs.next()) {
        existe = true;
      }

      pst.close();
      rs.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return existe;
  }

  @Override
  public Connection obtenerConexion() {
    return AdmConexion.super.obtenerConexion();
  }
}