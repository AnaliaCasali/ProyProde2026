package servlets;

import dao.UsuarioImpl;
import entities.Usuario;
import enums.Carrera;
import enums.TipoUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.PasswordUtil;

import java.io.IOException;
import java.util.Date;

@WebServlet("/usuarios")

public class ServletUsuario extends HttpServlet {

  // para manejo de operaciones de usuario
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    req.setAttribute("mensaje", "Hola desde el servlet del Prode Mundial 2026");
    req.setAttribute("fecha", new Date());

    String operacion = "nuevo";

    String email = "";
    String password = "";
    TipoUsuario tipo = null;
    String curso = "";
    Carrera carrera = null;
    String nombreGrupo = "";
    int idUsuario = -1;

    operacion = req.getParameter("operacion");

    // atributos a asignar y utilizar en jsp
    // editar usuario existente | crear usuario nuevo
    if (operacion != null && (operacion.equals("editar") || operacion.equals("nuevo"))) {
      email = req.getParameter("txtEmail");
      password = req.getParameter("txtPassword");
      tipo = TipoUsuario.valueOf(req.getParameter("lstTipo"));
      curso = req.getParameter("txtCurso");
      carrera = Carrera.valueOf(req.getParameter("lstCarrera"));
      nombreGrupo = req.getParameter("txtNombreGrupo");
      idUsuario = Integer.parseInt(req.getParameter("txtIdUsuario"));
    } else {
      if (req.getParameter("id") != null) {
        idUsuario = Integer.parseInt(req.getParameter("id"));
      } else
        idUsuario = -1;
    }

    // nuevo usuario
    UsuarioImpl usuarioDAO = new UsuarioImpl();
    if (operacion.equals("nuevo")) {
      if (usuarioDAO.existsByEmail(email)) {
        System.out.println("El correo ya se encuentra registrado");
        req.setAttribute("error", "El correo " + email + " ya se encuentra registrado. Por favor, utiliza otro correo.");
        RequestDispatcher rd = req.getRequestDispatcher("/formRegistro.jsp");
        rd.forward(req, res);
      } else {
        String claveHasheada = PasswordUtil.hashPassword(password);
        Usuario usuarioNuevo = new Usuario(email, claveHasheada, tipo, curso, carrera, nombreGrupo);
        usuarioDAO.insert(usuarioNuevo);

        req.setAttribute("mensajeExito", "¡Te has registrado correctamente!");

        RequestDispatcher rd = req.getRequestDispatcher("/formLogin.jsp");
        rd.forward(req, res);
      }
    }

    // editar usuario
    if (operacion.equals("editar")) { // si es editar
      Usuario usuarioEditar = usuarioDAO.getById(idUsuario);
      usuarioEditar.setEmail(email);
      if (password != null && !password.trim().isEmpty()) { // debe hashearse la nueva contraseña
        String claveHasheada = PasswordUtil.hashPassword(password);
        usuarioEditar.setPassword(claveHasheada);
      }
      usuarioEditar.setTipo(tipo);
      usuarioDAO.update(usuarioEditar);
    }

    // eliminar usuario
    if (operacion.equals("eliminar")) { // si es borrar
      usuarioDAO.delete(idUsuario);
    }
  }
}
