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

    try {
      // Capturamos la jornada actual del input oculto
      String jornadaActualStr = request.getParameter("jornadaActual");
      int jornadaActual = (jornadaActualStr != null && !jornadaActualStr.isEmpty()) ? Integer.parseInt(jornadaActualStr) : 1;

      // Traemos los partidos de esta fecha de una sola vez para no sobrecargar la base de datos
      List<Partido> partidosDeLaJornada = jugadaDAO.getPartidosByJornada(jornadaActual);

      // STREAMS: Obtenemos todos los parámetros del request, filtramos los goles locales y extraemos el ID
      List<Integer> idsPartidosFormulario = java.util.Collections.list(request.getParameterNames()).stream()
          .filter(param -> param.startsWith("golesL_"))
          .map(param -> Integer.parseInt(param.split("_")[1])) // Cortamos "golesL_12" para quedarnos con el "12"
          .collect(java.util.stream.Collectors.toList());

      LocalDateTime ahora = LocalDateTime.now();

      //  Procesamos cada partido que vino en el formulario
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

          // STREAMS: Buscamos el partido específico en la lista que ya tenemos en memoria
          Partido partidoInfo = partidosDeLaJornada.stream()
              .filter(p -> p.getIdPartido() == idPartido)
              .findFirst()
              .orElse(null);

          if (partidoInfo == null) continue;

          // En vez de lanzar la excepción y frenar todo, ignoramos los partidos bloqueados.
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

      // Preparamos los mensajes de feedback dinámicos para la vista
      if (pronosticosGuardados > 0) {
        request.setAttribute("mensaje", "¡Se guardaron " + pronosticosGuardados + " pronósticos correctamente!");
      } else if (errores.isEmpty()) {
        request.setAttribute("mensaje", "No hubo cambios nuevos para guardar.");
      }

      if (!errores.isEmpty()) {
        request.setAttribute("error", "Inconvenientes detectados: " + String.join(", ", errores));
      }

    } catch (Exception e) {
      request.setAttribute("error", "Error general en el guardado: " + e.getMessage());
    }

    // Volvemos a procesar la pantalla
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("formLogin.jsp");
      return;
    }

    try {
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

      // de aca -->
      if (!fechasDisponibles.isEmpty()) {
        int index = Math.max(1, Math.min(indiceJornada, fechasDisponibles.size())) - 1;
        LocalDate fechaSeleccionada = fechasDisponibles.get(index);

        // 1. Obtenemos los partidos de esa fecha
        List<Partido> partidosHoy = jornadasPorFecha.get(fechaSeleccionada);

        // 2. IMPORTANTE: Sacamos el ID de la etapa del primer partido de esa lista
        // Esto garantiza que el filtro sea el correcto sin importar el índice
        int idEtapaReal = partidosHoy.get(0).getEtapa().getIdEtapa();

        // 3. Pedimos las jugadas usando el ID real de la base de datos
        List<Jugada> listaJugadas = jugadaDAO.getByEtapa(usuarioLogueado.getIdUsuario(), idEtapaReal);

        // 4. Mapeamos
        Map<Integer, Jugada> mapaJugadas = listaJugadas.stream()
                .collect(Collectors.toMap(
                        j -> j.getPartido().getIdPartido(),
                        j -> j,
                        (j1, j2) -> j1
                ));

        // hasta aca <--

        request.setAttribute("listaPartidosJornada", jornadasPorFecha.get(fechaSeleccionada));
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