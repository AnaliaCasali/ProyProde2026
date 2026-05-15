package servlets;

import dao.CatalogoDAO;
import entities.Estadio;
import entities.Etapa;
import entities.Partido;
import interfaces.AdmConexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/cargar-partido")
public class CargarPartidoServlet extends HttpServlet implements AdmConexion {

  private static final String SQL_PARTIDOS_PENDIENTES =
      "SELECT p.idPartido, p.idEtapa, p.idEstadio, p.fechaHora, " +
          "et.nombreEtapa, es.estadio AS estadio_nombre, es.ciudad, es.pais " +
          "FROM partidos p " +
          "JOIN etapas et ON p.idEtapa = et.idEtapa " +
          "JOIN estadios es ON p.idEstadio = es.idEstadio " +
          "WHERE (p.equipoLocal IS NULL OR p.equipoVisitante IS NULL) " +
          "AND p.idEtapa BETWEEN 4 AND 9 " +
          "ORDER BY p.fechaHora ASC";

  private static final String SQL_COMPLETAR_EQUIPOS =
      "UPDATE partidos SET equipoLocal = ?, equipoVisitante = ? WHERE idPartido = ?";

  private final CatalogoDAO catalogoDAO = new CatalogoDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    cargarFormulario(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      int idPartido = leerEntero(request, "idPartido", "Debe seleccionar el partido a completar.");
      int idEquipoLocal = leerEntero(request, "equipoLocal", "Debe seleccionar el equipo local.");
      int idEquipoVisitante = leerEntero(request, "equipoVisitante", "Debe seleccionar el equipo visitante.");

      if (idEquipoLocal == idEquipoVisitante) {
        throw new IllegalArgumentException("El equipo local y el visitante no pueden ser el mismo.");
      }

      completarEquipos(idPartido, idEquipoLocal, idEquipoVisitante);
      request.setAttribute("mensajeExito", "El partido se cargo correctamente.");
    } catch (IllegalArgumentException e) {
      request.setAttribute("mensajeError", e.getMessage());
    } catch (RuntimeException e) {
      request.setAttribute("mensajeError", "No se pudo guardar el partido en la base de datos.");
    }

    cargarFormulario(request, response);
  }

  private void cargarFormulario(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      request.setAttribute("partidosPendientes", obtenerPartidosPendientes());
      request.setAttribute("equipos", catalogoDAO.obtenerEquipos());
    } catch (RuntimeException e) {
      request.setAttribute("partidosPendientes", Collections.emptyList());
      request.setAttribute("equipos", Collections.emptyList());
      request.setAttribute("mensajeError", e.getMessage());
    }

    request.getRequestDispatcher("cargarPartido.jsp").forward(request, response);
  }

  private List<Partido> obtenerPartidosPendientes() {
    List<Partido> partidos = new ArrayList<>();

    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_PARTIDOS_PENDIENTES);
         ResultSet rs = pst.executeQuery()) {

      while (rs.next()) {
        Etapa etapa = new Etapa();
        etapa.setIdEtapa(rs.getInt("idEtapa"));
        etapa.setNombreEtapa(rs.getString("nombreEtapa"));

        Estadio estadio = new Estadio();
        estadio.setIdEstadio(rs.getInt("idEstadio"));
        estadio.setEstadio(rs.getString("estadio_nombre"));
        estadio.setCiudad(rs.getString("ciudad"));
        estadio.setPais(rs.getString("pais"));

        Partido partido = new Partido();
        partido.setIdPartido(rs.getInt("idPartido"));
        partido.setEtapa(etapa);
        partido.setEstadio(estadio);
        partido.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());

        partidos.add(partido);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener los partidos pendientes.", e);
    }

    return partidos;
  }

  private void completarEquipos(int idPartido, int idEquipoLocal, int idEquipoVisitante) {
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_COMPLETAR_EQUIPOS)) {

      pst.setInt(1, idEquipoLocal);
      pst.setInt(2, idEquipoVisitante);
      pst.setInt(3, idPartido);
      pst.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error al completar los equipos del partido.", e);
    }
  }

  private int leerEntero(HttpServletRequest request, String parametro, String mensajeError) {
    String valor = request.getParameter(parametro);

    if (valor == null || valor.trim().isEmpty()) {
      throw new IllegalArgumentException(mensajeError);
    }

    try {
      return Integer.parseInt(valor);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(mensajeError);
    }
  }
}
