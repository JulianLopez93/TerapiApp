package com.ucaldas.terapiapp.helpers;

import android.content.Context;
import android.service.restrictions.RestrictionsReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ucaldas.terapiapp.R;
import com.ucaldas.terapiapp.modelo.Reserva;


import java.util.ArrayList;
import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<Reserva> reservas;
    private View.OnClickListener listener;

    public ReservaAdapter(Context context, ArrayList<Reserva> reservas){
        this.inflater = LayoutInflater.from(context);
        this.reservas=reservas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;
        private ViewPager imageViewPager;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nombreServicioL);
            descriptionTextView = itemView.findViewById(R.id.observacionesServicioL);
            imageViewPager = itemView.findViewById(R.id.imageViewPager);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.reservas_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reserva item = reservas.get(position);

        holder.nameTextView.setText(item.getServicio().getNombre());
        holder.descriptionTextView.setText(item.getObservaciones());

        ImagePagerAdapter adapter = new ImagePagerAdapter(item.getServicio().getImagenes(), inflater.getContext());
        holder.imageViewPager.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public void setData(ArrayList<Reserva> newItems) {
        reservas = newItems;
        notifyDataSetChanged();
    }

}