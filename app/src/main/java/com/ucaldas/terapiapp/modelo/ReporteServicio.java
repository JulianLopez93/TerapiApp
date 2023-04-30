package com.ucaldas.terapiapp.modelo;

public class ReporteServicio {
    private String nombre;
    private double precio;
    private int duracion;
    private int cantidad;

    public ReporteServicio() {}

    public ReporteServicio(String nombre, double precio, int duracion, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.duracion = duracion;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
