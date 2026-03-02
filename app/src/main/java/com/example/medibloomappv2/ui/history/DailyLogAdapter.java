package com.example.medibloomappv2.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.utils.DateUtils;
import com.google.android.material.chip.Chip;

public class DailyLogAdapter extends ListAdapter<DoseLogEntity, DailyLogAdapter.ViewHolder> {

    public DailyLogAdapter() {
        super(new DiffUtil.ItemCallback<DoseLogEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull DoseLogEntity a, @NonNull DoseLogEntity b) {
                return a.id == b.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull DoseLogEntity a, @NonNull DoseLogEntity b) {
                return a.status.equals(b.status);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_log, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvMedicineName, tvTakenOf;
        Chip chipStatus;

        ViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_date);
            tvMedicineName = v.findViewById(R.id.tv_medicine_name);
            tvTakenOf = v.findViewById(R.id.tv_taken_of);
            chipStatus = v.findViewById(R.id.chip_status);
        }

        void bind(DoseLogEntity log) {
            tvDate.setText(DateUtils.toDisplayDate(log.dateKey));
            tvMedicineName.setText(log.medicineName);
            switch (log.status) {
                case "TAKEN":
                    chipStatus.setText("✓ Taken");
                    chipStatus.setChipBackgroundColorResource(R.color.chip_taken_bg);
                    break;
                case "MISSED":
                    chipStatus.setText("✗ Missed");
                    chipStatus.setChipBackgroundColorResource(R.color.chip_missed_bg);
                    break;
                default:
                    chipStatus.setText("⏰ Upcoming");
                    chipStatus.setChipBackgroundColorResource(R.color.chip_upcoming_bg);
            }
        }
    }
}

