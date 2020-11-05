package com.app.root.globalpunjabtv.models;


public class PostGetSet {
   
    private Integer postid;
    private Integer catid;
    private String date;
    private String authorName;
    private String thumbnail;
    private String catname;
    private String postTitle;
    private String singlepostlink;

    public PostGetSet(int postid, int catid,String date,String authorName,String thumbnail,String catname, String postTitle, String singlepostlink) {
        this.postid=postid;
        this.catid=catid;
        this.date=date;
        this.authorName=authorName;
        this.thumbnail=thumbnail;
        this.catname=catname;
        this.postTitle=postTitle;
        this.singlepostlink=singlepostlink;
    }


    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        this.catid = catid;
    }
    public String getCatname() {
        return catname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCatname(String catname) {
        this.catname = catname;
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

}