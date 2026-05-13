<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    // redirige si el usuario ya inició sesión
    if(session.getAttribute("usuario") != null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<!doctype html>
<html lang="es" class="h-100">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Prode Mundial | Ingresar</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilo.css">
</head>

<body class="d-flex flex-column h-100">

    <jsp:include page="header.jsp" />

    <main class="container flex-fill mt-5 pt-4">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card shadow-sm">
                    <div class="card-body p-4 p-md-5">

                        <h1 class="card-title text-center h3 mb-4">Ingresa a tu cuenta</h1>


                        <c:if test="${not empty mensajeExito}">
                            <div class="alert alert-success" role="alert">
                                ${mensajeExito}
                            </div>
                        </c:if>

                        <c:choose>
                        <c:when test="${not empty mensajeError}">
                            <c:set var="errorAMostrar" value="${mensajeError}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="errorAMostrar" value="${param.mensajeError}" />
                        </c:otherwise>
                        </c:choose>

                        <c:if test="${not empty errorAMostrar}">
                            <div class="alert alert-danger" role="alert">
                            ${errorAMostrar}
                            </div>
                        </c:if>

                        <form action="SesionServlet" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label">
                                    Correo Electrónico <span class="text-danger">*</span>
                                </label>
                                <input type="email" name="email" id="email" class="form-control"
                                       placeholder="usuario@dominio.com" required="true" />
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label">
                                    Contraseña <span class="text-danger">*</span>
                                </label>
                                <input type="password" name="password" id="password" class="form-control"
                                       placeholder="1234" required="true" />
                            </div>
                            <p class="text-muted small">Los campos marcados con <span class="text-danger">*</span> son obligatorios.</p>

                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary">
                                    Iniciar Sesión
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>