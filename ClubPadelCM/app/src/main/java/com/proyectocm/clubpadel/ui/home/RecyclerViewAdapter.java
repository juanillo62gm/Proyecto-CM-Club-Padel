package com.proyectocm.clubpadel.ui.home;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.proyectocm.clubpadel.Booking;
import com.proyectocm.clubpadel.R;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final OnNoteListener onNoteListener;
    private final List<Booking> bookingList;

    public RecyclerViewAdapter(List<Booking> bookingList, OnNoteListener onNoteListener) {
        this.bookingList = bookingList;
        this.onNoteListener = onNoteListener;
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBindViewHolder(ViewHolder holder, int position) {
        int aux = bookingList.get(position).getnFloor();
        String aux2 = "Pista: " + aux;
        holder.nFloor.setText(aux2);
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(bookingList.get(position).getTime().getSeconds()), TimeZone.getDefault().toZoneId());
        int e = date.getDayOfMonth();
        int m = date.getMonthValue();
        int y = date.getYear();
        int hour = date.getHour();
        int minute = date.getMinute();
        String fecha = "Fecha: " + e + "/" + m + "/" + y;
        String hora = "Hora: " + hour + ":";
        if (minute == 0) {
            hora += "00";
        } else {
            hora += minute;
        }
        holder.date.setText(fecha);
        holder.time.setText(hora);
    }

    public int getItemCount() {
        return bookingList.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nFloor, date, time;
        OnNoteListener onNoteListener;

        private ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            nFloor = itemView.findViewById(R.id.floor_item);
            date = itemView.findViewById(R.id.date_item);
            time = itemView.findViewById(R.id.time_item);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


}
