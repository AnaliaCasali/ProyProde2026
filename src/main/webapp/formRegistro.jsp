<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="usuario" value="${sessionScope.usuario}" />
<c:set var="rolUsuario" value="${not empty usuario ? usuario.tipo : 'INVITADO'}" />

<c:if test="${rolUsuario != 'ADMINISTRADOR'}">
    <c:url var="loginUrl" value="formLogin.jsp">
            <c:param name="mensajeError" value="No tienes permisos para acceder a esta página."/>
        </c:url>
        <c:redirect url="${loginUrl}"/>
</c:if>

<%@ page import="enums.TipoUsuario" %>
<%@ page import="enums.Carrera" %>

<jsp:useBean id="usuario" class="entities.Usuario" scope="request" />
<jsp:useBean id="usuarioDao" class="dao.UsuarioImpl" scope="page" />

<c:if test="${param.operacion == 'editar'}">
    <c:set var="idUsuario" value="${Integer.parseInt(param.id)}" />
    <c:set var="usuarioEditar" value="${usuarioDao.getById(idUsuario)}" />
    <c:set var="listaUsuario" value="${usuarioDao.getAll()}" />
    <c:set var="listaTipoUsuario" value="${TipoUsuario.values()}" />
    <c:set var="listaCarreras" value="${Carrera.values()}" />
</c:if>

<%
    TipoUsuario[] tiposU = TipoUsuario.values();
    request.setAttribute("listaTipos", tiposU);
%>

<%
    Carrera[] tiposC = Carrera.values();
    request.setAttribute("listaCarreras", tiposC);
%>

<c:set var="btnText" value="Registrar" />
<c:set var="btnClass" value="btn-primary" />


<!doctype html>
<html lang="es" class="h-100">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Prode Mundial | Registra tu cuenta</title>
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

                        <h1 class="card-title text-center h3 mb-4">Registrar nueva cuenta</h1>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <form action="usuarios" method="POST">

                            <input type="hidden" name="txtIdUsuario" id="txtIdUsuario"
                                value="${not empty usuarioEditar.idUsuario ? usuarioEditar.idUsuario : -1}"
                            />

                            <input type="hidden" name="operacion" id="operacion"
                                value="${not empty param.operacion ? param.operacion : 'nuevo' }"
                            />


                            <div class="mb-3">
                                <label for="txtEmail" class="form-label">
                                    Correo Electrónico <span class="text-danger">*</span>
                                </label>
                                <input type="email" name="txtEmail" id="txtEmail" class="form-control"
                                    pattern="[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}"
                                    title="Por favor, ingresa un correo electrónico válido que incluya un dominio (ejemplo: usuario@correo.com)"
                                    placeholder="usuario@dominio.com"
                                    value="${not empty usuarioEditar.email ? usuarioEditar.email : ''}"
                                    ${isReadOnly ? 'readonly' : ''}
                                    required />
                            </div>
                            <div class="mb-3">
                                <label for="txtPassword" class="form-label">
                                    Contraseña <span class="text-danger">*</span>
                                </label>
                                <input type="password" name="txtPassword" id="txtPassword" class="form-control"
                                    pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}"
                                    title="Debe contener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un símbolo especial."
                                    placeholder="Contraseña"
                                    value="${not empty usuarioEditar.password ? usuarioEditar.password : ''}"
                                    ${isReadOnly ? 'readonly' : ''}
                                    required />
                            </div>
                            <div class="mb-3">
                                <label for="txtCurso" class="form-label">
                                    Curso <span class="text-danger">*</span>
                                </label>
                                <input type="text" name="txtCurso" id="txtCurso" class="form-control"
                                    placeholder="Ej: Primer Año"
                                    value="${not empty usuarioEditar.curso ? usuarioEditar.curso : ''}"
                                    ${isReadOnly ? 'readonly' : ''}
                                    required />
                            </div>
                            <div class="mb-3">
                                <div class="form-group">
                                    <label for="lstCarrera">
                                        Carrera <span class="text-danger">*</span>
                                    </label>
                                    <select name="lstCarrera" class="form-control" id="lstCarrera"
                                    ${isReadOnly ? 'disabled' : ''}
                                    required>
                                        <option value="">Seleccione una opción...</option>
                                        <c:forEach var="carrera" items="${listaCarreras}">
                                            <%-- .name(): valor técnico | .descripcion: lo que ve el usuario --%>
                                            <option value="${carrera.name()}">${carrera.descripcion}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="mb-3">
                                <div class="form-group">
                                    <label for="lstTipo">
                                        Tipo de Usuario <span class="text-danger">*</span>
                                    </label>
                                    <select name="lstTipo" class="form-control" id="lstTipo"
                                        ${isReadOnly ? 'disabled' : ''}
                                        required>
                                        <option value="">Seleccione una opción...</option>
                                            <c:forEach var="tipo" items="${listaTipos}">
                                            <%-- .name(): valor técnico | .descripcion: lo que ve el usuario --%>
                                                <option value="${tipo.name()}">${tipo.descripcion}</option>
                                            </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="txtNombreGrupo" class="form-label">
                                    Nombre de grupo
                                </label>
                                <input type="text" name="txtNombreGrupo" id="txtNombreGrupo" class="form-control"
                                    placeholder="Ej: Letritas"
                                    value="${not empty usuarioEditar.nombreGrupo ? usuarioEditar.nombreGrupo : ''}"
                                    ${isReadOnly ? 'readonly' : ''}
                                    />
                            </div>
                            <p class="text-muted small">Los campos marcados con <span class="text-danger">*</span> son obligatorios.</p>
                            <div class="d-grid mt-4">
                                <input type="submit"
                                value="${btnText}"
                                class="btn ${btnClass} btn-lg" />
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