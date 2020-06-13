package com._5fu8.admin.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * 将List转换为指定分隔符分隔的字符串存储 List的元素类型只支持常见的数据类型 可参考
 * Hibernate自定义类型
 */
public class ListStringType implements UserType {

    private static int[] TYPES = new int[]{Types.VARCHAR};

    /**
     * 修改类型对应的SQL类型,这里用VARCHAR
     */
    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    /**
     * 修改ListStringType对应的java类型（此处java类型为CType
     */
    @Override
    public Class returnedClass() {
        return CType.class;
    }

    /**
     * 自定义数据类型比对方法
     * 用作脏数据检查,o,o1为两个副本
     */
    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return Objects.equals(o, o1);
    }

    /**
     * 返回给定类型的hashCode
     */
    @Override
    public int hashCode(Object o) throws HibernateException {
        return Objects.hashCode(o);
    }

    /**
     * 读取数据转换为自定义类型返回
     * strings包含了自定义类型的映射字段名称
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names
            , SharedSessionContractImplementor session
            , Object owner) throws HibernateException, SQLException {
        String s = (String) rs.getObject(names[0]);
        if (rs.wasNull()) return null;
        if (null != s) {
            if (s.replaceAll(" ", "").isEmpty())
                return null;
            return new CType(s);
        }
        return null;
    }

    /**
     * 数据保存时被调用
     */
    @Override
    public void nullSafeSet(PreparedStatement st
            , Object value, int index
            , SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (null == value) {
            st.setNull(index, Types.VARCHAR);   //保存空值
        } else {
            String stringValue = String.valueOf(value);
            st.setString(index, stringValue);
        }
    }


    /**
     * 自定义类型的完全复制方法,构造返回对象
     * 1. 当nullSafeGet方法调用之后，我们获得了自定义数据对象，
     *    在向用户返回自定义数据之前,deepCopy方法被调用，
     *    它将根据自定义数据对象构造一个完全拷贝，把拷贝返还给客户使用。
     * 2. 此时我们就得到了自定义数据对象的两个版本
     *    原始版本由hibernate维护，用作脏数据检查依据;
     *    复制版本由用户使用，hibernate将在脏数据检查过程中比较这两个版本的数据;
     */
    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if (o == null) return null;
        CType c = (CType) o;
        CType nc = new CType();
        nc = c;
        return nc;
    }

    /**
     * 表示本类型实例是否可变
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * method called when Hibernate puts the data in a second level cache. The
     * data is stored in a serializable form （官方文档
     */
    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (CType) deepCopy(o);
    }

    /**
     * Returns the object from the 2 level cache （官方文档
     */
    @Override
    public Object assemble(Serializable serializable, Object o)
            throws HibernateException{
        return deepCopy(serializable);
    }

    @Override
    public Object replace(Object o, Object o1, Object o2)
            throws HibernateException {
        return deepCopy(o);
    }
}
