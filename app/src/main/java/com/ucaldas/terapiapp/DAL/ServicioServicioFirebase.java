package com.ucaldas.terapiapp.DAL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.functions.FirebaseFunctions;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.HashMap;
import java.util.Map;

public class ServicioServicioFirebase {

    private FirebaseFunctions functions;

    public ServicioServicioFirebase(){
        functions = FirebaseFunctions.getInstance();
    }

    public void crearServicio(Servicio servicio, View vista) {

        Map<String, Object> data = new HashMap<>();
        data.put("Nombre", servicio.getNombre());
        data.put("Imagenes", servicio.getImagenes());
        data.put("Duracion", servicio.getDuracion());
        data.put("Precio", servicio.getPrecio());
        data.put("Materiales", servicio.getMateriales());
        data.put("Descripcion", servicio.getDescripcion());
        data.put("Procedimiento", servicio.getProcedimiento());
        functions
                .getHttpsCallable("crearServicio")
                .call(data)
                .addOnSuccessListener(result -> {
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Servicio Creado")
                            .setMessage("El servicio "+servicio.getNombre()+" Fue Creado Correctamente")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    FragmentManager fragmentManager = ((AppCompatActivity) vista.getContext()).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new sobreNosotrosFragment());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }).show();
                })
                .addOnFailureListener(e -> {
                    new AlertDialog.Builder(vista.getContext())
                            .setTitle("Error")
                            .setMessage(e.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    vista.findViewById(R.id.btnCrearServicio).setEnabled(true);
                                    dialog.dismiss();
                                }
                            }).show();
                });
    }

}
