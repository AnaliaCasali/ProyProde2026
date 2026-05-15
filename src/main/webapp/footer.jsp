<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- NAVEGACIÓN INFERIOR (MOBILE) -->
    <div class="mobile-nav">
      <a href="index.jsp" class="active">
        <i class="bi bi-house-door-fill"></i>
        <span>Inicio</span>
      </a>
      <a href="jugar.jsp">
        <i class="bi bi-controller"></i>
        <span>Jugar</span>
      </a>
      <a href="PartidoServlet?accion=fixture">
        <i class="bi bi-calendar3"></i>
        <span>Fixture</span>
      </a>
      <a href="usuarios?accion=rankingGeneral">
        <i class="bi bi-trophy"></i>
        <span>Ranking</span>
      </a>
      <a href="como-jugar">
        <i class="bi bi-file-text"></i>
        <span>Reglas</span>
      </a>
      <c:if test="${not empty sessionScope.usuario}">
      <a href="informacionDeLaCuenta.jsp">
        <i class="bi bi-file-text"></i>
        <span>Mi Cuenta</span>
      </a>
      </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>