package entities;

public class etapas {
  private int id;
  private String nombreEtapa;


  public etapas(int id) {
    this.id = id;
  }

  public etapas(int id, String nombreEtapa) {
    this.id = id;
    this.nombreEtapa = nombreEtapa;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombreEtapa() {
    return nombreEtapa;
  }

  public void setNombreEtapa(String nombreEtapa) {
    this.nombreEtapa = nombreEtapa;
  }

  @Override
  public String toString() {
    return "etapas{" +
        "id=" + id +
        ", nombreEtapa='" + nombreEtapa + '\'' +
        '}';
  }
}
