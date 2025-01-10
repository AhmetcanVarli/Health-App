package com.example.bitirme_proje.Adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirme_proje.Models.Prescription;
import com.example.bitirme_proje.Listeners.PrescriptionListener;
import com.example.bitirme_proje.databinding.ItemContainerPrescriptionBinding;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescripitionViewHolder>{

    private List<Prescription> prescriptions;
    private final PrescriptionListener prescriptionListener;
    String TAG = "TAG";

    public PrescriptionAdapter(List<Prescription> prescriptions, PrescriptionListener prescriptionListener) {
        this.prescriptions = prescriptions;
        this.prescriptionListener = prescriptionListener;
    }

    @androidx.annotation.NonNull
    @Override
    public PrescripitionViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        ItemContainerPrescriptionBinding itemContainerPrescriptionBinding = ItemContainerPrescriptionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PrescripitionViewHolder(itemContainerPrescriptionBinding);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull PrescripitionViewHolder holder, int position) {
        holder.setPrescriptionData(prescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }


    class PrescripitionViewHolder extends RecyclerView.ViewHolder{
        ItemContainerPrescriptionBinding binding;

        public PrescripitionViewHolder(@NonNull ItemContainerPrescriptionBinding itemContainerPrescriptionBinding){
            super(itemContainerPrescriptionBinding.getRoot());
            binding = itemContainerPrescriptionBinding;
        }
        void setPrescriptionData(Prescription prescription){

            binding.prescriptonDoctorName.setText(prescription.doctorName);
            binding.prescriptonClinicName.setText(prescription.clinicName);
            binding.prescriptonDate.setText(prescription.date);
            binding.prescriptonTime.setText(prescription.time);

            binding.getRoot().setOnClickListener(view -> prescriptionListener.onPrescriptionClicked(prescription));
        }

    }
}
