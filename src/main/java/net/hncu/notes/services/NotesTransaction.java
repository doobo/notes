package net.hncu.notes.services;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface NotesTransaction {

    <T> boolean addDataToSQL(T t);
}
