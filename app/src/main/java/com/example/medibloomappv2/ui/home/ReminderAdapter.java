package com.example.medibloomappv2.ui.home;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.DoseLogEntity;
import com.example.medibloomappv2.utils.DateUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderAdapter extends ListAdapter<DoseLogEntity, ReminderAdapter.ViewHolder> {

    public interface OnMarkTaken {
        void onMark(long logId);
    }

    private final OnMarkTaken onMarkTaken;

    public ReminderAdapter(OnMarkTaken onMarkTaken) {
        super(DIFF);
        this.onMarkTaken = onMarkTaken;
    }

    private static final DiffUtil.ItemCallback<DoseLogEntity> DIFF =
            new DiffUtil.ItemCallback<DoseLogEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull DoseLogEntity a, @NonNull DoseLogEntity b) {
                    return a.id == b.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull DoseLogEntity a, @NonNull DoseLogEntity b) {
                    return a.status.equals(b.status);
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onMarkTaken);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View iconBg;
        final ImageView ivIcon;
        final TextView tvName, tvDosage, tvTime;
        final MaterialButton btnMarkTaken;
        final Chip chipStatus;

        ViewHolder(View v) {
            super(v);
            iconBg = v.findViewById(R.id.icon_bg);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvName = v.findViewById(R.id.tv_medicine_name);
            tvDosage = v.findViewById(R.id.tv_dosage);
            tvTime = v.findViewById(R.id.tv_time);
            btnMarkTaken = v.findViewById(R.id.btn_mark_taken);
            chipStatus = v.findViewById(R.id.chip_status);
        }

        void bind(DoseLogEntity log, OnMarkTaken listener) {
            Context ctx = itemView.getContext();
            tvName.setText(log.medicineName);

            String timeStr = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(new Date(log.scheduledAt));
            tvTime.setText(timeStr);

            switch (log.status) {
                case "TAKEN":
                    iconBg.setBackgroundTintList(ctx.getColorStateList(R.color.status_taken));
                    ivIcon.setImageResource(R.drawable.ic_check_circle);
                    btnMarkTaken.setVisibility(View.GONE);
                    chipStatus.setVisibility(View.VISIBLE);
                    chipStatus.setText("✓ Taken");
                    chipStatus.setChipBackgroundColorResource(R.color.chip_taken_bg);
                    chipStatus.setTextColor(ctx.getColor(R.color.chip_taken_text));
                    break;
                case "MISSED":
                    iconBg.setBackgroundTintList(ctx.getColorStateList(R.color.status_missed));
                    ivIcon.setImageResource(R.drawable.ic_close_circle);
                    btnMarkTaken.setVisibility(View.GONE);
                    chipStatus.setVisibility(View.VISIBLE);
                    chipStatus.setText("✗ Missed");
                    chipStatus.setChipBackgroundColorResource(R.color.chip_missed_bg);
                    chipStatus.setTextColor(ctx.getColor(R.color.chip_missed_text));
                    break;
                default: // UPCOMING
                    iconBg.setBackgroundTintList(ctx.getColorStateList(R.color.primary_pink_light));
                    ivIcon.setImageResource(R.drawable.ic_clock);
                    btnMarkTaken.setVisibility(View.VISIBLE);
                    chipStatus.setVisibility(View.GONE);
                    btnMarkTaken.setOnClickListener(v -> {
                        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f)
                                        .setDuration(100).start()).start();
                        listener.onMark(log.id);
                    });
                    break;
            }
        }
    }
}

