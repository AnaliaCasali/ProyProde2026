<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prode Mundial 2026</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="hero-inicio"> <div class="container mt-lg-5 mt-3">

    <div class="capsula-prode d-flex justify-content-between align-items-center mb-4 px-4 py-3 shadow-sm">
        <div class="d-flex align-items-center">
            <img src="${pageContext.request.contextPath}/img/logo_isp63.png" alt="Logo" style="height: 45px;">
            <h2 class="ms-3 fw-bold color-identidad-primario d-none d-md-block m-0">Jugar</h2>
        </div>
        <div class="puntos-badge px-3 py-2">
            <span class="fw-bold">PUNTOS: <c:out value="${puntosTotales != null ? puntosTotales : 0}" /></span>
        </div>
    </div>

    <div class="card-como-jugar p-3 mb-4 shadow-sm border-0">
        <form action="jugar" method="get" class="row g-2 align-items-center">
            <div class="col-12 col-md-5">
                <select name="jornada" class="form-select select-identidad rounded-pill">
                    <c:forEach var="i" begin="1" end="${totalJornadas}">
                        <option value="${i}" ${jornadaSeleccionada == i ? 'selected' : ''}>Jornada ${i}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-12 col-md-2">
                <button type="submit" class="btn btn-afa w-100 fw-bold">FILTRAR</button>
            </div>
        </form>
    </div>

    <c:if test="${not empty mensaje}">
        <div class="alert alert-success text-center fw-bold shadow-sm mx-auto" style="max-width: 650px;" role="alert">
            <i class="fas fa-check-circle me-2"></i><c:out value="${mensaje}"/>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center fw-bold shadow-sm mx-auto" style="max-width: 650px;" role="alert">
            <i class="fas fa-exclamation-triangle me-2"></i><c:out value="${error}"/>
        </div>
    </c:if>

    <form action="jugar" method="post">
        <input type="hidden" name="jornadaActual" value="${jornadaSeleccionada}">

        <div class="card-jornada shadow-lg mx-auto" style="max-width: 650px;">
            <div class="header-jornada text-center py-3">
                <h3 class="m-0 text-uppercase fw-bold text-white">FECHA ${jornadaSeleccionada}</h3>
            </div>

            <div class="body-jornada bg-white p-4 rounded-bottom">

                <div class="text-center mb-4">
                    <h4 class="fw-bold m-0 color-identidad-primario text-uppercase">
                        <c:out value="${fechaActualLabel}"/>
                    </h4>
                </div>

                <c:forEach var="partido" items="${listaPartidosJornada}" varStatus="status">
                    <div class="partido-row">

                        <div class="text-center mb-2">
                            <small class="text-muted fw-semibold">
                                <i class="fas fa-map-marker-alt me-1 color-identidad-secundario"></i>
                                <c:out value="${partido.estadio.nombreEstadio}"/> | <c:out value="${partido.estadio.pais}"/>
                            </small>
                        </div>

                        <div class="d-flex align-items-center justify-content-between">

                            <div class="text-center equipo-container">
                                <img src="${partido.equipoLocal.icono}" alt="Local" class="flag-img shadow-sm mb-2">
                                <div class="equipo-nombre fw-bold"><c:out value="${partido.equipoLocal.nombre}"/></div>
                            </div>

                            <div class="marcador-container d-flex flex-column align-items-center">

                                <c:set var="golesGuardadosL" value="" />
                                <c:set var="golesGuardadosV" value="" />
                                <c:forEach var="jugada" items="${listaJugadas}">
                                    <c:if test="${jugada.partido.idPartido == partido.idPartido}">
                                        <c:set var="golesGuardadosL" value="${jugada.golesLocal}" />
                                        <c:set var="golesGuardadosV" value="${jugada.golesVisitante}" />
                                    </c:if>
                                </c:forEach>

                                <c:set var="isBloqueado" value="${partido.fechaHora.isBefore(tiempoLimite)}" />

                                <div class="d-flex align-items-center gap-2 mb-1">
                                    <input type="number"
                                           name="golesL_${partido.idPartido}"
                                           class="form-control input-goles text-center <c:if test='${isBloqueado}'>bg-light text-muted border-secondary</c:if>"
                                           value="${not empty golesGuardadosL ? golesGuardadosL : 0}"
                                           min="0"
                                           onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                                           <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>>

                                    <div class="hora-partido fw-bold mx-1 text-center">
                                        ${partido.fechaHora.toLocalTime()}<br>
                                        <c:if test="${isBloqueado}">
                                            <span class="badge bg-danger mt-1" style="font-size: 0.6rem;">CERRADO</span>
                                        </c:if>
                                    </div>

                                    <input type="number"
                                           name="golesV_${partido.idPartido}"
                                           class="form-control input-goles text-center <c:if test='${isBloqueado}'>bg-light text-muted border-secondary</c:if>"
                                           value="${not empty golesGuardadosV ? golesGuardadosV : 0}"
                                           min="0"
                                           onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                                           <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>>
                                </div>
                            </div>

                            <div class="text-center equipo-container">
                                <img src="${partido.equipoVisitante.icono}" alt="Visitante" class="flag-img shadow-sm mb-2">
                                <div class="equipo-nombre fw-bold"><c:out value="${partido.equipoVisitante.nombre}"/></div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${!status.last}"><hr class="my-4"></c:if>
                </c:forEach>
            </div>
        </div>

        <div class="text-center mt-5 mb-5">
            <button type="submit" class="btn btn-afa btn-lg px-5 py-3 shadow hover-zoom">
                <c:choose>
                    <c:when test="${empty listaJugadas}">
                        <i class="fas fa-save me-2"></i>GUARDAR PRONÓSTICOS
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-edit me-2"></i>ACTUALIZAR CAMBIOS
                    </c:otherwise>
                </c:choose>
            </button>
        </div>
    </form>
</div>

</body>
</html>