package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {
    private int idPartido;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private etapas etapa;
    private Estadio estadio;
    private LocalDateTime fechaHora;
    private int golesLocal;
    private int golesVisitante;
    private boolean finalizado;


    //Constructor vacío.
    public Partido() {}

    //Constructor con todos los parametros
    public Partido(int idPartido, Equipo equipoLocal, Equipo equipoVisitante, etapas etapa, Estadio estadio, LocalDateTime fechaHora, int golesLocal, int golesVisitante, boolean finalizado) {
        this.idPartido = idPartido;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.etapa = etapa;
        this.estadio = estadio;
        this.fechaHora = fechaHora;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.finalizado = finalizado;
    }

    //GETT
    public int getIdPartido() {return idPartido;}
    public Equipo getEquipoLocal() {return equipoLocal;}
    public Equipo getEquipoVisitante() {return equipoVisitante;}
    public etapas getEtapa() {return etapa;}
    public Estadio getEstadio() {return estadio;}
    public LocalDateTime getFechaHora() {return fechaHora;}
    public int getGolesLocal() {return golesLocal;}
    public int getGolesVisitante() {return golesVisitante;}
    public boolean isFinalizado() {return finalizado;}

    //SETT
    public void setIdPartido(int idPartido) {this.idPartido = idPartido;}
    public void setEquipoLocal(Equipo equipoLocal) {this.equipoLocal = equipoLocal;}
    public void setEquipoVisitante(Equipo equipoVisitante) {this.equipoVisitante = equipoVisitante;}
    public void setEtapa(etapas etapa) {this.etapa = etapa;}
    public void setEstadio(Estadio estadio) {this.estadio = estadio;}
    public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora = fechaHora;}
    public void setGolesLocal(int golesLocal) {this.golesLocal = golesLocal;}
    public void setGolesVisitante(int golesVisitante) {this.golesVisitante = golesVisitante;}
    public void setFinalizado(boolean finalizado) {this.finalizado = finalizado;}

    @Override
    public String toString() {
        return "Partido{" +
                "idPartido=" + idPartido +
                ", equipolocal=" + equipoLocal +
                ", equipoVisitante=" + equipoVisitante +
                ", etapa=" + etapa +
                ", estadio=" + estadio +
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
        return idPartido == partido.idPartido && golesLocal == partido.golesLocal && golesVisitante == partido.golesVisitante && finalizado == partido.finalizado && Objects.equals(equipoLocal, partido.equipoLocal) && Objects.equals(equipoVisitante, partido.equipoVisitante) && Objects.equals(etapa, partido.etapa) && Objects.equals(estadio, partido.estadio) && Objects.equals(fechaHora, partido.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido, equipoLocal, equipoVisitante, etapa, estadio, fechaHora, golesLocal, golesVisitante, finalizado);
    }
}
