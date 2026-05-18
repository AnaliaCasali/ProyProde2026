<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/header.jsp"/>

<div class="hero-inicio mb-5 shadow-sm">
  <div class="container text-center">
    <h1 class="display-6 fw-bold header-prode-titulo">¡A JUGAR EL PRODE!</h1>
    <p class="text-muted mb-0">Cargá tus pronósticos y sumá puntos</p>
  </div>
</div>

<main class="container mb-5">

  <div class="contenedor-filtro-jornada">
    <div class="capsula-filtro bg-white shadow-sm">
      <form action="jugar" method="get" class="formulario-filtro">
        <div class="input-grupo-filtro">
          <select name="jornada" class="form-select selector-jornada">
            <c:forEach var="i" begin="1" end="${totalJornadas}">
              <option value="${i}" ${jornadaSeleccionada== i ?
              'selected' : ''}>Jornada ${i}</option>
            </c:forEach>
          </select>
        </div>
        <div class="btn-grupo-filtro">
          <button type="submit" class="btn btn-filtrar-prode">FILTRAR</button>
        </div>
      </form>
    </div>
  </div>

  <c:if test="${not empty mensaje}">
    <div class="alert alert-success text-center fw-bold shadow-sm mx-automb-4 alerta-mensajes" role="alert">
      <i class="bi bi-check-circle-fill me-2"></i>
      <c:out value="${mensaje}"/>
    </div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger text-center fw-bold shadow-sm mx-auto mb-4 alerta-mensajes" role="alert">
      <i class="bi bi-exclamation-triangle-fill me-2"></i>
      <c:out value="${error}"/>
    </div>
  </c:if>

  <form action="jugar" method="post">
    <input type="hidden" name="jornadaActual" value="${jornadaSeleccionada}">

    <div class="mb-4 text-center">
      <h5 class="fw-bold border-bottom pb-3 text-uppercase text-secondary d-inline-block px-4 contenedor-fecha-titulo">
        <i class="bi bi-calendar-event-fill text-primary me-2"></i> FECHA ${jornadaSeleccionada} —
        <c:out value="${fechaActualLabel}"/>
      </h5>
    </div>

    <div class="row g-4 justify-content-center">
      <c:forEach var="partido" items="${listaPartidosJornada}" varStatus="status">

        <%-- OPTIMIZACIÓN: Extraemos la jugada directo del mapa usando el ID de partido --%>
        <c:set var="jugadaUsuario" value="${mapaJugadas[partido.idPartido]}"/>

        <%-- El partido se bloquea si pasó el tiempo límite O si ya fue marcado como finalizado --%>
        <c:set var="isBloqueado" value="${partido.fechaHora.isBefore(tiempoLimite) || partido.finalizado}"/>

        <div class="col-12 col-md-6 col-lg-4">
          <div class="capsula-prode p-3 h-100 shadow-sm border-0 bg-white d-flex flex-column justify-content-between">

            <div class="text-center mb-3">
              <c:choose>
                <c:when test="${partido.finalizado}">
                  <span class="badge rounded-pill px-3 bg-success small etiqueta-finalizado">
                    <i class="bi bi-check-circle-fill me-1"></i> FINALIZADO
                  </span>
                </c:when>
                <c:when test="${isBloqueado}">
                  <span class="badge rounded-pill px-3 bg-danger small etiqueta-bloqueo">
                    <i class="bi bi-lock-fill me-1"></i> CERRADO
                  </span>
                </c:when>
              </c:choose>
            </div>

            <div class="d-flex align-items-center justify-content-between contenedor-equipos-marcador">

              <div class="equipo d-flex flex-column align-items-center text-center equipo-tarjeta-prode">
                <div class="mb-2 shadow-sm border rounded contenedor-bandera-prode">
                  <img src="${partido.equipoLocal.icono}" alt="Local" class="bandera-prode-img">
                </div>
                <span class="fw-bold small text-truncate d-block w-100 nombre-equipo-prode"
                      title="${partido.equipoLocal.nombre}">
                  <c:out value="${partido.equipoLocal.nombre}"/>
                </span>
              </div>

              <div class="versus flex-grow-1 text-center px-2">
                <div class="d-flex align-items-center justify-content-center gap-1 w-100">
                  <input type="number"
                         name="golesL_${partido.idPartido}"
                         class="form-control text-center fw-bold input-goles-prode ${isBloqueado ? 'input-prode-bloqueado' : ''}"
                         value="${not empty jugadaUsuario ? jugadaUsuario.golesLocal : ''}"
                         placeholder="0"
                         min="0"
                         onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                  <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>
                  >

                  <span class="text-muted px-1 fw-bold separador-goles">-</span>

                  <input type="number"
                         name="golesV_${partido.idPartido}"
                         class="form-control text-center fw-bold input-goles-prode ${isBloqueado ? 'input-prode-bloqueado' : ''}"
                         value="${not empty jugadaUsuario ? jugadaUsuario.golesVisitante : ''}"
                         placeholder="0"
                         min="0"
                         onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                  <c:if test="${isBloqueado}">readonly tabindex="-1"</c:if>
                  >
                </div>

                <c:choose>
                  <c:when test="${partido.finalizado}">
                    <div class="contenedor-resultado-oficial-prode mt-2">
                      <span class="badge bg-light text-dark border marcador-oficial-badge">
                        Oficial: ${partido.golesLocal} - ${partido.golesVisitante}
                      </span>
                      <c:choose>
                        <c:when test="${not empty jugadaUsuario}">
                          <div class="puntos-ganados-partido mt-1 fw-bold text-success">
                            +${jugadaUsuario.puntaje} pts
                          </div>
                        </c:when>
                        <c:otherwise>
                          <div class="puntos-ganados-partido mt-1 fw-bold text-muted small">
                            No jugado
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="text-muted mt-2 fw-semibold text-uppercase hora-partido-prode">
                      ${partido.fechaHora.toLocalTime()} HS
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>

              <div class="equipo d-flex flex-column align-items-center text-center equipo-tarjeta-prode">
                <div class="mb-2 shadow-sm border rounded contenedor-bandera-prode">
                  <img src="${partido.equipoVisitante.icono}" alt="Visitante" class="bandera-prode-img">
                </div>
                <span class="fw-bold small text-truncate d-block w-100 nombre-equipo-prode"
                      title="${partido.equipoVisitante.nombre}">
                  <c:out value="${partido.equipoVisitante.nombre}"/>
                </span>
              </div>
            </div>

            <div class="mt-3 pt-2 border-top text-center footer-tarjeta-prode">
              <div class="d-flex flex-wrap justify-content-center gap-2">
                <span class="text-truncate text-muted small">
                  <i class="bi bi-geo-alt-fill text-secondary"></i> <c:out value="${partido.estadio.estadio}"/>
                </span>
              </div>
            </div>

          </div>
        </div>
      </c:forEach>
    </div>

    <div class="text-center mt-5 contenedor-btn-guardar">
      <button type="submit" class="btn btn-afa btn-lg px-5 py-3 shadow-lg fw-bold">
        <c:choose>
          <c:when test="${empty listaJugadas}">
            <i class="bi bi-check-circle-fill me-2"></i>GUARDAR PRONÓSTICOS
          </c:when>
          <c:otherwise>
            <i class="bi bi-arrow-clockwise me-2"></i>ACTUALIZAR CAMBIOS
          </c:otherwise>
        </c:choose>
      </button>
    </div>
  </form>

  <div class="marcador-puntos-flotante">
    <div
        class="puntos-badge shadow-lg px-3 py-2 border border-warning d-flex align-items-center gap-2 badge-flotante-capsula">
      <i class="bi bi-trophy-fill text-warning fs-5"></i>
      <div class="text-start">
        <div class="text-uppercase fw-bold subtext-puntos">Mis Puntos</div>
        <div class="fw-bold fs-5 numeracion-puntos">
          <c:out value="${puntosTotales != null ? puntosTotales : 0}"/>
          pts
        </div>
      </div>
    </div>
  </div>

</main>

<jsp:include page="/footer.jsp"/>