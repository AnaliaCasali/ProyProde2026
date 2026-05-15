package servlets;

import dao.CatalogoDAO;
import dao.PartidoDAO;

import entities.Partido;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;


/**
 * Test de Integración: CargarPartidoServlet
 *
 * Estrategia:
 *  - Testcontainers levanta un MySQL real con el esquema de prueba (db/init-test.sql).
 *  - La aplicación debe estar desplegada en Tomcat (localhost:8080).
 *    Para CI, usar el plugin cargo-maven-plugin o un contenedor Tomcat aparte.
 *  - Las System properties DB_URL / DB_USER / DB_PASSWORD inyectan la BD del
 *    contenedor a la app (la app debe leerlas en su DataSource/ConnectionPool).
 *
 * Prerequisitos de init-test.sql:
 *  - Al menos 4 equipos (id 1-4)
 *  - Al menos 2 etapas (id 1-2)
 *  - Al menos 2 estadios (id 1-2)
 */

@DisplayName("Test de Integración: CargarPartidoServlet")
@Testcontainers
class CargarPartidoServletIntegrationTest {


  @Container
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName("prode")
      .withUsername("root")
      .withPassword("root")
      .withInitScript("db/init-test.sql");

  private static final String BASE_URL =
      "http://localhost:8080/ProyProde2026";

  private PartidoDAO partidoDAO;

  @BeforeAll
  static void setupClass() {

    RestAssured.baseURI = BASE_URL;

    System.setProperty("DB_URL", mysql.getJdbcUrl());
    System.setProperty("DB_USER", mysql.getUsername());
    System.setProperty("DB_PASSWORD", mysql.getPassword());
  }

  @BeforeEach
  void setup() {
    //catalogoDAO = new CatalogoDAO();
    partidoDAO = new PartidoDAO();
  }

  // =========================================================
  // GET
  // =========================================================

  @Test
  @DisplayName("GET /cargar-partido -> debería cargar formulario")
  void testGetFormulario() {

    given()
        .log().all()
      .when()
        .get("/cargar-partido")
        .then()
        .log().all()
        .statusCode(200)
        .header("Content-Type", containsString("text/html"))
        .body(containsString("Equipo local"))
        .body(containsString("Equipo visitante"))
        .body(containsString("Estadio"))
        .body(containsString("Fecha y hora"));
  }
  @Test
  @DisplayName("GET /cargar-partido -> debería responder rápido")
  void testGetPerformance() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .time(lessThan(5000L));
  }

  // =========================================================
  // POST EXITOSO
  // =========================================================

  @Test
  @DisplayName("POST /cargar-partido -> debería guardar partido")
  void testPostPartidoExitoso() {

    Response response =
        given()
            .log().all()
            .param("equipoLocal", 1)
            .param("equipoVisitante", 2)
            .param("idEtapa", 1)
            .param("idEstadio", 1)
            .param("fechaHora", "2026-06-01T14:30:00")
            .when()
            .post("/cargar-partido");

    response.then()
        .log().all()
        .statusCode(200)
        .body(containsString(
            "El partido se cargo correctamente."
        ));

    List<Partido> partidos = partidoDAO.getAll();

    org.assertj.core.api.Assertions.assertThat(partidos)
        .isNotEmpty()
        .anySatisfy(partido -> {

          org.assertj.core.api.Assertions.assertThat(
              partido.getEquipoLocal().getIdEquipo()
          ).isEqualTo(1);

          org.assertj.core.api.Assertions.assertThat(
              partido.getEquipoVisitante().getIdEquipo()
          ).isEqualTo(2);
        });
  }

  @Test
  @DisplayName("POST /cargar-partido -> debería crear valores iniciales correctos")
  void testPostValoresIniciales() {

    given()
        .param("equipoLocal", 3)
        .param("equipoVisitante", 4)
        .param("idEtapa", 2)
        .param("idEstadio", 2)
        .param("fechaHora", "2026-07-15T18:00:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "El partido se cargo correctamente."
        ));

    List<Partido> partidos = partidoDAO.getAll();

    org.assertj.core.api.Assertions.assertThat(partidos)
        .filteredOn(p ->
            p.getEquipoLocal().getIdEquipo() == 3)
        .anySatisfy(partido -> {

          org.assertj.core.api.Assertions.assertThat(
              partido.getGolesLocal()
          ).isZero();

          org.assertj.core.api.Assertions.assertThat(
              partido.getGolesVisitante()
          ).isZero();

          org.assertj.core.api.Assertions.assertThat(
              partido.isFinalizado()
          ).isFalse();
        });
  }

  // =========================================================
  // VALIDACIONES
  // =========================================================

  @Test
  @DisplayName("POST -> debería rechazar sin equipo local")
  void testSinEquipoLocal() {

    given()
        .param("equipoVisitante", 2)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "Debe seleccionar el equipo local."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar sin equipo visitante")
  void testSinEquipoVisitante() {

    given()
        .param("equipoLocal", 1)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "Debe seleccionar el equipo visitante."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar sin etapa")
  void testSinEtapa() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "Debe seleccionar una etapa."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar sin estadio")
  void testSinEstadio() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "Debe seleccionar un estadio."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar sin fecha")
  void testSinFecha() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "Debe ingresar la fecha y hora del partido."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar fecha inválida")
  void testFechaInvalida() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .param("fechaHora", "fecha-invalida")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "La fecha y hora ingresada no es valida."
        ));
  }

  @Test
  @DisplayName("POST -> debería rechazar equipos iguales")
  void testEquiposIguales() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 1)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString(
            "El equipo local y el visitante no pueden ser el mismo."
        ));
  }

  @Test
  @DisplayName("POST -> debería responder rápido")
  void testPerformance() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 1)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .time(lessThan(3000L));
  }
}