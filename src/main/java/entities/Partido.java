package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {
    private int idPartido;
    private String equipolocal;
    private String equipoVisitante;
    //private Etapa etapa;
    //private Estadio estadio;

    // Cambiado a LocalDateTime para manejar fechas y horas modernas de manera eficiente
    private LocalDateTime fechaHora;

    private int golesLocal;
    private int golesVisitante;
    private boolean finalizado;


    //Se crea el constructor y se lo deja comentado debido a que no dispongo de las demas entidades.
    /*public Partido(int idPartido, String equipolocal, String equipoVisitante, Etapa etapa, Estadio estadio, LocalDateTime fechaHora, int golesLocal, int golesVisitante, boolean finalizado) {
        this.idPartido = idPartido;
        this.equipolocal = equipolocal;
        this.equipoVisitante = equipoVisitante;
        this.etapa = etapa;
        this.estadio = estadio;
        this.fechaHora = fechaHora;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.finalizado = finalizado;
    }
     */

    //GET
    public int getIdPartido() {return idPartido;}
    public String getEquipolocal() {return equipolocal;}
    public String getEquipoVisitante() {return equipoVisitante;}
    public LocalDateTime getFechaHora() {return fechaHora;} // Actualizado a LocalDateTime
    public int getGolesLocal() {return golesLocal;}
    public int getGolesVisitante() {return golesVisitante;}
    public boolean isFinalizado() {return finalizado;}

    //SETT
    public void setIdPartido(int idPartido) {this.idPartido = idPartido;}
    public void setEquipolocal(String equipolocal) {this.equipolocal = equipolocal;}
    public void setEquipoVisitante(String equipoVisitante) {this.equipoVisitante = equipoVisitante;}
    public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora = fechaHora;} // Actualizado a LocalDateTime
    public void setGolesLocal(int golesLocal) {this.golesLocal = golesLocal;}
    public void setGolesVisitante(int golesVisitante) {this.golesVisitante = golesVisitante;}
    public void setFinalizado(boolean finalizado) {this.finalizado = finalizado;}

    @Override
    public String toString() {
        return "Partido{" +
            "idPartido=" + idPartido +
            ", equipolocal='" + equipolocal + '\'' +
            ", equipoVisitante='" + equipoVisitante + '\'' +
            ", fechaHora=" + fechaHora +
            ", golesLocal=" + golesLocal +
            ", golesVisitante=" + golesVisitante +
            ", finalizado=" + finalizado +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return idPartido == partido.idPartido && golesLocal == partido.golesLocal && golesVisitante == partido.golesVisitante && finalizado == partido.finalizado && Objects.equals(equipolocal, partido.equipolocal) && Objects.equals(equipoVisitante, partido.equipoVisitante) && Objects.equals(fechaHora, partido.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, equipolocal, equipoVisitante, fechaHora, golesLocal, golesVisitante, finalizado);
    }
}