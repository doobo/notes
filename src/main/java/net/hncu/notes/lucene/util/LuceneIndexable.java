package net.hncu.notes.lucene.util;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by doobo@foxmail.com on 2017/3/21.
 */
public interface LuceneIndexable {

    final static int MAX_QUERY = 100;

    /*类分析数据保存*/
    static Vector<LuceneStruct> luceneStructVector = new Vector<LuceneStruct>();

    static Vector<SearchStruct> afterScoreDoc = new Vector<SearchStruct>(MAX_QUERY);
}
