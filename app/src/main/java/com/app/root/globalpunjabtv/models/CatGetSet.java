package com.app.root.globalpunjabtv.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatGetSet {

    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("catlink")
    @Expose
    private String catlink;
    @SerializedName("catname")
    @Expose
    private String catname;

    public CatGetSet(String catid, String catlink, String catname) {
        this.catid=catid;
        this.catlink=catlink;
        this.catname=catname;

    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatlink() {
        return catlink;
    }

    public void setCatlink(String catlink) {
        this.catlink = catlink;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

}