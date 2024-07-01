package com.sal.leseniyashuleyasabato;


public class LessonModels {
    private final String date;
    private final String day_content;
    private final String day_question;
    private final String day_title;
    private final int share_image;

    public LessonModels(String date, int share_image, String day_title, String day_content, String day_question) {
        this.date = date;
        this.share_image = share_image;
        this.day_title = day_title;
        this.day_content = day_content;
        this.day_question = day_question;
    }

    public String getDate() {
        return this.date;
    }

    public String getDay_title() {
        return this.day_title;
    }

    public String getDay_content() {
        return this.day_content;
    }

    public String getDay_question() {
        return this.day_question;
    }

    public int getShare_image() {
        return this.share_image;
    }
}