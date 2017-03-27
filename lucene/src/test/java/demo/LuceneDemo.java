package demo;

import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import net.hncu.notes.lucene.util.LuceneAnnotation;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/9.
 */
public class LuceneDemo {

    @Test
    public void testSeachLuceneIndex() throws Exception {
        Query q1 = new TermQuery(new Term("password", "世界"));
        for (int i = 1; i < 25; i++) {
            AbstractLuceneIndex.doSearch(q1,i,5);
            System.out.println("=========================================");

        }
        for (int i = 0; i < 12; i++) {
            AbstractLuceneIndex.doSearch(q1,i,10);
            System.out.println("=========================================");
        }
    }

    public <T> void sizeof() throws Exception{
        long beforeMemory=Runtime.getRuntime().totalMemory();
        long afterMemory=Runtime.getRuntime().totalMemory();
        System.out.println("Memory used:"+(beforeMemory-afterMemory));
    }


    @Test
    public void testLuceneIndex(){

        User user;
        ArrayList list = new ArrayList();
        for (int i = 1; i <= 1; i++) {
            user = new User(i,"unit and book",
                    "管理","世界 友好 为什么 Hello World!",0,0);
            list.add(user);
        }
        AbstractLuceneIndex.addDocument(list,AbstractLuceneIndex.getIndexWriter());

        Query q1 = new TermQuery(new Term("password", "友好"));
        AbstractLuceneIndex.doSearch(q1,1,6);
        System.out.println(AbstractLuceneIndex.getIndexPath());
    }

    @Test
    public void addIndexLucene(){

        Field field = new StringField("name", "abc", Field.Store.NO);

        LuceneAnnotation.FieldEnum longField = LuceneAnnotation.FieldEnum.LongField;
    }


    /**
     * 添加索引文件
     * @throws Exception
     */
    @Test
    public void testRAMLucene() throws Exception{
        String[] strs = {"java apache ok hello alkf sddj alsd fj",
                "work base apache woie jro ifajf la fk ldd ajd lkasd"
                ,"kdl good apache lafj good hello virtual iwj irj ka dfd lk"};
        Document doc = new Document();
        Document doc1 = new Document();
        Document doc2 = new Document();


        Field one = new TextField("one",strs[0], Field.Store.YES);
        Field two = new TextField("one",strs[1], Field.Store.YES);
        Field three = new TextField("one",strs[2], Field.Store.YES);

        doc.add(one);doc1.add(two);doc2.add(three);

        //标准分词适配器
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_0,analyzer);
        File indexFile = new File("D:/temp/data/");
        Directory directory = FSDirectory.open(indexFile);
        IndexWriter writer = new IndexWriter(directory,cfg);
        writer.addDocument(doc);
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.close();
    }

    @Test
    public void searchDocument() throws Exception {
        QueryParser parser = new QueryParser("one",new StandardAnalyzer());
        Query query = parser.parse("base OR apache OR ok OR hello");

        File indexFile = new File("D:/temp/data/");
        Directory directory = FSDirectory.open(indexFile);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs topDocs = searcher.search(query,10);

        int count = topDocs.totalHits;

        System.out.println(count);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs){
            int docId = scoreDoc.doc;

            Document doc = searcher.doc(docId);

            System.out.println("搜索结果:"+doc.get("one"));
        }
        reader.close();

    }
}
