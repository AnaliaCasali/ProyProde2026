package servlets;

import dao.PartidoDAO;
import entities.Partido;
import entities.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLEncoder;
import enums.TipoUsuario;

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

        // comprobar sesión y rol: sólo administradores pueden acceder
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        Usuario usuarioLogueado = null;
        if (session != null) {
            usuarioLogueado = (Usuario) session.getAttribute("usuario");
        }

        if (usuarioLogueado == null || usuarioLogueado.getTipo() != TipoUsuario.ADMINISTRADOR) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }


        // Extraer idPartido de la ruta usando el método auxiliar
        int idPartidoInt = getIdPartidoFromPath(req);
        // Si no recibimos un id válido, mostramos un selector de partidos
        if (idPartidoInt == -1) {
            PartidoDAO partidoDAO = new PartidoDAO();
            List<Partido> partidos = partidoDAO.getAvailableForResultados();
            req.setAttribute("partidos", partidos);
            RequestDispatcher rd = req.getRequestDispatcher("/seleccionarPartido.jsp");
            rd.forward(req, res);
            return;
        }

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
        // comprobar sesión y rol: sólo administradores pueden hacer POST
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        Usuario usuarioLogueado = null;
        if (session != null) {
            usuarioLogueado = (Usuario) session.getAttribute("usuario");
        }
        if (usuarioLogueado == null || usuarioLogueado.getTipo() != TipoUsuario.ADMINISTRADOR) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Extraer idPartido de la ruta usando el método auxiliar
        int idPartido = getIdPartidoFromPath(req);
        if (idPartido == -1) {
            req.setAttribute("mensaje", "Error: idPartido requerido o inválido.");
            RequestDispatcher rd = req.getRequestDispatcher("/cargarResultado.jsp");
            rd.forward(req, res);
            return;
        }

        PartidoDAO partidoDAO = new PartidoDAO();
        Partido partido = partidoDAO.getById(idPartido);
        if (partido == null) {
            req.setAttribute("mensaje", "Error: partido no encontrado.");
            RequestDispatcher rd = req.getRequestDispatcher("/cargarResultado.jsp");
            rd.forward(req, res);
            return;
        }

        if (partido.isFinalizado()) {
            // No permitimos modificar un partido finalizado
            req.setAttribute("mensaje", "Este partido ya está finalizado. No se pueden modificar los goles.");
            req.setAttribute("partido", partido);
            req.setAttribute("idPartido", String.valueOf(idPartido));
            RequestDispatcher rd = req.getRequestDispatcher("/cargarResultado.jsp");
            rd.forward(req, res);
            return;
        }

        int golesLocal = Integer.parseInt(req.getParameter("golesLocal"));
        int golesVisitante = Integer.parseInt(req.getParameter("golesVisitante"));

        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setFinalizado(true);

        partidoDAO.update(partido);

        // PRG: redirigir al GET para recargar el partido y evitar reenvío del formulario
        String mensaje = URLEncoder.encode("Resultado guardado exitosamente.", "UTF-8");
        res.sendRedirect(req.getContextPath() + "/cargar-resultado/" + idPartido + "?mensaje=" + mensaje);
     }

 }
