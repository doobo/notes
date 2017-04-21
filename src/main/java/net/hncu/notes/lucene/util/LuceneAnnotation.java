package net.hncu.notes.lucene.util;

import org.apache.lucene.document.Field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by doobo@foxmail.com on 2017/3/22.
 */
@Target({ElementType.METHOD, ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LuceneAnnotation {

    public static enum FieldEnum{
        StringField,TextField,FloatField,LongField,StoredField
    }

    String saveIndexPath() default "undefined";

    FieldEnum field() default FieldEnum.TextField;

    String fieldName() default "undefined";

    Field.Store store() default Field.Store.YES;
}
