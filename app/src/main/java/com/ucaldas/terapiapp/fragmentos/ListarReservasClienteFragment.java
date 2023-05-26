package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.ReservaAdapter;
import com.ucaldas.terapiapp.modelo.Reserva;
import java.util.ArrayList;

public class ListarReservasClienteFragment extends Fragment {

    View vista;
    private RecyclerView reservasRecyclerView;
    private ReservaAdapter reservaAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_listar_reservas_cliente, container, false);

        reservasRecyclerView = vista.findViewById(R.id.listarReservasReciclerView);
        ArrayList<Reserva> lista = new ArrayList<>();
        String cliente="6";
        ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
        servicioReservacionFirebase.listarReservasPorCliente(cliente).addOnCompleteListener(new OnCompleteListener<ArrayList<Reserva>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Reserva>> task) {
                if (task.isSuccessful()){
                    ArrayList<Reserva> listaReservas = task.getResult();
                    if (listaReservas.size()!=0){
                        reservasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        reservaAdapter = new ReservaAdapter(vista.getContext(),listaReservas);
                        reservasRecyclerView.setAdapter(reservaAdapter);
                    }else{
                        new AlertDialog.Builder(vista.getContext())
                                .setTitle("Reservas")
                                .setMessage("Aun no ha realizado ninguna reserva.")
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