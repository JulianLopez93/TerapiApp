package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ucaldas.terapiapp.DAL.ServicioServicioFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.helpers.CargandoAlerta;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;
import java.util.Arrays;
public class CrearServicioFragment extends Fragment {
    private View vista;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<String> imagenes = new ArrayList<>(Arrays.asList("", "", ""));
    private int imagenIndex = 0;
    private AlertDialog cargandoAlerta;
    private Button btnCrearServicio;
    private TextView crearServicioNombre,crearServicioDuracion,crearServicioPrecio,
            crearServicioMateriales,crearServicioDescripcion,crearServicioProcedimiento;

    public CrearServicioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_crear_servicio_fragment, container, false);

        btnCrearServicio = vista.findViewById(R.id.btnCrearServicio);

        crearServicioNombre = vista.findViewById(R.id.crearServicioNombre);
        crearServicioDuracion = vista.findViewById(R.id.crearServicioDuracion);
        crearServicioPrecio = vista.findViewById(R.id.crearServicioPrecio);
        crearServicioMateriales = vista.findViewById(R.id.crearServicioMateriales);
        crearServicioDescripcion = vista.findViewById(R.id.crearServicioDescripcion);
        crearServicioProcedimiento = vista.findViewById(R.id.crearServicioProcedimiento);

        int[] imageViewsIds = {R.id.imagenServicio1, R.id.imagenServicio2, R.id.imagenServicio3};

        for (int i = 0; i < imageViewsIds.length; i++) {
            ImageView imageView = vista.findViewById(imageViewsIds[i]);
            agregarImagen(imageView,i);
            imageViews.add(imageView);
        }

        btnCrearServicio.setOnClickListener(v -> {
            cargandoAlerta = new CargandoAlerta().cargaAlerta(getLayoutInflater(), vista);
            cargandoAlerta.show();

            try {
                crearServicio();
                btnCrearServicio.setEnabled(false);
            } catch (Exception e) {
                cargandoAlerta.dismiss();
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("Faltan datos del servicio: " + e.getMessage().toString())
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            dialog.dismiss();
                            btnCrearServicio.setEnabled(true);
                        }).show();
            }
        });

        return vista;
    }

    private void VerificadorDeCamposCrearServicio() throws Exception {
        if (crearServicioNombre.getText().toString().isEmpty()){
            throw new Exception("el campo Nombre no puede estar vacio");
        }
        if (crearServicioDuracion.getText().toString().isEmpty()){
            throw new Exception("el campo Duracion no puede estar vacio");
        }
        if (crearServicioPrecio.getText().toString().isEmpty()){
            throw new Exception("el campo Precio no puede estar vacio");
        }
        if (crearServicioDescripcion.getText().toString().isEmpty()){
            throw new Exception("el campo Descripcion no puede estar vacio");
        }
        if (imagenes.get(0).equals("") && imagenes.get(1).equals("") && imagenes.get(2).equals("")){
            throw new Exception("debe seleccionar por lo menos 1 imagen");
        }
    }


    public void agregarImagen(ImageView imagen,int index){
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenIndex=index;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    public void crearServicio() throws Exception {

        VerificadorDeCamposCrearServicio();
        Servicio servicio = new Servicio("",crearServicioNombre.getText().toString(),
                imagenes,Integer.parseInt(crearServicioDuracion.getText().toString()),
                Double.parseDouble(crearServicioPrecio.getText().toString()),
                crearServicioMateriales.getText().toString(),
                crearServicioDescripcion.getText().toString(),
                crearServicioProcedimiento.getText().toString());

        ServicioServicioFirebase servicioServicioFirebase = new ServicioServicioFirebase();
        servicioServicioFirebase.crearServicio(servicio,vista,cargandoAlerta);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        cargandoAlerta = new CargandoAlerta().cargaAlerta(getLayoutInflater(),vista);
        cargandoAlerta.show();

        if (requestCode == 1 && resultCode == -1) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("ImagenesServicios");
            // Obtiene la referencia de la imagen seleccionada
            Uri selectedImageUri = data.getData();
            StorageReference imageRef = imagesRef.child(selectedImageUri.getLastPathSegment());

            // Carga la imagen a Firebase Storage
            UploadTask uploadTask = imageRef.putFile(selectedImageUri);

            // Escucha el progreso de la carga
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // La imagen se ha cargado correctamente
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            imagenes.set(imagenIndex,downloadUrl);
                            Glide.with(vista)
                                    .load(downloadUrl)
                                    .into(imageViews.get(imagenIndex));
                            cargandoAlerta.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // La carga ha fallado
                    Log.e("hola", "Error al cargar la imagen", exception);
                    cargandoAlerta.dismiss();
                }
            });
        }else if (resultCode == 0) {
            cargandoAlerta.dismiss();
        }
    }

}

