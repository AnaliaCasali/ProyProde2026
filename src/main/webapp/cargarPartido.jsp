<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="entities.Equipo" %>
<%@ page import="entities.Estadio" %>
<%@ page import="entities.Etapa" %>
<%@ include file="header.jsp" %>

<%
  List<Equipo> equipos = (List<Equipo>) request.getAttribute("equipos");
  List<Etapa> etapas = (List<Etapa>) request.getAttribute("etapas");
  List<Estadio> estadios = (List<Estadio>) request.getAttribute("estadios");
  String fechaHoraMinima = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
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
        <div class="col-12 col-md-6">
          <label for="idEtapa" class="form-label">Etapa</label>
          <select id="idEtapa" name="idEtapa" class="form-select" required>
            <option value="">Seleccionar etapa</option>
            <% if (etapas != null) {
              for (Etapa etapa : etapas) { %>
                <option value="<%= etapa.getIdEtapa() %>"><%= etapa.getNombreEtapa() %></option>
            <% }
            } %>
          </select>
        </div>

        <div class="col-12 col-md-6">
          <label for="fechaHora" class="form-label">Fecha y hora</label>
          <input id="fechaHora" name="fechaHora" type="datetime-local" class="form-control" min="<%= fechaHoraMinima %>" required>
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

        <div class="col-12">
          <label for="idEstadio" class="form-label">Estadio</label>
          <select id="idEstadio" name="idEstadio" class="form-select" required>
            <option value="">Seleccionar estadio</option>
            <% if (estadios != null) {
              for (Estadio estadio : estadios) { %>
                <option value="<%= estadio.getIdEstadio() %>">
                  <%= estadio.getEstadio() %> - <%= estadio.getCiudad() %>, <%= estadio.getPais() %>
                </option>
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
