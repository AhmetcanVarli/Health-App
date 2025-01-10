
package com.example.bitirme_proje.Utulities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Doktorlar {

    @SerializedName("doktor_adi")
    @Expose
    private String doktorAdi;

    public String getDoktorAdi() {
        return doktorAdi;
    }

    public void setDoktorAdi(String doktorAdi) {
        this.doktorAdi = doktorAdi;
    }

}
