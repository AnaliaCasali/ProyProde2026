package entities;

import java.util.Objects;

public class Equipo {
    private int idEquipo;
    private String nombre;
    private String icono;
    private String grupo;

    //Constructor Vacio
    public Equipo() {}

    //Constructor con Parametros
    public Equipo(int idEquipo, String nombre, String icono, String grupo) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.icono = icono;
        this.grupo = grupo;
    }

    //GETT
    public int getIdEquipo() {return idEquipo;}
    public String getNombre() {return nombre;}
    public String getIcono() {return icono;}
    public String getGrupo() {return grupo;}

    //SETT
    public void setIdEquipo(int idEquipo) {this.idEquipo = idEquipo;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setIcono(String icono) {this.icono = icono;}
    public void setGrupo(String grupo) {this.grupo = grupo;}

    @Override
    public String toString() {
        return "Equipo{" +
                "idEquipo=" + idEquipo +
                ", nombre='" + nombre + '\'' +
                ", icono='" + icono + '\'' +
                ", grupo='" + grupo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return idEquipo == equipo.idEquipo && Objects.equals(nombre, equipo.nombre) && Objects.equals(icono, equipo.icono) && Objects.equals(grupo, equipo.grupo);
    }

    @Override
    public int hashCode() {return Objects.hash(idEquipo, nombre, icono, grupo);}
}
