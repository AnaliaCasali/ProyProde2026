package servlets;

import dao.PartidoDAO;
import entities.Partido;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLEncoder;

@WebServlet("/cargar-resultado/*")
public class CargarResultadoServlet extends HttpServlet {

    // Método auxiliar para extraer idPartido de la ruta (devuelve int, -1 si no hay)
    private int getIdPartidoFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            return -1;
        }
        // Buscar el primer número en pathInfo (maneja /123 o /123/ u otros casos)
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(pathInfo);
        if (m.find()) {
            String num = m.group();
            try {
                return Integer.parseInt(num);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        // Si no encontramos número en el path, intentar obtener id desde query param (fallback)
        String idParam = req.getParameter("idPartido");
        if (idParam == null) {
            idParam = req.getParameter("id");
        }
        if (idParam != null) {
            try {
                return Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Extraer idPartido de la ruta usando el método auxiliar
        int idPartidoInt = getIdPartidoFromPath(req);

        req.setAttribute("idPartido", String.valueOf(idPartidoInt));
        PartidoDAO partidoDAO = new PartidoDAO();
        Partido partido = partidoDAO.getById(idPartidoInt);
        req.setAttribute("partido", partido);

        // Pasar mensaje si viene como query param (PRG después del POST)
        String mensaje = req.getParameter("mensaje");
        if (mensaje != null) {
            req.setAttribute("mensaje", mensaje);
        }

        RequestDispatcher rd = req.getRequestDispatcher("/cargarResultado.jsp");
        rd.forward(req, res);
    }
    

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Extraer idPartido de la ruta usando el método auxiliar
        int idPartido = getIdPartidoFromPath(req);
        if (idPartido == -1) {
            req.setAttribute("mensaje", "Error: idPartido requerido o inválido.");
            RequestDispatcher rd = req.getRequestDispatcher("/cargarResultado.jsp");
            rd.forward(req, res);
            return;
        }

        int golesLocal = Integer.parseInt(req.getParameter("golesLocal"));
        int golesVisitante = Integer.parseInt(req.getParameter("golesVisitante"));

        PartidoDAO partidoDAO = new PartidoDAO();
        Partido partido = partidoDAO.getById(idPartido);
        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setFinalizado(true);

        partidoDAO.update(partido);

        // PRG: redirigir al GET para recargar el partido y evitar reenvío del formulario
        String mensaje = URLEncoder.encode("Resultado guardado exitosamente.", "UTF-8");
        res.sendRedirect(req.getContextPath() + "/cargar-resultado/" + idPartido + "?mensaje=" + mensaje);
     }

 }
