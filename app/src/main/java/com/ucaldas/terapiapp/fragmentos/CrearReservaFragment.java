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
import com.ucaldas.terapiapp.helpers.SeleccionadorFecha;
import com.ucaldas.terapiapp.modelo.Reserva;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearReservaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearReservaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    TextView seleccionadorFecha;

    public CrearReservaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearReservaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearReservaFragment newInstance(String param1, String param2) {
        CrearReservaFragment fragment = new CrearReservaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_reserva, container, false);

        seleccionadorFecha = (TextView) vista.findViewById(R.id.fecha);
        seleccionadorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,vista);
            }
        });

        Spinner seleccionHora = vista.findViewById(R.id.seleccionHora);

        Button btnReservar = vista.findViewById(R.id.btnReservar);

        EditText fecha = vista.findViewById(R.id.fecha);
        EditText observaciones = vista.findViewById(R.id.observaciones);
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Reserva reserva = new Reserva(1,1,1,fecha.getText().toString(),seleccionHora.getSelectedItem().toString(),"Carrera 20a #63-07",observaciones.getText().toString());
                    crearReservacion(reserva);
                    btnReservar.setEnabled(false);
                }catch (Exception e){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("Debe llenar todos los datos de la reserva")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    btnReservar.setEnabled(true);
                                }
                            }).show();

                }
            }
        });

        return vista;

    }

    public void showDatePickerDialog(View view, View vista) {
        SeleccionadorFecha seleccionadorFecha = new SeleccionadorFecha(vista);
        seleccionadorFecha.show(requireActivity().getSupportFragmentManager(), "datePicker");

    }

    public void crearReservacion(Reserva reserva) {

            ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
            servicioReservacionFirebase.crearReservacion(reserva,vista);
    }
}