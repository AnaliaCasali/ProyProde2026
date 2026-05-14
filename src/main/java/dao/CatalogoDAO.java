package dao;

import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import interfaces.AdmConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO implements AdmConexion {

  private static final String SQL_EQUIPOS =
      "SELECT idEquipo, nombre, icono, grupo FROM equipos ORDER BY nombre";

  private static final String SQL_ETAPAS_ELIMINATORIAS =
      "SELECT idEtapa, nombreEtapa FROM etapas " +
          "WHERE nombreEtapa IN (?, ?, ?, ?, ?) " +
          "ORDER BY idEtapa";

  private static final String SQL_ESTADIOS =
      "SELECT idEstadio, estadio, ciudad, pais FROM estadios ORDER BY estadio";

  public List<Equipo> obtenerEquipos() {
    List<Equipo> equipos = new ArrayList<>();

    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_EQUIPOS);
         ResultSet rs = pst.executeQuery()) {

      while (rs.next()) {
        Equipo equipo = new Equipo();
        equipo.setIdEquipo(rs.getInt("idEquipo"));
        equipo.setNombre(rs.getString("nombre"));
        equipo.setIcono(rs.getString("icono"));
        equipo.setGrupo(rs.getString("grupo"));
        equipos.add(equipo);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener los equipos.", e);
    }

    return equipos;
  }

  public List<Etapa> obtenerEtapasEliminatorias() {
    List<Etapa> etapas = new ArrayList<>();

    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_ETAPAS_ELIMINATORIAS)) {

      pst.setString(1, "Dieciseisavos de Final");
      pst.setString(2, "Octavos de Final");
      pst.setString(3, "Cuartos de Final");
      pst.setString(4, "Semifinal");
      pst.setString(5, "Final");

      try (ResultSet rs = pst.executeQuery()) {
        while (rs.next()) {
          Etapa etapa = new Etapa();
          etapa.setIdEtapa(rs.getInt("idEtapa"));
          etapa.setNombreEtapa(rs.getString("nombreEtapa"));
          etapas.add(etapa);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener las etapas eliminatorias.", e);
    }

    return etapas;
  }

  public List<Estadio> obtenerEstadios() {
    List<Estadio> estadios = new ArrayList<>();

    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_ESTADIOS);
         ResultSet rs = pst.executeQuery()) {

      while (rs.next()) {
        Estadio estadio = new Estadio();
        estadio.setIdEstadio(rs.getInt("idEstadio"));
        estadio.setEstadio(rs.getString("estadio"));
        estadio.setCiudad(rs.getString("ciudad"));
        estadio.setPais(rs.getString("pais"));
        estadios.add(estadio);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener los estadios.", e);
    }

    return estadios;
  }
}
