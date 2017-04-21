package net.hncu.notes.lucene.util;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

/**
 * Created by doobo@foxmail.com on 2017/3/26.
 */
 class SearchStruct {

     int curPage;

     int pageSize;

     int pageCount;

     int count;

     Query query;

     ScoreDoc scoreDoc;

    SearchStruct(int curPage, int pageSize, Query query) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.query = query;
    }

    SearchStruct(int curPage, int pageSize,int pageCount
            ,int count, Query query,ScoreDoc scoreDoc) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.count = count;
        this.query = query;
        this.scoreDoc = scoreDoc;
    }



    SearchStruct() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchStruct)) return false;

        SearchStruct that = (SearchStruct) o;

        if (curPage != that.curPage) return false;
        if (pageSize != that.pageSize) return false;
        return query.equals(that.query);
    }

    @Override
    public int hashCode() {
        int result = curPage;
        result = 31 * result + pageSize;
        result = 31 * result + query.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SearchStruct{" +
                "curPage=" + curPage +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", count=" + count +
                ", query=" + query.getBoost() +
                ", scoreDoc=" + scoreDoc +
                '}';
    }
}
