package net.hncu.notes.lucene.util;

import org.apache.lucene.document.*;

/**
 * Created by doobo@foxmail.com on 2017/3/22.
 */
 class LuceneField {

     int id;

     String  fieldName;

     LuceneAnnotation.FieldEnum fieldEnum;

     Field.Store store;

    /**
     * 根据LucenField的结构添加Document的Field
     * @param obj
     * @param doc
     */
     void addLuceneDocumentFiled(Object obj, Document doc){
         switch (this.fieldEnum) {
             case TextField:
                 try {
                     doc.add(new TextField(this.fieldName
                             , obj.toString()
                             , this.store));
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             case StringField:
                 try {
                     doc.add(new StringField(this.fieldName
                             , obj.toString()
                             , this.store));
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             case LongField:
                 try {
                     doc.add(new LongField(this.fieldName
                             , (long) obj
                             , this.store));
                     System.out.println("LongField"+obj);
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             case FloatField:
                 try {
                     doc.add(new FloatField(this.fieldName
                             , (Float) obj
                             , this.store));
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             case StoredField:
                 try {
                     doc.add(new StoredField(this.fieldName
                             , obj.toString()));
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             default:
                 try {
                     doc.add(new TextField(this.fieldName
                             , obj.toString()
                             , this.store));
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
         }
     }
}
