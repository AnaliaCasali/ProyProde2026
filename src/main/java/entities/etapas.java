package entities;

public class etapas {
  private int idEtapa;
  private String nombreEtapa;

  //Cree el constructor vacio para poder usarlo en PartidoDAO
  public etapas() {}

  public etapas(int idEtapa) {
    this.idEtapa = idEtapa;
  }

  public etapas(int idEtapa, String nombreEtapa) {
    this.idEtapa = idEtapa;
    this.nombreEtapa = nombreEtapa;
  }

  public int getIdEtapa() {
    return idEtapa;
  }

  public void setIdEtapa(int idEtapa) {
    this.idEtapa = idEtapa;
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
        "id=" + idEtapa +
        ", nombreEtapa='" + nombreEtapa + '\'' +
        '}';
  }
}
