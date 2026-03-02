package com.example.medibloomappv2.ui.assistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.medibloomappv2.R;
import com.example.medibloomappv2.domain.model.ChatMessage;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AssistantFragment extends Fragment {

    private AssistantViewModel viewModel;
    private ChatAdapter chatAdapter;
    private RecyclerView rvChat;
    private EditText etMessage;
    private FloatingActionButton fabSend;
    private LottieAnimationView lottieTyping;
    private LinearLayout chipContainer;

    private final String[] suggestions = {
            "What is my medicine used for?",
            "Common side effects",
            "How can I improve my reminder habit?",
            "What should I eat while taking medicine?",
            "Explain my medicine schedule"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AssistantViewModel.class);
        viewModel.init(requireContext());

        rvChat = view.findViewById(R.id.rv_chat);
        etMessage = view.findViewById(R.id.et_message);
        fabSend = view.findViewById(R.id.fab_send);
        lottieTyping = view.findViewById(R.id.lottie_typing);
        chipContainer = view.findViewById(R.id.chip_container);

        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setStackFromEnd(true);
        rvChat.setLayoutManager(llm);
        chatAdapter = new ChatAdapter();
        rvChat.setAdapter(chatAdapter);

        // Suggestion chips
        for (String s : suggestions) {
            Chip chip = new Chip(requireContext());
            chip.setText(s);
            chip.setCheckable(false);
            chip.setChipBackgroundColorResource(R.color.chip_suggestion_bg);
            chip.setTextColor(requireContext().getColor(R.color.text_primary));
            chip.setOnClickListener(v -> sendMessage(s));
            chipContainer.addView(chip);
        }

        fabSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etMessage.setText("");
            }
        });

        // Observe messages
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            chatAdapter.submitList(messages);
            rvChat.smoothScrollToPosition(Math.max(0, messages.size() - 1));
        });

        // Observe typing
        viewModel.getIsTyping().observe(getViewLifecycleOwner(), typing -> {
            lottieTyping.setVisibility(typing ? View.VISIBLE : View.GONE);
            if (typing) lottieTyping.playAnimation();
        });

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendMessage(String text) {
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        viewModel.sendMessage(text, time);
    }
}

