package com.ucaldas.terapiapp.DAL;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.ucaldas.terapiapp.modelo.ReporteServicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicioReporteFirebase {

    private FirebaseFunctions functions;

    /**
     * Trae de la base de datos una lista con los reportes filtrados por un mes
     * en caso de no haber reportes se muestra un mensaje indicando esto
     * @return reportes, lista con los reportes
     */
    public ServicioReporteFirebase(){
        functions = FirebaseFunctions.getInstance();
    }
    public Task<ArrayList<ReporteServicio>> serviciosMasVendidosMes(String fecha, View vista, AlertDialog cargandoAlerta) {
        ArrayList<ReporteServicio> reportes = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", fecha);
        return functions
                .getHttpsCallable("reservasMasVendidasMes")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<ReporteServicio>>() {
                    @Override
                    public ArrayList<ReporteServicio> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        if (task.isSuccessful()) {
                            HttpsCallableResult result = task.getResult();
                            Log.d("hola", "onComplete: "+result.getData().toString());
                            if (result != null) {
                                ArrayList<Map<String, Object>> informacion = (ArrayList<Map<String, Object>>) result.getData();
                                for (Map<String, Object> d : informacion) {
                                    ReporteServicio reporte = new ReporteServicio();
                                    reporte.setNombre((String) d.get("Nombre"));
                                    reporte.setPrecio(Double.parseDouble (d.get("Precio")+""));
                                    reporte.setDuracion((Integer) d.get("Duracion"));
                                    reporte.setCantidad((Integer) d.get("Cantidad"));
                                    reportes.add(reporte);
                                }
                            }
                        }
                        if(reportes.size() == 0){
                            cargandoAlerta.dismiss();
                            new AlertDialog.Builder(vista.getContext())
                                    .setTitle("Error")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .show();
                        }
                        return reportes;
                    }
                });
    }

}
