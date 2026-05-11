package servlets;

import dao.JugadaDAO;
import entities.Jugada;
import entities.Partido;
import entities.Usuarios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/jugar")
public class JugadaServlet extends HttpServlet {

  private JugadaDAO jugadaDAO = new JugadaDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuarios usuarioLogueado = (Usuarios) request.getSession().getAttribute("usuario");

    // Si la sesión expiró por algún motivo durante el POST
    if (usuarioLogueado == null) {
      response.sendRedirect("login.jsp");
      return;
    }

    // Capturar datos del formulario
    int idPartido = Integer.parseInt(request.getParameter("idPartido"));
    int golesLocal = Integer.parseInt(request.getParameter("golesLocal"));
    int golesVisitante = Integer.parseInt(request.getParameter("golesVisitante"));

    try {
      // 1. Buscamos si el usuario ya tiene un pronóstico para este partido (Escenario 1 y 2)
      Jugada jugadaExistente = jugadaDAO.getByUserAndMatch(usuarioLogueado.getIdUsuario(), idPartido);
      LocalDateTime ahora = LocalDateTime.now();

      if (jugadaExistente != null) {
        // ESCENARIO 3: Validar que el partido NO haya empezado
        if (jugadaExistente.getPartido().getFechaHora().isAfter(ahora)) {

          // ESCENARIO 2: Modificación permitida
          jugadaExistente.setGolesLocal(golesLocal);
          jugadaExistente.setGolesVisitante(golesVisitante);
          jugadaDAO.update(jugadaExistente);
          request.setAttribute("mensaje", "¡Pronóstico modificado con éxito!");

        } else {
          // El partido ya empezó, bloqueamos la edición
          request.setAttribute("error", "Error: El partido ya ha comenzado. No puedes modificar tu pronóstico.");
        }
      } else {
        // Si no existía, es una jugada nueva (Lógica original de Heber)
        Jugada nueva = new Jugada();
        nueva.setUsuario(usuarioLogueado);

        Partido p = new Partido();
        p.setIdPartido(idPartido);
        nueva.setPartido(p);

        nueva.setGolesLocal(golesLocal);
        nueva.setGolesVisitante(golesVisitante);

        jugadaDAO.insert(nueva);
        request.setAttribute("mensaje", "¡Jugada registrada con éxito!");
      }

    } catch (Exception e) {
      request.setAttribute("error", "Ocurrió un error: " + e.getMessage());
    }

    // Volver a procesar la carga de datos para mostrar la pantalla actualizada
    processRequest(request, response);
  }

  // Método auxiliar para cargar los datos que van al JSP
  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuarios usuarioLogueado = (Usuarios) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("login.jsp");
      return;
    }

    try {
      // Carga de puntos para el banner
      int puntosTotales = jugadaDAO.getPuntosTotalesPorUsuario(usuarioLogueado.getIdUsuario());
      request.setAttribute("puntosTotales", puntosTotales);

      // Carga de la jornada que se quiere ver (Por defecto ponemos 1 si no viene en la URL)
      String idEtapaParam = request.getParameter("idEtapa");
      int idEtapa = (idEtapaParam != null && !idEtapaParam.isEmpty()) ? Integer.parseInt(idEtapaParam) : 1;

      // ESCENARIO 1: Recuperación de datos GUARDADOS DEL USUARIO para la etapa actual
      // (Reemplazamos el jugadaDAO.getAll() por getByEtapa, no estoy seguro asi que preguntar)
      List<Jugada> lista = jugadaDAO.getByEtapa(usuarioLogueado.getIdUsuario(), idEtapa);

      request.setAttribute("listaJugadas", lista);
      request.setAttribute("etapaActual", idEtapa); // Para que el JSP sepa en qué fecha estamos

      request.getRequestDispatcher("jugar.jsp").forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}