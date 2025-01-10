package com.example.bitirme_proje.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bitirme_proje.databinding.ItemContainerAppointmentBinding;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.example.bitirme_proje.Models.Appointment;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{
    private List<Appointment> appointments;
    String TAG = "TAG";

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerAppointmentBinding itemContainerAppointmentBinding = ItemContainerAppointmentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AppointmentViewHolder(itemContainerAppointmentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.setAppointmentData(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder{

        ItemContainerAppointmentBinding binding;

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy") ;

        public AppointmentViewHolder(@NonNull ItemContainerAppointmentBinding itemContainerAppointmentBinding) {
            super(itemContainerAppointmentBinding.getRoot());
            binding = itemContainerAppointmentBinding;

        }
        void setAppointmentData(Appointment appointment){


            binding.txtAppointmentDateText.setText(appointment.date);
            binding.txtAppointmentTimeText.setText(appointment.clock);
            binding.textClinicName.setText(appointment.clinicName);
            binding.textDoctorName.setText(appointment.doctorName);


        }

    }
}
