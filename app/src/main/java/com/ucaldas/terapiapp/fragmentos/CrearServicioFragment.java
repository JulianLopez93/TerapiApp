package com.ucaldas.terapiapp.fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.ucaldas.terapiapp.DAL.ServicioReservacionFirebase;
import com.ucaldas.terapiapp.DAL.ServicioServicioFirebase;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearServicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearServicioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<String> imagenes = new ArrayList<>(Arrays.asList("", "", ""));
    int imagenIndex = 0;

    public CrearServicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearServicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearServicioFragment newInstance(String param1, String param2) {
        CrearServicioFragment fragment = new CrearServicioFragment();
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
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_crear_servicio_fragment, container, false);
        Button btnCrearServicio = (Button) vista.findViewById(R.id.btnCrearServicio);

        TextView crearServicioNombre = (TextView) vista.findViewById(R.id.crearServicioNombre);
        TextView crearServicioDuracion = (TextView) vista.findViewById(R.id.crearServicioDuracion);
        TextView crearServicioPrecio = (TextView) vista.findViewById(R.id.crearServicioPrecio);
        TextView crearServicioMateriales = (TextView) vista.findViewById(R.id.crearServicioMateriales);
        TextView crearServicioDescripcion = (TextView) vista.findViewById(R.id.crearServicioDescripcion);
        TextView crearServicioProcedimiento = (TextView) vista.findViewById(R.id.crearServicioProcedimiento);


        int[] imageViewsIds = {R.id.imagenServicio1, R.id.imagenServicio2, R.id.imagenServicio3};

        for (int i = 0; i < imageViewsIds.length; i++) {
            ImageView imageView = vista.findViewById(imageViewsIds[i]);
            agregarImagen(imageView,i);
            imageViews.add(imageView);
        }

        btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Servicio servicio = new Servicio(crearServicioNombre.getText().toString(),
                            imagenes,Integer.parseInt(crearServicioDuracion.getText().toString()),
                            Double.parseDouble(crearServicioPrecio.getText().toString()),
                            crearServicioMateriales.getText().toString(),
                            crearServicioDescripcion.getText().toString(),
                            crearServicioProcedimiento.getText().toString());
                    crearServicio(servicio);
                    btnCrearServicio.setEnabled(false);
                }catch (Exception e){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("Debe llenar todos los datos del servicio")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    btnCrearServicio.setEnabled(true);
                                }
                            }).show();

                }
            }
        });

        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    Log.d("hola", "Upload is " + progress + "% done");
                }
            });

            // Maneja la carga exitosa o fallida
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // La imagen se ha cargado correctamente
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Obtiene la URL de descarga de la imagen
                            String downloadUrl = uri.toString();
                            imagenes.set(imagenIndex,downloadUrl);
                            Glide.with(vista)
                                    .load(downloadUrl)
                                    .into(imageViews.get(imagenIndex));
                            Log.d("hola", "URL de descarga de la imagen: " + downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // La carga ha fallado
                    Log.e("hola", "Error al cargar la imagen", exception);
                }
            });
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

    public void crearServicio(Servicio servicio) {
        ServicioServicioFirebase servicioServicioFirebase = new ServicioServicioFirebase();
        servicioServicioFirebase.crearServicio(servicio,vista);
    }

}

