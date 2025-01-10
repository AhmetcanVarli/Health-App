
package com.example.bitirme_proje.Utulities;

import java.util.List;


import com.example.bitirme_proje.Utulities.Doktorlar;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("klinik_adi")
    @Expose
    private String klinikAdi;
    @SerializedName("doktorlar")
    @Expose
    private List<Doktorlar> doktorlar = null;

    public String getKlinikAdi() {
        return klinikAdi;
    }

    public void setKlinikAdi(String klinikAdi) {
        this.klinikAdi = klinikAdi;
    }

    public List<Doktorlar> getDoktorlar() {
        return doktorlar;
    }

    public void setDoktorlar(List<Doktorlar> doktorlar) {
        this.doktorlar = doktorlar;
    }

}
