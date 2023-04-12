package com.ucaldas.terapiapp.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.CalendarioFecha;
import com.ucaldas.terapiapp.helpers.ReservaAdapter;
import com.ucaldas.terapiapp.helpers.SeleccionadorFecha;
import com.ucaldas.terapiapp.modelo.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListarReservasFragment extends Fragment {

    private RecyclerView reservasRecyclerView;
    private ReservaAdapter reservaAdapter;
    TextView listarReservaFecha;
    FloatingActionButton btnSiguienteFecha,btnAnteriorFecha;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listar_reservas, container, false);

        listarReservaFecha = (TextView) root.findViewById(R.id.listarReservaFecha);


        LocalDate fechaActual = LocalDate.now();
        String fechaActualString = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        btnSiguienteFecha = (FloatingActionButton) root.findViewById(R.id.btnSiguienteFecha);
        btnAnteriorFecha = (FloatingActionButton) root.findViewById(R.id.btnAnteriorFecha);

        btnAnteriorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDate fecha = LocalDate.parse(listarReservaFecha.getText());


                LocalDate fechaDisminuida = fecha.minusDays(1);

                String fechaDisminuidaString = fechaDisminuida.toString();
                listarReservaFecha.setText(fechaDisminuidaString);
            }
        });

        btnSiguienteFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDate fecha = LocalDate.parse(listarReservaFecha.getText());


                LocalDate fechaAumentada = fecha.plusDays(1);

                String fechaAumentadaString = fechaAumentada.toString();
                listarReservaFecha.setText(fechaAumentadaString);
            }
        });

        listarReservaFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogListarReserva(root);
            }
        });
        reservasRecyclerView = root.findViewById(R.id.listarReservasReciclerView);

        listarReservaFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Reserva> lista = new ArrayList<>();
                ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
                servicioReservacionFirebase.listarReservas(s.toString()).addOnCompleteListener(new OnCompleteListener<ArrayList<Reserva>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Reserva>> task) {
                        if (task.isSuccessful()){
                            ArrayList<Reserva> listaReservas = task.getResult();

                            reservasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            reservaAdapter = new ReservaAdapter(getContext(),listaReservas);

                            reservasRecyclerView.setAdapter(reservaAdapter);
                        }else{
                            task.getException().printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listarReservaFecha.setText(fechaActualString);

        return root;
    }

    public void showDatePickerDialogListarReserva(View vista) {
        CalendarioFecha calendarioFecha = new CalendarioFecha(vista);
        calendarioFecha.show(requireActivity().getSupportFragmentManager(), "datePicker");

    }
}