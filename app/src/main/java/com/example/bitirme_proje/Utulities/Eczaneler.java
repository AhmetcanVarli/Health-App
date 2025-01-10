
package com.example.bitirme_proje.Utulities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Eczaneler {

    @SerializedName("eczane_id")
    @Expose
    private String eczaneId;
    @SerializedName("eczane_adi")
    @Expose
    private String eczaneAdi;
    @SerializedName("eczane_adres")
    @Expose
    private String eczaneAdres;
    @SerializedName("eczane_tel")
    @Expose
    private String eczaneTel;
    @SerializedName("eczane_lat_lng")
    @Expose
    private String eczaneLatLng;

    public String getEczaneId() {
        return eczaneId;
    }

    public void setEczaneId(String eczaneId) {
        this.eczaneId = eczaneId;
    }

    public String getEczaneAdi() {
        return eczaneAdi;
    }

    public void setEczaneAdi(String eczaneAdi) {
        this.eczaneAdi = eczaneAdi;
    }

    public String getEczaneAdres() {
        return eczaneAdres;
    }

    public void setEczaneAdres(String eczaneAdres) {
        this.eczaneAdres = eczaneAdres;
    }

    public String getEczaneTel() {
        return eczaneTel;
    }

    public void setEczaneTel(String eczaneTel) {
        this.eczaneTel = eczaneTel;
    }

    public String getEczaneLatLng() {
        return eczaneLatLng;
    }

    public void setEczaneLatLng(String eczaneLatLng) {
        this.eczaneLatLng = eczaneLatLng;
    }

}
