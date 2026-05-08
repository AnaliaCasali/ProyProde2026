package dao;

import entities.Jugada;
import interfaces.AdmConexion;
import interfaces.DAO;

import java.util.List;

public class JugadaDAO implements DAO<Jugada, Integer>, AdmConexion {

    private static final String SQL_INSERT =
            "INSERT INTO Jugadas (idUsuario, idPartido, golesLocal, golesVisitante) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE Jugadas SET golesLocal = ?, golesVisitante = ? WHERE idJugada = ?";
    private static final String SQL_DELETE =
            "DELETE FROM Jugadas WHERE idJugada = ?";
    private static final String SQL_GETALL =
            "SELECT idJugada, idUsuario, idPartido, golesLocal, golesVisitante, puntaje FROM Jugadas";
    private static final String SQL_GETBYID =
            "SELECT idJugada, idUsuario, idPartido, golesLocal, golesVisitante, puntaje FROM Jugadas WHERE idJugada = ?";
    private static final String SQL_EXISTBYID =
            "SELECT COUNT(*) FROM Jugadas WHERE idJugada = ?";

    @Override
    public List<Jugada> getAll() {
        return List.of();
    }

    @Override
    public void insert(Jugada objeto) {

    }

    @Override
    public void update(Jugada objeto) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Jugada getById(Integer id) {
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }
}
