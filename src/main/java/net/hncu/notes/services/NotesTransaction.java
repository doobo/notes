package net.hncu.notes.services;

import net.hncu.notes.table.ChildType;
import net.hncu.notes.table.MainType;
import net.hncu.notes.table.Notes;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface NotesTransaction {

    MainType addMainType(String typeName, String description , Integer userID);

    ChildType addChildType(String typename, String description
            , Integer mainID, Integer userID);

    List<?> getMainType();

    List<?> getChildTypeByMID(Integer mid);

    List<?> getSimpleNotes(Integer curPage,Integer pageSize,TreeMap<String,Object> map);

    Object getDelNotes(Integer curPage,Integer pageSize);

    Object getSimpleChildType(Integer curPage,Integer pageSize);

    Object getSimpleUser(Integer curPage,Integer pageSize);

    Object getSimpleKingMaster(Integer curPage,Integer pageSize);

    Object getDelUser(Integer curPage,Integer pageSize);

    Object getIKLuceneCi(String text );

    Object getNoteByID(Integer id);

    Object getNoteByStaticID(Integer id);

    Object getNextNoteByTime(Long time,Integer nid,Integer cid);

    Object getPreNoteByTime(Long time,Integer nid,Integer cid);

    Notes addNote(String title, String description , Integer mid,
                  String simpleDesc, Integer chid, Integer share, Integer userID);

    Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID);

    Object getDelNotesByCID(Integer curPage,Integer pageSize,Integer cid);

    Object getNotesNoCheck(Integer curPage,Integer pageSize);

    Object getNoteToUpdate(Integer id);

    //改变笔记审核状态
    Object changeCheck(Integer id,Integer check);

    //更改分享状态
    Object changeShare(Integer id,Integer share);

    //更改笔记状态
    Object changeStatus(Integer id,Integer status);

    //更改笔记状态
    Object changeChildType(Integer id,Integer cid);

    <T> boolean testUnitHb();
}
