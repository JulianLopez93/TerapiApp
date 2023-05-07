package com.ucaldas.terapiapp.helpers;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;
import com.ucaldas.terapiapp.modelo.Servicio;

import java.util.ArrayList;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<Servicio> servicios;

    private View.OnClickListener listener;
    private Handler handler = new Handler();

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public ServicioAdapter(Context context, ArrayList<Servicio> servicios)
    {
        this.inflater = LayoutInflater.from(context);
        this.servicios = servicios;
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
        String nombre = servicios.get(position).getNombre();
        String descripcion = servicios.get(position).getDescripcion();
        String duracion = servicios.get(position).getDuracion()+" min";
        String precio = "$"+servicios.get(position).getPrecio()+"";
        holder.nombre.setText(nombre);
        holder.descripcion.setText(descripcion);
        holder.duracion.setText(duracion);
        holder.precio.setText(precio);
        ArrayList<String> listaImagenes = new ArrayList<>();
        for (String imagen: item.getImagenes()){
            if (!imagen.equals("")){
                listaImagenes.add(imagen);
            }
        }
        ImagePagerAdapter adapter = new ImagePagerAdapter(listaImagenes, inflater.getContext());
        holder.imageViewPager.setAdapter(adapter);

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
        TextView nombre,descripcion,duracion, precio;
        private ViewPager imageViewPager;
        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreServicioC);
            descripcion = itemView.findViewById(R.id.descripcionServicioC);
            duracion = itemView.findViewById(R.id.duracion);
            precio = itemView.findViewById(R.id.precio);
            imageViewPager = itemView.findViewById(R.id.imageViewPagerC);

        }
    }
}
