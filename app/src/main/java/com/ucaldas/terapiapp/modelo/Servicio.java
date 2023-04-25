package com.ucaldas.terapiapp.modelo;

import java.util.ArrayList;

public class Servicio {

    private String Nombre;
    private ArrayList<String> Imagenes;
    private int Duracion;
    private Double Precio;
    private String Materiales;
    private String Descripcion;
    private String Procedimiento;

    public Servicio() {
        Imagenes = new ArrayList<>();
    }

    public Servicio(String nombre, ArrayList<String> imagenes, int duracion, Double precio, String materiales, String descripcion, String procedimiento) {
        Nombre = nombre;
        Imagenes = imagenes;
        Duracion = duracion;
        Precio = precio;
        Materiales = materiales;
        Descripcion = descripcion;
        Procedimiento = procedimiento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public ArrayList<String> getImagenes() {
        return Imagenes;
    }

    public void agregarImagen(String url){
        Imagenes.add(url);
    }

    public void setImagenes(ArrayList<String> imagenes) {
        Imagenes = imagenes;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int duracion) {
        Duracion = duracion;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public String getMateriales() {
        return Materiales;
    }

    public void setMateriales(String materiales) {
        Materiales = materiales;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getProcedimiento() {
        return Procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        Procedimiento = procedimiento;
    }
}
