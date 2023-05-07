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
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;
import com.ucaldas.terapiapp.modelo.ReporteServicio;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioServicioFirebase {

    private FirebaseFunctions functions;

    private ArrayList<Servicio> listaServicios = new ArrayList<>();

    public ServicioServicioFirebase(){
        functions = FirebaseFunctions.getInstance();
    }

    public void crearServicio(Servicio servicio, View vista,AlertDialog cargandoAlerta) {

        Map<String, Object> data = new HashMap<>();
        data.put("Nombre", servicio.getNombre());
        data.put("Imagenes", servicio.getImagenes());
        data.put("Duracion", servicio.getDuracion());
        data.put("Precio", servicio.getPrecio());
        data.put("Materiales", servicio.getMateriales());
        data.put("Descripcion", servicio.getDescripcion());
        data.put("Procedimiento", servicio.getProcedimiento());
        functions
                .getHttpsCallable("crearServicio")
                .call(data)
                .addOnSuccessListener(result -> {
                    cargandoAlerta.dismiss();
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Servicio Creado")
                            .setMessage("El servicio "+servicio.getNombre()+" Fue Creado Correctamente")
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
                                    vista.findViewById(R.id.btnCrearServicio).setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).show();
                });
    }

    public Task<ArrayList<Servicio>> listarServicios()
    {

        return functions
                .getHttpsCallable("consultarServicios")
                .call()
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<Servicio>>() {
                @Override
                public ArrayList<Servicio> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                    if (task.isSuccessful()) {
                        HttpsCallableResult result = task.getResult();
                        if (result != null) {
                            ArrayList<Map<String, Object>> informacion = (ArrayList<Map<String, Object>>) result.getData();
                            for (Map<String, Object> d : informacion) {
                                Log.d("hola2", d.get("Nombre").toString());
                                Servicio servicio = new Servicio();
                                servicio.setNombre((String) d.get("Nombre"));
                                servicio.setDescripcion((String) d.get("Descripcion"));
                                servicio.setDuracion((Integer) d.get("Duracion"));
                                servicio.setPrecio(Double.parseDouble (d.get("Precio")+""));
                                servicio.setProcedimiento((String) d.get("Procedimiento"));
                                servicio.setMateriales((String) d.get("Materiales"));
                                servicio.setImagenes((ArrayList<String>) d.get("Imagenes"));
                                listaServicios.add(servicio);
                                Log.d("hola2","Servicio: "+ servicio.getNombre());
                            }
                            /*
                            ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) result.getData();
                            listaServicio = data.stream().map(d -> {
                                Servicio servicio = new Servicio();
                                servicio.setNombre((String) d.get("Nombre"));
                                servicio.setDuracion((Integer) d.get("Duracion"));
                                servicio.setPrecio((Double) d.get("Precio"));
                                listaServicios.add(servicio);
                                Log.d("hola2","Servicio: "+ servicio.getNombre());
                                return servicio;

                            }).collect(Collectors.toCollection(ArrayList::new));
                        */
                        }

                    }
                    Log.d("hola2", listaServicios.toString());
                    return listaServicios;
                }
            });
    }

}
