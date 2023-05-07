package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ucaldas.terapiapp.DAL.ServicioReporteFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.CargandoAlerta;
import com.ucaldas.terapiapp.helpers.ReporteAdapter;

import com.ucaldas.terapiapp.modelo.ReporteServicio;
import com.ucaldas.terapiapp.modelo.Reserva;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReporteFragment extends Fragment {

    Button btnAtras;
    String fecha ="";
    EditText EtxtMes;
    View vista;
    private RecyclerView reporteRecyclerView;
    private ReporteAdapter reporteAdapter;
    private AlertDialog cargandoAlerta;

    public ReporteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_reporte, container, false);
        btnAtras = vista.findViewById(R.id.btnAtras);
        cargandoAlerta = new CargandoAlerta().cargaAlerta(getLayoutInflater(),vista);
        EtxtMes = vista.findViewById(R.id.EtxtMes);
        EtxtMes.setInputType(InputType.TYPE_NULL);
        EtxtMes.setOnClickListener(v -> mostrarDatePickerDialogMes(vista));
        reporteRecyclerView = vista.findViewById(R.id.ReporteServiciosReciclerView);

        cambioMesListener(EtxtMes);

        btnAtras.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new sobreNosotrosFragment())
                    .commit();
        });
        return vista;
    }

    private void cambioMesListener(TextView EtxtMes) {
        EtxtMes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("hola", fecha);
                ServicioReporteFirebase servicioReporteFirebase = new ServicioReporteFirebase();
                servicioReporteFirebase.serviciosMasVendidosMes(fecha,vista,cargandoAlerta).addOnCompleteListener(new OnCompleteListener<ArrayList<ReporteServicio>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<ReporteServicio>> task) {

                        if (task.isSuccessful()){
                            ArrayList<ReporteServicio> listaReporteServicios = task.getResult();
                                reporteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                reporteAdapter = new ReporteAdapter(vista.getContext(),listaReporteServicios);
                                reporteRecyclerView.setAdapter(reporteAdapter);
                        }else{
                            cargandoAlerta.dismiss();
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
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void mostrarDatePickerDialogMes(View vista){
        Calendar calendario = Calendar.getInstance();
        int mes = calendario.get(Calendar.MONTH);
        int anio = calendario.get(Calendar.YEAR);
        int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker vista, int year, int month, int day) {
                fecha =(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day));
                String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
                EtxtMes.setText(meses[month]);

            }
    }, anio, mes, diaActual);

        datePickerDialog.show();
    }

}