package net.hncu.notes.controller;
import net.hncu.notes.lucene.util.AbstractLuceneIndex;
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
import java.util.TreeMap;


/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@RequestMapping("/kingmaster/")
@Controller
public class KingMasterController {

    @Autowired
    NotesTransaction nt;

    @Autowired
    UserTransaction ut;

    @ResponseBody
    @RequestMapping("addMainType")
    public Object addMainType(String typeName,String description,HttpSession session){
        System.out.println(typeName+description);
        return nt.addMainType(typeName.trim(),description.trim(),getUid(session));
    }

    @ResponseBody
    @RequestMapping("addChild")
    public Object addChildType(String typeName,String description,Integer mid,HttpSession session){
        return  nt.addChildType(typeName,description,mid,getUid(session));
    }

    //搜索符合条件的笔记
    @ResponseBody
    @RequestMapping("/getSearchNotes")
    public Object getSearchNotes(Integer curPage,Integer pageSize
            ,HttpSession session,Integer check,Integer share,Long startTime
            ,Long endTime,String wd){
        return nt.getSearchNotesByStatus(curPage,pageSize,startTime,endTime
                ,wd,check,share,0,ut.getRootId());
    }

    //搜索删除的笔记
    @ResponseBody
    @RequestMapping("/getSearchDelNotes")
    public Object getSearchDelNotes(Integer curPage,Integer pageSize
            ,HttpSession session,Integer check,Integer share,Long startTime
            ,Long endTime,String wd){
        return nt.getSearchNotesByStatus(curPage,pageSize,startTime,endTime
                ,wd,check,share,1,ut.getRootId());
    }

    @ResponseBody
    @RequestMapping("getNoteByID")
    public Object getNoteByID(Integer id,HttpSession session){
        if(id == null) return null;
        return nt.getNoteByStaticID(id);
    }

    @ResponseBody
    @RequestMapping("getMainType")
    public Object getMainType(){
        return nt.getMainType();
    }

    @ResponseBody
    @RequestMapping("getChildType")
    public Object getChildTypeByMid(Integer mid){
        return nt.getChildTypeByMID(mid);
    }

    @ResponseBody
    @RequestMapping("uploadNoteImage")
    public Object uploadHeadImg(String imgData,String type,String base64Len,
                                HttpServletRequest request) {
        String savePath = request.getServletContext().getRealPath("/images/notes/");
        if(type == null)
            type = "jpg";
        String picName = AbstractNotesUtils.getTimeMillisSequence()+"."+type;
        savePath = savePath + picName;
        try {
            AbstractNotesUtils.generateImage(imgData,savePath);
        }catch (Exception e){
            return false;
        }
        return picName;
    }

    @ResponseBody
    @RequestMapping("updateNote")
    public Object updateNote(String title,String description ,Integer mid,HttpSession session,
                             String simpleDesc,Integer chid,Integer share,Integer nid,String images){
        return nt.updateNote(title,description,mid,simpleDesc
                ,chid,share,nid,getUid(session),images,1);
    }

    @ResponseBody
    @RequestMapping("getNoteToUpdate")
    public Object getNoteToUpdate(Integer nid,HttpSession session){
        return  nt.getNoteToUpdate(nid);
    }

    @ResponseBody
    @RequestMapping("/getSimpleNotes")
    public Object toNext(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        map.put("notes",nt.getSimpleNotes(curPage,pageSize,map));
        return map;
    }

    @ResponseBody
    @RequestMapping("getSearchChildType")
    public Object getSimpleChildType(Integer curPage,Integer pageSize,String wd){

        return nt.getSearchChildType(curPage,pageSize,wd,ut.getRootId());
    }

    @ResponseBody
    @RequestMapping("searchUser")
    public Object getSimpleUser(Integer curPage,Integer pageSize,String wd){
        return nt.getSearchUser(curPage,pageSize,wd);
    }

