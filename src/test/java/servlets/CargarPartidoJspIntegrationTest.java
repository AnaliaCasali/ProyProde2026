package servlets;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Test de Integración: cargarPartido.jsp")
@Testcontainers
class CargarPartidoJspIntegrationTest {

  @Container
  static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName("prode")
      .withUsername("root")
      .withPassword("root")
      .withInitScript("db/init-test.sql");

  private static final String BASE_URL =
      "http://localhost:8080/ProyProde2026";

  @BeforeAll
  static void setupClass() {
    RestAssured.baseURI = BASE_URL;
    System.setProperty("DB_URL", mysql.getJdbcUrl());
    System.setProperty("DB_USER", mysql.getUsername());
    System.setProperty("DB_PASSWORD", mysql.getPassword());
  }

  // =========================================================
  // Estructura estática del JSP
  // =========================================================

  @Test
  @DisplayName("JSP -> debería renderizar el título de la sección")
  void testTituloSeccion() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("Cargar cruces eliminatorios"))
        .body(containsString("Administrador"))
        .body(containsString("Mundial 2026"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar los labels del formulario")
  void testLabelsFormulario() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("Etapa"))
        .body(containsString("Fecha y hora"))
        .body(containsString("Equipo local"))
        .body(containsString("Equipo visitante"))
        .body(containsString("Estadio"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar el formulario con action y method correctos")
  void testFormularioActionMethod() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("action=\"cargar-partido\""))
        .body(containsString("method=\"post\""));
  }

  @Test
  @DisplayName("JSP -> debería renderizar los selects con sus names")
  void testSelectsConNames() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("name=\"idEtapa\""))
        .body(containsString("name=\"fechaHora\""))
        .body(containsString("name=\"equipoLocal\""))
        .body(containsString("name=\"equipoVisitante\""))
        .body(containsString("name=\"idEstadio\""));
  }

  @Test
  @DisplayName("JSP -> debería renderizar el input fechaHora como datetime-local")
  void testInputFechaHoraTipo() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("type=\"datetime-local\""));
  }

  @Test
  @DisplayName("JSP -> debería renderizar el botón de envío")
  void testBotonSubmit() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("type=\"submit\""))
        .body(containsString("Guardar partido"));
  }

  // =========================================================
  // Contenido dinámico: listas desde la BD (request attributes)
  // =========================================================

  @Test
  @DisplayName("JSP -> debería renderizar la opción por defecto del select equipoLocal")
  void testSelectEquipoLocalOpcionDefecto() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("Seleccionar equipo"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar equipos en el select equipoLocal desde la BD")
  void testSelectEquipoLocalConOpciones() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("<select id=\"equipoLocal\""))
        .body(containsString("Argentina"))
        .body(containsString("Brasil"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar los mismos equipos en equipoVisitante")
  void testSelectEquipoVisitanteConOpciones() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("<select id=\"equipoVisitante\""))
        .body(containsString("Argentina"))
        .body(containsString("Brasil"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar la opción por defecto del select idEtapa")
  void testSelectEtapaOpcionDefecto() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("Seleccionar etapa"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar etapas en el select idEtapa desde la BD")
  void testSelectEtapaConOpciones() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("<select id=\"idEtapa\""))
        .body(containsString("Final"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar la opción por defecto del select idEstadio")
  void testSelectEstadioOpcionDefecto() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("Seleccionar estadio"));
  }

  @Test
  @DisplayName("JSP -> debería renderizar estadios en el select idEstadio desde la BD")
  void testSelectEstadioConOpciones() {

    given()
        .when()
        .get("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("<select id=\"idEstadio\""))
        .body(containsString("México"))
        .body(containsString("USA"));
  }

  // =========================================================
  // Renderizado condicional: mensajeExito (alert-success)
  // =========================================================

  @Test
  @DisplayName("JSP -> debería mostrar div alert-success cuando el servlet pone mensajeExito")
  void testMensajeExitoMuestraAlertSuccess() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 4)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("class=\"alert alert-success\""))
        .body(containsString("El partido se cargo correctamente."));
  }

  @Test
  @DisplayName("JSP -> no debería mostrar div alert-danger en POST exitoso")
  void testMensajeExitoNoMuestraAlertDanger() {

    Response response =
        given()
            .param("equipoLocal", 1)
            .param("equipoVisitante", 2)
            .param("idEtapa", 4)
            .param("idEstadio", 1)
            .param("fechaHora", "2026-06-01T14:30:00")
            .when()
            .post("/cargar-partido");

    response.then().statusCode(200);

    org.assertj.core.api.Assertions
        .assertThat(response.getBody().asString())
        .doesNotContain("alert-danger");
  }

  @Test
  @DisplayName("JSP -> no debería mostrar ninguna alerta en GET limpio")
  void testGetLimpioSinAlertas() {

    Response response =
        given()
            .when()
            .get("/cargar-partido");

    response.then().statusCode(200);

    org.assertj.core.api.Assertions
        .assertThat(response.getBody().asString())
        .doesNotContain("alert-success")
        .doesNotContain("alert-danger");
  }

  // =========================================================
  // Renderizado condicional: mensajeError (alert-danger)
  // =========================================================

  @Test
  @DisplayName("JSP -> debería mostrar div alert-danger cuando el servlet pone mensajeError")
  void testMensajeErrorMuestraAlertDanger() {

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
        .body(containsString("class=\"alert alert-danger\""))
        .body(containsString("El equipo local y el visitante no pueden ser el mismo."));
  }

  @Test
  @DisplayName("JSP -> no debería mostrar div alert-success cuando hay mensajeError")
  void testMensajeErrorNoMuestraAlertSuccess() {

    Response response =
        given()
            .param("equipoLocal", 1)
            .param("equipoVisitante", 1)
            .param("idEtapa", 1)
            .param("idEstadio", 1)
            .param("fechaHora", "2026-06-01T14:30:00")
            .when()
            .post("/cargar-partido");

    response.then().statusCode(200);

    org.assertj.core.api.Assertions
        .assertThat(response.getBody().asString())
        .doesNotContain("alert-success");
  }

  @Test
  @DisplayName("JSP -> debería mantener el formulario visible tras mostrar mensajeError")
  void testFormularioVisibleTrasMensajeError() {

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
        .body(containsString("alert-danger"))
        .body(containsString("name=\"equipoLocal\""))
        .body(containsString("name=\"equipoVisitante\""))
        .body(containsString("Guardar partido"));
  }

  @Test
  @DisplayName("JSP -> debería mantener el formulario visible tras mostrar mensajeExito")
  void testFormularioVisibleTrasMensajeExito() {

    given()
        .param("equipoLocal", 1)
        .param("equipoVisitante", 2)
        .param("idEtapa", 4)
        .param("idEstadio", 1)
        .param("fechaHora", "2026-06-01T14:30:00")
        .when()
        .post("/cargar-partido")
        .then()
        .statusCode(200)
        .body(containsString("alert-success"))
        .body(containsString("name=\"equipoLocal\""))
        .body(containsString("name=\"equipoVisitante\""))
        .body(containsString("Guardar partido"));
  }
}