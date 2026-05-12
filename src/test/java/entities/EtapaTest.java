package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EtapaTest {

  private Etapa etapa;

  @BeforeEach
  void setUp() {
    // Inicialización común si es necesaria; aquí se puede dejar vacío o preparar un objeto base
    etapa = new Etapa();
  }

  @Test
  void getIdEtapa() {
    // Arrange: Crear una instancia de Etapa con un id específico
    Etapa etapaConId = new Etapa(1, "Fase Grupos");

    // Act: Llamar al getter
    int id = etapaConId.getIdEtapa();

    // Assert: Verificar que el id sea el esperado
    assertThat(id).isEqualTo(1);
  }

  @Test
  void setIdEtapa() {
    // Arrange: Crear una instancia de Etapa
    Etapa etapa = new Etapa();

    // Act: Llamar al setter con un valor
    etapa.setIdEtapa(5);

    // Assert: Verificar que el getter devuelva el valor establecido
    assertThat(etapa.getIdEtapa()).isEqualTo(5);
  }

  @Test
  void getNombreEtapa() {
    // Arrange: Crear una instancia de Etapa con nombre
    Etapa etapaConNombre = new Etapa(2, "Semifinales");

    // Act: Llamar al getter
    String nombre = etapaConNombre.getNombreEtapa();

    // Assert: Verificar que el nombre sea el esperado
    assertThat(nombre).isEqualTo("Semifinales");
  }

  @Test
  void setNombreEtapa() {
    // Arrange: Crear una instancia de Etapa
    Etapa etapa = new Etapa();

    // Act: Llamar al setter con un valor
    etapa.setNombreEtapa("Final");

    // Assert: Verificar que el getter devuelva el valor establecido
    assertThat(etapa.getNombreEtapa()).isEqualTo("Final");
  }

  @Test
  void testToString() {
    // Arrange: Crear una instancia de Etapa con valores específicos
    Etapa etapa = new Etapa(3, "Cuartos de Final");

    // Act: Llamar a toString
    String result = etapa.toString();

    // Assert: Verificar que el string sea el esperado
    assertThat(result).isEqualTo("etapas{id=3, nombreEtapa='Cuartos de Final'}");
  }

  // Pruebas adicionales para constructores (no estaban en el esqueleto, pero son útiles para cobertura completa)
  @Test
  void testConstructorDefault() {
    // Arrange & Act: Crear instancia con constructor por defecto
    Etapa etapa = new Etapa();

    // Assert: Verificar valores por defecto
    assertThat(etapa.getIdEtapa()).isEqualTo(0);
    assertThat(etapa.getNombreEtapa()).isNull();
  }

  @Test
  void testConstructorConId() {
    // Arrange & Act: Crear instancia con constructor que recibe id
    Etapa etapa = new Etapa(10);

    // Assert: Verificar id y que nombre sea null
    assertThat(etapa.getIdEtapa()).isEqualTo(10);
    assertThat(etapa.getNombreEtapa()).isNull();
  }

  @Test
  void testConstructorConIdYNombre() {
    // Arrange & Act: Crear instancia con constructor completo
    Etapa etapa = new Etapa(20, "Octavos");

    // Assert: Verificar ambos valores
    assertThat(etapa.getIdEtapa()).isEqualTo(20);
    assertThat(etapa.getNombreEtapa()).isEqualTo("Octavos");
  }
}