package com.ucaldas.terapiapp.DAL;

import static android.content.ContentValues.TAG;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioReservacionFirebase {
    private FirebaseFunctions functions;
    private FirebaseFirestore db;

    public ServicioReservacionFirebase() {
        functions = FirebaseFunctions.getInstance();
        db = FirebaseFirestore.getInstance();
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
                            .setMessage("La reserva para el dia: " + reservacion.getFecha() + " a las: " + reservacion.getHora() + " fue realizada correctamente")
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

    public  void listarReservas(View vista) {
        functions
                .getHttpsCallable("consultarReservas")
                .call()
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        // manejar el resultado
                        ArrayList<HashMap<String, Object>> listaReservas = (ArrayList<HashMap<String, Object>>) httpsCallableResult.getData();
                        //ArrayList<String> listaReservas = (ArrayList<String>) httpsCallableResult.getData();
                        Log.d(TAG, "Lista de reservas: "+listaReservas);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // manejar el error
                        Log.e(TAG, "Error al llamar a la función: ", e);
                    }
                });
    }

/*
    public  void listarReservas() {
        ArrayList<Reserva> reservas=new ArrayList<>();

        db.collection("Reserva")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Reserva reserva=document.toObject(Reserva.class);
                                reservas.add(reserva);
                                Log.d(TAG,reserva.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        Log.d(TAG,"Reservas: "+reservas.toString());
                    }
                });

    }

 */


}
