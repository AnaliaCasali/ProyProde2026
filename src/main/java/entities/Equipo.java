package entities;

public class Equipo {
  private int idEquipo;
  private String nombre;
  private String icono;
  private String grupo;


  public Equipo(){

  }



  public Equipo(int idEquipo, String nombre, String icono, String grupo){
    this.idEquipo = idEquipo;
    this.nombre = nombre;
    this.icono = icono;
    this.grupo = grupo;
  }


  public int getIdEquipo() {
    return idEquipo;
  }

  public void setIdEquipo(int idEquipo) {
    this.idEquipo = idEquipo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  @Override
  public String toString() {
    return "Equipo{" +
        "idEquipo=" + idEquipo +
        ", nombre='" + nombre + '\'' +
        ", icono='" + icono + '\'' +
        ", grupo='" + grupo + '\'' +
        '}';
  }
}
