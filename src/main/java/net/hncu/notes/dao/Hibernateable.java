package net.hncu.notes.dao;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface Hibernateable {

    <T> boolean addDataByClass(T t);
}
