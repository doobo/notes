package net.hncu.notes.dao.impl;

import net.hncu.notes.dao.Hibernateable;
import net.hncu.notes.table.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Repository
public class HibernateableImpl implements Hibernateable {
    @Autowired
    private SessionFactory sessionFactory;

    //获取和当前线程绑定的 Session.
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存类的数据到数据库
     */
    @Override
    public <T> boolean addDataByClass(T t) {
        try {
            System.out.println(getSession().get(User.class,1));
            return true;
        } catch (Exception e) {
          throw new RuntimeException(e.getMessage());
        }
    }


    }
