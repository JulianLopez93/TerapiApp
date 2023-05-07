package com.ucaldas.terapiapp.modelo;

public class EstadoReserva {
    String Id;
    String Nombre;

    public EstadoReserva() {
    }

    public EstadoReserva(String id, String nombre) {
        Id = id;
        Nombre = nombre;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
