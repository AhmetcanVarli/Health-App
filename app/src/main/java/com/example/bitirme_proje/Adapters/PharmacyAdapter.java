package com.example.bitirme_proje.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.bitirme_proje.databinding.ItemContainerPharmacyBinding;
import com.example.bitirme_proje.Models.Pharmacy;
import com.example.bitirme_proje.Listeners.PharmacyListener;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {


    private List<Pharmacy> pharmacies;
    private final PharmacyListener pharmacyListener;

    public PharmacyAdapter(List<Pharmacy> pharmacies, PharmacyListener pharmacyListener){
        this.pharmacies = pharmacies;
        this.pharmacyListener = pharmacyListener;
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerPharmacyBinding itemContainerPharmacyBinding = ItemContainerPharmacyBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PharmacyViewHolder(itemContainerPharmacyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
        holder.setPharmacyData(pharmacies.get(position));
    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }


    class PharmacyViewHolder extends RecyclerView.ViewHolder{

        ItemContainerPharmacyBinding binding;

        public PharmacyViewHolder(@NonNull ItemContainerPharmacyBinding itemContainerPharmacyBinding ) {
            super(itemContainerPharmacyBinding.getRoot());
            binding = itemContainerPharmacyBinding;
        }
        void setPharmacyData(Pharmacy pharmacy){
            binding.txtPharmarcyName.setText(pharmacy.pharmacyName);
            binding.txtPharmarcyAdress.setText(pharmacy.pharmacyAdress);
            binding.txtPharmarcyPhone.setText(pharmacy.pharmacyPhone);

            binding.getRoot().setOnClickListener(view -> pharmacyListener.onPharmacyClicked(pharmacy));
        }
    }




}
