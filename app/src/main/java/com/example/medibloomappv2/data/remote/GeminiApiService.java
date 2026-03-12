package com.example.medibloomappv2.data.remote;

import com.example.medibloomappv2.data.remote.model.GeminiRequest;
import com.example.medibloomappv2.data.remote.model.GeminiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {

    // gemini-2.0-flash-001 = stable pinned version (avoids silent model deprecations)
    @POST("v1beta/models/gemini-2.0-flash-001:generateContent")
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}
