package com._5fu8.admin.table;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public class PicInfo {

    private Integer id;

    private String picPath;

    private Notes notes;

    private Integer width;

    private Integer height;

    private Date firstTime;

    public PicInfo() {
    }

    public PicInfo(Integer id) {
        this.id = id;
    }

    public PicInfo(String picPath, Notes notes, Integer width, Integer height) {
        this.picPath = picPath;
        this.notes = notes;
        this.width = width;
        this.height = height;
        this.firstTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }
}
