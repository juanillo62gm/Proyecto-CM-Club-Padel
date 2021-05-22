package com.proyectocm.clubpadel;

public class Match {

    private String idBooking;
    private String idUser1;
    private String idUser2;
    private String idUser3;
    private String idUser4;
    private int couples;
    private String result;

    public Match(String idBooking, String idUser1, String idUser2, String idUser3, String idUser4, int couples, String result) {
        this.idBooking = idBooking;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.idUser3 = idUser3;
        this.idUser4 = idUser4;
        this.couples = couples;
        this.result = result;
    }

    public Match() {
        this.idBooking = idBooking;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.idUser3 = idUser3;
        this.idUser4 = idUser4;
        this.couples = couples;
        this.result = result;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public String getIdUser3() {
        return idUser3;
    }

    public void setIdUser3(String idUser3) {
        this.idUser3 = idUser3;
    }

    public String getIdUser4() {
        return idUser4;
    }

    public void setIdUser4(String idUser4) {
        this.idUser4 = idUser4;
    }

    public int getCouples() {
        return couples;
    }

    public void setCouples(int couples) {
        this.couples = couples;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
