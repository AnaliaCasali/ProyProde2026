package servlets;

import dao.CatalogoDAO;
import dao.PartidoDAO;
import entities.Equipo;
import entities.Estadio;
import entities.Etapa;
import entities.Partido;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CargarPartidoServletTest {

  private CargarPartidoServlet servlet;
  private CatalogoDAO mockCatalogoDAO;
  private PartidoDAO mockPartidoDAO;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private List<Equipo> equipos;
  private List<Etapa> etapas;
  private List<Estadio> estadios;

  @BeforeEach
  void setUp() throws Exception {
    // Configuracion comun para probar el servlet sin usar base de datos real
    servlet = new CargarPartidoServlet();
    mockCatalogoDAO = mock(CatalogoDAO.class);
    mockPartidoDAO = mock(PartidoDAO.class);
    mockRequest = mock(HttpServletRequest.class);
    mockResponse = mock(HttpServletResponse.class);
    mockRequestDispatcher = mock(RequestDispatcher.class);

    setField(servlet, "catalogoDAO", mockCatalogoDAO);
    setField(servlet, "partidoDAO", mockPartidoDAO);

    equipos = Arrays.asList(new Equipo(1, "Local", "local.png", "A"));
    etapas = Arrays.asList(new Etapa(2, "Octavos de Final"));
    estadios = Arrays.asList(new Estadio(3, "Estadio", "Ciudad", "Pais"));

    when(mockRequest.getRequestDispatcher("cargarPartido.jsp")).thenReturn(mockRequestDispatcher);
    when(mockCatalogoDAO.obtenerEquipos()).thenReturn(equipos);
    when(mockCatalogoDAO.obtenerEtapasEliminatorias()).thenReturn(etapas);
    when(mockCatalogoDAO.obtenerEstadios()).thenReturn(estadios);
  }

  @Test
  void testDoGetCargaFormulario() throws Exception {
    // Act: Ejecutar el GET del servlet
    servlet.doGet(mockRequest, mockResponse);

    // Assert: Verificar que cargue catalogos y envie al JSP
    verify(mockRequest).setAttribute("equipos", equipos);
    verify(mockRequest).setAttribute("etapas", etapas);
    verify(mockRequest).setAttribute("estadios", estadios);
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  void testDoPostInsertaPartidoCorrectamente() throws Exception {
    // Arrange: Preparar parametros validos del formulario
    when(mockRequest.getParameter("equipoLocal")).thenReturn("1");
    when(mockRequest.getParameter("equipoVisitante")).thenReturn("2");
    when(mockRequest.getParameter("idEtapa")).thenReturn("3");
    when(mockRequest.getParameter("idEstadio")).thenReturn("4");
    when(mockRequest.getParameter("fechaHora")).thenReturn(LocalDateTime.now().plusDays(1).toString());
    doNothing().when(mockPartidoDAO).insert(any(Partido.class));

    // Act: Ejecutar el POST del servlet
    servlet.doPost(mockRequest, mockResponse);

    // Assert: Verificar que se cree el partido y se muestre mensaje de exito
    ArgumentCaptor<Partido> partidoCaptor = ArgumentCaptor.forClass(Partido.class);
    verify(mockPartidoDAO).insert(partidoCaptor.capture());

    Partido partido = partidoCaptor.getValue();
    assertThat(partido.getEquipoLocal().getIdEquipo()).isEqualTo(1);
    assertThat(partido.getEquipoVisitante().getIdEquipo()).isEqualTo(2);
    assertThat(partido.getEtapa().getIdEtapa()).isEqualTo(3);
    assertThat(partido.getEstadio().getIdEstadio()).isEqualTo(4);
    assertThat(partido.getGolesLocal()).isEqualTo(0);
    assertThat(partido.getGolesVisitante()).isEqualTo(0);
    assertThat(partido.isFinalizado()).isFalse();

    verify(mockRequest).setAttribute("mensajeExito", "El partido se cargo correctamente.");
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  void testDoPostConEquiposIgualesMuestraError() throws Exception {
    // Arrange: Preparar formulario invalido con el mismo equipo
    when(mockRequest.getParameter("equipoLocal")).thenReturn("1");
    when(mockRequest.getParameter("equipoVisitante")).thenReturn("1");
    when(mockRequest.getParameter("idEtapa")).thenReturn("3");
    when(mockRequest.getParameter("idEstadio")).thenReturn("4");
    when(mockRequest.getParameter("fechaHora")).thenReturn(LocalDateTime.now().plusDays(1).toString());

    // Act: Ejecutar el POST del servlet
    servlet.doPost(mockRequest, mockResponse);

    // Assert: Verificar que no guarde y muestre el error esperado
    verify(mockPartidoDAO, never()).insert(any(Partido.class));
    verify(mockRequest).setAttribute("mensajeError", "El equipo local y el visitante no pueden ser el mismo.");
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  void testDoPostConFechaInvalidaMuestraError() throws Exception {
    // Arrange: Preparar formulario con fecha invalida
    when(mockRequest.getParameter("equipoLocal")).thenReturn("1");
    when(mockRequest.getParameter("equipoVisitante")).thenReturn("2");
    when(mockRequest.getParameter("idEtapa")).thenReturn("3");
    when(mockRequest.getParameter("idEstadio")).thenReturn("4");
    when(mockRequest.getParameter("fechaHora")).thenReturn("fecha-invalida");

    // Act: Ejecutar el POST del servlet
    servlet.doPost(mockRequest, mockResponse);

    // Assert: Verificar que no guarde y muestre el error esperado
    verify(mockPartidoDAO, never()).insert(any(Partido.class));
    verify(mockRequest).setAttribute("mensajeError", "La fecha y hora ingresada no es valida.");
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  void testDoPostCuandoFallaInsertMuestraErrorDeBase() throws Exception {
    // Arrange: Preparar formulario valido y simular error al guardar
    when(mockRequest.getParameter("equipoLocal")).thenReturn("1");
    when(mockRequest.getParameter("equipoVisitante")).thenReturn("2");
    when(mockRequest.getParameter("idEtapa")).thenReturn("3");
    when(mockRequest.getParameter("idEstadio")).thenReturn("4");
    when(mockRequest.getParameter("fechaHora")).thenReturn(LocalDateTime.now().plusDays(1).toString());
    doThrow(new RuntimeException("Error SQL")).when(mockPartidoDAO).insert(any(Partido.class));

    // Act: Ejecutar el POST del servlet
    servlet.doPost(mockRequest, mockResponse);

    // Assert: Verificar mensaje generico de error de base
    verify(mockRequest).setAttribute("mensajeError", "No se pudo guardar el partido en la base de datos.");
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  void testDoGetCuandoFallaCatalogoCargaListasVacias() throws Exception {
    // Arrange: Simular error al cargar catalogos
    when(mockCatalogoDAO.obtenerEquipos()).thenThrow(new RuntimeException("Error al obtener los equipos."));

    // Act: Ejecutar el GET del servlet
    servlet.doGet(mockRequest, mockResponse);

    // Assert: Verificar listas vacias y mensaje de error
    verify(mockRequest).setAttribute("equipos", Collections.emptyList());
    verify(mockRequest).setAttribute("etapas", Collections.emptyList());
    verify(mockRequest).setAttribute("estadios", Collections.emptyList());
    verify(mockRequest).setAttribute("mensajeError", "Error al obtener los equipos.");
    verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}
