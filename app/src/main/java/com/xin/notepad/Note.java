package com.xin.notepad;

public class Note {

    private String tv_content;
    private String tv_date;

    public Note(String content,String date){
        this.tv_content = content;
        this.tv_date = date;
    }

    public String getTv_content() {
        return tv_content;
    }

    public String getTv_date() {
        return tv_date;
    }
}
