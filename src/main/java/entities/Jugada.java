package entities;


import java.util.Objects;

public class Jugada {
    private int idJugada;
    private int idUsuario;
    private int idPartido;
    private int golesLocal;
    private int golesVisitante;
    private int puntaje;

    public int getIdJugada() {
        return idJugada;
    }

    public void setIdJugada(int idJugada) {
        this.idJugada = idJugada;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Jugada jugada = (Jugada) o;
        return idJugada == jugada.idJugada && idUsuario == jugada.idUsuario && idPartido == jugada.idPartido && golesLocal == jugada.golesLocal && golesVisitante == jugada.golesVisitante && puntaje == jugada.puntaje;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugada, idUsuario, idPartido, golesLocal, golesVisitante, puntaje);
    }

    @Override
    public String toString() {
        return "Jugada{" +
                "idJugada=" + idJugada +
                ", idUsuario=" + idUsuario +
                ", idPartido=" + idPartido +
                ", golesLocal=" + golesLocal +
                ", golesVisitante=" + golesVisitante +
                ", puntaje=" + puntaje +
                '}';
    }
}
