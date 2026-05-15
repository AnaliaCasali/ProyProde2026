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
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.PasswordUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/usuarios")

public class UsuarioServlet extends HttpServlet {

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
    UsuarioImpl usuarioDAO = new UsuarioImpl();

    // atributos a asignar y utilizar en jsp
    // crear usuario nuevo
    if ("nuevo".equals(operacion)) {
      HttpSession session = req.getSession();
      Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
      if (usuarioSesion == null || usuarioSesion.getTipo() != TipoUsuario.ADMINISTRADOR) {
        res.sendRedirect("formLogin.jsp?mensajeError=Acceso denegado. Solo administradores pueden registrar usuarios.");
        return;
      }

      email = req.getParameter("txtEmail");
      password = req.getParameter("txtPassword");
      tipo = TipoUsuario.valueOf(req.getParameter("lstTipo"));
      curso = req.getParameter("txtCurso");
      carrera = Carrera.valueOf(req.getParameter("lstCarrera"));
      nombreGrupo = req.getParameter("txtNombreGrupo");

      if (usuarioDAO.existsByEmail(email)) {
        System.out.println("El correo ya se encuentra registrado");
        req.setAttribute("error", "El correo " + email + " ya se encuentra registrado. Por favor, utiliza otro correo.");
        RequestDispatcher rd = req.getRequestDispatcher("/formRegistro.jsp");
        rd.forward(req, res);
      } else {
        String claveHasheada = PasswordUtil.hashPassword(password);
        Usuario usuarioNuevo = new Usuario(email, claveHasheada, tipo, curso, carrera, nombreGrupo);
        usuarioDAO.insert(usuarioNuevo);

        req.getSession().setAttribute("mensajeExito", "¡Te has registrado correctamente!");
        res.sendRedirect("formLogin.jsp");
      }
    }

    // eliminar usuario
    if (operacion.equals("eliminar")) { // si es borrar
      usuarioDAO.delete(idUsuario);
    }

    // -- CAMBIAR CONTRASEÑA --
    if (operacion.equals("actualizarPassword")) {
      HttpSession session = req.getSession();

            /*prueba cambiar contraseña
            if(session.getAttribute("usuario") == null) {

                Usuario usuarioTest = new Usuario();

                usuarioTest.setIdUsuario(1);

                BCryptPasswordEncoder encoder =
                        new BCryptPasswordEncoder();

                // contraseña real: 12345678
                usuarioTest.setPassword(
                        encoder.encode("12345678")
                );

                session.setAttribute("usuario", usuarioTest);
            }
            fin prueba cambiar contraseña*/

      Usuario usuario = (Usuario) session.getAttribute("usuario");

      if (usuario == null) {
        res.sendRedirect("login.jsp");
        return;
      }

      String passwordActual = req.getParameter("passwordActual");
      String passwordNueva = req.getParameter("passwordNueva");
      String repetirPasswordNueva = req.getParameter("repetirPasswordNueva");

      //validar campos vacios
      if (passwordActual == null || passwordActual.isEmpty()
          || passwordNueva == null || passwordNueva.isEmpty()
          || repetirPasswordNueva == null || repetirPasswordNueva.isEmpty()) {
        req.setAttribute("error",
            "Este campo es obligatorio");
        req.getRequestDispatcher("cambiarContrasena.jsp")
            .forward(req, res);
        return;
      }

      //validar repeticion contraseña
      if (!passwordNueva.equals(repetirPasswordNueva)) {
        req.setAttribute("error",
            "Las contraseñas no coinciden");
        req.getRequestDispatcher("cambiarContrasena.jsp")
            .forward(req, res);
        return;
      }

      //validar longitud contraseña
      if (passwordNueva.length() < 8) {
        req.setAttribute("error",
            "La contraseña debe tener al menos 8 caracteres");
        req.getRequestDispatcher("cambiarContrasena.jsp")
            .forward(req, res);
        return;
      }

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

      // Verificar contraseña actual
      boolean coincide = encoder.matches(passwordActual, usuario.getPassword());
      if (coincide) {
        // Encriptar nueva contraseña
        String nuevaPasswordEncriptada = encoder.encode(passwordNueva);
        UsuarioImpl usuarioDao = new UsuarioImpl();
        usuarioDao.updatePassword(usuario.getIdUsuario(), nuevaPasswordEncriptada);

        // Actualizar sesión
        usuario.setPassword(nuevaPasswordEncriptada);
        req.setAttribute("mensaje", "Contraseña cambiada correctamente");

      } else {
        req.setAttribute("error", "La contraseña actual es incorrecta");
      }
      req.getRequestDispatcher("cambiarContrasena.jsp")
          .forward(req, res);
    }
  }

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
