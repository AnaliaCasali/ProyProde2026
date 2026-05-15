<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

    <!-- 1.CONTENIDO PRINCIPAL -->
    <main>
      <div class="countdown-section text-center my-5">

          <h1 class="countdown-title text-uppercase mb-4">
              ¡BIENVENIDO!
          </h1>

          <div class="countdown-board shadow-lg">
              <div class="countdown-inner">
                  <div id="countdown-reloj" class="countdown-timer font-monospace">
                      00:00:00:00
                  </div>

                  <div class="countdown-labels text-uppercase fw-bold mt-2">
                      <span>Días</span>
                      <span>Horas</span>
                      <span>Min</span>
                      <span>Seg</span>
                  </div>
              </div>
          </div>

          <h3 class="countdown-subtitle text-uppercase fw-bold mt-4">
              Para el Mundial
          </h3>

      </div>

      <div class="container pb-5">

        <!-- SECCIÓN TOP 3 RANKING -->
        <div class="mb-5">
          <h6 class="fw-bold mb-3 d-flex justify-content-between align-items-center">
            <span><i class="bi bi-trophy-fill text-warning me-2"></i> TOP 3 GENERAL</span>
            <a href="ranking.html" class="text-decoration-none small" style="color: var(--afa-celeste)">Ver todo</a>
          </h6>

          <!-- Puesto 1 -->
          <div class="capsula-prode puesto-1 py-2 d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center style="min-width: 0;">
              <span class="fw-bold fs-5 text-warning">#1</span>
              <div style="width: 35px; height: 35px; background: #eee; border-radius: 50%; overflow: hidden;">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Juan" alt="avatar">
              </div>
              <span class="fw-bold">3ro TSDS</span>
            </div>
            <div class="puntos-badge">1250 pts</div>
          </div>

          <!-- Puesto 2 -->
          <div class="capsula-prode puesto-2 py-2 d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center style="min-width: 0;">
              <span class="fw-bold fs-5 text-secondary">#2</span>
              <div style="width: 35px; height: 35px; background: #eee; border-radius: 50%; overflow: hidden;">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Maria" alt="avatar">
              </div>
              <span class="fw-bold">2do Lengua y Literatura</span>
            </div>
            <div class="puntos-badge">1180 pts</div>
          </div>

          <!-- Puesto 3 -->
          <div class="capsula-prode puesto-3 py-2 d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center style="min-width: 0;">
              <span class="fw-bold fs-5" style="color: #cd7f32;">#3</span>
              <div style="width: 35px; height: 35px; background: #eee; border-radius: 50%; overflow: hidden;">
                <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Heber" alt="avatar">
              </div>
              <span class="fw-bold">1ro Primaria</span>
            </div>
            <div class="puntos-badge">1050 pts</div>
          </div>
        </div>

      <div class="container pb-5">
        <!-- JORNADA 11/06 -->
        <div class="mb-4">
          <h6 class="fw-bold mb-3 border-bottom pb-2">
            <i class="bi bi-calendar-event text-primary"></i> JORNADA 11/06
          </h6>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/mx.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">MÉXICO</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                4:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/za.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">SUDÁFRICA</span>
            </div>
          </div>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/kr.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">COREA DEL SUR</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                11:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/cz.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">CHEQUIA</span>
            </div>
          </div>
        </div>

        <!-- JORNADA 12/06 -->
        <div class="mb-4">
          <h6 class="fw-bold mb-3 border-bottom pb-2">
            <i class="bi bi-calendar-event text-primary"></i> JORNADA 12/06
          </h6>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/ca.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">CANADÁ</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                4:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/ba.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">BOSNIA Y H.</span>
            </div>
          </div>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/us.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">EE. UU.</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                10:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/py.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">PARAGUAY</span>
            </div>
          </div>
        </div>

        <!-- JORNADA 13/06 -->
        <div class="mb-4">
          <h6 class="fw-bold mb-3 border-bottom pb-2">
            <i class="bi bi-calendar-event text-primary"></i> JORNADA 13/06
          </h6>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/qa.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">CATAR</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                4:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/ch.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">SUIZA</span>
            </div>
          </div>

          <div class="capsula-prode">
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/br.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">BRASIL</span>
            </div>
            <div class="versus">
              <div
                class="badge bg-light text-dark border mb-1"
                style="font-size: 0.6rem"
              >
                7:00 PM
              </div>
              <div class="h5 fw-bold m-0">VS</div>
            </div>
            <div class="equipo">
              <img
                src="https://flagcdn.com/w80/ma.png"
                style="width: 35px"
                class="mb-1 shadow-sm"
              />
              <span class="small fw-bold">MARRUECOS</span>
            </div>
          </div>
        </div>

        <div class="text-center mt-4">
          <a href="${pageContext.request.contextPath}/jugar" class="btn btn-afa shadow btn-lg"
            >EMPEZAR A JUGAR</a
          >
        </div>
      </div>
    </main>

<%@ include file="footer.jsp" %>
