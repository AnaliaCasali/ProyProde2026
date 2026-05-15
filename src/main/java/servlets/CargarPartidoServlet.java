package servlets;

import dao.CatalogoDAO;
import dao.PartidoDAO;
import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import entities.Partido;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;

@WebServlet("/cargar-partido")
public class CargarPartidoServlet extends HttpServlet {

  private final CatalogoDAO catalogoDAO = new CatalogoDAO();
  private final PartidoDAO partidoDAO = new PartidoDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    cargarFormulario(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      Partido partido = crearPartidoDesdeRequest(request);
      partidoDAO.insert(partido);
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
      request.setAttribute("equipos", catalogoDAO.obtenerEquipos());
      request.setAttribute("etapas", catalogoDAO.obtenerEtapasEliminatorias());
      request.setAttribute("estadios", catalogoDAO.obtenerEstadios());
    } catch (RuntimeException e) {
      request.setAttribute("equipos", Collections.emptyList());
      request.setAttribute("etapas", Collections.emptyList());
      request.setAttribute("estadios", Collections.emptyList());
      request.setAttribute("mensajeError", e.getMessage());
    }

    request.getRequestDispatcher("cargarPartido.jsp").forward(request, response);
  }

  private Partido crearPartidoDesdeRequest(HttpServletRequest request) {
    int idEquipoLocal = leerEntero(request, "equipoLocal", "Debe seleccionar el equipo local.");
    int idEquipoVisitante = leerEntero(request, "equipoVisitante", "Debe seleccionar el equipo visitante.");
    int idEtapa = leerEntero(request, "idEtapa", "Debe seleccionar una etapa.");
    int idEstadio = leerEntero(request, "idEstadio", "Debe seleccionar un estadio.");
    LocalDateTime fechaHora = leerFechaHora(request.getParameter("fechaHora"));

    if (idEquipoLocal == idEquipoVisitante) {
      throw new IllegalArgumentException("El equipo local y el visitante no pueden ser el mismo.");
    }

    Equipo local = new Equipo();
    local.setIdEquipo(idEquipoLocal);

    Equipo visitante = new Equipo();
    visitante.setIdEquipo(idEquipoVisitante);

    Etapa etapa = new Etapa();
    etapa.setIdEtapa(idEtapa);

    Estadio estadio = new Estadio();
    estadio.setIdEstadio(idEstadio);

    Partido partido = new Partido();
    partido.setEquipoLocal(local);
    partido.setEquipoVisitante(visitante);
    partido.setEtapa(etapa);
    partido.setEstadio(estadio);
    partido.setFechaHora(fechaHora);
    partido.setGolesLocal(0);
    partido.setGolesVisitante(0);
    partido.setFinalizado(false);

    return partido;
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

  private LocalDateTime leerFechaHora(String valor) {
    if (valor == null || valor.trim().isEmpty()) {
      throw new IllegalArgumentException("Debe ingresar la fecha y hora del partido.");
    }

    try {
      LocalDateTime fechaHora = LocalDateTime.parse(valor);

      if (fechaHora.isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("No se puede cargar el partido porque la fecha y hora ingresada es anterior a la actual.");
      }

      return fechaHora;
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("La fecha y hora ingresada no es valida.");
    }
  }
}
