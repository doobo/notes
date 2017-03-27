package demo;

import net.hncu.notes.lucene.util.LuceneAnnotation;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/21.
 */

@LuceneAnnotation
public class User {
    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.StringField,fieldName = "id")
    private Integer id;

    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.StringField,fieldName = "username")
    private String username;

    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.StringField,fieldName = "nickname")
    private String nickname;

    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.TextField,fieldName = "password")
    private String password;

    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.StringField,fieldName = "status")
    private Integer status;

    private Integer type;

    public User(Integer id, String username, String nickname, String password, Integer status, Integer type) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.status = status;
        this.type = type;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }


    @LuceneAnnotation(field = LuceneAnnotation.FieldEnum.LongField,fieldName = "time")
    private long setTime(){
        return new Date().getTime();
    }
}
