package com.ucaldas.terapiapp.DAL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;

import java.util.HashMap;
import java.util.Map;

public class ServicioReservacionFirebase {
    private FirebaseFunctions functions;

    public ServicioReservacionFirebase(){
        functions = FirebaseFunctions.getInstance();
    }
    public void crearReservacion(Reserva reservacion, View vista) {

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
}