    @ResponseBody
    @RequestMapping("getSimpleKingMaster")
    public Object getSimpleKingMaster(Integer curPage,Integer pageSize){
        return nt.getSimpleKingMaster(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getDelUser")
    public Object getDelUsers(Integer curPage,Integer pageSize,String wd){
        return nt.getSearchDelUser(curPage,pageSize,wd);
    }

    @ResponseBody
    @RequestMapping("getDelNotes")
    public Object getDelNotes(Integer curPage,Integer pageSize){
        return nt.getDelNotes(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getIKText")
    public Object getIKLuceneCi(String text){
        return nt.getIKLuceneCi(text);
    }

    @ResponseBody
    @RequestMapping("clearLuceneCache")
    public Object clearLuceneCache(){
        return AbstractLuceneIndex.clearCache();
    }

    @ResponseBody
    @RequestMapping("getNotesNoCheck")
    public Object getNotesNoCheck(Integer curPage,Integer pageSize){
        return nt.getNotesNoCheck(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("changeCheck")
    public Object changeCheck(Integer id,Integer check){
        return nt.changeCheck(id,check);
    }

    @ResponseBody
    @RequestMapping("changeShare")
    public Object changeShare(Integer id,Integer share){
        return nt.changeShare(id,share);
    }

    @ResponseBody
    @RequestMapping("changeStatus")
    public Object changeStatus(Integer id,Integer status){
        return nt.changeStatus(id,status);
    }

    @ResponseBody
    @RequestMapping("getChildTypeByID")
    public Object getChildTypeByID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return nt.getChildTypeByID(cid);
    }

    @ResponseBody
    @RequestMapping("getMainTypeByCID")
    public Object getMainTypeByCID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return nt.getMainTypeByCID(cid);
    }

    @ResponseBody
    @RequestMapping("updateMainType")
    public Object updateMainType(Integer mid,String title,String desc){
        if(mid == null || title == null && desc==null){
            return false;
        }
        return nt.updateMainType(mid,title,desc,null);
    }

    @ResponseBody
    @RequestMapping("updateChildType")
    public Object updateChildType(Integer cid,String title,String desc){
        if(cid == null || title == null && desc==null){
            return false;
        }
        return nt.updateChildType(cid,title,desc,null);
    }

    @ResponseBody
    @RequestMapping("getSearchInPicInfo")
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize
            ,HttpSession session,String wd){
        return nt.getSearchInPicInfo(curPage,pageSize,wd);
    }

    @ResponseBody
    @RequestMapping("uploadHeadImg")
    public Object uploadHeadImg(String imgData,String type,
                                HttpServletRequest request,HttpSession session) {
        String savePath = request.getServletContext().getRealPath("/images/head/");
        type = "jpg";
        savePath = savePath + getUser(session).getUsername()+"."+type;
        return AbstractNotesUtils.generateImage(imgData,savePath);
    }

    @ResponseBody
    @RequestMapping("isHeadImg")
    public Object isHeadImg(HttpServletRequest request,String username){
        if(username == null || username.isEmpty()){
            return false;
        }
        String savePath = request.getServletContext().getRealPath("/images/head/");
        savePath = savePath + username +"."+"jpg";
        if(new File(savePath).exists()){
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("updateUserPwd")
    public Object updateUserPwd(String pwd,Integer uid){
        if(pwd == null || pwd.isEmpty()) return false;
        return ut.updateUserPwd(pwd,uid);
    }

    @ResponseBody
    @RequestMapping("updateMasterPwd")
    public Object updateMasterPwd(String pwd,HttpSession session){
        if(pwd == null || pwd.isEmpty()) return false;
        return ut.updatePWD(pwd,getUid(session),1);
    }

    @ResponseBody
    @RequestMapping("updateNickName")
    public Object updateUserNickName(HttpSession session,String nickname){
        if(nickname == null || nickname.isEmpty()) return false;
        return ut.updateUserNickName(nickname,getUid(session));
    }

    @ResponseBody
    @RequestMapping("delSimpleUser")
    public Object delUser(Integer uid){
        if(uid == null) return false;
        return ut.delUser(uid,0);
    }

    @ResponseBody
    @RequestMapping("updateUserStatus")
    public Object changUserStatus(Integer status,Integer uid){
        if(uid == null) return false;
        return ut.updateUserStatus(uid,status,0);
    }

    @RequestMapping("clearSession")
    public String clearSession(HttpSession session){
        session.invalidate();
        return "redirect:../index.html";
    }

    @ResponseBody
    @RequestMapping("getMaster")
    public Object getMaster(HttpSession session){
        User user = getUser(session);
        user.setPassword("");
        return user;
    }

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("master");
    }

    private Integer getUid(HttpSession session){
        return ((User) session.getAttribute("master")).getId();
    }
}
