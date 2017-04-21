package net.hncu.notes.table;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public class Notes {

    private Integer id;

    private String title;

    private ChildType childType;

    private User user;

    private String description;

    private Integer check;

    private Integer status;

    private Integer share;

    private Date firstTime;

    public Notes() {
    }

    public Notes(Integer id, String title, ChildType childType
            , Integer check, Integer status
            , Integer share, Date firstTime) {
        this.id = id;
        this.title = title;
        this.childType = childType;
        this.check = check;
        this.status = status;
        this.share = share;
        this.firstTime = firstTime;
    }

    public Notes(String title,
                 Integer childTypeID, Integer userID
                , String description, Integer share) {
        this.title = title;
        this.childType = new ChildType(childTypeID,null);
        this.user = new User(userID);
        this.description = description;
        this.share = share;
        this.firstTime = new Date();
        this.status = 0;
        this.check = 0 ;
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

    public ChildType getChildType() {
        return childType;
    }

    public void setChildType(ChildType childType) {
        this.childType = childType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", childType=" + childType.getId() +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", check=" + check +
                ", status=" + status +
                ", share=" + share +
                ", firstTime=" + firstTime +
                '}';
    }
}
