package com._5fu8.admin.services.impl;

import com._5fu8.admin.services.UserTransaction;
import com._5fu8.admin.dao.impl.AbstractHibernate;
import com._5fu8.admin.table.User;
import com._5fu8.admin.utils.AbstractNotesUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/4/26.
 */
@Service
@Transactional
public class UserTransactionImpl extends AbstractHibernate implements UserTransaction {

    @Override
    public Class getEntityClass() {
        return User.class;
    }

    //超级管理员的id
    private Integer rid = null;

    //true,为初始化，false重新初始化，添加root账号
    private boolean INIT_TAG = true;

    //初始化超级管理员账号,true,为初始化，false重新初始化，添加root账号
    private   boolean initSystem(){
        if(rid != null) return true;
        String hql = "FROM User u WHERE u.username = :root";
        User user =(User) getQueryByHQL(hql)
                            .setParameter("root","root")
                            .uniqueResult();
        if(user == null){
            user = new User("root","超级管理员"
                    ,null,2,0);
            user.setPassword(AbstractNotesUtils.getMD5(
                    AbstractNotesUtils.getMD5("root",null)
            ,ROOT_KEY));
            addDataByClass(user);
            rid = user.getId();
            return false;
        }else{
            rid = user.getId();
            return true;
        }
    }

    @Override
    public Integer getRootId(){
        if(rid == null){
            initSystem();
        }
        return rid;
    }

    @Override
    public boolean getSystemStatus(){
        if(rid == null)
            initSystem();
        return INIT_TAG;
    }

    //判断用户名是否存在
    @Override
    public Object isUserName(String userName){
        String hql = "SELECT count(*) FROM User u WHERE u.username =:username";
        return getQueryByHQL(hql)
                .setParameter("username",userName)
                .uniqueResult();
    }

    @Override
    public Object getUserByUID(Integer id){
        return getPojoByID(id,User.class);
    }

    public Object getUserByName(String userName){
        String hql = "FROM User u WHERE u.username =:username AND u.status = 0" +
                " AND u.type = 0";
        return getQueryByHQL(hql)
                .setParameter("username",userName)
                .uniqueResult();
    }

    @Override
    public Object getMasterByName(String userName){
        String hql = "FROM User u WHERE u.username =:username AND u.status = 0" +
                " AND u.type = 1";
        return getQueryByHQL(hql)
                .setParameter("username",userName)
                .uniqueResult();
    }

    //添加普通用户
    @Override
    public Object addUser(String userName,String nickName,String pwd){
        //判读是否存在相同的用户名
        if(!isUserName(userName).toString().equals("0")){
            return null;
        }
        pwd = AbstractNotesUtils.getMD5(pwd,USER_KEY);
        User user = new User(userName,nickName,pwd,0,0);
        addDataByClass(user);
        return user;
    }

    @Override
    public Object addMaster(String userName,String nickName,String pwd){
        if(!isUserName(userName).toString().equals("0")){
            return false;
        }
        pwd = AbstractNotesUtils.getMD5(pwd,MASTER_KEY);
        User master = new User(userName,nickName,pwd,1,0);
        addDataByClass(master);
        return master;
    }

    @Override
    public Boolean updateUserPwd(String pwd,Integer uid){
      return updatePWD(pwd,uid,0);
    }

    @Override
    public Boolean delUser(Integer uid,Integer type){
        User user = getPojoByID(uid,User.class);
        if(user == null){
            return false;
        }
        if(user.getType() != type) return false;
        user.setStatus(1);
        updateDataByClass(user);
        return true;
    }

    public Boolean updateUserStatus(Integer uid,Integer status,Integer type){
        User user = getPojoByID(uid,User.class);
        if(user == null){
            return false;
        }
        if(user.getType() != type) return false;
        if(user.getStatus() > 1) return false;
        if(user.getStatus() == status) return true;
        user.setStatus(status);
        updateDataByClass(user);
        return true;
    }

    @Override
    public Boolean updatePWD(String pwd,Integer uid,Integer type){
        User user = getPojoByID(uid,User.class);
        if(pwd == null || user == null) return false;
        switch (type){
            case 0:
                if(user.getType() != 0) return false;
                if(AbstractNotesUtils.getMD5(pwd, UserTransaction.USER_KEY)
                        .equals(user.getPassword())){
                    return true;
                }else {
                    user.setPassword(AbstractNotesUtils.getMD5(pwd,UserTransaction.USER_KEY));
                    updateDataByClass(user);
                    return true;
                }
            case 1:
                if(user.getType() != 1 ) return false;
                if(AbstractNotesUtils.getMD5(pwd, UserTransaction.MASTER_KEY)
                        .equals(user.getPassword())){
                    return true;
                }else {
                    user.setPassword(AbstractNotesUtils.getMD5(pwd,UserTransaction.MASTER_KEY));
                    updateDataByClass(user);
                    return true;
                }
            case 2:
                if(user.getType() != 2) return false;
                if(AbstractNotesUtils.getMD5(pwd, UserTransaction.ROOT_KEY)
                        .equals(user.getPassword())){
                    return true;
                }else {
                    user.setPassword(AbstractNotesUtils.getMD5(pwd,UserTransaction.ROOT_KEY));
                    updateDataByClass(user);
                    return true;
                }
            default:
                    return false;
        }
    }

    @Override
    public Boolean updateUserNickName(String nickname, Integer uid){
        User user = getPojoByID(uid,User.class);
        if(nickname == null || user == null) return false;
        if( nickname.equals(user.getNickname())){
            return true;
        }else {
            user.setNickname(nickname);
            updateDataByClass(user);
            return true;
        }
    }

    public Object addMasterAccount(String userName,String nicknam,String pwd){
        if(!isUserName(userName).toString().equals("0")){
            return false;
        }

        User user = new User(userName,nicknam
                ,AbstractNotesUtils.getMD5(pwd,MASTER_KEY),1,0);
        addDataByClass(user);
        return true;
    }

    @Override
    public Object getSearchUser(Integer curPage,Integer pageSize,String wd
            ,Integer status,Integer type){
        TreeMap<String,Object> map = new TreeMap<>();
        String hql = "SELECT count(*)  FROM User u WHERE  u.status = :status" +
                " AND u.type = :type";
        if(wd != null && !wd.isEmpty()){
            wd = AbstractServices.getLikeWd(wd);
            hql = hql + " AND u.username like :wd";
            AbstractServices.setReturnMap(curPage, pageSize, map
                    , getQueryByHQL(hql)
                            .setParameter("wd",wd)
                            .setParameter("status",status)
                            .setParameter("type",type)
                            .uniqueResult());
            hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                    +" FROM User u WHERE  u.status = :status AND u.type = :type" +
                    " AND u.username like :wd ORDER BY u.id desc";
            map.put("users",getQueryByHQL(hql,curPage,pageSize)
                    .setParameter("wd",wd)
                    .setParameter("status",status)
                    .setParameter("type",type)
                    .list());
            return map;
        }
        AbstractServices.setReturnMap(curPage, pageSize, map, getQueryByHQL(hql)
                .setParameter("status",status)
                .setParameter("type",type)
                .uniqueResult());
        hql = "SELECT u.id,u.username,u.nickname,u.signTime "
                +" FROM User u WHERE  u.status = :status AND u.type = :type ORDER BY u.id desc";
        map.put("users",getQueryByHQL(hql,curPage,pageSize)
                                        .setParameter("status",status)
                                        .setParameter("type",type)
                                        .list());
        return map;
    }
}
