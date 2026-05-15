<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>

<!doctype html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Mundial Prode - Inicio</title>

    <!-- Bootstrap 5 -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"
    />

    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="../css/estilo.css" />
  </head>
  <body>
    <!-- NAVBAR SUPERIOR -->
    <nav class="navbar navbar-dark navbar-afa sticky-top shadow">
      <div class="container-fluid navbar-container">
        <!-- IZQUIERDA: Logo FIFA -->
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/a/a7/FIFA_World_Cup_2026_Logo.svg"
          class="logo-fifa"
          alt="FIFA 2026"
        />

        <!-- CENTRO: Título y Logo AFA -->
        <div class="navbar-brand-center">
          <span class="navbar-brand fw-bold m-0">PRODE MUNDIAL</span>
          <img
            src="https://upload.wikimedia.org/wikipedia/en/3/3d/Argentina_national_football_team_logo.svg"
            class="logo-afa"
            alt="AFA"
          />
        </div>

        <!-- Botón Hamburguesa (PC) -->
        <button
          class="navbar-toggler border-0"
          type="button"
          data-bs-toggle="offcanvas"
          data-bs-target="#menu"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
      </div>
    </nav>

    <!-- MENÚ LATERAL (OFFCANVAS) -->
    <div class="offcanvas offcanvas-end" id="menu">
      <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title fw-bold">MENÚ</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="offcanvas"
        ></button>
      </div>
      <div class="offcanvas-body p-0">
        <div class="list-group list-group-flush">
          <a
            href="index.html"
            class="list-group-item list-group-item-action active"
            >Inicio</a
          >
          <a href="jugar.html" class="list-group-item list-group-item-action"
            >Jugar</a
          >
          <a href="ranking.html" class="list-group-item list-group-item-action"
            >Ranking</a
          >
          <a href="fixture.html" class="list-group-item list-group-item-action"
            >Fixture</a
          >
        </div>
      </div>
    </div>
<main class="mainCambiarContraseña">
    <form class="formCambiarContraseña" action="UsuarioServlet" method="POST">

    <input type="hidden" name="operacion" value="actualizarPassword">

    <label name="passwordActual">CONTRASEÑA ACTUAL</label>
    <input type="password" name="passwordActual" id="contraseñaActual" placeholder="Contraseña Actual">
    <br>
    <label name="passwordNueva">CONTRASEÑA NUEVA</label>
    <input type="password" name="passwordNueva" id="contraseñaNueva" placeholder="Contraseña Nueva">
    <br>
    <label name="repetirpasswordNueva"> REPETIR CONTRASEÑA NUEVA</label>
    <input type="password" name="repetirPasswordNueva" id="repetirContraseñaNueva" placeholder="Repetir Contraseña Nueva">
    <br>
    <button type="submit" class="btnActualizarContraseña">ACTUALIZAR CONTRASEÑA</button>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3">
            ${error}
        </div>
    </c:if>
    </form>

</main>

    <!-- NAVEGACIÓN INFERIOR (MOBILE) -->
    <div class="mobile-nav">
      <a href="index.html" class="active">
        <i class="bi bi-house-door-fill"></i>
        <span>Inicio</span>
      </a>
      <a href="jugar.html">
        <i class="bi bi-controller"></i>
        <span>Jugar</span>
      </a>
      <a href="fixture.html">
        <i class="bi bi-calendar3"></i>
        <span>Fixture</span>
      </a>
      <a href="ranking.html">
        <i class="bi bi-trophy"></i>
        <span>Ranking</span>
      </a>
      <a href="#">
        <i class="bi bi-file-text"></i>
        <span>Reglas</span>
      </a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
