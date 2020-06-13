package com._5fu8.admin.services;

import com._5fu8.admin.table.ChildType;
import com._5fu8.admin.table.MainType;
import com._5fu8.admin.table.Notes;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public interface NotesTransaction {

    MainType addMainType(String typeName, String description , Integer userID);

    ChildType addChildType(String typename, String description
            , Integer mainID, Integer userID);

    //更新子分类信息
    Boolean updateChildType(Integer cid,String title,String desc,Integer uid);

    //搜索图片信息
    Object getSearchInPicInfo(Integer curPage,Integer pageSize,String wd);

    //搜索笔记状态大于0的图片信息
    Object getSearchInDelPicInfo(Integer curPage,Integer pageSize,String wd);

    //更新主分类信息
    Boolean updateMainType(Integer mid,String title,String desc,Integer uid);

    List<?> getMainType();

    //获取指定用户和管理员id的主分类信息
    Object getMainTypeByUid(Integer uid,Integer rid);

    //获取指定管理员和用户id已经主分类id的子分类信息
    Object getChildTypeWithMidByUid(Integer mid,Integer uid,Integer rid);

    //得到指定cid的指特定的主类信息
    Object getMainTypeByCID(Integer cid);

    //得到指定cid的特定列的子类信息
    Object getChildTypeByID(Integer cid);

    List<?> getChildTypeByMID(Integer mid);

    List<?> getSimpleNotes(Integer curPage,Integer pageSize,TreeMap<String,Object> map);

    //搜索指定条件的笔记
    Object getSearchNotes(Integer curPage,Integer pageSize
            ,Long startTime,Long endTime,String wd,Integer check,Integer share);

    //搜索指定状态的笔记
    Object getSearchNotesByStatus(Integer curPage,Integer pageSize
            ,Long startTime,Long endTime,String wd,Integer check,Integer share,Integer status,Integer rid);

    //搜索符合条件(不包含root创建的分类)的笔记子分类信息
    Object getSearchChildType(Integer curPage,Integer pageSize,String wd,Integer rid);

    Map<String,Object> getDelNotes(Integer curPage, Integer pageSize);

    Object getSimpleChildType(Integer curPage,Integer pageSize);

    Object getSimpleUser(Integer curPage,Integer pageSize);

    //通过搜索用户账号获取普通用户
    Object getSearchUser(Integer curPage,Integer pageSize,String wd);

    //搜索已删除的用户账号
    Object getSearchDelUser(Integer curPage,Integer pageSize,String wd);

    Object getSimpleKingMaster(Integer curPage,Integer pageSize);

    //获取删除的用户
    Object getDelUser(Integer curPage, Integer pageSize);

    HashSet<String> getIKLuceneCi(String text );

    Notes getNoteByID(Integer id);

    Object getNoteByStaticID(Integer id);

    Object getNextNoteByTime(Long time,Integer nid,Integer cid);

    Object getPreNoteByTime(Long time,Integer nid,Integer cid);

    Notes addNote(String title, String description , Integer mid,
                  String simpleDesc, Integer chid
            , Integer share, Integer userID,String images,Integer check);

    Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID,String images,Integer check);

    /**
     * 彻底从数据库删除笔记，同时更新Lucene
     * @param nid
     * @return
     */
    Boolean delNotesFinally(Integer nid);

    Object getDelNotesByCID(Integer curPage,Integer pageSize,Integer cid);

    Object getNotesNoCheck(Integer curPage,Integer pageSize);

    //获取root的简单笔记信息
    Object getRootNotes(Integer curPage,Integer pageSize,Integer rid);

    Object getNoteToUpdate(Integer id);

    //通过超级管理员获取note笔记
    Object getNoteByRoot(Integer nid);

    //改变笔记审核状态
    Object changeCheck(Integer id,Integer check);

    //更改分享状态
    Object changeShare(Integer id,Integer share);

    //更改笔记普通状态状态，但大于1时，不能更改
    Object changeStatus(Integer id,Integer status);

    //更改彻底删除的笔记的状态
    Object changeStatusByRoot(Integer id,Integer status);

    //更改笔记状态
    Object changeChildType(Integer id,Integer cid);

    //搜索已删除的状态大于指定值的笔记
    Object getSearchAllDelNotes(Integer curPage,Integer pageSize
            ,String wd,Integer status);
}
