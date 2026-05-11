package servlets;

import enums.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

public class ServletUsuario extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    req.setAttribute("mensaje", "Hola desde el servlet del Prode Mundial 2026");
    req.setAttribute("fecha", new Date());

    String operacion = "nuevo";

    String email = "";
    String password = "";
    TipoUsuario tipo = null;
    String curso = "";
    String carrera = "";
    String nombreGrupo = "";
    int idUsuario = -1;

    operacion = req.getParameter("operacion");

    // atributos a asignar y utilizar en jsp
    // editar usuario existente | crear usuario nuevo
    if (operacion.equals("editar") || operacion.equals("nuevo")) {
      email = req.getParameter("txtEmail");
      password = req.getParameter("txtPassword");
      tipo = TipoUsuario.valueOf(req.getParameter("lstTipo"));
      curso = req.getParameter("txtCurso");
      carrera = req.getParameter("txtCarrera");
      nombreGrupo = req.getParameter("txtNombreGrupo");
      idUsuario = Integer.parseInt(req.getParameter("txtIdUsuario"));
    } else {
      if (req.getParameter("id") != null) {
        idUsuario = Integer.parseInt(req.getParameter("id"));
      } else
        idUsuario = -1;
    }
  }
}
