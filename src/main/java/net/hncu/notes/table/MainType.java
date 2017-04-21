package net.hncu.notes.table;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public class MainType {

    private Integer id;

    private String typename;

    private String description;

    private User user;

    private Date firstTime;

    public MainType() {
    }

    public MainType(Integer id) {
        this.id = id;
    }

    public MainType(Integer id, String typename) {
        this.id = id;
        this.typename = typename;
    }

    public MainType(String typename, String description, Integer userID) {
        this.typename = typename;
        this.description = description;
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
        return "MainType{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", firstTime=" + firstTime +
                '}';
    }
}
