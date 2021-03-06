package com._5fu8.admin.services.impl;

import com._5fu8.admin.dao.impl.AbstractHibernate;
import com._5fu8.admin.lucene.NoteDocument;
import com._5fu8.admin.lucene.NotesLucene;
import com._5fu8.admin.lucene.util.AbstractLuceneIndex;
import com._5fu8.admin.services.NotesTransaction;
import com._5fu8.admin.table.ChildType;
import com._5fu8.admin.table.MainType;
import com._5fu8.admin.table.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Service
@Transactional
public class NotesTransactionImpl extends AbstractHibernate implements NotesTransaction {

    private final String ORDER_TIME = " ORDER BY n.firstTime desc";

    @Override
    public Class getEntityClass() {
        return Notes.class;
    }

    @Autowired
    NotesLucene nl;

    @Override
    public MainType addMainType(String typeName,String description ,Integer userID){
        MainType mainType = new MainType(typeName,description,userID);
        if(isExitMainTypeName(typeName)) return null;
        addDataByClass(mainType);
        return mainType;
    }

    @Override
    public List<?> getMainType(){
        String hql = "SELECT new MainType(m.id, m.typename) FROM MainType m";
        return getQueryByHQL(hql,null,null).list();
    }

    @Override
    public Object getMainTypeByUid(Integer uid,Integer rid){
        String hql;
        if(uid != null){
            hql = "SELECT new MainType(m.id, m.typename) FROM MainType m" +
                    " WHERE m.user.id = :uid OR m.user.id=:rid ORDER BY m.user.id";
            return getQueryByHQL(hql)
                    .setParameter("uid",uid)
                    .setParameter("rid",rid)
                    .list();
        }
        hql = "SELECT new MainType(m.id, m.typename) FROM MainType m" +
                " WHERE m.user.id=:rid ORDER BY m.user.id";
        return getQueryByHQL(hql)
                .setParameter("rid",rid)
                .list();
    }

    @Override
    public Object getChildTypeWithMidByUid(Integer mid,Integer uid,Integer rid){
        String hql = "SELECT new ChildType(c.id, c.typename) FROM ChildType c"
                +" WHERE c.mainType.id = :mid AND (c.user.id = :uid " +
                "OR c.user.id=:rid) ORDER BY c.user.id";
        return getQueryByHQL(hql)
                .setParameter("mid", mid)
                .setParameter("uid",uid)
                .setParameter("rid",rid)
                .list();
    }

    @Override
    public Object getMainTypeByCID(Integer cid){
        String hql = "SELECT c.mainType.id,c.mainType.typename,c.mainType.description" +
                " FROM ChildType c " +
                "WHERE c.id =:cid ";
        return getQueryByHQL(hql)
                .setParameter("cid",cid)
                .uniqueResult();
    }

    @Override
    public Object getChildTypeByID(Integer cid){
        String hql = "SELECT c.id,c.typename,c.description FROM ChildType c " +
                "WHERE c.id =:cid";
        return getQueryByHQL(hql)
                .setParameter("cid",cid)
                .uniqueResult();
    }


    @Override
    public List<?> getChildTypeByMID(Integer mid){
        String hql = "SELECT new ChildType(c.id, c.typename) FROM ChildType c"
                +" WHERE c.mainType = :mainType";
        MainType mainType = new MainType(mid,null);
        return getQueryByHQL(hql,null,null)
                .setParameter("mainType", mainType)
                .list();
    }

    @Override
    public List<?> getSimpleNotes(Integer curPage,Integer pageSize
            ,TreeMap<String,Object> map){
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0";
        Object count = getUniqueResultByHQL(hql);
        map.put("count",count);
        map.put("pageCount",getPageCount(pageSize,Integer.parseInt(count.toString())));
        map.put("curPage",curPage);
        map.put("pageSize",pageSize);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0"+ORDER_TIME;
        return getQueryByHQL(hql,curPage,pageSize).list();
    }

    @Override
    public Object getSearchNotes(Integer curPage,Integer pageSize
            ,Long startTime,Long endTime,String wd,Integer check,Integer share){
        String terms = AbstractServices.setHQLByParams(check,share,wd,
                startTime,endTime).toString();
        wd = AbstractServices.getLikeWd(wd);

        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes n WHERE n.status = 0 "+terms;
        if(startTime != null && endTime != null && wd != null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0  "
                   + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        if(startTime !=null && endTime !=null && wd == null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 "
                    + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .list());
            return map;
        }

        if(startTime == null && endTime == null && wd != null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 "
                    + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                .uniqueResult());
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0 "
                + terms+ORDER_TIME;
        map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }

