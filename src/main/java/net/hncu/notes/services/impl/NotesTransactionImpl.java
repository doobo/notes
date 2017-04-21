package net.hncu.notes.services.impl;

import net.hncu.notes.dao.impl.AbstractHibernate;
import net.hncu.notes.lucene.NoteDocument;
import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.table.ChildType;
import net.hncu.notes.table.MainType;
import net.hncu.notes.table.Notes;
import net.hncu.notes.table.User;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Service
public class NotesTransactionImpl implements NotesTransaction {

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
        return hb.getQueryByHQL(hql,null,null)
                .setEntity("mainType", mainType)
                .list();
    }

    @Override
    public List<?> getSimpleNotes(Integer curPage,Integer pageSize
            ,TreeMap<String,Object> map){
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 0";
        Object count = hb.getUniqueResultByHQL(hql);
        map.put("count",count);
        map.put("pageCount",getPageCount(pageSize,Integer.parseInt(count.toString())));
        map.put("curPage",curPage);
        map.put("pageSize",pageSize);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.check,n.share,n.firstTime FROM Notes n " +
                "WHERE n.status = 0"+ORDER_TIME;
        return hb.getQueryByHQL(hql,curPage,pageSize).list();
    }

    @Override
    public Object getDelNotes(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM Notes  n WHERE n.status = 1";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT n.id, n.title,n.childType.typename " +
                ",n.user.username,n.firstTime FROM Notes n " +
                "WHERE n.status = 1"+ORDER_TIME;
        map.put("delNotes",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleChildType(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM ChildType";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT c.id,c.typename,c.user.username,c.mainType.typename,c.firstTime"
                +" FROM ChildType c";
        map.put("childTypes",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }


    @Override
    public Object getSimpleUser(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 0 AND u.type = 0";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 0 AND u.type = 0";
        map.put("users",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getSimpleKingMaster(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 0 AND u.type = 1";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT  u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 0 AND u.type = 1";
        map.put("kingMaster",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
    }

    @Override
    public Object getDelUser(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = 1 AND u.type = 0";
        setReturnMap(curPage, pageSize, map, hql);
        hql = "SELECT  u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = 1 AND u.type = 0";
        map.put("delUser",hb.getQueryByHQL(hql,curPage,pageSize).list());
        return map;
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
        return hb.getQueryByHQL(hql,null,null).list();
    }

    @Override
    public Object getNoteByStaticID(Integer id){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id "
                +" FROM Notes n WHERE n.id = "+id;
        return hb.getQueryByHQL(hql,null,null).list();
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
    public Notes addNote(String title,String description ,Integer mid,
                        String simpleDesc,Integer chid,Integer share,Integer userID){
        Notes notes = new Notes(title,chid,userID,description,share);
        hb.addDataByClass(notes);
        NoteDocument noteDocument = new NoteDocument(notes.getId(),title,simpleDesc
        ,userID,chid,mid,0,0,share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return notes;
    }

    @Override
    public  Notes updateNote(String title, String description , Integer mid,
                     String simpleDesc, Integer chid, Integer
                             share,Integer nid, Integer userID){
        System.out.println(nid);
        Notes notes = hb.getPojoByID(nid,Notes.class);

        if(notes == null) return null;

        notes.setTitle(title);notes.setShare(share);
        notes.setChildType(new ChildType(chid,null));
        notes.setDescription(description);
        notes.setUser(new User(userID));
        notes.setCheck(0);
        hb.updateDataByClass(notes);

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
        map.put("Notes",hb.getQueryByHQL(hql,curPage,pageSize)
                .setInteger("cid",cid)
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
        map.put("Notes",hb.getQueryByHQL(hql,curPage,pageSize)
                .list());
        return map;
    }


    @Override
    public Object getNoteToUpdate(Integer id){
        String hql = "SELECT  n.title,n.user.username,n.firstTime," +
                "n.childType.typename,n.description,n.id,n.childType.id" +
                ",n.childType.mainType.id,n.share"
                +" FROM Notes n WHERE  n.status = 0 AND n.id = "+id;
        return hb.getUniqueResultByHQL(hql);
    }


    @Override
    public Object changeCheck(Integer id,Integer check){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setCheck(check);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByTerm(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setCheck(check);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeShare(Integer id,Integer share){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setShare(share);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByTerm(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setShare(share);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeStatus(Integer id,Integer status){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setStatus(status);
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByTerm(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setStatus(status);
        AbstractLuceneIndex.updateDocumentByTerm(noteDocument,"id");
        return true;
    }

    @Override
    public Object changeChildType(Integer id,Integer cid){
        Notes notes = hb.getPojoByID(id,Notes.class);
        if(notes == null) return false;
        notes.setChildType(new ChildType(cid,null));
        hb.updateDataByClass(notes);

        NoteDocument noteDocument = nl.getNoteDocumentByTerm(id);
        if(noteDocument == null) throw new RuntimeException("未更新缓存");
        noteDocument.setChid(cid);
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


    @Override
    public <T> boolean testUnitHb() {
        User user ;
//        for (int i = 0; i < 100; i++) {
//            user = new User("us_"+i,"管理员",
//                    "pw_"+i,0,0,new Date());
//            hb.addDataByClass(user);
//        }

          //单个添加
//        user = new User("us_"+103,"管理员",
//                    "pw_"+103,0,0,new Date());
//        hb.addDataByClass(user);

        if(false)//变成true测试事物处理
        throw new RuntimeException("Hello");

        org.hibernate.Criteria criteria = hb.getSession().createCriteria(User.class);
        System.out.println(hb.getCountByCritieria(criteria));

        criteria = hb.getSession().createCriteria(User.class);
        criteria.addOrder(Order.desc("signTime"));
        criteria.addOrder(Order.asc("id"));

        List list = hb.getPageByCriteria(1,5,criteria);
        for (Object obj:
             list) {
            System.out.println(obj);
        }

        return true;
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
