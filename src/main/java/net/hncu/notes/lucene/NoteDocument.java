package net.hncu.notes.lucene;

import net.hncu.notes.lucene.util.LuceneAnnotation;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@LuceneAnnotation
public class NoteDocument {

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "id")
    public Integer id;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.TextField
            ,fieldName = "title")
    private String title;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.TextField
            ,fieldName = "description")
    private String description;

    private Date firstTime;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "uid")
    private Integer uid;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "chid")
    private Integer chid;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "maid")
    private Integer maid;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "status")
    private Integer status;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "check")
    private Integer check;

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.StringField
            ,fieldName = "share")
    private Integer share;


    public NoteDocument() {
    }

    public NoteDocument(Integer id, String title, String description,
                        Integer uid, Integer chid,
                        Integer maid, Integer status, Integer check, Integer share) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.firstTime = new Date();
        this.uid = uid;
        this.chid = chid;
        this.maid = maid;
        this.status = status;
        this.check = check;
        this.share = share;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getChid() {
        return chid;
    }

    public void setChid(Integer chid) {
        this.chid = chid;
    }

    public Integer getMaid() {
        return maid;
    }

    public void setMaid(Integer maid) {
        this.maid = maid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    @LuceneAnnotation(
            field=LuceneAnnotation.FieldEnum.LongField
            ,fieldName = "time")
    public long setTime(){
        if(this.firstTime == null){
            return new Date().getTime();
        }
        return this.firstTime.getTime();
    }
}
