package com.ucaldas.terapiapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Calendar;

public class CrearReserva extends AppCompatActivity {

    private Spinner seleccionHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_reserva);

        seleccionHora = findViewById(R.id.seleccionHora);
        seleccionHora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void showDatePickerDialog(View view) {
        SeleccionadorFecha seleccionadorFecha = new SeleccionadorFecha();
        seleccionadorFecha.show(getSupportFragmentManager(), "datePicker");
    }

}

