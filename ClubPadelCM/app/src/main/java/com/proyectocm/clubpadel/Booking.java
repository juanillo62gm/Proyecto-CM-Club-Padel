package com.proyectocm.clubpadel;

import com.google.firebase.Timestamp;

public class Booking {

    private int nFloor;
    private String idUser;
    private Timestamp time;

    public Booking(int nFloor, String idUser, Timestamp time) {
        this.nFloor = nFloor;
        this.idUser = idUser;
        this.time = time;
    }

    public Booking() {
        this.nFloor = nFloor;
        this.idUser = idUser;
        this.time = time;
    }

    public int getnFloor() {
        return nFloor;
    }

    public void setnFloor(int nFloor) {
        this.nFloor = nFloor;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
