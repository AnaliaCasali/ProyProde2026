package enums;

public enum Carrera {
  TECNICATURA_SUPERIOR_EN_DESARROLLO_DE_SOFTWARE("Tecnicatura Superior en Desarrollo de Software"),
  PROFESORADO_DE_EDUCACION_PRIMARIA("Profesorado de Educación Primaria"),
  PROFESORADO_EN_LENGUA_Y_LITERATURA("Profesorado de Lengua y Literatura"),
  TECNICATURA_EN_ADMINISTRACION_RURAL("Tecnicatura en Administración Rural");

  private final String descripcion;

  Carrera(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
