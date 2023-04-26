package com.ucaldas.terapiapp.DAL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioReservacionFirebase {
    private FirebaseFunctions functions;

    public ServicioReservacionFirebase(){
        functions = FirebaseFunctions.getInstance();
    }
    public void crearReservacion(Reserva reservacion, View vista,AlertDialog cargandoAlerta) {

        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", reservacion.getFecha());
        data.put("Hora", reservacion.getHora());
        data.put("Id_Cliente", reservacion.getId_Cliente());
        data.put("Id_EstadoReserva", reservacion.getId_EstadoReserva());
        data.put("Id_Servicio", reservacion.getId_Servicio());
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
                                    fragmentTransaction.replace(R.id.fragment_container, new sobreNosotrosFragment());
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
                        ArrayList<Reserva> listaReserva = new ArrayList<>();
                        if (task.isSuccessful()) {
                            HttpsCallableResult result = task.getResult();
                            if (result != null) {
                                ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) result.getData();
                                listaReserva = data.stream().map(d -> {
                                    Reserva reserva = new Reserva();
                                    reserva.setId_Servicio((Integer)d.get("Id_Servicio"));
                                    reserva.setId_Cliente((Integer)d.get("Id_Cliente"));
                                    reserva.setId_EstadoReserva((Integer)d.get("Id_EstadoReserva"));
                                    reserva.setFecha((String) d.get("Fecha"));
                                    reserva.setHora((String) d.get("Hora"));
                                    reserva.setObservaciones((String) d.get("Observaciones"));
                                    reserva.setLugar((String) d.get("Lugar"));
                                    listaReservas.add(reserva);
                                    return reserva;
                                }).collect(Collectors.toCollection(ArrayList::new));
                            }
                        }
                        return listaReservas;
                    }
                });
    }
}