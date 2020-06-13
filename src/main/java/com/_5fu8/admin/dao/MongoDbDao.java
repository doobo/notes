package com._5fu8.admin.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import qpc.object.PageResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 描述:
 * mongoDB基础方法封装
 */
@Slf4j
public abstract class MongoDbDao<T> {

    /**
     * 反射获取泛型类型
     * @return
     */
    protected abstract Class<T> getEntityClass();

    @Autowired
    private MongoTemplate mongoTemplate;

    /***
     * 保存一个对象
     * @param t
     */
    public void save(T t) {
        log.info("-------------->MongoDB save start");
        this.mongoTemplate.save(t);
    }

    /***
     * 根据id从几何中查询对象
     * @param id
     * @return
     */
    public T queryById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        log.info("-------------->MongoDB find start");
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    /**
     * 根据条件查询集合
     *
     * @param object
     * @return
     */
    public List<T> queryList(T object) {
        Query query = getQueryByObject(object);
        log.info("-------------->MongoDB find start");
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 根据条件查询只返回一个文档
     *
     * @param object
     * @return
     */
    public T queryOne(T object) {
        Query query = getQueryByObject(object);
        log.info("-------------->MongoDB find start");
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    /***
     * 根据条件分页查询
     * @param object
     * @param pageNum 查询起始值
     * @param pageSize  查询大小
     * @return
     */
    public PageResult<T> getPage(T object, Integer pageNum, Integer pageSize) {
        Query query = getQueryByObject(object);
        if(pageNum != null){
            pageNum -= 1;
        }
        PageResult<T> pageModel = new PageResult<T>();
        Pageable pageable = PageRequest.of(pageNum==null?0:pageNum,pageSize==null?10:pageSize);

        log.info("-------------->MongoDB queryPage start");
        List<T> list = this.mongoTemplate.find(query.with(pageable), this.getEntityClass());
        pageModel.setRecords(list);
        return pageModel;
    }

    /***
     * 根据条件查询库中符合条件的记录数量
     * @param object
     * @return
     */
    public Long getCount(T object) {
        Query query = getQueryByObject(object);
        log.info("-------------->MongoDB Count start");
        return this.mongoTemplate.count(query, this.getEntityClass());
    }

    /***
     * 删除对象
     * @param t
     * @return
     */
    public int delete(T t) {
        log.info("-------------->MongoDB delete start");
        return (int) this.mongoTemplate.remove(t).getDeletedCount();
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void deleteById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
            log.info("-------------->MongoDB deleteById start");
            if (obj != null) {
                this.delete(obj);
            }
        }
    }

    /*MongoDB中更新操作分为三种
     * 1：updateFirst     修改第一条
     * 2：updateMulti     修改所有匹配的记录
     * 3：upsert  修改时如果不存在则进行添加操作
     * */
    /**
     * 修改匹配到的第一条记录
     * @param srcObj
     * @param targetObj
     */
    public void updateFirst(T srcObj, T targetObj){
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        log.info("-------------->MongoDB updateFirst start");
        this.mongoTemplate.updateFirst(query,update,this.getEntityClass());
    }

    /***
     * 修改匹配到的所有记录
     * @param srcObj
     * @param targetObj
     */
    public void updateMulti(T srcObj, T targetObj){
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        log.info("-------------->MongoDB updateFirst start");
        this.mongoTemplate.updateMulti(query,update,this.getEntityClass());
    }

    /***
     * 修改匹配到的记录，若不存在该记录则进行添加
     * @param srcObj
     * @param targetObj
     */
    public void updateInsert(T srcObj, T targetObj){
        Query query = getQueryByObject(srcObj);
        Update update = getUpdateByObject(targetObj);
        log.info("-------------->MongoDB updateInsert start");
        this.mongoTemplate.upsert(query,update,this.getEntityClass());
    }

    /**
     * 分页查询
     * @param map
     * @return
     */
    public PageResult<T> selectPage(Map<String, Object> map, Query query){
        query = query == null?new Query():query;
        if(map == null){
            map = new HashMap(2);
            map.put("pageNum", 1);
            map.put("pageSize", 10);
        }
        Integer pageNum = Integer.valueOf(String.valueOf(map.get("pageNum")));
        Integer pageSize = Integer.valueOf(String.valueOf(map.get("pageSize")));
        if(pageNum != null){
            pageNum -= 1;
        }
        PageResult<T> pageModel = new PageResult<T>();
        pageSize = pageSize==null?10:pageSize;
        pageNum = pageNum==null?0:pageNum;
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        List<T> list= this.mongoTemplate.find(query.with(pageable), getEntityClass());
        pageModel.setRecords(list);
        pageModel.setPageSize(pageSize);
        pageModel.setPageNo(++pageNum);
        return pageModel;
    }

    /**
     * 自定义条件查询一条数据
     * @param query
     * @return
     */
    public T selectOne(Query query){
        query = query == null?new Query():query;
        return this.mongoTemplate.findOne(query,this.getEntityClass());
    }

    /**
     * 查询总数
     * @param query
     * @return
     */
    public Long selectCount(Query query){
        query = query == null?new Query():query;
        return mongoTemplate.count(query, this.getEntityClass());
    }

    /**
     * 将查询条件对象转换为query
     *
     * @param object
     * @return
     * @author Jason
     */
    public Query getQueryByObject(T object, Criteria criteria, List<Criteria> andList, List<Criteria> orList) {
        Query query = new Query();
        String[] fds = getFiledName(object);
        criteria = criteria == null ? new Criteria() : criteria;
        andList = andList == null ? new ArrayList<>() : andList;
        orList = orList == null ? new ArrayList<>() : orList;
        for (int i = 0; i < fds.length; i++) {
            String filedName = fds[i];
            Object filedValue = getFieldValueByName(filedName, object);
            if(filedValue == null){
            }else if(filedValue.getClass().equals(String[].class)){
                orList.addAll(Arrays.asList((String[]) filedValue).stream().map(m->Criteria.where(filedName).is(m)).collect(Collectors.toList()));
            }else if(filedValue instanceof List){
                orList.addAll(((List<Object>)filedValue).stream().map(m->Criteria.where(filedName).is(m)).collect(Collectors.toList()));
            }else if(filedValue instanceof Set){
                criteria.orOperator();
                orList.addAll(((Set<Object>)filedValue).stream().map(m->Criteria.where(filedName).is(m)).collect(Collectors.toList()));
            } else if (filedValue != null) {
                andList.add(Criteria.where(filedName).is(filedValue));
            }
        }
        if(orList != null && !orList.isEmpty()){
            criteria.orOperator(orList.stream().toArray(Criteria[]::new));
        }
        if(andList != null && !andList.isEmpty()){
            criteria.andOperator(andList.stream().toArray(Criteria[]::new));
        }
        query.addCriteria(criteria);
        return query;
    }

    private Query getQueryByObject(T object){
        return getQueryByObject(object, null, null, null);
    }

    public Query getQueryByObject(T object, Criteria criteria){
        return getQueryByObject(object, criteria, null, null);
    }

    /**
     * 将查询条件对象转换为update
     *
     * @param object
     * @return
     * @author Jason
     */
    private Update getUpdateByObject(T object) {
        Update update = new Update();
        String[] fileds = getFiledName(object);
        for (int i = 0; i < fileds.length; i++) {
            String filedName = (String) fileds[i];
            Object filedValue =getFieldValueByName(filedName, object);
            if (filedValue != null) {
                update.set(filedName, filedValue);
            }
        }
        return update;
    }

    /***
     * 获取对象属性返回字符串数组
     * @param o
     * @return
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = getAllFields(o);
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取父类所有的属性
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /***
     * 根据属性获取对象属性值
     * @param fieldName
     * @param o
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String e = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + e + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[0]);
            return method.invoke(o, new Object[0]);
        } catch (Exception var6) {
            return null;
        }
    }
}
