package com.example.medibloomappv2.domain.model;

public class ChatMessage {
    public static final String ROLE_USER = "user";
    public static final String ROLE_AI = "ai";

    private final long id;
    private final String role;
    private final String text;
    private final String time;

    public ChatMessage(long id, String role, String text, String time) {
        this.id = id;
        this.role = role;
        this.text = text;
        this.time = time;
    }

    public long getId() { return id; }
    public String getRole() { return role; }
    public String getText() { return text; }
    public String getTime() { return time; }
    public boolean isUser() { return ROLE_USER.equals(role); }
}

