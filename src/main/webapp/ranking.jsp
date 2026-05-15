<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
<jsp:include page="header.jsp" />

<div class="hero-inicio mb-5 shadow-sm">
    <div class="container text-center">
        <h1 class="display-6 fw-bold" style="color: var(--afa-azul-oscuro);">Ranking General</h1>
        <p class="text-muted mb-0">Tabla de posiciones del Prode Mundial 2026</p>
    </div>
</div>

<main class="container mb-5">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-5">

            <div class="d-flex flex-column gap-3 mb-5">
                <c:forEach var="u" items="${rankingCompleto}" varStatus="loop">
                    <c:set var="posicion" value="${loop.index + 1}" />
                    <c:if test="${u.email == sessionScope.usuario.email}">
                            <c:set var="misPuntosReales" value="${u.puntajeTotal}" />
                            <c:set var="miPosicionReal" value="${posicion}" />
                        </c:if>

                    <c:set var="estiloCard" value="background-color: #ffffff;" />
                    <c:choose>
                        <c:when test="${posicion == 1}"><c:set var="estiloCard" value="background-color: #fff9e6; border-color: #ffeeba;" /></c:when>
                        <c:when test="${posicion == 2}"><c:set var="estiloCard" value="background-color: #f8f9fa; border-color: #dee2e6;" /></c:when>
                        <c:when test="${posicion == 3}"><c:set var="estiloCard" value="background-color: #fff5f2; border-color: #f5c6cb;" /></c:when>
                    </c:choose>

                    <div class="d-flex align-items-center p-3 rounded-4 shadow-sm border" style="${estiloCard}">

                        <div class="me-3">
                            <div class="bg-white border rounded-3 px-1 py-1 text-center d-flex flex-column align-items-center" style="min-width: 48px;">
                                <c:choose>
                                    <c:when test="${posicion == 1}"><span>&#129351;</span></c:when>
                                    <c:when test="${posicion == 2}"><span>&#129352;</span></c:when>
                                    <c:when test="${posicion == 3}"><span>&#129353;</span></c:when>
                                    <c:otherwise><span class="fw-bold small text-muted">#${posicion}</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="rounded-circle me-3 d-flex align-items-center justify-content-center bg-primary bg-opacity-10"
                             style="width: 42px; height: 42px; min-width: 42px; border: 2px solid #fff;">
                            <i class="bi bi-person-fill text-primary"></i>
                        </div>

                        <div class="flex-grow-1" style="min-width: 0;">
                            <h6 class="mb-0 fw-bold text-truncate" style="font-size: 0.95rem; color: var(--afa-azul-oscuro);">
                                ${not empty u.nombreGrupo ? u.nombreGrupo : u.email.split('@')[0]}
                            </h6>
                            <div class="text-muted" style="font-size: 0.72rem; line-height: 1.2;">
                                <span class="d-block text-truncate text-uppercase opacity-75">${u.carrera.descripcion}</span>
                                <span class="fw-bold text-primary text-uppercase">${not empty u.curso ? u.curso : 'S/D'}</span>
                            </div>
                        </div>

                        <div class="ms-2 text-end">
                            <span class="fw-bold d-block h5 mb-0" style="color: var(--afa-azul-oscuro);">${u.puntajeTotal}</span>
                            <span class="text-muted" style="font-size: 0.65rem; text-transform: uppercase;">pts</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</main>


<c:if test="${not empty sessionScope.usuario}">
    <div class="fixed-bottom p-3" style="z-index: 1040; bottom: 70px;">
        <div class="container d-flex justify-content-center p-0">
            <div class="col-12 col-md-8 col-lg-5">
                <div class="d-flex align-items-center p-3 rounded-4 border border-warning shadow-lg"
                     style="background-color: #fffdf5; border-width: 2px !important;">

                    <div class="bg-white border border-warning rounded-3 px-2 py-1 text-center" style="min-width: 45px;">
                        <span class="fw-bold small text-warning">
                            #${not empty miPosicionReal ? miPosicionReal : '-'}
                        </span>
                    </div>

                    <div class="flex-grow-1" style="min-width: 0;">
                        <h6 class="mb-0 fw-bold text-truncate" style="font-size: 0.9rem; color: var(--afa-azul-oscuro);">
                            ${not empty sessionScope.usuario.nombreGrupo ? sessionScope.usuario.nombreGrupo : 'Mi Perfil'}
                        </h6>
                        <div class="text-muted" style="font-size: 0.65rem; line-height: 1.1;">
                            <span class="d-block text-truncate text-uppercase">${sessionScope.usuario.carrera.descripcion}</span>
                            <span class="fw-bold text-uppercase text-secondary">${sessionScope.usuario.curso}</span>
                        </div>
                    </div>

                    <div class="text-end ms-2">
                        <span class="fw-bold fs-5 d-block text-dark">
                            ${not empty misPuntosReales ? misPuntosReales : '0'}
                        </span>
                        <span class="text-muted" style="font-size: 0.6rem;">PUNTOS</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div style="height: 160px;"></div>
</c:if>

<jsp:include page="footer.jsp" />