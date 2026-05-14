<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>¿Como jugar?</title>

    <!-- Fuente -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- CSS -->
    <link rel="stylesheet" href="css/estilo.css">

</head>

<body>

<div class="container py-4">

    <div class="card-como-jugar">

        <h1 class="titulo-seccion">
            <i class="bi bi-controller"></i>
            ¿Cómo jugar?
        </h1>


        <div class="regla-box">
            <i class="bi bi-person-check-fill icono-regla"></i>

            <div>
                <h3>1. Iniciá sesión</h3>

                <p>
                    Para participar del Prode tenés que estar registrado e iniciar sesión con tu cuenta.
                </p>
            </div>
        </div>

        <div class="regla-box">
            <i class="bi bi-controller icono-regla"></i>

            <div>
                <h3>2. Entrá a “Jugar”</h3>

                <p>
                    Desde la sección “Jugar” podés cargar tus pronósticos para cada partido de la jornada.
                </p>
            </div>
        </div>

        <div class="regla-box">
            <i class="bi bi-clock-fill icono-regla"></i>

            <div>
                <h3>3. Tiempo límite</h3>

                <p>
                    tenes tiempo hasta 15 min antes de cada partido para subir tus jugadas.
                </p>
            </div>
        </div>

        <div class="regla-box">
            <i class="bi bi-floppy-fill icono-regla"></i>

            <div>
                <h3>4. Guardá tus jugadas</h3>

                <p>
                    Antes de salir, asegurate de presionar el botón “Guardar pronóstico” para no perder tus resultados cargados.
                </p>
            </div>
        </div>

        <p class="subtitulo">
            Sumá puntos acertando resultados y goles exactos.
        </p>

        <div class="regla-box">
            <i class="bi bi-check-circle-fill icono-regla"></i>
            <div>
                <h3>Resultado correcto</h3>
                <p>+50 puntos por acertar ganador o empate.</p>
            </div>
        </div>

        <div class="regla-box">
            <i class="bi bi-trophy-fill icono-regla"></i>
            <div>
                <h3>Goles exactos</h3>
                <p>+20 puntos por cada cantidad exacta de goles acertada, pudiendo sumar un maxico de +40 puntos por ambos marcadores acertados.</p>
            </div>
        </div>
        <div class="ejemplos-capsula">
<h2 class="mt-4">📊 Ejemplos de puntuación</h2>

<div class="contenedor-ejemplos">

    <div class="ejemplo-item acierto-total">
        <h3>✅ Acierto completo</h3>

        <p><strong>Pronóstico:</strong> Argentina 3 - 0</p>
        <p><strong>Resultado real:</strong> Argentina 3 - 0</p>

        <div class="puntos-box">
            +90 puntos
        </div>
    </div>

    <div class="ejemplo-item acierto-parcial">
        <h3>🔵 Un gol exacto</h3>

        <p><strong>Pronóstico:</strong> Argentina 3 - 0</p>
        <p><strong>Resultado real:</strong> Argentina 3 - 1</p>

        <div class="puntos-box">
            +70 puntos
        </div>
    </div>

    <div class="ejemplo-item solo-resultado">
        <h3>🟡 Solo resultado correcto</h3>

        <p><strong>Pronóstico:</strong> Argentina 3 - 0</p>
        <p><strong>Resultado real:</strong> Argentina 2 - 1</p>

        <div class="puntos-box">
            +50 puntos
        </div>
    </div>

    <div class="ejemplo-item ningun-resultado">
        <h3>🔴 Ningún resultado correcto</h3>

        <p><strong>Pronóstico:</strong> Argentina 0 - 0</p>
        <p><strong>Resultado real:</strong> Argentina 2 - 1</p>

        <div class="puntos-box">
            +0 puntos
        </div>
    </div>

</div>
</div>

        <a href="index.jsp" class="btn-volver">
            <i class="bi bi-arrow-left"></i>
            Volver al inicio
        </a>

    </div>

</div>

</body>
</html>