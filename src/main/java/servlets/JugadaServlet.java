package servlets;

import dao.JugadaDAO;
import entities.Jugada;
import entities.Partido;
import entities.Usuario;
import exceptions.FormatoGolesInvalidoException;
import exceptions.PartidoYaComenzadoException;
import exceptions.TiempoLimiteException;
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

        if (golesLocal < 0 || golesVisitante < 0) {
          throw new FormatoGolesInvalidoException("Los goles no pueden ser números negativos.");
        }
      } catch (NumberFormatException e) {
        throw new FormatoGolesInvalidoException("Por favor, ingresa valores numéricos válidos para los goles.");
      }

      // 2. LÓGICA DE NEGOCIO Y TIEMPOS (Escenarios 2 y 3)
      Jugada jugadaExistente = jugadaDAO.getByUserAndMatch(usuarioLogueado.getIdUsuario(), idPartido);
      LocalDateTime ahora = LocalDateTime.now();

      // Buscamos el partido para validar la hora (si no existe la jugada, lo buscamos del DAO)
      Partido partidoInfo = (jugadaExistente != null) ?
          jugadaExistente.getPartido() :
          jugadaDAO.getPartidosByJornada(1).stream() // Búsqueda rápida por ID
          .filter(p -> p.getIdPartido() == idPartido)
          .findFirst().orElse(null);

      if (partidoInfo != null) {
        LocalDateTime horaPartido = partidoInfo.getFechaHora();

        if (ahora.isAfter(horaPartido)) {
          throw new PartidoYaComenzadoException("El partido ya ha comenzado. No puedes modificar tu pronóstico.");
        }

        if (ahora.isAfter(horaPartido.minusMinutes(15))) {
          throw new TiempoLimiteException("El tiempo límite expiró. Solo puedes modificar hasta 15 min antes.");
        }
      }

      if (jugadaExistente != null) {
        // ACTUALIZACIÓN (Escenario 2)
        jugadaExistente.setGolesLocal(golesLocal);
        jugadaExistente.setGolesVisitante(golesVisitante);
        jugadaDAO.update(jugadaExistente);
        request.setAttribute("mensaje", "¡Pronóstico modificado con éxito!");
      } else {
        // INSERCIÓN
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
      request.setAttribute("error", "Error en el sistema: " + e.getMessage());
    }

    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    if (usuarioLogueado == null) {
      response.sendRedirect("login.jsp");
      return;
    }

    try {
      // 1. Carga de Puntos
      int puntosTotales = jugadaDAO.getPuntosTotalesPorUsuario(usuarioLogueado.getIdUsuario());
      request.setAttribute("puntosTotales", puntosTotales);

      // 2. Agrupamiento de jornadas por fecha (Lógica de tu compañero con Streams)
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

        // ESCENARIO 1: Carga de jugadas del usuario para pre-completar los campos
        // Usamos el index+1 como idEtapa para sincronizar con tu DAO
        List<Jugada> listaJugadas = jugadaDAO.getByEtapa(usuarioLogueado.getIdUsuario(), index + 1);

        request.setAttribute("listaPartidosJornada", jornadasPorFecha.get(fechaSeleccionada));
        request.setAttribute("listaJugadas", listaJugadas);
        request.setAttribute("jornadaSeleccionada", index + 1);
        request.setAttribute("totalJornadas", fechasDisponibles.size());
        request.setAttribute("fechaActualLabel", fechaSeleccionada);
      }

      request.getRequestDispatcher("jugar.jsp").forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar: " + e.getMessage());
    }
  }
}