package servlets;

import dao.PartidoDAO;
import dao.UsuarioImpl;
import entities.Partido;
import entities.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {

  private UsuarioImpl usuarioDAO = new UsuarioImpl();
  private PartidoDAO partidoDAO = new PartidoDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // 1. OBTENER TOP 3 DEL RANKING
    List<Usuario> rankingCompleto = usuarioDAO.obtenerRankingUsuarios();
    List<Usuario> top3Usuarios = rankingCompleto.stream()
        .limit(3)
        .collect(Collectors.toList());
    request.setAttribute("topUsuarios", top3Usuarios);

    // 2. OBTENER PRÓXIMAS JORNADAS (Partidos no finalizados)
    List<Partido> todosLosPartidos = partidoDAO.getAll();

    // Agrupamos cronológicamente usando TreeMap
    Map<LocalDate, List<Partido>> agrupadosCronologicamente = todosLosPartidos.stream()
        .filter(p -> !p.isFinalizado())
        .collect(Collectors.groupingBy(
            p -> p.getFechaHora().toLocalDate(),
            TreeMap::new,
            Collectors.toList()
        ));

    // Formateador para convertir la fecha a texto "dd/MM"
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    // Usamos LinkedHashMap para mantener el orden de los 3 primeros,
    // pero ahora la clave es un String ya formateado listo para el JSP.
    Map<String, List<Partido>> proximas3Jornadas = new LinkedHashMap<>();

    agrupadosCronologicamente.entrySet().stream()
        .limit(3)
        .forEach(entry -> {
          String fechaFormateada = entry.getKey().format(formatter);
          proximas3Jornadas.put(fechaFormateada, entry.getValue());
        });

    request.setAttribute("proximasJornadas", proximas3Jornadas);

    // 3. MANDAR LOS DATOS AL JSP
    request.getRequestDispatcher("index.jsp").forward(request, response);
  }
}