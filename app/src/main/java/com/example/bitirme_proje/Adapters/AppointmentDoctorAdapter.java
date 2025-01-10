package com.example.bitirme_proje.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirme_proje.Models.Appointment;
import com.example.bitirme_proje.databinding.ItemContainerAppointmentBinding;
import com.example.bitirme_proje.databinding.DoctorAppointmentHistoryBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentDoctorAdapter extends RecyclerView.Adapter<AppointmentDoctorAdapter.AppointmentViewHolder>{

    private List<Appointment> appointments;
    String TAG = "TAG";

    public AppointmentDoctorAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DoctorAppointmentHistoryBinding doctorAppointmentHistoryBinding = DoctorAppointmentHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AppointmentDoctorAdapter.AppointmentViewHolder(doctorAppointmentHistoryBinding);
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

        DoctorAppointmentHistoryBinding binding;

        public AppointmentViewHolder(@NonNull DoctorAppointmentHistoryBinding doctorAppointmentHistoryBinding) {
            super(doctorAppointmentHistoryBinding.getRoot());
            binding = doctorAppointmentHistoryBinding;

        }
        void setAppointmentData(Appointment appointment){


            binding.txtAppointmentDateText.setText(appointment.date);
            binding.txtAppointmentTimeText.setText(appointment.clock);
            binding.txtPatientName.setText(appointment.userName + " " + appointment.userSurname);


        }

    }


}
