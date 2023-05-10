package com.ucaldas.terapiapp.helpers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.fragmentos.CrearReservaFragment;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<Servicio> servicios;

    private View.OnClickListener listener;
    private Handler handler = new Handler();
    private Context mContext;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public ServicioAdapter(Context context, ArrayList<Servicio> servicios)
    {
        this.inflater = LayoutInflater.from(context);
        this.servicios = servicios;
        mContext = context;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.servicios_items,parent,false);
        view.setOnClickListener(this);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio item = servicios.get(position);
        Log.d("Hola", "Materiales: "+servicios.get(position).getMateriales().toString());
        Log.d("Hola", "Procedimiento: "+servicios.get(position).getProcedimiento().toString());
        String nombre = servicios.get(position).getNombre();
        String descripcion = servicios.get(position).getDescripcion();
        String duracion = servicios.get(position).getDuracion()+" min";
        String precio = "$"+servicios.get(position).getPrecio()+"";
        String materiales = servicios.get(position).getMateriales();
        String procedimiento = servicios.get(position).getProcedimiento();
        holder.nombre.setText(nombre);
        holder.descripcion.setText(descripcion);
        holder.duracion.setText(duracion);
        holder.precio.setText(precio);
        holder.materiales2.setText(materiales);
        holder.procedimiento2.setText(procedimiento);
        ArrayList<String> listaImagenes = new ArrayList<>();
        for (String imagen: item.getImagenes()){
            if (!imagen.equals("")){
                listaImagenes.add(imagen);
            }
        }
        ImagePagerAdapter adapter = new ImagePagerAdapter(listaImagenes, inflater.getContext());
        holder.imageViewPager.setAdapter(adapter);

        holder.btnReservar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CrearReservaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Id", item.getId());
                bundle.putString("Nombre", item.getNombre());
                bundle.putString("Descripcion", item.getDescripcion());
                bundle.putString("Materiales", item.getMateriales());
                bundle.putString("Procedimiento", item.getProcedimiento());
                bundle.putString("Duracion", item.getDuracion()+"");
                bundle.putString("Precio", item.getPrecio()+"");
                bundle.putString("Imagenes", item.getImagenes()+"");

                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        });

        holder.titMateriales.setVisibility(View.GONE);
        holder.titProcedimiento.setVisibility(View.GONE);
        holder.materiales2.setVisibility(View.GONE);
        holder.procedimiento2.setVisibility(View.GONE);


        // Establecer ViewPager para cambiar automáticamente de página
        final int count = adapter.getCount();
        final Runnable runnable = new Runnable() {
            int currentPage = 0;
            @Override
            public void run() {
                if (currentPage == count) {
                    currentPage = 0;
                }
                holder.imageViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }



    @Override
    public int getItemCount() {
        return this.servicios.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ServicioViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,descripcion,duracion, precio, materiales2, procedimiento2, titMateriales, titProcedimiento;
        private ViewPager imageViewPager;
        Button btnReservar;
        Button btnVerMas;
        public LinearLayout mExpandedLayout;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreServicioC);
            descripcion = itemView.findViewById(R.id.descripcionServicioC);
            duracion = itemView.findViewById(R.id.duracion);
            precio = itemView.findViewById(R.id.precio);
            imageViewPager = itemView.findViewById(R.id.imageViewPagerC);
            btnReservar = itemView.findViewById(R.id.btnReservarC);
            btnVerMas = itemView.findViewById(R.id.btnVerMas);
            materiales2 = itemView.findViewById(R.id.txtMateriales2);
            procedimiento2 = itemView.findViewById(R.id.txtProcedimiento2);
            titMateriales = itemView.findViewById(R.id.textView2);
            titProcedimiento = itemView.findViewById(R.id.textView4);

            btnVerMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (materiales2.getVisibility() == View.GONE) {
                        materiales2.setVisibility(View.VISIBLE);
                        procedimiento2.setVisibility(View.VISIBLE);
                        titMateriales.setVisibility(View.VISIBLE);
                        titProcedimiento.setVisibility(View.VISIBLE);
                        btnVerMas.setText("Ver menos");
                    } else {
                        materiales2.setVisibility(View.GONE);
                        procedimiento2.setVisibility(View.GONE);
                        titMateriales.setVisibility(View.GONE);
                        titProcedimiento.setVisibility(View.GONE);
                        btnVerMas.setText("Ver más");
                    }
                }
            });

        }
    }
}
