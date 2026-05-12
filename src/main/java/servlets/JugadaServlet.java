package servlets;

import dao.JugadaDAO;
import entities.Jugada;
import entities.Partido;
import entities.Usuarios;
import exceptions.FormatoGolesInvalidoException;
import exceptions.PartidoYaComenzadoException;
import exceptions.TiempoLimiteException;
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

    if (usuarioLogueado == null) {
      response.sendRedirect("login.jsp");
      return;
    }

    try {
      // 1. VALIDACIÓN DE FORMATO (FormatoGolesInvalidoException)
      int idPartido;
      int golesLocal;
      int golesVisitante;

      try {
        idPartido = Integer.parseInt(request.getParameter("idPartido"));
        golesLocal = Integer.parseInt(request.getParameter("golesLocal"));
        golesVisitante = Integer.parseInt(request.getParameter("golesVisitante"));

        // Validamos que no metan goles negativos inspeccionando el HTML
        if (golesLocal < 0 || golesVisitante < 0) {
          throw new FormatoGolesInvalidoException("Los goles no pueden ser números negativos.");
        }
      } catch (NumberFormatException e) {
        throw new FormatoGolesInvalidoException("Por favor, ingresa valores numéricos válidos para los goles.");
      }

      // 2. LÓGICA DE NEGOCIO Y TIEMPOS
      Jugada jugadaExistente = jugadaDAO.getByUserAndMatch(usuarioLogueado.getIdUsuario(), idPartido);
      LocalDateTime ahora = LocalDateTime.now();

      if (jugadaExistente != null) {
        LocalDateTime horaPartido = jugadaExistente.getPartido().getFechaHora();

        // Validamos si el partido ya arrancó (PartidoYaComenzadoException)
        if (ahora.isAfter(horaPartido)) {
          throw new PartidoYaComenzadoException("El partido ya ha comenzado. No puedes modificar tu pronóstico.");
        }

        // Validamos la regla de los 15 minutos (TiempoLimiteException)
        if (ahora.isAfter(horaPartido.minusMinutes(15))) {
          throw new TiempoLimiteException("El tiempo límite para editar expiró. Solo puedes modificar hasta 15 minutos antes del partido.");
        }

        // ESCENARIO 2: Pasó todas las validaciones, actualizamos
        jugadaExistente.setGolesLocal(golesLocal);
        jugadaExistente.setGolesVisitante(golesVisitante);
        jugadaDAO.update(jugadaExistente);
        request.setAttribute("mensaje", "¡Pronóstico modificado con éxito!");

      } else {
        // Lógica original de Inserción para jugadas nuevas
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

    } catch (FormatoGolesInvalidoException | PartidoYaComenzadoException | TiempoLimiteException e) {
      request.setAttribute("error", e.getMessage());
    } catch (Exception e) {
      request.setAttribute("error", "Ocurrió un error en el sistema: " + e.getMessage());
    }

    // Volver a procesar la carga de datos para mostrar la pantalla
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