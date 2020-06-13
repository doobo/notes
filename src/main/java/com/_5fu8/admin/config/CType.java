package com._5fu8.admin.config;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 自定义数据类型
 */
public class CType implements Serializable {

    // mType的长度
    private static int TYPE_LENGTH = 16;

    // mtype实际上就是char数组
    private char[] mtype = new char[TYPE_LENGTH];

    // 默认构造函数
    public CType() {
        this.mtype = new char[0];
    }

    // 通过构造函数转换为CType类型
    public CType(Object object) {
        String str = String.valueOf(object);
        if (StringUtils.isBlank(str))
            this.mtype = new char[0];
        this.mtype = str.toCharArray();
    }

    public boolean isEmpty() {
        return String.valueOf(this.mtype)
                .replace(" ", "").length() < TYPE_LENGTH;
    }

    public String toString() {
        return String.valueOf(this.mtype);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CType ctype = (CType) o;
        return Arrays.equals(mtype, ctype.mtype);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mtype);
    }
}
