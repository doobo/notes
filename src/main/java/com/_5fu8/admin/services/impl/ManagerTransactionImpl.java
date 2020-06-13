package com._5fu8.admin.services.impl;

import com._5fu8.admin.lucene.util.AbstractLuceneIndex;
import com._5fu8.admin.dao.impl.AbstractHibernate;
import com._5fu8.admin.lucene.NoteDocument;
import com._5fu8.admin.lucene.NotesLucene;
import com._5fu8.admin.services.ManagerTransaction;
import com._5fu8.admin.table.ChildType;
import com._5fu8.admin.table.MainType;
import com._5fu8.admin.table.Notes;
import com._5fu8.admin.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Service
@Transactional
public class ManagerTransactionImpl extends AbstractHibernate implements ManagerTransaction {

    @Override
    public Class getEntityClass() {
        return Notes.class;
    }

    private final String ORDER_TIME = " ORDER BY n.firstTime desc";

    @Autowired
    NotesLucene nl;

    @Override
    public MainType addMainType(String typeName,String description ,Integer userID){
        MainType mainType = new MainType(typeName,description,userID);
        addDataByClass(mainType);
        return mainType;
    }

    @Override
    public List<?> getMainType(){
        String hql = "SELECT new MainType(m.id, m.typename) FROM MainType m";
        return getQueryByHQL(hql,null,null).list();
    }

    @Override
    public List<?> getChildTypeByMID(Integer mid){
        String hql = "SELECT new ChildType(c.id, c.typename) FROM ChildType c"
                +" WHERE c.mainType = :mainType";
        MainType mainType = new MainType(mid,null);
        return getQueryByHQL(hql)
                .setParameter("mainType", mainType)
                .list();
    }

