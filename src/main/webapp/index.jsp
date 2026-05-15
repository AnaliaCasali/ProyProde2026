<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${proximasJornadas == null}">
    <jsp:forward page="inicio" />
</c:if>
<%@ include file="header.jsp" %>

    <main>
      <section class="hero-inicio mb-4">
        <h1 class="fw-bold">¡BIENVENIDO!</h1>
        <div class="countdown-box my-3">38 DÍAS</div>
        <p class="small text-uppercase fw-bold">Para el mundial</p>
      </section>

      <div class="container pb-5">

        <div class="mb-5">
          <h6 class="fw-bold mb-3 d-flex justify-content-between align-items-center">
            <span><i class="bi bi-trophy-fill text-warning me-2"></i> TOP 3 GENERAL</span>
            <a href="ranking" class="text-decoration-none small" style="color: var(--afa-celeste)">Ver todo</a>
          </h6>

          <c:forEach var="usuarioTop" items="${topUsuarios}" varStatus="status">
            <c:set var="colorPuesto" value="${status.count == 1 ? 'text-warning' : (status.count == 2 ? 'text-secondary' : 'text-danger')}" />
            <c:set var="clasePuesto" value="puesto-${status.count}" />

            <div class="capsula-prode ${clasePuesto} py-2 d-flex justify-content-between align-items-center mb-2">
              <div class="d-flex align-items-center" style="min-width: 0;">
                <span class="fw-bold fs-5 ${colorPuesto} me-2">#${status.count}</span>
                <div style="width: 35px; height: 35px; background: #eee; border-radius: 50%; overflow: hidden; margin-right: 10px;">
                  <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=${usuarioTop.email}" alt="avatar" style="width: 100%;">
                </div>
                <span class="fw-bold text-truncate" style="max-width: 150px;">
                  <c:choose>
                    <c:when test="${not empty usuarioTop.nombreGrupo}">
                       <c:out value="${usuarioTop.nombreGrupo}" />
                    </c:when>
                    <c:otherwise>
                       <c:out value="${usuarioTop.curso} ${usuarioTop.carrera.descripcion}" />
                    </c:otherwise>
                  </c:choose>
                </span>
              </div>
              <div class="puntos-badge"><c:out value="${usuarioTop.puntajeTotal}" /> pts</div>
            </div>
          </c:forEach>

          <c:if test="${empty topUsuarios}">
             <p class="text-muted text-center mt-3">Todavía no hay puntajes registrados.</p>
          </c:if>
        </div>

      <div class="container pb-5">

        <c:forEach var="jornada" items="${proximasJornadas}">
            <div class="mb-4">
              <h6 class="fw-bold mb-3 border-bottom pb-2 text-uppercase">
                              <i class="bi bi-calendar-event text-primary"></i> JORNADA ${jornada.key}
              </h6>

              <c:forEach var="partido" items="${jornada.value}">
                  <div class="capsula-prode mb-2">
                    <div class="equipo text-center" style="width: 30%;">
                      <img src="${partido.equipoLocal.icono}" style="width: 35px" class="mb-1 shadow-sm" alt="Local"/>
                      <span class="small fw-bold d-block text-truncate"><c:out value="${partido.equipoLocal.nombre}"/></span>
                    </div>
                    <div class="versus text-center" style="width: 40%;">
                      <div class="badge bg-light text-dark border mb-1" style="font-size: 0.6rem">
                        ${partido.fechaHora.toLocalTime()}
                      </div>
                      <div class="h5 fw-bold m-0 text-muted">VS</div>
                    </div>
                    <div class="equipo text-center" style="width: 30%;">
                      <img src="${partido.equipoVisitante.icono}" style="width: 35px" class="mb-1 shadow-sm" alt="Visitante"/>
                      <span class="small fw-bold d-block text-truncate"><c:out value="${partido.equipoVisitante.nombre}"/></span>
                    </div>
                  </div>
              </c:forEach>
            </div>
        </c:forEach>

        <c:if test="${empty proximasJornadas}">
             <p class="text-muted text-center mt-3">No hay partidos próximos programados.</p>
        </c:if>

        <div class="text-center mt-4">
          <a href="${pageContext.request.contextPath}/jugar" class="btn btn-afa shadow btn-lg">EMPEZAR A JUGAR</a>
        </div>
      </div>
    </main>

<%@ include file="footer.jsp" %>