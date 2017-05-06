package net.hncu.notes.lucene;

import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.*;

/**
 * Created by doobo@foxmail.com on 2017/4/4.
 */
@Component
public class NotesLucene {
    /**
     *按照时间顺序获取Note
     * @param curPage
     * @param pageSize
     * @return
     */
    public Object getLuceneNoteByTime(Integer curPage,Integer pageSize){
        Sort sort=new Sort(new SortField("time", SortField.Type.LONG
                ,true));//true为降序排列
        BooleanQuery query = new BooleanQuery();
        sameQueryOption(query);
        return changMap(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize,sort));
    }

    //搜索管理员发布的已分享的信息
    public Object getRootLuceneNoteByTime(Integer curPage,Integer pageSize,Integer rid){
        Sort sort=new Sort(new SortField("time", SortField.Type.LONG
                ,true));//true为降序排列
        BooleanQuery query = new BooleanQuery();
        setRootNotesQuery(query,rid);
        return changMap(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize,sort));
    }

    //按照时间顺序获取笔记,包含内容介绍
    public Object getLuceneNote(Integer curPage,Integer pageSize){
        Sort sort=new Sort(new SortField("time", SortField.Type.LONG
                ,true));//true为降序排列
        BooleanQuery query = new BooleanQuery();
        sameQueryOption(query);
        return changMap2(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize,sort));
    }

    //模糊搜索标题
    public Object getTitleSearchResult(Integer curPage,Integer pageSize,String title){
        BooleanQuery query = new BooleanQuery();
        setTitleSameQueryOption(query,title);
        return changMap2(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize));

    }

    //模糊搜索标题和内容
    public Object getTitleOrDescResult(Integer curPage,Integer pageSize,String wd){
        BooleanQuery query = new BooleanQuery();
        setTitleAndDescBooleanQuery(query,wd);
        return changMap2(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize));

    }

    public NoteDocument getNoteDocumentByID(Integer id){
        Term term = new Term("id",id.toString());
        return  documentToNoteDocument(AbstractLuceneIndex.doSearch(term));
    }
    /**
     * 把Document封装成NoteDocument
     * @param doc
     * @return
     */
    private NoteDocument documentToNoteDocument(Document doc){
        if(doc == null) return null;
        try {
            NoteDocument notes = new NoteDocument();
            notes.setId(Integer.parseInt(doc.get("id")));
            notes.setCheck(Integer.parseInt(doc.get("check")));
            notes.setShare(Integer.parseInt(doc.get("share")));
            notes.setStatus(Integer.parseInt(doc.get("status")));
            notes.setChid(Integer.parseInt(doc.get("chid")));
            notes.setMaid(Integer.parseInt(doc.get("maid")));
            notes.setUid(Integer.parseInt(doc.get("uid")));

            notes.setFirstTime(new Date(Long.parseLong(doc.get("time"))));

            notes.setTitle(doc.get("title"));
            notes.setDescription(doc.get("description"));
            return notes;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //不同模糊搜索
    private final Query shareQ = new TermQuery(new Term("share", "0"));
    private final Query statusQ = new TermQuery(new Term("status", "0"));
    private final Query checkQ = new TermQuery(new Term("check", "1"));
    private void sameQueryOption(BooleanQuery query) {
        query.add(shareQ, BooleanClause.Occur.MUST);
        query.add(statusQ, BooleanClause.Occur.MUST);
        query.add(checkQ, BooleanClause.Occur.MUST);
        Query q1 = NumericRangeQuery.newLongRange("time", 1L,
                new Date().getTime(), false,true);
        query.add(q1, BooleanClause.Occur.MUST);
    }

    private void setRootNotesQuery(BooleanQuery query,Integer rid){
        query.add(shareQ, BooleanClause.Occur.MUST);
        query.add(statusQ, BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("uid", rid.toString()))
                , BooleanClause.Occur.MUST);
    }

    //设置模糊搜索的符合标题条件的Query
    private void setTitleSameQueryOption(BooleanQuery query,String title){
        query.add(shareQ
                , BooleanClause.Occur.MUST);
        query.add(statusQ
                , BooleanClause.Occur.MUST);
        query.add(checkQ
                , BooleanClause.Occur.MUST);
        QueryParser parser = new QueryParser("title",
                AbstractLuceneIndex.getAnalyzer());
        try {
            query.add(parser.parse(QueryParser.escape(title))
                    , BooleanClause.Occur.MUST);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    //设置模糊搜索的符合标题和内容条件的Query
    private void setTitleAndDescBooleanQuery(BooleanQuery query,String wd){
        query.add(shareQ
                , BooleanClause.Occur.MUST);
        query.add(statusQ
                , BooleanClause.Occur.MUST);
        query.add(checkQ
                , BooleanClause.Occur.MUST);
        String[] fields = { "title", "description" };
        Map<String, Float> boosts = new HashMap<String, Float>();
        boosts.put("title", 10f);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
                AbstractLuceneIndex.getAnalyzer(), boosts);
        try {
            query.add(parser.parse(QueryParser.escape(wd))
                    , BooleanClause.Occur.MUST);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 解析document
     * @param map
     * @return
     */
    private Object changMap(TreeMap<String, Object> map) {
        if (map.get("doc") != null) {
            List<Vector<String>> list = new ArrayList();
            for (Document doc
                    : (ArrayList<Document>) map.get("doc")) {
                Vector<String> vector = new Vector<>();
                vector.addElement(doc.get("id"));
                vector.addElement(doc.get("title"));
                vector.addElement(doc.get("time"));
                list.add(vector);
            }
            map.put("notes", list);
        }
        map.remove("doc");
        return map;
    }


    /**
     * 解析document
     * @param map
     * @return
     */
    private Object changMap2(TreeMap<String, Object> map) {
        if (map.get("doc") != null) {
            List<Vector<String>> list = new ArrayList();
            for (Document doc
                    : (ArrayList<Document>) map.get("doc")) {
                Vector<String> vector = new Vector<>();
                vector.addElement(doc.get("id"));
                vector.addElement(doc.get("title"));
                vector.addElement(doc.get("time"));
                String str = doc.get("description");
                if(str != null && str.length() >= 130)
                    str = str.substring(0,130)+"...";
                vector.addElement(str);
                list.add(vector);
            }
            map.put("notes", list);
        }
        map.remove("doc");
        return map;
    }
}
