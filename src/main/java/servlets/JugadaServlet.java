package servlets;

import dao.JugadaDAO;
import entities.Jugada;
import entities.Partido;
import entities.Usuario;
import exceptions.FormatoGolesInvalidoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    if (usuarioLogueado == null) {
      response.sendRedirect("formLogin.jsp");
      return;
    }

    int pronosticosGuardados = 0;
    List<String> errores = new ArrayList<>();
    int jornadaActual = 1;

    try {
      // Capturamos la jornada actual del input oculto
      String jornadaActualStr = request.getParameter("jornadaActual");
      if (jornadaActualStr != null && !jornadaActualStr.isEmpty()) {
        jornadaActual = Integer.parseInt(jornadaActualStr);
      }

      // Traemos todos los partidos para validar sin importar qué jornada viaje en el input
      List<Partido> partidosDeLaJornada = jugadaDAO.getAllPartidos();

      // STREAMS: Obtenemos todos los parámetros del request, filtramos los goles locales y extraemos el ID
      List<Integer> idsPartidosFormulario = java.util.Collections.list(request.getParameterNames()).stream()
          .filter(param -> param.startsWith("golesL_"))
          .map(param -> Integer.parseInt(param.split("_")[1])) // Cortamos "golesL_12" para quedarnos con el "12"
          .collect(java.util.stream.Collectors.toList());

      LocalDateTime ahora = LocalDateTime.now();

      // Procesamos cada partido que vino en el formulario
      for (Integer idPartido : idsPartidosFormulario) {
        try {
          String strGolesL = request.getParameter("golesL_" + idPartido);
          String strGolesV = request.getParameter("golesV_" + idPartido);

          // Si el usuario borró el número y lo dejó en blanco, saltamos este partido
          if (strGolesL == null || strGolesL.isEmpty() || strGolesV == null || strGolesV.isEmpty()) {
            continue;
          }

          int golesLocal = Integer.parseInt(strGolesL);
          int golesVisitante = Integer.parseInt(strGolesV);

          if (golesLocal < 0 || golesVisitante < 0) {
            throw new FormatoGolesInvalidoException("Los goles no pueden ser negativos.");
          }

          // STREAMS: Buscamos el partido específico en la lista global en memoria
          Partido partidoInfo = partidosDeLaJornada.stream()
              .filter(p -> p.getIdPartido() == idPartido)
              .findFirst()
              .orElse(null);

          if (partidoInfo == null) continue;

          // Validación de tiempo límite (15 minutos antes)
          if (ahora.isAfter(partidoInfo.getFechaHora().minusMinutes(15))) {
            continue;
          }

          // Consultamos si el usuario ya tenía una jugada para este partido
          Jugada jugadaExistente = jugadaDAO.getByUserAndMatch(usuarioLogueado.getIdUsuario(), idPartido);

          if (jugadaExistente != null) {
            // Solo hacemos UPDATE si el usuario realmente cambió los números
            if (jugadaExistente.getGolesLocal() != golesLocal || jugadaExistente.getGolesVisitante() != golesVisitante) {
              jugadaExistente.setGolesLocal(golesLocal);
              jugadaExistente.setGolesVisitante(golesVisitante);
              jugadaDAO.update(jugadaExistente);
              pronosticosGuardados++;
            }
          } else {
            // Jugada totalmente nueva
            Jugada nueva = new Jugada();
            nueva.setUsuario(usuarioLogueado);
            Partido p = new Partido();
            p.setIdPartido(idPartido);
            nueva.setPartido(p);
            nueva.setGolesLocal(golesLocal);
            nueva.setGolesVisitante(golesVisitante);

            jugadaDAO.insert(nueva);
            pronosticosGuardados++;
          }

        } catch (NumberFormatException e) {
          errores.add("Formato inválido en el partido " + idPartido);
        } catch (FormatoGolesInvalidoException e) {
          errores.add("Partido " + idPartido + ": " + e.getMessage());
        }
      }

      // Preparamos los mensajes de feedback guardándolos en sesión (para no perderlos en el redirect)
      HttpSession session = request.getSession();
      if (pronosticosGuardados > 0) {
        session.setAttribute("mensajeSession", "¡Se guardaron " + pronosticosGuardados + " pronósticos correctamente!");
      } else if (errores.isEmpty()) {
        session.setAttribute("mensajeSession", "No hubo cambios nuevos para guardar.");
      }

      if (!errores.isEmpty()) {
        session.setAttribute("errorSession", "Inconvenientes detectados: " + String.join(", ", errores));
      }

    } catch (Exception e) {
      request.getSession().setAttribute("errorSession", "Error general en el guardado: " + e.getMessage());
    }

    // FIX VITAL: Redirección limpia pasando la jornada para evitar cruce de datos con la jornada 1
    response.sendRedirect("jugar?jornada=" + jornadaActual);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("formLogin.jsp");
      return;
    }

    try {
      // Bajamos los mensajes temporales de la sesión al request (para los Toasts) y los limpiamos
      HttpSession session = request.getSession();
      if (session.getAttribute("mensajeSession") != null) {
        request.setAttribute("mensaje", session.getAttribute("mensajeSession"));
        session.removeAttribute("mensajeSession");
      }
      if (session.getAttribute("errorSession") != null) {
        request.setAttribute("error", session.getAttribute("errorSession"));
        session.removeAttribute("errorSession");
      }

      // 1. Carga de Puntos
      int puntosTotales = jugadaDAO.getPuntosTotalesPorUsuario(usuarioLogueado.getIdUsuario());
      request.setAttribute("puntosTotales", puntosTotales);

      // 2. Agrupamiento de jornadas por fecha
      List<Partido> todosLosPartidos = jugadaDAO.getAllPartidos();

      Map<LocalDate, List<Partido>> jornadasPorFecha = todosLosPartidos.stream()
          .collect(Collectors.groupingBy(
              p -> p.getFechaHora().toLocalDate(),
              TreeMap::new,
              Collectors.toList()
          ));

      List<LocalDate> fechasDisponibles = new ArrayList<>(jornadasPorFecha.keySet());

      // 3. Determinar Jornada a mostrar
      String jParam = request.getParameter("jornada");
      int indiceJornada = (jParam != null && !jParam.isEmpty()) ? Integer.parseInt(jParam) : 1;

      if (!fechasDisponibles.isEmpty()) {
        int index = Math.max(1, Math.min(indiceJornada, fechasDisponibles.size())) - 1;
        LocalDate fechaSeleccionada = fechasDisponibles.get(index);

        // Obtenemos los partidos de esa fecha
        List<Partido> partidosHoy = jornadasPorFecha.get(fechaSeleccionada);

        // Sacamos el ID de la etapa real del primer partido de esa lista
        int idEtapaReal = partidosHoy.get(0).getEtapa().getIdEtapa();

        // Pedimos las jugadas usando el ID real de la etapa de la DB
        List<Jugada> listaJugadas = jugadaDAO.getByEtapa(usuarioLogueado.getIdUsuario(), idEtapaReal);

        // Mapeamos a un Map para que el JSP consulte directamente por ID de partido
        Map<Integer, Jugada> mapaJugadas = listaJugadas.stream()
            .collect(Collectors.toMap(
                j -> j.getPartido().getIdPartido(),
                j -> j,
                (j1, j2) -> j1
            ));

        // Enviamos todo empaquetado al JSP
        request.setAttribute("listaPartidosJornada", partidosHoy);
        request.setAttribute("mapaJugadas", mapaJugadas); // <--- FIX VITAL: Enviado con éxito al request
        request.setAttribute("listaJugadas", listaJugadas);
        request.setAttribute("jornadaSeleccionada", index + 1);
        request.setAttribute("totalJornadas", fechasDisponibles.size());
        request.setAttribute("fechaActualLabel", fechaSeleccionada);
      }

      request.setAttribute("tiempoLimite", LocalDateTime.now().plusMinutes(15));

      request.getRequestDispatcher("jugar.jsp").forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar: " + e.getMessage());
    }
  }
}