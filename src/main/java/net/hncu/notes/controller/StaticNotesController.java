package net.hncu.notes.controller;

import net.hncu.notes.services.NotesTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by doobo@foxmail.com on 2017/3/28.
 */
@RequestMapping("/static/")
@Controller
public class StaticNotesController {

    @Autowired
    private NotesTransaction nt;

    @ResponseBody
    @RequestMapping("addMainType")
    public Object addMainType(String typeName,String description){
        System.out.println(typeName+description);
        return nt.addMainType(typeName.trim(),description.trim(),1);
    }

    @ResponseBody
    @RequestMapping("addChild")
    public Object addChildType(String typeName,String description,Integer mid){
        return  nt.addChildType(typeName,description,mid,1);
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
    @RequestMapping("addNote")
    public Object addNote(String title,String description ,Integer mid,
                          String simpleDesc,Integer chid,Integer share){
        return nt.addNote(title,description,mid,simpleDesc,chid,share,1);
    }

    @ResponseBody
    @RequestMapping("updateNote")
    public Object updateNote(String title,String description ,Integer mid,
                          String simpleDesc,Integer chid,Integer share,Integer nid){
        return nt.updateNote(title,description,mid,simpleDesc,chid,share,nid,1);
    }

    @ResponseBody
    @RequestMapping("getNoteByID")
    public Object getNoteByID(Integer id){
        if(id == null) return null;
        return nt.getNoteByStaticID(id);
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
    @RequestMapping("getNoteToUpdate")
    public Object getNoteToUpdate(Integer nid){
        return  nt.getNoteToUpdate(nid);
    }

}
