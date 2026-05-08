<!doctype html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Mundial Prode - Inicio</title>

    <!-- Bootstrap 5 -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"
    />

    <!-- CSS Personalizado -->
    <link rel="stylesheet" href="../css/estilo.css" />
  </head>
  <body>
    <!-- NAVBAR SUPERIOR -->
    <nav class="navbar navbar-dark navbar-afa sticky-top shadow">
      <div class="container-fluid navbar-container">
        <!-- IZQUIERDA: Logo FIFA -->
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/a/a7/FIFA_World_Cup_2026_Logo.svg"
          class="logo-fifa"
          alt="FIFA 2026"
        />

        <!-- CENTRO: Título y Logo AFA -->
        <div class="navbar-brand-center">
          <span class="navbar-brand fw-bold m-0">PRODE MUNDIAL</span>
          <img
            src="https://upload.wikimedia.org/wikipedia/en/3/3d/Argentina_national_football_team_logo.svg"
            class="logo-afa"
            alt="AFA"
          />
        </div>

        <!-- Botón Hamburguesa (PC) -->
        <button
          class="navbar-toggler border-0"
          type="button"
          data-bs-toggle="offcanvas"
          data-bs-target="#menu"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
      </div>
    </nav>

    <!-- MENÚ LATERAL (OFFCANVAS) -->
    <div class="offcanvas offcanvas-end" id="menu">
      <div class="offcanvas-header border-bottom">
        <h5 class="offcanvas-title fw-bold">MENÚ</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="offcanvas"
        ></button>
      </div>
      <div class="offcanvas-body p-0">
        <div class="list-group list-group-flush">
          <a
            href="index.html"
            class="list-group-item list-group-item-action active"
            >Inicio</a
          >
          <a href="jugar.html" class="list-group-item list-group-item-action"
            >Jugar</a
          >
          <a href="ranking.html" class="list-group-item list-group-item-action"
            >Ranking</a
          >
          <a href="fixture.html" class="list-group-item list-group-item-action"
            >Fixture</a
          >
        </div>
      </div>
    </div>

    <!-- CONTENIDO PRINCIPAL -->
    <main>
      <section class="hero-inicio mb-4">
        <h1 class="fw-bold">¡BIENVENIDO!</h1>
        <div class="countdown-box my-3">38 DÍAS</div>
        <p class="small text-uppercase fw-bold">Para el mundial</p>
      </section>

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
          <a href="jugar.html" class="btn btn-afa shadow btn-lg"
            >EMPEZAR A JUGAR</a
          >
        </div>
      </div>
    </main>

    <!-- NAVEGACIÓN INFERIOR (MOBILE) -->
    <div class="mobile-nav">
      <a href="index.html" class="active">
        <i class="bi bi-house-door-fill"></i>
        <span>Inicio</span>
      </a>
      <a href="jugar.html">
        <i class="bi bi-controller"></i>
        <span>Jugar</span>
      </a>
      <a href="fixture.html">
        <i class="bi bi-calendar3"></i>
        <span>Fixture</span>
      </a>
      <a href="ranking.html">
        <i class="bi bi-trophy"></i>
        <span>Ranking</span>
      </a>
      <a href="como-jugar">
        <i class="bi bi-file-text"></i>
        <span>Reglas</span>
      </a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
