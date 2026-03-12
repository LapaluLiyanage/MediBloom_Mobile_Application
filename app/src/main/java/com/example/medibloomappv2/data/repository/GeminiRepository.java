package com.example.medibloomappv2.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.medibloomappv2.BuildConfig;
import com.example.medibloomappv2.data.remote.RetrofitClient;
import com.example.medibloomappv2.data.remote.model.GeminiRequest;
import com.example.medibloomappv2.data.remote.model.GeminiResponse;

import java.io.IOException;
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

    public void sendMessage(String userMessage,
                            MutableLiveData<String> responseLiveData,
                            MutableLiveData<Boolean> loadingLiveData,
                            MutableLiveData<String> errorLiveData) {

        conversationHistory.add(new GeminiRequest.Content("user", userMessage));
        loadingLiveData.postValue(true);

        String systemPrompt = "You are MediBloom Assistant, a friendly and empathetic health companion. "
                + "You help users understand general information about medications, build healthy habits, "
                + "and manage their medicine routines. Always be warm, supportive, and use emojis occasionally. "
                + "IMPORTANT: Only provide general health information, never personalised medical advice. "
                + "Always recommend consulting a doctor for medical concerns. "
                + "The user currently has these medicines registered: " + medicineContext + ". "
                + "Keep responses concise and easy to understand.";

        GeminiRequest request = new GeminiRequest(
                new ArrayList<>(conversationHistory),
                new GeminiRequest.SystemInstruction(systemPrompt)
        );

        // Verify the key is not empty before making the call
        if (BuildConfig.GEMINI_API_KEY == null || BuildConfig.GEMINI_API_KEY.isEmpty()) {
            loadingLiveData.postValue(false);
            errorLiveData.postValue("Gemini API key is missing. Please add it to local.properties.");
            return;
        }

        RetrofitClient.getInstance().getGeminiApiService()
                .generateContent(BuildConfig.GEMINI_API_KEY, request)
                .enqueue(new Callback<GeminiResponse>() {
                    @Override
                    public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                        loadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            String text = response.body().getText();
                            if (text != null && !text.isEmpty()) {
                                conversationHistory.add(new GeminiRequest.Content("model", text));
                                responseLiveData.postValue(text);
                            } else {
                                // Response was blocked or empty – remove the user message from history
                                if (!conversationHistory.isEmpty())
                                    conversationHistory.remove(conversationHistory.size() - 1);
                                errorLiveData.postValue("The assistant couldn't generate a response. Please try rephrasing.");
                            }
                        } else {
                            // Read the actual error body so we know what went wrong
                            String errorBody = "";
                            try {
                                if (response.errorBody() != null) {
                                    errorBody = response.errorBody().string();
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "Could not read error body", e);
                            }

                            Log.e(TAG, "API error " + response.code() + ": " + errorBody);

                            // Remove the failed user message from history so retry works
                            if (!conversationHistory.isEmpty())
                                conversationHistory.remove(conversationHistory.size() - 1);

                            // Show a specific, helpful message based on the HTTP status code
                            String userMessage2 = errorMessageFor(response.code(), errorBody);
                            errorLiveData.postValue(userMessage2);
                        }
                    }

                    @Override
                    public void onFailure(Call<GeminiResponse> call, Throwable t) {
                        loadingLiveData.postValue(false);
                        if (!conversationHistory.isEmpty())
                            conversationHistory.remove(conversationHistory.size() - 1);
                        Log.e(TAG, "Network failure: " + t.getMessage(), t);
                        errorLiveData.postValue("Network error. Please check your internet connection and try again.");
                    }
                });
    }

    /** Returns a user-friendly error string based on HTTP status code and raw error body. */
    private String errorMessageFor(int code, String body) {
        switch (code) {
            case 400:
                if (body.contains("API_KEY_INVALID") || body.contains("invalid"))
                    return "Your Gemini API key is invalid. Please check local.properties.";
                return "Bad request sent to the AI service. Please try again.";
            case 401:
                return "Authentication failed. Please check your Gemini API key.";
            case 403:
                if (body.contains("API_KEY_INVALID"))
                    return "Your Gemini API key is invalid or has been revoked.";
                if (body.contains("PERMISSION_DENIED"))
                    return "This API key does not have permission to use Gemini. Enable it at aistudio.google.com.";
                return "Access denied. Please check your Gemini API key.";
            case 404:
                return "AI model not found. Please check the model name in GeminiApiService.";
            case 429:
                return "You've hit the free quota limit. Please wait a moment and try again.";
            case 500:
            case 503:
                return "Google's AI service is temporarily unavailable. Please try again later.";
            default:
                return "AI service error (" + code + "). Please try again.";
        }
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}
