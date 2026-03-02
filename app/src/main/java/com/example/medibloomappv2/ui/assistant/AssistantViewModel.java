package com.example.medibloomappv2.ui.assistant;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.data.local.entity.MedicineEntity;
import com.example.medibloomappv2.data.remote.model.GeminiRequest;
import com.example.medibloomappv2.data.repository.GeminiRepository;
import com.example.medibloomappv2.domain.model.ChatMessage;
import com.example.medibloomappv2.utils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AssistantViewModel extends ViewModel {

    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isTyping = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> aiResponse = new MutableLiveData<>();
    private GeminiRepository geminiRepository;
    private boolean initialized = false;

    public void init(Context context) {
        if (initialized) return;
        initialized = true;

        // Build medicine context string for the AI system prompt
        Executors.newSingleThreadExecutor().execute(() -> {
            PreferenceManager prefs = new PreferenceManager(context);
            String userId = prefs.getUserId();
            StringBuilder sb = new StringBuilder();
            if (userId != null) {
                List<MedicineEntity> meds = AppDatabase.getInstance(context)
                        .medicineDao().getAllMedicinesSync(userId);
                for (MedicineEntity m : meds) {
                    sb.append(m.name).append(" (").append(m.dosage).append("), ");
                }
            }
            String medContext = sb.length() > 0 ? sb.toString() : "No medicines registered yet";
            geminiRepository = new GeminiRepository(medContext);

            // Add welcome message
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            addMessage(new ChatMessage(System.currentTimeMillis(), ChatMessage.ROLE_AI,
                    "Hi there! 🌸 I'm your MediBloom Assistant. I can help you understand your medicines, build healthy habits, and answer general health questions. How can I help you today?",
                    time));
        });

        // Observe AI response
        aiResponse.observeForever(response -> {
            if (response != null && !response.isEmpty()) {
                String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                addMessage(new ChatMessage(System.currentTimeMillis(), ChatMessage.ROLE_AI, response, time));
            }
        });
    }

    public void sendMessage(String text, String time) {
        addMessage(new ChatMessage(System.currentTimeMillis(), ChatMessage.ROLE_USER, text, time));
        if (geminiRepository != null) {
            geminiRepository.sendMessage(text, aiResponse, isTyping, error);
        }
    }

    private void addMessage(ChatMessage msg) {
        List<ChatMessage> current = messages.getValue();
        if (current == null) current = new ArrayList<>();
        List<ChatMessage> updated = new ArrayList<>(current);
        updated.add(msg);
        messages.postValue(updated);
    }

    public LiveData<List<ChatMessage>> getMessages() { return messages; }
    public LiveData<Boolean> getIsTyping() { return isTyping; }
    public LiveData<String> getError() { return error; }
}

