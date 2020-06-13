package com._5fu8.admin.controller;
import com._5fu8.admin.lucene.util.AbstractLuceneIndex;
import com._5fu8.admin.services.NotesTransaction;
import com._5fu8.admin.services.UserTransaction;
import com._5fu8.admin.table.User;
import com._5fu8.admin.utils.AbstractNotesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
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
    @PostMapping("addMainType")
    public Object addMainType(String typeName,String description,HttpSession session){
        System.out.println(typeName+description);
        return nt.addMainType(typeName.trim(),description.trim(),getUid(session));
    }

    @ResponseBody
    @PostMapping("addChild")
    public Object addChildType(String typeName,String description,Integer mid,HttpSession session){
        return  nt.addChildType(typeName,description,mid,getUid(session));
    }

    //搜索符合条件的笔记
    @ResponseBody
    @PostMapping("/getSearchNotes")
    public Object getSearchNotes(Integer curPage,Integer pageSize
            ,HttpSession session,Integer check,Integer share,Long startTime
            ,Long endTime,String wd){
        return nt.getSearchNotesByStatus(curPage,pageSize,startTime,endTime
                ,wd,check,share,0,ut.getRootId());
    }

    //搜索删除的笔记
    @ResponseBody
    @PostMapping("/getSearchDelNotes")
    public Object getSearchDelNotes(Integer curPage,Integer pageSize
            ,HttpSession session,Integer check,Integer share,Long startTime
            ,Long endTime,String wd){
        return nt.getSearchNotesByStatus(curPage,pageSize,startTime,endTime
                ,wd,check,share,1,ut.getRootId());
    }

    @ResponseBody
    @PostMapping("getNoteByID")
    public Object getNoteByID(Integer id,HttpSession session){
        if(id == null) return null;
        return nt.getNoteByStaticID(id);
    }

    @ResponseBody
    @PostMapping("getMainType")
    public Object getMainType(){
        return nt.getMainType();
    }

    @ResponseBody
    @PostMapping("getChildType")
    public Object getChildTypeByMid(Integer mid){
        return nt.getChildTypeByMID(mid);
    }

    @ResponseBody
    @PostMapping("uploadNoteImage")
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
    @PostMapping("updateNote")
    public Object updateNote(String title,String description ,Integer mid,HttpSession session,
                             String simpleDesc,Integer chid,Integer share,Integer nid,String images){
        return nt.updateNote(title,description,mid,simpleDesc
                ,chid,share,nid,getUid(session),images,1);
    }

    @ResponseBody
    @PostMapping("getNoteToUpdate")
    public Object getNoteToUpdate(Integer nid,HttpSession session){
        return  nt.getNoteToUpdate(nid);
    }

    @ResponseBody
    @PostMapping("/getSimpleNotes")
    public Object toNext(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        map.put("notes",nt.getSimpleNotes(curPage,pageSize,map));
        return map;
    }

    @ResponseBody
    @PostMapping("getSearchChildType")
    public Object getSimpleChildType(Integer curPage,Integer pageSize,String wd){

        return nt.getSearchChildType(curPage,pageSize,wd,ut.getRootId());
    }

    @ResponseBody
    @PostMapping("searchUser")
    public Object getSimpleUser(Integer curPage,Integer pageSize,String wd){
        return nt.getSearchUser(curPage,pageSize,wd);
    }

    @ResponseBody
    @PostMapping("getSimpleKingMaster")
    public Object getSimpleKingMaster(Integer curPage,Integer pageSize){
        return nt.getSimpleKingMaster(curPage,pageSize);
    }

    @ResponseBody
    @PostMapping("getDelUser")
    public Object getDelUsers(Integer curPage,Integer pageSize,String wd){
        return nt.getSearchDelUser(curPage,pageSize,wd);
    }

    @ResponseBody
    @PostMapping("getDelNotes")
    public Map<String,Object> getDelNotes(Integer curPage, Integer pageSize){
        return nt.getDelNotes(curPage,pageSize);
    }

    @ResponseBody
    @PostMapping("getIKText")
    public HashSet<String> getIKLuceneCi(String text){
        return nt.getIKLuceneCi(text);
    }

    @ResponseBody
    @PostMapping("clearLuceneCache")
    public Object clearLuceneCache(){
        return AbstractLuceneIndex.clearCache();
    }

    @ResponseBody
    @PostMapping("getNotesNoCheck")
    public Object getNotesNoCheck(Integer curPage,Integer pageSize){
        return nt.getNotesNoCheck(curPage,pageSize);
    }

    @ResponseBody
    @PostMapping("changeCheck")
    public Object changeCheck(Integer id,Integer check){
        return nt.changeCheck(id,check);
    }

    @ResponseBody
    @PostMapping("changeShare")
    public Object changeShare(Integer id,Integer share){
        return nt.changeShare(id,share);
    }

    @ResponseBody
    @PostMapping("changeStatus")
    public Object changeStatus(Integer id,Integer status){
        return nt.changeStatus(id,status);
    }

    @ResponseBody
    @PostMapping("getChildTypeByID")
    public Object getChildTypeByID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return nt.getChildTypeByID(cid);
    }

    @ResponseBody
    @PostMapping("getMainTypeByCID")
    public Object getMainTypeByCID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return nt.getMainTypeByCID(cid);
    }

    @ResponseBody
    @PostMapping("updateMainType")
    public Boolean updateMainType(Integer mid,String title,String desc){
        if(mid == null || title == null && desc==null){
            return false;
        }
        return nt.updateMainType(mid,title,desc,null);
    }

    @ResponseBody
    @PostMapping("updateChildType")
    public Boolean updateChildType(Integer cid,String title,String desc){
        if(cid == null || title == null && desc==null){
            return false;
        }
        return nt.updateChildType(cid,title,desc,null);
    }

    @ResponseBody
    @GetMapping("getSearchInPicInfo")
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize
            ,HttpSession session,String wd){
        return nt.getSearchInPicInfo(curPage,pageSize,wd);
    }

    @ResponseBody
    @PostMapping("uploadHeadImg")
    public Boolean uploadHeadImg(String imgData,String type,
                                HttpServletRequest request,HttpSession session) {
        String savePath = request.getServletContext().getRealPath("/images/head/");
        type = "jpg";
        savePath = savePath + getUser(session).getUsername()+"."+type;
        return AbstractNotesUtils.generateImage(imgData,savePath);
    }

    @ResponseBody
    @PostMapping("isHeadImg")
    public Boolean isHeadImg(HttpServletRequest request,String username){
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
    @PostMapping("updateUserPwd")
    public Boolean updateUserPwd(String pwd,Integer uid){
        if(pwd == null || pwd.isEmpty()) return false;
        return ut.updateUserPwd(pwd,uid);
    }

    @ResponseBody
    @PostMapping("updateMasterPwd")
    public Boolean updateMasterPwd(String pwd,HttpSession session){
        if(pwd == null || pwd.isEmpty()) return false;
        return ut.updatePWD(pwd,getUid(session),1);
    }

    @ResponseBody
    @PostMapping("updateNickName")
    public Boolean updateUserNickName(HttpSession session,String nickname){
        if(nickname == null || nickname.isEmpty()) return false;
        return ut.updateUserNickName(nickname,getUid(session));
    }

    @ResponseBody
    @PostMapping("delSimpleUser")
    public Boolean delUser(Integer uid){
        if(uid == null) return false;
        return ut.delUser(uid,0);
    }

    @ResponseBody
    @PostMapping("updateUserStatus")
    public Boolean changUserStatus(Integer status,Integer uid){
        if(uid == null) return false;
        return ut.updateUserStatus(uid,status,0);
    }

    @GetMapping("clearSession")
    public String clearSession(HttpSession session){
        session.invalidate();
        return "redirect:../index.html";
    }

    @ResponseBody
    @PostMapping("getMaster")
    public User getMaster(HttpSession session){
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
