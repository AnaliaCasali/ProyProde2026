package servlets;

import dao.JugadaDAO;
import dao.PartidoDAO; // <-- Importamos el DAO de partidos
import entities.Etapa;
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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/jugar")
public class JugadaServlet extends HttpServlet {

  private JugadaDAO jugadaDAO = new JugadaDAO();
  private PartidoDAO partidoDAO = new PartidoDAO(); // <-- Instanciamos el DAO de tu colega

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("formLogin.jsp");
      return;
    }

    try {
      // 1. Carga de Puntos Históricos Totales
      int puntosTotales = jugadaDAO.getPuntosTotalesPorUsuario(usuarioLogueado.getIdUsuario());
      request.setAttribute("puntosTotales", puntosTotales);

      // 2. Agrupamiento de jornadas por fecha (Cambiado para usar el DAO correcto)
      List<Partido> todosLosPartidos = partidoDAO.getAll();

      Map<LocalDate, List<Partido>> jornadasPorFecha = todosLosPartidos.stream()
          .collect(Collectors.groupingBy(
              p -> p.getFechaHora().toLocalDate(),
              TreeMap::new,
              Collectors.toList()
          ));

      List<LocalDate> fechasDisponibles = new ArrayList<>(jornadasPorFecha.keySet());

      // 3. Determinar Jornada a mostrar
      String jParam = request.getParameter("jornada");
      if (jParam == null || jParam.isEmpty()) {
        jParam = request.getParameter("jornadaActual");
      }
      int indiceJornada = (jParam != null && !jParam.isEmpty()) ? Integer.parseInt(jParam) : 1;

      if (!fechasDisponibles.isEmpty()) {
        int index = Math.max(1, Math.min(indiceJornada, fechasDisponibles.size())) - 1;
        LocalDate fechaSeleccionada = fechasDisponibles.get(index);

        // Obtenemos los partidos reales de esa fecha en memoria
        List<Partido> partidosHoy = jornadasPorFecha.get(fechaSeleccionada);

        // Sacamos el ID de la etapa real del primer partido de esa lista
        int idEtapaReal = partidosHoy.get(0).getEtapa().getIdEtapa();

        // 1. Traemos el pool completo de jugadas de la etapa (fase de grupos completa)
        List<Jugada> jugadasDeLaEtapa = jugadaDAO.getByEtapa(usuarioLogueado.getIdUsuario(), idEtapaReal);

        // 2. Armamos un Set rápido con los IDs de los partidos que corresponden a HOY
        Set<Integer> idsPartidosDeHoy = partidosHoy.stream()
            .map(Partido::getIdPartido)
            .collect(Collectors.toSet());

        // 3. FILTRADO: Dejamos en 'listaJugadas' únicamente las que correspondan a la fecha actual
        List<Jugada> listaJugadas = jugadasDeLaEtapa.stream()
            .filter(j -> idsPartidosDeHoy.contains(j.getPartido().getIdPartido()))
            .collect(Collectors.toList());

        // Sumamos los puntos específicos que el usuario ganó SOLO en esta jornada
        int puntosDeLaJornada = listaJugadas.stream()
            .mapToInt(Jugada::getPuntaje)
            .sum();

        // Mapeamos a un Map por ID de partido para el JSP
        Map<Integer, Jugada> mapaJugadas = listaJugadas.stream()
            .collect(Collectors.toMap(
                j -> j.getPartido().getIdPartido(),
                j -> j,
                (j1, j2) -> j1
            ));

        // Enviamos todo empaquetado al request para el JSP
        request.setAttribute("listaPartidosJornada", partidosHoy);
        request.setAttribute("mapaJugadas", mapaJugadas);
        request.setAttribute("listaJugadas", listaJugadas);
        request.setAttribute("puntosJornada", puntosDeLaJornada);
        request.setAttribute("jornadaSeleccionada", index + 1);
        request.setAttribute("totalJornadas", fechasDisponibles.size());
        request.setAttribute("fechaActualLabel", fechaSeleccionada);
      }

      request.setAttribute("tiempoLimite", LocalDateTime.now().plusMinutes(15));
      request.getRequestDispatcher("jugar.jsp").forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la vista: " + e.getMessage());
    }
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

    // Capturamos la jornada actual desde el formulario para no perder la posición
    String jornadaActualStr = request.getParameter("jornadaActual");
    int jornadaActual = (jornadaActualStr != null && !jornadaActualStr.isEmpty()) ? Integer.parseInt(jornadaActualStr) : 1;

    try {
      // Conseguimos el ID relacional de la etapa usando el truco de la fecha (Cambiado para usar partidoDAO)
      List<Partido> todosLosPartidos = partidoDAO.getAll();
      Map<LocalDate, List<Partido>> jornadasPorFecha = todosLosPartidos.stream()
          .collect(Collectors.groupingBy(p -> p.getFechaHora().toLocalDate(), TreeMap::new, Collectors.toList()));
      List<LocalDate> fechasDisponibles = new ArrayList<>(jornadasPorFecha.keySet());

      int index = Math.max(1, Math.min(jornadaActual, fechasDisponibles.size())) - 1;
      LocalDate fechaSeleccionada = fechasDisponibles.get(index);
      List<Partido> partidosDeLaJornada = jornadasPorFecha.get(fechaSeleccionada);
      int idEtapaReal = partidosDeLaJornada.get(0).getEtapa().getIdEtapa();

      List<Integer> idsPartidosFormulario = java.util.Collections.list(request.getParameterNames()).stream()
          .filter(param -> param.startsWith("golesL_"))
          .map(param -> Integer.parseInt(param.split("_")[1]))
          .collect(Collectors.toList());

      LocalDateTime ahora = LocalDateTime.now();

      for (Integer idPartido : idsPartidosFormulario) {
        try {
          String strGolesL = request.getParameter("golesL_" + idPartido);
          String strGolesV = request.getParameter("golesV_" + idPartido);

          if (strGolesL == null || strGolesL.isEmpty() || strGolesV == null || strGolesV.isEmpty()) {
            continue;
          }

          int golesLocal = Integer.parseInt(strGolesL);
          int golesVisitante = Integer.parseInt(strGolesV);

          if (golesLocal < 0 || golesVisitante < 0) {
            throw new FormatoGolesInvalidoException("Los goles no pueden ser negativos.");
          }

          Partido partidoInfo = partidosDeLaJornada.stream()
              .filter(p -> p.getIdPartido() == idPartido)
              .findFirst()
              .orElse(null);

          if (partidoInfo == null) continue;

          boolean partidoYaEmpezoOLlegoLimite = ahora.isAfter(partidoInfo.getFechaHora().minusMinutes(15));

          // Corregido para mapear directo con la propiedad real de Partido.java (isFinalizado)
          if (partidoInfo.isFinalizado() || partidoYaEmpezoOLlegoLimite) {
            continue;
          }

          String strGolesLForm = request.getParameter("golesL_" + idPartido);
          String strGolesVForm = request.getParameter("golesV_" + idPartido);

          Jugada jugadaExistente = jugadaDAO.getByUserAndMatch(usuarioLogueado.getIdUsuario(), idPartido);

          if (jugadaExistente != null) {
            if (jugadaExistente.getGolesLocal() != golesLocal || jugadaExistente.getGolesVisitante() != golesVisitante) {
              jugadaExistente.setGolesLocal(golesLocal);
              jugadaExistente.setGolesVisitante(golesVisitante);
              jugadaDAO.update(jugadaExistente);
              pronosticosGuardados++;
            }
          } else {
            Jugada nueva = new Jugada();
            nueva.setUsuario(usuarioLogueado);
            nueva.setPartido(partidoInfo);
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

    doGet(request, response);
  }
}