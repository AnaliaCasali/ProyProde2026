package dao;

import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import entities.Partido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PartidoDAOTest {

  private PartidoDAO partidoDAO;
  private Connection mockConnection;
  private PreparedStatement mockPreparedStatement;
  private ResultSet mockResultSet;

  @BeforeEach
  void setUp() {
    //  Configuración rápida y mínima para cada prueba
    partidoDAO = spy(new PartidoDAO());

    // Usar spy para mockear métodos de instancia
    mockConnection = mock(Connection.class);
    mockPreparedStatement = mock(PreparedStatement.class);
    mockResultSet = mock(ResultSet.class);

    // Mockear el método obtenerConexion para devolver la conexión mockeada
    doReturn(mockConnection).when(partidoDAO).obtenerConexion();
  }

  @Test
  void testGetAll() throws SQLException {
    // Arrange: Preparar mocks para simular BD
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

    // Simular datos en ResultSet
    when(mockResultSet.next()).thenReturn(true, false);
    when(mockResultSet.getInt("idPartido")).thenReturn(1);
    when(mockResultSet.getInt("equipoLocal")).thenReturn(10);
    when(mockResultSet.getString("local_nombre")).thenReturn("Equipo Local");
    when(mockResultSet.getString("local_icono")).thenReturn("icono1.png");
    when(mockResultSet.getString("local_grupo")).thenReturn("A");
    when(mockResultSet.getInt("equipoVisitante")).thenReturn(20);
    when(mockResultSet.getString("visitante_nombre")).thenReturn("Equipo Visitante");
    when(mockResultSet.getString("visitante_icono")).thenReturn("icono2.png");
    when(mockResultSet.getString("visitante_grupo")).thenReturn("B");
    when(mockResultSet.getInt("idEtapa")).thenReturn(1);
    when(mockResultSet.getString("nombreEtapa")).thenReturn("Fase Grupos");
    when(mockResultSet.getInt("idEstadio")).thenReturn(100);
    when(mockResultSet.getString("estadio_nombre")).thenReturn("Estadio Nacional");
    when(mockResultSet.getString("ciudad")).thenReturn("Ciudad");
    when(mockResultSet.getString("pais")).thenReturn("País");
    when(mockResultSet.getTimestamp("fechaHora")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
    when(mockResultSet.getInt("golesLocal")).thenReturn(2);
    when(mockResultSet.getInt("golesVisitante")).thenReturn(1);
    when(mockResultSet.getBoolean("finalizado")).thenReturn(true);

    // Act: Ejecutar el método
    List<Partido> partidos = partidoDAO.getAll();

    // Assert: Verificar resultados (Self-validating)
    assertThat(partidos).hasSize(1);
    Partido partido = partidos.get(0);
    assertThat(partido.getIdPartido()).isEqualTo(1);
    assertThat(partido.getEquipoLocal().getNombre()).isEqualTo("Equipo Local");
    assertThat(partido.getEquipoVisitante().getNombre()).isEqualTo("Equipo Visitante");
    assertThat(partido.getEtapa().getNombreEtapa()).isEqualTo("Fase Grupos"); // Verifica Etapa
    assertThat(partido.getEstadio().getEstadio()).isEqualTo("Estadio Nacional");
    assertThat(partido.getGolesLocal()).isEqualTo(2);
    assertThat(partido.getGolesVisitante()).isEqualTo(1);
    assertThat(partido.isFinalizado()).isTrue();

    // Verificar interacciones con mocks
    verify(mockConnection).prepareStatement(PartidoDAO.SQL_GETALL);
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).close();
    verify(mockPreparedStatement).close();
    verify(mockConnection).close();
  }

  @Test
  void testInsert() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);
    ResultSet mockGeneratedKeys = mock(ResultSet.class);
    when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
    when(mockGeneratedKeys.next()).thenReturn(true);
    when(mockGeneratedKeys.getInt(1)).thenReturn(123);

    // Crear Partido con Etapa
    Equipo local = new Equipo(10, "Local", "icono.png", "A");
    Equipo visitante = new Equipo(20, "Visitante", "icono2.png", "B");
    Etapa etapa = new Etapa(1, "Fase Grupos");
    Estadio estadio = new Estadio(100, "Estadio", "Ciudad", "País");
    Partido partido = new Partido(0, local, visitante, etapa, estadio, LocalDateTime.now(), 2, 1, true);

    // Act
    partidoDAO.insert(partido);

    // Assert
    assertThat(partido.getIdPartido()).isEqualTo(123);
    verify(mockPreparedStatement).setInt(1, 10); // equipoLocal
    verify(mockPreparedStatement).setInt(2, 20); // equipoVisitante
    verify(mockPreparedStatement).setInt(3, 1); // idEtapa de Etapa
    verify(mockPreparedStatement).setInt(4, 100); // idEstadio
    verify(mockPreparedStatement).executeUpdate();
    verify(mockGeneratedKeys).getInt(1);
  }

  @Test
  void testGetById() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, false);

    // Configurar ResultSet similar a getAll
    when(mockResultSet.getInt("idPartido")).thenReturn(1);
    when(mockResultSet.getInt("equipoLocal")).thenReturn(10);
    when(mockResultSet.getString("local_nombre")).thenReturn("Equipo Local");
    when(mockResultSet.getString("local_icono")).thenReturn("icono1.png");
    when(mockResultSet.getString("local_grupo")).thenReturn("A");
    when(mockResultSet.getInt("equipoVisitante")).thenReturn(20);
    when(mockResultSet.getString("visitante_nombre")).thenReturn("Equipo Visitante");
    when(mockResultSet.getString("visitante_icono")).thenReturn("icono2.png");
    when(mockResultSet.getString("visitante_grupo")).thenReturn("B");
    when(mockResultSet.getInt("idEtapa")).thenReturn(1);
    when(mockResultSet.getString("nombreEtapa")).thenReturn("Fase Grupos");
    when(mockResultSet.getInt("idEstadio")).thenReturn(100);
    when(mockResultSet.getString("estadio_nombre")).thenReturn("Estadio Nacional");
    when(mockResultSet.getString("ciudad")).thenReturn("Ciudad");
    when(mockResultSet.getString("pais")).thenReturn("País");
    when(mockResultSet.getTimestamp("fechaHora")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
    when(mockResultSet.getInt("golesLocal")).thenReturn(2);
    when(mockResultSet.getInt("golesVisitante")).thenReturn(1);
    when(mockResultSet.getBoolean("finalizado")).thenReturn(true);

    // Act
    Partido partido = partidoDAO.getById(1);

    // Assert
    assertThat(partido).isNotNull();
    assertThat(partido.getIdPartido()).isEqualTo(1);
    assertThat(partido.getEtapa().getNombreEtapa()).isEqualTo("Fase Grupos");
    verify(mockPreparedStatement).setInt(1, 1);
  }

  @Test
  void testExistsById_True() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);

    // Act
    boolean exists = partidoDAO.existsById(1);

    // Assert
    assertThat(exists).isTrue();
  }

  @Test
  void testExistsById_False() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(false);

    // Act
    boolean exists = partidoDAO.existsById(999);

    // Assert
    assertThat(exists).isFalse();
  }

  @Test
  void testUpdate() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    // Mock existsById para que retorne true (usando spy para llamar al método real o mockearlo)
    doReturn(true).when(partidoDAO).existsById(1);

    Equipo local = new Equipo(10, "Local", "icono.png", "A");
    Equipo visitante = new Equipo(20, "Visitante", "icono2.png", "B");
    Etapa etapa = new Etapa(1, "Fase Grupos");
    Estadio estadio = new Estadio(100, "Estadio", "Ciudad", "País");
    Partido partido = new Partido(1, local, visitante, etapa, estadio, LocalDateTime.now(), 3, 0, false);

    // Act
    partidoDAO.update(partido);

    // Assert
    verify(mockPreparedStatement).setInt(1, 10);
    verify(mockPreparedStatement).setInt(2, 20);
    verify(mockPreparedStatement).setInt(3, 1); // idEtapa
    verify(mockPreparedStatement).setInt(4, 100);
    verify(mockPreparedStatement).setInt(9, 1); // idPartido
    verify(mockPreparedStatement).executeUpdate();
  }

  @Test
  void testDelete() throws SQLException {
    // Arrange
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    // Act
    partidoDAO.delete(1);

    // Assert
    verify(mockPreparedStatement).setInt(1, 1);
    verify(mockPreparedStatement).executeUpdate();
  }
}
