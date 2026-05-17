<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="header.jsp" %>

 <main style="padding-top: 30px;">
   <div class="container">
     <h3>Cargar resultado</h3>

     <p style="color:red;">${mensaje}</p>

     <form class="card shadow p-4" method="post" action="${pageContext.request.contextPath}/cargar-resultado/${idPartido}">
       <!-- Equipo local -->
       <div class="row mb-3">
         <div class="col-md-6">
           <label for="equipoLocal" class="form-label fw-bold">Equipo local</label>
           <input type="text" disabled class="form-control" id="equipoLocal" value="${partido.equipoLocal.nombre}"
       readonly />
         </div>
         <div class="col-md-6">
           <label for="golesLocal" class="form-label fw-bold">Goles</label>
           <input type="number" class="form-control" id="golesLocal"
                  name="golesLocal" min="0" value="${partido.golesLocal}" required />
         </div>
       </div>

       <!-- Equipo visitante -->
       <div class="row mb-3">
         <div class="col-md-6">
           <label for="equipoVisitante" class="form-label fw-bold">Equipo visitante</label>
           <input type="text"  disabled class="form-control" id="equipoVisitante" value="${partido.equipoVisitante.nombre}"
                   readonly />
         </div>
         <div class="col-md-6">
           <label for="golesVisitante" class="form-label fw-bold">Goles</label>
           <input type="number" class="form-control" id="golesVisitante"
                  name="golesVisitante" min="0" required value="${partido.golesVisitante}" />
         </div>
       </div>

       <!-- Botón enviar -->
       <div class="d-grid">
         <button type="submit" class="btn btn-primary fw-bold">
            Guardar Resultado
         </button>
       </div>
     </form>
   </div>
 </main>

<%@ include file="footer.jsp" %>