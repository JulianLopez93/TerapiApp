package com.ucaldas.terapiapp.DAL;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicioReporteFirebase {

    private FirebaseFunctions functions;

    public ServicioReporteFirebase(){
        functions = FirebaseFunctions.getInstance();
    }

    public Task<ArrayList<String>> serviciosMasVendidosMes(String fecha) {

        Map<String, Object> data = new HashMap<>();
        data.put("Fecha", fecha);

        return functions.getHttpsCallable("reservasMasVendidasMes")
                .call(data)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> result = (ArrayList<String>) task.getResult().getData();
                        Log.d("hola", task.getResult().getData().toString());
                        return result;
                    } else {
                        Exception e = task.getException();
                        Log.d("hola", "error"+task.getException().toString());
                        throw e;
                    }
                });
    }

}
