
package com.example.bitirme_proje.Utulities;

import java.util.List;


import com.example.bitirme_proje.Utulities.Ilceler;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CityDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ilceler")
    @Expose
    private List<Ilceler> ilceler = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ilceler> getIlceler() {
        return ilceler;
    }

    public void setIlceler(List<Ilceler> ilceler) {
        this.ilceler = ilceler;
    }

}
