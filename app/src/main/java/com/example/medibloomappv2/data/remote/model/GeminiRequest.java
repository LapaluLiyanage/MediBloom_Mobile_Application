package com.example.medibloomappv2.data.remote.model;

import java.util.List;

public class GeminiRequest {
    public List<Content> contents;
    public SystemInstruction system_instruction;

    public GeminiRequest(List<Content> contents, SystemInstruction systemInstruction) {
        this.contents = contents;
        this.system_instruction = systemInstruction;
    }

    public static class Content {
        public String role; // "user" or "model"
        public List<Part> parts;

        public Content(String role, String text) {
            this.role = role;
            this.parts = List.of(new Part(text));
        }
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }

    public static class SystemInstruction {
        public List<Part> parts;

        public SystemInstruction(String text) {
            this.parts = List.of(new Part(text));
        }
    }
}

