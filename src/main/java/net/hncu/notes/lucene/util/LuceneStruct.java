package net.hncu.notes.lucene.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

/**
 * Created by doobo@foxmail.com on 2017/3/22.
 */
 class  LuceneStruct {

     String className;

     String saveIndexPath;

     Vector<LuceneField> fieldVector = new Vector<LuceneField>();

     Vector<LuceneField> methodVector = new Vector<LuceneField>();

     LuceneStruct(Class<?> cls){
        if (!cls.isAnnotationPresent(LuceneAnnotation.class)){
            throw new RuntimeException(cls.getName()+"is not LuceneAnnotation!");
        }

        LuceneAnnotation luceneAnnotation = cls.getAnnotation(LuceneAnnotation.class);
        if("undefined".equals(luceneAnnotation.saveIndexPath())){
            saveIndexPath = getMD5(cls.getName());
        }else {
            saveIndexPath = luceneAnnotation.saveIndexPath();
        }
        className = cls.getName();

        LuceneField field;
        //获取属性的注解名和对应的参数
        for (int i = 0; i < cls.getDeclaredFields().length; i++) {
            if (cls.getDeclaredFields()[i].isAnnotationPresent(LuceneAnnotation.class)) {
                luceneAnnotation =
                        cls.getDeclaredFields()[i].getAnnotation(LuceneAnnotation.class);
                field = new LuceneField();
                field.id = i;
                if("undefined".equals(luceneAnnotation.fieldName())){
                    cls.getDeclaredFields()[i].setAccessible(true);
                    field.fieldName =  cls.getDeclaredFields()[i].getName();
                }else {
                    field.fieldName = luceneAnnotation.fieldName();
                }
                field.fieldEnum = luceneAnnotation.field();
                field.store = luceneAnnotation.store();
                fieldVector.add(field);
            }
        }

        //获取属性的注解名和对应的参数
        for (int i = 0; i < cls.getDeclaredMethods().length; i++) {
            if (cls.getDeclaredMethods()[i].isAnnotationPresent(LuceneAnnotation.class)) {
                luceneAnnotation =
                        cls.getDeclaredMethods()[i].getAnnotation(LuceneAnnotation.class);
                field = new LuceneField();
                field.id = i;
                if("undefined".equals(luceneAnnotation.fieldName())){
                    cls.getDeclaredMethods()[i].setAccessible(true);
                    field.fieldName =  cls.getDeclaredMethods()[i].getName();
                }else {
                    field.fieldName = luceneAnnotation.fieldName();
                }
                field.fieldEnum = luceneAnnotation.field();
                field.store = luceneAnnotation.store();
                methodVector.add(field);
            }
        }
    }

     LuceneStruct(String className) {
        this.className = className;
    }

    String getMD5(String str) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(str.getBytes());
            byte[] md = mdInst.digest();
            StringBuilder sb = new StringBuilder(md.length * 2);
            for (byte b : md) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LuceneStruct that = (LuceneStruct) o;

        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}
