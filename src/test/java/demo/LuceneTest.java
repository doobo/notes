package demo;

import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by doobo@foxmail.com on 2017/4/7.
 */
public class LuceneTest {

    @Test
    public void testSearch(){
       NotesLucene nt = new NotesLucene();
       nt.getNoteDocumentByTerm(2);
    }


    @Test
    public void testLucene(){
        Ur ur = new Ur(12,"Hello World 可是看到老师");
        AbstractLuceneIndex.updateDocumentByTerm(ur,"urid");
        AbstractLuceneIndex.clearCache();




        org.apache.lucene.search.Query q2 = new TermQuery(
                new Term("title2", "hello"));
        System.out.println(changMap(
                AbstractLuceneIndex.doSearch(q2,1,10)));



        ur = new Ur(13,"Hello Wordsdf 看看的开发商的认可");
        AbstractLuceneIndex.updateDocumentByTerm(ur,"urid");
        AbstractLuceneIndex.clearCache();



        System.out.println(changMap(
                AbstractLuceneIndex.doSearch(q2,1,10)));
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
                vector.addElement(doc.get("urid"));
                vector.addElement(doc.get("title2"));
                list.add(vector);
            }
            map.put("notes", list);
        }
        map.remove("doc");
        return map;
    }
}
