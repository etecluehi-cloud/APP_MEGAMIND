package com.example.myapplication;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import com.google.common.util.concurrent.ListenableFuture;

public class GeminiManager {

    private static final String API_KEY = "AIzaSyD-DnrcAY-Xs538FMl1hVFWfOIT_MkE-78";

    private final GenerativeModelFutures model;

    public GeminiManager() {

        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash",
                API_KEY
        );

        model = GenerativeModelFutures.from(gm);
    }

    public ListenableFuture<GenerateContentResponse> corrigirRedacao(String prompt) {

        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        return model.generateContent(content);
    }
}