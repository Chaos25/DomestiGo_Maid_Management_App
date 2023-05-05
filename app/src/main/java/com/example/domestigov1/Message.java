package com.example.domestigov1;

import com.google.firebase.database.ServerValue;

//public class Message {
//    private String text;
//
//    public Message() {}
//
//    public Message(String text) {
//        this.text = text;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//}
public class Message {
    private String text;
    private String from;
    private long timestamp;

    public Message() {}

    public Message(String text, String from) {
        this.text = text;
        this.from = from;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


