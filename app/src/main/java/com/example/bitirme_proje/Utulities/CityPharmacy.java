
package com.example.bitirme_proje.Utulities;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CityPharmacy {

    @SerializedName("cityDetail")
    @Expose
    private List<CityDetail> cityDetail = null;

    public List<CityDetail> getCityDetail() {
        return cityDetail;
    }

    public void setCityDetail(List<CityDetail> cityDetail) {
        this.cityDetail = cityDetail;
    }

}
