package com.web.auth.entity;

public class Token {
    private String body;

    public Token(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
