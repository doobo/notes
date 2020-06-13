package com._5fu8.admin.controller;

import com._5fu8.admin.services.ManagerTransaction;
import com._5fu8.admin.services.NotesTransaction;
import com._5fu8.admin.services.UserTransaction;
import com._5fu8.admin.table.User;
import com._5fu8.admin.utils.AbstractNotesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by doobo@foxmail.com on 2017/3/28.
 */
@RequestMapping("/static/")
@Controller
public class StaticNotesController {

    @Autowired
    private NotesTransaction nt;

    @Autowired
    private ManagerTransaction mt;

    @Autowired
    private UserTransaction ut;

    @ResponseBody
    @PostMapping("addMainType")
    public Object addMainType(String typeName,String description,HttpSession session){
        return nt.addMainType(typeName.trim(),description.trim(),getUid(session));
    }

    @ResponseBody
    @PostMapping("addChild")
    public Object addChildType(String typeName,String description,Integer mid,HttpSession session){
        return  nt.addChildType(typeName,description,mid,getUid(session));
    }

    @ResponseBody
    @PostMapping("getMainType")
    public Object getMainType(HttpSession session){
        System.out.println(ut.getRootId());
       return nt.getMainTypeByUid(getUid(session),ut.getRootId());
    }

    @ResponseBody
    @PostMapping("getChildType")
    public Object getChildTypeByMid(Integer mid,HttpSession session){
        if(mid == null) return false;
        return nt.getChildTypeWithMidByUid(mid,getUid(session),ut.getRootId());
    }

    @ResponseBody
    @PostMapping("addNote")
    public Object addNote(String title,String description ,Integer mid,
                          String simpleDesc,Integer chid,Integer share
            ,HttpSession session,String images){
        return nt.addNote(title,description,mid,simpleDesc
                ,chid,share,getUid(session),images,0);
    }

    @ResponseBody
    @PostMapping("updateNote")
    public Object updateNote(String title,String description ,Integer mid,HttpSession session,
                          String simpleDesc,Integer chid
            ,Integer share,Integer nid,String images){
        return mt.updateNote(title,description,mid,simpleDesc,
                chid,share,nid,getUid(session),images);
    }

    //文章图片异步上传
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
            return picName;
        }catch (Exception e){
            return false;
        }
    }

    @ResponseBody
    @PostMapping("getNoteByID")
    public Object getNoteByID(Integer id,HttpSession session){
        if(id == null) return null;
        return mt.getNoteByStaticID(id,getUid(session));
    }

    @ResponseBody
    @PostMapping("getNextNote")
    public Object getNextNote(Long time,Integer cid,Integer nid){
        return nt.getNextNoteByTime(time,nid,cid);
    }

    @ResponseBody
    @PostMapping("getPreNote")
    public Object getPreNote(Long time,Integer cid,Integer nid){
        return nt.getPreNoteByTime(time,nid,cid);
    }

    @ResponseBody
    @PostMapping("getNoteToUpdate")
    public Object getNoteToUpdate(Integer nid,HttpSession session){
        return  mt.getNoteToUpdate(nid,getUid(session));
    }

    private User getUser(HttpSession session){
        return (User) session.getAttribute("user");
    }

    private Integer getUid(HttpSession session){
        return ((User) session.getAttribute("user")).getId();
    }
}
