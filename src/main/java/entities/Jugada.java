package entities;

import java.util.Objects;

public class Jugada {
  private int idJugada;
  private Usuario usuario;
  private Partido partido;
  private int golesLocal;
  private int golesVisitante;
  private int puntaje;

  public Jugada() {
  }

  public Jugada (int idJugada, Usuario usuario, Partido partido, int golesLocal, int golesVisitante, int puntaje) {
    this.idJugada = idJugada;
    this.usuario = usuario;
    this.partido = partido;
    this.golesLocal = golesLocal;
    this.golesVisitante = golesVisitante;
    this.puntaje = puntaje;
  }

  public int getIdJugada() {
    return idJugada;
  }

  public void setIdJugada(int idJugada) {
    this.idJugada = idJugada;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Partido getPartido() {
    return partido;
  }

  public void setPartido(Partido partido) {
    this.partido = partido;
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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Jugada jugada = (Jugada) o;
    return idJugada == jugada.idJugada &&
        Objects.equals(usuario, jugada.usuario) &&
        Objects.equals(partido, jugada.partido);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idJugada, usuario, partido);
  }

  @Override
  public String toString() {
    return "Jugada{" +
        "idJugada=" + idJugada +
        ", usuario=" + (usuario != null ? usuario.getIdUsuario() : "null") +
        ", partido=" + (partido != null ? partido.getIdPartido() : "null") +
        ", golesLocal=" + golesLocal +
        ", golesVisitante=" + golesVisitante +
        ", puntaje=" + puntaje +
        '}';
  }
}