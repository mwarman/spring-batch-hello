package com.leanstacks.hello.model;

public class Greeting {

    private String language;

    private String text;

    public Greeting() {
    }

    public Greeting(String language, String text) {
        this.language = language;
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "language: " + this.language + ", text: " + this.text;
    }

}
