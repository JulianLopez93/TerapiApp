package com.ucaldas.terapiapp.helpers;

import static java.lang.Math.round;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;

import java.util.ArrayList;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<Reserva> reservas;
    private Handler handler = new Handler();

    public ReservaAdapter(Context context, ArrayList<Reserva> reservas){
        this.inflater = LayoutInflater.from(context);
        this.reservas=reservas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreServicioL;
        private TextView observacionesServicioL;
        private TextView horaServicioL;

        private TextView estadoReserva;

        private TextView fechaReserva;
        private TextView precioServicio;
        private ViewPager imageViewPager;


        public ViewHolder(View itemView) {
            super(itemView);
            nombreServicioL = itemView.findViewById(R.id.nombreServicioL);
            observacionesServicioL = itemView.findViewById(R.id.observacionesServicioL);
            imageViewPager = itemView.findViewById(R.id.imageViewPager);
            horaServicioL = itemView.findViewById(R.id.horaServicioL);
            fechaReserva = itemView.findViewById(R.id.fechaReserva);
            estadoReserva = itemView.findViewById(R.id.estadoReserva);
            precioServicio = itemView.findViewById(R.id.precioServicio);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.reservas_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);

        holder.nombreServicioL.setText(reserva.getServicio().getNombre());
        holder.observacionesServicioL.setText(reserva.getObservaciones());
        reserva.getEstadoReserva().getNombre();
        holder.horaServicioL.setText(reserva.getHora());
        holder.estadoReserva.setText(reserva.getEstadoReserva().getNombre());
        holder.fechaReserva.setText(reserva.getFecha());
        holder.precioServicio.setText("$" + round(reserva.getServicio().getPrecio()));
        ArrayList<String> listaImagenes = new ArrayList<>();
        for (String imagen: reserva.getServicio().getImagenes()){
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
        return reservas.size();
    }
}