    @Override
    public Object getNotesMap(Integer curPage,Integer pageSize,Integer uid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes  n WHERE n.status = 0 AND " +
                " n.user.id = "+uid;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0 AND " +
                " n.user.id = "+uid+ORDER_TIME;
        map.put("notes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getNotesByTerms(Integer curPage,Integer pageSize,Integer uid
            ,String terms,Long startTime,Long endTime,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes n WHERE n.status = 0 AND " +
                " n.user.id = "+uid+terms;
        if(startTime != null && endTime != null && wd != null){
            setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        if(startTime !=null && endTime !=null && wd == null){
            setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("startTime",new Date(startTime))
                    .setParameter("endTime",new Date(endTime))
                    .list());
            return map;
        }

        if(startTime == null && endTime == null && wd != null){
            setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                .uniqueResult());
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0 AND " +
                " n.user.id = "+uid+ terms+ORDER_TIME;
        map.put("notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }


    @Override
    public Object getDelNotes(Integer curPage,Integer pageSize,Integer uid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 1 " +
                "AND n.user.id = "+uid;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.firstTime FROM Notes n " +
                "WHERE n.status = 1"+" AND n.user.id = "+uid+ORDER_TIME;
        map.put("delNotes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getDelNotesSearch(Integer curPage,Integer pageSize,Integer uid
                ,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 1 " +
                "AND n.user.id = "+uid;
        if(wd != null && !wd.isEmpty()){
            hql = hql + " AND n.title like :wd";
            setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                        .setParameter("wd",wd)
                        .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.firstTime FROM Notes n " +
                    "WHERE n.status = 1"+" AND n.user.id = "+uid
                    +" AND n.title like :wd "+ORDER_TIME;
            map.put("delNotes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.firstTime FROM Notes n " +
                "WHERE n.status = 1"+" AND n.user.id = "+uid+ORDER_TIME;
        map.put("delNotes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleChildType(Integer curPage,Integer pageSize,Integer id){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  ChildType c WHERE c.user.id = "+id;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c WHERE c.user.id = "+id;
        map.put("childTypes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSearchChildType(Integer curPage,Integer pageSize,String wd
            ,Integer uid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  ChildType c WHERE c.user.id = "+uid;
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql +" AND c.typename like :wd";
            setReturnMap(curPage,pageSize,map,getQueryByHQL(hql)
                        .setParameter("wd",wd)
                        .uniqueResult());
            hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                    +" FROM ChildType c WHERE c.user.id = "+uid
                    + " AND c.typename like :wd"+" ORDER BY c.mainType.id desc";
            map.put("childTypes",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }

        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c WHERE c.user.id = "+uid+" ORDER BY c.mainType.id desc";
        map.put("childTypes",getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize,String wd
            ,Integer uid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  PicInfo p WHERE " +
                "p.notes.user.id = "+uid+" AND p.notes.status=0";
        if(wd != null && !wd.isEmpty()){
            hql = hql +" AND p.notes.title like :wd";
            setReturnMap(curPage,pageSize,map,getQueryByHQL(hql)
                    .setParameter("wd",wd)
                    .uniqueResult());
            hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id," +
                    "p.notes.title"
                    +" FROM PicInfo p WHERE p.notes.user.id = "+uid
                    + " AND p.notes.title like :wd AND p.notes.status=0 ORDER BY p.notes desc";
            map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id"
                +",p.notes.title FROM PicInfo p WHERE p.notes.user.id = "+uid
                +" AND p.notes.status=0 ORDER BY p.notes desc";
        map.put("pics",getQueryByHQL(hql,curPage,pageSize)
                .list());

        return map;
    }

    @Override
    public Object getChildTypeByID(Integer cid,Integer uid){
        String hql = "SELECT c.id,c.typename,c.description FROM ChildType c " +
                "WHERE c.id =:cid AND c.user.id =:uid";
        return getQueryByHQL(hql)
                .setParameter("cid",cid)
                .setParameter("uid",uid)
                .uniqueResult();
    }

    @Override
    public Object updateChildType(Integer cid,String title,String desc,Integer uid){
        ChildType childType = getPojoByID(cid,ChildType.class);
        if(childType == null || childType.getUser().getId() != uid) return false;
        if(isExitChildTypeName(title,childType.getMainType().getId())){
            return false;
        }
        if(title.equals(childType.getTypename()) && desc.equals(childType.getDescription()))
            return true;
        childType.setTypename(title);
        childType.setDescription(desc);
        updateDataByClass(childType);
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
    public Object updateMainType(Integer mid,String title,String desc,Integer uid){
        if(isExitMainTypeName(title)) return false;
        MainType mainType = getPojoByID(mid,MainType.class);
        if(mainType == null || mainType.getUser().getId() != uid) return false;
        if(title.equals(mainType.getTypename()) && desc.equals(mainType.getDescription())) {
            return true;
        }
        mainType.setTypename(title);
        mainType.setDescription(desc);
        updateDataByClass(mainType);
        return true;
    }

    @Override
    public Object getMainTypeByCID(Integer cid,Integer uid){
        String hql = "SELECT c.mainType.id,c.mainType.typename,c.mainType.description" +
                " FROM ChildType c " +
                "WHERE c.id =:cid AND c.user.id =:uid";
        return getQueryByHQL(hql)
                .setParameter("cid",cid)
                .setParameter("uid",uid)
                .uniqueResult();
    }



    @Override
    public Object getIKLuceneCi(String text ){
        return  AbstractLuceneIndex.getIKLuceneCi(text);
    }


    @Override
    public Object getNoteByID(Integer id){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE  n.status = 0 AND n.id = "+id;
        return getQueryByHQL(hql).list();
    }

    @Override
    public Object getNoteByStaticID(Integer id,Integer uid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE n.id = "+id+" AND n.user.id = " + uid
                +" AND n.status < 2";
        return getQueryByHQL(hql).list();
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
        addDataByClass(childType);
        return childType;
    }

    @Override
    public  Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID,String images){
        Notes notes = getPojoByID(nid,Notes.class);

        if(notes == null || notes.getStatus() != 0)
            throw  new RuntimeException("The Note is not exit!");

        if(notes.getUser().getId() != userID )
            throw  new RuntimeException("The Note is not exit!");

        notes.setTitle(title);notes.setShare(share);
        notes.setChildType(new ChildType(chid,null));
        notes.setDescription(description);
        notes.setUser(new User(userID));
        notes.setCheck(0);
        updateDataByClass(notes);

        //删除原来笔记的图片，只在数据库删除
        String hql = "DELETE FROM PicInfo p WHERE p.notes.id = :id";
        getQueryByHQL(hql)
                .setParameter("id",notes.getId())
                .executeUpdate();
        //更新笔记在数据库中的图片名
        AbstractServices.insertImages(images, notes,this);

        NoteDocument noteDocument = new NoteDocument(notes.getId(),title,simpleDesc
                ,userID,chid,mid,0,0,share);
        noteDocument.setFirstTime(notes.getFirstTime());

        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return notes;
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
    public Object getNotesNoCheck(Integer curPage,Integer pageSize,Integer uid){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0 " +
                " AND n.check = 0 AND n.user.id = "+uid;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.user.id " +
                ",n.user.username,n.firstTime,n.childType.typename FROM Notes n " +
                "WHERE n.status = 0"+" AND n.check = 0 AND n.user.id = "+uid+ORDER_TIME;
        map.put("Notes",getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }


    @Override
    public Object getNoteToUpdate(Integer id,Integer uid){
        String hql = "SELECT n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id" +
                ",n.childType.mainType.id,n.share"
                +" FROM Notes n WHERE n.status = 0 AND n.id = "+id
                +" AND n.user.id = "+ uid;
        return getUniqueResultByHQL(hql);
    }


    @Override
    public Object changeCheck(Integer id,Integer check,Integer uid){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
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
    public Object changeShare(Integer id,Integer share,Integer uid){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
        if(notes == null) return false;
        if(notes.getShare() == share) return false;
        notes.setShare(share);
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setShare(share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeStatus(Integer id,Integer status,Integer uid){
        Notes notes = getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
        if(notes == null) return false;
        if(notes.getStatus() == status) return false;
        notes.setStatus(status);
        updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setStatus(status);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
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

    private void setReturnMap(Integer curPage,Integer pageSize,
    TreeMap<String,Object> map,Object count){
        map.put("count",count);
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
