package net.hncu.notes.table;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public class ChildType {

    private Integer id;

    private String typename;

    private String description;

    private MainType mainType;

    private User user;

    private Date firstTime;

    public ChildType() {
    }

    public ChildType(Integer id, String typename) {
        this.id = id;
        this.typename = typename;
    }

    public ChildType(String typename, String description, Integer mainTypeID, Integer userID) {
        this.typename = typename;
        this.description = description;
        this.mainType = new MainType(mainTypeID);
        this.user = new User(userID);
        this.firstTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MainType getMainType() {
        return mainType;
    }

    public void setMainType(MainType mainType) {
        this.mainType = mainType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    @Override
    public String toString() {
        return "ChildType{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", description='" + description + '\'' +
                ", mainType=" + mainType.getId() +
                ", user=" + user +
                ", firstTime=" + firstTime +
                '}';
    }
}
