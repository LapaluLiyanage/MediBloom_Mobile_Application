package com.example.medibloomappv2.ui.mood;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.local.entity.MoodEntryEntity;
import com.example.medibloomappv2.data.repository.MoodRepository;
import com.example.medibloomappv2.utils.DateUtils;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;

public class MoodFragment extends Fragment {

    private MoodRepository repository;
    private PreferenceManager prefs;
    private String selectedMood = null;
    private CardView cardGood, cardOkay, cardNotWell;
    private LinearLayout layoutNote;
    private EditText etNote;
    private MaterialButton btnSaveMood;
    private RecyclerView rvMoodHistory;
    private MoodHistoryAdapter adapter;
    private TextView tvInsight, tvGoodDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mood, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new MoodRepository(requireContext());
        prefs = new PreferenceManager(requireContext());

        cardGood = view.findViewById(R.id.card_mood_good);
        cardOkay = view.findViewById(R.id.card_mood_okay);
        cardNotWell = view.findViewById(R.id.card_mood_not_well);
        layoutNote = view.findViewById(R.id.layout_note);
        etNote = view.findViewById(R.id.et_note);
        btnSaveMood = view.findViewById(R.id.btn_save_mood);
        rvMoodHistory = view.findViewById(R.id.rv_mood_history);
        tvInsight = view.findViewById(R.id.tv_insight_text);
        tvGoodDays = view.findViewById(R.id.tv_good_days);

        rvMoodHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MoodHistoryAdapter();
        rvMoodHistory.setAdapter(adapter);

        cardGood.setOnClickListener(v -> selectMood("GOOD", cardGood));
        cardOkay.setOnClickListener(v -> selectMood("OKAY", cardOkay));
        cardNotWell.setOnClickListener(v -> selectMood("NOT_WELL", cardNotWell));

        btnSaveMood.setOnClickListener(v -> saveMood());

        loadMoodHistory();
        loadWeeklyInsights();
    }

    private void selectMood(String mood, CardView card) {
        selectedMood = mood;

        // Reset all cards
        resetCard(cardGood);
        resetCard(cardOkay);
        resetCard(cardNotWell);

        // Highlight selected with animation
        card.animate().scaleX(1.08f).scaleY(1.08f).setDuration(150).withEndAction(() ->
                card.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
        ).start();

        int colorRes;
        switch (mood) {
            case "GOOD": colorRes = R.color.mood_good; break;
            case "OKAY": colorRes = R.color.mood_okay; break;
            default: colorRes = R.color.mood_not_well; break;
        }
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), colorRes));
        card.setCardElevation(16f);

        // Show note field with transition
        TransitionManager.beginDelayedTransition((ViewGroup) requireView());
        layoutNote.setVisibility(View.VISIBLE);
    }

    private void resetCard(CardView card) {
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface));
        card.setCardElevation(4f);
    }

    private void saveMood() {
        if (selectedMood == null) return;
        String note = etNote.getText() != null ? etNote.getText().toString().trim() : "";
        String userId = prefs.getUserId() != null ? prefs.getUserId() : "local";

        MoodEntryEntity entry = new MoodEntryEntity(
                DateUtils.today(), selectedMood, note, System.currentTimeMillis(), userId);

        repository.insertMood(entry, id -> {
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                // Success animation
                btnSaveMood.setText("✓ Saved!");
                btnSaveMood.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.status_taken));
                btnSaveMood.postDelayed(() -> {
                    btnSaveMood.setText("Save Mood");
                    btnSaveMood.setBackgroundTintList(null);
                    resetCard(cardGood); resetCard(cardOkay); resetCard(cardNotWell);
                    layoutNote.setVisibility(View.GONE);
                    etNote.setText("");
                    selectedMood = null;
                }, 2000);
            });
        });
    }

    private void loadMoodHistory() {
        String userId = prefs.getUserId() != null ? prefs.getUserId() : "local";
        repository.getRecentMoods(userId).observe(getViewLifecycleOwner(), moods -> {
            if (moods != null) adapter.submitList(moods);
        });
    }

    private void loadWeeklyInsights() {
        String userId = prefs.getUserId() != null ? prefs.getUserId() : "local";
        repository.getWeeklyInsights(userId, DateUtils.weekStartDate(), DateUtils.today(),
                (good, okay, notWell) -> {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> {
                        int total = good + okay + notWell;
                        tvGoodDays.setText(good + " out of " + total + " days");
                        if (good > total / 2) {
                            tvInsight.setText("You've been feeling good most of this week! Keep up with your medicine routine. 🌸");
                        } else if (notWell > total / 2) {
                            tvInsight.setText("It's been a tough week. Remember to take your medicines and stay hydrated. 💙");
                        } else {
                            tvInsight.setText("You're doing okay this week. Consistency with medicines helps mood! 🌼");
                        }
                    });
                });
    }
}

