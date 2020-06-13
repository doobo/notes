package com._5fu8.admin.controller;

import com._5fu8.admin.services.impl.AbstractServices;
import com._5fu8.admin.lucene.util.AbstractLuceneIndex;
import com._5fu8.admin.services.ManagerTransaction;
import com._5fu8.admin.services.UserTransaction;
import com._5fu8.admin.table.User;
import com._5fu8.admin.utils.AbstractNotesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@RequestMapping("/manager/")
@Controller
public class ManagerController {
    @Autowired
    ManagerTransaction mt;

    @Autowired
    UserTransaction ut;

    @ResponseBody
    @RequestMapping("/getSimpleNotes")
    public Object toNext(Integer curPage, Integer pageSize, HttpSession session) {
        return mt.getNotesMap(curPage,pageSize,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("/getSearchResult")
    public Object getSearchNotes(Integer curPage,Integer pageSize
            ,HttpSession session,Integer check,Integer share,Long startTime
    ,Long endTime,String wd){
        StringBuffer terms = AbstractServices.setHQLByParams(check,share,wd,
            startTime,endTime);
        return mt.getNotesByTerms(curPage,pageSize,getUserID(session)
                ,terms.toString(),startTime,endTime,
                AbstractServices.getLikeWd(wd));
    }

    @ResponseBody
    @RequestMapping("getSimpleChildType")
    public Object getSimpleChildType(Integer curPage, Integer pageSize, HttpSession session) {
        return mt.getSimpleChildType(curPage, pageSize, getUser(session).getId());
    }

    @ResponseBody
    @RequestMapping("getSearchChildType")
    public Object getSearchChildType(Integer curPage,Integer pageSize,HttpSession session
    ,String wd){
        if(wd != null && !wd.isEmpty()){
            wd = wd.replace("%","/%");
            wd = wd.replace("_","/_");
            wd = "%"+wd+"%";
        }
        return mt.getSearchChildType(curPage,pageSize,wd,getUserID(session));
    }

    @ResponseBody
    @GetMapping("getSearchInPicInfo")
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize
            ,HttpSession session,String wd){
        return mt.getSearchInPicInfo(curPage,pageSize,wd,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("getChildTypeByID")
    public Object getChildTypeByID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return mt.getChildTypeByID(cid,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("updateChildType")
    public Object updateChildType(Integer cid,String title,String desc,HttpSession session){
        if(cid == null || title == null && desc==null){
            return false;
        }
        return mt.updateChildType(cid,title,desc,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("getMainTypeByCID")
    public Object getMainTypeByCID(Integer cid,HttpSession session){
        if(cid == null) return null;
        return mt.getMainTypeByCID(cid,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("updateMainType")
    public Object updateMainType(Integer mid,String title,String desc,HttpSession session){
        if(mid == null || title == null && desc==null){
            return false;
        }
        return mt.updateMainType(mid,title,desc,getUserID(session));
    }


    @ResponseBody
    @RequestMapping("getSimpleUser")
    public Object getSimpleUser(HttpSession session) {
        User user = getUser(session);
        user.setPassword("");
        return user;
    }

    @ResponseBody
    @RequestMapping("getDelNotes")
    public Object getDelNotes(Integer curPage, Integer pageSize, HttpSession session) {
        return mt.getDelNotes(curPage, pageSize, getUser(session).getId());
    }

    @ResponseBody
    @RequestMapping("getDelNotesByWd")
    public Object getDelNotesByWd(Integer curPage, Integer pageSize
            , HttpSession session,String wd){
        if(wd != null && !wd.isEmpty()){
            wd = wd.replace("%","/%");
            wd = wd.replace("_","/_");
            wd = "%"+wd+"%";
        }
        return mt.getDelNotesSearch(curPage,pageSize,getUserID(session),wd);
    }

    @ResponseBody
    @RequestMapping("getIKText")
    public Object getIKLuceneCi(String text) {
        return mt.getIKLuceneCi(text);
    }

    @ResponseBody
    @RequestMapping("getNotesNoCheck")
    public Object getNotesNoCheck(Integer curPage, Integer pageSize, HttpSession session) {
        return mt.getNotesNoCheck(curPage, pageSize, getUser(session).getId());
    }

    @ResponseBody
    @RequestMapping("changeShare")
    public Object changeShare(Integer id, Integer share, HttpSession session) {
        return mt.changeShare(id, share, getUser(session).getId());
    }

    @ResponseBody
    @RequestMapping("changeStatus")
    public Object changeStatus(Integer id, Integer status, HttpSession session) {
        return mt.changeStatus(id, status, getUser(session).getId());
    }

    @RequestMapping("clearSession")
    public String clearSession(HttpSession session) {
        session.invalidate();
        return "redirect:../index.html";
    }

    @ResponseBody
    @RequestMapping("uploadHeadImg")
    public Object uploadHeadImg(String imgData, String type,
                                HttpServletRequest request, HttpSession session) {
        String savePath = request.getServletContext().getRealPath("/images/head/");
        type = "jpg";
        savePath = savePath + getUser(session).getUsername()+"."+type;
        return AbstractNotesUtils.generateImage(imgData,savePath);
    }

    @ResponseBody
    @RequestMapping("isHeadImg")
    public Object isHeadImg(HttpServletRequest request,HttpSession session){
        String savePath = request.getServletContext().getRealPath("/images/head/");
        savePath = savePath + getUser(session).getUsername()+"."+"jpg";
        if(new File(savePath).exists()){
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("updatePwd")
    public Object updateUserPwd(HttpSession session,String pwd){
        if(pwd == null || pwd.isEmpty()) return false;
        return ut.updateUserPwd(pwd,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("updateNickName")
    public Boolean updateUserNickName(HttpSession session,String nickname){
        if(nickname == null || nickname.isEmpty()) return false;
        return ut.updateUserNickName(nickname,getUserID(session));
    }

    @ResponseBody
    @RequestMapping("clearLuceneCache")
    public Object clearLuceneCache(){
        return AbstractLuceneIndex.clearCache();
    }


    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private int getUserID(HttpSession session){
        return getUser(session).getId();
    }
}