    @Override
    public Object getSearchNotesByStatus(Integer curPage,Integer pageSize
            ,Long startTime,Long endTime,String wd,Integer check,Integer share,Integer status,Integer rid){
        String terms = " "+ AbstractServices.setHQLByParams(check,share,wd,
                startTime,endTime).toString();
        terms = terms + " AND n.user.id != " + rid +" ";
        wd = AbstractServices.getLikeWd(wd);
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes n WHERE n.status = "+status+terms;
        if(startTime != null && endTime != null && wd != null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = "+status
                    + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        if(startTime !=null && endTime !=null && wd == null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = "+status
                    + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .list());
            return map;
        }

        if(startTime == null && endTime == null && wd != null){
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = "+status
                    + terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                .uniqueResult());
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = "+status
                + terms+ORDER_TIME;
        map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;

    }

    @Override
    public Object getSearchChildType(Integer curPage,Integer pageSize,String wd,Integer rid) {
        TreeMap<String, Object> map = new TreeMap<>();
        wd = AbstractServices.getLikeWd(wd);
        String hql = "SELECT count(*)  FROM  ChildType c ";
        if (wd != null && !wd.isEmpty()) {
            hql = hql + " WHERE c.typename like :wd AND c.user.id != "+rid;
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("wd", wd)
                    .uniqueResult());
            hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                    + " FROM ChildType c WHERE"
                    + " c.typename like :wd AND c.user.id != "+rid + " ORDER BY c.mainType.id";
            map.put("childTypes", getQueryByHQL(hql, curPage, pageSize)
                    .setParameter("wd", wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql+"WHERE c.user.id != " +rid);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c WHERE c.user.id != "+rid+" ORDER BY c.mainType.id";
        map.put("childTypes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Map<String,Object> getDelNotes(Integer curPage, Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 1";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.firstTime FROM Notes n " +
                "WHERE n.status = 1"+ORDER_TIME;
        map.put("delNotes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleChildType(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM ChildType";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c";
        map.put("childTypes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleUser(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 0 AND u.type = 0";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 0 AND u.type = 0";
        map.put("users",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    public Object getSearchUser(Integer curPage,Integer pageSize,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 0 AND u.type = 0";
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql + " AND u.username like :wd";
            AbstractServices.setReturnMap(curPage, pageSize, map
                    , getQueryByHQL(hql)
                            .setParameter("wd",wd)
                            .uniqueResult());
            hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                    +" FROM User u WHERE  u.status = 0 AND u.type = 0" +
                    " AND u.username like :wd";
            map.put("users",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 0 AND u.type = 0";
        map.put("users",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSearchDelUser(Integer curPage,Integer pageSize,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 1 AND u.type = 0";
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql + " AND u.username like :wd";
            AbstractServices.setReturnMap(curPage, pageSize, map
                    , getQueryByHQL(hql)
                            .setParameter("wd",wd)
                            .uniqueResult());
            hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                    +" FROM User u WHERE  u.status = 1 AND u.type = 0" +
                    " AND u.username like :wd";
            map.put("users",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 1 AND u.type = 0";
        map.put("users",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleKingMaster(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 0 AND u.type = 1";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT  u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 0 AND u.type = 1";
        map.put("kingMaster",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getDelUser(Integer curPage, Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 1 AND u.type = 0";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT  u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE u.status = 1 AND u.type = 0";
        map.put("delUser",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }


    @Override
    public HashSet<String> getIKLuceneCi(String text ){
        return  AbstractLuceneIndex.getIKLuceneCi(text);
    }


    @Override
    public Notes getNoteByID(Integer id){
        String hql = "SELECT new Notes(n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id)"
                +" FROM Notes n WHERE n.status = 0 AND n.id = "+id;
        return getQueryByHQL(hql,Notes.class)
                .uniqueResult();
    }

    @Override
    public Object getNoteByStaticID(Integer id){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE n.id = "+id;
        return getQueryByHQL(hql,null,null).list();
    }

    @Override
    public Object getNextNoteByTime(Long time,Integer nid,Integer cid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE  n.status = 0 AND n.firstTime > :time" +
                " AND n.childType.id = "+cid
                +" ORDER BY n.firstTime";

        return getQueryByHQL(hql,1,1)
                .setParameter("time",new Date(time)).list();
    }

    @Override
    public Object getPreNoteByTime(Long time,Integer nid,Integer cid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE  n.status = 0 AND n.firstTime < :time" +
                " AND n.childType.id = "+cid
                +ORDER_TIME;
        return getQueryByHQL(hql,1,1)
                .setParameter("time",new Date(time)).list();
    }

    @Override
    public ChildType addChildType(String typename, String description
            , Integer mainID, Integer userID){
        ChildType childType = new ChildType(typename,description, mainID,userID);
        if(isExitChildTypeName(typename,mainID)) return null;
        addDataByClass(childType);
        return childType;
    }

    @Override
    public Boolean updateChildType(Integer cid,String title,String desc,Integer uid){
        ChildType childType = getPojoByID(cid,ChildType.class);
        if(isExitChildTypeName(title,childType.getMainType().getId())) return false;
        if(uid != null){
            if(childType.getUser().getId() == uid)
                return false;
        }
        if(title.equals(childType.getTypename()) && desc.equals(childType.getDescription()))
            return true;
        childType.setTypename(title);
        childType.setDescription(desc);
        updateDataByClass(childType);
        return true;
    }

    @Override
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  PicInfo p WHERE " +
                "p.notes.status=0 AND p.width>=300";
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql +" AND p.notes.title like :wd";
            AbstractServices.setReturnMap(curPage,pageSize,map,getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id," +
                    "p.notes.title"
                    +" FROM PicInfo p WHERE "
                    + "p.notes.title like :wd AND p.width>=300 AND p.notes.status=0 ORDER BY p.notes desc";
            map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id"
                +",p.notes.title FROM PicInfo p WHERE "
                +"p.notes.status=0 AND p.width>=300 ORDER BY p.notes desc";
        map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }

    public Object getSearchInDelPicInfo(Integer curPage,Integer pageSize,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  PicInfo p WHERE " +
                "p.notes.status>1";
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql +" AND p.notes.title like :wd";
            AbstractServices.setReturnMap(curPage,pageSize,map,getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id," +
                    "p.notes.title"
                    +" FROM PicInfo p WHERE "
                    + "p.notes.title like :wd AND p.notes.status>1 ORDER BY p.notes desc";
            map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id"
                +",p.notes.title FROM PicInfo p WHERE "
                +"p.notes.status>1 ORDER BY p.notes desc";
        map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }

    @Override
    public Boolean updateMainType(Integer mid,String title,String desc,Integer uid){
        MainType mainType = getPojoByID(mid,MainType.class);
        if(isExitMainTypeName(title)) return false;
        if(uid != null){
            if(mainType.getUser().getId() == uid)
                return false;
        }
        if(title.equals(mainType.getTypename()) && desc.equals(mainType.getDescription()))
            return true;
        mainType.setTypename(title);
        mainType.setDescription(desc);
        updateDataByClass(mainType);
        return true;
    }

    //是否存在与子类别相同的类别名
    public boolean isExitChildTypeName(String typename,Integer mid){
        String hql = "SELECT count(*) FROM ChildType c WHERE c.mainType.id = :mid" +
                " AND c.typename = :typename";
        if(getQueryByHQL(hql)
                .setParameter("mid",mid)
                .setParameter("typename",typename)
                .uniqueResult()
                .toString().equals("0")){
            return false;
        }
        return true;
    }

    //是否存在与主类别相同的主类名
    public boolean isExitMainTypeName(String typename){
        String hql = "SELECT count(*) FROM MainType m WHERE" +
                "  m.typename = :typename";
        if(getQueryByHQL(hql)
                .setParameter("typename",typename)
                .uniqueResult()
                .toString().equals("0")){
            return false;
        }
        return true;
    }

    @Override
    public Notes addNote(String title,String description ,Integer mid,
                        String simpleDesc,Integer chid,Integer share
            ,Integer userID,String images,Integer check){
        if(check == null) check = 0;
        Notes notes = new Notes(title,chid,userID,description,share);
        notes.setCheck(check);
        addDataByClass(notes);
        AbstractServices.insertImages(images, notes,this);
        NoteDocument noteDocument = new NoteDocument(notes.getId(),title,simpleDesc
        ,userID,chid,mid,0,check,share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return notes;
    }

    @Override
    public  Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID,String images,Integer check){
        Notes notes = getPojoByID(nid,Notes.class);

        if(check == null ) check = 0;

        if(notes == null) throw  new RuntimeException("The Note is not exit!");

        notes.setTitle(title);notes.setShare(share);
        notes.setChildType(new ChildType(chid,null));
        notes.setDescription(description);
        //notes.setUser(new User(userID));//不修改笔记所属用户
        notes.setCheck(check);
        updateDataByClass(notes);

        String hql = "DELETE FROM PicInfo p WHERE p.notes.id = :id";
        getQueryByHQL(hql)
                .setParameter("id",notes.getId())
                .executeUpdate();

        NoteDocument noteDocument = new NoteDocument(notes.getId(),title,simpleDesc
                ,notes.getUser().getId(),chid,mid,0,check,share);
        noteDocument.setFirstTime(notes.getFirstTime());
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");

        AbstractServices.insertImages(images, notes,this);
        return notes;
    }

    /**
     * 彻底从数据库删除笔记，同时更新Lucene
     * @param nid
     * @return
     */
    @Override
    public Boolean delNotesFinally(Integer nid){
        Notes notes = getPojoByID(nid,Notes.class);
        if(notes == null) return false;
        if(notes.getStatus() < 2) return false;
        String hql = "DELETE FROM PicInfo p WHERE p.notes.id = :id";
        getQueryByHQL(hql)
                .setParameter("id",notes.getId())
                .executeUpdate();
        NoteDocument noteDocument = new NoteDocument(notes.getId(),notes.getTitle(),
                notes.getDescription()
                ,notes.getUser().getId(),notes.getChildType().getId(),
                notes.getChildType().getMainType().getId()
                ,4,0,notes.getShare());
        noteDocument.setFirstTime(notes.getFirstTime());
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        getSession().delete(notes);
        return true;
    }

    @Override
    public Object getDelNotesByCID(Integer curPage,Integer pageSize,Integer cid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0" +
                " AND n.childType.id = "+cid;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.user.id " +
                ",n.user.username,n.firstTime,n.childType.typename FROM Notes n " +
                "WHERE n.status = 0"+ " AND n.childType.id = :cid" + ORDER_TIME;
        map.put("Notes",getQueryByHQL(hql,curPage,pageSize)
                .setParameter("cid",cid)
                .list());
        return map;
    }

    @Override
    public Object getNotesNoCheck(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0 " +
                " AND n.check = 0";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.user.id " +
                ",n.user.username,n.firstTime,n.childType.typename FROM Notes n " +
                "WHERE n.status = 0"+" AND n.check = 0 "+ORDER_TIME;
        map.put("Notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }

    @Override
    public Object getRootNotes(Integer curPage,Integer pageSize,Integer rid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0 " +
                " AND n.user.id = "+rid;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.user.id " +
                ",n.user.username,n.firstTime,n.childType.typename FROM Notes n " +
                "WHERE n.status = 0"+" AND n.user.id = "+rid+ORDER_TIME;
        map.put("Notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }


    @Override
    public Object getNoteToUpdate(Integer id){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id" +
                ",n.childType.mainType.id,n.share"
                +" FROM Notes n WHERE  n.status = 0 AND n.id = "+id;
        return getUniqueResultByHQL(hql);
    }

    @Override
    public Object getNoteByRoot(Integer nid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id" +
                ",n.childType.mainType.id,n.share"
                +" FROM Notes n WHERE n.id = "+nid;
        return getUniqueResultByHQL(hql);
    }

    @Override
    public Object changeCheck(Integer id,Integer check){
        if(check != 1 && check != 0) check = 0;
        Notes notes = getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setCheck(check);
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setCheck(check);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeShare(Integer id,Integer share){
        if(share != 1 && share != 0) share = 0;
        Notes notes = getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setShare(share);
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setShare(share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeStatus(Integer id,Integer status){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes == null) return false;
        if(notes.getStatus()>1) return false;
        notes.setStatus(status);
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null){
            //throw new RuntimeException("未更新缓存");
            return true;
        }
        noteDocument.setStatus(status);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeStatusByRoot(Integer id,Integer status){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setStatus(status);
        updateDataByClass(notes);
        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setStatus(status);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeChildType(Integer id,Integer cid){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setChildType(new ChildType(cid,null));
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setChid(cid);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object getSearchAllDelNotes(Integer curPage,Integer pageSize
            ,String wd,Integer status){
       TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes n WHERE n.status > "+status;
        if(wd != null){
            wd = AbstractServices.getLikeWd(wd);
            hql = "SELECT count(*) FROM Notes n WHERE n.title like :wd AND n.status > "+status;
            AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.title like :wd AND n.status > "+status
                    +ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status > "+status
                    +ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .list());
            return map;
    }


    //设置返回值的统一名称
    private void setReturnMap(Integer curPage, Integer pageSize,
                              TreeMap<String, Object> map, String hql) {
        map.put("count",getUniqueResultByHQL(hql));
        map.put("pageCount",getPageCount(pageSize,
                Integer.parseInt(map.get("count").toString())));
        map.put("curPage",curPage);
        map.put("pageSize",pageSize);
    }


    // 计算分页的总页面数
    private int getPageCount(int pageSize, int count) {
        int pageCount = 1;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }
}
