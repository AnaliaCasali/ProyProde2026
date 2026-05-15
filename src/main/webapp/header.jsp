<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"/>
<%-- Usamos la ruta absoluta también para el CSS por si navegás dentro de subrutas --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css"/>

<nav class="navbar navbar-dark navbar-afa sticky-top shadow">
  <div class="container-fluid navbar-container">

    <img src="https://upload.wikimedia.org/wikipedia/commons/a/a7/FIFA_World_Cup_2026_Logo.svg" class="logo-fifa" alt="FIFA 2026"/>

    <div class="navbar-brand-center">
        <span class="navbar-brand fw-bold m-0">PRODE MUNDIAL</span>
    </div>

    <c:if test="${sessionScope.usuario != null}">
      <span class="text-white small d-md-none">👤 ${sessionScope.usuario.email}</span>
    </c:if>
    <%-- Este botón solo se ve en PC gracias a d-none d-md-block --%>
    <button class="navbar-toggler border-0 d-none d-md-block" type="button" data-bs-toggle="offcanvas" data-bs-target="#menu">
      <span class="navbar-toggler-icon"></span>
    </button>

  </div>
</nav>

<div class="offcanvas offcanvas-end" id="menu">
  <div class="offcanvas-header border-bottom">

    <c:choose>
      <c:when test="${sessionScope.usuario != null}">
        <div>
          <h5 class="offcanvas-title fw-bold m-0">MENÚ</h5>
          <small class="text-muted">¡Hola, ${sessionScope.usuario.email}!</small>
        </div>
      </c:when>
      <c:otherwise>
        <h5 class="offcanvas-title fw-bold">MENÚ</h5>
      </c:otherwise>
    </c:choose>

    <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
  </div>

  <div class="offcanvas-body p-0">
    <div class="list-group list-group-flush">

      <%-- RUTAS CORREGIDAS: Todas apuntan al contexto absoluto para que no fallen nunca --%>
      <a href="${pageContext.request.contextPath}/index.jsp" class="list-group-item list-group-item-action">Inicio</a>
      <a href="${pageContext.request.contextPath}/jugar" class="list-group-item list-group-item-action">Jugar</a>
      <a href="${pageContext.request.contextPath}/PartidoServlet?accion=fixture" class="list-group-item list-group-item-action">Fixture</a>
      <a href="${pageContext.request.contextPath}/usuarios?accion=rankingGeneral" class="list-group-item list-group-item-action">Ranking</a>

      <%-- SECCIÓN DE GESTIÓN ADMINISTRADOR --%>
      <c:if test="${sessionScope.usuario != null && sessionScope.usuario.tipo == 'ADMINISTRADOR'}">
        <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Gestión</div>
        <a href="${pageContext.request.contextPath}/cargarPartido.jsp" class="list-group-item list-group-item-action">Nuevo Partido</a>
        <a href="${pageContext.request.contextPath}/formRegistro.jsp" class="list-group-item list-group-item-action">Registrar Usuario</a>
      </c:if>

      <%-- SECCIÓN MI CUENTA --%>
      <c:if test="${sessionScope.usuario != null}">
        <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Mi Cuenta</div>
        <a href="${pageContext.request.contextPath}/informacionDeLaCuenta.jsp" class="list-group-item list-group-item-action">Información de la Cuenta</a>
        <a href="${pageContext.request.contextPath}/sesion?cerrarSesion=true" class="list-group-item list-group-item-action text-danger fw-bold">
          <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
        </a>
      </c:if>

      <%-- SECCIÓN ACCESO ANÓNIMO --%>
      <c:if test="${sessionScope.usuario == null}">
        <div class="list-group-item bg-light text-secondary small fw-bold text-uppercase border-top">Acceso</div>
        <a href="${pageContext.request.contextPath}/formLogin.jsp" class="list-group-item list-group-item-action text-primary fw-bold">
          <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar sesión
        </a>
      </c:if>

    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>