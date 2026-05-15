<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Mundial Prode</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css"/>

  </head>
  <body>

    <!-- NAVBAR SUPERIOR -->
    <nav class="navbar navbar-dark navbar-afa sticky-top shadow">
      <div class="container-fluid navbar-container">

        <!-- IZQUIERDA: Logo -->
        <img src="https://upload.wikimedia.org/wikipedia/commons/a/a7/FIFA_World_Cup_2026_Logo.svg" class="logo-fifa" alt="FIFA 2026"/>
        <!-- CENTRO: Título -->
        <div class="navbar-brand-center">
            <span class="navbar-brand fw-bold m-0">PRODE MUNDIAL</span>
        </div>

        <!-- DERECHA: Usuario en mobile / Hamburguesa en PC -->
        <c:if test="${sessionScope.usuario != null}">
          <span class="text-white small d-md-none">👤 ${sessionScope.usuario.email}</span>
        </c:if>
        <button class="navbar-toggler border-0 d-none d-md-block" type="button" data-bs-toggle="offcanvas" data-bs-target="#menu">
          <span class="navbar-toggler-icon"></span>
        </button>

      </div>
    </nav>

    <!-- MENÚ LATERAL (OFFCANVAS) -->
    <div class="offcanvas offcanvas-end" id="menu">
      <div class="offcanvas-header border-bottom">

        <!-- Si está logueado muestra su nombre, sino muestra MENÚ -->
        <c:if test="${sessionScope.usuario != null}">
          <h5 class="offcanvas-title fw-bold">MENÚ</h5>
          <p class="offcanvas-title fw-bold">¡Hola, ${sessionScope.usuario.email}!</p>
        </c:if>
        <c:if test="${sessionScope.usuario == null}">
          <h5 class="offcanvas-title fw-bold">MENÚ</h5>
        </c:if>

        <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
      </div>
      <div class="offcanvas-body p-0">
        <div class="list-group list-group-flush">

          <a href="inicio" class="list-group-item list-group-item-action">Inicio</a>
          <a href="jugar.jsp" class="list-group-item list-group-item-action">Jugar</a>
          <a href="PartidoServlet?accion=fixture" class="list-group-item list-group-item-action">Fixture</a>
          <a href="usuarios?accion=rankingGeneral" class="list-group-item list-group-item-action">Ranking</a>

          <c:if test="${sessionScope.usuario != null && sessionScope.usuario.tipo == 'ADMINISTRADOR'}">
            <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Gestión</div>

            <a href="cargarPartido.jsp" class="list-group-item list-group-item-action">Nuevo Partido</a>
            <a href="formRegistro.jsp" class="list-group-item list-group-item-action">Registrar Usuario</a>
          </c:if>

          <c:if test="${sessionScope.usuario != null}">
            <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Mi Cuenta</div>

            <a href="informacionDeLaCuenta.jsp" class="list-group-item list-group-item-action">Información de la Cuenta</a>
            <a href="sesion?cerrarSesion=true" class="list-group-item list-group-item-action text-danger fw-bold">
              <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
            </a>
          </c:if>

          <c:if test="${sessionScope.usuario == null}">
            <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Acceso</div>

            <a href="formLogin.jsp" class="list-group-item list-group-item-action text-primary fw-bold">
              <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar sesión
            </a>
          </c:if>

        </div>
      </div>
    </div>

  </body>
</html>