package com.ucaldas.terapiapp.DAL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.fragmentos.ServiciosFragment;
import com.ucaldas.terapiapp.modelo.Cliente;
import com.ucaldas.terapiapp.modelo.EstadoReserva;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioReservacionFirebase {
    private final FirebaseFunctions functions;

    public ServicioReservacionFirebase(){
        functions = FirebaseFunctions.getInstance();
    }
    public void crearReservacion(Reserva reservacion, View vista,AlertDialog cargandoAlerta) {

        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", reservacion.getFecha());
        data.put("Hora", reservacion.getHora());
        data.put("Id_Cliente", reservacion.getCliente().getId());
        data.put("Id_EstadoReserva", reservacion.getEstadoReserva().getId());
        data.put("Id_Servicio", reservacion.getServicio().getId());
        data.put("Lugar", reservacion.getLugar());
        data.put("Observaciones", reservacion.getObservaciones());
        functions
                .getHttpsCallable("crearReserva")
                .call(data)
                .addOnSuccessListener(result -> {
                    cargandoAlerta.dismiss();
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Reserva Creada")
                            .setMessage("La reserva para el dia: "+reservacion.getFecha()+" a las: "+reservacion.getHora()+ " fue realizada correctamente")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    FragmentManager fragmentManager = ((AppCompatActivity) vista.getContext()).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new ServiciosFragment());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }).show();
                })
                .addOnFailureListener(e -> {
                    cargandoAlerta.dismiss();
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Error")
                            .setMessage(e.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    vista.findViewById(R.id.btnReservar).setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).show();
                });
    }

    public Task<String> horasOcupadas(String fecha) {

        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", fecha);

        return functions
                .getHttpsCallable("consultarReservasFecha")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = task.getResult().getData().toString();
                        return result;
                    }
                });
    }

    public Task<ArrayList<Reserva>> listarReservas(String fecha) {
        ArrayList<Reserva> listaReservas = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", fecha);
        return functions
                .getHttpsCallable("consultarReservasPorFecha")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<Reserva>>() {
                    @Override
                    public ArrayList<Reserva> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()) {
                            HttpsCallableResult result = task.getResult();
                            if (result != null) {
                                ArrayList<Map<String, Object>> informacion = (ArrayList<Map<String, Object>>) result.getData();
                                for (Map<String, Object> d : informacion) {
                                    Reserva reserva = new Reserva();
                                    Servicio servicio = new Servicio();
                                    servicio.setNombre((String)d.get("NombreServicio"));
                                    servicio.setPrecio(Double.parseDouble(d.get("PrecioServicio")+""));
                                    servicio.setDuracion((Integer) d.get("DuracionServicio"));
                                    servicio.setDescripcion((String)d.get("DescripcionServicio"));
                                    servicio.setMateriales((String)d.get("MaterialesServicio"));
                                    servicio.setProcedimiento((String)d.get("ProcedimientoServicio"));
                                    servicio.setImagenes((ArrayList<String>) d.get("ImagenesServicio"));

                                    Cliente cliente = new Cliente();
                                    cliente.setId((String)d.get("Id_Cliente"));

                                    EstadoReserva estadoReserva = new EstadoReserva();
                                    estadoReserva.setId((String)d.get("Id_EstadoReserva"));
                                    estadoReserva.setNombre((String)d.get("NombreEstadoReserva"));

                                    reserva.setServicio(servicio);
                                    reserva.setCliente(cliente);
                                    reserva.setEstadoReserva(estadoReserva);
                                    reserva.setFecha((String) d.get("Fecha"));
                                    reserva.setHora((String) d.get("Hora"));
                                    reserva.setObservaciones((String) d.get("Observaciones"));
                                    reserva.setLugar((String) d.get("Lugar"));

                                    listaReservas.add(reserva);
                                }
                            }
                        }else{
                            Log.d("hola", "then: error");
                        }
                        return listaReservas;
                    }
                });
    }

    public Task<ArrayList<Reserva>> listarReservasPorCliente(String idCliente) {
        ArrayList<Reserva> listaReservas = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("Id_Cliente", idCliente);
        return functions
                .getHttpsCallable("consultarReservasPorCliente")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<Reserva>>() {
                    @Override
                    public ArrayList<Reserva> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()) {
                            HttpsCallableResult result = task.getResult();
                            if (result != null) {
                                //Obtener la informacion de las reservas y guardarla en un arraylist de tipo reserva
                                ArrayList<Map<String, Object>> informacion = (ArrayList<Map<String, Object>>) result.getData();
                                for (Map<String, Object> d : informacion) {
                                    Reserva reserva = new Reserva();
                                    Servicio servicio = new Servicio();
                                    servicio.setNombre((String)d.get("NombreServicio"));
                                    servicio.setPrecio(Double.parseDouble(d.get("PrecioServicio")+""));
                                    servicio.setDuracion((Integer) d.get("DuracionServicio"));
                                    servicio.setDescripcion((String)d.get("DescripcionServicio"));
                                    servicio.setMateriales((String)d.get("MaterialesServicio"));
                                    servicio.setProcedimiento((String)d.get("ProcedimientoServicio"));
                                    servicio.setImagenes((ArrayList<String>) d.get("ImagenesServicio"));

                                    Cliente cliente = new Cliente();
                                    cliente.setId((String)d.get("Id_Cliente"));

                                    EstadoReserva estadoReserva = new EstadoReserva();
                                    estadoReserva.setId((String)d.get("Id_EstadoReserva"));
                                    estadoReserva.setNombre((String)d.get("NombreEstadoReserva"));

                                    reserva.setServicio(servicio);
                                    reserva.setCliente(cliente);
                                    reserva.setEstadoReserva(estadoReserva);
                                    reserva.setFecha((String) d.get("Fecha"));
                                    reserva.setHora((String) d.get("Hora"));
                                    reserva.setObservaciones((String) d.get("Observaciones"));
                                    reserva.setLugar((String) d.get("Lugar"));

                                    listaReservas.add(reserva);
                                }
                                // Crear un comparador que compare las reservas por EstadoReserva
                                Comparator<Reserva> comparador = new Comparator<Reserva>() {
                                    @Override
                                    public int compare(Reserva reserva1, Reserva reserva2) {
                                        return reserva1.getEstadoReserva().getNombre().compareTo(reserva2.getEstadoReserva().getNombre());
                                    }
                                };

                               // Ordenar la lista de reservas por EstadoReserva
                                Collections.sort(listaReservas, comparador);

                            }
                        }else{
                            Log.d("hola", "then: error");
                        }
                        return listaReservas;
                    }
                });
    }
}