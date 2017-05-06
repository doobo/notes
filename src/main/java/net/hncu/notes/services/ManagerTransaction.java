package net.hncu.notes.services;

import net.hncu.notes.table.ChildType;
import net.hncu.notes.table.MainType;
import net.hncu.notes.table.Notes;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface ManagerTransaction {

    MainType addMainType(String typeName, String description, Integer userID);

    ChildType addChildType(String typename, String description
            , Integer mainID, Integer userID);

    List<?> getMainType();

    List<?> getChildTypeByMID(Integer mid);

    //获取指定用户的notes
    Object getNotesMap(Integer curPage,Integer pageSize,Integer uid);

    Object getDelNotes(Integer curPage, Integer pageSize,Integer uid);

    //搜索符合条件的已删除笔记
    Object getDelNotesSearch(Integer curPage,Integer pageSize,Integer uid
            ,String wd);

    //获取符合条件的notes
    Object getNotesByTerms(Integer curPage,Integer pageSize,Integer uid
            ,String terms,Long startTime,Long endTime,String wd);

    Object getSimpleChildType(Integer curPage, Integer pageSize,Integer id);

    //搜索符合条件的子类信息
    Object getSearchChildType(Integer curPage,Integer pageSize,String wd
            ,Integer uid);

    //搜索符合条件的图片信息
    Object getSearchInPicInfo(Integer curPage,Integer pageSize,String wd
            ,Integer uid);

    //更新子类信息
    Object updateChildType(Integer cid,String title,String desc,Integer uid);

    //通过子类id获取子类信息
    Object getChildTypeByID(Integer cid,Integer uid);

    //更新主类信息
    Object updateMainType(Integer mid,String title,String desc,Integer uid);

    //通过子类id获取对应的主类信息
    Object getMainTypeByCID(Integer cid,Integer uid);

    Object getIKLuceneCi(String text);

    Object getNoteByID(Integer id);

    Object getNoteByStaticID(Integer id,Integer uid);

    Object getNextNoteByTime(Long time, Integer nid, Integer cid);

    Object getPreNoteByTime(Long time, Integer nid, Integer cid);

    Notes updateNote(String title, String description, Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share, Integer nid, Integer userID,String images);

    Object getDelNotesByCID(Integer curPage, Integer pageSize, Integer cid);

    Object getNotesNoCheck(Integer curPage, Integer pageSize,Integer uid);

    Object getNoteToUpdate(Integer id,Integer uid);

    //改变笔记审核状态
    Object changeCheck(Integer id, Integer check,Integer uid);

    //更改分享状态
    Object changeShare(Integer id, Integer share,Integer uid);

    //更改笔记状态
    Object changeStatus(Integer id, Integer status,Integer uid);

}
