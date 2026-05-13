package entities;

import enums.Carrera;
import enums.TipoUsuario;

import java.util.Objects;

public class Usuario implements Comparable {
  private int idUsuario;
  private String email;
  private String password;
  private TipoUsuario tipo;
  private String curso;
  private Carrera carrera;
  private String nombreGrupo;

  private int puntajeTotal;

  public Usuario() {
  }

  public Usuario(String email, String password, TipoUsuario tipo, String curso, Carrera carrera, String nombreGrupo) {
    this.email = email;
    this.password = password;
    this.tipo = tipo;
    this.curso = curso;
    this.carrera = carrera;
    this.nombreGrupo = nombreGrupo;
  }

  public Usuario(String email, String password, TipoUsuario tipo, String curso, Carrera carrera) {
    this.email = email;
    this.password = password;
    this.tipo = tipo;
    this.curso = curso;
    this.carrera = carrera;
  }

  public int getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public TipoUsuario getTipo() {
    return tipo;
  }

  public void setTipo(TipoUsuario tipo) {
    this.tipo = tipo;
  }

  public String getCurso() {
    return curso;
  }

  public void setCurso(String curso) {
    this.curso = curso;
  }

  public Carrera getCarrera() {
    return carrera;
  }

  public void setCarrera(Carrera carrera) {
    this.carrera = carrera;
  }

  public String getNombreGrupo() {
    return nombreGrupo;
  }

  public void setNombreGrupo(String nombreGrupo) {
    this.nombreGrupo = nombreGrupo;
  }

  public int getPuntajeTotal() {
    return puntajeTotal;
  }

  public void setPuntajeTotal(int puntajeTotal) {
    this.puntajeTotal = puntajeTotal;
  }

  // métodos
  // equals y hashcode solo para email - idUsuario
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Usuario otro = (Usuario) o;
    return idUsuario == otro.idUsuario &&
        Objects.equals(email, otro.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario, email);
  }

  @Override
  public int compareTo(Object o) {
    Usuario otro = (Usuario) o;
    int comparacionEmail = this.email.compareTo(otro.email);

    if (comparacionEmail != 0)
      return comparacionEmail;

    return this.password.compareTo(otro.password);
  }
}
