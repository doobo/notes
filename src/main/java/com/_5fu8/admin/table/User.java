package com._5fu8.admin.table;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Data
@Accessors(chain = true)
public class User {

    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private Integer type;

    private Integer status;

    private Date signTime;

    public User( String username, String nickname,
                String password, Integer type, Integer status, Date signTime) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.type = type;
        this.status = status;
        this.signTime = signTime;
    }

    public User( String username, String nickname,
                 String password, Integer type, Integer status) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.type = type;
        this.status = status;
        this.signTime = new Date();
    }

    public User(Integer id) {
        this.id = id;
    }

    public User() {
    }
}
