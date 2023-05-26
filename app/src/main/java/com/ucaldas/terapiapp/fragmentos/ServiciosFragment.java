package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ucaldas.terapiapp.DAL.ServicioServicioFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.ServicioAdapter;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;


public class ServiciosFragment extends Fragment {
    View vista;
    private RecyclerView serviciosRecyclerView;
    private ServicioAdapter servicioAdapter;

    public ServiciosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_listar_servicios, container, false);
        serviciosRecyclerView = vista.findViewById(R.id.listarServiciosRecyclerView);

        ServicioServicioFirebase servicioServicioFirebase = new ServicioServicioFirebase();
        servicioServicioFirebase.listarServicios().addOnCompleteListener(new OnCompleteListener<ArrayList<Servicio>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Servicio>> task) {

                if (task.isSuccessful()){
                    ArrayList<Servicio> listaServicios = task.getResult();
                    if (listaServicios.size()!=0){
                        serviciosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        servicioAdapter = new ServicioAdapter(vista.getContext(),listaServicios);
                        serviciosRecyclerView.setAdapter(servicioAdapter);
                    }else{
                        new AlertDialog.Builder(vista.getContext())
                                .setTitle("Servicios")
                                .setMessage("No hay servicios disponibles.")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }

                }else{
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Error")
                            .setMessage(task.getException().toString())
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            }
        });
        return vista;

    }
}