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
      // regex para que el email contenga un formato valido antes de registrar
      String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
      if (!email.matches(regexEmail)) {
        req.setAttribute("error", "Formato de correo inválido. Debe contener '@' y un '.' después del arroba.");
        req.setAttribute("listaTipos", TipoUsuario.values());
        req.setAttribute("listaCarreras", Carrera.values());
        req.getRequestDispatcher("/formRegistro.jsp").forward(req, res);
        return;
      }

      // regex para que la contraseña contenga un formato valido
      String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";
      if (!password.matches(regexPassword)) {
        req.setAttribute("error", "La contraseña debe tener mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un símbolo.");
        req.setAttribute("listaTipos", TipoUsuario.values());
        req.setAttribute("listaCarreras", Carrera.values());
        req.getRequestDispatcher("/formRegistro.jsp").forward(req, res);
        return;
      }

      // para verificar el email a registrar ya existe
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

      case "rankingGeneral":
        UsuarioImpl daoRankingGeneral = new UsuarioImpl();
        List<Usuario> rankingCompleto = daoRankingGeneral.obtenerRankingUsuarios();

        req.setAttribute("rankingCompleto", rankingCompleto);
        RequestDispatcher rdCompleto = req.getRequestDispatcher("/ranking.jsp");
        rdCompleto.forward(req, res);
        break;

      default: // por defecto
        res.sendRedirect("index.jsp");
        break;
    }
  }
}
