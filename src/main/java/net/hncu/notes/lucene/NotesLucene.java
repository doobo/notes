package net.hncu.notes.lucene;

import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.*;

/**
 * Created by doobo@foxmail.com on 2017/4/4.
 */
@Component
public class NotesLucene {

    public Object getLuceneNoteByTime(Integer curPage,Integer pageSize){
        Sort sort=new Sort(new SortField("time", SortField.Type.LONG
                ,true));//true为降序排列
        BooleanQuery query = new BooleanQuery();
        sameQueryOption(query);
        return changMap(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize,sort));
    }

    public Object getLuceneNote(Integer curPage,Integer pageSize){
        Sort sort=new Sort(new SortField("time", SortField.Type.LONG
                ,true));//true为降序排列
        BooleanQuery query = new BooleanQuery();
        sameQueryOption(query);
        return changMap2(AbstractLuceneIndex.doSearch(
                query,curPage,pageSize,sort));
    }

    public NoteDocument getNoteDocumentByTerm(Integer id){
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

    private void sameQueryOption(BooleanQuery query) {
        Query q1 = NumericRangeQuery.newLongRange("time", 1L,
                new Date().getTime(), false,true);
        Query q2 = new TermQuery(new Term("share", "0"));
        Query q3 = new TermQuery(new Term("status", "0"));
        query.add(q1, BooleanClause.Occur.MUST);
        query.add(q2, BooleanClause.Occur.MUST);
        query.add(q3, BooleanClause.Occur.MUST);
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
                if(str != null && str.length() > 100)
                    str = str.substring(0,100);
                vector.addElement(str);
                list.add(vector);
            }
            map.put("notes", list);
        }
        map.remove("doc");
        return map;
    }
}
