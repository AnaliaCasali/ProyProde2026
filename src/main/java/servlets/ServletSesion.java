package servlets;

import dao.UsuarioImpl;
import entities.Usuarios;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.PasswordUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletSesion extends HttpServlet {

  private UsuarioImpl usuarioDAO = new UsuarioImpl();
  // hashmap para almacenar los usuarios con sesion iniciada
  private static Map<String, Usuarios> usuariosActivos = new HashMap<>();

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String parametroLogout = req.getParameter("cerrarSesion");

    if ("true".equals(parametroLogout)) {
      cerrarSesion(req, res);
    } else {
      res.sendRedirect("index.jsp");
    }
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    iniciarSesion(req, res);
  }

  // metodos
  // para iniciar sesion
  private void iniciarSesion(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    // obtengo los parametros de la peticion
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    // llamo a dos funciones: validar credenciales y redirigir A
    String destino = validarCredenciales(email, password, req);
    redirigirA(destino, req, res);
  }

  // para cerrar sesion
  private void cerrarSesion(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    HttpSession session = req.getSession(false);

    if (session != null) {
      Usuarios usuario = (Usuarios) session.getAttribute("usuario");
      if (usuario != null) {
        usuariosActivos.remove(usuario.getEmail());
      }
      session.invalidate();
    }

    req.setAttribute("mensajeExito", "Se ha cerrado la sesión correctamente.");
    redirigirA("formLogin.jsp", req, res);
  }

  // para validar email y password
  private String validarCredenciales(String email, String password, HttpServletRequest req) {
    if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
      req.setAttribute("mensajeError", "Email y contraseña son obligatorios.");
      return "formLogin.jsp";
    }

    email = email.trim();

    if (usuariosActivos.containsKey(email)) {
      req.setAttribute("mensajeError",
          "No puedes iniciar sesión en dos equipos al mismo tiempo.");
      return "formLogin.jsp";
    }

    Usuarios usuario = usuarioDAO.getByEmail(email);

    if (usuario == null) {
      req.setAttribute("mensajeError", "Credenciales no válidas.");
      return "formLogin.jsp";
    }

    // verificar contraseña con BCrypt, con manejo de errores
    try {
      if (!PasswordUtil.verifyPassword(password, usuario.getPassword())) {
        req.setAttribute("mensajeError", "Credenciales no válidas.");
        return "formLogin.jsp";
      }
    } catch (Exception e) {
      System.err.println("Error verificando contraseña: " + e.getMessage());
      req.setAttribute("mensajeError", "Error en el sistema de autenticación.");
      return "formLogin.jsp";
    }

    usuariosActivos.put(email, usuario);
    HttpSession sesion = req.getSession();
    sesion.setAttribute("usuario", usuario);

    req.setAttribute("mensajeExito", "Sesión iniciada correctamente.");
    return "index.jsp";
  }

  private void redirigirA(String destino, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    RequestDispatcher rd = req.getRequestDispatcher(destino);
    rd.forward(req, res);
  }
}

