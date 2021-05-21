package com.proyectocm.clubpadel;

import java.time.LocalDateTime;
import java.util.Date;

public class Booking {

    private int nFloor;
    private String idUser;
    private LocalDateTime time;

    public Booking(int nFloor, String idUser, LocalDateTime time){
        this.nFloor = nFloor;
        this.idUser = idUser;
        this.time = time;
    }

    public Booking(){
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setnFloor(int nFloor) {
        this.nFloor = nFloor;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
