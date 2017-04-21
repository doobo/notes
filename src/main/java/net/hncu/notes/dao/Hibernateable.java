package net.hncu.notes.dao;


import org.hibernate.Session;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface Hibernateable {

    Session getSession();

    <T> T getPojoByID(int id, Class<T> tClass);

    <T> boolean addDataByClass(T t);

    <T> boolean updateDataByClass(T t);





}
