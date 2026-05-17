<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="entities.Equipo" %>
<%@ page import="entities.Partido" %>
<%@ include file="header.jsp" %>

<%
  List<Equipo> equipos = (List<Equipo>) request.getAttribute("equipos");
  List<Partido> partidosPendientes = (List<Partido>) request.getAttribute("partidosPendientes");
  DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<main class="container py-4 pb-5">
  <section class="admin-panel">
    <div class="admin-panel-header">
      <div>
        <p class="admin-eyebrow">Administrador</p>
        <h1>Cargar cruces eliminatorios</h1>
      </div>
      <span class="admin-badge">Mundial 2026</span>
    </div>

    <% if (request.getAttribute("mensajeExito") != null) { %>
      <div class="alert alert-success" role="alert">
        <%= request.getAttribute("mensajeExito") %>
      </div>
    <% } %>

    <% if (request.getAttribute("mensajeError") != null) { %>
      <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("mensajeError") %>
      </div>
    <% } %>

    <form action="cargar-partido" method="post" class="admin-form">
      <div class="row g-3">
        <div class="col-12">
          <label for="idPartido" class="form-label">Partido pendiente</label>
          <select id="idPartido" name="idPartido" class="form-select" required>
            <option value="">Seleccionar partido</option>
            <% if (partidosPendientes != null) {
              for (Partido partido : partidosPendientes) { %>
                <option value="<%= partido.getIdPartido() %>">
                  <%= partido.getEtapa().getNombreEtapa() %> -
                  <%= partido.getFechaHora().format(formatoFecha) %> -
                  <%= partido.getEstadio().getEstadio() %>
                </option>
            <% }
            } %>
          </select>
        </div>

        <div class="col-12 col-md-6">
          <label for="equipoLocal" class="form-label">Equipo local</label>
          <select id="equipoLocal" name="equipoLocal" class="form-select" required>
            <option value="">Seleccionar equipo</option>
            <% if (equipos != null) {
              for (Equipo equipo : equipos) { %>
                <option value="<%= equipo.getIdEquipo() %>"><%= equipo.getNombre() %></option>
            <% }
            } %>
          </select>
        </div>

        <div class="col-12 col-md-6">
          <label for="equipoVisitante" class="form-label">Equipo visitante</label>
          <select id="equipoVisitante" name="equipoVisitante" class="form-select" required>
            <option value="">Seleccionar equipo</option>
            <% if (equipos != null) {
              for (Equipo equipo : equipos) { %>
                <option value="<%= equipo.getIdEquipo() %>"><%= equipo.getNombre() %></option>
            <% }
            } %>
          </select>
        </div>
      </div>

      <div class="admin-actions">
        <button type="submit" class="btn btn-afa">
          <i class="bi bi-save-fill me-2"></i>Guardar partido
        </button>
      </div>
    </form>
  </section>
</main>

<%@ include file="footer.jsp" %>
