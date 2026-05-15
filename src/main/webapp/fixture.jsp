<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="header.jsp" />

<main class="container mt-4 mb-5">
    <div class="text-center mb-5">
        <h1 class="display-6 fw-bold" style="color: var(--afa-azul-oscuro);">Fixture Mundial 2026</h1>
        <p class="text-muted">Cronograma oficial de partidos y resultados</p>
    </div>

    <c:if test="${empty partidos}">
        <div class="alert alert-info text-center shadow-sm rounded-4" role="alert">
            Aún no hay partidos programados para mostrar.
        </div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="p" items="${partidos}">
            <div class="col-12 col-md-6 col-lg-4">
                <%-- Cápsula inspirada en tu estilo.css --%>
                <div class="capsula-prode p-3 h-100 shadow-sm border-0 bg-white d-flex flex-column justify-content-between">


                    <div class="text-center mb-3">
                        <span class="badge rounded-pill px-3" style="background-color: var(--afa-azul-oscuro); font-size: 0.7rem; letter-spacing: 1px;">
                            ${p.etapa.nombreEtapa}
                        </span>
                    </div>


                    <div class="d-flex align-items-center justify-content-between">


                        <div class="equipo d-flex flex-column align-items-center text-center" style="width: 38%;">
                            <div class="mb-2 shadow-sm border rounded" style="width: 55px; height: 38px; overflow: hidden; background-color: #f8f9fa;">
                                <img src="${p.equipoLocal.icono}" alt="${p.equipoLocal.nombre}" style="width: 100%; height: 100%; object-fit: cover;">
                            </div>
                            <span class="fw-bold small text-truncate d-block w-100" style="color: #333;" title="${p.equipoLocal.nombre}">
                                ${p.equipoLocal.nombre}
                            </span>
                        </div>


                        <div class="versus flex-grow-1 text-center px-1">
                            <c:choose>
                                <c:when test="${p.finalizado}">
                                    <div class="d-flex justify-content-center align-items-center gap-2">
                                        <span class="h4 fw-bold mb-0" style="color: var(--afa-azul-oscuro);">${p.golesLocal}</span>
                                        <span class="text-muted small">-</span>
                                        <span class="h4 fw-bold mb-0" style="color: var(--afa-azul-oscuro);">${p.golesVisitante}</span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="bg-light border rounded-pill py-1 px-2 d-inline-block">
                                        <span class="fw-bold text-primary small" style="font-size: 0.75rem;">VS</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>


                        <div class="equipo d-flex flex-column align-items-center text-center" style="width: 38%;">
                            <div class="mb-2 shadow-sm border rounded" style="width: 55px; height: 38px; overflow: hidden; background-color: #f8f9fa;">
                                <img src="${p.equipoVisitante.icono}" alt="${p.equipoVisitante.nombre}" style="width: 100%; height: 100%; object-fit: cover;">
                            </div>
                            <span class="fw-bold small text-truncate d-block w-100" style="color: #333;" title="${p.equipoVisitante.nombre}">
                                ${p.equipoVisitante.nombre}
                            </span>
                        </div>
                    </div>


                    <div class="mt-3 pt-2 border-top text-center" style="font-size: 0.68rem; color: #7f8c8d;">
                        <div class="d-flex flex-wrap justify-content-center gap-2">
                            <span><i class="bi bi-calendar3"></i> ${p.fechaHoraFormateada}</span>
                            <span class="text-truncate"><i class="bi bi-geo-alt-fill"></i> ${p.estadio.estadio}</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

<jsp:include page="footer.jsp" />