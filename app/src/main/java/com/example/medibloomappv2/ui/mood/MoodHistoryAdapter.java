package com.example.medibloomappv2.ui.mood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.MoodEntryEntity;
import com.example.medibloomappv2.utils.DateUtils;

public class MoodHistoryAdapter extends ListAdapter<MoodEntryEntity, MoodHistoryAdapter.ViewHolder> {

    public MoodHistoryAdapter() {
        super(new DiffUtil.ItemCallback<MoodEntryEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull MoodEntryEntity a, @NonNull MoodEntryEntity b) {
                return a.id == b.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull MoodEntryEntity a, @NonNull MoodEntryEntity b) {
                return a.moodType.equals(b.moodType) && a.dateKey.equals(b.dateKey);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvMood, tvDate, tvNote;

        ViewHolder(View v) {
            super(v);
            tvEmoji = v.findViewById(R.id.tv_emoji);
            tvMood = v.findViewById(R.id.tv_mood_label);
            tvDate = v.findViewById(R.id.tv_date);
            tvNote = v.findViewById(R.id.tv_note);
        }

        void bind(MoodEntryEntity entry) {
            tvDate.setText(DateUtils.toDisplayDate(entry.dateKey));
            tvNote.setVisibility(entry.note != null && !entry.note.isEmpty() ? View.VISIBLE : View.GONE);
            tvNote.setText(entry.note);

            switch (entry.moodType) {
                case "GOOD":
                    tvEmoji.setText("🙂");
                    tvMood.setText("Good");
                    break;
                case "OKAY":
                    tvEmoji.setText("😐");
                    tvMood.setText("Okay");
                    break;
                default:
                    tvEmoji.setText("😞");
                    tvMood.setText("Not Well");
                    break;
            }
        }
    }
}

