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
import java.util.List;

@WebServlet("/usuarios")

public class UsuarioServlet extends HttpServlet {

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


  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String accion = req.getParameter("accion");

    if (accion == null) {
      accion = "index";
    }

    switch (accion) {
      case "verRanking":
        UsuarioImpl usuarioDAO = new UsuarioImpl();

        List<Usuario> listaRanking = usuarioDAO.obtenerRankingUsuarios();

        List<Usuario> top3 = listaRanking.stream()
            .limit(3) // para filtrar los primeros 3
            .collect(java.util.stream.Collectors.toList());

        req.setAttribute("usuariosRanking", top3);
        RequestDispatcher rdRanking = req.getRequestDispatcher("/index.jsp");
        rdRanking.forward(req, res);
        break;

      case "registro":
        req.setAttribute("listaTipos", TipoUsuario.values());
        req.setAttribute("listaCarreras", Carrera.values());

        RequestDispatcher rdRegistro = req.getRequestDispatcher("/formRegistro.jsp");
        rdRegistro.forward(req, res);
        break;

      default: // por defecto
        res.sendRedirect("index.jsp");
        break;
    }
  }
}
