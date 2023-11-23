package com.dam.crazydisplay;

public class MessageData {
    String message;
    String date;

    public MessageData(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
