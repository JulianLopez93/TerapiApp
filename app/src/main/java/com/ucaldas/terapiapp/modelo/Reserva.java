package com.ucaldas.terapiapp.modelo;

public class Reserva {
    private int Id_Servicio;
    private int Id_Cliente;
    private int Id_EstadoReserva;
    private String Fecha;
    private String Hora;
    private String Lugar;
    private String Observaciones;

    public Reserva() {

    }

    public Reserva(int id_Servicio, int id_Cliente, int id_EstadoReserva, String fecha, String hora, String lugar, String observaciones) {
        Id_Servicio = id_Servicio;
        Id_Cliente = id_Cliente;
        Id_EstadoReserva = id_EstadoReserva;
        Fecha = fecha;
        Hora = hora;
        Lugar = lugar;
        Observaciones = observaciones;
    }

    public int getId_Servicio() {
        return Id_Servicio;
    }

    public void setId_Servicio(int id_Servicio) {
        Id_Servicio = id_Servicio;
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        Id_Cliente = id_Cliente;
    }

    public int getId_EstadoReserva() {
        return Id_EstadoReserva;
    }

    public void setId_EstadoReserva(int id_EstadoReserva) {
        Id_EstadoReserva = id_EstadoReserva;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
