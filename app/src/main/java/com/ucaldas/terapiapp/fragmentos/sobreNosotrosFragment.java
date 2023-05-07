package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ucaldas.terapiapp.DAL.ServicioReporteFirebase;
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.DAL.ServicioServicioFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sobreNosotrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sobreNosotrosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView datoEjemplo;

    View vista;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String nombreEjemplo;

    public sobreNosotrosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sobreNosotrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static sobreNosotrosFragment newInstance(String param1, String param2) {
        sobreNosotrosFragment fragment = new sobreNosotrosFragment();
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

        vista = inflater.inflate(R.layout.fragment_sobre_nosotros, container, false);

        datoEjemplo = vista.findViewById(R.id.textView3);

        obtenerServicio();


        return vista;
    }

    public void obtenerServicio()
    {
        ServicioServicioFirebase servicioServicioFirebase = new ServicioServicioFirebase();

        servicioServicioFirebase.listarServicios().addOnCompleteListener(new OnCompleteListener<ArrayList<Servicio>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Servicio>> task) {
                if (task.isSuccessful())
                {
                    ArrayList<Servicio> listaServicios = task.getResult();

                    nombreEjemplo = listaServicios.get(0).getNombre();
                    Log.d("hola2", "Servicio imp: " + nombreEjemplo);
                    datoEjemplo.setText(nombreEjemplo);
                }
                else{
                    //cargandoAlerta.dismiss();
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


    }
}