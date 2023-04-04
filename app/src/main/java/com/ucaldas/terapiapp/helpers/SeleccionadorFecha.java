package com.ucaldas.terapiapp.helpers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class SeleccionadorFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private View vista;

    public SeleccionadorFecha(View vista) {
        this.vista = vista;
    }

    private ArrayList<String> horas;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtener la fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.MyDatePickerStyle, this, year, month, day);

        // Crear un nuevo DatePickerDialog y devolverlo
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Obtener el campo de texto y establecer la fecha seleccionada en él
        EditText editTextDate = getActivity().findViewById(R.id.fecha);
        editTextDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth));

        ArrayList<String> opciones = new ArrayList(Arrays.asList("8:00a.m.", "9:00a.m.", "10:00a.m.", "11:00a.m.", "2:00a.m.", "3:00a.m.", "4:00a.m."));
        FragmentActivity activity = getActivity();
        ServicioReservacionFirebase servicioReservacionFirebase = new ServicioReservacionFirebase();
        servicioReservacionFirebase.horasOcupadas(editTextDate.getText().toString()).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    horas = new ArrayList<String>(Arrays.asList(task.getResult().replace("[","").replace("]","").replace(" ","").split(",")));
                    opciones.removeAll(horas);
                    llenarSpinner(opciones,activity,vista);
                }else{
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public void llenarSpinner(ArrayList<String> opciones, FragmentActivity activity, View vista){
            Spinner miSpinner = activity.findViewById(R.id.seleccionHora);
            ArrayAdapter<String> adapter = new ArrayAdapter(activity.getApplicationContext(), android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            miSpinner.setAdapter(adapter);

        if (opciones.size()==0){
            new AlertDialog.Builder(vista.getContext())
                    .setTitle("Error")
                    .setMessage("La fecha seleccionada no tiene horarios disponibles para reservar")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}