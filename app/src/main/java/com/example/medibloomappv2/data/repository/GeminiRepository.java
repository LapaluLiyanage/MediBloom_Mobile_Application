package com.example.medibloomappv2.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.medibloomappv2.BuildConfig;
import com.example.medibloomappv2.data.remote.RetrofitClient;
import com.example.medibloomappv2.data.remote.model.GeminiRequest;
import com.example.medibloomappv2.data.remote.model.GeminiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeminiRepository {

    private static final String TAG = "GeminiRepository";
    private final List<GeminiRequest.Content> conversationHistory = new ArrayList<>();
    private final String medicineContext;

    public GeminiRepository(String medicineContext) {
        this.medicineContext = medicineContext;
    }

    public void sendMessage(String userMessage, MutableLiveData<String> responseLiveData,
                            MutableLiveData<Boolean> loadingLiveData,
                            MutableLiveData<String> errorLiveData) {

        conversationHistory.add(new GeminiRequest.Content("user", userMessage));
        loadingLiveData.postValue(true);

        String systemPrompt = "You are MediBloom Assistant, a friendly and empathetic health companion app. "
                + "You help users understand general information about medications, build healthy habits, "
                + "and manage their medicine routines. Always be warm, supportive, and use emojis occasionally. "
                + "IMPORTANT: Only provide general health information, never personalized medical advice. "
                + "Always recommend consulting a doctor for medical concerns. "
                + "The user currently has these medicines registered: " + medicineContext + ". "
                + "Keep responses concise and easy to understand.";

        GeminiRequest request = new GeminiRequest(
                new ArrayList<>(conversationHistory),
                new GeminiRequest.SystemInstruction(systemPrompt)
        );

        RetrofitClient.getInstance().getGeminiApiService()
                .generateContent(BuildConfig.GEMINI_API_KEY, request)
                .enqueue(new Callback<GeminiResponse>() {
                    @Override
                    public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                        loadingLiveData.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            String text = response.body().getText();
                            if (!text.isEmpty()) {
                                conversationHistory.add(new GeminiRequest.Content("model", text));
                                responseLiveData.postValue(text);
                            } else {
                                errorLiveData.postValue("Empty response from AI.");
                            }
                        } else {
                            Log.e(TAG, "API error: " + response.code());
                            errorLiveData.postValue("AI service error. Please try again.");
                        }
                    }

                    @Override
                    public void onFailure(Call<GeminiResponse> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        Log.e(TAG, "Network failure: " + t.getMessage());
                        errorLiveData.postValue("Network error. Please check your connection.");
                    }
                });
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}

