package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PartidoTest {

    private Partido partido;

    @BeforeEach
    void setUp() {
        // Inicialización común si es necesaria; aquí se puede dejar vacío o preparar un objeto base
        partido = new Partido();
    }

    @Test
    void getIdPartido() {
        // Arrange: Crear una instancia de Partido con un id específico
        Partido partidoConId = new Partido(1, null, null, null, null, null, 0, 0, false);

        // Act: Llamar al getter
        int id = partidoConId.getIdPartido();

        // Assert: Verificar que el id sea el esperado
        assertThat(id).isEqualTo(1);
    }

    @Test
    void setIdPartido() {
        // Arrange: Crear una instancia de Partido
        Partido partido = new Partido();

        // Act: Llamar al setter con un valor
        partido.setIdPartido(10);

        // Assert: Verificar que el getter devuelva el valor establecido
        assertThat(partido.getIdPartido()).isEqualTo(10);
    }

    @Test
    void getEquipoLocal() {
        // Arrange: Crear un equipo y un partido con ese equipo
        Equipo local = new Equipo();
        local.setNombre("Argentina");
        Partido partidoConLocal = new Partido(1, local, null, null, null, null, 0, 0, false);

        // Act: Llamar al getter
        Equipo resultado = partidoConLocal.getEquipoLocal();

        // Assert: Verificar que el equipo sea el esperado
        assertThat(resultado).isEqualTo(local);
    }

    @Test
    void setEquipoLocal() {
        // Arrange: Crear una instancia de Partido y un Equipo
        Partido partido = new Partido();
        Equipo local = new Equipo();
        local.setNombre("Brasil");

        // Act: Llamar al setter con el valor
        partido.setEquipoLocal(local);

        // Assert: Verificar que el getter devuelva el valor establecido
        assertThat(partido.getEquipoLocal()).isEqualTo(local);
    }

    @Test
    void getEquipoVisitante() {
        // Arrange
        Equipo visitante = new Equipo();
        visitante.setNombre("Francia");
        Partido partidoConVisitante = new Partido(1, null, visitante, null, null, null, 0, 0, false);

        // Act
        Equipo resultado = partidoConVisitante.getEquipoVisitante();

        // Assert
        assertThat(resultado).isEqualTo(visitante);
    }

    @Test
    void setEquipoVisitante() {
        // Arrange
        Partido partido = new Partido();
        Equipo visitante = new Equipo();
        visitante.setNombre("Alemania");

        // Act
        partido.setEquipoVisitante(visitante);

        // Assert
        assertThat(partido.getEquipoVisitante()).isEqualTo(visitante);
    }

    @Test
    void getEtapa() {
        // Arrange
        Etapa etapa = new Etapa(1, "Final");
        Partido partidoConEtapa = new Partido(1, null, null, etapa, null, null, 0, 0, false);

        // Act
        Etapa resultado = partidoConEtapa.getEtapa();

        // Assert
        assertThat(resultado).isEqualTo(etapa);
    }

    @Test
    void setEtapa() {
        // Arrange
        Partido partido = new Partido();
        Etapa etapa = new Etapa(2, "Semifinal");

        // Act
        partido.setEtapa(etapa);

        // Assert
        assertThat(partido.getEtapa()).isEqualTo(etapa);
    }

    @Test
    void getEstadio() {
        // Arrange
        Estadio estadio = new Estadio(1, "Estadio Azteca", "CDMX", "México");
        Partido partidoConEstadio = new Partido(1, null, null, null, estadio, null, 0, 0, false);

        // Act
        Estadio resultado = partidoConEstadio.getEstadio();

        // Assert
        assertThat(resultado).isEqualTo(estadio);
    }

    @Test
    void setEstadio() {
        // Arrange
        Partido partido = new Partido();
        Estadio estadio = new Estadio(2, "Estadio Akron", "Guadalajara", "México");

        // Act
        partido.setEstadio(estadio);

        // Assert
        assertThat(partido.getEstadio()).isEqualTo(estadio);
    }

    @Test
    void getFechaHora() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 11, 15, 0);
        Partido partidoConFecha = new Partido(1, null, null, null, null, fecha, 0, 0, false);

        // Act
        LocalDateTime resultado = partidoConFecha.getFechaHora();

        // Assert
        assertThat(resultado).isEqualTo(fecha);
    }

    @Test
    void setFechaHora() {
        // Arrange
        Partido partido = new Partido();
        LocalDateTime fecha = LocalDateTime.of(2026, 7, 19, 12, 0);

        // Act
        partido.setFechaHora(fecha);

        // Assert
        assertThat(partido.getFechaHora()).isEqualTo(fecha);
    }

    @Test
    void getGolesLocal() {
        // Arrange
        Partido partidoConGoles = new Partido(1, null, null, null, null, null, 3, 0, false);

        // Act
        int goles = partidoConGoles.getGolesLocal();

        // Assert
        assertThat(goles).isEqualTo(3);
    }

    @Test
    void setGolesLocal() {
        // Arrange
        Partido partido = new Partido();

        // Act
        partido.setGolesLocal(2);

        // Assert
        assertThat(partido.getGolesLocal()).isEqualTo(2);
    }

    @Test
    void getGolesVisitante() {
        // Arrange
        Partido partidoConGoles = new Partido(1, null, null, null, null, null, 0, 4, false);

        // Act
        int goles = partidoConGoles.getGolesVisitante();

        // Assert
        assertThat(goles).isEqualTo(4);
    }

    @Test
    void setGolesVisitante() {
        // Arrange
        Partido partido = new Partido();

        // Act
        partido.setGolesVisitante(1);

        // Assert
        assertThat(partido.getGolesVisitante()).isEqualTo(1);
    }

    @Test
    void isFinalizado() {
        // Arrange
        Partido partidoFinalizado = new Partido(1, null, null, null, null, null, 0, 0, true);

        // Act
        boolean terminado = partidoFinalizado.isFinalizado();

        // Assert
        assertThat(terminado).isTrue();
    }

    @Test
    void setFinalizado() {
        // Arrange
        Partido partido = new Partido();

        // Act
        partido.setFinalizado(true);

        // Assert
        assertThat(partido.isFinalizado()).isTrue();
    }

    @Test
    void testToString() {
        // Arrange
        Partido partido = new Partido(5, null, null, null, null, null, 2, 1, true);

        // Act
        String result = partido.toString();

        // Assert
        assertThat(result).contains("idPartido=5");
        assertThat(result).contains("golesLocal=2");
        assertThat(result).contains("finalizado=true");
    }

    @Test
    void testEquals() {
        // Arrange
        Equipo local = new Equipo();
        local.setNombre("A");
        Partido partido1 = new Partido(1, local, null, null, null, null, 0, 0, false);
        Partido partido2 = new Partido(1, local, null, null, null, null, 0, 0, false);

        // Act & Assert
        assertThat(partido1).isEqualTo(partido2);

        // Cambiando un valor para que no sean iguales
        partido2.setGolesLocal(1);
        assertThat(partido1).isNotEqualTo(partido2);
    }

    @Test
    void testHashCode() {
        // Arrange
        Partido partido1 = new Partido(1, null, null, null, null, null, 0, 0, false);
        Partido partido2 = new Partido(1, null, null, null, null, null, 0, 0, false);

        // Act & Assert
        assertThat(partido1.hashCode()).isEqualTo(partido2.hashCode());
    }

    @Test
    void getFechaHoraFormateada() {
        // Arrange: Partido con fecha
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 11, 15, 0);
        Partido partidoConFecha = new Partido(1, null, null, null, null, fecha, 0, 0, false);

        // Act
        String formateada = partidoConFecha.getFechaHoraFormateada();

        // Assert
        assertThat(formateada).isEqualTo("11/06/2026 - 15:00 hs");
    }

    @Test
    void getFechaHoraFormateada_FechaNula() {
        // Arrange: Partido sin fecha
        Partido partidoSinFecha = new Partido();
        partidoSinFecha.setFechaHora(null);

        // Act
        String formateada = partidoSinFecha.getFechaHoraFormateada();

        // Assert
        assertThat(formateada).isEqualTo("Fecha a confirmar");
    }

    // Pruebas adicionales para constructores
    @Test
    void testConstructorDefault() {
        // Arrange & Act
        Partido partido = new Partido();

        // Assert: Verificar valores por defecto
        assertThat(partido.getIdPartido()).isEqualTo(0);
        assertThat(partido.getFechaHora()).isNull();
        assertThat(partido.isFinalizado()).isFalse();
    }

    @Test
    void testConstructorCompleto() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 11, 15, 0);

        // Act
        Partido partido = new Partido(10, null, null, null, null, fecha, 2, 1, true);

        // Assert: Verificar valores inicializados
        assertThat(partido.getIdPartido()).isEqualTo(10);
        assertThat(partido.getGolesLocal()).isEqualTo(2);
        assertThat(partido.getGolesVisitante()).isEqualTo(1);
        assertThat(partido.isFinalizado()).isTrue();
        assertThat(partido.getFechaHora()).isEqualTo(fecha);
    }
}