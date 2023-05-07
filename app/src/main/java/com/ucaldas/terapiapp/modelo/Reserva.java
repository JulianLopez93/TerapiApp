package com.ucaldas.terapiapp.modelo;

public class Reserva {
    private Servicio Servicio;
    private Cliente Cliente;
    private EstadoReserva EstadoReserva;
    private String Fecha;
    private String Hora;
    private String Lugar;
    private String Observaciones;

    public Reserva() {

    }

    public Reserva(Servicio servicio, Cliente cliente, EstadoReserva estadoReserva, String fecha, String hora, String lugar, String observaciones) {
        Servicio = servicio;
        Cliente = cliente;
        EstadoReserva = estadoReserva;
        Fecha = fecha;
        Hora = hora;
        Lugar = lugar;
        Observaciones = observaciones;
    }

    public Servicio getServicio() {
        return Servicio;
    }

    public void setServicio(Servicio servicio) {
        Servicio = servicio;
    }

    public Cliente getCliente() {
        return Cliente;
    }

    public void setCliente(Cliente cliente) {
        Cliente = cliente;
    }

    public EstadoReserva getEstadoReserva() {
        return EstadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        EstadoReserva = estadoReserva;
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
