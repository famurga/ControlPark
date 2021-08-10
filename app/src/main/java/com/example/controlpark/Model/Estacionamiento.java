package com.example.controlpark.Model;

import java.io.Serializable;

public class Estacionamiento implements Serializable {

    private String nombre;
   private String direccion;
    private String descripcion;
    private double latitud;
    private double longitud;
    private int espacios;
    private double precio;

    public Estacionamiento(){

    }
    public Estacionamiento(String nombre, String direccion,String descripcion, double latitud, double longitud, int espacios,double precio) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.espacios = espacios;
        this.precio = precio;

    }


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirección() {

        return direccion;
    }

    public void setDirección(String direccion) {
        this.direccion = direccion;
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

    public int getEspacios() {

        return espacios;
    }
    public void setEspacios(int espacios) {
        this.espacios = espacios;
    }
    public double getPrecio() {

        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }


}
