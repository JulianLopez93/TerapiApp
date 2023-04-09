package com.ucaldas.terapiapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;

import org.w3c.dom.Text;

public class ReservasAdapter extends FirestoreRecyclerAdapter<Reserva, ReservasAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReservasAdapter(@NonNull FirestoreRecyclerOptions<Reserva> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reserva reserva) {
        holder.textViewFecha.setTextContent(reserva.getFecha());
        holder.textViewHora.setTextContent(reserva.getHora());
        holder.textViewCliente.setTextContent(Integer.toString(reserva.getId_Cliente()));
        holder.textViewEstado.setTextContent(Integer.toString(reserva.getId_EstadoReserva()));
        holder.textViewServicio.setTextContent(Integer.toString(reserva.getId_Servicio()));
        holder.textViewLugar.setTextContent(reserva.getLugar());
        holder.textViewObservaciones.setTextContent(reserva.getObservaciones());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lista_reservas, parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Text textViewFecha, textViewHora, textViewCliente, textViewEstado, textViewServicio,textViewLugar,textViewObservaciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
/*
            textViewFecha=itemView.findViewById(R.id.textViewFecha);
            textViewHora=itemView.findViewById(R.id.textViewHora);
            textViewCliente=itemView.findViewById(R.id.textViewCliente);
            textViewEstado=itemView.findViewById(R.id.textViewEstado);
            textViewServicio=itemView.findViewById(R.id.textViewServicio);
            textViewLugar=itemView.findViewById(R.id.textViewLugar);
            textViewObservaciones=itemView.findViewById(R.id.textViewObservaciones);
            */

        }


    }

}
