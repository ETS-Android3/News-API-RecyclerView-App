package com.samim.bbcnewsdemoapp;

class BBCNewsClass {

    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;

    @Override
    public String toString() {
        return  title + "\n" + author + " - " + publishedAt;
    }
}
