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

  // Método auxiliar para no repetir código de carga de puntos y lista
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

      // Carga de jugadas (Para que el usuario vea lo que acaba de guardar)
      List<Jugada> lista = jugadaDAO.getAll();
      request.setAttribute("listaJugadas", lista);

      request.getRequestDispatcher("jugar.jsp").forward(request, response);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}