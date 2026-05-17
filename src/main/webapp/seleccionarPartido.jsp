<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/header.jsp" />

<div class="container mt-4">
  <h3>Seleccionar partido</h3>
  <p>Seleccione un partido para cargar su resultado:</p>

  <c:if test="${empty partidos}">
    <div class="alert alert-info">No hay partidos disponibles para cargar resultados.</div>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">Volver</a>
  </c:if>

  <c:if test="${not empty partidos}">
    <form id="selectForm" onsubmit="return goToSelected();">
      <div class="mb-3">
        <select id="idPartido" class="form-select">
          <option value="">-- Seleccione --</option>
          <c:forEach var="p" items="${partidos}">
            <option value="${p.idPartido}">
              ${p.fechaHora} - ${p.equipoLocal.nombre} vs ${p.equipoVisitante.nombre}
            </option>
          </c:forEach>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Abrir</button>
      <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary ms-2">Volver</a>
    </form>
  </c:if>
</div>

<script>
  function goToSelected() {
    var sel = document.getElementById('idPartido');
    var id = sel.value;
    if (!id) {
      alert('Por favor seleccione un partido.');
      return false;
    }
    window.location.href = '${pageContext.request.contextPath}/cargar-resultado/' + id;
    return false;
  }
</script>

<jsp:include page="/footer.jsp" />


