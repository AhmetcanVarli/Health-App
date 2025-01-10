
package com.example.bitirme_proje.Utulities;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Ilceler {

    @SerializedName("ilce_adi")
    @Expose
    private String ilceAdi;
    @SerializedName("eczaneler")
    @Expose
    private List<Eczaneler> eczaneler = null;

    public String getIlceAdi() {
        return ilceAdi;
    }

    public void setIlceAdi(String ilceAdi) {
        this.ilceAdi = ilceAdi;
    }

    public List<Eczaneler> getEczaneler() {
        return eczaneler;
    }

    public void setEczaneler(List<Eczaneler> eczaneler) {
        this.eczaneler = eczaneler;
    }

}
