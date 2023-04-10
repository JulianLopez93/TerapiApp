package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReservasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View vista;
   RecyclerView recyclerReservas;




    public ListaReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaReservasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaReservasFragment newInstance(String param1, String param2) {
        ListaReservasFragment fragment = new ListaReservasFragment();
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
        vista= inflater.inflate(R.layout.fragment_lista_reservas, container, false);
        recyclerReservas=vista.findViewById(R.id.recyclerId);
        recyclerReservas.setLayoutManager(new LinearLayoutManager(getContext()));

        listarReservacion();




        return vista;
    }

    public void listarReservacion() {

        ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
        servicioReservacionFirebase.listarReservas(vista);
    }


}