package servlets;

import entities.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.IOException;
import static org.mockito.Mockito.*;

class UsuarioServletTest {

    @Test
    void doPostUsuarioNoLogueado() throws ServletException, IOException {
        // Arrange : se preparan los mocks del request, response y session
        // simulando un usuario que no inició sesión
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(null);

        // Act : Se ejecuta el metodo doPost del servlet
        servlet.doPost(request, response);

        // Assert : se verifica que redireccione al login
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void doPostCamposVacios() throws Exception {

        // Arrange : se preparan los mocks del request, response, session
        // y un usuario logueado para simular la petición
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        Usuario usuario = new Usuario();
        usuario.setPassword("12345678");

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(usuario);

        // Se simulan campos vacíos
        when(request.getParameter("passwordActual")).thenReturn("");

        when(request.getParameter("passwordNueva")).thenReturn("");

        when(request.getParameter("repetirPasswordNueva")).thenReturn("");

        when(request.getRequestDispatcher("cambiarContrasena.jsp")).thenReturn(dispatcher);

        // Act : se ejecuta el metodo doPost del servlet
        servlet.doPost(request, response);

        // Assert : se verifica que se muestre el mensaje de error
        // y que redireccione nuevamente al JSP
        verify(request).setAttribute("error", "Este campo es obligatorio");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostPasswordsNoCoinciden() throws Exception {

        // Arrange : se preparan los mocks y un usuario logueado
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        Usuario usuario = new Usuario();
        usuario.setPassword("12345678");

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(usuario);

        // Se simulan contraseñas distintas
        when(request.getParameter("passwordActual")).thenReturn("12345678");

        when(request.getParameter("passwordNueva")).thenReturn("password123");

        when(request.getParameter("repetirPasswordNueva")).thenReturn("password456");

        when(request.getRequestDispatcher("cambiarContrasena.jsp")).thenReturn(dispatcher);

        // Act : se ejecuta el metodo doPost
        servlet.doPost(request, response);

        // Assert : se verifica que se muestre el mensaje de error
        verify(request).setAttribute("error", "Las contraseñas no coinciden");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostPasswordMuyCorta() throws Exception {

        // Arrange : se preparan los mocks y un usuario logueado
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        Usuario usuario = new Usuario();
        usuario.setPassword("12345678");

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(usuario);

        // Contraseña menor a 8 caracteres
        when(request.getParameter("passwordActual")).thenReturn("12345678");

        when(request.getParameter("passwordNueva")).thenReturn("123");

        when(request.getParameter("repetirPasswordNueva")).thenReturn("123");

        when(request.getRequestDispatcher("cambiarContrasena.jsp")).thenReturn(dispatcher);

        // Act : se ejecuta el metodo doPost
        servlet.doPost(request, response);

        // Assert : se verifica el mensaje de error
        verify(request).setAttribute("error", "La contraseña debe tener al menos 8 caracteres");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostPasswordActualIncorrecta() throws Exception {

        // Arrange : se preparana los mocks y un usuario logueado
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuario = new Usuario();

        // Contraseña real encriptada
        usuario.setPassword(encoder.encode("12345678"));

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(usuario);

        // Contraseña actual incorrecta
        when(request.getParameter("passwordActual")).thenReturn("incorrecta");

        when(request.getParameter("passwordNueva")).thenReturn("password123");

        when(request.getParameter("repetirPasswordNueva")).thenReturn("password123");

        when(request.getRequestDispatcher("cambiarContrasena.jsp")).thenReturn(dispatcher);

        // Act : se ejecuta el metodo doPost
        servlet.doPost(request, response);

        // Assert : se verifica el mensaje de error
        verify(request).setAttribute("error", "La contraseña actual es incorrecta");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostCambioPasswordExitoso() throws Exception {

        // Arrange : se preparan los mocks y un usuario logueado
        UsuarioServlet servlet = new UsuarioServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(1);

        // Contraseña real encriptada
        usuario.setPassword(encoder.encode("12345678"));

        when(request.getParameter("operacion")).thenReturn("actualizarPassword");

        when(request.getSession()).thenReturn(session);

        when(session.getAttribute("usuario")).thenReturn(usuario);

        // Datos correctos
        when(request.getParameter("passwordActual")).thenReturn("12345678");

        when(request.getParameter("passwordNueva")).thenReturn("password123");

        when(request.getParameter("repetirPasswordNueva")).thenReturn("password123");

        when(request.getRequestDispatcher("cambiarContrasena.jsp")).thenReturn(dispatcher);

        // Act : se ejecuta el metodo doPost
        servlet.doPost(request, response);

        // Assert : se verifica mensaje de éxito
        verify(request).setAttribute("mensaje", "Contraseña cambiada correctamente");

        verify(dispatcher).forward(request, response);
    }

}