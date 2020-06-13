package com._5fu8.admin.table;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Data
@Accessors(chain = true)
public class Notes {

    private Integer id;

    private String title;

    private ChildType childType;

    private User user;

    private String description;

    private Integer check;

    private Integer status;

    private Integer share;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false, name = "create_time")
    @org.hibernate.annotations.Generated(
            GenerationTime.INSERT
    )
    private Date firstTime;

    @Id
    @GenericGenerator(name="UUIDGENERATE",strategy="uuid2")
    @GeneratedValue(generator="UUIDGENERATE")
    @Column(name="aid",length=24)
    private String aid;

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
        this.check = 0;
    }

    public Notes(String title, String userName, Date firstTime, String typename
            , String description, Integer nid, Integer cid) {
        this.setTitle(title)
            .setDescription(description)
            .setId(nid)
            .setFirstTime(firstTime)
            .setChildType(new ChildType().setId(cid).setTypename(typename))
            .setUser(new User().setNickname(userName));
    }
}
