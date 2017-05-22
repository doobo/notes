package net.hncu.notes.services.impl;

import net.hncu.notes.dao.impl.AbstractHibernate;
import net.hncu.notes.lucene.NoteDocument;
import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import net.hncu.notes.services.ManagerTransaction;
import net.hncu.notes.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Service
public class ManagerTransactionImpl implements ManagerTransaction {

    private final String ORDER_TIME = " ORDER BY n.firstTime desc";

    @Autowired
    AbstractHibernate hb;

    @Autowired
    NotesLucene nl;

    @Override
    public MainType addMainType(String typeName,String description ,Integer userID){
        MainType mainType = new MainType(typeName,description,userID);
        hb.addDataByClass(mainType);
        return mainType;
    }

    @Override
    public List<?> getMainType(){
        String hql = "SELECT new MainType(m.id, m.typename) FROM MainType m";
        return hb.getQueryByHQL(hql,null,null).list();
    }

    @Override
    public List<?> getChildTypeByMID(Integer mid){
        String hql = "SELECT new ChildType(c.id, c.typename) FROM ChildType c"
                +" WHERE c.mainType = :mainType";
        MainType mainType = new MainType(mid,null);
        return hb.getQueryByHQL(hql)
                .setEntity("mainType", mainType)
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
        map.put("notes",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getNotesByTerms(Integer curPage,Integer pageSize,Integer uid
            ,String terms,Long startTime,Long endTime,String wd){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*) FROM Notes n WHERE n.status = 0 AND " +
                " n.user.id = "+uid+terms;
        if(startTime != null && endTime != null && wd != null){
            setReturnMap(curPage, pageSize, map, hb.getQueryByHQL(hql)
                    .setTimestamp("startTime",new Date(startTime))
                    .setTimestamp("endTime",new Date(endTime))
                    .setString("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setTimestamp("startTime",new Date(startTime))
                    .setTimestamp("endTime",new Date(endTime))
                    .setString("wd",wd)
                    .list());
            return map;
        }

        if(startTime !=null && endTime !=null && wd == null){
            setReturnMap(curPage, pageSize, map, hb.getQueryByHQL(hql)
                    .setTimestamp("startTime",new Date(startTime))
                    .setTimestamp("endTime",new Date(endTime))
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setTimestamp("startTime",new Date(startTime))
                    .setTimestamp("endTime",new Date(endTime))
                    .list());
            return map;
        }

        if(startTime == null && endTime == null && wd != null){
            setReturnMap(curPage, pageSize, map, hb.getQueryByHQL(hql)
                    .setString("wd",wd)
                    .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                    "WHERE n.status = 0 AND " +
                    " n.user.id = "+uid+ terms+ORDER_TIME;
            map.put("notes",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setString("wd",wd)
                    .list());
            return map;
        }

        setReturnMap(curPage, pageSize, map, hb.getQueryByHQL(hql)
                .uniqueResult());
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0 AND " +
                " n.user.id = "+uid+ terms+ORDER_TIME;
        map.put("notes",hb.getQueryByHQL(hql,curPage,pageSize)
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
        map.put("delNotes",hb.getQueryByHQL(hql,curPage,pageSize).list());
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
            setReturnMap(curPage, pageSize, map, hb.getQueryByHQL(hql)
                        .setString("wd",wd)
                        .uniqueResult());
            hql = "SELECT n.id, n.title,n.childType.typename " +
                    ",n.user.username,n.firstTime FROM Notes n " +
                    "WHERE n.status = 1"+" AND n.user.id = "+uid
                    +" AND n.title like :wd "+ORDER_TIME;
            map.put("delNotes",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setString("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.firstTime FROM Notes n " +
                "WHERE n.status = 1"+" AND n.user.id = "+uid+ORDER_TIME;
        map.put("delNotes",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleChildType(Integer curPage,Integer pageSize,Integer id){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM  ChildType c WHERE c.user.id = "+id;
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c WHERE c.user.id = "+id;
        map.put("childTypes",hb.getQueryByHQL(hql,curPage,pageSize).list());
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
            setReturnMap(curPage,pageSize,map,hb.getQueryByHQL(hql)
                        .setString("wd",wd)
                        .uniqueResult());
            hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                    +" FROM ChildType c WHERE c.user.id = "+uid
                    + " AND c.typename like :wd"+" ORDER BY c.mainType.id desc,c.firstTime desc";
            map.put("childTypes",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setString("wd",wd)
                    .list());
            return map;
        }

        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c WHERE c.user.id = "+uid+" ORDER BY c.mainType.id desc,c.firstTime desc";
        map.put("childTypes",hb.getQueryByHQL(hql,curPage,pageSize).list());
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
            setReturnMap(curPage,pageSize,map,hb.getQueryByHQL(hql)
                    .setString("wd",wd)
                    .uniqueResult());
            hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id," +
                    "p.notes.title"
                    +" FROM PicInfo p WHERE p.notes.user.id = "+uid
                    + " AND p.notes.title like :wd AND p.notes.status=0 ORDER BY p.notes desc";
            map.put("pics",hb.getQueryByHQL(hql,curPage,pageSize)
                    .setString("wd",wd)
                    .list());
            return map;
        }
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT p.id,p.picPath,p.width,p.height,p.firstTime,p.notes.id"
                +",p.notes.title FROM PicInfo p WHERE p.notes.user.id = "+uid
                +" AND p.notes.status=0 ORDER BY p.notes desc";
        map.put("pics",hb.getQueryByHQL(hql,curPage,pageSize)
                .list());

        return map;
    }

    @Override
    public Object getChildTypeByID(Integer cid,Integer uid){
        String hql = "SELECT c.id,c.typename,c.description FROM ChildType c " +
                "WHERE c.id =:cid AND c.user.id =:uid";
        return hb.getQueryByHQL(hql)
                .setInteger("cid",cid)
                .setInteger("uid",uid)
                .uniqueResult();
    }

    @Override
    public Object updateChildType(Integer cid,String title,String desc,Integer uid){
        ChildType childType = hb.getPojoByID(cid,ChildType.class);
        if(childType == null || childType.getUser().getId() != uid) return false;
        if(isExitChildTypeName(title,childType.getMainType().getId())){
            return false;
        }
        if(title.equals(childType.getTypename()) && desc.equals(childType.getDescription()))
            return true;
        childType.setTypename(title);
        childType.setDescription(desc);
        hb.updateDataByClass(childType);
        return true;
    }


    //是否存在与子类别相同的类别名
    public boolean isExitChildTypeName(String typename,Integer mid){
        String hql = "SELECT count(*) FROM ChildType c WHERE c.mainType.id = :mid" +
                " AND c.typename = :typename";
        if(hb.getQueryByHQL(hql)
                .setInteger("mid",mid)
                .setString("typename",typename)
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
        if(hb.getQueryByHQL(hql)
                .setString("typename",typename)
                .uniqueResult()
                .toString().equals("0")){
            return false;
        }
        return true;
    }


    @Override
    public Object updateMainType(Integer mid,String title,String desc,Integer uid){
        if(isExitMainTypeName(title)) return false;
        MainType mainType = hb.getPojoByID(mid,MainType.class);
        if(mainType == null || mainType.getUser().getId() != uid) return false;
        if(title.equals(mainType.getTypename()) && desc.equals(mainType.getDescription())) {
            return true;
        }
        mainType.setTypename(title);
        mainType.setDescription(desc);
        hb.updateDataByClass(mainType);
        return true;
    }

    @Override
    public Object getMainTypeByCID(Integer cid,Integer uid){
        String hql = "SELECT c.mainType.id,c.mainType.typename,c.mainType.description" +
                " FROM ChildType c " +
                "WHERE c.id =:cid AND c.user.id =:uid";
        return hb.getQueryByHQL(hql)
                .setInteger("cid",cid)
                .setInteger("uid",uid)
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
        return hb.getQueryByHQL(hql).list();
    }

    @Override
    public Object getNoteByStaticID(Integer id,Integer uid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE n.id = "+id+" AND n.user.id = " + uid
                +" AND n.status < 2";
        return hb.getQueryByHQL(hql).list();
    }

    @Override
    public Object getNextNoteByTime(Long time,Integer nid,Integer cid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE  n.status = 0 AND n.firstTime > :time" +
                " AND n.childType.id = "+cid
                +" ORDER BY n.firstTime";

        return hb.getQueryByHQL(hql,1,1)
                .setTimestamp("time",new Date(time)).list();
    }


    @Override
    public Object getPreNoteByTime(Long time,Integer nid,Integer cid){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE  n.status = 0 AND n.firstTime < :time" +
                " AND n.childType.id = "+cid
                +ORDER_TIME;
        return hb.getQueryByHQL(hql,1,1)
                .setTimestamp("time",new Date(time)).list();
    }

    @Override
    public ChildType addChildType(String typename, String description
            , Integer mainID, Integer userID){
        ChildType childType = new ChildType(typename,description, mainID,userID);
        hb.addDataByClass(childType);
        return childType;
    }

    @Override
    public  Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID,String images){
        Notes notes = hb.getPojoByID(nid,Notes.class);

        if(notes == null || notes.getStatus() != 0)
            throw  new RuntimeException("The Note is not exit!");

        if(notes.getUser().getId() != userID )
            throw  new RuntimeException("The Note is not exit!");

        notes.setTitle(title);notes.setShare(share);
        notes.setChildType(new ChildType(chid,null));
        notes.setDescription(description);
        notes.setUser(new User(userID));
        notes.setCheck(0);
        hb.updateDataByClass(notes);

        //删除原来笔记的图片，只在数据库删除
        String hql = "DELETE FROM PicInfo p WHERE p.notes.id = :id";
        hb.getQueryByHQL(hql)
                .setInteger("id",notes.getId())
                .executeUpdate();
        //更新笔记在数据库中的图片名

        NoteDocument noteDocument = new NoteDocument(notes.getId(),title,simpleDesc
                ,userID,chid,mid,0,0,share);
        noteDocument.setFirstTime(notes.getFirstTime());
        AbstractServices.insertImages(images, notes,hb,noteDocument);

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
        map.put("Notes",hb.getQueryByHQL(hql,curPage,pageSize)
                .setInteger("cid",cid)
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
        map.put("Notes",hb.getQueryByHQL(hql,curPage,pageSize)
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
        return hb.getUniqueResultByHQL(hql);
    }


    @Override
    public Object changeCheck(Integer id,Integer check,Integer uid){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
        if(notes == null) return false;
        notes.setCheck(check);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setCheck(check);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeShare(Integer id,Integer share,Integer uid){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
        if(notes == null) return false;
        if(notes.getShare() == share) return false;
        notes.setShare(share);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setShare(share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeStatus(Integer id,Integer status,Integer uid){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes.getUser().getId() != uid){
            return false;
        }
        if(notes == null) return false;
        if(notes.getStatus() == status) return false;
        notes.setStatus(status);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByID(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setStatus(status);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    //设置返回值的统一名称
    private void setReturnMap(Integer curPage, Integer pageSize,
                              TreeMap<String, Object> map, String hql) {
        map.put("count",hb.getUniqueResultByHQL(hql));
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
