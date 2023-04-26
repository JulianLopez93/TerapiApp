package com.ucaldas.terapiapp.helpers;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.ucaldas.terapiapp.R;

public class CargandoAlerta {
    public AlertDialog cargaAlerta(LayoutInflater inflater, View vista){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(vista.getContext());
        View dialogLayout = inflater.inflate(R.layout.cargando, null);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
