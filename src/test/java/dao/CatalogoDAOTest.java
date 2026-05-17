package dao;

import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatalogoDAOTest {

  private CatalogoDAO catalogoDAO;
  private Connection mockConnection;
  private PreparedStatement mockPreparedStatement;
  private ResultSet mockResultSet;

  @BeforeEach
  void setUp() {
    // Configuracion comun para simular la base de datos en cada prueba
    catalogoDAO = spy(new CatalogoDAO());
    mockConnection = mock(Connection.class);
    mockPreparedStatement = mock(PreparedStatement.class);
    mockResultSet = mock(ResultSet.class);

    doReturn(mockConnection).when(catalogoDAO).obtenerConexion();
  }

  @Test
  void testObtenerEquipos() throws SQLException {
    // Arrange: Preparar mocks para simular la consulta de equipos
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, false);
    when(mockResultSet.getInt("idEquipo")).thenReturn(10);
    when(mockResultSet.getString("nombre")).thenReturn("Argentina");
    when(mockResultSet.getString("icono")).thenReturn("argentina.png");
    when(mockResultSet.getString("grupo")).thenReturn("A");

    // Act: Ejecutar el metodo
    List<Equipo> equipos = catalogoDAO.obtenerEquipos();

    // Assert: Verificar que se arme la lista esperada
    assertThat(equipos).hasSize(1);
    Equipo equipo = equipos.get(0);
    assertThat(equipo.getIdEquipo()).isEqualTo(10);
    assertThat(equipo.getNombre()).isEqualTo("Argentina");
    assertThat(equipo.getIcono()).isEqualTo("argentina.png");
    assertThat(equipo.getGrupo()).isEqualTo("A");

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).close();
    verify(mockPreparedStatement).close();
    verify(mockConnection).close();
  }

  @Test
  void testObtenerEtapasEliminatorias() throws SQLException {
    // Arrange: Preparar mocks para simular etapas eliminatorias
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, false);
    when(mockResultSet.getInt("idEtapa")).thenReturn(3);
    when(mockResultSet.getString("nombreEtapa")).thenReturn("Cuartos de Final");

    // Act: Ejecutar el metodo
    List<Etapa> etapas = catalogoDAO.obtenerEtapasEliminatorias();

    // Assert: Verificar parametros de filtro y resultado
    assertThat(etapas).hasSize(1);
    assertThat(etapas.get(0).getIdEtapa()).isEqualTo(3);
    assertThat(etapas.get(0).getNombreEtapa()).isEqualTo("Cuartos de Final");

    verify(mockPreparedStatement).setString(1, "Dieciseisavos de Final");
    verify(mockPreparedStatement).setString(2, "Octavos de Final");
    verify(mockPreparedStatement).setString(3, "Cuartos de Final");
    verify(mockPreparedStatement).setString(4, "Semifinal");
    verify(mockPreparedStatement).setString(5, "Final");
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).close();
    verify(mockPreparedStatement).close();
    verify(mockConnection).close();
  }

  @Test
  void testObtenerEstadios() throws SQLException {
    // Arrange: Preparar mocks para simular la consulta de estadios
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true, false);
    when(mockResultSet.getInt("idEstadio")).thenReturn(100);
    when(mockResultSet.getString("estadio")).thenReturn("Estadio Nacional");
    when(mockResultSet.getString("ciudad")).thenReturn("Ciudad");
    when(mockResultSet.getString("pais")).thenReturn("Pais");

    // Act: Ejecutar el metodo
    List<Estadio> estadios = catalogoDAO.obtenerEstadios();

    // Assert: Verificar que se arme la lista esperada
    assertThat(estadios).hasSize(1);
    Estadio estadio = estadios.get(0);
    assertThat(estadio.getIdEstadio()).isEqualTo(100);
    assertThat(estadio.getEstadio()).isEqualTo("Estadio Nacional");
    assertThat(estadio.getCiudad()).isEqualTo("Ciudad");
    assertThat(estadio.getPais()).isEqualTo("Pais");

    verify(mockConnection).prepareStatement(anyString());
    verify(mockPreparedStatement).executeQuery();
    verify(mockResultSet).close();
    verify(mockPreparedStatement).close();
    verify(mockConnection).close();
  }

  @Test
  void testObtenerEquiposCuandoHayError() throws SQLException {
    // Arrange: Simular un error de base de datos
    when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error SQL"));

    // Act & Assert: Verificar que el DAO lance RuntimeException con mensaje claro
    assertThatThrownBy(() -> catalogoDAO.obtenerEquipos())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error al obtener los equipos.");
  }
}
