package com.ucaldas.terapiapp.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ucaldas.terapiapp.R;

public class sobreNosotrosFragment extends Fragment {
    View vista;

    public sobreNosotrosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_sobre_nosotros, container, false);

        return vista;
    }

}