package com.example.controlpark.Model;

import java.io.Serializable;

public class Estacionamiento implements Serializable {

    private String nombre;
    private String dirección;
    private String descripcion;
    private double latitud;
    private double longitud;
    private int espaciosDisponibles;

    public Estacionamiento(){

    }
    public Estacionamiento(String nombre, String dirección,String descripcion, double latitud, double longitud, int espaciosDisponibles) {
        this.nombre = nombre;
        this.dirección = dirección;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.espaciosDisponibles = espaciosDisponibles;
    }


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public String getDescripcion() {

        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getEspaciosDisponibles() {
        return espaciosDisponibles;
    }
    public void setEspaciosDisponibles(int espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }


}
