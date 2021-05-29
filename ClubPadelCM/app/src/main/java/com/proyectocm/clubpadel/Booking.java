package com.proyectocm.clubpadel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@SuppressWarnings("rawtypes")
public class Booking implements Comparable {

    private final int nFloor;
    private final String idUser;
    private final Timestamp time;

    public Booking(int nFloor, String idUser, Timestamp time) {
        this.nFloor = nFloor;
        this.idUser = idUser;
        this.time = time;
    }

    public int getnFloor() {
        return nFloor;
    }

    public String getIdUser() {
        return idUser;
    }

    public Timestamp getTime() {
        return time;
    }

    @NotNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toString() {
        return "Pista " + nFloor + " " + getDate(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate(Timestamp time) {
        String res;
        String min;
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.getSeconds()), TimeZone.getDefault().toZoneId());
        int e = date.getDayOfMonth();
        int m = date.getMonthValue();
        int y = date.getYear();
        int hour = date.getHour();
        int minute = date.getMinute();
        if (minute == 0) {
            min = "00";
        } else {
            min = "30";
        }
        res = e + "/" + m + "/" + y + " " + hour + ":" + min;
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compareTo(Object o) {
        LocalDateTime fecha = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.time.getSeconds()), TimeZone.getDefault().toZoneId());
        LocalDateTime fecha2 = LocalDateTime.ofInstant(Instant.ofEpochSecond(((Booking) o).getTime().getSeconds()), TimeZone.getDefault().toZoneId());
        return fecha.compareTo(fecha2);
    }
}
