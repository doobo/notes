package net.hncu.notes.controller;

import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.services.UserTransaction;
import net.hncu.notes.table.User;
import net.hncu.notes.utils.AbstractNotesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Created by doobo@foxmail.com on 2017/4/17.
 */
@RequestMapping("/public/")
@Controller
public class PublicController {

    @Autowired
    private NotesTransaction nt;

    @Autowired
    private NotesLucene nl;

    @Autowired
    private UserTransaction ut;

    @ResponseBody
    @RequestMapping("getSearchResult")
    public Object getSearchResult(Integer curPage,Integer pageSize,String wd,Integer type){
//        System.out.println(wd+"   "+type);
        if(type != null && type == 2){
            return nl.getTitleSearchResult(curPage,pageSize,wd);
        }
        return nl.getTitleOrDescResult(curPage,pageSize,wd);
    }

    @ResponseBody
    @RequestMapping("getMainType")
    public Object getMainType(HttpSession session){
        Object obj = getUid(session);
        if(obj instanceof Integer){
            return nt.getMainTypeByUid((Integer) obj,getRid());
        }
        return nt.getMainTypeByUid(null,getRid());
    }

    @ResponseBody
    @RequestMapping("getChildType")
    public Object getChildTypeByMid(Integer mid){
        return nt.getChildTypeByMID(mid);
    }


    @ResponseBody
    @RequestMapping("getNoteByID")
    public Object getNoteByID(Integer id){
        if(id == null) return null;
        return nt.getNoteByID(id);
    }

    @ResponseBody
    @RequestMapping("getNotesByCid")
    public Object getNoteByCID(Integer curPage,Integer pageSize,Integer cid){
        return nt.getDelNotesByCID(curPage,pageSize,cid);
    }

    @ResponseBody
    @RequestMapping("getNextNote")
    public Object getNextNote(Long time,Integer cid,Integer nid){
        return nt.getNextNoteByTime(time,nid,cid);
    }

    @ResponseBody
    @RequestMapping("getPreNote")
    public Object getPreNote(Long time,Integer cid,Integer nid){
        return nt.getPreNoteByTime(time,nid,cid);
    }

    @ResponseBody
    @RequestMapping("getIKText")
    public Object getIKLuceneCi(String text){
        return nt.getIKLuceneCi(text);
    }

    @ResponseBody
    @RequestMapping("ToRegister")
    public Object register(String userName,String nickName,String pwd){
        if(nickName==null || nickName.isEmpty()){
            nickName = "u"+userName;
        }
        if(ut.addUser(userName,nickName,pwd) == null){
            return false;
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("loginController")
    public Object login(String userName,String pwd,HttpSession session){
        if(userName == null || pwd == null){
            return false;
        }
        Object obj = ut.getUserByName(userName);
        if(obj == null){
            return false;
        }
        if(((User)obj).getPassword().equals(AbstractNotesUtils.getMD5(pwd,ut.USER_KEY))){
            session.setAttribute("user",obj);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("masterLogin")
    public Object toMaster(String userName,String pwd,HttpSession session){
        if(userName == null || pwd == null){
            return false;
        }
        User master = (User) ut.getMasterByName(userName);
        if(master == null || master.getType() != 1) return false;
        if(master.getPassword().equals(AbstractNotesUtils.getMD5(pwd,ut.MASTER_KEY))){
            session.setAttribute("master",master);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("systemLogin")
    public Object toRoot(String pwd,HttpSession session){
        if(pwd == null || pwd.isEmpty()) return false;
        User root = (User) ut.getUserByUID(getRid());
        if(root == null || root.getType() != 2) return false;
        if(root.getPassword().equals(AbstractNotesUtils.getMD5(pwd,ut.ROOT_KEY))){
            session.setAttribute("root",root);
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("isRootImg")
    public Object isHeadImg(HttpServletRequest request){
        String savePath = request.getServletContext().getRealPath("/images/head/");
        savePath = savePath + "root" +"."+"jpg";
        if(new File(savePath).exists()){
            return true;
        }
        return false;
    }

    //获取超级管理员的id
    private Integer getRid(){
        return ut.getRootId();
    }

    private Object getUid(HttpSession session){
        if(session.getAttribute("user")!=null){
            return ((User)session.getAttribute("user")).getId();
        }else {
            return  false;
        }
    }
}
