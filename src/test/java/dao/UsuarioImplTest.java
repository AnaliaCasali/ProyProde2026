package dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UsuarioImplTest {

    private UsuarioImpl usuarioDAO;

    private Connection mockConnection;

    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() {

        // Arrange : se preparan los mocks de conexión y preparedStatement
        usuarioDAO = spy(new UsuarioImpl());

        mockConnection = mock(Connection.class);

        mockPreparedStatement = mock(PreparedStatement.class);

        // Se mockea la conexión a la BD
        doReturn(mockConnection).when(usuarioDAO).obtenerConexion();
    }

    @Test
    void testUpdatePassword() throws SQLException {

        // Arrange : se simula el comportamiento de la BD
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act : se ejecuta el metodo updatePassword
        usuarioDAO.updatePassword(1, "passwordEncriptada");

        // Assert : se verifica que los parámetros y ejecución sean correctos
        verify(mockPreparedStatement).setString(1, "passwordEncriptada");

        verify(mockPreparedStatement).setInt(2, 1);

        verify(mockPreparedStatement).executeUpdate();

        verify(mockPreparedStatement).close();

        verify(mockConnection).close();
    }
}