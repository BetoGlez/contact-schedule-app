package com.agonzalez.practica3_albertogonzalezhernandez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> implements ScheduleTouchAdapter, View.OnClickListener {
    private View.OnClickListener listener;
    private List<AppointmentItem> appointmentsList;
    private Context context;

    public ScheduleAdapter(List<AppointmentItem> appointmentsList, Context context) {
        this.context = context;
        this.appointmentsList = appointmentsList;
    }

    @Override
    public void onInsertItem(AppointmentItem appointmentItem) {
        appointmentsList.add(appointmentItem);
        notifyItemInserted(appointmentsList.size() - 1);
    }

    @Override
    public void onDeleteItem(int position) {
        appointmentsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onEditItem(int position, AppointmentItem editedItem) {
        appointmentsList.set(position, editedItem);
        notifyItemChanged(position, editedItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_appointment, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, int pos) {
        AppointmentItem item = appointmentsList.get(pos);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }
    public void setOnClickListener(View.OnClickListener listener) { this.listener = listener; }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appointmentTitleTv, appointmentDateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            appointmentTitleTv = itemView.findViewById(R.id.appointmentTitleTv);
            appointmentDateTv = itemView.findViewById(R.id.appointmentDateTv);
        }

        public void bindData(final AppointmentItem appointment) {
            appointmentTitleTv.setText(appointment.getTitle());
            appointmentDateTv.setText(appointment.getDate());
        }
    }
}
