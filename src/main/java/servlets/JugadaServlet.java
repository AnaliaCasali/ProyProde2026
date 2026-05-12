package servlets;

import dao.JugadaDAO;
import entities.Jugada;
import entities.Partido;
import entities.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/jugar")
public class JugadaServlet extends HttpServlet {

  private JugadaDAO jugadaDAO = new JugadaDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    // Capturar datos del formulario
    int idPartido = Integer.parseInt(request.getParameter("idPartido"));
    int golesLocal = Integer.parseInt(request.getParameter("golesLocal"));
    int golesVisitante = Integer.parseInt(request.getParameter("golesVisitante"));

    try {
      Jugada nueva = new Jugada();
      nueva.setUsuario(usuarioLogueado);

      Partido p = new Partido();
      p.setIdPartido(idPartido);
      nueva.setPartido(p);

      nueva.setGolesLocal(golesLocal);
      nueva.setGolesVisitante(golesVisitante);

      // Ejecutar la funcionalidad: REGISTRAR
      jugadaDAO.insert(nueva);

      // Notificar éxito
      request.setAttribute("mensaje", "¡Jugada registrada con éxito!");

    } catch (Exception e) {
      request.setAttribute("error", "No se pudo registrar la jugada: " + e.getMessage());
    }

    // IMPORTANTE: Volver a procesar la carga de datos para mostrar el banner actualizado
    processRequest(request, response);
  }

  /**
   * Método auxiliar encargado de centralizar la carga de datos para la vista jugar.jsp.
   * Se utiliza tanto en doGet como en doPost para asegurar que la vista siempre tenga
   * la información actualizada de puntos, jornadas y partidos.
   */
  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("login.jsp");
      return;
    }

    try {
      // 1. Carga de Puntos y Datos Base
      int puntosTotales = jugadaDAO.getPuntosTotalesPorUsuario(usuarioLogueado.getIdUsuario());
      request.setAttribute("puntosTotales", puntosTotales);

      // 2. Obtener todos los partidos y agrupar por FECHA (ignorando la hora)
      List<Partido> todos = jugadaDAO.getAllPartidos();

      // Agrupamos en un TreeMap (para mantener el orden cronológico de los días)
      Map<LocalDate, List<Partido>> jornadasPorFecha = todos.stream()
          .collect(java.util.stream.Collectors.groupingBy(
              p -> p.getFechaHora().toLocalDate(),
              java.util.TreeMap::new,
              java.util.stream.Collectors.toList()
          ));

      // Convertimos las llaves (fechas) en una lista para acceder por índice
      List<java.time.LocalDate> fechasDisponibles = new ArrayList<>(jornadasPorFecha.keySet());

      // 3. Determinar qué "Día" (Jornada) mostrar
      String jParam = request.getParameter("jornada");
      int indiceJornada = (jParam != null) ? Integer.parseInt(jParam) : 1;

      // Validamos que el índice esté dentro del rango de días detectados
      if (!fechasDisponibles.isEmpty()) {
        // Ajustamos el índice (Jornada 1 = índice 0)
        int index = Math.max(1, Math.min(indiceJornada, fechasDisponibles.size())) - 1;
        java.time.LocalDate fechaSeleccionada = fechasDisponibles.get(index);

        // Enviamos los partidos de ese día específico
        request.setAttribute("listaPartidosJornada", jornadasPorFecha.get(fechaSeleccionada));
        request.setAttribute("jornadaSeleccionada", index + 1);
        request.setAttribute("totalJornadas", fechasDisponibles.size());
        request.setAttribute("fechaActualLabel", fechaSeleccionada);
      }

      request.getRequestDispatcher("jugar.jsp").forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar jornadas: " + e.getMessage());
    }
  }
}