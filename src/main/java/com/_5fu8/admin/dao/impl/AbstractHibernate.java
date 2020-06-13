package com._5fu8.admin.dao.impl;

import com._5fu8.admin.dao.Hibernateable;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import qpc.object.PageResult;
import qpc.object.basic.ResultUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
public abstract class AbstractHibernate implements Hibernateable {

    public abstract Class<?> getEntityClass();

    @PersistenceContext
    private EntityManager entityManager;

    /**获取sessionFactory*/
    private SessionFactory getSessionFactory() {
        Session session = entityManager.unwrap(Session.class);
        return session.getSessionFactory();
    }

    public HibernateTemplate getHibernateTemplate(){
        return new HibernateTemplate(getSessionFactory());
    }

    //获取和当前线程绑定的 Session.
    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    /**
     * 保存对象实例到数据库
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean addDataByClass(T t) {
        try {
            getSession().save(t);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过id更新数据
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean updateDataByClass(T t){
        getSession().update(t);
        return true;
    }

    /**
     * 通过ID获取数据
     */
    public <T> T getPojoByID(int id, Class<T> tClass) {
        return (T) getSession().get(tClass, id);
    }

    /**
     * 分页获取指定类的数据
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> List<T> getPageByClass(PageResult<T> pageResult, Class<T> tClass) {
        try {
            CriteriaBuilder crb=getSession().getCriteriaBuilder();
            CriteriaQuery<T> crq = crb.createQuery(tClass);
            Root<T> root = crq.from(tClass);
            crq.select(root);
            if (!pageResult.getOrderBy().isEmpty()) {
                if ("desc".compareToIgnoreCase(pageResult.getSort()) == 0)
                    crq.orderBy(crb.desc(root.get(pageResult.getOrderBy())));
                else {
                    crq.orderBy(crb.asc(root.get(pageResult.getOrderBy())));
                }
            }
            Query<T> query = getSession().createQuery(crq);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 分页查询
     * @param pageResult
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> PageResult<T> listByPage(PageResult<T> pageResult, Class<T> tClass) {
        CriteriaBuilder crb=getSession().getCriteriaBuilder();
        CriteriaQuery<T> crq = crb.createQuery(tClass);
        Root<T> root = crq.from(tClass);
        crq.select(root);
        Query<T> query = getSession().createQuery(crq);
        int counts = query.list().size();
        if (!pageResult.getOrderBy().isEmpty()) {
            if ("desc".compareToIgnoreCase(pageResult.getSort()) == 0)
                crq.orderBy(crb.desc(root.get(pageResult.getOrderBy())));
            else {
                crq.orderBy(crb.asc(root.get(pageResult.getOrderBy())));
            }
        }
        query.setFirstResult(pageResult.getFirstRec());
        query.setMaxResults(pageResult.getPageSize());
        pageResult.setRecords(query.list());
        int pageSize = pageResult.getPageSize();
        int pages = ResultUtils.getPages(counts, pageSize);
        pageResult.setTotal(counts);
        pageResult.setPages(pages);
        return pageResult;
    }

    /**
     * 通过设置Criteria的来获取满足条件的分页数据
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return
     */
    public List<?> getPageByCriteria(int pageNo, int pageSize, DetachedCriteria criteria) {
        try {
            return getHibernateTemplate().findByCriteria(criteria
                    ,(pageNo - 1) * pageSize, pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过Criteria查询符合条件的总数
     * @param criteria
     * @return
     */
    public Long getCountByCriteria(Criteria criteria){
        criteria.setProjection(Projections.rowCount());
        return  (Long) criteria.uniqueResult();
    }

    /**
     *返回Query
     * @param hql
     * @param curPage
     * @param pageSize
     * @return
     */
    public Query getQueryByHQL(String hql, Integer curPage, Integer pageSize){
        if(curPage == null || pageSize ==null){
            return  getSession().createQuery(hql);
        }
        return getSession().createQuery(hql)
                .setFirstResult((curPage - 1)* pageSize)
                .setMaxResults(pageSize);
    }

    /**
     * 获取Query
     * @param hql
     * @return
     */
    public <T> Query<T> getQueryByHQL(String hql, Class<T> cls){
        return getSession().createQuery(hql, cls);
    }

    /**
     * 获取Query
     * @param hql
     * @return
     */
    public Query getQueryByHQL(String hql){
        return getSession().createQuery(hql);
    }


    /**
     * 执行HQL返回唯一结果集,如平均值,总记录数
     */
    public Object getUniqueResultByHQL(String hql){
        return getSession().createQuery(hql).uniqueResult();
    }

}
