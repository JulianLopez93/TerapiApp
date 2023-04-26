package com.ucaldas.terapiapp.fragmentos;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.CargandoAlerta;
import com.ucaldas.terapiapp.helpers.SeleccionadorFecha;
import com.ucaldas.terapiapp.modelo.Reserva;

public class CrearReservaFragment extends Fragment {
    private View vista;
    private TextView seleccionadorFecha;
    private AlertDialog cargandoAlerta;
    public CrearReservaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_crear_reserva, container, false);

        seleccionadorFecha =  vista.findViewById(R.id.fecha);

        seleccionadorFecha.setOnClickListener(v -> mostrarDatePickerDialogCrearReserva(vista));

        Spinner seleccionHora = vista.findViewById(R.id.seleccionHora);

        Button btnReservar = vista.findViewById(R.id.btnReservar);

        EditText fecha = vista.findViewById(R.id.fecha);
        EditText observaciones = vista.findViewById(R.id.observaciones);

        btnReservar.setOnClickListener(v -> {
            cargandoAlerta = new CargandoAlerta().cargaAlerta(getLayoutInflater(),vista);
            cargandoAlerta.show();
            try {
                Reserva reserva = new Reserva(1,1,1,fecha.getText().toString(),seleccionHora.getSelectedItem().toString(),"Carrera 20a #63-07",observaciones.getText().toString());
                crearReservacion(reserva);
                btnReservar.setEnabled(false);
            } catch (Exception e) {
                cargandoAlerta.dismiss();
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("Debe llenar todos los datos de la reserva")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            dialog.dismiss();
                            btnReservar.setEnabled(true);
                        })
                        .show();
            }
        });

        return vista;

    }

    public void mostrarDatePickerDialogCrearReserva(View vista) {
        SeleccionadorFecha seleccionadorFecha = new SeleccionadorFecha(vista);
        seleccionadorFecha.show(requireActivity().getSupportFragmentManager(), "datePicker");

    }

    public void crearReservacion(Reserva reserva) {

            ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
            servicioReservacionFirebase.crearReservacion(reserva,vista,cargandoAlerta);
    }
}