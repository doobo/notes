package net.hncu.notes.dao.impl;

import net.hncu.notes.dao.Hibernateable;
import net.hncu.notes.table.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by doobo@foxmail.com on 2017/3/23.
 */
@Component
public  class AbstractHibernate implements Hibernateable {
    @Autowired
    private SessionFactory sessionFactory;

    //获取和当前线程绑定的 Session.
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存对象实例到数据库
     *
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
     *
     * @param pageSize
     * @param curPage
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> List<T> getPageByClass(int pageSize, int curPage, Class<T> tClass) {
        try {
            Criteria criteria = getSession().createCriteria(tClass);
            List<?> list = criteria.setFirstResult((curPage - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
            return (List<T>) list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过设置Criteria的来获取满足条件的分页数据
     * @param pageNo
     * @param pageSize
     * @param criteria
     * @return
     */
    public List<?> getPageByCriteria(int pageNo, int pageSize, Criteria criteria) {
        //        Criteria criteria = getSession().createCriteria(t.getClass());
        //        criteria.add(Restrictions.eq(I18nUtils.getFieldNameByNo(t, 1), new Type(type)));
        //        criteria.addOrder(Order.desc(I18nUtils.getFieldNameByNo(t, 7)));
        //        criteria.add(Restrictions.eq(I18nUtils.getFieldNameByNo(t, 8), '1'));
        //        criteria.add(Restrictions.eq(I18nUtils.getFieldNameByNo(t, 10), '0'));
        try {
            return criteria.setFirstResult((pageNo - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过Criteria查询符合条件的总数
     * @param criteria
     * @return
     */
    public Long getCountByCritieria(Criteria criteria){
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
    public Query getQueryByHQL(String hql,Integer curPage,Integer pageSize){
        if(curPage == null || pageSize ==null){
            return  getSession().createQuery(hql);
        }
        return  getSession().createQuery(hql)
                .setFirstResult((curPage - 1)* pageSize)
                .setMaxResults(pageSize);
    }

    /**
     * 获取Query
     * @param hql
     * @return
     */
    public Query getQueryByHQL(String hql){
        return  getSession().createQuery(hql);
    }


    /**
     * 执行HQL返回唯一结果集,如平均值、总记录数
     */
    public Object getUniqueResultByHQL(String hql){
        return  getSession().createQuery(hql).uniqueResult();
    }

}
