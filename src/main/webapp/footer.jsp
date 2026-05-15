<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="mobile-nav">
  <a href="${pageContext.request.contextPath}/index.jsp" class="active">
    <i class="bi bi-house-door-fill"></i>
    <span>Inicio</span>
  </a>

  <%-- Apuntamos directo al SERVLET, no al .jsp suelto --%>
  <a href="${pageContext.request.contextPath}/jugar">
    <i class="bi bi-controller"></i>
    <span>Jugar</span>
  </a>

  <a href="${pageContext.request.contextPath}/PartidoServlet?accion=fixture">
    <i class="bi bi-calendar3"></i>
    <span>Fixture</span>
  </a>

  <a href="${pageContext.request.contextPath}/usuarios?accion=rankingGeneral">
    <i class="bi bi-trophy"></i>
    <span>Ranking</span>
  </a>

  <a href="${pageContext.request.contextPath}/como-jugar">
    <i class="bi bi-file-text"></i>
    <span>Reglas</span>
  </a>

  <c:if test="${not empty sessionScope.usuario}">
    <a href="${pageContext.request.contextPath}/informacionDeLaCuenta.jsp">
      <i class="bi bi-person-circle"></i> <%-- Cambiado por un icono más descriptivo de cuenta --%>
      <span>Mi Cuenta</span>
    </a>
  </c:if>
</div>

<script>
    (function() {
        // Buscamos si el div del reloj existe en la pantalla actual
        const relojElemento = document.getElementById("countdown-reloj");

        // Si no existe (porque estamos en el fixture, cuenta, etc.), frenamos el script acá
        if (!relojElemento) return;

        const fechaMundial = new Date("2026-06-11T16:00:00").getTime();

        actualizarCuentaRegresiva();
        const intervalo = setInterval(actualizarCuentaRegresiva, 1000);

        function actualizarCuentaRegresiva() {
            const ahora = new Date().getTime();
            const diferencia = fechaMundial - ahora;

            if (diferencia <= 0) {
                clearInterval(intervalo);
                relojElemento.innerText = "¡ARRANCÓ EL MUNDIAL!";
                relojElemento.style.color = "var(--afa-amarillo, #ffc107)";
                return;
            }

            const dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
            const horas = Math.floor((diferencia % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutos = Math.floor((diferencia % (1000 * 60 * 60)) / (1000 * 60));
            const segundos = Math.floor((diferencia % (1000 * 60)) / 1000);

            const dd = dias < 10 ? "0" + dias : dias;
            const hh = horas < 10 ? "0" + horas : horas;
            const mm = minutes = minutos < 10 ? "0" + minutos : minutos;
            const ss = segundos < 10 ? "0" + segundos : segundos;

            relojElemento.innerText = dd + ":" + hh + ":" + mm + ":" + ss;
        }
    })();
</script>