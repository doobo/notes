package net.hncu.notes.controller;

import net.hncu.notes.services.NotesTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by doobo@foxmail.com on 2017/4/17.
 */
@RequestMapping("/public/")
@Controller
public class PublicController {

    @Autowired
    private NotesTransaction nt;

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
}
