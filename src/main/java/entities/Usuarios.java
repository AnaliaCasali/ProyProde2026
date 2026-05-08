package entities;

import enums.TipoUsuario;

public class Usuarios {
  private int idUsuario;
  private String email;
  private String password;
  private TipoUsuario tipo;
  private String curso;
  private String carrera;
  private String nombreGrupo;

  public Usuarios() {

  }

  public Usuarios(String email, String password, String curso, String carrera, String nombreGrupo) {
    this.email = email;
    this.password = password;
    this.curso = curso;
    this.carrera = carrera;
    this.nombreGrupo = nombreGrupo;
  }

  public Usuarios(String email, String password, TipoUsuario tipo, String curso, String carrera) {
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
  } // Se agrega setter de idUsuario por problemas en JugadaDAO a la hora de mapear los datos en el rs

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

  public String getCurso() {
    return curso;
  }

  public void setCurso(String curso) {
    this.curso = curso;
  }

  public String getCarrera() {
    return carrera;
  }

  public void setCarrera(String carrera) {
    this.carrera = carrera;
  }

  public String getNombreGrupo() {
    return nombreGrupo;
  }

  public void setNombreGrupo(String nombreGrupo) {
    this.nombreGrupo = nombreGrupo;
  }

  // métodos
  // tostring

  // equals

  // hashcode

  // compare/comparable

}
