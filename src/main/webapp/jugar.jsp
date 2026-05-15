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
<jsp:include page="/header.jsp" />
<body class="hero-inicio"> <div class="container mt-lg-5 mt-3">

    <div class="capsula-prode d-flex justify-content-between align-items-center mb-4 px-4 py-3 shadow-sm">
        <div class="d-flex align-items-center">
            <img src="${pageContext.request.contextPath}/img/logo_isp63.png" alt="Logo" style="height: 45px;">
            <h2 class="ms-3 fw-bold color-identidad-primario d-none d-md-block m-0" style="color: var(--afa-azul-oscuro);">Jugar</h2>
        </div>

        <div class="d-flex align-items-center gap-2">
            <div class="puntos-badge px-3 py-2">
                <span class="fw-bold">TOTAL: <c:out value="${puntosTotales != null ? puntosTotales : 0}" /> pts</span>
            </div>
            <div class="puntos-badge px-3 py-2" style="background-color: var(--afa-amarillo); color: var(--afa-azul-oscuro); border: 1px solid var(--afa-azul-oscuro);">
                <span class="fw-bold">ESTA FECHA: <c:out value="${puntosJornada != null ? puntosJornada : 0}" /> pts</span>
            </div>
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
            <div class="header-jornada text-center py-3" style="background-color: var(--afa-azul-oscuro); border-radius: 15px 15px 0 0;">
                <h3 class="m-0 text-uppercase fw-bold text-white">FECHA ${jornadaSeleccionada}</h3>
            </div>

            <div class="body-jornada bg-white p-4 rounded-bottom">

                <div class="text-center mb-4">
                    <h4 class="fw-bold m-0 text-uppercase" style="color: var(--afa-azul-oscuro);">
                        <c:out value="${fechaActualLabel}"/>
                    </h4>
                </div>

                <c:forEach var="partido" items="${listaPartidosJornada}" varStatus="status">
                    <div class="partido-row p-3 rounded shadow-sm" style="background-color: #f8f9fa; border: 1px solid #e9ecef; margin-bottom: 20px;">

                        <div class="text-center mb-3">
                            <small class="text-muted fw-semibold" style="font-size: 0.8rem;">
                                <i class="fas fa-map-marker-alt me-1" style="color: var(--afa-celeste);"></i>
                                <c:out value="${partido.estadio.estadio}"/> | <c:out value="${partido.estadio.pais}"/>
                            </small>
                        </div>

                       <div class="d-flex align-items-center justify-content-between">

                           <div class="text-center equipo-container" style="width: 35%;">
                               <img src="${partido.equipoLocal.icono}" alt="Local" class="flag-img shadow-sm mb-2" style="max-height: 40px; object-fit: contain;">
                               <div class="equipo-nombre fw-bold" style="font-size: 0.95rem; color: #2c3e50;"><c:out value="${partido.equipoLocal.nombre}"/></div>
                           </div>

                           <div class="marcador-container d-flex flex-column align-items-center" style="width: 30%;">

                               <%-- MODIFICACIÓN LÓGICA PRINCIPAL: Mapeamos los goles directo usando el ID de partido como key del mapa --%>
                               <c:set var="golesGuardadosL" value="${mapaJugadas[partido.idPartido].golesLocal}" />
                               <c:set var="golesGuardadosV" value="${mapaJugadas[partido.idPartido].golesVisitante}" />

                               <c:set var="isBloqueado" value="${partido.fechaHora.isBefore(tiempoLimite) || partido.finalizado}" />

                               <div class="d-flex align-items-center gap-2 mb-1">
                                   <input type="number"
                                          name="golesL_${partido.idPartido}"
                                          class="form-control text-center py-2 <c:if test='${isBloqueado}'>bg-light text-muted border-secondary</c:if>"
                                          value="${golesGuardadosL}"
                                          min="0"
                                          onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                                          <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>
                                          style="width: 60px; font-size: 1.25rem; font-weight: 700; border-radius: 8px;">

                                   <div class="hora-partido fw-bold mx-1 text-center text-muted" style="font-size: 0.8rem; min-width: 70px; line-height: 1.3;">
                                       ${partido.fechaHora.toLocalTime()}<br>
                                       <c:choose>
                                           <c:when test="${partido.finalizado}">
                                               <span class="badge bg-success mt-1" style="font-size: 0.55rem; padding: 4px 6px; letter-spacing: 0.5px;">FINAL</span>
                                           </c:when>
                                           <c:when test="${partido.fechaHora.isBefore(tiempoLimite)}">
                                               <span class="badge bg-danger mt-1" style="font-size: 0.55rem; padding: 4px 6px; letter-spacing: 0.5px;">CERRADO</span>
                                           </c:when>
                                       </c:choose>
                                   </div>

                                   <input type="number"
                                          name="golesV_${partido.idPartido}"
                                          class="form-control text-center py-2 <c:if test='${isBloqueado}'>bg-light text-muted border-secondary</c:if>"
                                          value="${golesGuardadosV}"
                                          min="0"
                                          onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                                          <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>
                                          style="width: 60px; font-size: 1.25rem; font-weight: 700; border-radius: 8px;">
                                </div>
                           </div>

                           <div class="text-center equipo-container" style="width: 35%;">
                               <img src="${partido.equipoVisitante.icono}" alt="Visitante" class="flag-img shadow-sm mb-2" style="max-height: 40px; object-fit: contain;">
                               <div class="equipo-nombre fw-bold" style="font-size: 0.95rem; color: #2c3e50;"><c:out value="${partido.equipoVisitante.nombre}"/></div>
                           </div>

                       </div> <%-- FIN DEL D-FLEX DE EQUIPOS --%>

                       <c:if test="${partido.finalizado}">
                           <div class="w-100 text-center mt-3 pt-2" style="border-top: 1px dashed rgba(27, 54, 93, 0.2);">
                               <div class="d-inline-flex align-items-center justify-content-center gap-3 px-4 py-2 rounded-pill shadow-sm"
                                    style="background-color: rgba(27, 54, 93, 0.05); border: 1px solid rgba(27, 54, 93, 0.15);">

                                   <small class="fw-bold text-uppercase" style="color: var(--afa-azul-oscuro); font-size: 0.65rem; letter-spacing: 0.5px;">
                                       Resultado Oficial:
                                   </small>
                                   <span class="fw-bold fs-5" style="color: var(--afa-azul-oscuro); min-width: 45px;">
                                       ${partido.golesLocal} - ${partido.golesVisitante}
                                   </span>

                                   <%-- Leemos los puntos de la jugada directo desde el mapa --%>
                                   <c:if test="${not empty mapaJugadas[partido.idPartido]}">
                                       <span class="badge ${mapaJugadas[partido.idPartido].puntaje > 0 ? 'bg-success' : 'bg-secondary'} px-2 py-1.5" style="font-size: 0.7rem; border-radius: 6px;">
                                           +${mapaJugadas[partido.idPartido].puntaje} pts obtenidos
                                       </span>
                                   </c:if>
                               </div>
                           </div>
                       </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="text-center mt-5 mb-5">
            <button type="submit" class="btn btn-afa btn-lg px-5 py-3 shadow hover-zoom">
                <c:choose>
                    <%-- Cambiamos mapaJugadas por listaJugadas para que JSTL mida la colección nativa correctamente --%>
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