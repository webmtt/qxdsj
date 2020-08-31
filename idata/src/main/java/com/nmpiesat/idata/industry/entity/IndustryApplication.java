package com.nmpiesat.idata.industry.entity;

public class IndustryApplication {

    private String id;
    private String imageurl;		// 图片URL
    private String title;		// 标题
    private String entitle;     //英文标题
    private String content;     //简介
    private String example;//样例
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEntitle() {
        return entitle;
    }

    public void setEntitle(String entitle) {
        this.entitle = entitle;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "IndustryApplication{" +
                "id='" + id + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", title='" + title + '\'' +
                ", entitle='" + entitle + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
