package com.ucaldas.terapiapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.ReporteServicio;

import java.util.ArrayList;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    LayoutInflater inflater;
    ArrayList<ReporteServicio> servicios;


    public ReporteAdapter(Context context, ArrayList<ReporteServicio> servicios){
        this.inflater = LayoutInflater.from(context);
        this.servicios = servicios;
    }
    @NonNull
    @Override
    public ReporteAdapter.ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.reporteservicio_items,parent,false);
        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteAdapter.ReporteViewHolder holder, int position) {
        String nombre = servicios.get(position).getNombre();
        String precio = servicios.get(position).getPrecio()+"";
        String duracion = servicios.get(position).getDuracion()+"";
        String cantidad = servicios.get(position).getCantidad()+"";
        holder.txtNombre.setText(nombre);
        holder.txtPrecio.setText(precio);
        holder.txtDuracion.setText(duracion);
        holder.txtTotalVendidos.setText(cantidad);
    }

    @Override
    public int getItemCount() {
        return this.servicios.size();
    }

    public class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtPrecio,txtDuracion,txtTotalVendidos;
        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtDuracion = itemView.findViewById(R.id.txtDuracion);
            txtTotalVendidos = itemView.findViewById(R.id.txtTotalVendidos);
        }
    }
}
