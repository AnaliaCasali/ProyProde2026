package entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase Estadio estructurada como JavaBean para su uso en JSP.
 */
public class Estadio implements Serializable {

    // Los atributos deben ser privados
    private int idEstadio;
    private String estadio;
    private String ciudad;
    private String pais;

    // REQUISITO JSP: Constructor vacío obligatorio
    public Estadio() {
    }

    // Constructor útil para instanciar rápido desde un DAO o Servlet
    public Estadio(int idEstadio, String estadio, String ciudad, String pais) {
        this.idEstadio = idEstadio;
        this.estadio = estadio;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // REQUISITO JSP: Getters y Setters
    public int getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(int idEstadio) {
        this.idEstadio = idEstadio;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    // Implementación de Equals y HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estadio estadio1 = (Estadio) o;
        return idEstadio == estadio1.idEstadio &&
                Objects.equals(estadio, estadio1.estadio) &&
                Objects.equals(ciudad, estadio1.ciudad) &&
                Objects.equals(pais, estadio1.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstadio, estadio, ciudad, pais);
    }
}