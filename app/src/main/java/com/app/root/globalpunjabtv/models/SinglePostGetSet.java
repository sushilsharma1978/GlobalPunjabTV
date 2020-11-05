package com.app.root.globalpunjabtv.models;

public class SinglePostGetSet {

    private Integer postid;
    private String postTitle;
    private String singlepostlink;
    private String content;
    private String image;

    public SinglePostGetSet(int postid, String post_title, String singlepostlink, String content, String image) {
    this.postid=postid;
    this.postTitle=post_title;
    this.singlepostlink=singlepostlink;
    this.content=content;
    this.image=image;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getSinglepostlink() {
        return singlepostlink;
    }

    public void setSinglepostlink(String singlepostlink) {
        this.singlepostlink = singlepostlink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}