package com.ucaldas.terapiapp.fragmentos;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
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
import com.ucaldas.terapiapp.helpers.ImagePagerAdapter;
import com.ucaldas.terapiapp.helpers.SeleccionadorFecha;
import com.ucaldas.terapiapp.modelo.Cliente;
import com.ucaldas.terapiapp.modelo.EstadoReserva;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;

public class CrearReservaFragment extends Fragment {
    private View vista;
    private TextView seleccionadorFecha,nombreServicio,descripcionServicio,
            precioServicio,duracionServicio;
    private String idServicio;
    private AlertDialog cargandoAlerta;
    private ViewPager imageViewPager;

    Bundle bundle;
    public CrearReservaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_crear_reserva, container, false);
        bundle = getArguments();
        inicializarArgumentosBundle(bundle);

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
                Servicio servicio = new Servicio();
                servicio.setId(idServicio);
                Cliente cliente = new Cliente();
                cliente.setId("1");
                EstadoReserva estadoReserva = new EstadoReserva();
                estadoReserva.setId("1");
                Reserva reserva = new Reserva(servicio,cliente,estadoReserva,fecha.getText().toString(),seleccionHora.getSelectedItem().toString(),"Carrera 20a #63-07",observaciones.getText().toString());
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

    private void inicializarArgumentosBundle(Bundle bundle) {
        idServicio = bundle.getString("Id");
        nombreServicio = vista.findViewById(R.id.nombreServicio);
        descripcionServicio = vista.findViewById(R.id.descripcionServicio);
        precioServicio = vista.findViewById(R.id.precioServicio);
        duracionServicio = vista.findViewById(R.id.duracionServicio);
        imageViewPager = vista.findViewById(R.id.imageViewPagerCrearReserva);

        nombreServicio.setText(bundle.getString("Nombre"));
        descripcionServicio.setText(bundle.getString("Descripcion"));
        precioServicio.setText("$ "+bundle.getString("Precio"));
        duracionServicio.setText(bundle.getString("Duracion")+" Min");
        ArrayList<String> listaImagenes = new ArrayList<>();
        String[] arrayImagenes = bundle.getString("Imagenes").split(",");

        for (String imagen: arrayImagenes){
            imagen=imagen.replaceAll("\\[|\\]| ", "");
            if (!imagen.equals("")){
                listaImagenes.add(imagen);
            }
        }
        ImagePagerAdapter adapter = new ImagePagerAdapter(listaImagenes, getContext());
        imageViewPager.setAdapter(adapter);

        agregarMovimientoImagenes(imageViewPager,adapter);
    }

    private void agregarMovimientoImagenes(ViewPager imageViewPager, ImagePagerAdapter adapter) {
        Handler handler = new Handler();
        final int count = adapter.getCount();
        final Runnable runnable = new Runnable() {
            int currentPage = 0;
            @Override
            public void run() {
                if (currentPage == count) {
                    currentPage = 0;
                }
                imageViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
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