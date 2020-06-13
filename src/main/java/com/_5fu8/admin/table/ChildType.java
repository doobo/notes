package com._5fu8.admin.table;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Data
@Accessors(chain = true)
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
}
