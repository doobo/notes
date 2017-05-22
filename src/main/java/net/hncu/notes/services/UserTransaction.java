package net.hncu.notes.services;

import net.hncu.notes.dao.impl.AbstractHibernate;
import net.hncu.notes.table.User;

/**
 * Created by doobo@foxmail.com on 2017/4/26.
 */
public interface UserTransaction {

    String USER_KEY = "note_user_20170426";

    String MASTER_KEY = "note_master_20170502";

    String ROOT_KEY = "note_root_20170503";

    //获取超级管理员的id
    Integer setOrGetRootId();

    //获取用户状态
    boolean getSystemStatus();

    User getUserByUID(Integer id);

    //获取UserName存在的数量
    Object isUserName(String userName);

    //通过用户名获取user
    Object getUserByName(String userName);

    //获取管理员信息
    Object getMasterByName(String userName);

    //添加普通用户
    Object addUser(String userName,String nickName,String pwd);

    //添加管理员
    Object addMaster(String userName,String nickName,String pwd);

    //更新普通用户密码
    Object updateUserPwd(String pwd,Integer uid);

    //删除指定用户类型的用户
    Object delUser(Integer uid,Integer type);

    //更改指定用户类型的用户状态
    Object updateUserStatus(Integer uid,Integer status,Integer type);

    //更新指定用户类型的用户密码
    public boolean updatePWD(String pwd,Integer uid,Integer type);

    //修改用户昵称
    Object updateUserNickName(String nickname, Integer uid);

    //添加网站管理员
    Object addMasterAccount(String userName,String nicknam,String pwd);

    //获取符合条件的user
    Object getSearchUser(Integer curPage,Integer pageSize,String wd
            ,Integer status,Integer type);
}
