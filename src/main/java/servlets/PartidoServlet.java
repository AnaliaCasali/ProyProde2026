package servlets;

import dao.PartidoDAO;
import entities.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/PartidoServlet")
public class PartidoServlet extends HttpServlet {

    private PartidoDAO partidoDAO;

    @Override
    public void init() throws ServletException {
        partidoDAO = new PartidoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) accion = "fixture";

        switch (accion) {
            case "fixture":
            default:
                List<Partido> listaPartidos = partidoDAO.getAll();
                request.setAttribute("partidos", listaPartidos);
                request.getRequestDispatcher("fixture.jsp").forward(request, response);
                break;
        }
    }
}