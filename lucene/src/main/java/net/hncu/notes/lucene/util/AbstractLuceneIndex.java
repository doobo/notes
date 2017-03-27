package net.hncu.notes.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by doobo@foxmail.com on 2017/3/21.
 */
public abstract class AbstractLuceneIndex implements LuceneIndexable {

    /* 增、删、改 */
    private static IndexWriter indexWriter;

    /*查询入口*/
    private static IndexReader indexReader;

    /* 执行查询 */
    private static IndexSearcher indexSearcher;

    /*分词器*/
    private static Analyzer analyzer;

    /*目录*/
    private static Directory directory;

    /*统一Document，节约内存*/
    private static Document doc;

    /*统一Lucene_Field，节约内存*/
    private static org.apache.lucene.document.Field luceneField;

    /*统一reflect_Field[]，节约内存*/
    private static Field[] fields;

    private static Vector<LuceneField> luceneFields = new Vector<LuceneField>();

    static {
        try {
            // 创建保存目录、分词器
            directory = FSDirectory.open(new File(getIndexPath()));
            analyzer = new IKAnalyzer();
            //监听线程，当JVM结束时，就执行此线程。释放资源
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    closeIndexWrite();
                    closeIndexReader();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IndexWriter getIndexWriter() {
        if (indexWriter == null) {
            try {
                IndexWriterConfig indexCpnfig = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
                indexWriter = new IndexWriter(directory, indexCpnfig);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return indexWriter;
    }

    /*关闭indexWrite*/
    public static void closeIndexWrite() {
        if (indexWriter != null) {
            System.out.println("关闭indexWriter对象");
            try {
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*获取IndexSearcher*/
    public static IndexSearcher getIndexSearcher() {
        if (indexSearcher == null) {
            try {
                indexSearcher = new IndexSearcher(getIndexReader());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return indexSearcher;
    }

    /*获取IndexReader*/
    public static IndexReader getIndexReader() {
        if (indexReader == null) {
            File indexFile = new File(getIndexPath());
            Directory directory = null;
            try {
                directory = FSDirectory.open(indexFile);
                indexReader = DirectoryReader.open(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return indexReader;
    }

    /*关闭IndexReader*/
    public static void closeIndexReader() {
        if (indexReader != null) {
            System.out.println("关闭IndexReader对象");
            try {
                indexReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }

    public static void setAnalyzer(Analyzer analyzer) {
        AbstractLuceneIndex.analyzer = analyzer;
    }

    /* 添加索引
         *   array 添加的索引值
         *   write 操作索引对象
         */
    public static void addDocument(List<Object> array, IndexWriter write) {
        for (Object ins : array) {
            doc = new Document();
            updateDocument(doc, ins, "id");
        }
        try {
            getIndexWriter().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加Document
     *
     * @param doc
     * @param t
     * @param <T>
     */
    public static <T> void writeDataToDocument(Document doc, T t) {
        setDocumentValue(doc, t);
        try {
            getIndexWriter().addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新document
     *
     * @param doc
     * @param t
     * @param termParam
     * @param <T>
     */
    public static <T> void updateDocument(Document doc, T t, String termParam) {
        setDocumentValue(doc, t);
        try {
            getIndexWriter().updateDocument(new Term(termParam, doc.get(termParam)), doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static <T> void setDocumentValue(Document doc, T t) {
        if (isLuceneAnnotation(t.getClass())) {
            LuceneStruct luceneStruct = new LuceneStruct(t.getClass().getName());
            if (luceneStructVector.contains(luceneStruct)) {
                luceneStruct = luceneStructVector.get(luceneStructVector.indexOf(luceneStruct));
            } else {
                luceneStruct = new LuceneStruct(t.getClass());
                luceneStructVector.add(luceneStruct);
            }
            setAnnotationValueToDocument(doc, t, luceneStruct);
        } else {
            setOriginalDataToDocument(doc, t);
        }
    }


    /**
     * 获取注解对应的值并加入doc
     *
     * @param doc
     * @param t
     * @param luceneStruct
     * @param <T>
     */
    private static <T> void setAnnotationValueToDocument(Document doc,
                                                         T t, LuceneStruct luceneStruct) {
        luceneFields = luceneStruct.fieldVector;
        LuceneField lField;
        Enumeration<LuceneField> en = luceneFields.elements();
        Field fd;
        while (en.hasMoreElements()) {
            lField = en.nextElement();
            fd = t.getClass().getDeclaredFields()[lField.id];
            fd.setAccessible(true);
            try {
                lField.addLuceneDocumentFiled(fd.get(t), doc);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        luceneFields = luceneStruct.methodVector;
        en = luceneFields.elements();
        Method md;
        while (en.hasMoreElements()) {
            lField = en.nextElement();
            md = t.getClass().getDeclaredMethods()[lField.id];
            md.setAccessible(true);
            try {
                lField.addLuceneDocumentFiled(md.invoke(t), doc);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接反射获取原始值，并加入doc
     *
     * @param doc
     * @param t
     * @param <T>
     */
    private static <T> void setOriginalDataToDocument(Document doc, T t) {
        fields = t.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
            try {
                // 得到访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                fields[i].setAccessible(true);
                luceneField = new TextField(fields[i].getName(), fields[i].get(t).toString(),
                        org.apache.lucene.document.Field.Store.YES);
                //恢复访问控制权限
                fields[i].setAccessible(accessFlag);
                doc.add(luceneField);
            } catch (Exception e) {
                throw new RuntimeException("获取属性值失败，ExcelControllerImpl.Java");
            }
    }

    /**
     * 判断是否属于LuceneAnnotation
     *
     * @param cls
     * @return
     */
    private static boolean isLuceneAnnotation(Class<?> cls) {
        if (cls.isAnnotationPresent(LuceneAnnotation.class))
            return true;
        return false;
    }

    /**
     * 每次读取磁盘查询，不缓存
     *
     * @param query
     * @param curPage
     * @param pageSize
     * @return
     */
    private static ScoreDoc[] setSearchDocs(Query query, int curPage, int pageSize) {
        int count = 0;
        int pageCount = 0;
        TopScoreDocCollector topCollector = null;
        try {
            topCollector = TopScoreDocCollector.create(MAX_QUERY, false);
            getIndexSearcher().search(query, topCollector);
            count = topCollector.getTotalHits();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (count == 0) {
            return null;
        }

        pageCount = getPageCount(pageSize, count);
        if (curPage > pageCount) {
            curPage = pageCount;
        }
        ScoreDoc[] sdoc = topCollector.topDocs((curPage - 1) * pageSize, pageSize).scoreDocs;
        if (sdoc.length == 0) return null;
        if (afterScoreDoc.size() > MAX_QUERY) afterScoreDoc.clear();
        afterScoreDoc.add(new SearchStruct(curPage, pageSize, pageCount,
                count, query, sdoc[sdoc.length - 1]));
        return sdoc;
    }

    // 计算分页的总页面数
    private static int getPageCount(int pageSize, int count) {
        int pageCount = 1;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }

    public static String getIndexPath() {
        String savePath = getIKProperties("index_path");
        if (savePath == null) {
            try {
                savePath = AbstractLuceneIndex.class.getProtectionDomain()
                        .getCodeSource().getLocation().getPath();
                File directory = new File(savePath + "../../" + File.separator + "indexWrite");
                savePath = java.net.URLDecoder.decode(directory.getCanonicalPath(), "UTF-8");
                return savePath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savePath;
    }

    private static String getIKProperties(String key) {
        Properties prop = new Properties();
        try {
            String path = AbstractLuceneIndex.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            path = java.net.URLDecoder.decode(path, "UTF-8");
            FileInputStream fis =
                    new FileInputStream(path + "IKAnalyzer.cfg.xml");
            prop.loadFromXML(fis);
            //prop.list(System.out);
            return prop.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 演变成分页查询
     *
     * @param query
     */
    public static TreeMap<String, Object> doSearch(Query query, int curPage, int pageSize) {
        // 创建IndexSearcher
        // 指定索引库的地址
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        ScoreDoc[] sdoc = null;
        SearchStruct searchStruct = new SearchStruct(curPage - 1, pageSize, query);
        try {
            if (!afterScoreDoc.contains(searchStruct)) {
                sdoc = setSearchDocs(query, curPage, pageSize);
                searchStruct = new SearchStruct(curPage, pageSize, query);
                if (afterScoreDoc.contains(searchStruct)) {
                    searchStruct = afterScoreDoc.get(afterScoreDoc.indexOf(searchStruct));
                } else {
                    setRunturnMap(curPage, treeMap);
                    return treeMap;
                }
            } else {
                searchStruct = afterScoreDoc.get(afterScoreDoc.indexOf(searchStruct));
                TopDocs topDocs = getIndexSearcher().searchAfter(searchStruct.scoreDoc, query, pageSize);
                sdoc = topDocs.scoreDocs;
                if (sdoc.length == 0) {
                    treeMap.put("curPage", curPage);
                    treeMap.put("pageCount", searchStruct.pageCount);
                    treeMap.put("count", searchStruct.count);
                    treeMap.put("doc", null);
                    return treeMap;
                }
                if (afterScoreDoc.size() > MAX_QUERY) afterScoreDoc.clear();
                afterScoreDoc.add(new SearchStruct(curPage, pageSize,
                        searchStruct.pageCount, searchStruct.count, query, sdoc[sdoc.length - 1]));
            }
            // 根据查询条件匹配出的记录总数
            if (sdoc == null) {
                setRunturnMap(curPage, treeMap);
                return treeMap;
            }
            ArrayList<Document> docList = new ArrayList<>(pageSize);
            Document doc;
            System.out.println("总页数：" + searchStruct.pageCount + "\t当前页：" + curPage);
            for (ScoreDoc scoreDoc : sdoc) {
                // 获取文档的ID
                int docId = scoreDoc.doc;
                // 通过ID获取文档
                doc = getIndexSearcher().doc(docId);
                docList.add(doc);
                //System.out.print("用户ID：" + doc.get("id"));
                //System.out.print("用户昵称：" + doc.get("nickname"));
                //System.out.print("\t用户密码：" + doc.get("password"));
                //System.out.println("最后修改时间：" + doc.get("time"));
            }
            treeMap.put("curPage", curPage);
            treeMap.put("pageCount", searchStruct.pageCount);
            treeMap.put("count", searchStruct.count);
            treeMap.put("doc", docList);
            return treeMap;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void setRunturnMap(int curPage, TreeMap<String, Object> treeMap) {
        treeMap.put("curPage", curPage);
        treeMap.put("pageCount", null);
        treeMap.put("count", null);
        treeMap.put("doc", null);
    }
}
