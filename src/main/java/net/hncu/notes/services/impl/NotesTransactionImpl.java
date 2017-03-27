package net.hncu.notes.services.impl;

import net.hncu.notes.dao.Hibernateable;
import net.hncu.notes.services.NotesTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Service
public class NotesTransactionImpl implements NotesTransaction {

    @Autowired
    Hibernateable hibernateable;

    @Override
    public <T> boolean addDataToSQL(T t) {
        hibernateable.addDataByClass(t);
        return true;
    }
}
