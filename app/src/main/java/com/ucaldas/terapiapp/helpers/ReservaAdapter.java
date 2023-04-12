package com.ucaldas.terapiapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;

import java.util.ArrayList;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> implements View.OnClickListener {
    LayoutInflater inflater;
    ArrayList<Reserva> reservas;

    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public ReservaAdapter(Context context, ArrayList<Reserva> reservas){
        this.inflater = LayoutInflater.from(context);
        this.reservas=reservas;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.reservas_items,parent,false);
        view.setOnClickListener(this);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        String id = reservas.get(position).getId_Servicio()+"";
        String descripcion = reservas.get(position).getObservaciones();
        String hora = reservas.get(position).getHora();
        holder.id.setText(id);
        holder.descripcion.setText(descripcion);
        holder.hora.setText(hora);
    }

    @Override
    public int getItemCount() {
        return this.reservas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class ReservaViewHolder extends RecyclerView.ViewHolder{
        TextView id,descripcion,hora;
        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.nombreServicioL);
            descripcion = itemView.findViewById(R.id.observacionesServicioL);
            hora = itemView.findViewById(R.id.horaServicioL);

        }
    }
}
