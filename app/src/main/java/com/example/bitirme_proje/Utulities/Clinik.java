
package com.example.bitirme_proje.Utulities;

import java.util.List;


import com.example.bitirme_proje.Utulities.Datum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Clinik {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
