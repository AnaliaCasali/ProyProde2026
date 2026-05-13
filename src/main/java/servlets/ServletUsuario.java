package servlets;

import dao.UsuarioImpl;
import entities.Usuario;
import enums.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

            if(usuario == null) {
                res.sendRedirect("login.jsp");
                return;
            }

            String passwordActual = req.getParameter("passwordActual");

            String passwordNueva = req.getParameter("passwordNueva");

            String repetirPasswordNueva = req.getParameter("repetirPasswordNueva");

            //validar campos vacios
            if(passwordActual == null || passwordActual.isEmpty()
                    || passwordNueva == null || passwordNueva.isEmpty()
                    || repetirPasswordNueva == null || repetirPasswordNueva.isEmpty()) {

                req.setAttribute("error",
                        "Este campo es obligatorio");

                req.getRequestDispatcher("cambiarContrasena.jsp")
                        .forward(req, res);

                return;
            }

            //validar repeticion contraseña
            if(!passwordNueva.equals(repetirPasswordNueva)) {

                req.setAttribute("error",
                        "Las contraseñas no coinciden");

                req.getRequestDispatcher("cambiarContrasena.jsp")
                        .forward(req, res);

                return;
            }

            //validar longitud contraseña
            if(passwordNueva.length() < 8) {

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
}
