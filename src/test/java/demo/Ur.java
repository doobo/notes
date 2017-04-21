package demo;

import net.hncu.notes.lucene.util.LuceneAnnotation;

/**
 * Created by doobo@foxmail.com on 2017/4/8.
 */
public class Ur {

    @LuceneAnnotation(fieldName = "urid",field = LuceneAnnotation.FieldEnum.StringField)
    private Integer urid;

    @LuceneAnnotation(fieldName = "title2",field = LuceneAnnotation.FieldEnum.TextField)
    private String title2;

    public Ur(Integer urid, String title2) {
        this.urid = urid;
        this.title2 = title2;
    }
}
